package com.linlihouse.project.init.util;

import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.io.InputStreamFacade;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ZHJJ
 * Date: 2018/5/4
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
public class FileGenerater {
    private HashMap<String, String> replaceStrs = new HashMap<String, String>();
    private String templatePath;
    private String targetPath;

    public void addReplaceStr(String keystr, String replaceStr) {
        this.replaceStrs.put(keystr, replaceStr);
    }

    public void removeReplaceStr(String keystr) {
        this.replaceStrs.remove(keystr);
    }

    public void clearReplaceStrs() {
        this.replaceStrs = new HashMap<String, String>();
    }

    public HashMap<String, String> getReplaceStrs() {
        return replaceStrs;
    }

    public void setReplaceStrs(HashMap<String, String> replaceStrs) {
        this.replaceStrs = replaceStrs;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public FileGenerater() {
    }

    public FileGenerater(HashMap<String, String> replaceStrs, String templatePath, String targetPath) {
        this.replaceStrs = replaceStrs;
        this.templatePath = templatePath;
        this.targetPath = targetPath;
    }

    public void generate() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            System.out.println("this.templatePath->"+this.templatePath);
            reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(this.templatePath), "utf-8"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.targetPath), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (this.replaceStrs != null) {
                    Set<String> keySet = this.replaceStrs.keySet();
                    Iterator<String> keyIterator = keySet.iterator();
                    while (keyIterator.hasNext()) {
                        String keyStr = keyIterator.next();
                        String replaceStr = this.replaceStrs.get(keyStr);
                        line = line.replaceAll("\\$\\{" + keyStr + "\\}", replaceStr);
                    }
                }
                writer.write(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generate(String templatePath, String targetPath) {
        this.templatePath = templatePath;
        this.targetPath = targetPath;
        generate();
    }

    public void generate(HashMap<String, String> replaceStrs, String templatePath, String targetPath) {
        this.replaceStrs = replaceStrs;
        this.templatePath = templatePath;
        this.targetPath = targetPath;
        generate();
    }
    //文件拷贝
    public void copy(){
        final InputStream sourceFileStream = getClass().getClassLoader().getResourceAsStream(this.getTemplatePath());
        File targetFile=new File(this.getTargetPath());
        InputStreamFacade inputStreamFacade= () -> sourceFileStream;
        try {
            FileUtils.copyStreamToFile(inputStreamFacade, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void copy(String templatePath, String targetPath){
        this.templatePath = templatePath;
        this.targetPath = targetPath;
        copy();
    }
}
