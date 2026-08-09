package org.apache.shiro.spring.boot.dingtalk.property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *	 enterpriseinternal：、H5configuration
 * @author [@Loong Wan](https://github.com/loong10k)
 */
@Getter
@Setter
@ToString
public class ShiroDingTalkCropAppProperties {

	/**
	 * 	enterpriseinternal：ID
	 */
	private String agentId;
	/**
	 * 	enterpriseinternal：application uniqueidentifierkey
	 */
	private String appKey;
	/**
	 * 	enterpriseinternal：application
	 */
	private String appSecret;

}
