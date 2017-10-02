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

import gui.ColorTheme;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author staxos
 */
public class MorphingChainGUI extends Stage {
    
    protected Group root;
    protected Scene scene;
    
    private MorphChainToolBar morphChainToolBar;
    
    private ArrayList<PairData> pairData;
    private ArrayList<String> OntDetails;
    
    private EventHandler<MouseEvent> onMouseDraggedHandler;
    private int gridLocked;
    protected Label utilityLabel;
    protected Label utilityLabelExplanation;
    protected Graph graph;
    
    private double dragStartX, dragStartY;  
    
    private RightClickPopUpMenu rClickPopUp;
    protected String aspect;
    protected Rectangle rectangleSelect;
    
    
    
    
    private void onMouseClickedFunc(double mX, double mY) {
        this.dragStartX = mX;
        this.dragStartY = mY;
        if ( this.graph != null && !this.graph.isEmpty()){
            this.graph.setDragStartPoint(dragStartX, dragStartY);
        }
        
        
    }
    
    
    private void onMouseDraggedFunc( double mDropX, double mDropY){        
        this.scene.setCursor(Cursor.CLOSED_HAND);
        double diffX = (-this.dragStartX+mDropX);
        double diffY = (-this.dragStartY+mDropY);
        
        graph.translateGuiNodes(diffX, diffY);
        
        /*
        for ( int i = 0 ; i < this.guiNodes.size(); i++ ){
            this.guiNodes.get(i).setCenter(this.guiNodes.get(i).circle.getCenterX()+diffX, this.guiNodes.get(i).circle.getCenterY()+diffY);            
        }*/
        this.dragStartX = mDropX;
        this.dragStartY = mDropY;
       
        
    }

    
    
    
    protected void onMouseReleased(double mouseX, double mouseY){        
        this.scene.setCursor(Cursor.DEFAULT);
        if ( this.rectangleSelect!=null && this.root.getChildren().indexOf(this.rectangleSelect)!=-1){
            this.root.getChildren().remove(this.rectangleSelect);
            
            if ( this.gridLocked == 0){ // on graph locked
                double startX, startY;
        if ( this.dragStartX < mouseX){ startX = dragStartX;} else {startX = mouseX; }
        if ( this.dragStartY < mouseY){ startY = dragStartY;} else {startY = mouseY; }
                this.graph.selectAllNodesInsideRectangle(startX, startY, startX+this.rectangleSelect.getWidth(), startY+ rectangleSelect.getHeight());
                
            }
        }
    }
    
    
    private void onMouseDraggedOnTranslateNodesMode(double mouseX, double mouseY){          
        this.graph.dragSelectedGuiNodes(mouseX, mouseY);
    }
    
    
    private void onMouseDraggedOnGraphLockedMode(double mouseX, double mouseY){
        double startX, startY;
        if ( this.dragStartX < mouseX){ startX = dragStartX;} else {startX = mouseX; }
        if ( this.dragStartY < mouseY){ startY = dragStartY;} else {startY = mouseY; }
        
            if ( this.rectangleSelect == null || this.root.getChildren().indexOf(this.rectangleSelect)==-1 ){
                this.rectangleSelect = new Rectangle(startX, startY, Math.abs(mouseX-dragStartX), Math.abs(mouseY-dragStartY));
                rectangleSelect.setFill(Color.TRANSPARENT);
                rectangleSelect.setStroke(Color.GRAY);
                this.root.getChildren().add(rectangleSelect);
            } else if (this.root.getChildren().indexOf(this.rectangleSelect)!=-1 ){
                if (mouseX >= dragStartX && mouseY >= dragStartY){
                this.rectangleSelect.setWidth (Math.abs(mouseX-dragStartX));
                this.rectangleSelect.setHeight( Math.abs(mouseY-dragStartY) );
                } else {
                    this.root.getChildren().remove(this.rectangleSelect);
                    this.rectangleSelect = new Rectangle(startX, startY, Math.abs(mouseX-dragStartX), Math.abs(mouseY-dragStartY));
                rectangleSelect.setFill(Color.TRANSPARENT);
                rectangleSelect.setStroke(Color.GRAY);
                this.root.getChildren().add(rectangleSelect);
                }
            }
    }
    
    
    private void onMouseDoubleClickedFunc(boolean proceed){
                
        if ( proceed ) {
            this.gridLocked++;
            if ( this.gridLocked == 3) {
                this.gridLocked = 0;
            }
        }
        
        switch (this.gridLocked) {
            
            case 0: //Graph Locked
                this.scene.setOnMouseDragged( new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        onMouseDraggedOnGraphLockedMode(event.getSceneX(), event.getSceneY());
                    }
                });
                
