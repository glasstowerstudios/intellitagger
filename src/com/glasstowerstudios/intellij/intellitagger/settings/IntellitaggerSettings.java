package com.glasstowerstudios.intellij.intellitagger.settings;

import com.glasstowerstudios.intellij.intellitagger.resources.IntellitaggerBundle;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.text.StringUtil;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Controller for settings UI of the Intellitagger plugin.
 */
public class IntellitaggerSettings  implements Configurable {
    private JPanel mRootContainerPanel;
    private JLabel mFormDescriptionLabel;
    private JLabel mPossibleTagNamesTitle;
    private JTextField mPossibleTagNamesField;
    private JLabel mVariableNameTitle;
    private JTextField mVariableNameField;
    private JLabel mUndefinedReferencePolicyTitle;
    private ButtonGroup mUndefinedReferencePolicyGroup;
    private JRadioButton mAdjustReferencesRadioButton;
    private JRadioButton mAdjustVariableNameRadioButton;
    private JRadioButton mNoAdjustmentRadioButton;

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
        mFormDescriptionLabel.setText(IntellitaggerBundle.message("settings.description"));
        mPossibleTagNamesTitle.setText(
          IntellitaggerBundle.message("settings.possibleTagIdentifiers.title"));
        mPossibleTagNamesField.setToolTipText(
          IntellitaggerBundle.message("settings.possibleTagIdentifiers.tooltip"));
        mVariableNameTitle.setText(IntellitaggerBundle.message("settings.variableName.title"));
        mVariableNameField.setToolTipText(IntellitaggerBundle.message("settings.variableName.tooltip"));
        updatePossibleLogtagsFromSettings();
        updateLogtagVariableNameFromSettings();

        mUndefinedReferencePolicyTitle.setText(
          IntellitaggerBundle.message("settings.undefinedReference.title"));
      mUndefinedReferencePolicyTitle.setToolTipText(
        IntellitaggerBundle.message("settings.undefinedReference.tooltip"));
      mAdjustReferencesRadioButton.setText(
          IntellitaggerBundle.message("settings.undefinedReference.adjustReferenceOption.title"));
        mAdjustReferencesRadioButton.setToolTipText(
          IntellitaggerBundle.message("settings.undefinedReference.adjustReferenceOption.tooltip"));
        mAdjustVariableNameRadioButton.setText(
          IntellitaggerBundle.message("settings.undefinedReference.adjustVariableNameOption.title"));
        mAdjustVariableNameRadioButton.setToolTipText(
          IntellitaggerBundle.message("settings.undefinedReference.adjustVariableNameOption.tooltip"));
        mNoAdjustmentRadioButton.setText(
          IntellitaggerBundle.message("settings.undefinedReference.noAdjustmentOption.title"));
        mNoAdjustmentRadioButton.setToolTipText(
          IntellitaggerBundle.message("settings.undefinedReference.noAdjustmentOption.tooltip"));

        mUndefinedReferencePolicyGroup = new ButtonGroup();
        mUndefinedReferencePolicyGroup.add(mAdjustReferencesRadioButton);
        mUndefinedReferencePolicyGroup.add(mAdjustVariableNameRadioButton);
        mUndefinedReferencePolicyGroup.add(mNoAdjustmentRadioButton);

        setUIFromUndefinedReferencePolicy(SettingsHelper.getUndefinedReferencePolicy());
        return mRootContainerPanel;
    }

    @Override
    public boolean isModified() {
        return !getPossibleLogtagsFromSettingsAsCSVString().equals(mPossibleTagNamesField.getText())
                || !SettingsHelper.getLogtagVariableName().equals(mVariableNameField.getText().trim())
                || SettingsHelper.getUndefinedReferencePolicy() != getUndefinedReferencePolicyFromUI();
    }

    @Override
    public void apply() throws ConfigurationException {
        String[] logtagsArray = parsePossibleLogtagCSVStringIntoArray();
        SettingsHelper.setPossibleLogtagNames(logtagsArray);
        SettingsHelper.setLogtagVariableName(mVariableNameField.getText().trim());
        SettingsHelper.setUndefinedReferencePolicy(getUndefinedReferencePolicyFromUI());
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
        mVariableNameField.setText(SettingsHelper.getLogtagVariableName());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void setUIFromUndefinedReferencePolicy(SettingsHelper.UndefinedReferencePolicy aPolicy) {
        switch (aPolicy) {
            case ADJUST_VARIABLE_NAME:
                mAdjustVariableNameRadioButton.setSelected(true);
                break;

            case ADJUST_REFERENCES:
                mAdjustReferencesRadioButton.setSelected(true);
                break;

            case NO_ADJUSTMENT:
            default:
                mNoAdjustmentRadioButton.setSelected(true);
                break;
        }
    }

    private SettingsHelper.UndefinedReferencePolicy getUndefinedReferencePolicyFromUI() {
        SettingsHelper.UndefinedReferencePolicy policy;
        if (mAdjustReferencesRadioButton.isSelected()) {
            policy = SettingsHelper.UndefinedReferencePolicy.ADJUST_REFERENCES;
        } else if (mAdjustVariableNameRadioButton.isSelected()) {
            policy = SettingsHelper.UndefinedReferencePolicy.ADJUST_VARIABLE_NAME;
        } else {
            policy = SettingsHelper.UndefinedReferencePolicy.NO_ADJUSTMENT;
        }

        return policy;
    }
}
