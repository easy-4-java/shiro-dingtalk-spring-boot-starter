/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.shiro.spring.boot.dingtalk.authc;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.utils.WebUtils;
import org.apache.shiro.biz.web.filter.authc.AbstractTrustableAuthenticatingFilter;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.dingtalk.exception.DingTalkCodeNotFoundException;
import org.apache.shiro.spring.boot.dingtalk.token.DingTalkMaAuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * https://open.dingtalk.com/document/orgapp-client/mini-program-free-login
 */
@Slf4j
public class DingTalkMaAuthenticatingFilter extends AbstractTrustableAuthenticatingFilter {

	private static final Logger log = LoggerFactory.getLogger(DingTalkMaAuthenticatingFilter.class);

	public static final String SPRING_SECURITY_FORM_APP_KEY = "key";
	public static final String SPRING_SECURITY_FORM_TOKEN_KEY = "token";
	public static final String SPRING_SECURITY_FORM_CODE_KEY = "authCode";

	private String keyParameter = SPRING_SECURITY_FORM_APP_KEY;
	private String tokenParameter = SPRING_SECURITY_FORM_TOKEN_KEY;
	private String authCodeParameter = SPRING_SECURITY_FORM_CODE_KEY;

	public DingTalkMaAuthenticatingFilter() {
		super();
	}

	@Override
	/** Returns whether the access allowed is enabled.
	 * @param request the request
	 * @param response the response
	 * @param mappedValue the mappedValue
	 * @return the result
	 */
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// 判断是否无状态
		if (isSessionStateless()) {
			// Step 1、生成 Shiro Token
			AuthenticationToken token = createToken(request, response);
			try {
				//Step 2、委托给Realm进行登录
				Subject subject = getSubject(request, response);
				subject.login(token);
				//Step 3、执行授权成功后的函数
				return onAccessSuccess(token, subject, request, response);
			} catch (AuthenticationException e) {
				//Step 4、执行授权失败后的函数
				return onAccessFailure(token, e, request, response);
			}
		}
		return super.isAccessAllowed(request, response, mappedValue);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

		// 1、判断是否登录请求
		if (isLoginRequest(request, response)) {

			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				String mString = "Authentication url [" + getLoginUrl() + "] Not Http Post request.";
				if (log.isTraceEnabled()) {
					log.trace(mString);
				}

				WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

				// Response Authentication status information
				JSONObject.writeJSONString(response.getWriter(), AuthcResponse.fail(HttpStatus.SC_BAD_REQUEST, mString));

				return false;
			}
		}
		// 2、未授权情况
		else {

			String mString = "Attempting to access a path which requires authentication. ";
			if (log.isTraceEnabled()) {
				log.trace(mString);
			}

			// Ajax 请求：响应json数据对象
			if (WebUtils.isAjaxRequest(request)) {

				WebUtils.toHttp(response).setStatus(HttpStatus.SC_OK);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

				// Response Authentication status information
				JSONObject.writeJSONString(response.getWriter(), AuthcResponse.fail(HttpStatus.SC_UNAUTHORIZED, mString));

				return false;
			}
			// 普通请求：重定向到登录页
			saveRequestAndRedirectToLogin(request, response);
			return false;
		}
	}



	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		// Post && JSON
		if(WebUtils.isObjectRequest(request)) {

			if (log.isDebugEnabled()) {
				log.debug("Post && JSON");
			}

			try {

				DingTalkMaLoginRequest loginRequest = objectMapper.readValue(request.getReader(), DingTalkMaLoginRequest.class);

				if ( !StringUtils.hasText(loginRequest.getKey())) {
					log.debug("No key (appId or appKey) found in request.");
					throw new DingTalkCodeNotFoundException("No key (appId or appKey) found in request.");
				}
				if ( !StringUtils.hasText(loginRequest.getAuthCode())) {
					log.debug("No AuthCode found in request.");
					throw new DingTalkCodeNotFoundException("No AuthCode found in request.");
				}
				return new DingTalkMaAuthenticationToken(loginRequest, getHost(request));
			} catch (Exception e) {
				throw new AuthenticationException(e);
			}
		}


		/**
		 * 	application uniqueidentifierkey
		 */
		String appId = obtainKey(request);
		String token = obtainToken(request);
		String authCode = obtainAuthCode(request);

		if ( !StringUtils.hasText(appId)) {
			log.debug("No appId found in request.");
			throw new DingTalkCodeNotFoundException("No appId found in request.");
		}
		if ( !StringUtils.hasText(authCode)) {
			log.debug("No authCode found in request.");
			throw new DingTalkCodeNotFoundException("No authCode found in request.");
		}

		DingTalkMaLoginRequest loginRequest = new DingTalkMaLoginRequest(appId, token, authCode);

		return new DingTalkMaAuthenticationToken(loginRequest, getHost(request));
	}

	/** Extracts the key parameter from the HTTP request.
	 * @param request the request
	 * @return the result
	 */
	protected String obtainKey(ServletRequest request) {
        return request.getParameter(keyParameter);
    }

	/** Extracts the token parameter from the HTTP request.
	 * @param request the request
	 * @return the result
	 */
	protected String obtainToken(ServletRequest request) {
		return request.getParameter(tokenParameter);
	}

	/** Extracts the auth code parameter from the HTTP request.
	 * @param request the request
	 * @return the result
	 */
	protected String obtainAuthCode(ServletRequest request) {
		return request.getParameter(authCodeParameter);
	}

	/** Returns the key parameter.
	 * @return the result
	 */
	public String getKeyParameter() {
		return keyParameter;
	}

	/** Sets the key parameter.
	 * @param keyParameter the keyParameter
	 */
	public void setKeyParameter(String keyParameter) {
		this.keyParameter = keyParameter;
	}

	/** Sets the token parameter.
	 * @param tokenParameter the tokenParameter
	 */
	public void setTokenParameter(String tokenParameter) {
		this.tokenParameter = tokenParameter;
	}

	/** Returns the token parameter.
	 * @return the result
	 */
	public String getTokenParameter() {
		return tokenParameter;
	}

	/** Returns the auth code parameter.
	 * @return the result
	 */
	public String getAuthCodeParameter() {
		return authCodeParameter;
	}

	/** Sets the auth code parameter.
	 * @param authCodeParameter the authCodeParameter
	 */
	public void setAuthCodeParameter(String authCodeParameter) {
		this.authCodeParameter = authCodeParameter;
	}

}
