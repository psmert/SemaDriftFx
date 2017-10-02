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
package gui;

import guigraph.MyButton;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author staxos
 */
public class TablePanel extends VBox{
    
    private Text titleText;
    private MyTable tableAvg;
    private MyTable tableLabelPairs;
    private MyTable tableIntensionPairs;
    private MyTable tableExtensionPairs;
    private MyTable tableWholePairs;
    private MyButton buttonExportTSV;
    
    private Text conConTxt;
    private Text conConLTxt;
    private Text conConITxt;
    private Text conConETxt;
    private Text conConWTxt;
    
    private Stage stage;
    
    
    
    public MyTable getTableAvg(){
        return this.tableAvg;
    }
    
    public MyTable getTableLabelPairs(){
        return this.tableLabelPairs;
    }
    
    public MyTable getTableIntensionPairs(){
        return this.tableIntensionPairs;
    }
    
    public MyTable getTableExtensionPairs(){
        return this.tableExtensionPairs;
    }
    
    public MyTable getTableWholePairs(){
        return this.tableWholePairs;
    }
    
     private Tooltip getToolTip (String msg) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(msg);
        return tooltip;
    }
    
    private void initialize(){
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color: transparent;"
                + "-fx-background: transparent;");
        
        VBox vboxScr = new VBox();
        vboxScr.setMinHeight(0);
        
        
        //Average Concept Stability
//        String txtHeadingStyle = "-fx-fill:" + ColorPallete.ToolbarBg; //styling\n" 
        Text avgConcTxt = new Text("Average Concept Stability");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(avgConcTxt);
        
        boolean editable = false;
        int colMinWidth = 50;
        ArrayList<String> tableAvgColNames = new ArrayList<>(Arrays.asList("Label", "Intensional", "Extensional","Whole"));
        

        //this.tableAvg = new MyTable(tableAvgColNames, editable, colMinWidth);
        
        tableAvg = new MyTable();
        tableAvg.setEditable(true);
        for ( int i= 0; i < tableAvgColNames.size(); i++){
            final int k = i;
            TableColumn col = new TableColumn<ConceptMetricsSchema, String>();//(columnName);
            col.setMinWidth(50);
           col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ConceptMetricsSchema, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ConceptMetricsSchema, String> param) {
                    if ( k== 0){ return new SimpleStringProperty(param.getValue().getLabel());} 
                    else if ( k == 1){  return new SimpleStringProperty(param.getValue().getIntensional());}
                    else if ( k == 2){  return new SimpleStringProperty(param.getValue().getExtensional());}
                    else if ( k == 3){  return new SimpleStringProperty(param.getValue().getWhole());}
                    return new SimpleStringProperty(param.getValue().getLabel());
                }
            });
            Label labelCol = new Label(tableAvgColNames.get(i));
            labelCol.setTextFill(Color.BLACK);
            labelCol.setTooltip( getToolTip(tableAvgColNames.get(i)));
            col.setGraphic(labelCol);
            
            tableAvg.addColumnName(tableAvgColNames.get(i));
            tableAvg.getColumns().add(col);
        }

        //Styling table
        TableUtils.styleTable(tableAvg);
        tableAvg.setMaxWidth(400);

        vboxScr.getChildren().add(tableAvg);

        //Table 2
        //Concept per Concept Stability
        this.conConTxt = new Text("Concept-per-Concept Stability");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(conConTxt);

        //Label Aspect
        this.conConLTxt = new Text("Label Aspect");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(conConLTxt);

        tableLabelPairs = new MyTable();
        TableUtils.styleTable(tableLabelPairs);
        vboxScr.getChildren().add(tableLabelPairs);

        //Intensional
        this.conConITxt = new Text("Intensional Aspect");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(conConITxt);

        tableIntensionPairs = new MyTable();
        TableUtils.styleTable(tableIntensionPairs);
        vboxScr.getChildren().add(tableIntensionPairs);

        //Extensional
        this.conConETxt = new Text("Extensional Aspect");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(conConETxt);

        tableExtensionPairs = new MyTable();
        TableUtils.styleTable(tableExtensionPairs);
        vboxScr.getChildren().add(tableExtensionPairs);

        //Whole
        this.conConWTxt = new Text("Whole");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(conConWTxt);

        tableWholePairs = new MyTable();
        TableUtils.styleTable(tableWholePairs);
        vboxScr.getChildren().add(tableWholePairs);

        //construct dummy dynamic table
