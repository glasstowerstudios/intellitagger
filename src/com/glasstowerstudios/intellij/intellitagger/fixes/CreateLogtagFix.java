package com.glasstowerstudios.intellij.intellitagger.fixes;

import com.glasstowerstudios.intellij.intellitagger.operation.LogTagFieldAdder;
import com.glasstowerstudios.intellij.intellitagger.resources.IntellitaggerBundle;
import com.glasstowerstudios.intellij.intellitagger.settings.SettingsHelper;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateVarFromUsageFix;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiReferenceExpression;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link BaseIntentionAction} (i.e. QuickFix) that allows for IDEA to add a 'LOGTAG' field to a
 * class automatically when it is detected that the variable with the specified name (exactly
 * "LOGTAG") is missing.
 */
public class CreateLogtagFix extends CreateVarFromUsageFix {
  public CreateLogtagFix(PsiReferenceExpression referenceElement) {
    super(referenceElement);
  }

  @Nls
  @Override
  protected String getText(String aVarName) {
    SettingsHelper.UndefinedReferencePolicy policy = SettingsHelper.getUndefinedReferencePolicy();
    switch (policy) {
      case ADJUST_VARIABLE_NAME:
        return IntellitaggerBundle.message("add.logtag.adjust.text",
                                           myReferenceExpression.getReferenceName());

      case ADJUST_REFERENCES:
      case NO_ADJUSTMENT:
      default:
        return IntellitaggerBundle.message("add.logtag.noAdjust.text",
                                           SettingsHelper.getLogtagVariableName());
    }
  }

  @Override
  protected void invokeImpl(PsiClass psiClass) {
    SettingsHelper.UndefinedReferencePolicy policy = SettingsHelper.getUndefinedReferencePolicy();
    switch (policy) {
      case ADJUST_VARIABLE_NAME:
        LogTagFieldAdder.addLogtagToClass(myReferenceExpression.getReferenceName(),
                                          psiClass);
        break;

      case ADJUST_REFERENCES:
      case NO_ADJUSTMENT:
      default:
        LogTagFieldAdder.addLogtagToClass(SettingsHelper.getLogtagVariableName(), psiClass);
    }
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return IntellitaggerBundle.message("add.logtag.family");
  }
}