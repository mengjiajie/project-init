package com.linlihouse.project.init.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
/**
 * Created with IntelliJ IDEA.
 * User: ZHJJ
 * Date: 2018/5/4
 * Time: 23:22
 * To change this template use File | Settings | File Templates.
 */
@Mojo( name = "info")
public class LogProjectInfo  extends AbstractMojo {
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("getPluginContext:"+getPluginContext());
        getLog().info("project->" + getPluginContext().get("project"));
        MavenProject projectInfo= (MavenProject) getPluginContext().get("project");
        getLog().info("project-getGroupId->" + projectInfo.getGroupId());
        getLog().info("project-getArtifactId->" + projectInfo.getArtifactId());
        getLog().info("project-getPackaging->" + projectInfo.getPackaging());
        getLog().info("project-getAbsolutePath->" + projectInfo.getBasedir().getAbsolutePath());
    }
}
