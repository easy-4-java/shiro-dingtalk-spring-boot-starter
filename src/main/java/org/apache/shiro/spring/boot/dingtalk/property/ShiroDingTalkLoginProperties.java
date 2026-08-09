package org.apache.shiro.spring.boot.dingtalk.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 	application：scan codeloginconfiguration
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@Getter
@Setter
@ToString
public class ShiroDingTalkLoginProperties {

	/**
	 * 	application-scan codeloginapplication appId
	 */
	private String appId;
	/**
	 * 	application-scan codeloginapplication appSecret
	 */
	private String appSecret;

}
