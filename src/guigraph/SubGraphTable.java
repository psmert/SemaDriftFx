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

/**
 *
 * @author staxos
 */
public class SubGraphTable {
    
    ArrayList< ArrayList <String>> table;
    String title;
    
    public void setTable( ArrayList<ArrayList< String>> t ){
        this.table = t;
    }
    public String getTitle(){
        return this.title;
    }
    
    public ArrayList<String> getRowNames(){
        ArrayList<String> rowNames = new ArrayList<>();
        for ( int r = 0 ; r < this.table.size(); r++ ){
            rowNames.add(this.table.get(r).get(0));
        }
        return rowNames;
    }
    
    public ArrayList<String> getColNames(){
        return this.table.get(0);
    }
    
    public String getValueAt( int r, int c ){
        return this.table.get(r).get(c);
    }
    
    public void setTitle( String title ){
        this.title = title;
    }
    
    public void print(){
        System.out.println(title);
        for ( int r = 0 ; r< table.size(); r++){
            for ( int c = 0 ; c < table.get(r).size(); c++){
                System.out.print(table.get(r).get(c) + "  ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
    
}
