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

import gui.MyTable;
import gui.TableUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class SubGraphTablePanel extends VBox{
    
    private Text titleText;
    private MyTable tableLabelPairs;
    private MyButton buttonExportTSV;
    private Text conConTxt;
    private Text conConLTxt;
    
    private Stage stage;
    private String aspect;
    
    
    
    public TableView getTableLabelPairs(){
        return this.tableLabelPairs;
    }
    
    public void fillTable( SubGraphTable sgTable){   
        
        this.aspect = aspect;

        //columns (schema)
        ArrayList<String> columnNames = sgTable.getColNames();
        for (int i = 0; i < columnNames.size(); i++) {
            final int k = i;
            String columnName = columnNames.get(i);     
            
            TableColumn col = new TableColumn<>();//(columnName);
            if ( k > 0 ){
                col.setPrefWidth(columnName.length()*8);
            }
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(k).toString());
                }
            });
            Label labelCol = new Label(columnName);
            labelCol.setTextFill(Color.BLACK);
            labelCol.setTooltip( new Tooltip(columnName));
            col.setGraphic(labelCol);
            this.tableLabelPairs.addColumnName(labelCol.getText());
            
            this.tableLabelPairs.getColumns().add(col);
        }

        //rows (data)
        ObservableList<ObservableList> tablePairData = FXCollections.observableArrayList();

        ArrayList<String> rowNames = sgTable.getRowNames();
        rowNames.remove(0);
        ArrayList<String> colNames = new ArrayList<>();
        for ( int i = 1 ; i < columnNames.size(); i++){
            colNames.add(columnNames.get(i));
        }

        for (int i = 0; i < rowNames.size(); i++) { //rows
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(rowNames.get(i));
            for (int j = 0; j < colNames.size(); j++) {
                row.add(sgTable.getValueAt(i+1, j+1));
            }
            tablePairData.add(row);
        }
        this.tableLabelPairs.setItems(tablePairData);
        
        
        TableUtils.reAdjustTableHeight(this.tableLabelPairs);
    }
    
    
    
    
    
    private void initialize(){
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color: transparent;"
                + "-fx-background: transparent;");
        
        VBox vboxScr = new VBox();
        vboxScr.setMinHeight(0);
        
        
        //Table 2
        //Concept per Concept Stability
        this.conConTxt = new Text("Concept-per-Concept Stability");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(conConTxt);

        //Label Aspect
        this.conConLTxt = new Text(this.aspect+" Aspect");
//      avgConcTxt.setStyle(txtHeadingStyle);
        vboxScr.getChildren().add(conConLTxt);

        this.tableLabelPairs = new MyTable();
        TableUtils.styleTable(tableLabelPairs);
        vboxScr.getChildren().add(tableLabelPairs);

        
        scrollPane.setContent(vboxScr);
        this.getChildren().add(scrollPane);
    }
    
    
    private void exportTableViewToFile( Text titleText, MyTable t, PrintWriter writer){
        writer.println(titleText.getText());
        
        for ( int j = 0 ; j < t.getColumns().size(); j++ ){
            //TableColumn tCol = (TableColumn) t.getColumns().get(j);
            //writer.print( tCol.getText() + "\t");
            writer.print( t.getColName(j) + "\t");
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
        configureTXTFileChooser(fileChooser, "Subgraph_"+this.titleText.getText().replace("->", "__"));
        File f =  fileChooser.showSaveDialog(this.stage);
        
        if ( f!=null){
        try{
            PrintWriter writer = new PrintWriter(f.getAbsolutePath(), "UTF-8");            
            
            exportTableViewToFile(this.conConLTxt, this.tableLabelPairs, writer);
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
    
    
    
    SubGraphTablePanel(String titleStr, Stage s, String aspect){
        this.stage = s;
        this.aspect = aspect;
        
        HBox titlePane = new HBox();
        titlePane.setPadding(new Insets(10,20,10,10));
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
    
    
    
}
