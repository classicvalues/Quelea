/* 
 * This file is part of Quelea, free projection software for churches.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITYs or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.quelea.windows.options;

import java.io.File;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import org.quelea.data.powerpoint.OOPresentation;
import org.quelea.data.powerpoint.OOUtils;
import org.quelea.services.languages.LabelGrabber;
import org.quelea.services.languages.LanguageFile;
import org.quelea.services.languages.LanguageFileManager;
import org.quelea.services.utils.PropertyPanel;
import org.quelea.services.utils.QueleaProperties;
import org.quelea.windows.main.QueleaApp;

/**
 * A panel where the general options in the program are set.
 * <p/>
 * @author Michael
 */
public class OptionsGeneralPanel extends GridPane implements PropertyPanel {

    private final CheckBox startupUpdateCheckBox;
    private final CheckBox capitalFirstCheckBox;
    private final CheckBox oneMonitorWarnCheckBox;
    private final CheckBox oneLineModeCheckBox;
    private final CheckBox autoTranslateCheckBox;
    private final CheckBox clearLiveOnRemoveCheckBox;
    private final CheckBox useOOCheckBox;
    private final CheckBox autoPlayVidCheckBox;
    private final CheckBox uniformFontSizeCheckBox;
    private final ComboBox<LanguageFile> languageFileComboBox;
    private final TextField ooPathTextField;
    private final DirectoryChooser ooChooser;
    private final Button selectButton;
    private final Slider maximumFontSizeSlider;
    private final Slider additionalLineSpacingSlider;
    private final Slider maxCharsSlider;
//    private final Slider minLinesSlider;
    private LanguageFile currentLanguageFile;
    private final CheckBox showSmallSongTextBox;
    private final CheckBox showSmallBibleTextBox;
    private final ComboBox smallTextPositionCombo;

