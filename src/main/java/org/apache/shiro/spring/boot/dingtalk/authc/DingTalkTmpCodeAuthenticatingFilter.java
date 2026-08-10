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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.biz.authc.AuthcResponse;
import org.apache.shiro.biz.utils.WebUtils2;
import org.apache.shiro.web.util.WebUtils;
import org.apache.shiro.biz.web.filter.authc.AbstractTrustableAuthenticatingFilter;
import org.apache.shiro.biz.web.servlet.http.HttpStatus;
import org.apache.shiro.spring.boot.dingtalk.exception.DingTalkCodeNotFoundException;
import org.apache.shiro.spring.boot.dingtalk.token.DingTalkTmpCodeAuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.nio.charset.StandardCharsets;

/**
 *  Enterprise internal application free-login：https://open.dingtalk.com/document/orgapp-server/enterprise-internal-application-logon-free
 *  Third-party enterprise application free-login： https://open.dingtalk.com/document/orgapp-server/third-party-enterprise-application-logon-free
 *  Application management backend free-login: https://open.dingtalk.com/document/orgapp-server/log-on-site-application-management-backend
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */

public class DingTalkTmpCodeAuthenticatingFilter extends AbstractTrustableAuthenticatingFilter {

	private static final Logger log = LoggerFactory.getLogger(DingTalkTmpCodeAuthenticatingFilter.class);


	public static final String SPRING_SECURITY_FORM_APP_KEY = "key";
	public static final String SPRING_SECURITY_FORM_TOKEN_KEY = "token";
	public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";

	private String keyParameter = SPRING_SECURITY_FORM_APP_KEY;
	private String tokenParameter = SPRING_SECURITY_FORM_TOKEN_KEY;
	private String codeParameter = SPRING_SECURITY_FORM_CODE_KEY;

	public DingTalkTmpCodeAuthenticatingFilter() {
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
			if (WebUtils2.isAjaxRequest(request)) {

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
		if(WebUtils2.isObjectRequest(request)) {

			if (log.isDebugEnabled()) {
				log.debug("Post && JSON");
			}

			try {
				DingTalkTmpCodeLoginRequest loginRequest = objectMapper.readValue(request.getReader(), DingTalkTmpCodeLoginRequest.class);

				if ( !StringUtils.hasText(loginRequest.getKey())) {
					log.debug("No key (appId or appKey) found in request.");
					throw new DingTalkCodeNotFoundException("No key (appId or appKey) found in request.");
				}
				if ( !StringUtils.hasText(loginRequest.getCode())) {
					log.debug("No Code found in request.");
					throw new DingTalkCodeNotFoundException("No loginTmpCode or Code found in request.");
				}
				return new DingTalkTmpCodeAuthenticationToken(loginRequest, getHost(request));
			} catch (Exception e) {
				throw new AuthenticationException(e);
			}
		}

		/**
		 * 	application uniqueidentifierkey
		 */
		String appId = obtainKey(request);
		String token = obtainToken(request);
		String code = obtainCode(request);

		if ( !StringUtils.hasText(appId)) {
			log.debug("No key (appId or appKey) found in request.");
			throw new DingTalkCodeNotFoundException("No appId found in request.");
		}
		if ( !StringUtils.hasText(code)) {
			log.debug("No Code found in request.");
			throw new DingTalkCodeNotFoundException("No Code found in request.");
		}

		DingTalkTmpCodeLoginRequest loginRequest = new DingTalkTmpCodeLoginRequest(appId, token, code);

		return new DingTalkTmpCodeAuthenticationToken(loginRequest, getHost(request));
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

	/** Extracts the code parameter from the HTTP request.
	 * @param request the request
	 * @return the result
	 */
	protected String obtainCode(ServletRequest request) {
		return request.getParameter(codeParameter);
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

	/** Returns the code parameter.
	 * @return the result
	 */
	public String getCodeParameter() {
		return codeParameter;
	}

	/** Sets the code parameter.
	 * @param codeParameter the codeParameter
	 */
	public void setCodeParameter(String codeParameter) {
		this.codeParameter = codeParameter;
	}

}
