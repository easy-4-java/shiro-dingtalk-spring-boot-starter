package org.apache.shiro.spring.boot.dingtalk.authc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enterprise internal application free-login、Third-party enterprise application free-login、Application management backend free-login
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DingTalkTmpCodeLoginRequest {

	/**
	 * 	application uniqueidentifierkey
	 */
	protected String key;
	/**
	 * temporarylogincredentialcode
	 */
	protected String code;
	/**
	 * 	request token，used forbindinguser
	 */
	protected String token;
	/**
	 * Access Token
	 */
	protected String accessToken;

    /**
     * Constructs a new ding talk tmp code login request instance.
     *
     */
	@JsonIgnoreProperties(ignoreUnknown = true)
    @JsonCreator
    public DingTalkTmpCodeLoginRequest(@JsonProperty("key") String key,
									   @JsonProperty("token") String token,
									   @JsonProperty("code") String code) {
        this.key = key;
		this.token = token;
		this.code = code;
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

	/** Returns the code.
	 * @return the result
	 */
	public String getCode() {
		return code;
	}

	/** Sets the code.
	 * @param code the code
	 */
	public void setCode(String code) {
		this.code = code;
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
