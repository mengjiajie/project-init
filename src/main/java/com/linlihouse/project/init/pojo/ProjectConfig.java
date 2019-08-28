package com.linlihouse.project.init.pojo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: ZHJJ
 * Date: 2018/5/5
 * Time: 8:08
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ProjectConfig {
    private String groupId;
    private String artifactId;
    private String name;
    private String artifactIdPackageName;
    private String basePackageName;
    private String basePackagePath;
    private String projectAbsolutePath;
    private String basePackageAbsolutePath;
    private String configPackageAbsolutePath;
    private String configPackageName;
    private String daoPackageAbsolutePath;
    private String daoPackageName;
    private String servicePackageAbsolutePath;
    private String servicePackageName;
    private String serviceImplPackageAbsolutePath;
    private String serviceImplPackageName;
    private String apiPackageAbsolutePath;
    private String apiPackageName;
    private String utilPackageAbsolutePath;
    private String utilPackageName;
    private String pojoPackageAbsolutePath;
    private String pojoPackageName;
    private String resourcePath;
    private String resourceMapperPath;
    private boolean needMq;
    private boolean needRedis;
    private String mqPackageAbsolutePath;
    private String mqPackageName;
    private String converterPackageAbsolutePath;
    private String converterPackageName;
    private String handlerPackageAbsolutePath;
    private String handlerPackageName;
    private String mqImplPackageAbsolutePath;
    private String mqImplPackageName;
    private String mapperXmlPath;
    private String templatePath;

    private String eurekaUrl;
    private String port;
    private String redisIp;
    private String datasourceUrl;
    private String datasourceUsername;
    private String datasourcePassword;
    private String mqIp;
    private String mqUsername;
    private String mqPassword;
    private String mqHost;



    public ProjectConfig(String groupId,String artifactId,String projectAbsolutePath,boolean needMq,boolean needRedis) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.name = artifactId;
        this.projectAbsolutePath = projectAbsolutePath;
        this.artifactIdPackageName = artifactId.replaceAll("-",".");
        this.basePackageName = this.groupId + "." + this.artifactIdPackageName;
        this.basePackagePath = this.basePackageName.replaceAll("\\.","\\/");
        this.basePackageAbsolutePath = this.projectAbsolutePath + "/src/main/java/" + this.basePackagePath ;
        this.configPackageAbsolutePath = this.basePackageAbsolutePath + "/config";
        this.configPackageName = this.basePackageName + ".config";
        this.daoPackageAbsolutePath = this.basePackageAbsolutePath + "/dao";
        this.daoPackageName = this.basePackageName + ".dao";
        this.servicePackageAbsolutePath = this.basePackageAbsolutePath + "/service";
        this.servicePackageName = this.basePackageName + ".service";
        this.serviceImplPackageAbsolutePath = this.servicePackageAbsolutePath + "/impl";
        this.serviceImplPackageName = this.servicePackageName + ".impl";
        this.apiPackageAbsolutePath = this.basePackageAbsolutePath + "/api";
        this.apiPackageName = this.basePackageName + ".api";
        this.utilPackageAbsolutePath = this.basePackageAbsolutePath + "/util";
        this.utilPackageName = this.basePackageName + ".util";
        this.pojoPackageAbsolutePath = this.basePackageAbsolutePath + "/pojo";
        this.pojoPackageName = this.basePackageName + ".pojo";
        this.resourcePath = this.projectAbsolutePath + "/src/main/resources";
        this.resourceMapperPath = this.resourcePath + "/" + this.basePackagePath + "/mapper";
        this.mapperXmlPath = this.basePackagePath + "/mapper/*.xml";
        this.needMq = needMq;
        this.needRedis = needRedis;
        if(this.needMq){
            this.mqPackageAbsolutePath = this.basePackageAbsolutePath + "/mq";
            this.mqPackageName = this.basePackageName + ".mq";
            this.converterPackageAbsolutePath = this.mqPackageAbsolutePath + "/converter";
            this.converterPackageName = this.mqPackageName + ".converter";
            this.handlerPackageAbsolutePath = this.mqPackageAbsolutePath + "/handler";
            this.handlerPackageName = this.mqPackageName + ".handler";
            this.mqImplPackageAbsolutePath = this.mqPackageAbsolutePath + "/impl";
            this.mqImplPackageName = this.mqPackageName + ".impl";
        }
    }


}
