package com.glasstowerstudios.intellij.intellitagger.operation;

import com.intellij.openapi.actionSystem.DataContext;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * An abstract base class for all operations available from the Intellitagger plugin.
 */
public abstract class BaseIntellitaggerOperation {
  private DataContext mContext;

  public BaseIntellitaggerOperation(@NonNull DataContext aContext) {
    mContext = aContext;
  }

  protected DataContext getDataContext() {
    return mContext;
  }

}
