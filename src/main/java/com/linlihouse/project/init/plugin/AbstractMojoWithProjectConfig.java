package com.linlihouse.project.init.plugin;

import com.linlihouse.project.init.pojo.ProjectConfig;
import com.linlihouse.project.init.util.FileGenerater;
import com.linlihouse.project.init.util.PropertiesUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractMojoWithProjectConfig extends AbstractMojo {

    private ProjectConfig projectConfig;

    public ProjectConfig getProjectConfig() {
        return projectConfig;
    }

    public void setProjectConfig(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }

    public void init() {
        MavenProject projectInfo = (MavenProject) getPluginContext().get("project");
        String projectArtifactId = projectInfo.getArtifactId();
        String projectGroupId = projectInfo.getGroupId();
        getLog().info("projectArtifactId->" + projectArtifactId + ",projectGroupId->" + projectGroupId);
        if (projectGroupId.contains("-")) {
            throw new RuntimeException("请以包名作为项目的groupId");
        }

        String projectAbsolutePath = projectInfo.getBasedir().getAbsolutePath();
        ProjectConfig projectConfig = null;
        Properties properties = new Properties();
        try {
            File configFile = new File(projectAbsolutePath + "/config.properties");
            if (!configFile.exists()) {
                String targetPath = projectAbsolutePath + "/config.properties";
                FileGenerater fileGenerater = new FileGenerater();
                String configPropertiesTemplatePath = "template/config-properties.template";
                fileGenerater.generate(configPropertiesTemplatePath, targetPath);
                throw new RuntimeException("配置文件不存在,已经自动生成,但需要填入相关的值!");
            }
            properties.load(new FileInputStream(configFile));
            boolean needMq = PropertiesUtil.getBoolean(properties, "need.mq", false);
            boolean needRedis = PropertiesUtil.getBoolean(properties, "need.redis", false);
            projectConfig = new ProjectConfig(projectGroupId, projectArtifactId, projectAbsolutePath, needMq, needRedis);
            String eurekaUrl = PropertiesUtil.getString(properties, "eureka.url");
            projectConfig.setEurekaUrl(eurekaUrl);
            String port = PropertiesUtil.getString(properties, "port");
            projectConfig.setPort(port);

            String redisIp = PropertiesUtil.getString(properties, "redis.ip");
            projectConfig.setRedisIp(redisIp);


            String datasourceUrl = PropertiesUtil.getString(properties, "datasource.url");
            projectConfig.setDatasourceUrl(datasourceUrl);
            String datasourceUsername = PropertiesUtil.getString(properties, "datasource.username");
            projectConfig.setDatasourceUsername(datasourceUsername);
            String datasourcePassword = PropertiesUtil.getString(properties, "datasource.password");
            projectConfig.setDatasourcePassword(datasourcePassword);

            String mqIp = PropertiesUtil.getString(properties, "mq.ip");
            projectConfig.setMqIp(mqIp);
            String mqUsername = PropertiesUtil.getString(properties, "mq.username");
            projectConfig.setMqUsername(mqUsername);
            String mqPassword = PropertiesUtil.getString(properties, "mq.password");
            projectConfig.setMqPassword(mqPassword);
            String mqHost = PropertiesUtil.getString(properties, "mq.host");
            projectConfig.setMqHost(mqHost);


        } catch (IOException e) {
            getLog().error("error ", e);
        }
        assert projectConfig != null;
        projectConfig.setTemplatePath(projectConfig.getResourcePath() + "/template");
        this.projectConfig = projectConfig;
    }
}
