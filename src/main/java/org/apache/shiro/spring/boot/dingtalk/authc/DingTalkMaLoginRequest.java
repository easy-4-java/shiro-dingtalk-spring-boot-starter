package org.apache.shiro.spring.boot.dingtalk.authc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/** Login request DTO for Ding Talk Ma authentication.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DingTalkMaLoginRequest {
	/**
	 * application uniqueidentifierkey
	 */
	protected String corpId;
	/**
	 * application uniqueidentifierkey
	 */
	protected String key;
	/**
	 * temporarylogincredentialcode
	 */
	protected String authCode;
	/**
	 * 	request token，used forbindinguser
	 */
	protected String token;
	/**
	 * Access Token
	 */
	protected String accessToken;

    /**
     * Constructs a new ding talk ma login request instance.
     *
     */
	@JsonIgnoreProperties(ignoreUnknown = true)
    @JsonCreator
    public DingTalkMaLoginRequest(@JsonProperty("key") String key,
								  @JsonProperty("token") String token,
								  @JsonProperty("authCode") String authCode) {
        this.key = key;
		this.token = token;
        this.authCode = authCode;
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

	/** Returns the auth code.
	 * @return the result
	 */
	public String getAuthCode() {
		return authCode;
	}

	/** Sets the auth code.
	 * @param authCode the authCode
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	/** Returns the access token.
	 * @return the result
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/** Sets the access token.
	 * @param accessToken the accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
