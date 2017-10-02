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

import core.SeeDriftCore;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author staxos
 */
public class OntologyToolBar extends BorderPane {
    
    private OntologyControlBar ontologyControlBar;
    private OntologyTreePanel ontologyTreePanel;
    private Text OntDetails;
    private String OntFilePath;
    int ont_no;
    
    OntologyToolBar(Stage primaryStage, SemaDriftFx seeDriftFXNew, int randsize, int ont_no){
        
        this.ont_no = ont_no;
        boolean disableLeftRightRefresh = true;
        this.ontologyControlBar = new OntologyControlBar(primaryStage, seeDriftFXNew, ont_no, disableLeftRightRefresh);
        this.ontologyTreePanel = new OntologyTreePanel(randsize);
        this.setTop(ontologyControlBar);
        this.setCenter(ontologyTreePanel.getOntTree());
        this.setMaxHeight(Double.MAX_VALUE);
        
        this.OntDetails = new Text();
        String textStyle = "-fx-fill:" + ColorTheme.ToolbarBg; //styling
        /*other options
         + "    -fx-font: 100px Tahoma;\n"
         + "    -fx-stroke: black;\n"
         + "    -fx-stroke-width: 1;";
         */
        this.OntDetails.setStyle(textStyle);
        
        this.setBottom( this.OntDetails );
        
        this.OntFilePath = "";
        setHighLightedOpenButton(true);
    }
    
    
    
    public void setOntFilePath ( String ontFilePath ){
        this.OntFilePath = ontFilePath;
    }
    
    public void setOntDetails( String ontDetails ){
        this.OntDetails.setText(ontDetails);
    }
    
    
    public String getOntFilePath(){
        return this.OntFilePath;
    }
    
    public Text getOntDetails(){
        return this.OntDetails;
    }
    
    public TreeView getOntTree(){
        return this.ontologyTreePanel.getOntTree();
    }
    
    
    public void buildOntTreeView(SeeDriftCore seeDriftCore){        
        this.ontologyTreePanel.buildOntTreeView(this.ont_no, seeDriftCore);
    }
    
    
    
    public void set_ont_no(Stage primaryStage, SemaDriftFx seeDriftFXNew, int new_ont_no, boolean disableRemoveButton){
        this.ont_no = new_ont_no;
        boolean disableLeftRightRefresh = true;
        if ( this.isLoaded() ){
            disableLeftRightRefresh = false;
        }
        //System.out.println("ONT:"+ ont_no+ " isLoaded:"+this.isLoaded());
        this.ontologyControlBar = new OntologyControlBar(primaryStage, seeDriftFXNew, this.ont_no, disableLeftRightRefresh);
        this.setTop(ontologyControlBar);
        
        if ( disableRemoveButton ){
            this.ontologyControlBar.buttonRemove.setDisable(true);
        }
    }
    
    public int get_ont_no(){
        return this.ont_no;
    }
    
    public boolean removeButtonIsDisabled(){
        return this.ontologyControlBar.buttonRemove.isDisabled();
    }
    public void setDisableRemoveButton( boolean value ){
        this.ontologyControlBar.buttonRemove.setDisable(value);
    }
    
    public void disableController(boolean setDisableVal){
        disableOpenButton(setDisableVal);
        
        this.ontologyTreePanel.getOntTree().setDisable(setDisableVal);
        
        if ( this.isLoaded() ){
            this.ontologyControlBar.buttonReload1.setDisable(setDisableVal);
            this.ontologyControlBar.buttonLeft.setDisable(setDisableVal);
            this.ontologyControlBar.buttonRight.setDisable(setDisableVal);
        }
        
        if ( !setDisableVal && !this.isLoaded() ){
            this.ontologyControlBar.buttonOpen1.setID("highlighted");
        }
    }
    
    public void disableOpenButton (boolean setDisableVal){
        this.ontologyControlBar.buttonOpen1.setDisable(setDisableVal);
        if ( !this.isLoaded() ){
            setHighLightedOpenButton(!setDisableVal);
        }
    }
    public void disableTreePanel (boolean setDisableVal){
        this.ontologyTreePanel.getOntTree().setDisable(setDisableVal);
    }
    
    public boolean isDisabled_(){
        return this.ontologyControlBar.buttonOpen1.isDisabled();
    }
    
    public boolean isLoaded() {
        if ( this.OntFilePath.equals("") ){
            return false;
        }
        return true;
    }
    
    
    
    public void setHighLightedOpenButton( boolean value ){
        if ( this.ontologyControlBar.buttonOpen1!=null){
            if ( value ){
            this.ontologyControlBar.buttonOpen1.setID("highlighted");
            } else {
                this.ontologyControlBar.buttonOpen1.setID("");
            }
            
        }
    }
    
    
}
