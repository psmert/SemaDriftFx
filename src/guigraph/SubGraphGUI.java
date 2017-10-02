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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author staxos
 */
public class SubGraphGUI extends MorphingChainGUI {
    
    private MyButton buttonSave;
    private MyButton buttonResize;
    
    private HBox hboxBottom;
    private double toolBarWidth;
    private double labelMaxWidth;
    private double labelY;
    private double toolBarY;
    private Label labelAspect;
    
    
    
    private void showGraph(){
        ArrayList<GraphEdge> guiEdges = this.graph.getGuiEdges();
        for ( int i = 0 ; i < guiEdges.size(); i++){
            this.root.getChildren().add(guiEdges.get(i).line);
            this.root.getChildren().add(guiEdges.get(i).labelWeight);
        }        
        
        ArrayList<GraphNode> guiNodes = this.graph.getGuiNodes();
        for ( int i = 0; i <guiNodes.size(); i++){
            
            this.root.getChildren().add(guiNodes.get(i).circle);
            this.root.getChildren().add(guiNodes.get(i).label);
            this.root.getChildren().add(guiNodes.get(i).labelOntDetail);         
            
        }        
        
    }
    
    /*
    SubGraphGUI( Graph g ){
        double padding = 100;
        this.graph = g;
        this.graph.translateGuiNodes(-this.graph.getMinNodeX()+padding/2.0, -this.graph.getMinNodeY()+padding/2.0);
        
        this.root = new Group();
        this.scene = new Scene(root, g.getWidth()+padding*2, g.getHeight()+padding);
        this.root.setStyle("-fx-background-color: e1ecf4;");
        this.stage = new Stage();
        
        
        this.stage.setScene(scene);
        this.stage.show();
        
        showGraph();
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
        WritableImage image = this.getScene().snapshot(null);
        FileChooser fileChooser = new FileChooser();
        configureSaveFileChooser(fileChooser);
        File file = fileChooser.showSaveDialog(this);
        try {
            if ( file != null){
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }
    
    private void resizeButtonAction(){
        SubGraphGUI subGraph = new SubGraphGUI(graph, this.aspect);
        this.hide();
    }
    
    private Tooltip getToolTip (String msg) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(msg);
        return tooltip;
    }
    
    
    private HBox getToolBar(){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setTranslateX(this.scene.getWidth()-toolBarWidth);
        hbox.setTranslateY(this.scene.getHeight()-toolBarY);
        
               
        
        if ( hbox.getChildren().indexOf(buttonResize)==-1){
            Image imgRef = new Image(getClass().getResourceAsStream("/graphics/resize.png"));
            buttonResize = new MyButton("", new ImageView(imgRef), false);
            buttonResize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                resizeButtonAction();
            }         
        });
           
      
    }
        buttonResize.setTooltip(getToolTip("Resize to Fit Graph"));
        hbox.getChildren().add(buttonResize);
        
        
        
        if ( hbox.getChildren().indexOf(buttonSave)==-1){
            Image imgRef = new Image(getClass().getResourceAsStream("/graphics/save.png"));
            buttonSave = new MyButton("", new ImageView(imgRef), false);
            buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                saveButtonAction();
            }         
        });
      
    }
        buttonSave.setTooltip(getToolTip("Save"));
        hbox.getChildren().add(buttonSave);
        
        
        
        
         return hbox;
    }
    
    private void onResizeBottom() {
        utilityLabel.setTranslateY(this.scene.getHeight()-10-25);
    if ( this.hboxBottom != null ){
            hboxBottom.setTranslateX(this.scene.getWidth()-toolBarWidth);
            hboxBottom.setTranslateY(this.scene.getHeight()-toolBarY);
             
    }
    if ( this.labelAspect != null){
        labelAspect.setTranslateX(this.scene.getWidth()-this.toolBarWidth - this.labelMaxWidth);
        labelAspect.setTranslateY(this.scene.getHeight()-labelY);
    }
    }
    
    
    private void addResizeEventListeners(){
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                           onResizeBottom();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
               onResizeBottom();
            }
        });
    }
    
    
    SubGraphGUI( Graph g, String aspect ){        
        super(g, g.getWidth()+100*2, g.getHeight()+150);
        addResizeEventListeners();
        
        this.aspect = aspect;
        
        double padding = 100;
        this.graph.translateGuiNodes(-this.graph.getMinNodeX()+padding/2.0, -this.graph.getMinNodeY()+padding/2.0);
        
          
        showGraph();
        
        this.toolBarWidth = 100;
        this.toolBarY = 40;
        this.labelMaxWidth = 110;
        this.labelY = 30;
        
        this.hboxBottom = getToolBar();
        this.root.getChildren().add(this.hboxBottom);
        
        
        this.labelAspect = new Label();
        labelAspect.setText(this.aspect);
        labelAspect.setFont(Font.font("Arial",FontWeight.BOLD, 18));
        labelAspect.setTranslateX(this.scene.getWidth()-this.toolBarWidth - labelMaxWidth);
        labelAspect.setTranslateY(this.scene.getHeight()-labelY);
        this.root.getChildren().add(labelAspect);
        
    }
}
