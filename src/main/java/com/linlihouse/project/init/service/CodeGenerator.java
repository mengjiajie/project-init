package com.linlihouse.project.init.service;

import com.linlihouse.project.init.pojo.EntityInfo;
import com.linlihouse.project.init.pojo.FiledInfo;
import com.linlihouse.project.init.pojo.ProjectConfig;
import com.linlihouse.project.init.util.CastUtil;
import com.linlihouse.project.init.util.FileGenerater;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CodeGenerator {

    private EntityInfo entityInfo;
    private ProjectConfig projectConfig;

    public void generate() {
        generatePojo();
        generateDao();
        generateController();
        generateService();
        generateServiceImpl();
        generateMapperXml();
    }

    private void generatePojo() {
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("packageName", projectConfig.getPojoPackageName());

        fileGenerater.addReplaceStr("entityName", this.entityInfo.getEntityName());
        StringBuilder filedCode = new StringBuilder();

        List<FiledInfo> filedInfoList = this.entityInfo.getFiledInfoList();
        for (FiledInfo filedInfo : filedInfoList) {
            if (CastUtil.isNumberType(filedInfo.getFiledType())) {
                filedCode.append("    private ").append(filedInfo.getFiledType()).append(" ").append(filedInfo.getFiledName()).append(" = -1 ; // ").append(filedInfo.getComment()).append("\n");
            } else {
                filedCode.append("    private ").append(filedInfo.getFiledType()).append(" ").append(filedInfo.getFiledName()).append(" ; // ").append(filedInfo.getComment()).append("\n");
            }
        }
        fileGenerater.addReplaceStr("filed", filedCode.toString());
        String pojoTemplatePath = "template/java/generat/Pojo.template";
        String pojoPath = projectConfig.getPojoPackageAbsolutePath() + "/" + this.entityInfo.getEntityName() + ".java";
        fileGenerater.generate(pojoTemplatePath, pojoPath);
        pojoTemplatePath = "template/java/generat/PojoCondition.template";
        pojoPath = projectConfig.getPojoPackageAbsolutePath() + "/" + this.entityInfo.getEntityName() + "Condition.java";
        fileGenerater.generate(pojoTemplatePath, pojoPath);
        pojoTemplatePath = "template/java/generat/PojoCollection.template";
        pojoPath = projectConfig.getPojoPackageAbsolutePath() + "/" + this.entityInfo.getEntityName() + "Collection.java";
        fileGenerater.generate(pojoTemplatePath, pojoPath);

    }

    private void generateDao() {
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("packageName", projectConfig.getDaoPackageName());
        fileGenerater.addReplaceStr("entityName", this.entityInfo.getEntityName());
        fileGenerater.addReplaceStr("entityNameLowerCase", this.entityInfo.getEntityNameLowerCase());
        fileGenerater.addReplaceStr("entityFullName", this.projectConfig.getPojoPackageName() + "." +  this.entityInfo.getEntityName());
        fileGenerater.addReplaceStr("conditionFullName", this.projectConfig.getPojoPackageName() + "." +  this.entityInfo.getEntityName() + "Condition");
        String pojoTemplatePath = "template/java/generat/dao.template";
        String pojoPath = projectConfig.getDaoPackageAbsolutePath() + "/I" +this.entityInfo.getEntityName() + "Mapper.java";
        fileGenerater.generate(pojoTemplatePath, pojoPath);

    }
    private void generateController() {
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("packageName", projectConfig.getApiPackageName());
        fileGenerater.addReplaceStr("entityName", this.entityInfo.getEntityName());
        fileGenerater.addReplaceStr("entityNameLowerCase", this.entityInfo.getEntityNameLowerCase());
        fileGenerater.addReplaceStr("pojoPackageName", this.projectConfig.getPojoPackageName());
        fileGenerater.addReplaceStr("servicePackageName", this.projectConfig.getServicePackageName());

        String pojoTemplatePath = "template/java/generat/controller.template";
        String pojoPath = projectConfig.getApiPackageAbsolutePath() + "/" +this.entityInfo.getEntityName() + "Controller.java";
        fileGenerater.generate(pojoTemplatePath, pojoPath);

    }
    private void generateService() {
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("packageName", projectConfig.getServicePackageName());
        fileGenerater.addReplaceStr("entityName", this.entityInfo.getEntityName());
        fileGenerater.addReplaceStr("entityNameLowerCase", this.entityInfo.getEntityNameLowerCase());
        fileGenerater.addReplaceStr("pojoPackageName", this.projectConfig.getPojoPackageName());


        String pojoTemplatePath = "template/java/generat/service.template";
        String pojoPath = projectConfig.getServicePackageAbsolutePath() + "/I" +this.entityInfo.getEntityName() + "Service.java";
        fileGenerater.generate(pojoTemplatePath, pojoPath);

    }


    private void generateServiceImpl() {
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("packageName", projectConfig.getServiceImplPackageName());
        fileGenerater.addReplaceStr("entityName", this.entityInfo.getEntityName());
        fileGenerater.addReplaceStr("entityNameLowerCase", this.entityInfo.getEntityNameLowerCase());
        fileGenerater.addReplaceStr("pojoPackageName", this.projectConfig.getPojoPackageName());
        fileGenerater.addReplaceStr("daoPackageName", this.projectConfig.getDaoPackageName());
        fileGenerater.addReplaceStr("servicePackageName", this.projectConfig.getServicePackageName());


        String pojoTemplatePath = "template/java/generat/serviceImpl.template";
        String pojoPath = projectConfig.getServiceImplPackageAbsolutePath() + "/" +this.entityInfo.getEntityName() + "ServiceImpl.java";
        fileGenerater.generate(pojoTemplatePath, pojoPath);

    }
    private void generateMapperXml() {
        FileGenerater fileGenerater = new FileGenerater();
        fileGenerater.addReplaceStr("entityName", this.entityInfo.getEntityName());
        fileGenerater.addReplaceStr("entityNameLowerCase", this.entityInfo.getEntityNameLowerCase());
        fileGenerater.addReplaceStr("pojoPackageName", this.projectConfig.getPojoPackageName());
        fileGenerater.addReplaceStr("daoPackageName", this.projectConfig.getDaoPackageName());
        fileGenerater.addReplaceStr("tableName", this.entityInfo.getTableName());
        fileGenerater.addReplaceStr("resultCode", buildResultCode());
        fileGenerater.addReplaceStr("setCode", buildSetCode());
        fileGenerater.addReplaceStr("whereCode", buildWhereCode());


        String pojoTemplatePath = "template/java/generat/mapper.template";
        String pojoPath = projectConfig.getResourceMapperPath() + "/I" +this.entityInfo.getEntityName() + "Mapper.xml";
        fileGenerater.generate(pojoTemplatePath, pojoPath);

    }

    private String buildWhereCode() {
        StringBuilder setCode = new StringBuilder();
        List<FiledInfo> filedInfoList = this.entityInfo.getFiledInfoList();
        for (FiledInfo filedInfo : filedInfoList) {
            if(CastUtil.isNumberType(filedInfo.getFiledType())){
                setCode.append("            ").append("<if test=\"").append(filedInfo.getFiledName()).append(" !=-1\">\n")
                        .append("                ").append(" and ").append(filedInfo.getColumnName()).append("=#{").append(filedInfo.getFiledName()).append("}\n")
                        .append("            ").append("</if>\n");

            }else{
                setCode.append("            ").append("<if test=\"").append(filedInfo.getFiledName()).append(" !=null\">\n")
                        .append("                ").append(" and ").append(filedInfo.getColumnName()).append(" like concat('%',#{").append(filedInfo.getFiledName()).append("},'%')\n")
                        .append("            ").append("</if>\n");
            }

        }
        return setCode.toString();
    }

    private String buildSetCode() {
        StringBuilder setCode = new StringBuilder();
        List<FiledInfo> filedInfoList = this.entityInfo.getFiledInfoList();
        for (FiledInfo filedInfo : filedInfoList) {
            if(CastUtil.isNumberType(filedInfo.getFiledType())){
                setCode.append("                ").append("<if test=\"").append(filedInfo.getFiledName()).append(" !=-1\">\n")
                        .append("                    ").append(" , ").append(filedInfo.getColumnName()).append("=#{").append(filedInfo.getFiledName()).append("}\n")
                        .append("                ").append("</if>\n");

            }else{
                setCode.append("                ").append("<if test=\"").append(filedInfo.getFiledName()).append(" !=null\">\n")
                        .append("                    ").append(" , ").append(filedInfo.getColumnName()).append("=#{").append(filedInfo.getFiledName()).append("}\n")
                        .append("                ").append("</if>\n");
            }

        }
        return setCode.toString();
    }

    private String buildResultCode() {
        StringBuilder resultCode = new StringBuilder();
        List<FiledInfo> filedInfoList = this.entityInfo.getFiledInfoList();
        for (FiledInfo filedInfo : filedInfoList) {
            resultCode.append("        <result column=\"").append(filedInfo.getColumnName()).append("\" property=\"").append(filedInfo.getFiledName()).append("\"/>\n");
        }
        return resultCode.toString();
    }


}
