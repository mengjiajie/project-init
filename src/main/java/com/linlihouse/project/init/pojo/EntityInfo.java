package com.linlihouse.project.init.pojo;

import lombok.Data;

import java.util.List;

@Data
public class EntityInfo {
    private String tableName;
    private String entityName;
    private String entityNameLowerCase;
    private List<FiledInfo> filedInfoList;
}
