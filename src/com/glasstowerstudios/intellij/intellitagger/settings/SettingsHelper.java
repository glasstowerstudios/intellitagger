package com.glasstowerstudios.intellij.intellitagger.settings;

import com.intellij.ide.util.PropertiesComponent;

/**
 * Helper class to retrieve and modify settings specific to Intellitagger plugin.
 */
public class SettingsHelper {
    private static final String POSSIBLE_LOGTAG_SETTINGS_KEY = "possibleLogtagNames";
    private static final String LOGTAG_VARIABLE_NAME_SETTINGS_KEY = "logtagVariableName";

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
}
