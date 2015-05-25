package com.glasstowerstudios.intellij.intellitagger.base;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.ResolveScopeManager;
import com.intellij.psi.search.GlobalSearchScope;

/**
 *
 */
public class PsiTreeModifier {
    private Project mProject;
    private JavaPsiFacade mJavaFacade;
    private PsiElementFactory mElementFactory;

    public PsiTreeModifier(Project aProject) {
        mProject = aProject;
        mJavaFacade = JavaPsiFacade.getInstance(mProject);
        mElementFactory = mJavaFacade.getElementFactory();
    }

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
}
