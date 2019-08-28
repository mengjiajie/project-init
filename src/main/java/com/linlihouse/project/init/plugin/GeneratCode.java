package com.linlihouse.project.init.plugin;

import com.google.common.base.CaseFormat;
import com.linlihouse.project.init.pojo.*;
import com.linlihouse.project.init.service.CodeGenerator;
import com.linlihouse.project.init.util.FileGenerater;
import com.linlihouse.project.init.util.StringUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.sql.*;
import java.util.*;

@Mojo(name = "generat")
public class GeneratCode extends AbstractMojoWithProjectConfig {

    Connection connection;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        init();
        getLog().info("初始化完毕.");
        ProjectConfig projectConfig = super.getProjectConfig();
        assert projectConfig != null;
        File file = new File(projectConfig.getResourcePath() + "/template");
        File[] templateFiles = file.listFiles();
        if (templateFiles == null || templateFiles.length == 0) {
            // 表明模板目录为空
            getLog().info("模板文件为空,已经自动生成示例模板.");
            FileGenerater fileGenerater = new FileGenerater();
            String targetPath = projectConfig.getTemplatePath() + "/SampleTemplate.template";
            String templatePath = "template/SampleTemplate.template";
            fileGenerater.copy(templatePath, targetPath);
            throw new RuntimeException("模板文件为空,已经自动生成示例模板.");
        }

        connection = connectDatabase(projectConfig);

        getLog().info("读取到模板文件.共" + templateFiles.length + "个");
        List<EntityInfo> entityInfoList = new ArrayList<>(templateFiles.length);
        for (File templateFile : templateFiles) {
            EntityInfo entityInfo = new EntityInfo();
            try (InputStream fileInputStream = new FileInputStream(templateFile);
                 InputStreamReader reader = new InputStreamReader(fileInputStream);
                 BufferedReader br = new BufferedReader(reader)) {
                String line = null;
                List<FiledInfo> filedInfoList = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    line = line.trim();

                    if (!line.startsWith("#") && line.length() > 0) {
                        String[] lineStrs = line.split("-");
                        if (Objects.equals(lineStrs[0], "table")) {
                            entityInfo.setTableName(lineStrs[1]);
                            entityInfo.setEntityName(lineStrs[2]);
                            getLog().info("读取到表名" + entityInfo.getTableName());

                            Map<EntityKey, EntityValue> map = getColumnInfo(connection, entityInfo.getTableName());

                            for (Map.Entry<EntityKey, EntityValue> stringMap : map.entrySet()) {
                                FiledInfo filedInfo = new FiledInfo();
                                filedInfo.setColumnName(stringMap.getKey().getFieldName());
                                filedInfo.setFiledType(castType(stringMap.getValue().getValue()));
                                getLog().info("类型"+filedInfo.getFiledType());
                                filedInfo.setFiledName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, stringMap.getKey().getFieldName()));
                                filedInfo.setComment(stringMap.getValue().getComment());
                                filedInfo.setPrimaryKey(isPrimaryKey(stringMap.getKey().getFieldName(), entityInfo.getTableName()));
                                filedInfoList.add(filedInfo);
                                getLog().info("读取字段" + filedInfo.getColumnName());
                            }
                            //设置属性
                        }
                    }

                }
                entityInfo.setFiledInfoList(filedInfoList);
                entityInfo.setEntityNameLowerCase(StringUtil.toLowerCase(entityInfo.getEntityName()));
                entityInfoList.add(entityInfo);
                getLog().info("模板文件解析完毕");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        //解析模版文件完毕
        for (EntityInfo entityInfo : entityInfoList) {
            CodeGenerator generator = new CodeGenerator(entityInfo, projectConfig);
            generator.generate();
        }


    }

    public Connection connectDatabase(ProjectConfig projectConfig) {
        Properties props = new Properties();
        String resourcePath = projectConfig.getResourcePath();
        File file = new File(resourcePath + "/application-dev.properties");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            props.load(fileInputStream);
            String url = props.get("spring.datasource.url").toString();
            String username = props.get("spring.datasource.username").toString();
            String password = props.get("spring.datasource.password").toString();
            String driver = props.get("spring.datasource.driver-class-name").toString();

            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);

            return connection;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<EntityKey, EntityValue> getColumnInfo(Connection connection, String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
            Map<EntityKey, EntityValue> map = new HashMap<>();
            while (columns.next()) {
                EntityKey entityKey = new EntityKey();
                EntityValue entityValue = new EntityValue();
                String column_name = columns.getString("COLUMN_NAME");
                String type_name = columns.getString("TYPE_NAME");
                String comment = columns.getString("REMARKS");
                entityKey.setFieldName(column_name);
                entityValue.setComment(comment);
                entityValue.setValue(type_name);
                map.put(entityKey, entityValue);
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String castType(String type) {
        switch (type) {
            case "BIGINT":
                type = "long";
                break;
            case "VARCHAR":
                type = "String";
                break;
            case "TEXT":
                type = "String";
                break;
            case "INT":
                type = "int";
                break;
            case "TIMESTAMP":
                type = "String";
                break;
            case "DOUBLE":
                type = "double";
                break;
        }
        return type;
    }

    private boolean isPrimaryKey(String columnName, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
        ResultSet keys = metaData.getPrimaryKeys(null, null, tableName);
        while (keys.next()) {
            String key = (String) keys.getObject(4);
            if (columnName.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {


        Properties props = new Properties();
//        String resourcePath = projectConfig.getResourcePath();
//        File file = new File(resourcePath+"/application-dev.properties");
        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            props.load(fileInputStream);
//            String url = props.get("spring.datasource.url").toString();
//            String username = props.get("spring.datasource.username").toString();
//            String password = props.get("spring.datasource.password").toString();
//            String driver = props.get("spring.datasource.driver-class-name").toString();
            String url = "jdbc:mysql://rm-wz9qf783j343w5y4jeo.mysql.rds.aliyuncs.com:3306/linli-refact?characterEncoding=utf8&allowMultiQueries=true&autoReconnect=true&useSSL=false";
            String username = "dev";
            String password = "mUwLnYaU27";
            String driver = "com.mysql.jdbc.Driver";

            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);

            String tableName = "t_score_record";

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
            ResultSet importedKeys = metaData.getPrimaryKeys(null, null, tableName);
            while (importedKeys.next()) {
                System.out.println("主键" + importedKeys.getObject(4));
            }
            while (columns.next()) {
                String column_name = columns.getString("COLUMN_NAME");
                String type_name = columns.getString("TYPE_NAME");
                String remark = columns.getString("REMARKS");
                System.out.println(StringUtil.toLowerCase(column_name) + "-" + castType(type_name) + "-" + remark);
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
