package com.linlihouse.project.init.plugin;

import com.linlihouse.project.init.pojo.ProjectConfig;
import com.linlihouse.project.init.util.FileGenerater;
import com.linlihouse.project.init.util.StringUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.FileUtils;

/**
 * Created with IntelliJ IDEA.
 * User: ZHJJ
 * Date: 2018/5/4
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
@Mojo(name = "init")
public class InitProject extends AbstractMojoWithProjectConfig {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        init();
        ProjectConfig projectConfig = super.getProjectConfig();
        assert projectConfig != null;
        makePackage(projectConfig);//创建包
        generatePom(projectConfig);
        copyPropertiesFile(projectConfig);
        generateJavaFile(projectConfig);

        /*try {
            FileUtils.deleteDirectory(projectConfig.getProjectAbsolutePath() + "/.idea");
            FileUtils.fileDelete(projectConfig.getProjectAbsolutePath() + "/" + projectConfig.getArtifactId() + ".iml");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void generateJavaFile(ProjectConfig projectConfig) {
        //生成Application文件

        String artifactId = projectConfig.getArtifactId();
        String[] strs = artifactId.split("-");
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strs) {
            stringBuilder.append(StringUtil.toUpperCase(str));
        }
        stringBuilder.append("Application");
        String className = stringBuilder.toString();

        String templatePath = "template/java/TempApplication.template";
        String targetPath = projectConfig.getBasePackageAbsolutePath() + "/" + className + ".java";
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("packageName", projectConfig.getBasePackageName());
        fileGenerater.addReplaceStr("className", className);
        fileGenerater.addReplaceStr("daoPackageName", projectConfig.getDaoPackageName());
        fileGenerater.generate(templatePath, targetPath);
        //生成redis 配置文件
        if(projectConfig.isNeedRedis()){
            templatePath = "template/java/RedisConfig.template";
            fileGenerater.addReplaceStr("packageName",projectConfig.getConfigPackageName());
            targetPath = projectConfig.getConfigPackageAbsolutePath() + "/RedisConfig.java";
            fileGenerater.generate(templatePath, targetPath);
        }
        //生成mq相关文件
        if(projectConfig.isNeedMq()){
            templatePath = "template/java/RabbitConfig.template";
            fileGenerater.addReplaceStr("packageName",projectConfig.getConfigPackageName());
            fileGenerater.addReplaceStr("handlerFullName",projectConfig.getHandlerPackageName() + ".TempHandler");
            targetPath = projectConfig.getConfigPackageAbsolutePath() + "/RabbitConfig.java";
            fileGenerater.generate(templatePath, targetPath);

            templatePath = "template/java/MqTempHandler.template";
            fileGenerater.addReplaceStr("packageName",projectConfig.getHandlerPackageName());
            targetPath = projectConfig.getHandlerPackageAbsolutePath() + "/TempHandler.java";
            fileGenerater.generate(templatePath, targetPath);

            templatePath = "template/java/MqTempConverter.template";
            fileGenerater.addReplaceStr("packageName",projectConfig.getConverterPackageName());
            targetPath = projectConfig.getConverterPackageAbsolutePath() + "/TempConverter.java";
            fileGenerater.generate(templatePath, targetPath);

           /* templatePath = "template/java/MqTempConverter.template";
            fileGenerater.addReplaceStr("packageName",projectConfig.getConverterPackageName());
            targetPath = projectConfig.getConverterPackageAbsolutePath() + "/TempConverter.java";
            fileGenerater.generate(templatePath, targetPath);*/

            templatePath = "template/java/MqService.template";
            fileGenerater.addReplaceStr("packageName",projectConfig.getMqImplPackageName());
            fileGenerater.addReplaceStr("interfaceFullName",projectConfig.getMqPackageName() + ".IMqService");
            targetPath = projectConfig.getMqImplPackageAbsolutePath() + "/MqService.java";
            fileGenerater.generate(templatePath, targetPath);

            templatePath = "template/java/IMqService.template";
            fileGenerater.addReplaceStr("packageName",projectConfig.getMqPackageName());
            targetPath = projectConfig.getMqPackageAbsolutePath() + "/IMqService.java";
            fileGenerater.generate(templatePath, targetPath);

        }


    }

    private void copyPropertiesFile(ProjectConfig projectConfig) {

        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("eurekaUrl", projectConfig.getEurekaUrl());
        fileGenerater.addReplaceStr("port", projectConfig.getPort());
        fileGenerater.addReplaceStr("redisIp", projectConfig.getRedisIp());
        fileGenerater.addReplaceStr("datasourceUrl", projectConfig.getDatasourceUrl());
        fileGenerater.addReplaceStr("datasourceUsername", projectConfig.getDatasourceUsername());
        fileGenerater.addReplaceStr("datasourcePassword", projectConfig.getDatasourcePassword());
        fileGenerater.addReplaceStr("mqIp", projectConfig.getMqIp());
        fileGenerater.addReplaceStr("mqUsername", projectConfig.getMqUsername());
        fileGenerater.addReplaceStr("mqPassword", projectConfig.getMqPassword());
        fileGenerater.addReplaceStr("mqHost", projectConfig.getMqHost());
        fileGenerater.addReplaceStr("name", projectConfig.getName());
        fileGenerater.addReplaceStr("mapperXmlPath", projectConfig.getMapperXmlPath());
        String applicationDevTemplatePath = "";
        if (projectConfig.isNeedMq() && projectConfig.isNeedRedis()) { //同时依赖redis和mq
            getLog().info("同时依赖redis和mq");
            applicationDevTemplatePath = "template/application-dev-mq-redis.template";
        } else if (projectConfig.isNeedRedis() && !projectConfig.isNeedMq()) { // 依赖redis 不依赖mq
            getLog().info("依赖redis 不依赖mq");
            applicationDevTemplatePath = "template/application-dev-redis.template";
        } else if (!projectConfig.isNeedRedis() && projectConfig.isNeedMq()) {// 不依赖redis 依赖mq
            getLog().info("不依赖redis 依赖mq");
            applicationDevTemplatePath = "template/application-dev-mq.template";
        } else { //都不依赖
            getLog().info("都不依赖");
            applicationDevTemplatePath = "template/application-dev.template";
        }
        String targetPath = projectConfig.getResourcePath() + "/application-dev.properties";
        fileGenerater.generate(applicationDevTemplatePath, targetPath);
        getLog().info("生成开发配置文件");
        fileGenerater.copy("template/application.properties", projectConfig.getResourcePath() + "/application.properties");
        fileGenerater.copy("template/application-prod.properties", projectConfig.getResourcePath() + "/application-prod.properties");
        fileGenerater.copy("template/application-test.properties", projectConfig.getResourcePath() + "/application-test.properties");
        fileGenerater.copy("template/.gitignore", projectConfig.getProjectAbsolutePath() + "/.gitignore");
        fileGenerater.copy("template/banner.txt", projectConfig.getResourcePath() + "/banner.txt");
        getLog().info("复制其他资源文件");
        FileGenerater generater = new FileGenerater();
        generater.addReplaceStr("daoPackageName", projectConfig.getDaoPackageName());
        String logbackPath = projectConfig.getResourcePath() + "/logback.xml";
        String logbackTemplatePath = "template/logback.template";
        generater.generate(logbackTemplatePath, logbackPath);

    }

    private void generatePom(ProjectConfig projectConfig) {
        getLog().info("生成pom.xml");
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("groupId", projectConfig.getGroupId());
        fileGenerater.addReplaceStr("artifactId", projectConfig.getArtifactId());
        fileGenerater.addReplaceStr("name", projectConfig.getName());
        String pomTemplatePath = "";
        if (projectConfig.isNeedMq() && projectConfig.isNeedRedis()) { //同时依赖redis和mq
            getLog().info("同时依赖redis和mq");
            pomTemplatePath = "template/PomWithRedisAndMq.template";
        } else if (projectConfig.isNeedRedis() && !projectConfig.isNeedMq()) { // 依赖redis 不依赖mq
            getLog().info("依赖redis 不依赖mq");
            pomTemplatePath = "template/PomWithRedis.template";
        } else if (!projectConfig.isNeedRedis() && projectConfig.isNeedMq()) {// 不依赖redis 依赖mq
            getLog().info("不依赖redis 依赖mq");
            pomTemplatePath = "template/PomWithMq.template";
        } else { //都不依赖
            getLog().info("都不依赖");
            pomTemplatePath = "template/PomWithOutRedisAndMq.template";
        }
        String targetPath = projectConfig.getProjectAbsolutePath() + "/pom.xml";
        fileGenerater.generate(pomTemplatePath, targetPath);
    }

    private void makePackage(ProjectConfig projectConfig) {
        FileUtils.mkdir(projectConfig.getApiPackageAbsolutePath());
        FileUtils.mkdir(projectConfig.getPojoPackageAbsolutePath());
        FileUtils.mkdir(projectConfig.getServicePackageAbsolutePath());
        FileUtils.mkdir(projectConfig.getServiceImplPackageAbsolutePath());
        FileUtils.mkdir(projectConfig.getUtilPackageAbsolutePath());
        FileUtils.mkdir(projectConfig.getConfigPackageAbsolutePath());
        FileUtils.mkdir(projectConfig.getDaoPackageAbsolutePath());
        FileUtils.mkdir(projectConfig.getResourceMapperPath());
        FileUtils.mkdir(projectConfig.getTemplatePath());
        if (projectConfig.isNeedMq()) {
            FileUtils.mkdir(projectConfig.getMqPackageAbsolutePath());
            FileUtils.mkdir(projectConfig.getMqImplPackageAbsolutePath());
            FileUtils.mkdir(projectConfig.getConverterPackageAbsolutePath());
            FileUtils.mkdir(projectConfig.getHandlerPackageAbsolutePath());

        }
    }
}
