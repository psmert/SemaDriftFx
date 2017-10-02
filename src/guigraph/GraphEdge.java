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

import com.sun.javafx.scene.control.skin.ColorPalette;
import gui.ColorTheme;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author staxos
 */
public class GraphEdge {

    public Line line;
    public Label labelWeight;
    public int labelX;
    public int labelY;

//    private double red;
//    private double green;
//    private double blue;
//    private double alpha;
    
    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private Stage stage;
    
    private GraphNode startNode;
    private GraphNode endNode;
    
    
    public GraphNode getEndNode(){
        return this.endNode;
    }
    
    
    public void checkAndAddNodeifStartOrEnd(GraphNode node){
        if ( Math.abs( this.startX - node.circle.getCenterX() ) <3 && Math.abs( this.startY - node.circle.getCenterY() )<3){
            this.startNode = node;
        }
        if ( Math.abs( this.endX - node.circle.getCenterX())<3  && Math.abs(this.endY - node.circle.getCenterY())<3 ){
            this.endNode = node;
        }
    }
    
    private void drawNodeStrokes(){
        if ( startNode != null){
        this.startNode.edgeIsSelected();
        }
        if ( endNode!=null){
        this.endNode.edgeIsSelected();
        }
    }
    
    
    private void recoverNodeStrokes(){
        if ( startNode !=null){
        this.startNode.reccoverNodeFromHover();
        }
        if ( endNode!=null){
        this.endNode.reccoverNodeFromHover();
        }
    }
    
    
    
    public void setStart( double sX, double sY){
        this.startX = sX;
        this.startY = sY;
        this.line.setStartX(sX);
        this.line.setStartY(sY);
        setLabelPositionAndAngle();
    }
    
    
    public void setEnd( double eX, double eY){
        this.endX = eX;
        this.endY = eY;
        this.line.setEndX(eX);
        this.line.setEndY(eY);
        setLabelPositionAndAngle();
    }
    
    
    
    public double getStartX(){
        return this.startX;
    }
    
    public double getStartY() {
        return this.startY;
    }
    
    public double getEndX(){
        return this.endX;
    }
    
    public double getEndY(){
        return this.endY;
    }

    
    private void showWeightValue(double mouseX, double mouseY) {
        int stageWidth = 50, stageHeight = 40;
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #FFF8DC;");
        Scene scene = new Scene(root, stageWidth, stageHeight);
        this.stage = new Stage();
        stage.setX(mouseX-stageWidth);
        stage.setY(mouseY-stageHeight);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Label labelToShow = new Label(this.labelWeight.getText());
        labelToShow.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        root.getChildren().add(labelToShow);        
    }

    
    private double getAngle(double startX, double startY, double endX, double endY) {
        
        
        
        double angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public void onMouseEnteredAction() {
//        this.line.setStroke(Color.FIREBRICK);
        this.line.setStroke(Color.web(ColorTheme.graphEdgeHover));
        drawNodeStrokes();
    }

    public void onMouseExitedAction() {
//        this.line.setStroke(new Color(red, green, blue, alpha));
        this.line.setStroke(Color.web(ColorTheme.graphEdge));
        recoverNodeStrokes();
    }

    public void onMouseReleasedAction() {
        this.stage.hide();
        
    }
    
    
    public GraphEdge getCopy(){
        GraphEdge e = new GraphEdge(this.labelWeight.getText(), this.startX, this.startY, this.endX, this.endY);
        return e;
    }
    

    GraphEdge(String weightStr, double startX, double startY, double endX, double endY) {
        
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        this.line = new Line(startX, startY, endX, endY);
        double weightDouble = Double.valueOf(weightStr.replace(",", "."));
//        this.red = 90.0 / 256.0;
//        this.green = 145.0 / 256.0;
//        this.blue = 229.0 / 256.0;
//        this.alpha = 1.0 * weightDouble;
//        this.line.setStroke(new Color(red, green, blue, alpha)); //approximately CORNFLOWERBLUE
        this.line.setStroke(Color.web(ColorTheme.graphEdge));
        this.line.setStrokeWidth(4);

        this.line.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showWeightValue(event.getScreenX(), event.getScreenY());

            }
        });

        this.line.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseReleasedAction();

            }
        });

        this.line.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseEnteredAction();
            }
        });

        this.line.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseExitedAction();
            }
        });

        this.labelWeight = new Label(weightStr);
        
        setLabelPositionAndAngle();
        

    }
    
    
    
    private void setLabelPositionAndAngle(){
        this.labelWeight.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        double wStart = 0.6;
        double wEnd = 0.4;
        this.labelWeight.setTranslateX((endX * wEnd + startX * wStart));
        this.labelWeight.setTranslateY((endY * wEnd + startY * wStart));
        this.labelX = (int) (endX * wEnd + startX * wStart);
        this.labelY = (int) (endY * wEnd + startY * wStart);
        
        //no label angle
//        this.labelWeight.setRotate(getAngle(startX, startY, endX, endY));
    }
}