    /**
     * Create a new general panel.
     */
    public OptionsGeneralPanel() {
        int rows = 0;
        setVgap(5);
        setHgap(10);
        setPadding(new Insets(5));

//        Label spacer = new Label("");
//        GridPane.setConstraints(spacer, 1, rows);
//        getChildren().add(spacer);
//        rows++;

        Label userOptions = new Label(LabelGrabber.INSTANCE.getLabel("user.options.options"));
        userOptions.setFont(Font.font(userOptions.getFont().getFamily(), FontWeight.BOLD, userOptions.getFont().getSize()));
        GridPane.setConstraints(userOptions, 1, rows);
        getChildren().add(userOptions);
        rows++;

        Label interfaceLanguageLabel = new Label(LabelGrabber.INSTANCE.getLabel("interface.language.label"));
        GridPane.setConstraints(interfaceLanguageLabel, 1, rows);
        getChildren().add(interfaceLanguageLabel);
        languageFileComboBox = new ComboBox<>();
        for (LanguageFile file : LanguageFileManager.INSTANCE.languageFiles()) {
            languageFileComboBox.getItems().add(file);
        }
        interfaceLanguageLabel.setLabelFor(languageFileComboBox);
        GridPane.setConstraints(languageFileComboBox, 2, rows);
        getChildren().add(languageFileComboBox);
        rows++;

        Label startupLabel = new Label(LabelGrabber.INSTANCE.getLabel("check.for.update.label"));
        GridPane.setConstraints(startupLabel, 1, rows);
        getChildren().add(startupLabel);
        startupUpdateCheckBox = new CheckBox();
        startupLabel.setLabelFor(startupUpdateCheckBox);
        GridPane.setConstraints(startupUpdateCheckBox, 2, rows);
        getChildren().add(startupUpdateCheckBox);
        rows++;

        Label warnLabel = new Label(LabelGrabber.INSTANCE.getLabel("1.monitor.warn.label"));
        GridPane.setConstraints(warnLabel, 1, rows);
        getChildren().add(warnLabel);
        oneMonitorWarnCheckBox = new CheckBox();
        warnLabel.setLabelFor(oneMonitorWarnCheckBox);
        GridPane.setConstraints(oneMonitorWarnCheckBox, 2, rows);
        getChildren().add(oneMonitorWarnCheckBox);
        rows++;

        Label useOOLabel = new Label(LabelGrabber.INSTANCE.getLabel("use.oo.label"));
        GridPane.setConstraints(useOOLabel, 1, rows);
        getChildren().add(useOOLabel);
        useOOCheckBox = new CheckBox();
        useOOCheckBox.selectedProperty().addListener(new javafx.beans.value.ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (useOOCheckBox.isSelected()) {
                    ooPathTextField.setDisable(true);
                    selectButton.setDisable(false);
                } else {
                    ooPathTextField.setDisable(true);
                    selectButton.setDisable(true);
                }
            }
        });
        useOOLabel.setLabelFor(useOOCheckBox);
        GridPane.setConstraints(useOOCheckBox, 2, rows);
        getChildren().add(useOOCheckBox);
        rows++;

        Label ooPathLabel = new Label(LabelGrabber.INSTANCE.getLabel("oo.path"));
        GridPane.setConstraints(ooPathLabel, 1, rows);
        getChildren().add(ooPathLabel);
        ooPathTextField = new TextField();
        ooPathTextField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(ooPathTextField, Priority.ALWAYS);
        ooPathTextField.setEditable(false);
        ooPathLabel.setLabelFor(ooPathTextField);
        GridPane.setConstraints(ooPathTextField, 2, rows);
        getChildren().add(ooPathTextField);
        ooChooser = new DirectoryChooser();
        selectButton = new Button(LabelGrabber.INSTANCE.getLabel("browse"));
        selectButton.setDisable(true);
        selectButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent t) {
                File dir = ooChooser.showDialog(QueleaApp.get().getMainWindow());
                if (dir != null) {
                    ooPathTextField.setText(dir.getAbsolutePath());
                }
            }
        });
        GridPane.setConstraints(selectButton, 3, rows);
        getChildren().add(selectButton);
        rows++;

        Label oneLineModeLabel = new Label(LabelGrabber.INSTANCE.getLabel("one.line.mode.label"));
        GridPane.setConstraints(oneLineModeLabel, 1, rows);
        getChildren().add(oneLineModeLabel);
        oneLineModeCheckBox = new CheckBox();
        startupLabel.setLabelFor(oneLineModeCheckBox);
        GridPane.setConstraints(oneLineModeCheckBox, 2, rows);
        getChildren().add(oneLineModeCheckBox);
        rows++;

        Label autoPlayVidLabel = new Label(LabelGrabber.INSTANCE.getLabel("autoplay.vid.label"));
        GridPane.setConstraints(autoPlayVidLabel, 1, rows);
        getChildren().add(autoPlayVidLabel);
        autoPlayVidCheckBox = new CheckBox();
        startupLabel.setLabelFor(autoPlayVidCheckBox);
        GridPane.setConstraints(autoPlayVidCheckBox, 2, rows);
        getChildren().add(autoPlayVidCheckBox);
        rows++;

        Label autoTranslateLabel = new Label(LabelGrabber.INSTANCE.getLabel("auto.translate.label"));
        GridPane.setConstraints(autoTranslateLabel, 1, rows);
        getChildren().add(autoTranslateLabel);
        autoTranslateCheckBox = new CheckBox();
        startupLabel.setLabelFor(autoTranslateCheckBox);
        GridPane.setConstraints(autoTranslateCheckBox, 2, rows);
        getChildren().add(autoTranslateCheckBox);
        rows++;

        Label clearLiveOnRemoveLabel = new Label(LabelGrabber.INSTANCE.getLabel("clear.live.on.remove.schedule") + " ");
        GridPane.setConstraints(clearLiveOnRemoveLabel, 1, rows);
        getChildren().add(clearLiveOnRemoveLabel);
        clearLiveOnRemoveCheckBox = new CheckBox();
        startupLabel.setLabelFor(clearLiveOnRemoveCheckBox);
        GridPane.setConstraints(clearLiveOnRemoveCheckBox, 2, rows);
        getChildren().add(clearLiveOnRemoveCheckBox);
        rows++;

        Label showSmallSongTextLabel = new Label(LabelGrabber.INSTANCE.getLabel("show.small.song.text.label"));
        GridPane.setConstraints(showSmallSongTextLabel, 1, rows);
        getChildren().add(showSmallSongTextLabel);
        showSmallSongTextBox = new CheckBox();
        GridPane.setConstraints(showSmallSongTextBox, 2, rows);
        getChildren().add(showSmallSongTextBox);
        showSmallSongTextLabel.setLabelFor(showSmallSongTextBox);
        rows++;

        Label showSmallBibleTextLabel = new Label(LabelGrabber.INSTANCE.getLabel("show.small.bible.text.label"));
        GridPane.setConstraints(showSmallBibleTextLabel, 1, rows);
        getChildren().add(showSmallBibleTextLabel);
        showSmallBibleTextBox = new CheckBox();
        GridPane.setConstraints(showSmallBibleTextBox, 2, rows);
        getChildren().add(showSmallBibleTextBox);
        showSmallBibleTextLabel.setLabelFor(showSmallBibleTextBox);
        rows++;

        Label smallTextPositionLabel = new Label(LabelGrabber.INSTANCE.getLabel("small.text.position.label"));
        GridPane.setConstraints(smallTextPositionLabel, 1, rows);
        getChildren().add(smallTextPositionLabel);
        smallTextPositionCombo = new ComboBox();
        smallTextPositionCombo.getItems().addAll(LabelGrabber.INSTANCE.getLabel("left"), LabelGrabber.INSTANCE.getLabel("right"));
        GridPane.setConstraints(smallTextPositionCombo, 2, rows);
        getChildren().add(smallTextPositionCombo);
        smallTextPositionLabel.setLabelFor(smallTextPositionCombo);
        rows++;

        Label spacer1 = new Label("");
        GridPane.setConstraints(spacer1, 1, rows);
        getChildren().add(spacer1);
        rows++;

        Label textOptions = new Label(LabelGrabber.INSTANCE.getLabel("text.options.options"));
        textOptions.setFont(Font.font(textOptions.getFont().getFamily(), FontWeight.BOLD, textOptions.getFont().getSize()));
        GridPane.setConstraints(textOptions, 1, rows);
        getChildren().add(textOptions);
        rows++;

        Label capitalFirstLabel = new Label(LabelGrabber.INSTANCE.getLabel("capitalise.start.line.label"));
        GridPane.setConstraints(capitalFirstLabel, 1, rows);
        getChildren().add(capitalFirstLabel);
        capitalFirstCheckBox = new CheckBox();
        startupLabel.setLabelFor(capitalFirstCheckBox);
        GridPane.setConstraints(capitalFirstCheckBox, 2, rows);
        getChildren().add(capitalFirstCheckBox);
        rows++;

        Label uniformFontSizeLabel = new Label(LabelGrabber.INSTANCE.getLabel("uniform.font.size.label"));
        GridPane.setConstraints(uniformFontSizeLabel, 1, rows);
        getChildren().add(uniformFontSizeLabel);
        uniformFontSizeCheckBox = new CheckBox();
        startupLabel.setLabelFor(uniformFontSizeCheckBox);
        GridPane.setConstraints(uniformFontSizeCheckBox, 2, rows);
        getChildren().add(uniformFontSizeCheckBox);
        rows++;

        Label maxFontSizeLabel = new Label(LabelGrabber.INSTANCE.getLabel("max.font.size.label"));
        GridPane.setConstraints(maxFontSizeLabel, 1, rows);
        getChildren().add(maxFontSizeLabel);
        maximumFontSizeSlider = new Slider(12, 1000, 1000);
        GridPane.setConstraints(maximumFontSizeSlider, 2, rows);
        getChildren().add(maximumFontSizeSlider);
        maxFontSizeLabel.setLabelFor(maximumFontSizeSlider);
        final Label maxFontSizeValue = new Label(Integer.toString((int) maximumFontSizeSlider.getValue()));
        GridPane.setConstraints(maxFontSizeValue, 3, rows);
        getChildren().add(maxFontSizeValue);
        maxFontSizeValue.setLabelFor(maximumFontSizeSlider);
        maximumFontSizeSlider.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                maxFontSizeValue.setText(Integer.toString((int) maximumFontSizeSlider.getValue()));
            }
        });
        rows++;

        Label additionalLineSpacingLabel = new Label(LabelGrabber.INSTANCE.getLabel("additional.line.spacing.label"));
        GridPane.setConstraints(additionalLineSpacingLabel, 1, rows);
        getChildren().add(additionalLineSpacingLabel);
        additionalLineSpacingSlider = new Slider(0, 50, 10);
        GridPane.setConstraints(additionalLineSpacingSlider, 2, rows);
        getChildren().add(additionalLineSpacingSlider);
        maxFontSizeLabel.setLabelFor(additionalLineSpacingSlider);
        final Label additionalLineSpacingValue = new Label(Integer.toString((int) additionalLineSpacingSlider.getValue()));
        GridPane.setConstraints(additionalLineSpacingValue, 3, rows);
        getChildren().add(additionalLineSpacingValue);
        additionalLineSpacingValue.setLabelFor(additionalLineSpacingSlider);
        additionalLineSpacingSlider.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                additionalLineSpacingValue.setText(Integer.toString((int) additionalLineSpacingSlider.getValue()));
            }
        });
        rows++;

        Label maxCharsLabel = new Label(LabelGrabber.INSTANCE.getLabel("max.chars.line.label"));
        GridPane.setConstraints(maxCharsLabel, 1, rows);
        getChildren().add(maxCharsLabel);
        maxCharsSlider = new Slider(10, 160, 0);
        GridPane.setConstraints(maxCharsSlider, 2, rows);
        getChildren().add(maxCharsSlider);
        maxCharsLabel.setLabelFor(maxCharsSlider);
        final Label maxCharsValue = new Label(Integer.toString((int) maxCharsSlider.getValue()));
        GridPane.setConstraints(maxCharsValue, 3, rows);
        getChildren().add(maxCharsValue);
        maxCharsValue.setLabelFor(maxCharsSlider);
        maxCharsSlider.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                maxCharsValue.setText(Integer.toString((int) maxCharsSlider.getValue()));
            }
        });
        rows++;

