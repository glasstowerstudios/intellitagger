package com.glasstowerstudios.intellij.intellitagger.listeners;

import com.glasstowerstudios.intellij.intellitagger.fixes.CreateLogtagFixManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;

/**
 *
 */
public class IntellitaggerFileOpenedListener implements FileEditorManagerListener {
  private static final Logger Log = Logger.getInstance(IntellitaggerFileOpenedListener.class);

  @Override
  public void fileOpened(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
    Document doc = FileDocumentManager.getInstance().getDocument(virtualFile);
    CreateLogtagFixManager.getInstance().applyCreateLogtagFixesForDocument(doc);
  }

  @Override
  public void fileClosed(FileEditorManager fileEditorManager, VirtualFile virtualFile) {

  }

  @Override
  public void selectionChanged(FileEditorManagerEvent fileEditorManagerEvent) {

  }
}
