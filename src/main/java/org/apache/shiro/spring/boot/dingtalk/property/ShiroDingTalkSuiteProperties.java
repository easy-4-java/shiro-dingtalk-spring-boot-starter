package org.apache.shiro.spring.boot.dingtalk.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 	third-partyenterpriseapplication：、H5configuration
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@Getter
@Setter
@ToString
public class ShiroDingTalkSuiteProperties {

	/**
	 * 	third-partyenterpriseapplication：ID
	 */
	private String suiteId;
	/**
	 * 	third-partyenterpriseapplication：application uniqueidentifierkey
	 */
	private String appId;
	/**
	 * 	third-partyenterpriseapplication：application uniqueidentifierkey
	 */
	private String suiteKey;
	/**
	 *	third-partyenterpriseapplication：application
	 */
	private String suiteSecret;

}