//        Random rand = new Random();
//        int randSize = rand.nextInt((10 - 1) + 1) + 1;
//        fillDummyTable(randSize, tableLabelPairs);
//        fillDummyTable(randSize, tableIntensionPairs);
//        fillDummyTable(randSize, tableExtensionPairs);
//        fillDummyTable(randSize, tableWholePairs);
        scrollPane.setContent(vboxScr);
        this.getChildren().add(scrollPane);
    }
    
    
    private void exportTableViewToFile( Text titleText, MyTable t, PrintWriter writer){
        writer.println(titleText.getText());
        
        for ( int j = 0 ; j < t.getColumns().size(); j++ ){
           // TableColumn tCol = (TableColumn) t.getColumns().get(j);
           // writer.print( tCol.getText()+ "\t");
           writer.print( t.getColName(j)+ "\t");
        }
        writer.println("");
        
        ObservableList<ObservableList> tableData = t.getItems();
        
        for (int i = 0 ; i < tableData.size();i++ ) {
            for ( int j = 0 ; j < tableData.get(i).size(); j++){
                writer.print(tableData.get(i).get(j).toString().replace(",",".") + "\t");
            }
            writer.println("");        
        }
        
        writer.println("");
    }
    
    private void exportTableAvgToFile (PrintWriter writer ){
        writer.println( this.conConTxt.getText() );
        
        for ( int j = 0 ; j < this.tableAvg.getColumns().size(); j++ ){
            TableColumn tCol = (TableColumn) tableAvg.getColumns().get(j);
            writer.print( tCol.getText() + "\t");
        }
        writer.println("");
        
        ObservableList<ConceptMetricsSchema>  tableData = tableAvg.getItems();
        writer.println(tableData.get(0).getLabel().replace(",",".") +"\t"+ tableData.get(0).getIntensional().replace(",",".")
                +"\t"+ tableData.get(0).getExtensional().replace(",",".") +"\t"+ tableData.get(0).getWhole().replace(",",".") );
        writer.println("");
    }
    
    
    
    private static void configureTXTFileChooser(
            final FileChooser fileChooser, String initialFileName) {
        fileChooser.setInitialFileName(initialFileName);
        fileChooser.setTitle("Save Tables as...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TSV", "*.tsv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }
    
    private void exportToFile(){
        FileChooser fileChooser = new FileChooser();
        configureTXTFileChooser(fileChooser, this.titleText.getText().replace("->", "__"));
        File f =  fileChooser.showSaveDialog(this.stage);
        
        if ( f!=null){
        try{
            PrintWriter writer = new PrintWriter(f.getAbsolutePath(), "UTF-8");
            
            exportTableAvgToFile(writer);
            
            exportTableViewToFile(this.conConLTxt, this.tableLabelPairs, writer);
            exportTableViewToFile(this.conConITxt, this.tableIntensionPairs, writer);
            exportTableViewToFile(this.conConETxt, this.tableExtensionPairs, writer);
            exportTableViewToFile(this.conConWTxt, this.tableWholePairs, writer);
            writer.close();
        } catch (IOException e) {
            // do something
        }
        }
       
            
        
        
    }
    
    public void setStage( Stage s){
        this.stage = s;
    }
    
    
    
    private MyButton getExportButton(){
        Image imgExport = new Image(getClass().getResourceAsStream("/graphics/export.png"));
        ImageView imgExportIV = new ImageView(imgExport);
        imgExportIV.setFitHeight(32);
        imgExportIV.setFitWidth(32);
        this.buttonExportTSV = new MyButton("Export", imgExportIV, false);
        this.buttonExportTSV.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exportToFile();
            }
        });
        return this.buttonExportTSV;
        //this.getChildren().add(this.buttonExportTSV);
    }
    
    TablePanel(String titleStr, Stage s){
        this.stage = s;
        
        HBox titlePane = new HBox();
        titlePane.setPadding(new Insets(20,25,10,10));
        titlePane.setMaxWidth(Double.MAX_VALUE);
        //titlePane.setStyle("-fx-background-color: blue;");
        
        HBox titleTextPane = new HBox();
        titleTextPane.setMaxWidth(Double.MAX_VALUE);
        titleTextPane.setAlignment(Pos.CENTER);
        
        //titleTextPane.setStyle("-fx-background-color: orange;");
        this.titleText = new Text(titleStr);
        titleText.setFont(Font.font("Times", FontWeight.NORMAL, 18));        
        titleTextPane.getChildren().add(titleText);        
        
        this.buttonExportTSV = getExportButton();                
        HBox buttonExpPane = new HBox();
        buttonExpPane.setAlignment(Pos.CENTER_RIGHT);
       // buttonExpPane.setStyle("-fx-background-color: white;");
        buttonExpPane.getChildren().add(this.buttonExportTSV);
        
        
        
        titlePane.getChildren().addAll(titleTextPane, buttonExpPane);
        titlePane.setHgrow(titleTextPane, Priority.ALWAYS);
        
        this.getChildren().add(titlePane);
        
        
        initialize();
    }
    
    TablePanel(){
        initialize();
    }
}
