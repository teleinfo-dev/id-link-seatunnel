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

package org.apache.seatunnel.connectors.seatunnel.id.sink;

import org.apache.seatunnel.api.configuration.ReadonlyConfig;
import org.apache.seatunnel.api.configuration.util.OptionRule;
import org.apache.seatunnel.api.table.catalog.CatalogTable;
import org.apache.seatunnel.api.table.connector.TableSink;
import org.apache.seatunnel.api.table.factory.Factory;
import org.apache.seatunnel.api.table.factory.TableSinkFactory;
import org.apache.seatunnel.api.table.factory.TableSinkFactoryContext;
import org.apache.seatunnel.api.table.type.SeaTunnelRowType;
import org.apache.seatunnel.connectors.seatunnel.id.config.IdConfig;

import com.google.auto.service.AutoService;

/**
 * @author FreeSky
 * @version 1.0
 * @date Created in 2024/8/6
 * @description
 */
@AutoService(Factory.class)
public class IdSinkFactory implements TableSinkFactory {

    /**
     * 返回同一工厂接口之间的唯一标识符
     *
     * @return
     */
    @Override
    public String factoryIdentifier() {
        return IdConfig.CONNECTOR_ID;
    }

    /**
     * 验证任务配置的参数的是否符合规则
     *
     * @return
     */
    @Override
    public OptionRule optionRule() {
        return OptionRule.builder()
                .optional(IdConfig.URL)
                .optional(IdConfig.HANDLE_USER)
                .optional(IdConfig.PRIVATE_KEY)
                .build();
    }

    @Override
    public TableSink createSink(TableSinkFactoryContext context) {
        // 获取配置
        ReadonlyConfig readonlyConfig = context.getOptions();
        // 获取CatalogTable
        CatalogTable catalogTable = context.getCatalogTable();
        SeaTunnelRowType seaTunnelRowType = catalogTable.getSeaTunnelRowType();
        return () -> new IdSink(readonlyConfig, seaTunnelRowType);
    }
}
