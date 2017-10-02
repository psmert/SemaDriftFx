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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author staxos
 */
public class OntologyControlBar extends GridPane{
    public Button buttonRemove;
    public MyButton buttonOpen1;
    public Button buttonReload1;
    public Button buttonLeft;
    public Button buttonRight;
    
    
    private Tooltip getToolTip (String msg) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(msg);
        return tooltip;
    }
    
    OntologyControlBar( Stage primaryStage, SemaDriftFx seeDriftFXNew, int ont_no, boolean disableLeftRightRefresh ){
        
        //this.setPadding(new Insets(0, 0, 10, 0));
        //this.setVgap(10);
        //this.setHgap(10);
        
        //Horizontal Panel for Title
        HBox titleHBox = new HBox();
        titleHBox.setPadding(new Insets(0,0,10,0));
        Label titleLabel = new Label("Ontology "+(ont_no+1) );
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setFont(Font.font("Calibri", FontWeight.NORMAL, 14));
        titleHBox.setAlignment(Pos.CENTER);
        
        titleHBox.getChildren().add(titleLabel);
        
        
        //Horizontal Panel for All Buttons
        HBox htoolbar = new HBox();
        htoolbar.setPadding(new Insets(0, 0, 10, 0));
        htoolbar.setSpacing(2);
        

        
        //OPEN ONTOLOGY BUTTON
        Image imgOpen = new Image(getClass().getResourceAsStream("/graphics/openblank32.png"));

        buttonOpen1 = new MyButton("", new ImageView(imgOpen), false);
        buttonOpen1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                seeDriftFXNew.openButtonAction(ont_no, primaryStage);
            }
        });
        buttonOpen1.setMaxWidth(Double.MAX_VALUE);
        buttonOpen1.setTooltip(getToolTip("Open Ontology"));
        
        
        
        //REFRESH BUTTON
        Image imgRef = new Image(getClass().getResourceAsStream("/graphics/refresh32.png"));
        buttonReload1 = new MyButton("", new ImageView(imgRef), false);
        buttonReload1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //re-open the loaded ontology
                seeDriftFXNew.refreshButtonAction(ont_no, primaryStage);
            }
        });
        buttonReload1.setTooltip(getToolTip("Reload Ontology"));
        buttonReload1.setDisable(disableLeftRightRefresh);
        
        
        //REMOVE BUTTON
        Image imgRem = new Image(getClass().getResourceAsStream("/graphics/1482340341_minus.png"));
        ImageView imgRemIV = new ImageView(imgRem);
        imgRemIV.setFitHeight(32);
        imgRemIV.setFitWidth(32);
        buttonRemove = new MyButton("", imgRemIV, false);
        buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                seeDriftFXNew.removeButtonAction(ont_no, primaryStage);
            }
        });
        buttonRemove.setTooltip(getToolTip("Remove Ontology"));
        
        
        
        //LEFT Button
        Image imgLeft = new Image(getClass().getResourceAsStream("/graphics/left.png")); //("/graphics/1482356289_arrow-left-01.png"));
        ImageView imgLeftIV = new ImageView(imgLeft);
        imgLeftIV.setFitHeight(32);
        imgLeftIV.setFitWidth(32);
        buttonLeft = new MyButton("", imgLeftIV, false);
        buttonLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                seeDriftFXNew.leftButtonAction(ont_no, primaryStage);
            }
        });
        buttonLeft.setDisable(disableLeftRightRefresh);
        buttonLeft.setTooltip(getToolTip("Move Left"));
        
        
        //Right Button
        Image imgRight = new Image(getClass().getResourceAsStream("/graphics/right.png")); //("/graphics/1482356317_arrow-right-01.png"));
        ImageView imgRightIV = new ImageView(imgRight);
        imgRightIV.setFitHeight(32);
        imgRightIV.setFitWidth(32);
        buttonRight = new MyButton("", imgRightIV, false);
        buttonRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                seeDriftFXNew.rightButtonAction(ont_no, primaryStage);
            }
        });
        buttonRight.setDisable(disableLeftRightRefresh);
        buttonRight.setTooltip(getToolTip("Move Right"));
        
        
        
        
        
        htoolbar.getChildren().addAll(buttonLeft, buttonOpen1, buttonReload1, buttonRemove, buttonRight );
       
        this.add(titleHBox,0,0);
        this.add(htoolbar,0,1);
        
    }
    
    
    

    
    
}
