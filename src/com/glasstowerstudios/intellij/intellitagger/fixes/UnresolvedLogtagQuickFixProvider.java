package com.glasstowerstudios.intellij.intellitagger.fixes;

import com.glasstowerstudios.intellij.intellitagger.settings.SettingsHelper;
import com.intellij.codeInsight.daemon.QuickFixActionRegistrar;
import com.intellij.codeInsight.quickfix.UnresolvedReferenceQuickFixProvider;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiReferenceExpression;

import org.jetbrains.annotations.NotNull;

/**
 * An {@link UnresolvedReferenceQuickFixProvider} that enables the use of a {@link CreateLogtagFix}
 * if it is detected that the unresolved reference corresponds exactly to the identifier "LOGTAG".
 */
public class UnresolvedLogtagQuickFixProvider
  extends UnresolvedReferenceQuickFixProvider<PsiJavaCodeReferenceElement> {

  @Override
  public void registerFixes(@NotNull PsiJavaCodeReferenceElement aRef,
                            @NotNull QuickFixActionRegistrar quickFixActionRegistrar) {
    if (SettingsHelper.isVariableNameInAcceptedLogtagNames(aRef.getElement().getText())) {
      quickFixActionRegistrar.register(new CreateLogtagFix((PsiReferenceExpression) aRef));
    }
  }

  @NotNull
  @Override
  public Class<PsiJavaCodeReferenceElement> getReferenceClass() {
    return PsiJavaCodeReferenceElement.class;
  }
}
