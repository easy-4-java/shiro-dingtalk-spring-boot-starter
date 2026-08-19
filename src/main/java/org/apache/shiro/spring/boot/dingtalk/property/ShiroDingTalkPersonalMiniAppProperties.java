package org.apache.shiro.spring.boot.dingtalk.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *	third-partyapplication：configuration
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class ShiroDingTalkPersonalMiniAppProperties {

	/**
	 * 	AppId：applicationAppId，AppIdapplication uniqueidentifier，retrieveloginuserauthorization access_token
	 */
	private String appId;
	/**
	 * 	AppSecret：applicationAppSecret，AppSecretretrieveloginuserauthorization access_token
	 */
	private String appSecret;

}
