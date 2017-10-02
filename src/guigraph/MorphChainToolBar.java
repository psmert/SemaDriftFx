/*
 * Copyright 2016 staxos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guigraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;

/**
 *
 * @author staxos
 */
public class MorphChainToolBar extends HBox{
    private MorphingChainGUI morphingChainGUI;
    
    private ComboBox fromComboBox;
    private ComboBox labelComboBox;
    private ComboBox toComboBox;
    private ComboBox pairTypeComboBox;
    private HBox fromHBox;
    private HBox toHBox;
    private HBox threshHBox;
    private Spinner<Double> spinner;
    private MyButton buttonRefresh;
    private MyButton buttonSave;
    
            
    private int fromIdx;
    private int toIdx;
    private int labelIdx;
    private int pairTypeIdx;
    private double thresholdValue;
    private double threshMinValue;
    private double threshMaxValue;
    
    
    ArrayList<String> OntDetails;
    ArrayList<PairData> pairData;
    ArrayList<String> pairTypes;
    
    
    
    public int getLabelIdx(){
        return this.labelIdx;
    }
    
    public double getThreshold(){
        return this.thresholdValue;
    }
    
    public int getFromIdx(){
        return this.fromIdx;
    }
    
    public int getToIdx(){
        return this.toIdx;
    }
    