//        Label minLinesLabel = new Label(LabelGrabber.INSTANCE.getLabel("min.emulated.lines.label") + " (" + LabelGrabber.INSTANCE.getLabel("advanced.label") + ")");
//        GridPane.setConstraints(minLinesLabel, 1, rows);
//        getChildren().add(minLinesLabel);
//        minLinesSlider = new Slider(1, 20, 0);
//        GridPane.setConstraints(minLinesSlider, 2, rows);
//        getChildren().add(minLinesSlider);
//        minLinesLabel.setLabelFor(minLinesSlider);
//        final Label minLinesValue = new Label(Integer.toString((int) minLinesSlider.getValue()));
//        GridPane.setConstraints(minLinesValue, 3, rows);
//        getChildren().add(minLinesValue);
//        minLinesValue.setLabelFor(minLinesSlider);
//        minLinesSlider.valueProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
//                minLinesValue.setText(Integer.toString((int) minLinesSlider.getValue()));
//            }
//        });
//        rows++;
        readProperties();
    }

    /**
     * Reset the mechanism for determining if the user has changed the interface
     * language. Call before showing the options dialog.
     */
    public void resetLanguageChanged() {
        currentLanguageFile = languageFileComboBox.getValue();
    }

    /**
     * Determine if the user has changed the interface language since the last
     * call of resetLanguageChanged().
     */
    public boolean hasLanguageChanged() {
        return !languageFileComboBox.getValue().equals(currentLanguageFile);
    }

    /**
     * @inheritDoc
     */
    @Override
    public final void readProperties() {
        QueleaProperties props = QueleaProperties.get();
        languageFileComboBox.setValue(LanguageFileManager.INSTANCE.getCurrentFile());
        startupUpdateCheckBox.setSelected(props.checkUpdate());
        useOOCheckBox.setSelected(props.getUseOO());
        ooPathTextField.setText(props.getOOPath());
        capitalFirstCheckBox.setSelected(props.checkCapitalFirst());
        oneMonitorWarnCheckBox.setSelected(props.showSingleMonitorWarning());
        uniformFontSizeCheckBox.setSelected(props.getUseUniformFontSize());
        oneLineModeCheckBox.setSelected(props.getOneLineMode());
        autoTranslateCheckBox.setSelected(props.getAutoTranslate());
        autoPlayVidCheckBox.setSelected(props.getAutoPlayVideo());
        clearLiveOnRemoveCheckBox.setSelected(props.getClearLiveOnRemove());
        maxCharsSlider.setValue(props.getMaxChars());
//        minLinesSlider.setValue(props.getMinLines());
        showSmallSongTextBox.setSelected(props.getSmallSongTextShow());
        showSmallBibleTextBox.setSelected(props.getSmallBibleTextShow());
        smallTextPositionCombo.getSelectionModel().select(props.getSmallTextPosition().equals("left") ? 0 : 1);
        additionalLineSpacingSlider.setValue(props.getAdditionalLineSpacing());
        maximumFontSizeSlider.setValue(props.getMaxFontSize());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setProperties() {
        QueleaProperties props = QueleaProperties.get();
        props.setLanguageFile(languageFileComboBox.getValue().getFile().getName());
        boolean checkUpdate = getStartupUpdateCheckBox().isSelected();
        props.setCheckUpdate(checkUpdate);
        boolean useOO = getUseOOCheckBox().isSelected();
        props.setUseOO(useOO);
        String ooPath = getOOPathTextField().getText();
        props.setOOPath(ooPath);
        boolean showWarning = getOneMonitorWarningCheckBox().isSelected();
        props.setSingleMonitorWarning(showWarning);
        boolean checkCapital = getCapitalFirstCheckBox().isSelected();
        props.setCapitalFirst(checkCapital);
        boolean useUniformFontSize = uniformFontSizeCheckBox.isSelected();
        props.setUseUniformFontSize(useUniformFontSize);
        boolean clearLive = clearLiveOnRemoveCheckBox.isSelected();
        props.setClearLiveOnRemove(clearLive);
        boolean oneLineMode = getOneLineModeCheckBox().isSelected();
        props.setOneLineMode(oneLineMode);
        boolean autoTranslate = getAutoTranslateCheckBox().isSelected();
        props.setAutoTranslate(autoTranslate);
        boolean autoPlayVid = autoPlayVidCheckBox.isSelected();
        props.setAutoPlayVideo(autoPlayVid);
        //One line mode needs to be updated manually
        QueleaApp.get().getMainWindow().getMainPanel().getPreviewPanel().updateOneLineMode();
        QueleaApp.get().getMainWindow().getMainPanel().getLivePanel().updateOneLineMode();
        int maxCharsPerLine = (int) getMaxCharsSlider().getValue();
        props.setMaxChars(maxCharsPerLine);
//        int minLines = (int) getMinLinesSlider().getValue();
//        props.setMinLines(minLines);
        boolean showSmallSongText = showSmallSongTextBox.isSelected();
        props.setSmallSongTextShow(showSmallSongText);
        boolean showSmallBibleText = showSmallBibleTextBox.isSelected();
        props.setSmallBibleTextShow(showSmallBibleText);
        int smallTextPosition = smallTextPositionCombo.getSelectionModel().getSelectedIndex();
        props.setSmallTextPosition(smallTextPosition == 0 ? "left" : "right");
        props.setMaxFontSize(maximumFontSizeSlider.getValue());
        props.setAdditionalLineSpacing(additionalLineSpacingSlider.getValue());
        //Initialise presentation
        if (!OOPresentation.isInit()) {
            OOUtils.attemptInit();
        }
    }

    /**
     * Get the max chars slider.
     * <p/>
     * @return the max chars slider.
     */
    public Slider getMaxCharsSlider() {
        return maxCharsSlider;
    }

//    /**
//     * Get the min lines slider.
//     * <p/>
//     * @return the min lines slider.
//     */
//    public Slider getMinLinesSlider() {
//        return minLinesSlider;
//    }
    /**
     * Get the startup readProperties checkbox.
     * <p/>
     * @return the startup readProperties checkbox.
     */
    public CheckBox getStartupUpdateCheckBox() {
        return startupUpdateCheckBox;
    }

    /**
     * Get the capitalise first character in each line checkbox.
     * <p/>
     * @return the capitalise first character in each line checkbox.
     */
    public CheckBox getCapitalFirstCheckBox() {
        return capitalFirstCheckBox;
    }

    /**
     * Get the "one monitor warning" checkbox.
     * <p/>
     * @return the "one monitor warning" checkbox.
     */
    public CheckBox getOneMonitorWarningCheckBox() {
        return oneMonitorWarnCheckBox;
    }

    /**
     * Get the "one line mode" checkbox.
     * <p/>
     * @return the "one line mode" checkbox.
     */
    public CheckBox getOneLineModeCheckBox() {
        return oneLineModeCheckBox;
    }

    /**
     * Get the "auto translate" checkbox.
     * <p/>
     * @return the "auto translate" checkbox.
     */
    public CheckBox getAutoTranslateCheckBox() {
        return autoTranslateCheckBox;
    }

    /**
     * Get the "use openoffice" checkbox.
     * <p/>
     * @return the "use openoffice" checkbox.
     */
    public CheckBox getUseOOCheckBox() {
        return useOOCheckBox;
    }

    /**
     * Get the "openoffice path" text field.
     * <p/>
     * @return the "openoffice path" text field.
     */
    public TextField getOOPathTextField() {
        return ooPathTextField;
    }

    /**
     * Get the "use small song text" checkbox.
     * <p/>
     * @return the "use small song text" checkbox.
     */
    public CheckBox getShowSmallSongTextCheckBox() {
        return showSmallSongTextBox;
    }

    /**
     * Get the "use small bible text" checkbox.
     * <p/>
     * @return the "use small bible text" checkbox.
     */
    public CheckBox getShowSmallBibleTextCheckBox() {
        return showSmallBibleTextBox;
    }

    /**
     * Get the "use small text" checkbox.
     * <p/>
     * @return the "use small text" checkbox.
     */
    public ComboBox getSmallTextPositionComboBox() {
        return smallTextPositionCombo;
    }
}
