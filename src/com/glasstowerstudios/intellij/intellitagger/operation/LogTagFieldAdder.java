package com.glasstowerstudios.intellij.intellitagger.operation;

import com.glasstowerstudios.intellij.intellitagger.util.IntellitaggerPsiTreeUtils;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

/**
 * An opearation that adds a 'LOGTAG' field to an enclosing public class.
 */
public class LogTagFieldAdder {

  /**
   * Add a logging tag field to a {@link PsiClass}.
   *
   * @param aVariableName The name of the field to add.
   * @param aPublicClass  The {@link PsiClass} which will be modified to contain the new field.
   */
  public static void addLogtagToClass(final String aVariableName, final PsiClass aPublicClass) {
    final Project proj = aPublicClass.getProject();

//    if (SwingUtilities.isEventDispatchThread()) {
      new WriteCommandAction(proj) {
        @Override
        protected void run(Result result) throws Throwable {
          IntellitaggerPsiTreeUtils modifier = new IntellitaggerPsiTreeUtils(proj);
          modifier.addLogtagToClass(aVariableName, aPublicClass);
        }
      }.execute();
//    }
  }
}
