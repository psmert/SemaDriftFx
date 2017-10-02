/*
 *   Copyright 2016 CERTH-ITI (http://certh.gr, http://iti.gr)
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import semanticdriftmetrics.Constructors.AverageDrift;
import semanticdriftmetrics.Constructors.OntClass;
import semanticdriftmetrics.Constructors.Version;
import semanticdriftmetrics.Constructors.VersionPairs;
import semanticdriftmetrics.Exceptions.OntologyCreationException;
import semanticdriftmetrics.SemanticDrift;
import semanticdriftmetrics.SemanticDriftMetrics;

/**
 *
 * @author UpLPIS
 */
public class SeeDriftCore extends Task {

    private static boolean DEBUG = true; //inherited from Fx
    //for the Task
    public BooleanProperty buttonDisabledProperty;
    private String mode; //call either gui or wars

    //Properties
    private String server = "160.40.51.52";
    private int port = 21;
    private String user = "ftpuser";
    private String pass = "d3mc@r3";

    private int delay;

    //backend
    private SemanticDrift semantic;

    public SeeDriftCore(boolean DEBUG) {

        semantic = new SemanticDrift();
        this.DEBUG = DEBUG;
        try {
            //initialize
            semantic.addVersionFile("");
            semantic.addVersionFile("");
        } catch (OntologyCreationException ex) {
            System.err.println("wrong");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SeeDriftCore.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param filePath
     * @throws org.semanticweb.owlapi.model.OWLOntologyCreationException
     */
    public void addOntology(String filePath) throws OntologyCreationException, FileNotFoundException {
        semantic.addVersionFile(filePath);
        if (DEBUG) {
            System.out.println("Semantic core added Version: " + filePath + "\nCurrent Versions: " + printVersions());
        }
    }

    public void clearOntologies() {
        semantic.clear();
        if (DEBUG) {
            System.out.println("Semantic core is cleared: " + printVersions());
        }
    }

    private String printVersions() {
        String out = new String();
        for (Version v : semantic.getVersions()) {
            out += v.getName();
            out += " ";
        }
        return out;
    }

    public ArrayList<AverageDrift> getAverageDrift() {
        return semantic.getAverageDrift();
    }

    /**
     * Get concept per concept pairs
     *
     * @return
     */
    public ArrayList<VersionPairs> getWholeVersionPairs() {
        return semantic.getWholeVersionPairs();
    }

    public ArrayList<VersionPairs> getLabelVersionPairs() {
        return semantic.getLabelVersionPairs();
    }

    public ArrayList<VersionPairs> getExtensionVersionPairs() {
        return semantic.getExtensionVersionPairs();
    }

    public ArrayList<VersionPairs> getIntensionVersionPairs() {
        return semantic.getIntensionVersionPairs();
    }

    /**
     * 0 or 1 for each ontology
     *
     * @param position
     */
    public ArrayList<OntClass> getTree(int position) {
        Version ont = semantic.getVersions().get(position);
        return ont.getTree();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SeeDriftCore demupdate = new SeeDriftCore(true);

//        demupdate.ftp.checkFTPport();
//        demupdate.updateWars();
//        demupdate.updateGUI();
    }

    public int updateGUI() {
        System.out.println("Checking connection...");
        System.out.println("Updating System GUI");
        String src = "/gui/";
        String dest = "./";
        File down = new File(dest);
        down.mkdir();
        //boolean success = true; //do not download

        return 1;
    }

    public int updateWars(int delay) {
        System.out.println("Checking connection...");

        System.out.println("Updating System Core war files");
        String src = "/wars/";
        String dest = "./";
        File down = new File(dest);
        down.mkdir();

        //boolean success = true;
        System.out.println("Wating for Tomcat to undeploy, for " + delay + "ms....");

        return 1;
    }

    //Method for the buttons - called upon Thread start
    @Override
    public Void call() {
        buttonDisabledProperty.set(true);
        //dummyTask(); //gradually fill the progress
        if (mode.equals("gui")) {
            this.updateGUI();
        } else if (mode.equals("wars")) {
            this.updateWars(delay);
        }
        buttonDisabledProperty.set(false);
        this.updateProgress(1, 1);
        return null;
    }

    public void dummyTask() {
        int max = 10000000;
        for (int i = 1; i <= max; i++) {
            if (isCancelled()) {
                break;
            }
            updateProgress(i, max);
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
