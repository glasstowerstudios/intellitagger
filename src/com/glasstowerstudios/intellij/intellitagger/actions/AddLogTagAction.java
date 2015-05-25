package com.glasstowerstudios.intellij.intellitagger.actions;

import com.glasstowerstudios.intellij.intellitagger.base.PsiTreeModifier;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.lang.LanguageFormatting;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

public class AddLogTagAction extends AnAction {
    private PsiTreeModifier mModifier;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
        SdkTypeId type = projectSdk.getSdkType();

        mModifier = new PsiTreeModifier(project);

        boolean isJavaBased = JavaSdkType.class.isAssignableFrom(type.getClass());
        if (!isJavaBased) {
            Messages.showMessageDialog(project, "This doesn't appear to be a Java-based project. Sorry, you won't be" +
                    " able to use this plugin.", "Error", Messages.getErrorIcon());
            return;
        }

        Editor editor = anActionEvent.getData(PlatformDataKeys.EDITOR);
        PsiFile file = anActionEvent.getData(PlatformDataKeys.PSI_FILE);
        PsiClass publicClass = getEnclosingPublicClassFromFile(editor, file);

        FormattingModelBuilder modelBuilder = LanguageFormatting.INSTANCE.forContext(publicClass);

        if (publicClass != null) {
            addLogtagToClass(editor, publicClass, modelBuilder);
        }

        return;
    }

    private void addLogtagToClass(final Editor aEditor, final PsiClass aPublicClass,
                                  final FormattingModelBuilder aFormatBuilder) {
        // Add:
        // public static final String LOGTAG = <ClassName>.class.getSimpleName();
        new WriteCommandAction(aEditor.getProject()) {

            @Override
            protected void run(Result result) throws Throwable {
                mModifier.addLogtagToClass(aPublicClass);
            }
        }.execute();
    }

    private PsiClass getEnclosingPublicClassFromFile(Editor aEditor, PsiFile aFile) {
        int offset = aEditor.getCaretModel().getOffset();
        PsiElement elementAtCurrentPosition = aFile.findElementAt(offset);
        while (elementAtCurrentPosition != null) {
            PsiClass parentClass = PsiTreeUtil.getParentOfType(elementAtCurrentPosition, PsiClass.class);

            if (parentClass == null) {
                return null;
            }

            PsiModifierList modifiers = parentClass.getModifierList();
            if (modifiers != null && modifiers.hasModifierProperty(PsiModifier.PUBLIC)) {
                return parentClass;
            }

            elementAtCurrentPosition = parentClass;
        }

        return null;
    }
}
