package com.glasstowerstudios.intellij.intellitagger.operation;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ProjectRootManager;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * A {@link BaseIntellitaggerOperation} that can determine if a particular project environment derives from a Java-based
 * API (i.e. Java JDK, Android, etc...).g
 */
public class JavaSourceChecker extends BaseIntellitaggerOperation {
    public JavaSourceChecker(@NonNull DataContext aContext) {
        super(aContext);
    }

    public boolean isJavaBasedSourceCode() {
        DataContext dataContext = getDataContext();
        Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
        SdkTypeId type = projectSdk.getSdkType();

        return JavaSdkType.class.isAssignableFrom(type.getClass());
    }
}
