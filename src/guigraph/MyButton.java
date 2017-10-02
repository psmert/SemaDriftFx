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

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sun.plugin2.message.SetJVMIDMessage;

/**
 *
 * @author staxos
 */
public class MyButton extends Button{
    String defaultId;
    String hoveredId;
    
    public void onMouseEnteredAction(){
        this.defaultId = this.getId();
        this.setId(hoveredId);
    }
    public void onMouseExitedAction(){
        this.setId(defaultId);
    }
    
    
    private void initializeEvents(){
          
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseEnteredAction();
            }
        });
        
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseExitedAction();
            }
        });
    }
    
    
    public void setID(String idStr){
        this.defaultId = idStr;
        this.setId(idStr);
    }
    
    private void commonInitialization(boolean changeFontWeightOnHover){
        this.defaultId="";
        if (changeFontWeightOnHover){
            this.hoveredId = "hoveredchangefont";
        }else {
             this.hoveredId = "hovered";
        }
        this.getStylesheets().add(MyButton.class.getResource("/css/styles.css").toExternalForm());      
        initializeEvents();
    }
    
    public MyButton(String text, boolean changeFontWeightOnHover){
        super( text );
        commonInitialization(changeFontWeightOnHover);
    }
    
    
    
    public MyButton(String text, ImageView imgView, boolean changeFontWeightOnHover){
        super(text,imgView);
        commonInitialization(changeFontWeightOnHover);
    }
}
