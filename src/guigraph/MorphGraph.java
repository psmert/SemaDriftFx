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

import java.util.ArrayList;

/**
 *
 * @author staxos
 */
public class MorphGraph {
    
    
    private ArrayList<StabilityData> stabilityData;
    private MorphNode root;
    
    private ArrayList<ArrayList<MorphNode>> parentsOfLevel;
    private ArrayList<MorphNode> leaves;
    
    MorphGraph( ArrayList<StabilityData> stabilityData) {
        this.stabilityData = stabilityData;
        this.parentsOfLevel = new ArrayList<>();
    }
    
    public int getMaxNumberOfNodesOnASingleLevel(){
        int maxNumberOfNodes = 0;
        for ( int level = 0 ; level < this.parentsOfLevel.size(); level++ ){
            if ( maxNumberOfNodes < this.parentsOfLevel.get(level).size()){
                maxNumberOfNodes = this.parentsOfLevel.get(level).size();
            }
        }
        if( this.leaves!=null && maxNumberOfNodes< this.leaves.size()){
            maxNumberOfNodes = this.leaves.size();
        }
        return maxNumberOfNodes;
    }
    
    public int getLevels(){
        return this.parentsOfLevel.size();
    }
    
    
    public ArrayList<MorphNode> getLeaves(){
        return this.leaves;
    }
    
    
    
    
    public ArrayList<MorphNode> getParentsOfLevel(int level){
        if ( this.parentsOfLevel.size() ==0){
             System.out.println("Error  parentsOfLevel.size=0");
        }
        if (level > this.parentsOfLevel.size()) {
            System.out.println("Error level>parentsOfLevel.size");
        }
        
        return parentsOfLevel.get(level);
    }
    
    
    
    
    
    public void buildGraphForLabel(String rowName, double threshold){
        
        root = new MorphNode(rowName);
        
        
        ArrayList<MorphNode> parentsInThisRound = new ArrayList<>();
        ArrayList<MorphNode> childrenInThisRound = new ArrayList<>();
        
        
        
        
        for ( int round = 0 ; round < stabilityData.size(); round++){
            
            if ( round == 0 ){
                parentsInThisRound.add(root);
            } else {
                parentsInThisRound = childrenInThisRound;
            }
            
            childrenInThisRound = new ArrayList<>();
      
            for (int parent = 0; parent < parentsInThisRound.size(); parent++) {
                ArrayList<String> childrenNodeNames = stabilityData.get(round).getColNamesWithWeightGreaterThan(rowName, threshold);
                ArrayList<String> weights = stabilityData.get(round).getWeightsForSpecificRowAndColArrayList(rowName, childrenNodeNames);
                
                
                ArrayList<MorphNode> childrenNodes = new ArrayList<>();
                for (int child = 0; child < childrenNodeNames.size(); child++) {

                    boolean alreadyExists = false;
                    for (int i = 0; i < childrenInThisRound.size(); i++) {
                        if (childrenNodeNames.get(child).equals(childrenInThisRound.get(i).name)) {
                            System.out.println( childrenNodeNames.get(child) +" EXISTS!");
                            alreadyExists = true;
                            childrenNodes.add( childrenInThisRound.get(i) );//
                        }
                    }
                    if (!alreadyExists) {
                        childrenNodes.add(new MorphNode(childrenNodeNames.get(child)));
                    }
                }
                
                parentsInThisRound.get(parent).weights = weights;
                parentsInThisRound.get(parent).nextNodes = childrenNodes;
                
                //childrenInThisRound.addAll(childrenNodes);
                
                for ( int i = 0 ; i < childrenNodes.size(); i++ ){
                    boolean exists = false;
                    for ( int j = 0 ; j < childrenInThisRound.size(); j++){
                        if ( childrenNodes.get(i).name.equals(childrenInThisRound.get(j).name)){
                            exists = true;
                        }
                    }
                    if ( !exists ){
                        childrenInThisRound.add(childrenNodes.get(i));
                    }
                }
                
                
                if ( round == this.stabilityData.size()-1 ){
                    this.leaves = childrenInThisRound;
                }
                
            }
            this.parentsOfLevel.add(parentsInThisRound);
        }       
        
        
        
        
        
    }
    
    
    
    
    
    
    
    
    
    public void buildGraphForLabel(String rowName, double threshold, int fromIdx, int toIdx){
        
        root = new MorphNode(rowName);
        
        
        ArrayList<MorphNode> parentsInThisRound = new ArrayList<>();
        ArrayList<MorphNode> childrenInThisRound = new ArrayList<>();
        
        
        
        
        for ( int round = fromIdx ; round < toIdx; round++){
            
            if ( round == fromIdx ){
                parentsInThisRound.add(root);
            } else {
                parentsInThisRound = childrenInThisRound;
            }
            
            childrenInThisRound = new ArrayList<>();
      
            for (int parent = 0; parent < parentsInThisRound.size(); parent++) {
                ArrayList<String> childrenNodeNames = stabilityData.get(round).getColNamesWithWeightGreaterThan(parentsInThisRound.get(parent).name, threshold);
                ArrayList<String> weights = stabilityData.get(round).getWeightsForSpecificRowAndColArrayList(parentsInThisRound.get(parent).name, childrenNodeNames);
                
                
                ArrayList<MorphNode> childrenNodes = new ArrayList<>();
                for (int child = 0; child < childrenNodeNames.size(); child++) {

                    boolean alreadyExists = false;
                    for (int i = 0; i < childrenInThisRound.size(); i++) {
                        if (childrenNodeNames.get(child).equals(childrenInThisRound.get(i).name)) {
                           // System.out.println( childrenNodeNames.get(child) +" EXISTS!");
                            alreadyExists = true;
                            childrenNodes.add( childrenInThisRound.get(i) );//
                        }
                    }
                    if (!alreadyExists) {
                        childrenNodes.add(new MorphNode(childrenNodeNames.get(child)));
                    }
                }
                
                parentsInThisRound.get(parent).weights = weights;
                parentsInThisRound.get(parent).nextNodes = childrenNodes;
                
                //childrenInThisRound.addAll(childrenNodes);
                
                for ( int i = 0 ; i < childrenNodes.size(); i++ ){
                    boolean exists = false;
                    for ( int j = 0 ; j < childrenInThisRound.size(); j++){
                        if ( childrenNodes.get(i).name.equals(childrenInThisRound.get(j).name)){
                            exists = true;
                        }
                    }
                    if ( !exists ){
                        childrenInThisRound.add(childrenNodes.get(i));
                    }
                }
                
                
                if ( round == this.stabilityData.size()-1 || round == toIdx-1){
                    this.leaves = childrenInThisRound;
                }
                
            }
            this.parentsOfLevel.add(parentsInThisRound);
        }    
        
        
        
        
        
        
    }
    
    
    
}
