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

import java.util.ArrayList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author staxos
 */
public class MyTable extends TableView {
    public ArrayList<String> columnNames;

    public MyTable(ArrayList<String> colNames, boolean editable, int colMinWidth) {
        this.columnNames = new ArrayList<>();
        this.setEditable(editable);

        for (int i = 0; i < colNames.size(); i++) {
            TableColumn newCol = new TableColumn(colNames.get(i));
            newCol.setMinWidth(colMinWidth);
            newCol.setCellValueFactory(
                    new PropertyValueFactory<>(colNames.get(i)));
            this.getColumns().add(newCol);
        }
        
        

    }
    
    public void addColumnName(String colName){
        this.columnNames.add(colName);
    }
    public String getColName(int index){
        return this.columnNames.get(index);
    }
    
    public MyTable(){
        this.columnNames = new ArrayList<>();
    }

}
