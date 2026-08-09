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
package org.apache.shiro.spring.boot.dingtalk.token;

import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import org.apache.shiro.biz.authc.token.DefaultAuthenticationToken;
import org.apache.shiro.spring.boot.dingtalk.authc.DingTalkScanCodeLoginRequest;

/**
 * DingTalk Authentication Token
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@SuppressWarnings("serial")
public class DingTalkScanCodeAuthenticationToken extends DefaultAuthenticationToken {

	/**
	 * loginrequestinformation
	 */
	private DingTalkScanCodeLoginRequest principal;
	/**
	 * third-partyplatformUnionID（third-partyuser uniqueID）
	 */
	protected String unionid;
	/**
	 * third-partyplatformOpenID（third-partyapplicationuser uniqueID）
	 */
	protected String openid;
	/**
	 * userinformation
	 */
	protected OapiSnsGetuserinfoBycodeResponse.UserInfo userInfo ;

	public DingTalkScanCodeAuthenticationToken(DingTalkScanCodeLoginRequest loginRequest, String host) {
		this.principal = loginRequest;
		this.setHost(host);
	}

	@Override
	/** Returns the principal.
	 * @return the result
	 */
	public Object getPrincipal() {
		return principal;
	}

	/** Returns the unionid.
	 * @return the result
	 */
	public String getUnionid() {
		return unionid;
	}

	/** Sets the unionid.
	 * @param unionid the unionid
	 */
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	/** Returns the openid.
	 * @return the result
	 */
	public String getOpenid() {
		return openid;
	}

	/** Sets the openid.
	 * @param openid the openid
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public OapiSnsGetuserinfoBycodeResponse.UserInfo getUserInfo() {
		return userInfo;
	}

	/** Sets the user info.
	 * @param userInfo the userInfo
	 */
	public void setUserInfo(OapiSnsGetuserinfoBycodeResponse.UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
