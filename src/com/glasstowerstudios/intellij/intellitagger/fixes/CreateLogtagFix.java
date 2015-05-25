package com.glasstowerstudios.intellij.intellitagger.fixes;

import com.glasstowerstudios.intellij.intellitagger.operation.LogTagFieldAdder;
import com.glasstowerstudios.intellij.intellitagger.resources.IntellitaggerBundle;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateVarFromUsageFix;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceExpression;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link BaseIntentionAction} (i.e. QuickFix) that allows for IDEA to add a 'LOGTAG' field to a class automatically
 * when it is detected that the variable with the specified name (exactly "LOGTAG") is missing.
 */
public class CreateLogtagFix extends CreateVarFromUsageFix {
    public CreateLogtagFix(PsiReferenceExpression referenceElement) {
        super(referenceElement);
    }

    @Nls
    @Override
    protected String getText(String aVarName) {
        PsiFile enclosingFile = getElement().getContainingFile();
        return IntellitaggerBundle.message("add.logtag.text", enclosingFile.getName());
    }

    @Override
    protected void invokeImpl(PsiClass psiClass) {
        LogTagFieldAdder.addLogtagToClass(psiClass);
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return IntellitaggerBundle.message("add.logtag.family");
    }
}