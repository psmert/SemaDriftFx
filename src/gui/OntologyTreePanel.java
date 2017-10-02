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
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.DEBUG;
import semanticdriftmetrics.Constructors.OntClass;
import semanticdriftmetrics.Constructors.OntProperty;

/**
 *
 * @author staxos
 */
public class OntologyTreePanel {

    private TreeView OntTree;

    OntologyTreePanel(int randSize) {
        this.OntTree = new TreeView();
        //buildDummyTree(0, this.OntTree);
        this.OntTree.setMaxHeight(Double.MAX_VALUE);
    }

    private Node generateIcon(String resourcePath) {
        return new ImageView(new Image(getClass().getResourceAsStream(resourcePath)));
    }

    private void buildDummyTree(int randSize, TreeView targetTV) {
        TreeItem<String> rootItem = new TreeItem<String>("owl:Thing", generateIcon("/graphics/ontology_class.png"));
        rootItem.setExpanded(true);
        for (int i = 1; i < randSize; i++) {
            TreeItem<String> item = new TreeItem<String>("Class" + i, generateIcon("/graphics/ontology_class.png"));
            rootItem.getChildren().add(item);
        }
        TreeView<String> tree = new TreeView<String>(rootItem);
        targetTV.setRoot(rootItem);
    }

    TreeView getOntTree() {
        return this.OntTree;
    }

    public void buildOntTreeView(int sourcePosition, SeeDriftCore seeDriftCore) {

        ArrayList<OntClass> OntTree_ = seeDriftCore.getTree(sourcePosition);
        if (DEBUG) {
            System.out.println("Tree size for ontology " + sourcePosition + " is: " + OntTree_.size());
        }

        //add root
        TreeItem<String> rootItem = new TreeItem<String>("owl:Thing", generateIcon("/graphics/ontology_class.png"));
        rootItem.setExpanded(true);

        //add root nodes
        for (int i = 0; i < OntTree_.size(); i++) {

            TreeItem<String> item = buildOntClassSubTree(OntTree_.get(i));
            rootItem.getChildren().add(item);
        }
        this.OntTree.setRoot(rootItem);
    }

    private TreeItem<String> buildOntClassSubTree(OntClass ontClass) {
        //add node
        TreeItem<String> item = new TreeItem<String>(ontClass.getName(), generateIcon("/graphics/ontology_class.png"));

        //add subclasses
        for (OntClass subClass : ontClass.getSubclasses()) {
            item.getChildren().add(buildOntClassSubTree(subClass));
        }
        //add properties
        for (OntProperty property : ontClass.getProperties()) {
            TreeItem<String> propItem;
            
            //TODO custom tree view here
            if (property.isDatatype()) {
                propItem = new TreeItem<String>(property.getName() + " <range:" + property.getRange() +">", generateIcon("/graphics/ontology_data_property.png"));
            } else {
                propItem = new TreeItem<String>(property.getName() + " <range:" + property.getRange() +">", generateIcon("/graphics/ontology_obj_property.png"));
            }
            item.getChildren().add(propItem);
        }
        return item;

    }

}
