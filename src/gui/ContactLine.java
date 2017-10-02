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
package gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author staxos
 */
public class ContactLine extends HBox{
    
    
    
    ContactLine( String name, String email){
        
        Font txtFont = Font.font("Verdana", FontWeight.NORMAL, 12);
        
        Label nameText = new Label(name);
        nameText.setPadding(new Insets(4,0,0,0));
        nameText.setFont(txtFont);        
        this.getChildren().add(nameText);
        
        
        SelectableText emailField = new SelectableText(email);
        this.getChildren().add(emailField);
        
        
    }
    
}
