# 概述
SeaTunnel（[SeaTunnel官网](https://seatunnel.apache.org/zh-CN/)）是一个非常易用、超高性能的分布式数据集成平台，支持实时海量数据同步。 每天可稳定高效同步数百亿数据，已被近百家企业应用于生产。

本项目基于SeaTunnel开发了基于标识解析的IDSink连接器，支持将不同数据源的数据写入到标识解析体系中。

IDSink连接器请关注`seatunnel-connectors-v2/connector-id`模块。

# IDSink连接器说明
## ID
>标识解析数据连接器
## 支持引擎
Spark
Flink
SeaTunnel Zeta
## 主要特性
❏ exactly-once
❏ cdc
## 描述（Description）
一个连接企业节点导入标识数据Sink插件。

## 支持的企业节点版本
企业节点标准版：1.0
企业节点商业版：2.1
企业节点SaaS版：1.0

## 连接器选项（Sink Options）
|             名称              |   类型   | 是否必须 |  默认值  |                             描述                       |
|-----------------------------|--------|------|-------|------------------------------------------------------|
| url                         | String | 是    | -     | 企业节点服务地址，例如http://127.0.0.1:3100                                  |
| prefix                     | String    | 是    | -     | 企业前缀，例如88.111.1                                      |
| meta_handle                 | String | 是    | -     | 元数据模版标识编码，例如88.111.1/meta_1b2f898f72                                  |
| handle_user                 | String | 是    | -     | 应用标识身份，例如88.608.24061301/App_SeaTunnel                                  |
| private_key                 | String | 是    | -     | 应用标识身份私钥，pem格式                                      |
| code_mapping                 | String | 是    | -     | 标识编码映射字段，示例（Example）name                                    |



## 示例（Example）
```json
sink {
      ID {
            # 当前插件正在处理与该参数对应的数据集
            source_table_name = "test-source-1"
            # 企业节点地址
            url = "https://idhub-manage-a-qa.idx.space/"
            # 前缀
            prefix = "88.608.24061301"
            # 元数据
            meta_handle = "88.608.24061301/META_702f1f140f"
            # 标识身份
            handle_user = "88.608.24061301/App_SeaTunnel"
            # 标识身份私钥
            private_key = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCun4o40UhrqU1uWOrvFQdqyUmQ\nSCFaVqLDBcPtrLL3h0eaXRpyZVhy1PZ99Raz7b8LwrLi5BATYV/RkqjcQVwsuJaZ7/5A7oc/523E\nHmqeU97zpcE7mJ65WRmiUFTIM3Kl3et90Bz9gA7XcF1Hpx15NCaeNRRSrulkcGxYTL0RQaTk5hdo\njxWXgWA7Hu9K9x6B/cxwg2OCUB8ZIRLHrxS4uif4OQTmApvqrn4a44DnJSLv6gzzG3PCPFrsg5gT\n5KwIKXj/rVNUW6DU7/6fOzzy19L/xP2Am8TaxCu/5ORFDJJWmHTCRRDjg1iX7R71KRAUrvEFm3GR\nhFOSOHtsDBhHAgMBAAECggEARFjXbeOkF9d9IgqVcZxmecptC5bfzw8Hx5NTG4yzw6RkRyvXtRyr\noM5v597s0uHkG8NJ5vpivGMpcMqZYxQF8s8QyxGfD2UM9NstIEzTaELlibG/zwauYVdZbClK9c0F\nO5x/lo/FOQdNR8+IAwqKw6zAzL1zxeUGHrTOA0WN3tmNtF/j66lgI4Ia7Z+k6lKpuDnP1dk/jOuy\nOvQHIDaiVudzusSOkCrpAgrvS2KOOan5V4uLW/s5nWdQffV6T25dg7PLw1n9boeqeAlqsDTa4hRb\nR3kGdss2azuvdqNdKTXHHusl01ZyNDRCOWtqZOUicVqXclJuB7rmmfZRJ4IkAQKBgQD62eYcY2CE\nv/MU/OyyZAwskjw3b/kPmeUfMOHNIU7cCtI5JeVj/sHw0ApCZg4P24eu0McTi8pZsyu0YkGlMg2h\nOlrxNLG9Tk8VDb8kJteEBvRUSv5uKw+2LB8ovgNon6cRawqLD3vxN8X4Xc0d41DQ00c8fLG5LB8Z\nboeL5FKiAQKBgQCyNRmgYqH8409a3VLRToP9X/eiPTf3xQ5SoDzvmVMDHuIMXEyHhEPcOgeSa6GP\nEDlQI1v92XarxbOv0aAwNf7flt+DD5QdRNzmo9o1cVutb1q2vwg6zeunn4DyVlqu/PFU67s3Ft+f\nnAbD8Q3fRiASFKJKXf/sK9xYsy0WUI0qRwKBgDL84Wn5xR+WiIlGk7H65gKVQsod52kFZe3+GJeC\nYq4VQP++tS6rFK6nCr7OUaNlguHErAJMErhC0+pZYnB6iQyxeNS4WjxZU3e7EJP/lSTP6Q/NIoQw\nGoRz2W2FmdnFQccnk8AKSK4SIRpHrq6Zis63cy5XRiSWVEfuEH0dzrIBAoGAZPVAN2SpcKM2abvJ\nF1rr27dqizczZeL1Ykn1O6gZJ0NvvKDtmjiMWU9mysS/GlWOop/llcKFQHsYeRrBLY7pfbHznkRN\nHOfOXbul+3DlBzR2p+FBwE64mpX6b1AMv+X8sqd5wJPszHeQPaFqwnrv4E5gQtFOE3YltvqLKIEp\n+DECgYEA9VEsDl9qxj1FiowUUNy2HpMDL4Qj/WER1NRWbTV71KycAGayAuwxUgFg+Pu3uM/l9fnV\nDq0i30+ruIlqhsdUVJpG7eoSQf+eYB1HzU99ANsOXIsIeR5bWMU9i8oRPB8sUbz32O+x44TfjAAu\nEjHhJ8F+kO2C1DsNCwDe4upEaqk=\n-----END PRIVATE KEY-----"
            # 编码映射字段
            code_mapping = "name"
        }
}
``` 

# 使用方式

## 源码构建
第一步：从github下载源码

第二步：编译构建源码
```shell
cd seatunnel
sh ./mvnw clean install -DskipTests -Dskip.spotless=true
```

第三步：获取构建好的二进制包
```shell
cp seatunnel-dist/target/apache-seatunnel-x.x.x-bin.tar.gz /The-Path-You-Want-To-Copy
```
第四步：在目标目录下解压二进制包
```shell
tar -zxvf apache-seatunnel-x.x.x-bin.tar.gz
```
第五步：进入apache-seatunnel-x.x.x/connectors，检查是否存在connector-id连接器jar包
```shell
cd apache-seatunnel-x.x.x/connectors

# 检查connector-id
ls | grep connector-id
```
第六步：进入apache-seatunnel-x.x.x/lib，检查是否存在所需数据源驱动程序包，不存在的需手动上传

第七步：编写任务脚本文件，Source任务脚本可参考[SeaTunnel官方文档](https://seatunnel.apache.org/zh-CN/docs/2.3.8/connector-v2/source)，IDSink文档参考，以下为一完整示例
```json
env {
    parallelism = 1
    job.mode = "BATCH"
}
# 源连接器
source {
    Jdbc {
        # 插件处理的数据将被注册为其他插件可以直接访问的数据集
        result_table_name = "test-source"
        # 数据库连接信息
        url = "jdbc:mysql://127.0.0.1:3306/source_db?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&tinyInt1isBit=false"
        driver = "com.mysql.cj.jdbc.Driver"
        user = "root"
        password = "*********"
        fetch_size = 1000
        table_list = [
            {
                table_path = "source_db.source_table_10w"
                query = "select id, name, age, gender, created_at from source_db.source_table"
            }
        ]
    }
}

# 转换连接器
transform {
    FieldMapper {
        source_table_name = "test-source"
        result_table_name = "test-source-1"
        field_mapper = {
            name = name
            age = age
            gender = gender
            created_at = create_at
        }
    }
}

sink {
    ID {
        # 当前插件正在处理与该参数对应的数据集
        source_table_name = "test-source-1"
        # 企业节点地址
        url = "https://idhub-manage-a-qa.idx.space/"
        # 前缀
        prefix = "88.608.24061301"
        # 元数据
        meta_handle = "88.608.24061301/META_702f1f140f"
        # 标识身份
        handle_user = "88.608.24061301/App_SeaTunnel"
        # 标识身份私钥
        private_key = "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCun4o40UhrqU1uWOrvFQdqyUmQ\nSCFaVqLDBcPtrLL3h0eaXRpyZVhy1PZ99Raz7b8LwrLi5BATYV/RkqjcQVwsuJaZ7/5A7oc/523E\nHmqeU97zpcE7mJ65WRmiUFTIM3Kl3et90Bz9gA7XcF1Hpx15NCaeNRRSrulkcGxYTL0RQaTk5hdo\njxWXgWA7Hu9K9x6B/cxwg2OCUB8ZIRLHrxS4uif4OQTmApvqrn4a44DnJSLv6gzzG3PCPFrsg5gT\n5KwIKXj/rVNUW6DU7/6fOzzy19L/xP2Am8TaxCu/5ORFDJJWmHTCRRDjg1iX7R71KRAUrvEFm3GR\nhFOSOHtsDBhHAgMBAAECggEARFjXbeOkF9d9IgqVcZxmecptC5bfzw8Hx5NTG4yzw6RkRyvXtRyr\noM5v597s0uHkG8NJ5vpivGMpcMqZYxQF8s8QyxGfD2UM9NstIEzTaELlibG/zwauYVdZbClK9c0F\nO5x/lo/FOQdNR8+IAwqKw6zAzL1zxeUGHrTOA0WN3tmNtF/j66lgI4Ia7Z+k6lKpuDnP1dk/jOuy\nOvQHIDaiVudzusSOkCrpAgrvS2KOOan5V4uLW/s5nWdQffV6T25dg7PLw1n9boeqeAlqsDTa4hRb\nR3kGdss2azuvdqNdKTXHHusl01ZyNDRCOWtqZOUicVqXclJuB7rmmfZRJ4IkAQKBgQD62eYcY2CE\nv/MU/OyyZAwskjw3b/kPmeUfMOHNIU7cCtI5JeVj/sHw0ApCZg4P24eu0McTi8pZsyu0YkGlMg2h\nOlrxNLG9Tk8VDb8kJteEBvRUSv5uKw+2LB8ovgNon6cRawqLD3vxN8X4Xc0d41DQ00c8fLG5LB8Z\nboeL5FKiAQKBgQCyNRmgYqH8409a3VLRToP9X/eiPTf3xQ5SoDzvmVMDHuIMXEyHhEPcOgeSa6GP\nEDlQI1v92XarxbOv0aAwNf7flt+DD5QdRNzmo9o1cVutb1q2vwg6zeunn4DyVlqu/PFU67s3Ft+f\nnAbD8Q3fRiASFKJKXf/sK9xYsy0WUI0qRwKBgDL84Wn5xR+WiIlGk7H65gKVQsod52kFZe3+GJeC\nYq4VQP++tS6rFK6nCr7OUaNlguHErAJMErhC0+pZYnB6iQyxeNS4WjxZU3e7EJP/lSTP6Q/NIoQw\nGoRz2W2FmdnFQccnk8AKSK4SIRpHrq6Zis63cy5XRiSWVEfuEH0dzrIBAoGAZPVAN2SpcKM2abvJ\nF1rr27dqizczZeL1Ykn1O6gZJ0NvvKDtmjiMWU9mysS/GlWOop/llcKFQHsYeRrBLY7pfbHznkRN\nHOfOXbul+3DlBzR2p+FBwE64mpX6b1AMv+X8sqd5wJPszHeQPaFqwnrv4E5gQtFOE3YltvqLKIEp\n+DECgYEA9VEsDl9qxj1FiowUUNy2HpMDL4Qj/WER1NRWbTV71KycAGayAuwxUgFg+Pu3uM/l9fnV\nDq0i30+ruIlqhsdUVJpG7eoSQf+eYB1HzU99ANsOXIsIeR5bWMU9i8oRPB8sUbz32O+x44TfjAAu\nEjHhJ8F+kO2C1DsNCwDe4upEaqk=\n-----END PRIVATE KEY-----"
        # 编码映射字段
        code_mapping = "name"
    }
}
```
第八步：执行脚本任务
```shell
# 进入解压目录
cd apache-seatunnel-x.x.x

# 执行任务
./bin/seatunnel.sh --config ./config/mysql-to-id.conf -m local
```

## 使用docker运行
第一步：按照源码构建方式前三步，编译构建二进制包

第二步：构建镜像
```shell
# 进入dist目录
cd seatunnel-dist

# 构建镜像
docker build -f src/main/docker/Dockerfile --build-arg VERSION=2.3.9-SNAPSHOT -t apache/seatunnel:2.3.9-SNAPSHOT .

# 示例
docker build -f src/main/docker/Dockerfile --build-arg VERSION=2.3.9-SNAPSHOT -t apache/seatunnel:2.3.9-SNAPSHOT .
```
第三步：查看镜像是否构建完成
```shell
# 查看镜像
docker images | grep apache/seatunnel
```
第四步：参考第一种方式第七步编写任务脚本

第五步：使用docker运行任务脚本
```shell
# 指定任务脚本文件运行任务
docker run --rm -it -v /<The-Config-Directory-To-Mount>/:/config apache/seatunnel:<version_tag> ./bin/seatunnel.sh -m local -c /config/fake_to_console.conf


# 示例
docker run --rm -it -v /seatunnel-examples/examples/:/config apache/seatunnel:2.3.9-SNAPSHOT ./bin/seatunnel.sh -m local -c /config/mysql-to-id.conf

```