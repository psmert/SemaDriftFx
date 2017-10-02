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

/**
 *
 * @author staxos
 */
public class PairData {
    public StabilityData labelPairData;
    public StabilityData intensionPairData;
    public StabilityData extensionPairData;
    public StabilityData wholePairData;
    
    public PairData( StabilityData labelPairData, StabilityData intensionPairData, StabilityData extensionPairData, StabilityData wholePairData){
        this.labelPairData = labelPairData;
        this.intensionPairData = intensionPairData;
        this.extensionPairData = extensionPairData;
        this.wholePairData = wholePairData;
    }
    
    public StabilityData getPairDataType(int idx){
        switch(idx){
            case 0:
                return this.labelPairData;
            case 1:
                return this.intensionPairData;
            case 2:
                return this.extensionPairData;
            case 3:
                return this.wholePairData;
            default:
                
                System.out.println("PairData.getPairData() index out of bounds!");
                break;
        }
        return null;
                
    }
}
