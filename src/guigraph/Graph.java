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

import java.util.ArrayList;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author staxos
 */
public class Graph {
    private ArrayList <GraphEdge> guiEdges;
    private ArrayList<GraphNode> guiNodes;
    
    private ArrayList<Double> distsFromDragStartX, distsFromDragStartY;
    
    
    private double dragStartX, dragStartY;
    
    
    public ArrayList<GraphNode> getGuiNodes(){
        return this.guiNodes;
    }
    
    public ArrayList<GraphEdge> getGuiEdges(){
        return this.guiEdges;
    }
    
    public Graph(){
        
    }
    
    public void selectAllNodesInsideRectangle( double startX, double startY, double endX, double endY){
       // System.out.println("Selecting");
        for ( int i = 0 ; i< this.guiNodes.size(); i++){
            if ( this.guiNodes.get(i).circle.getCenterX()> startX && 
                   this.guiNodes.get(i).circle.getCenterX()< endX &&
                    this.guiNodes.get(i).circle.getCenterY()> startY &&
                    this.guiNodes.get(i).circle.getCenterY()< endY){
                this.guiNodes.get(i).setSelected(true);
            }
        }
    }
    
    
    public GraphNode aNodeIsBeingPressed(){
        for ( int i = 0 ; i < this.guiNodes.size(); i++ ){
            if ( this.guiNodes.get(i).isPressed()){
                return this.guiNodes.get(i);
                
            }
        }
        return null;
    }
    
    
    public double getMinNodeX(){
        double minX = Double.MAX_VALUE ;
        for ( int i = 0 ; i < this.guiNodes.size(); i++){
            if ( guiNodes.get(i).circle.getCenterX() < minX ){
                minX = guiNodes.get(i).circle.getCenterX();
            }
        }
        return minX;
    }
    private double getMaxNodeX(){
        double maxX = 0 ;
        for ( int i = 0 ; i < this.guiNodes.size(); i++){
            if ( guiNodes.get(i).circle.getCenterX() > maxX ){
                maxX = guiNodes.get(i).circle.getCenterX();
            }
        }
        return maxX;
        
    }
    public double getMinNodeY(){
        double minY = Double.MAX_VALUE ;
        for ( int i = 0 ; i < this.guiNodes.size(); i++){
            if ( guiNodes.get(i).circle.getCenterY() < minY ){
                minY = guiNodes.get(i).circle.getCenterY();
            }
        }
        return minY;
    }
    private double getMaxNodeY(){
        double maxY = 0 ;
        for ( int i = 0 ; i < this.guiNodes.size(); i++){
            if ( guiNodes.get(i).circle.getCenterY() > maxY ){
                maxY = guiNodes.get(i).circle.getCenterY();
            }
        }
        return maxY;
    }
    
    public double getWidth(){
        return getMaxNodeX() - getMinNodeX();
    }
    
    public double getHeight(){
        return getMaxNodeY() - getMinNodeY();
    }
    
    
    
    
    
    
    public Graph getSubGraphOfSelectedItems(){
        
        Graph g = new Graph();
        ArrayList<GraphNode> selectedNodes = new ArrayList<>();
        for ( int i = 0 ; i < this.guiNodes.size(); i++){
            if ( this.guiNodes.get(i).isSelected()){
                selectedNodes.add( this.guiNodes.get(i).getCopy() );
            }
        } 
        
        ArrayList<GraphEdge> copyOfEdges = new ArrayList<>();
        for ( int i = 0 ; i < this.guiEdges.size(); i++){
            copyOfEdges.add( this.guiEdges.get(i).getCopy() ); 
        }
        
        //Select Only the edges that have both begin and end points in selectedNodes
        ArrayList<GraphEdge> selectedEdges = new ArrayList<>();
        for ( int i = 0 ; i < copyOfEdges.size(); i++){
            boolean hasStart = false, hasEnd = false;
            for ( int j = 0 ; j < selectedNodes.size(); j++){
                if ( selectedNodes.get(j).edgeStartsOnThisNode( copyOfEdges.get(i).getStartX(), copyOfEdges.get(i).getStartY()) ){
                    hasStart = true;
                }
                if ( selectedNodes.get(j).edgeEndsOnThisNode( copyOfEdges.get(i).getEndX(), copyOfEdges.get(i).getEndY()) ){
                    hasEnd = true;
                }
            }
            if ( hasStart && hasEnd ){
                selectedEdges.add(copyOfEdges.get(i).getCopy());
            }
        }
        
        //Do Node-Edge Associations
        for ( int i = 0 ; i < selectedNodes.size(); i++){
            for ( int j = 0 ; j < selectedEdges.size(); j++){
                selectedNodes.get(i).addEdge(selectedEdges.get(j));
            }
        }
        for ( int i = 0 ; i < selectedEdges.size(); i++ ){
            for ( int j = 0 ; j < selectedNodes.size(); j++){
                selectedEdges.get(i).checkAndAddNodeifStartOrEnd(selectedNodes.get(j));
            }
        }
        
        //set graph g
        g.setNodes(selectedNodes);
        g.setEdges(selectedEdges);
        
        return g;
    }
    
    public int getNoLevels(){
        if ( this.guiNodes.size() == 0 ){
            return 0;
        }
        int maxL = 0 ;
        for ( int i = 0; i < this.guiNodes.size(); i++){
            if ( maxL < this.guiNodes.get(i).getLevel()){
                maxL = this.guiNodes.get(i).getLevel();
            }
        }
        return maxL+1;
    }
    
    
    public ArrayList<GraphNode> getNodesOfLevel(int level){
        ArrayList<GraphNode> nodesOfLevel = new ArrayList<>();
        for ( int i = 0 ; i < this.guiNodes.size(); i++){
            if ( this.guiNodes.get(i).getLevel() == level){
                nodesOfLevel.add(this.guiNodes.get(i));
            }
        }
        return nodesOfLevel;
    }
    
    
    
    
    
