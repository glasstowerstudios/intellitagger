package com.glasstowerstudios.intellij.intellitagger.util;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.ResolveScopeManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Utilities specific to the Intellitagger plugin for operating on trees of {@link PsiElement}s.
 */
public class IntellitaggerPsiTreeUtils {
    private DataContext mContext;
    private Project mProject;
    private JavaPsiFacade mJavaFacade;
    private PsiElementFactory mElementFactory;

    public IntellitaggerPsiTreeUtils(@NonNull DataContext aContext) {
        this (PlatformDataKeys.PROJECT.getData(aContext));
        mContext = aContext;
    }

    public IntellitaggerPsiTreeUtils(@NonNull Project aProject) {
        mProject = aProject;
        mJavaFacade = JavaPsiFacade.getInstance(mProject);
        mElementFactory = mJavaFacade.getElementFactory();
    }

    /**
     * Add a 'LOGTAG' field to the source for a given {@link PsiClass}.
     *
     * @param aPsiClass The enclosing {@link PsiClass} on which to perform this operation.
     */
    public void addLogtagToClass(PsiClass aPsiClass) {
        PsiManager manager = aPsiClass.getManager();
        GlobalSearchScope resolveScope = ResolveScopeManager.getElementResolveScope(aPsiClass);
        PsiExpression initializer =
                mElementFactory.createExpressionFromText(aPsiClass.getName()+ ".class.getSimpleName()", null);
        PsiField logtagField = mElementFactory.createField("LOGTAG", PsiType.getJavaLangString(manager, resolveScope));

        if (logtagField.getModifierList() != null) {
            logtagField.getModifierList().setModifierProperty("public", true);
            logtagField.getModifierList().setModifierProperty("static", true);
            logtagField.getModifierList().setModifierProperty("final", true);
        }

        logtagField.setInitializer(initializer);
        aPsiClass.add(logtagField);
    }

    /**
     * Retrieve the outermost {@link PsiClass} within a given {@link PsiFile}.
     *
     * @param aFile The {@link PsiFile} on which to operate.
     *
     * @return The outermost {@link PsiClass} in the file, if one exists; null, otherwise.g
     */
    public PsiClass getEnclosingPublicClassFromFile(PsiFile aFile) {
        Editor editor = PlatformDataKeys.EDITOR.getData(mContext);
        int offset = editor.getCaretModel().getOffset();
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
