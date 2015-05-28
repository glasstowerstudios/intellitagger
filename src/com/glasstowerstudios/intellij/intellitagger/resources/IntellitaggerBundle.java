package com.glasstowerstudios.intellij.intellitagger.resources;

import com.intellij.CommonBundle;
import com.intellij.reference.SoftReference;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.util.ResourceBundle;

/**
 * Resource bundle for Intellitagger IDEA plugin.
 */
public class IntellitaggerBundle {
  @NonNls
  private static final String BUNDLE = "resources.intellitagger";
  private static Reference<ResourceBundle> mBundle;

  private IntellitaggerBundle() {
  }

  public static String message(@NotNull @NonNls @PropertyKey(
    resourceBundle = "resources.intellitagger"
  ) String key, @NotNull Object... params) {
    return CommonBundle.message(getBundle(), key, params);
  }

  private static ResourceBundle getBundle() {
    ResourceBundle bundle = SoftReference.dereference(mBundle);
    if (bundle == null) {
      bundle = ResourceBundle.getBundle(BUNDLE);
      mBundle = new java.lang.ref.SoftReference(bundle);
    }

    return bundle;
  }
}
