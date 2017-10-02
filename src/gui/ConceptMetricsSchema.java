/*
 * Copyright 2016 athstavr.
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

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author athstavr
 */
public class ConceptMetricsSchema {
    
    private final SimpleStringProperty Label;
    private final SimpleStringProperty Intensional;
    private final SimpleStringProperty Extensional;
    private final SimpleStringProperty Whole;

    public ConceptMetricsSchema(String Label, String Intensional, String Extensional, String Whole) {
        this.Label = new SimpleStringProperty(Label);
        this.Intensional = new SimpleStringProperty(Intensional);
        this.Extensional = new SimpleStringProperty(Extensional);
        this.Whole = new SimpleStringProperty(Whole);
    }

    public String getLabel() {
        return Label.get();
    }

    public String getIntensional() {
        return Intensional.get();
    }

    public String getExtensional() {
        return Extensional.get();
    }

    public String getWhole() {
        return Whole.get();
    }
    
    public void setLabel(String Label) {
        this.Label.set(Label);
    }

    public void setIntensional(String intensional) {
        this.Intensional.set(intensional);
    }

    public void setExtensional(String extensional) {
        this.Extensional.set(extensional);
    }

    public void setWhole(String whole) {
        this.Whole.set(whole);
    }
           
    
}
