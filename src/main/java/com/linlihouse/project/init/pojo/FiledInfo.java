package com.linlihouse.project.init.pojo;

import lombok.Data;

@Data
public class FiledInfo {
    private String filedName;
    private String filedType;
    private String columnName;
    private String comment;
    private boolean primaryKey;
}