    public void setButtonReFreshHighlighted( boolean val){
        if ( val ){
        if ( this.buttonRefresh != null){
            this.buttonRefresh.setID("highlighted");
        }
        }
    }
    
    
    private boolean successfullyFilled(){
        if ( this.fromIdx == -1){
            System.out.println("NOT SUCCESSFULLY FILLED, invalid fromIdx");
            return false;
        }
        
        if ( this.toIdx == -1){
            System.out.println("NOT SUCCESSFULLY FILLED, invalid toIdx");
            return false;
        }
        
        if ( this.labelIdx == -1){
            System.out.println("NOT SUCCESSFULLY FILLED, invalid labelIdx");
            return false;
        }
        
        if ( this.pairTypeIdx == -1){
            System.out.println("NOT SUCCESSFULLY FILLED, invalid pairTypeIdx");
            return false;
            
        }
        
        if ( this.thresholdValue< this.threshMinValue || this.thresholdValue> this.threshMaxValue ){
            
            System.out.println("NOT SUCCESSFULLY FILLED, invalid spinnervalue");
            return false;
        }
        
        return true;
    }
    
    
    public void setThresholdValue( double spinnerVal){        
        this.thresholdValue = spinnerVal;
        System.out.println("MorphChainToolBar object spinnerValue:"+this.thresholdValue);
    }
    
    
    private void initializeSpinnerHBox(double minValue, double maxValue, double initialValue, double amountToStepBy){
        this.threshHBox = new HBox();
        this.threshHBox.setSpacing(5);
        
        VBox labelVBox = new VBox();
        Label labelThresh = new Label("Threshold:");
        labelThresh.setFont(Font.font("Segoe", FontWeight.NORMAL, 12));
        labelVBox.getChildren().add(labelThresh);
        labelVBox.setAlignment(Pos.CENTER_RIGHT);
        this.threshHBox.getChildren().add(labelVBox);
        
        
        this.spinner = new Spinner<Double>();     
        
        VBox spinBox = new VBox();
 
        // Value factory.
        SpinnerValueFactory<Double> valueFactory = //
                new SpinnerValueFactory.DoubleSpinnerValueFactory(minValue, maxValue, initialValue, amountToStepBy){
                    @Override
                    public void decrement(int steps) {
                        this.setValue(this.getValue()-amountToStepBy);
                        setThresholdValue( this.getValue().doubleValue() );
                        checkToolBarStatus();
                    }
 
                    @Override
                    public void increment(int steps) {
                        this.setValue(this.getValue()+amountToStepBy);
                        setThresholdValue( this.getValue().doubleValue() );
                        checkToolBarStatus();
                    }
                };
        this.spinner.setValueFactory(valueFactory);        
        this.spinner.setEditable(true);
        this.spinner.getEditor().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String text = spinner.getEditor().getText();
                SpinnerValueFactory<Double> valueFactory = spinner.getValueFactory();
                StringConverter<Double> converter = valueFactory.getConverter();
                Double enterValue = converter.fromString(text);
                //System.out.println("Value in spinner is "+enterValue);
                if (enterValue > maxValue){
                    enterValue = maxValue;
                    spinner.getValueFactory().setValue(maxValue);
                }
                if( enterValue <minValue){
                    enterValue = minValue;
                    spinner.getValueFactory().setValue(minValue);
                }
                
                setThresholdValue(enterValue.doubleValue());
                
                checkToolBarStatus();
                        
            }
        });
        this.thresholdValue = this.spinner.getValueFactory().getValue();
        spinBox.setAlignment(Pos.CENTER);
        spinBox.getChildren().add(spinner);
        this.threshHBox.getChildren().add(spinBox);
        
        
        this.getChildren().add(this.threshHBox);
    }
    
    
    
    
    private ComboBox createComboBox(ArrayList<String> optionList, String setValueString) {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (int i = 0; i < optionList.size(); i++) {
            options.add(optionList.get(i));
        }
        final ComboBox comboBox = new ComboBox(options);
        comboBox.setStyle("-fx-font: 9pt \"Segoe\"");

        comboBox.setValue(setValueString);
        comboBox.setMaxHeight(Double.MAX_VALUE);
        return comboBox;
    }
    
    
    public int getIndexInStringArrayList(ArrayList<String> arrayList, String s){
        for ( int i = 0 ; i < arrayList.size(); i++){
            if (arrayList.get(i).equals(s)){
                return i;
            }
        }
        return -1;
    }
    
    
    /*
    private void resetComboBoxOptions( Pane container, ComboBox comboBox, ArrayList<String> newOptions, String comboBoxType){
        int comboBoxIndexInPanel = container.getChildren().indexOf(comboBox);
        if ( comboBoxIndexInPanel != -1){
            System.out.println("found combobox!");
            container.getChildren().remove(comboBox);
        } else{
            System.out.println("Did not find combobox!");
        }
        
        
        if ( comboBoxType.equals("TO_COMBOBOX")){
            comboBox = getToComboBox(this.OntDetails, "Select Ontology Version");
        }
        else if ( comboBoxType.equals("LABEL_COMBOBOX")){
            comboBox = getLabelComboBox(pairData.get(fromIdx).labelPairData.rowNames, "Select a "+  this.OntDetails.get(this.fromIdx) + " Label") ;
        } else{
            System.out.println("MorphChainToolBar.resetComboBoxOptions() ERROR!  comboBoxType:"+comboBoxType+" is invalid!");
        }
        
        
        comboBox.getItems().clear();
        ObservableList<String> comboNewOptions = FXCollections.observableArrayList();
        for ( int i = 0 ; i < newOptions.size(); i++){
            comboNewOptions.add(newOptions.get(i));
        }
        comboBox.getItems().addAll(comboNewOptions);
        if ( comboBoxIndexInPanel != -1){
            container.getChildren().add(comboBoxIndexInPanel, comboBox);
        }
        else {
            container.getChildren().add(comboBox);
        }
        comboBox.setDisable(false);
        
    }*/
    
    
    private static void configureSaveFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Save Snapshot as...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }
    
    
    private void saveButtonAction(){
        WritableImage image = this.morphingChainGUI.getScene().snapshot(null);
        FileChooser fileChooser = new FileChooser();
        configureSaveFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(this.morphingChainGUI);
        try {
            if ( file != null){
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }
    
    
    
    private void setFromIdx(String selectedValue){
        
        this.fromIdx = getIndexInStringArrayList(this.OntDetails, selectedValue);
        
        //System.out.println("SELECTED FROM:" +selectedValue+ " in index:" + this.fromIdx );
        
        //reset toComboBox 
        this.toIdx = -1;
        int comboBoxIndexInPanel = this.toHBox.getChildren().indexOf(this.toComboBox);
        if ( comboBoxIndexInPanel != -1){
            this.toHBox.getChildren().remove(this.toComboBox);
        }
        this.toComboBox = getToComboBox(this.OntDetails, "Select Ontology Version");
        this.toComboBox.getItems().clear();
        ObservableList<String> toOptions = FXCollections.observableArrayList();
        for ( int i = fromIdx+1 ; i < this.OntDetails.size(); i++){
            toOptions.add(this.OntDetails.get(i));
        }
        this.toComboBox.getItems().addAll(toOptions);
        if ( comboBoxIndexInPanel != -1){
            this.toHBox.getChildren().add(comboBoxIndexInPanel, this.toComboBox);
        }
        else {
            this.toHBox.getChildren().add(this.toComboBox);
        }
        this.toComboBox.setDisable(false);
        
        
        
        //reset labelComboBox
        this.labelIdx = -1;
        int labelBoxIndexInPanel = this.getChildren().indexOf(this.labelComboBox);
        if ( labelBoxIndexInPanel != -1){
            this.getChildren().remove(this.labelComboBox);
        }
        this.labelComboBox = getLabelComboBox(pairData.get(fromIdx).labelPairData.rowNames, "Select a "+  this.OntDetails.get(this.fromIdx) + " Concept") ;
        this.labelComboBox.getItems().clear();
        ObservableList<String> options = FXCollections.observableArrayList();
        for (int i = 0; i < this.pairData.get(this.fromIdx).labelPairData.rowNames.size(); i++) {
            options.add(this.pairData.get(this.fromIdx).labelPairData.rowNames.get(i));
        }
        this.labelComboBox.getItems().addAll(options);
        if ( labelBoxIndexInPanel != -1){
        this.getChildren().add(labelBoxIndexInPanel, this.labelComboBox);
        } else {
            this.getChildren().add(this.labelComboBox);
        }
        
        //add pairType comboBox
        if ( this.pairTypeComboBox == null){
            this.pairTypeComboBox = getPairTypeComboBox(this.pairTypes, "Select Aspect");
            ObservableList<String> pairTypeOptions = FXCollections.observableArrayList();
            for (int i = 0; i < pairTypes.size(); i++) {
                pairTypeOptions.add(this.pairTypes.get(i));
                
            }
            this.getChildren().add(this.pairTypeComboBox);
        }
        
        
        //initialize spinner
        double initialValue = 0.4;
        double amountToStepBy = 0.1;
        double minValue = this.threshMinValue;
        double maxValue = this.threshMaxValue;        
        if ( this.getChildren().indexOf(this.threshHBox)==-1){
            initializeSpinnerHBox(minValue, maxValue, initialValue, amountToStepBy);
        }
        
        //add Refresh Button
        if ( this.getChildren().indexOf(this.buttonRefresh)==-1){
            Image imgRef = new Image(getClass().getResourceAsStream("/graphics/refresh32.png"));
            this.buttonRefresh = new MyButton("Refresh", new ImageView(imgRef), false);
            this.buttonRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                refreshButtonAction();
            }
        });
            this.buttonRefresh.setFont(Font.font("Segoe", FontWeight.NORMAL, 12));
            this.getChildren().add(this.buttonRefresh);
        }
        
        
        //add Save Button
        if ( this.getChildren().indexOf(this.buttonSave)==-1){
            Image imgRef = new Image(getClass().getResourceAsStream("/graphics/save.png"));
            this.buttonSave = new MyButton("Save", new ImageView(imgRef), false);
            this.buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                saveButtonAction();
            }
            
        });
            this.buttonSave.setFont(Font.font("Segoe", FontWeight.NORMAL, 12));
            this.getChildren().add(this.buttonSave);
        }
        
        this.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                widthChangeAction();
            }
        } );
    }
    
    private void refreshButtonAction(){
        checkToolBarStatus();
        this.buttonRefresh.setID("");
    }
    
    
    private void setToIdx(String selectedValue){
        //System.out.println("SELECTED TO:" +selectedValue+ " in index:" + this.toIdx );
        
        this.toIdx = getIndexInStringArrayList(this.OntDetails, selectedValue);
    }
    
    private void setLabelIdx(String selectedValue){
        //System.out.println("SELECTED TO:" +selectedValue+ " in index:" + this.toIdx );
        
        this.labelIdx = getIndexInStringArrayList(this.pairData.get(this.fromIdx).labelPairData.rowNames, selectedValue);
    }
    
    
    
    private void setPairTypeIdx(String selectedValue){
        this.pairTypeIdx = getIndexInStringArrayList( this.pairTypes, selectedValue);
    }
    
    
    
    private ComboBox getFromComboBox( ArrayList<String> OntDetails, String setValueString){
        
        
        final ComboBox fromComboBox = createComboBox(new ArrayList(OntDetails.subList(0, OntDetails.size()-1)), setValueString);
        
        fromComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                
                setFromIdx(fromComboBox.getValue().toString());
            }
        });
        
        return fromComboBox;
        
    }
    
    
    
    private ComboBox getPairTypeComboBox( ArrayList<String> arrayList, String setValueString){
        
        
        final ComboBox pairTypeComboBox = createComboBox(arrayList, setValueString);
        
        pairTypeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                
                setPairTypeIdx(pairTypeComboBox.getValue().toString());
                checkToolBarStatus();
            }
        });
        
        return pairTypeComboBox;
        
    }
    
    private void checkToolBarStatus(){
        if( this.successfullyFilled() ){
            System.out.println("MORPH CHAIN FORM IS SUCCESSFULLY FILLED");
            this.morphingChainGUI.buildGraph(this.fromIdx, this.toIdx, this.labelIdx, this.pairTypeIdx, this.thresholdValue, this.pairTypes);
        }
        else {
            System.out.println("FORM IS NOT SUCCESSFULLY FILLED");
        }
    }
    
    private ComboBox getToComboBox( ArrayList<String> OntDetails, String setValueString){
        final ComboBox toComboBox = createComboBox(new ArrayList(OntDetails.subList(1, OntDetails.size())), setValueString);
        
        toComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setToIdx(toComboBox.getValue().toString());
                checkToolBarStatus();
            }
        });
        return toComboBox;
        
    }
    
    
    
    
    
    private ComboBox getLabelComboBox( ArrayList<String> labels, String setValueString){
        final ComboBox labelComboBox = createComboBox(labels, setValueString);
        
        labelComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setLabelIdx(labelComboBox.getValue().toString());
                checkToolBarStatus();
            }
        });
        return labelComboBox;
        
    }
    
    
    private Label getLabelForComboBox(String labelText){
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe", FontWeight.NORMAL, 12));
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setMaxHeight(Double.MAX_VALUE);
        return label;
    }
    
    public void widthChangeAction(){
        this.morphingChainGUI.setWidth(this.getWidth()+50);
    }
    
    
    MorphChainToolBar(ArrayList<String> OntDetails, ArrayList<PairData> pairData, MorphingChainGUI morphingChainGUI){
        
        this.morphingChainGUI = morphingChainGUI;
        this.OntDetails = OntDetails;
        this.pairData = pairData;
        
        this.fromIdx = -1;
        this.toIdx = -1 ;
        this.labelIdx = -1;
        this.pairTypeIdx = -1;
        
        this.setSpacing(10);
        this.setMinHeight(50.0);
        this.setPadding(new Insets(5,5,5,5));
        
        
        this.fromHBox = new HBox();
        this.fromHBox.setSpacing(5);
        Label fromLabel = getLabelForComboBox("From:");      
        this.fromComboBox = getFromComboBox(OntDetails, "Select Ontology Version");
        fromHBox.getChildren().addAll(fromLabel, this.fromComboBox);       
        this.getChildren().add(fromHBox);
        
        
        this.toHBox = new HBox();
        this.toHBox.setSpacing(5);
        Label toLabel = getLabelForComboBox("To:");      
        this.toComboBox = getToComboBox(OntDetails, "Select Ontology Version");
        this.toComboBox.setDisable(true);
        toHBox.getChildren().addAll(toLabel, this.toComboBox);       
        this.getChildren().add(toHBox);
                
        
        this.threshMinValue = 0.0;
        this.threshMaxValue = 1.0;
        
        this.pairTypes = new ArrayList<>(Arrays.asList("Label","Intensional","Extensional","Whole"));
        
        
        
    }
    
}
