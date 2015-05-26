package com.glasstowerstudios.intellij.intellitagger.settings;

import com.glasstowerstudios.intellij.intellitagger.resources.IntellitaggerBundle;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Controller for settings UI of the Intellitagger plugin.
 */
public class IntellitaggerSettings  implements Configurable {
    private JPanel mRootContainerPanel;
    private JTextField mPossibleTagNamesField;
    private JTextField mLogtagNameField;

    @Nls
    @Override
    public String getDisplayName() {
        return IntellitaggerBundle.message("settings.display.name");
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return IntellitaggerBundle.message("settings.help.topic");
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        updatePossibleLogtagsFromSettings();
        updateLogtagVariableNameFromSettings();
        return mRootContainerPanel;
    }

    @Override
    public boolean isModified() {
        return !getPossibleLogtagsFromSettingsAsCSVString().equals(mPossibleTagNamesField.getText())
                || !SettingsHelper.getLogtagVariableName().equals(mLogtagNameField.getText().trim());
    }

    @Override
    public void apply() throws ConfigurationException {
        String[] logtagsArray = parsePossibleLogtagCSVStringIntoArray();
        SettingsHelper.setPossibleLogtagNames(logtagsArray);
        SettingsHelper.setLogtagVariableName(mLogtagNameField.getText().trim());
    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

    private String[] parsePossibleLogtagCSVStringIntoArray() {
        String possibleLogtagNames = mPossibleTagNamesField.getText();
        List<String> possibleTagStrings = StringUtil.split(possibleLogtagNames, ",");
        String[] logtagsArray = new String[possibleTagStrings.size()];
        int i = 0;
        for (String nextPossibleTag : possibleTagStrings) {
            logtagsArray[i] = nextPossibleTag.trim();
            i++;
        }

        return logtagsArray;
    }

    private String getPossibleLogtagsFromSettingsAsCSVString() {
        StringBuilder logtagBuilder = new StringBuilder();
        String[] possibleLogtags = SettingsHelper.getPossibleLogtagNames();
        if (possibleLogtags.length == 0) {
            return "";
        }

        for (String nextLogtag : possibleLogtags) {
            logtagBuilder.append(nextLogtag + ", ");
        }

        String finalLogtagsString = logtagBuilder.toString().trim();
        return finalLogtagsString.substring(0, finalLogtagsString.length() - 1);
    }

    private void updatePossibleLogtagsFromSettings() {
        mPossibleTagNamesField.setText(getPossibleLogtagsFromSettingsAsCSVString());
    }

    private void updateLogtagVariableNameFromSettings() {
        mLogtagNameField.setText(SettingsHelper.getLogtagVariableName());
    }
}
