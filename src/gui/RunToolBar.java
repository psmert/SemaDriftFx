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
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author staxos
 */
public class RunToolBar extends HBox{
    
    private MyButton buttonRun;
    private MyButton buttonMorphChain;
    private MyButton buttonAbout;
    
    public MyButton getButtonRun(){
        return this.buttonRun;
    }
    
    public void setDisableButtonRun(boolean val){
        this.buttonRun.setDisable(val);
        setHighlighted(this.buttonRun, !val);
    }
    
    
    public void setHighlighted(MyButton b, boolean val){
        if ( val ){
            b.setID("highlighted");
        } else {
            b.setID("");
        }
    }
    
    
    public MyButton getButtonMorphChain(){
        return this.buttonMorphChain;
    }
    
    public void setDisableButtonMorphChain( boolean val){
        this.buttonMorphChain.setDisable(val);
        setHighlighted(this.buttonMorphChain, !val);
    }
    
    
    private Tooltip getToolTip (String msg) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(msg);
        return tooltip;
    }
    
    
    RunToolBar( SemaDriftFx seeDriftFX, int spacing ){
        
        this.setSpacing(spacing);
        
        
        //Add RUN BUTTON
        Image imgRun = new Image(getClass().getResourceAsStream("/graphics/run32.png"));
        buttonRun = new MyButton("Measure \n  Drift", new ImageView(imgRun), false);
        buttonRun.setDisable(true);
        buttonRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seeDriftFX.runButtonAction();
            }
        });
        buttonRun.setFont(Font.font("Segoe", FontWeight.NORMAL, 12));
        //this.buttonRun.setTooltip(getToolTip("Measure Drift") );
        this.getChildren().add(buttonRun);
        
               
        
        
        
        //Add MORPHING CHAIN BUTTON
        Image imgMorphChain = new Image(getClass().getResourceAsStream("/graphics/1482850024_5_MorphingChain.png"));
        buttonMorphChain = new MyButton("Visualize\n  Chains", new ImageView(imgMorphChain), false);
        buttonMorphChain.setDisable(true);
        buttonMorphChain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seeDriftFX.morphChainButtonAction();
            }
        });
        buttonMorphChain.wrapTextProperty().setValue(Boolean.TRUE);
        buttonMorphChain.setFont(Font.font("Segoe", FontWeight.NORMAL, 12));
        //buttonMorphChain.setTooltip(getToolTip("Morphing Chains"));
        this.getChildren().add(buttonMorphChain);
        
        
        //Add ABOUT BUTTON
        Image imgAbout = new Image(getClass().getResourceAsStream("/graphics/about.png"));
        ImageView imgAboutIV = new ImageView(imgAbout);
        imgAboutIV.setFitHeight(32);
        imgAboutIV.setFitWidth(32);
        buttonAbout = new MyButton("", imgAboutIV, false);
        buttonAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AboutSection about = new AboutSection();
            }
        });
        buttonAbout.setTooltip(getToolTip("About"));
        this.getChildren().add(buttonAbout);
        
        
    }
    
}
