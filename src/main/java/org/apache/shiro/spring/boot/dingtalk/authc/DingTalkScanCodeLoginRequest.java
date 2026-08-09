package org.apache.shiro.spring.boot.dingtalk.authc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DingTalk scan-code login authorization for third-party systems
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingTalkScanCodeLoginRequest {

	/**
	 * 	application uniqueidentifierkey
	 */
	protected String key;
	/**
	 * 	request token，used forbindinguser
	 */
	protected String token;
	/**
	 * temporarylogincredentialcode
	 */
	protected String loginTmpCode;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonCreator
	public DingTalkScanCodeLoginRequest(@JsonProperty("key") String key,
										@JsonProperty("token") String token,
									    @JsonProperty("loginTmpCode") String loginTmpCode) {
		this.key = key;
		this.token = token;
		this.loginTmpCode = loginTmpCode;
	}

	/** Returns the key.
	 * @return the result
	 */
	public String getKey() {
		return key;
	}

	/** Sets the key.
	 * @param key the key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/** Returns the token.
	 * @return the result
	 */
	public String getToken() {
		return token;
	}

	/** Sets the token.
	 * @param token the token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/** Returns the login tmp code.
	 * @return the result
	 */
	public String getLoginTmpCode() {
		return loginTmpCode;
	}

	/** Sets the login tmp code.
	 * @param loginTmpCode the loginTmpCode
	 */
	public void setLoginTmpCode(String loginTmpCode) {
		this.loginTmpCode = loginTmpCode;
	}

}