                this.scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        onMouseReleased(event.getSceneX(), event.getSceneY());
                    }
                });
                this.scene.setOnScroll(null);
                this.graph.setGuiNodesMouseDragEvent(false);        
                break;
            case 1: // Enable Translate Nodes    
                this.scene.setOnMouseDragged( new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        onMouseDraggedOnTranslateNodesMode(event.getSceneX(), event.getSceneY());
                    }
                });
                this.scene.setOnScroll(null);
                this.graph.setGuiNodesMouseDragEvent(true);
                break;
            case 2: //Enable Translate Whole Graph
                this.scene.setOnMouseDragged( this.onMouseDraggedHandler);
                this.scene.setOnScroll( (ScrollEvent event)-> {
                    
                    double deltaY = event.getDeltaY();
                    zoom( deltaY, event.getSceneX(), event.getSceneY() );
                });
                this.scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        onMouseReleased(event.getSceneX(), event.getSceneY());
                    }
                });
                this.graph.setGuiNodesMouseDragEvent(false);  
                break;
            default:
                break;
        }
        showUtilityLabel();
        
    }
    
    public void zoom ( double deltaY, double mouseX, double mouseY ){
        if (this.graph.isEmpty()){
            return;
        }
        
        double zoomFactor = 1.05;
        if ( deltaY < 0 ){
            zoomFactor = 1.0/zoomFactor;
        }
        double trFactor = 0.2;
        
        double midX = (this.scene.getWidth())/2;
        double midY = (this.scene.getHeight())/2;        
        
        double translateX = (midX-mouseX)*trFactor;
        double translateY = (midY-mouseY)*trFactor;
        
        this.graph.scaleGuiNodes(zoomFactor, zoomFactor);
        this.graph.translateGuiNodes(translateX, translateY);
        
    }
    
    
    
    
    private void showUtilityLabel(){
        
        if ( this.utilityLabel == null){
            utilityLabel = new Label();
            utilityLabelExplanation = new Label();
        } else {
            this.root.getChildren().remove(this.utilityLabel);
            this.root.getChildren().remove(this.utilityLabelExplanation);
        }
        
        switch (this.gridLocked) {
            case 0:
                utilityLabel.setText("Graph Locked");
                this.utilityLabelExplanation.setText("Double-click anywhere to switch modes");
                break;
            case 1:
                utilityLabel.setText("Move Nodes Enabled");
                this.utilityLabelExplanation.setText("Double-click anywhere to switch modes");
                break;
            case 2:
                utilityLabel.setText("Pan & Zoom Enabled");
                this.utilityLabelExplanation.setText("Double-click anywhere to switch modes");
                break;
            default:
                break;
        }
        int padding = 25;
        utilityLabel.setPadding(new Insets(10,10,10,10));
        utilityLabel.setFont(Font.font("Calibri",FontWeight.NORMAL,16));
        utilityLabel.setStyle("-fx-background-color: #E2E3E4;");
        utilityLabel.setTranslateY(this.scene.getHeight()-10-padding);
        //utilityLabel.setTranslateX(padding);     
        
        this.root.getChildren().add(utilityLabel);
        
        
        utilityLabelExplanation.setPadding(new Insets(10,10,10,10));
        utilityLabelExplanation.setFont(Font.font("Calibri", FontWeight.NORMAL, 11));
        switch (this.gridLocked){
                case 0:
                    this.utilityLabelExplanation.setTranslateY(utilityLabel.getTranslateY()-25); //was -40 for some reason
                    break;
                default:
                    this.utilityLabelExplanation.setTranslateY(utilityLabel.getTranslateY()-25);
            }
        this.root.getChildren().add(utilityLabelExplanation);
    }
    
    
    
    private void onRightClickAction(double mouseScreenX, double mouseScreenY){
        if ( this.graph!=null && !this.graph.isEmpty()){  
            GraphNode pressedNode = this.graph.aNodeIsBeingPressed();
            if ( pressedNode!=null && !pressedNode.isSelected()){
                pressedNode.setSelected(true);
            }
            this.rClickPopUp = new RightClickPopUpMenu(this.graph.getSubGraphOfSelectedItems(), mouseScreenX, mouseScreenY, this.aspect, this.graph, pressedNode);
        }
    }
    
    private void onResizeAction(){
        if ( this.scene != null && this.utilityLabel != null ){
            utilityLabel.setTranslateY(this.scene.getHeight()-10-25);
            switch (this.gridLocked){
                case 0:
                    this.utilityLabelExplanation.setTranslateY(utilityLabel.getTranslateY()-40);
                    break;
                default:
                    this.utilityLabelExplanation.setTranslateY(utilityLabel.getTranslateY()-25);
            }
            
            if ( this.morphChainToolBar != null ){                
                this.morphChainToolBar.setButtonReFreshHighlighted(true);
            }
        }        
    }
    
    
    
    private void addResizeEventListeners(){
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                           onResizeAction();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
               onResizeAction();
            }
        });
    }
    
    
    
    
    private void initializeEventListeners(){
        this.onMouseDraggedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseDraggedFunc(event.getSceneX(), event.getSceneY());
            }
        };
        //this.scene.setOnMouseDragged(this.onMouseDraggedHandler);
        
        this.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                onCloseAction();
            }
        });
          
        this.scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (closedRClickPopUpMenu()){
                    //return;
                }
                
                if ( event.getClickCount()== 1 ){
                    onMouseClickedFunc(event.getSceneX(), event.getSceneY());
                    if ( event.getButton() == MouseButton.SECONDARY){
                        onRightClickAction(event.getScreenX(), event.getScreenY());
                    }
                }
                if ( event.getClickCount()== 2){
                    onMouseDoubleClickedFunc(true);
                }
            }
        });
        
        addResizeEventListeners();
    }
    
    
    
    
    
    
    
    private void constructorCommonInitialization( double sceneW, double sceneH){
        this.gridLocked = 2;
        
        root = new Group();
        this.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        this.setTitle("Visualize Chains");
        this.scene = new Scene(root, sceneW,sceneH);
        
        
        
        initializeEventListeners();        
        
        this.setScene(this.scene);
        this.scene.getStylesheets().add(MorphingChainGUI.class.getResource("/css/styles.css").toExternalForm());
        this.show();
    }
    
    
    
    public MorphingChainGUI( ArrayList<String> OntDetails, ArrayList<PairData> pairData ){
        this.pairData = pairData;
        this.OntDetails = OntDetails;
        double sceneW =1200, sceneH = 800;
        constructorCommonInitialization(sceneW, sceneH);
        
        //System.out.println(pairData.get(1).wholePairData.rowNames.get(3)+":"+pairData.get(1).wholePairData.colNames.get(4)+":"+pairData.get(1).wholePairData.tableValues.get(3).get(4));
        
        this.morphChainToolBar = new MorphChainToolBar(OntDetails,pairData, this);
        this.setMinWidth(1180);
        this.root.getChildren().add(this.morphChainToolBar);                
        
    }
    
    private boolean closedRClickPopUpMenu(){
        if ( this.rClickPopUp != null && rClickPopUp.mouse_IsOutside() && !rClickPopUp.isHidden()){
            this.rClickPopUp.hide();
            this.rClickPopUp = null;            
            
            return true;
        }
        this.rClickPopUp = null;
        return false;
    }
    
    
    private void onCloseAction(){
        closedRClickPopUpMenu();
    }
    
    
    //this is the constructor that serves for the extended class SubGraphGUI
    public MorphingChainGUI( Graph subGraph, double sceneW, double sceneH){
        this.graph = subGraph;
        
        constructorCommonInitialization( sceneW, sceneH);
        /*
        this.gridLocked = 0;
        
        root = new Group();
        this.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        this.setTitle("Morphing Chains");
        this.scene = new Scene(root, sceneW, sceneH);
        
        
        initializeEventListeners();*/
        /*
        this.onMouseDraggedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseDraggedFunc(event.getSceneX(), event.getSceneY());
            }
        };
        //this.scene.setOnMouseDragged(this.onMouseDraggedHandler);
        
        this.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                onCloseAction();
            }
        });
        
          
        this.scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {                
                if (closedRClickPopUpMenu()){
                    return;
                }
                
                if ( event.getClickCount()== 1 ){
                    onMouseClickedFunc(event.getSceneX(), event.getSceneY());
                    if ( event.getButton() == MouseButton.SECONDARY){
                        onRightClickAction(event.getScreenX(), event.getScreenY());
                    }
                }
                if ( event.getClickCount()== 2){
                    onMouseDoubleClickedFunc(true);
                }
            }
        });
        
        addResizeEventListeners(); */
        
        
       // this.setScene(this.scene);
       // this.show();
        //System.out.println(pairData.get(1).wholePairData.rowNames.get(3)+":"+pairData.get(1).wholePairData.colNames.get(4)+":"+pairData.get(1).wholePairData.tableValues.get(3).get(4));
        onMouseDoubleClickedFunc(false);
        
    }
    
    
    
    
    public ArrayList<StabilityData> getDriftFor( int pairTypeIdx ){
        ArrayList<StabilityData> sData = new ArrayList<>();
        for ( int i = 0 ; i < this.pairData.size(); i++){
            sData.add( this.pairData.get(i).getPairDataType(pairTypeIdx) );
        }
        return sData;
    }
    
    
    
    //pairData.get( fromIdx ).getPairDataType( pairTypeIdx). rowNames.get(labelIdx); => Initial Row Name  
       
    
    public void buildGraph(int fromIdx, int toIdx, int labelIdx, int pairTypeIdx, double threshold, ArrayList<String> pairTypes){
        this.aspect = pairTypes.get(pairTypeIdx);
        //System.out.println("toolbar height is:"+ this.morphChainToolBar.getHeight() );
        
        System.out.println( "Selected Label:"+this.pairData.get( fromIdx ).getPairDataType( pairTypeIdx). rowNames.get(labelIdx) );
        
        String selectedLabel = this.pairData.get( fromIdx ).getPairDataType( pairTypeIdx). rowNames.get(labelIdx);
        
        
        ArrayList<StabilityData> stabilityDataArray = getDriftFor( pairTypeIdx );
        

        MorphGraph morphGraph = new MorphGraph(stabilityDataArray);
        morphGraph.buildGraphForLabel( selectedLabel, threshold, fromIdx, toIdx);

        System.out.println("SHOWING MORPHING CHAIN for " + selectedLabel + "  IDX:" + labelIdx);
        
        //==remove everything but the morphChain toolbar from root=======
        for ( int i = 0 ; i< this.root.getChildren().size(); i++ ){
            if (i != this.root.getChildren().indexOf(this.morphChainToolBar) && i != this.root.getChildren().indexOf(this.utilityLabel) ){
                this.root.getChildren().remove(i);
            }
        }
        //================================================================
        
        // if there are no leaves on graph, do nothing from here on. Return.
        
        int vPadding = 50;
        int ontDetailTitleHeight = (int) (this.scene.getHeight() * 0.0625);
        int hZoneWidth = ((int) this.scene.getWidth() )/ (morphGraph.getLevels()+1);
        int vZoneHeight = ((int) this.scene.getHeight()- (int) this.morphChainToolBar.getHeight()-vPadding - ontDetailTitleHeight) / ( morphGraph.getMaxNumberOfNodesOnASingleLevel());
        
        int refX = hZoneWidth/2;
        int refY = vPadding +(int) this.morphChainToolBar.getHeight() + ontDetailTitleHeight+ vZoneHeight/2;
        
        int titleRefX = hZoneWidth/2 - 50;
        int titleRefY = vPadding+ (int) this.morphChainToolBar.getHeight();
        
        ArrayList<Label> ontDetailLabels = new ArrayList<>();
        Group graphicsGroup = new Group();
        for ( int i = fromIdx; i <= toIdx; i++){
            Label ontDetailLabel = new Label(this.OntDetails.get(i));
            ontDetailLabels.add(ontDetailLabel);
            ontDetailLabel.setTranslateX((double) titleRefX + hZoneWidth *(i-fromIdx));
            ontDetailLabel.setTranslateY( (double) titleRefY);
            ontDetailLabel.setFont( Font.font("Arial", FontWeight.BOLD, 18));
            graphicsGroup.getChildren().add(ontDetailLabel);
        }
        
        int radius = 20;
        String fillColor = ColorTheme.graphNode;// "red";
        String strokeColor = "white"; 
        
        
        
        
        //========= Get Node Coordinates =========
        ArrayList<GraphNode> nodes = new ArrayList<>();
        for (int level = 0; level < morphGraph.getLevels(); level++) {
            ArrayList<MorphNode> parents = morphGraph.getParentsOfLevel(level);
            for (int p = 0; p < parents.size(); p++) {
                int x = refX+hZoneWidth*level;
                int y = refY+vZoneHeight*p;
                
                parents.get(p).x = x;
                parents.get(p).y = y;
                
                nodes.add(new GraphNode(parents.get(p).name, radius, x, y, fillColor, strokeColor, level, ontDetailLabels.get(level).getText() ));

            }
        }

        ArrayList<MorphNode> leaves = morphGraph.getLeaves();
        for (int l = 0; l < leaves.size(); l++) {
            int x = refX+ hZoneWidth*(morphGraph.getLevels());
            int y = refY+vZoneHeight*l; 
            
            leaves.get(l).x = x;
            leaves.get(l).y = y;
            
            nodes.add( new GraphNode(leaves.get(l).name, radius, x, y, fillColor, strokeColor, morphGraph.getLevels(), ontDetailLabels.get(morphGraph.getLevels()).getText() ));
        }
        //===========================
        
        //=========Draw Edges============
        ArrayList<GraphEdge> edges = new ArrayList<>();
        for (int level = 0; level < morphGraph.getLevels(); level++) {
            ArrayList<MorphNode> parents = morphGraph.getParentsOfLevel(level);
            for (int p = 0; p < parents.size(); p++) {
                for ( int c = 0; c < parents.get(p).nextNodes.size(); c++){
                    MorphNode startNode = parents.get(p);
                    MorphNode endNode = parents.get(p).nextNodes.get(c);
                    edges.add( new GraphEdge(startNode.weights.get(c), startNode.x, startNode.y, endNode.x, endNode.y) ) ;
                }
            }
        }
        
        for ( int i = 0 ; i < nodes.size(); i++ ){
            for ( int j = 0 ; j < edges.size(); j++){
                nodes.get(i).addEdge(edges.get(j));
            }
        }
        for ( int i = 0 ; i < edges.size(); i++){
            for ( int j = 0 ; j < nodes.size(); j++){
                edges.get(i).checkAndAddNodeifStartOrEnd(nodes.get(j));
            }
        }
        
        
        for ( int i = 0 ; i < edges.size(); i++){
            graphicsGroup.getChildren().add(edges.get(i).line);
            graphicsGroup.getChildren().add(edges.get(i).labelWeight);
        }
        
        for ( int i = 0 ; i < nodes.size(); i++){
            graphicsGroup.getChildren().add(nodes.get(i).circle);
            graphicsGroup.getChildren().add(nodes.get(i).label);
            graphicsGroup.getChildren().add(nodes.get(i).labelOntDetail);
        }
        
        this.graph = new Graph();
        this.graph.setNodes(nodes);
        this.graph.setEdges(edges);
        
        
        this.root.getChildren().add(graphicsGroup);
        
        onMouseDoubleClickedFunc(false);
        if ( this.morphChainToolBar!=null){
            this.morphChainToolBar.toFront();
        }
                    
    }
    
    
    
    
    
}
