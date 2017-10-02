/*
 * Copyright 2017 staxos.
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

import gui.SemaDriftFx;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 *
 * @author staxos
 */
public class RightClickPopUpMenu {
    private Scene scene;
    private Stage stage;
    private VBox root;
    private Button buttonShowSubgraph;
    private Button buttonSubGraphTable;
    private Button buttonSelectFirstDescendants;
    private Button buttonDeSelectFirstDescendants;
    private Button buttonSelectAllDescendants;
    private Button buttonDeSelectAllDescendants;
    private Graph subGraph;
    private String aspect;
    //private Graph graph;
    
    private ArrayList<SubGraphTablePanel> subGraphTablePanels;
    
    
    private boolean mouseIsOutside;
    
    public boolean isHidden(){
        return !this.stage.isShowing();
    }
    
    public void showSubGraphAction(){
        System.out.println("Showing subgraph");
        SubGraphGUI sgraphGUI = new SubGraphGUI( this.subGraph, this.aspect );
        this.stage.hide();
    }
    
    public void hide(){
        this.stage.hide();
    }
    
    public boolean mouse_IsOutside(){
        return this.mouseIsOutside;
    }
    
    
    
    public void onMouseExitedAction(){
       mouseIsOutside = true;
    }
    
    
    public void onMouseEnteredAction(){
       mouseIsOutside = false;
    }
    
    
    public VBox createPage(int pageIndex){
        VBox myVBox = new VBox();
        myVBox.getChildren().add(this.subGraphTablePanels.get(pageIndex));
        return myVBox;
    }
    
    
    public void showSubGraphTableAction(){
        int noDrifts = this.subGraph.getNoLevels()-1;
        if ( this.subGraph.isEmpty() || noDrifts <= 0){
            return;
        }
        ArrayList<SubGraphTable> subGraphTables = new ArrayList<>();
        for ( int i = 0 ; i < noDrifts; i++ ){
            SubGraphTable sTable = this.subGraph.getTable_forDrift(i);
            if ( sTable != null) {
                subGraphTables.add( this.subGraph.getTable_forDrift(i));
            }
            //subGraphTables.get(i).print();
        }
        if ( subGraphTables.size() == 0){
            Alert alert = new Alert(AlertType.WARNING, "Selected Nodes are not connected. \nSubgraph Table cannot be displayed.");
            alert.showAndWait();
            return;
        }
        
        this.subGraphTablePanels = new ArrayList<>();
        
        for ( int i = 0 ; i < subGraphTables.size(); i++ ){
            this.subGraphTablePanels.add( new SubGraphTablePanel( subGraphTables.get(i).getTitle(), this.stage, this.aspect) );
            this.subGraphTablePanels.get(i).fillTable( subGraphTables.get(i));
        }
        
        
        Pagination tablePanelPagination = new Pagination(subGraphTables.size());
        tablePanelPagination.setPageFactory(new Callback<Integer, Node>() {
 
            @Override
            public Node call(Integer pageIndex) {          
                return createPage(pageIndex);               
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(tablePanelPagination);
        root.setPadding(new Insets(0, 15, 0, 15));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        stage.setTitle("Subgraph Drift Measures");
        
        stage.setScene(new Scene(root, 600, 300));
        stage.getScene().getStylesheets().add(SemaDriftFx.class.getResource("/css/styles.css").toExternalForm());
        stage.show();
        for ( int i = 0 ; i < subGraphTablePanels.size(); i++){
            subGraphTablePanels.get(i).setStage(stage);
        }
        
        this.stage.hide();
    }
    
    
    public void selectFirstDescendantsAction( GraphNode pressedNode, boolean select ){
        ArrayList<GraphNode> firstDescendants = pressedNode.getFirstDescendants();
        for ( int i = 0 ; i < firstDescendants.size(); i++){
            if ( firstDescendants.get(i).isSelected()!= select){
                firstDescendants.get(i).setSelected(true);
            }
        }
        this.stage.hide();
    }
    
    
    public void selectAllDescendantsAction( GraphNode pressedNode, boolean select ){
        ArrayList<GraphNode>  parents = new ArrayList<>();
        parents.add(pressedNode);
        
        while ( parents.size() > 0 ){
            ArrayList<GraphNode> nxtParents = new ArrayList<>();
            
            for ( int i = 0 ; i < parents.size(); i++ ){
                ArrayList<GraphNode> descendants = parents.get(i).getFirstDescendants();
                for ( int j = 0 ; j < descendants.size(); j++){
                    if ( descendants.get(j)!=null && descendants.get(j).isSelected()!=select){
                        descendants.get(j).setSelected(true);
                    }
                }
                
                for ( int j = 0 ; j < descendants.size(); j++){
                    if ( descendants.get(j)!=null){
                        nxtParents.add(descendants.get(j) );
                    }
                }
            }
            parents = nxtParents;
        }
        
        this.stage.hide();
    }
    
    
    RightClickPopUpMenu(Graph subGraph, double mouseScreenX, double mouseSceenY, String aspect, Graph graph, GraphNode pressedNode){
        //this.graph = graph;
        
        this.root = new VBox();
        this.scene = new Scene(root, 210, 26.3*6);
        this.stage = new Stage();
        this.stage.setX(mouseScreenX);
        this.stage.setY(mouseSceenY);
        this.stage.setScene(scene);
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.show();
        
        this.subGraph = subGraph;
        this.aspect = aspect;
        
        
        this.scene.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseExitedAction();
            }
        });
        
        this.scene.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseEnteredAction();
            }
        });
        
        
        
        this.buttonShowSubgraph = new MyButton("Show Subgraph...", true);
        this.buttonShowSubgraph.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        this.buttonShowSubgraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubGraphAction();
            }
        });
        if (subGraph  == null || subGraph.isEmpty() ){
            this.buttonShowSubgraph.setDisable(true);
        }
        this.buttonShowSubgraph.setMaxWidth(Double.MAX_VALUE);        
        this.root.getChildren().add(this.buttonShowSubgraph);
        
        
        
        
        
        
        this.buttonSubGraphTable= new MyButton("SubGraph Drift Measures...", true);
        this.buttonSubGraphTable.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        this.buttonSubGraphTable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSubGraphTableAction();
            }
        });
        if (subGraph  == null || subGraph.isEmpty() ){
            this.buttonSubGraphTable.setDisable(true);
        }
        this.buttonSubGraphTable.setMaxWidth(Double.MAX_VALUE);
        this.root.getChildren().add( this.buttonSubGraphTable );
        
        
        
        
        
        
        this.buttonSelectFirstDescendants = new MyButton("Select First Descendants", true);
        this.buttonSelectFirstDescendants.setFont(Font.font("Arial", FontWeight.NORMAL,14));
        this.buttonSelectFirstDescendants.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectFirstDescendantsAction( pressedNode, true );
            }
        });
        if (graph  == null || graph.isEmpty() || pressedNode == null /*(pressedNode!=null && !pressedNode.isSelected())*/ ){
            this.buttonSelectFirstDescendants.setDisable(true);
        }
        
        this.buttonSelectFirstDescendants.setMaxWidth(Double.MAX_VALUE);
        this.root.getChildren().add(this.buttonSelectFirstDescendants);
        
        
        
        
        
        this.buttonDeSelectFirstDescendants = new MyButton("DeSelect First Descendants", true);
        this.buttonDeSelectFirstDescendants.setFont(Font.font("Arial", FontWeight.NORMAL,14));
        this.buttonDeSelectFirstDescendants.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectFirstDescendantsAction( pressedNode, false );
            }
        });
        if (graph  == null || graph.isEmpty() || pressedNode == null /*(pressedNode!=null && !pressedNode.isSelected())*/ ){
            this.buttonDeSelectFirstDescendants.setDisable(true);
        }
        
        this.buttonDeSelectFirstDescendants.setMaxWidth(Double.MAX_VALUE);
        this.root.getChildren().add(this.buttonDeSelectFirstDescendants);
        
        
        
        
               
        
        
        this.buttonSelectAllDescendants = new MyButton("Select All Descendants", true);
        this.buttonSelectAllDescendants.setFont(Font.font("Arial", FontWeight.NORMAL,14));
        this.buttonSelectAllDescendants.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectAllDescendantsAction( pressedNode, true );
            }
        });
        if (graph  == null || graph.isEmpty() || pressedNode == null /*(pressedNode!=null && !pressedNode.isSelected())*/ ){
            this.buttonSelectAllDescendants.setDisable(true);
        }
        
        this.buttonSelectAllDescendants.setMaxWidth(Double.MAX_VALUE);
        this.root.getChildren().add(this.buttonSelectAllDescendants);
        
        
        
        
        
        this.buttonDeSelectAllDescendants = new MyButton("Deselect All Descendants", true);
        this.buttonDeSelectAllDescendants.setFont(Font.font("Arial", FontWeight.NORMAL,14));
        this.buttonDeSelectAllDescendants.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectAllDescendantsAction( pressedNode, false );
            }
        });
        if (graph  == null || graph.isEmpty() || pressedNode == null /*(pressedNode!=null && !pressedNode.isSelected())*/ ){
            this.buttonDeSelectAllDescendants.setDisable(true);
        }
        
        this.buttonDeSelectAllDescendants.setMaxWidth(Double.MAX_VALUE);
        this.root.getChildren().add(this.buttonDeSelectAllDescendants);
        
    }
    
}
