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
public class MorphNode {
    public ArrayList<MorphNode> nextNodes;
    public ArrayList<String> weights;
    public String name;
    public int x;
    public int y;
    
    MorphNode( String name ){
        this.name = name;
    }
    
    MorphNode( String name, ArrayList<MorphNode> nextNodes, ArrayList<String> weights ){
        this.name = name;
        this.nextNodes = nextNodes;
        this.weights = weights;
    }
    
    boolean hasNext(){
        if ( nextNodes.size() == 0 ){
            return false;
        }
        return true;
    }
}
