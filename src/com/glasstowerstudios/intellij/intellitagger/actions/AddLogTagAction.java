package com.glasstowerstudios.intellij.intellitagger.actions;

import com.glasstowerstudios.intellij.intellitagger.util.IntellitaggerPsiTreeUtils;
import com.glasstowerstudios.intellij.intellitagger.operation.JavaSourceChecker;
import com.glasstowerstudios.intellij.intellitagger.operation.LogTagFieldAdder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

/**
 * An action that allows IDEA to add a 'LOGTAG' field to an existing class opened within an editor.
 */
public class AddLogTagAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        DataContext dataContext = anActionEvent.getDataContext();
        Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        JavaSourceChecker checker = new JavaSourceChecker(dataContext);
        boolean isJavaBased = checker.isJavaBasedSourceCode();
        if (!isJavaBased) {
            Messages.showMessageDialog(project, "This doesn't appear to be a Java-based project. Sorry, you won't be" +
                    " able to use this plugin.", "Error", Messages.getErrorIcon());
            return;
        }

        IntellitaggerPsiTreeUtils psiTreeUtils = new IntellitaggerPsiTreeUtils(dataContext);
        PsiFile file = anActionEvent.getData(PlatformDataKeys.PSI_FILE);
        PsiClass publicClass = psiTreeUtils.getEnclosingPublicClassFromFile(file);

        if (publicClass != null) {
            LogTagFieldAdder.addLogtagToClass(publicClass);
        }

        return;
    }
}
