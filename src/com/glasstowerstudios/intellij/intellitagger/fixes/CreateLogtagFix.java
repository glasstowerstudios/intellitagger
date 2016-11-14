package com.glasstowerstudios.intellij.intellitagger.fixes;

import com.glasstowerstudios.intellij.intellitagger.operation.LogTagFieldAdder;
import com.glasstowerstudios.intellij.intellitagger.resources.IntellitaggerBundle;
import com.glasstowerstudios.intellij.intellitagger.settings.SettingsHelper;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateVarFromUsageFix;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.RenameJavaMemberProcessor;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.usageView.UsageInfo;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * A {@link BaseIntentionAction} (i.e. QuickFix) that allows for IDEA to add a 'LOGTAG' field to a
 * class automatically when it is detected that the variable with the specified name (exactly
 * "LOGTAG") is missing.
 */
public class CreateLogtagFix extends CreateVarFromUsageFix {
  private static final Logger LOG = Logger.getInstance(CreateLogtagFix.class);

  public CreateLogtagFix(final PsiReferenceExpression referenceElement) {
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
        // We first add the member variable as the reference expression name, then perform a rename
        // refactoring to change it.
        LogTagFieldAdder.addLogtagToClass(myReferenceExpression.getReferenceName(), psiClass);
        renameAllReferences(psiClass, myReferenceExpression.getReferenceName(),
                            SettingsHelper.getLogtagVariableName());
        break;

      case NO_ADJUSTMENT:
      default:
        LogTagFieldAdder.addLogtagToClass(SettingsHelper.getLogtagVariableName(), psiClass);
    }
  }

  /**
   * Refactor all references of a member variable within a given {@link PsiClass} having a given
   * identifier to have another identifier.
   *
   * @param aClass The {@link PsiClass} defining the scope of the variable within which to search.
   *               aFromName must be an identifier for a member variable of this class, or nothing
   *               will happen.
   * @param aFromName The name/identifier of the reference to search for.
   * @param aToName The new name/identifier of the references after renaming is complete.
   */
  private void renameAllReferences(PsiClass aClass, String aFromName, final String aToName) {
    final PsiElement member = getClassLevelMemberWithName(aClass, aFromName);
    if (member != null) {
      final Collection<PsiReference> allRefs = ReferencesSearch.search(member).findAll();
      RenamePsiElementProcessor processor = RenameJavaMemberProcessor.forElement(member);
      UsageInfo usages[] = new UsageInfo[allRefs.size()];
      int i = 0;
      for (PsiReference ref : allRefs) {
        usages[i] = new UsageInfo(ref);
        i++;
      }

      processor.renameElement(member, aToName, usages, null);
    }
  }

  /**
   * Retrieve the {@link PsiElement} having a given name within a {@link PsiClass}.
   *
   * @param aClass The {@link PsiClass} to search within.
   * @param aName The name of the variable to search for.
   *
   * @return The {@link PsiElement} member of class aClass having name aName, if it exists; null,
   *         otherwise.
   */
  private PsiElement getClassLevelMemberWithName(PsiClass aClass, String aName) {
    for (PsiElement nextElement : aClass.getChildren()) {
      if (nextElement instanceof PsiField) {
        PsiField field = (PsiField) nextElement;
        if (field.getName().equals(aName)) {
          return nextElement;
        }
      }
    }

    return null;
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return IntellitaggerBundle.message("add.logtag.family");
  }

  public PsiReferenceExpression getReferenceExpression() {
    return myReferenceExpression;
  }
}