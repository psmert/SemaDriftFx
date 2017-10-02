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
public class StabilityData {

    public ArrayList<String> colNames;
    public ArrayList<String> rowNames;
    public ArrayList<ArrayList<String>> tableValues;

    public int getRowIndex(String s) {
        for (int i = 0; i < rowNames.size(); i++) {
            if (this.rowNames.get(i).equals(s)) {
                return i;
            }
        }

        return -1;
    }

    public String getRowString(int rowIdx) {
        return this.rowNames.get(rowIdx);
    }

    public String getMaxForRow(String rowName) {
        int rowIdx = getRowIndex(rowName);
        if (rowIdx == -1) {
            return "";
        }

        double maxVal = Double.parseDouble(this.tableValues.get(rowIdx).get(0).replace(",", "."));
        int maxColIdx = 0;

        for (int colIdx = 0; colIdx < this.colNames.size(); colIdx++) {
            if (maxVal < Double.parseDouble(this.tableValues.get(rowIdx).get(colIdx).replace(",", "."))) {
                maxVal = Double.parseDouble(this.tableValues.get(rowIdx).get(colIdx).replace(",", "."));
                maxColIdx = colIdx;
            }
        }

        return this.tableValues.get(rowIdx).get(maxColIdx);

    }

    public ArrayList<String> getColNamesWithWeightGreaterThan(String rowName, double threshold) {

        ArrayList<String> colNamesWithWeightGTth = new ArrayList<String>();
        int rowIdx = getRowIndex(rowName);

        for (int colIdx = 0; colIdx < this.colNames.size(); colIdx++) {
            if (Double.parseDouble(this.tableValues.get(rowIdx).get(colIdx).replace(",", ".")) >= threshold) {
                colNamesWithWeightGTth.add(this.colNames.get(colIdx));
            }
        }

        return colNamesWithWeightGTth;
    }

    public ArrayList<String> getWeightsForSpecificRowAndColArrayList(String rowName, ArrayList<String> columnNames) {

        int rowIdx = getRowIndex(rowName);
        ArrayList<String> weights = new ArrayList<String>();
        for (int i = 0; i < columnNames.size(); i++) {
            for (int colIdx = 0; colIdx < this.colNames.size(); colIdx++) {
                if (this.colNames.get(colIdx).equals(columnNames.get(i))) {
                    weights.add(this.tableValues.get(rowIdx).get(colIdx));
                }
            }
        }

        return weights;
    }
}
