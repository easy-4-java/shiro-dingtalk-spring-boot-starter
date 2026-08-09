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
package org.apache.shiro.spring.boot;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.shiro.spring.boot.dingtalk.property.ShiroDingTalkCropAppProperties;
import org.apache.shiro.spring.boot.dingtalk.property.ShiroDingTalkLoginProperties;
import org.apache.shiro.spring.boot.dingtalk.property.ShiroDingTalkPersonalMiniAppProperties;
import org.apache.shiro.spring.boot.dingtalk.property.ShiroDingTalkSuiteProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
/** Configuration properties for Shiro Ding Talk.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */

@ConfigurationProperties(ShiroDingTalkProperties.PREFIX)
@Getter
@Setter
@ToString
public class ShiroDingTalkProperties {

	public static final String PREFIX = "shiro.dingtalk";
	
	/** Whether Enable DingTalk Authentication. */
	private boolean enabled = false;

	/**
	 * 	enterpriseID
	 */
	private String corpId;
	
	/**
	 *    enterpriseinternal：、H5configuration
	 */
	private List<ShiroDingTalkCropAppProperties> cropApps;
	/**
	 *    third-partyapplication：configuration
	 */
	private List<ShiroDingTalkPersonalMiniAppProperties> apps;
	/**
	 * 	third-partyenterpriseapplication：、H5configuration
	 */
	private List<ShiroDingTalkSuiteProperties> suites;
	/**
	 *	 application：scan codeloginconfiguration
	 */
	private List<ShiroDingTalkLoginProperties> logins;
	
}

