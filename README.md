# 基于Spring Cloud 的微服务脚手架

## 主要功能

- 生成包
- 生成资源文件
- 生成`application-dev.properties`
- 引入依赖
- 生成实体类
- 生成实体聚合类
- 生成实体查询类
- 生成`controller`
- 生成`service`及其实现类
- 生成`mapper`及对应`xml`文件

## 提供接口

- 新增实体
- 修改实体
- 删除实体(逻辑删除)
- 按id查询实体
- 按条件查询实体
- 查询所有实体

## 如何使用

1. 新建maven项目
    - 要使用待生成项目的`groupId`和`artifactId`
    - `groupId`格式为`com.公司名.产品线名`
    - `artifactId`格式为`模块名-server`

2. 引入插件

    ```
    <plugin>
        <groupId>com.linlihouse</groupId>
        <artifactId>project-init</artifactId>
        <version>4.0-SNAPSHOT</version>
    </plugin>
    ```

3. 加载插件后执行:

    1. 执行 `project-init:init `

    2. 第一次执行后,需要修改配置文件`config.properties`

        ```properties
        
        #eureka服务的地址
        eureka.url=http://localhost:10000/eureka/
        #服务的端口号
        port=10002
        
        # 是否依赖redis组件
        need.redis=false
        #如果依赖redis 请填写正确的ip 如果不依赖,请保持原样,不要自以为是的删除
        redis.ip=192.168.1.3
        
        #数据库相关
        #url
        datasource.url=jdbc:mysql://localhost:3306/study?characterEncoding=utf8&allowMultiQueries=true&autoReconnect=true&useSSL=false
        #username
        datasource.username=yuanlei
        #password
        datasource.password=s1s2s3
        
        # 是否依赖mq组件
        need.mq=false
        #如果依赖mq 请填写正确的值 如果不依赖,请保持原样,不要自以为是的删除
        mq.ip=192.168.1.4
        mq.username=test
        mq.password=123456
        mq.host=test
        
        ```

        修改配置文件后需要再次执行`project-init:init`

    3. 执行`project-init:generat `

    4. 同样的,第一次执行会报错,并生成示例模板文件.

    5. 按照示例模板新建足够你用的模板文件.

    6. 再次运行`project-init:generat `