package com.glasstowerstudios.intellij.intellitagger.components;

import com.glasstowerstudios.intellij.intellitagger.listeners.IntellitaggerFileOpenedListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.sun.javafx.beans.annotations.NonNull;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class IntellitaggerOnSaveComponent
  implements ProjectComponent {
  private static final Logger Log = Logger.getInstance(IntellitaggerOnSaveComponent.class);

  private Project mProject;

  public IntellitaggerOnSaveComponent(@NonNull Project aProject) {
    mProject = aProject;
  }

//  @Override
//  public void initComponent() {
//    PsiManager.getInstance(proj).addPsiTreeChangeListener(this);
//    MessageBus bus = ApplicationManager.getApplication().getMessageBus();
//
//    MessageBusConnection connection = bus.connect();
//
//    connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC,
//                         new FileDocumentManagerAdapter() {
//                           @Override
//                           public void beforeDocumentSaving(Document aDocument) {
//                             CreateLogtagFixManager.getInstance().applyCreateLogtagFixesForDocument(aDocument);
//                           }
//                         });
//  }

//  @Override
//  public void disposeComponent() {
//
//  }

  @NotNull
  @Override
  public String getComponentName() {
    return "Intellitagger OnSave Component";
  }

  @Override
  public void projectOpened() {
//    PsiManager.getInstance(proj).addPsiTreeChangeListener(this);
    MessageBus bus = ApplicationManager.getApplication().getMessageBus();
    MessageBusConnection connection = bus.connect();
    connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new IntellitaggerFileOpenedListener());
//    FileEditorManager.getInstance(mProject).addFileEditorManagerListener(new IntellitaggerFileOpenedListener());
//    Log.info("***** DEBUG_jwir3: Project: " + mProject.getName() + " opened!");
//    CreateLogtagFixManager.getInstance().applyCreateLogtagFixesForDocument(aDocument);
  }

  @Override
  public void projectClosed() {

  }

  @Override
  public void initComponent() {

  }

  @Override
  public void disposeComponent() {

  }
}
