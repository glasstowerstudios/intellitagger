package com.glasstowerstudios.intellij.intellitagger.settings;

import com.intellij.ide.util.PropertiesComponent;

/**
 * Helper class to retrieve and modify settings specific to Intellitagger plugin.
 */
public class SettingsHelper {
  private static final String POSSIBLE_LOGTAG_SETTINGS_KEY = "possibleLogtagNames";
  private static final String LOGTAG_VARIABLE_NAME_SETTINGS_KEY = "logtagVariableName";
  private static final String UNDEFINED_REFERENCE_POLICY_SETTINGS_KEY = "undefinedReferencePolicy";

  public static String[] getPossibleLogtagNames() {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    String[] possibleNames = propertiesComponent.getValues(POSSIBLE_LOGTAG_SETTINGS_KEY);

    return possibleNames != null ? possibleNames : new String[0];
  }

  public static void setPossibleLogtagNames(String[] aLogtagNames) {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    propertiesComponent.setValues(POSSIBLE_LOGTAG_SETTINGS_KEY, aLogtagNames);
  }

  public static boolean isVariableNameInAcceptedLogtagNames(String aVariableName) {
    for (String acceptedString : getPossibleLogtagNames()) {
      if (aVariableName.equals(acceptedString)) {
        return true;
      }
    }

    return false;
  }

  public static final String getLogtagVariableName() {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    return propertiesComponent.getOrInit(LOGTAG_VARIABLE_NAME_SETTINGS_KEY, "LOGTAG");
  }

  public static final void setLogtagVariableName(String aVariableName) {
    // TODO: We should check here to make sure this is a legal Java identifier.
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    propertiesComponent.setValue(LOGTAG_VARIABLE_NAME_SETTINGS_KEY, aVariableName);
  }

  public static final UndefinedReferencePolicy getUndefinedReferencePolicy() {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    int urp = propertiesComponent.getOrInitInt(UNDEFINED_REFERENCE_POLICY_SETTINGS_KEY,
                                               UndefinedReferencePolicy.NO_ADJUSTMENT.ordinal());
    if (urp == UndefinedReferencePolicy.ADJUST_REFERENCES.ordinal()) {
      return UndefinedReferencePolicy.ADJUST_REFERENCES;
    }

    if (urp == UndefinedReferencePolicy.ADJUST_VARIABLE_NAME.ordinal()) {
      return UndefinedReferencePolicy.ADJUST_VARIABLE_NAME;
    }

    return UndefinedReferencePolicy.NO_ADJUSTMENT;
  }

  public static final void setUndefinedReferencePolicy(UndefinedReferencePolicy aPolicy) {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    propertiesComponent
      .setValue(UNDEFINED_REFERENCE_POLICY_SETTINGS_KEY, Integer.toString(aPolicy.ordinal()));
  }

  public enum UndefinedReferencePolicy {
    NO_ADJUSTMENT,
    ADJUST_VARIABLE_NAME,
    ADJUST_REFERENCES
  }
}
