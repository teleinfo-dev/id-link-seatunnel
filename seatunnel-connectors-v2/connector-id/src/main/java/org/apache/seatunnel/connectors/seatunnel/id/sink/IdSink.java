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
import org.apache.seatunnel.api.sink.SinkWriter;
import org.apache.seatunnel.api.sink.SupportMultiTableSink;
import org.apache.seatunnel.api.table.type.SeaTunnelRow;
import org.apache.seatunnel.api.table.type.SeaTunnelRowType;
import org.apache.seatunnel.connectors.seatunnel.common.sink.AbstractSimpleSink;
import org.apache.seatunnel.connectors.seatunnel.common.sink.AbstractSinkWriter;
import org.apache.seatunnel.connectors.seatunnel.id.config.IdClientParameter;
import org.apache.seatunnel.connectors.seatunnel.id.config.IdConfig;

import java.io.IOException;

/**
 * @author FreeSky
 * @version 1.0
 * @date Created in 2024/8/6
 * @description
 */
public class IdSink extends AbstractSimpleSink<SeaTunnelRow, Void>
        implements SupportMultiTableSink {

    private final ReadonlyConfig config;

    private final SeaTunnelRowType seaTunnelRowType;

    private final IdClientParameter idClientParameter = new IdClientParameter();

    public IdSink(ReadonlyConfig config, SeaTunnelRowType rowType) {
        this.config = config;
        // 获取IdClient配置
        idClientParameter.setUrl(config.get(IdConfig.URL));
        idClientParameter.setHandleUser(config.get(IdConfig.HANDLE_USER));
        idClientParameter.setPrivateKey(config.get(IdConfig.PRIVATE_KEY));
        this.seaTunnelRowType = rowType;
    }

    /**
     * 获取插件名称
     *
     * @return
     */
    @Override
    public String getPluginName() {
        return IdConfig.CONNECTOR_ID;
    }

    @Override
    public AbstractSinkWriter<SeaTunnelRow, Void> createWriter(SinkWriter.Context context)
            throws IOException {
        return new IdSinkWriter(seaTunnelRowType, idClientParameter, config);
    }
}
