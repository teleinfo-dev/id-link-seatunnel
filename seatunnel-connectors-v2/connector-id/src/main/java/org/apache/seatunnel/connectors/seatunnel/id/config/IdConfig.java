/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.connectors.seatunnel.id.config;

import org.apache.seatunnel.api.configuration.Option;
import org.apache.seatunnel.api.configuration.Options;

/**
 * @author FreeSky
 * @version 1.0
 * @date Created in 2024/8/6
 * @description
 */
public class IdConfig {

    /** 插件名称 */
    public static final String CONNECTOR_ID = "ID";

    // 企业节点地址
    public static final Option<String> URL =
            Options.key("url").stringType().noDefaultValue().withDescription("IDLink request url");

    // 标识身份
    public static final Option<String> HANDLE_USER =
            Options.key("handle_user")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("IDLink request handle_user");
    // 标识私钥
    public static final Option<String> PRIVATE_KEY =
            Options.key("private_key")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("IDLink request private_key");

    // 元数据
    public static final Option<String> META_HANDLE =
            Options.key("meta_handle")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("IDLink request meta_handle");
    // 前缀
    public static final Option<String> PREFIX =
            Options.key("prefix")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("IDLink request prefix");

    // 编码映射字段a
    public static final Option<String> CODE_MAPPING =
            Options.key("code_mapping")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("IDLink request code_mapping");
}
