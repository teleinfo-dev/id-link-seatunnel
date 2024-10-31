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
import org.apache.seatunnel.api.serialization.SerializationSchema;
import org.apache.seatunnel.api.sink.SupportMultiTableSinkWriter;
import org.apache.seatunnel.api.table.type.RowKind;
import org.apache.seatunnel.api.table.type.SeaTunnelRow;
import org.apache.seatunnel.api.table.type.SeaTunnelRowType;
import org.apache.seatunnel.api.table.type.SqlType;
import org.apache.seatunnel.connectors.seatunnel.common.sink.AbstractSinkWriter;
import org.apache.seatunnel.connectors.seatunnel.id.client.OpenApiClientContext;
import org.apache.seatunnel.connectors.seatunnel.id.config.IdClientParameter;
import org.apache.seatunnel.connectors.seatunnel.id.config.IdConfig;
import org.apache.seatunnel.connectors.seatunnel.id.constant.ResponseCode;
import org.apache.seatunnel.format.json.JsonSerializationSchema;

import cn.hutool.core.date.DateUtil;
import cn.teleinfo.idhub.manage.doip.server.domain.DoipReturn;
import cn.teleinfo.idhub.manage.doip.server.dto.instance.HandleAttributesDTO;
import cn.teleinfo.idhub.manage.doip.server.dto.instance.HandleInputDTO;
import cn.teleinfo.idhub.manage.doip.server.enums.DoipClientCodeEnum;
import cn.teleinfo.idhub.manage.doip.server.enums.DoipOp;
import cn.teleinfo.idhub.sdk.client.OpenApiClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author FreeSky
 * @version 1.0
 * @date Created in 2024/8/6
 * @description
 */
@Slf4j
public class IdSinkWriter extends AbstractSinkWriter<SeaTunnelRow, Void>
        implements SupportMultiTableSinkWriter<Void> {

    private final ReadonlyConfig config;
    private final SeaTunnelRowType seaTunnelRowType;
    private final IdClientParameter idClientParameter;
    private final SerializationSchema serializationSchema;

    public IdSinkWriter(
            SeaTunnelRowType seaTunnelRowType,
            IdClientParameter idClientParameter,
            ReadonlyConfig config) {
        this(
                config,
                seaTunnelRowType,
                idClientParameter,
                new JsonSerializationSchema(seaTunnelRowType));
    }

    public IdSinkWriter(
            ReadonlyConfig config,
            SeaTunnelRowType seaTunnelRowType,
            IdClientParameter idClientParameter,
            SerializationSchema serializationSchema) {
        this.config = config;
        this.seaTunnelRowType = seaTunnelRowType;
        this.idClientParameter = idClientParameter;
        this.serializationSchema = serializationSchema;
        OpenApiClientContext.createClient(idClientParameter);
    }

    @Override
    public void write(SeaTunnelRow element) throws IOException {
        OpenApiClient idClient = OpenApiClientContext.getClient(idClientParameter);
        // 获取数据写入类别，Insert，Update_after， Delete
        RowKind rowKind = element.getRowKind();
        // 插入
        if (rowKind.equals(RowKind.INSERT)) {
            boolean result =
                    processData(element, idClient, DoipOp.CREATE.getName(), RowKind.INSERT.name());
            if (result) {
                return;
            }
        }
        // 修改
        if (rowKind.equals(RowKind.UPDATE_AFTER)) {
            boolean result =
                    processData(
                            element,
                            idClient,
                            DoipOp.UPDATE.getName(),
                            RowKind.UPDATE_AFTER.name());
            if (result) {
                return;
            }
        }
        // 删除
        if (rowKind.equals(RowKind.DELETE)) {
            boolean result =
                    processData(element, idClient, DoipOp.DELETE.getName(), RowKind.DELETE.name());
            if (result) {
                return;
            }
        }
    }

    /**
     * 处理数据
     *
     * @param element
     * @param idClient
     * @param operation
     * @param format
     * @return
     */
    private boolean processData(
            SeaTunnelRow element, OpenApiClient idClient, String operation, String format) {
        String metaHandle = config.get(IdConfig.META_HANDLE);
        String targetId = constructHandle(element, metaHandle);
        if (Objects.isNull(targetId)) {
            log.error("mapping field value is null, please check");
            return false;
        }
        if (operation.equals(DoipOp.UPDATE.getName())) {
            // 如果是更新的操作，则标识里一定已经存在了，解析标识，不存在的话，则证明编码映射字段被更改了
            DoipReturn doipReturn =
                    idClient.getIntanceApi().get(targetId, DoipOp.RETRIEVE.getName());
            if (Objects.equals(doipReturn.getCode(), DoipClientCodeEnum.DO_NOT_FOUND.getCode())
                    || Objects.equals(doipReturn.getCode(), ResponseCode.HANDLE_NOT_FOUND)) {
                String suffix = targetId.substring(metaHandle.length());
                log.error(
                        "please check the mapping field, do not change the mapping field value [{}]",
                        suffix);
                return false;
            }
        }
        HandleInputDTO handleInputDTO = buildHandleInputDTO(element);
        try {
            DoipReturn<Map<String, Object>> result =
                    idClient.getIntanceApi().post(targetId, operation, handleInputDTO);
            if (Objects.equals(result.getCode(), DoipClientCodeEnum.SUCCESS.getCode())) {
                return true;
            }
            log.error(
                    String.format(
                            "IDLink execute %s exception, response status code:[{}], content:[{}]",
                            format),
                    result.getCode(),
                    result.getMessage());
        } catch (Exception e) {
            log.error("IDLink execute exception", e);
        }
        return false;
    }

    /**
     * 组装标识
     *
     * @return
     */
    private String constructHandle(SeaTunnelRow element, String metaHandle) {
        String key = config.get(IdConfig.CODE_MAPPING);
        // 获取所有字段名称
        List<String> fields = Arrays.asList(seaTunnelRowType.getFieldNames());
        // 若填写的编码映射字段不是数据库字段，则返回null
        if (!fields.contains(key)) {
            return null;
        }
        String suffix = element.getField(fields.indexOf(key)).toString();
        return metaHandle + suffix;
    }

    /** */
    private HandleInputDTO buildHandleInputDTO(SeaTunnelRow element) {
        HandleInputDTO handleInputDTO = new HandleInputDTO();
        // 应用身份
        handleInputDTO.setAppHandle(config.get(IdConfig.HANDLE_USER));
        // 元数据
        handleInputDTO.setType(config.get(IdConfig.META_HANDLE));
        // 获取所有字段名称
        List<String> fields = Arrays.asList(seaTunnelRowType.getFieldNames());
        Map<String, Object> content = new HashMap<>();
        for (String field : fields) {
            // 时间类型按格式转换
            SqlType fieldType = seaTunnelRowType.getFieldType(fields.indexOf(field)).getSqlType();
            Object value = element.getField(fields.indexOf(field));
            content.put(field, value);
            // 转换datetime类型
            if (fieldType.equals(SqlType.TIMESTAMP)) {
                content.put(field, DateUtil.format((LocalDateTime) value, "yyyy-MM-dd HH:mm:ss"));
            }
        }
        HandleAttributesDTO handleAttributesDTO = new HandleAttributesDTO();
        handleAttributesDTO.setContent(content);
        // 数据body
        handleInputDTO.setAttributes(handleAttributesDTO);
        return handleInputDTO;
    }

    @Override
    public void close() throws IOException {
        OpenApiClientContext.removeClient(idClientParameter);
    }
}
