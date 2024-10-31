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

package org.apache.seatunnel.connectors.seatunnel.id.client;

import org.apache.seatunnel.connectors.seatunnel.id.config.IdClientParameter;

import cn.teleinfo.idhub.sdk.client.OpenApiClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author FreeSky
 * @version 1.0
 * @date Created in 2024/8/6
 * @description
 */
public class OpenApiClientContext {

    private static final Map<String, OpenApiClient> OPEN_API_CLIENT_CONTEXT =
            new ConcurrentHashMap<>();

    /** 私有构造方法，防止外部通过new创建实例 */
    private OpenApiClientContext() {}

    /**
     * 获取OpenApiClient的实例
     *
     * @param idClientParameter 参数对象
     * @return OpenApiClient的实例
     */
    public static OpenApiClient getClient(IdClientParameter idClientParameter) {
        OpenApiClient openApiClient =
                OPEN_API_CLIENT_CONTEXT.get(idClientParameter.getHandleUser());
        if (openApiClient == null) {
            createClient(idClientParameter);
        }
        return OPEN_API_CLIENT_CONTEXT.get(idClientParameter.getHandleUser());
    }

    /**
     * 创建OpenApiClient
     *
     * @param idClientParameter
     */
    public static void createClient(IdClientParameter idClientParameter) {
        if (OPEN_API_CLIENT_CONTEXT.containsKey(idClientParameter.getHandleUser())) {
            return;
        }
        OpenApiClient openApiClient =
                new OpenApiClient(
                        idClientParameter.getUrl(),
                        idClientParameter.getHandleUser(),
                        idClientParameter.getPrivateKey());
        OPEN_API_CLIENT_CONTEXT.put(idClientParameter.getHandleUser(), openApiClient);
    }

    /**
     * 移除OpenApiClient
     *
     * @param idClientParameter
     */
    public static void removeClient(IdClientParameter idClientParameter) {
        OPEN_API_CLIENT_CONTEXT.remove(idClientParameter.getHandleUser());
    }
}
