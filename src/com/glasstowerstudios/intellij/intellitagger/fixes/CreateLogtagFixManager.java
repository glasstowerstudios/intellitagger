package com.glasstowerstudios.intellij.intellitagger.fixes;

import com.glasstowerstudios.intellij.intellitagger.util.IntellitaggerPsiTreeUtils;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiReferenceExpression;
import com.sun.javafx.beans.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CreateLogtagFixManager {
  private static final CreateLogtagFixManager sInstance = new CreateLogtagFixManager();

  private Map<Document, List<CreateLogtagFix>> mDocumentFixMap;

  private CreateLogtagFixManager() {
    mDocumentFixMap = new HashMap<>();
  }

  public static final CreateLogtagFixManager getInstance() {
    return sInstance;
  }

  public void addPossibleFix(@NonNull Document aDocument, @NonNull CreateLogtagFix aFix) {
    List<CreateLogtagFix> fixes;
    if (mDocumentFixMap.containsKey(aDocument)) {
      fixes = mDocumentFixMap.get(aDocument);
    } else {
      fixes = new ArrayList<>();
    }

    fixes.add(aFix);
    mDocumentFixMap.put(aDocument, fixes);
  }

  private void initializeForDocument(final Document aDocument) {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      @Override
      public void run() {
        VirtualFile file = FileDocumentManager.getInstance().getFile(aDocument);
        PsiClass enclosingClass = IntellitaggerPsiTreeUtils
          .getEnclosingPublicClassFromFile(referenceElement.getContainingFile());
        invokeImpl(enclosingClass);
        Editor editor =
          FileEditorManager.getInstance(referenceElement.getProject()).getSelectedTextEditor();
        if (editor != null) {
          CreateLogtagFixManager.getInstance()
                                .addPossibleFix(editor.getDocument(), CreateLogtagFix.this);
        }
      }
    }, ModalityState.NON_MODAL);
  }

  public List<CreateLogtagFix> getFixesForDocument(@NonNull Document aDocument) {
    List<CreateLogtagFix> fixesForDocument = mDocumentFixMap.get(aDocument);
    if (fixesForDocument == null) {
      fixesForDocument = new ArrayList<>();
    }

    return fixesForDocument;
  }

  public void applyCreateLogtagFixesForDocument(@NonNull Document aDocument) {
    for (CreateLogtagFix fix : getFixesForDocument(aDocument)) {
      PsiReferenceExpression refExpression = fix.getReferenceExpression();
      PsiClass enclosingClass = IntellitaggerPsiTreeUtils.getEnclosingPublicClassFromFile(refExpression.getContainingFile());
      fix.invokeImpl(enclosingClass);
    }
  }
}