    public SubGraphTable getTable_forDrift(int d){
        
        SubGraphTable subGraphTable = new SubGraphTable();
        
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        
        ArrayList<GraphNode> gNodes = this.getGuiNodes(); // selected nodes
        ArrayList<GraphEdge> gEdges = this.getGuiEdges(); // selected edges
        
        
        // remove nodes with no edges
        for ( int i = 0 ; i < gNodes.size(); i++){
            if ( !gNodes.get(i).hasEdges() ){
                gNodes.remove(i);
                i--;
            }
        }
        
        System.out.println(" SubGraph has "+gNodes.size()+" nodes and "+gEdges.size() + "edges");
        
        // create table of Drift - d
        ArrayList<GraphNode> nodesOfL1 = this.getNodesOfLevel(d);
        ArrayList<GraphNode> nodesOfL2 = this.getNodesOfLevel(d+1);
        
        if ( nodesOfL1 == null || nodesOfL2 == null || nodesOfL1.size() == 0 || nodesOfL2.size() == 0){
            if ( nodesOfL1 == null ) {
                System.out.println("nodesOfL1 Null");
            }
            if ( nodesOfL2 == null ) {
                System.out.println("nodesOfL2 Null");
            }
            if ( nodesOfL1.size() == 0){
                System.out.println("nodesOfL1.size()==0");
                
            }
            if ( nodesOfL2.size() == 0){
                System.out.println("nodesOfL2.size()==0");
            }
            
            return null;
        }
        
        ArrayList<String> firstRow = new ArrayList<>();
        firstRow.add("%");
        for ( int c= 0 ; c < nodesOfL2.size(); c++){
            firstRow.add( nodesOfL2.get(c).label.getText() );
        }
        table.add(firstRow);
        
        for ( int r = 0 ; r < nodesOfL1.size(); r++ ){
            ArrayList<String> row = new ArrayList<>();
            row.add(nodesOfL1.get(r).label.getText());
            
            for ( int c = 0 ; c < nodesOfL2.size(); c++){
                GraphEdge connectingEdge = nodesOfL2.get(c).getEdgeThatEndsHereAndStartsOnNode(nodesOfL1.get(r));
                if ( connectingEdge != null ){
                    row.add(connectingEdge.labelWeight.getText());
                } else {
                    row.add("0,000");
                }
                
            }
            
            table.add(row);
        }        
        
        subGraphTable.setTable(table);
        subGraphTable.setTitle ( nodesOfL1.get(0).labelOntDetail.getText() + "-" + nodesOfL2.get(0).labelOntDetail.getText());
        
        return subGraphTable;
    }
    
    
    
    
    
    public void setDragStartPoint( double dragStartX, double dragStartY ){
        this.dragStartX = dragStartX;
        this.dragStartY = dragStartY;
        
        this.distsFromDragStartX = new ArrayList<>();
        this.distsFromDragStartY = new ArrayList<>();
        
        for ( int i = 0 ; i < this.guiNodes.size(); i++){
            this.distsFromDragStartX.add( this.guiNodes.get(i).circle.getCenterX() - dragStartX);
            this.distsFromDragStartY.add( this.guiNodes.get(i).circle.getCenterY() - dragStartY);
        }
    }
    
    
    
    
    
    public void setNodes ( ArrayList<GraphNode> nodes){
        this.guiNodes = nodes;
    }
    
    public void setEdges( ArrayList<GraphEdge> guiEdges){
        this.guiEdges = guiEdges;
    }
    
    public boolean isEmpty(){
        return this.guiNodes == null || this.guiNodes.size() == 0;
    }
    
    /*
    public void zoom ( double deltaY, double mouseX, double mouseY ){
        if ( this.guiNodes == null || this.guiNodes.size() == 0 ){
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
        for ( int i = 0 ; i < this.guiNodes.size(); i++ ){
            this.guiNodes.get(i).setCenter(this.guiNodes.get(i).circle.getCenterX()*zoomFactor+translateX, this.guiNodes.get(i).circle.getCenterY()*zoomFactor+translateY);
        }        
        
    }*/
    
    public void scaleGuiNodes( double scaleX, double scaleY){
        for ( int i = 0 ; i < this.guiNodes.size(); i++ ){
            this.guiNodes.get(i).setCenter(this.guiNodes.get(i).circle.getCenterX()*scaleX, this.guiNodes.get(i).circle.getCenterY()*scaleY);
        } 
    }
    
    
    public void translateGuiNodes(double trValueX, double trValueY){
        for ( int i = 0 ; i < this.guiNodes.size(); i++ ){
            this.guiNodes.get(i).setCenter(this.guiNodes.get(i).circle.getCenterX()+trValueX, this.guiNodes.get(i).circle.getCenterY()+trValueY);            
        }
    }
    
    public void dragSelectedGuiNodes( double mouseX, double mouseY){
        for ( int i = 0 ; i < this.guiNodes.size(); i++ ){
            if ( this.guiNodes.get(i).isSelected()){
                double diffX = mouseX+  this.distsFromDragStartX.get(i);
                double diffY = mouseY + this.distsFromDragStartY.get(i);
                this.guiNodes.get(i).setCenter(diffX, diffY); 
            }
        }
    }
    
    public void setGuiNodesMouseDragEvent(boolean value){
        if ( this.guiNodes != null){
            for ( int i = 0 ;i < this.guiNodes.size(); i++){
                this.guiNodes.get(i).setMouseDragEvent(value);
            }
        }
    }
    
    
    
    
}
