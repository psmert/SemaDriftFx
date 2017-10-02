/*
 *    Copyright 2016 CERTH-ITI (http://certh.gr, http://iti.gr)
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

/*

 Main
 - Toolbar HBox
 |  
 -
 - HBox Main
 |  -BpOnt1  - BpOnt2
 |   |       |
 -

 */
package gui;

import core.SeeDriftCore;
import guigraph.MorphingChainGUI;
import guigraph.MyButton;
import guigraph.PairData;
import guigraph.StabilityData;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import semanticdriftmetrics.Constructors.AverageDrift;
import semanticdriftmetrics.Constructors.VersionPairs;
import semanticdriftmetrics.Exceptions.OntologyCreationException;

/**
 *
 * @author UpLPIS
 */
public class SemaDriftFx extends Application {

    //control variables
    public static String version = "1.05";
    private static SeeDriftCore seeDriftCore;
    private static boolean DEBUG = false;

    //GUI Global components
    private ArrayList<OntologyToolBar> ontologyToolBars;
    private RunToolBar runToolBar;
    private TablePanel initialTablePanel;
    private ArrayList<TablePanel> tablePanels;
    private Pagination tablePanelPagination;
    private VBox vboxRun;
    private BorderPane border;
    private HBox hboxMain;
    private HBox ontToolBarHBox;
    private Scene scene;
    public Stage primaryStage;
    ScrollPane scrollPane;
    private Thread myThread;
    //private Button buttonDetach;

    private ArrayList<PairData> pairData;

    private Tooltip getToolTip(String msg) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(msg);
        return tooltip;
    }

    //items
    public SemaDriftFx() {
        this.seeDriftCore = new SeeDriftCore(DEBUG);
        this.ontologyToolBars = new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) { //added final

        this.primaryStage = primaryStage;

        if (DEBUG) {
            System.out.println("Working dir: " + new File("./").getAbsolutePath());
        }

        //material from 
        //https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm#CHDGHCDG
        border = new BorderPane();
        AnchorPane aPane = buildToolbarHBox();
        border.setTop(aPane);

        this.hboxMain = buildHBoxMain(primaryStage);
        border.setCenter(this.hboxMain);

//      addStackPane(hbox);         // Add stack to HBox in top region
//      border.setCenter(addGridPane());
//      border.setRight(addFlowPane());
        this.scene = new Scene(border, 1035, 720);
        this.scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                onResizeAction();
            }
        });

//        primaryStage.setTitle("SemaDriftFx " + version);
        primaryStage.setTitle("SemaDriftFx");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        primaryStage.setResizable(true);

        //TODO set minimum size
        primaryStage.setScene(scene);
        this.scene.getStylesheets().add(SemaDriftFx.class.getResource("/css/styles.css").toExternalForm());
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                Platform.exit();
            }
        });

        //DEBUGGING AND TESTING
        if (DEBUG) {
            System.out.println("TEST: Setting ont 1...");
            setFiletoOnt(new File("C:\\Dropbox\\AmI\\PERICLES Project\\SemanticDriftMetrics\\versions\\2004\\tate_2004.owl"), 0);
            System.out.println("TEST: Setting ont 2...");
            setFiletoOnt(new File("C:\\Dropbox\\AmI\\PERICLES Project\\SemanticDriftMetrics\\versions\\2013\\tate_2013.owl"), 1);
        }
    }

    private AnchorPane buildToolbarHBox() {
        //Top toolbar
        AnchorPane aPane = new AnchorPane();
        aPane.setPadding(new Insets(15, 12, 15, 12));
        aPane.setStyle("-fx-background-color: " + ColorTheme.ToolbarBg + ";");

        //internal hbox for right alignment
        HBox hbox_Toolbar_right = new HBox();
        //image
        ImageView imageHouse = new ImageView(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        hbox_Toolbar_right.getChildren().add(imageHouse);
        //text
        Text title = new Text("  A Tool to Measure Semantic Concept Drift");
        title.setFont(Font.font("Segoe", FontWeight.MEDIUM, 22));
        title.setFill(Color.WHITE);
        hbox_Toolbar_right.getChildren().add(title);

        hbox_Toolbar_right.setAlignment(Pos.CENTER_LEFT);

        this.runToolBar = new RunToolBar(this, 10);
        runToolBar.setAlignment(Pos.CENTER_RIGHT);

        AnchorPane.setTopAnchor(hbox_Toolbar_right, 5.0);
        AnchorPane.setLeftAnchor(hbox_Toolbar_right, 10.0);
        AnchorPane.setRightAnchor(runToolBar, 10.0);
        AnchorPane.setTopAnchor(runToolBar, 15.0);

        aPane.getChildren().addAll(hbox_Toolbar_right, runToolBar);

        return aPane;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {

                if (arg.equals("debug")) {
                    DEBUG = true;
                    System.out.println("DEBUG mode enabled.");
                } else if (arg.contains("delay")) {
                    String delayStr = arg.substring(arg.lastIndexOf("delay") + 5, arg.length());
                    try {
                        int delayInt = Integer.parseInt(delayStr);
                    } catch (NumberFormatException ex) {
                        System.err.println("ERROR :: Wrong number format. The syntax is delayXXXX, where XXXX is an integer\n" + ex.getMessage());
                    }
                } else {
                    DEBUG = false;
                }
            }
        }
        launch(args);
    }

    private Node generateIcon(String resourcePath) {
        return new ImageView(new Image(getClass().getResourceAsStream(resourcePath)));
    }

    private void refreshOntologies() {

        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            addOntologyFileToCore(this.ontologyToolBars.get(i).getOntFilePath());
        }
    }

    private void addOntologyFileToCore(String filepath) {
        try {
            seeDriftCore.addOntology(filepath);
        } catch (OntologyCreationException ex) {
            String msg = "Error while opening ontology\nOntology File is: " + filepath + "\nThe message is: " + ex.getMessage();
            showErrorDialog(msg, ex);
        } catch (FileNotFoundException ex) {
            String msg = "Ontology file not found\nOntology File is: " + filepath + "\nThe message is: " + ex.getMessage();
            showErrorDialog(msg, ex);
        }
    }

    private void enableFirstEmptyOntToolBarAndDisableTheRest() {
        boolean enabledFirstEmpty = false;
        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            if (!this.ontologyToolBars.get(i).isLoaded() && enabledFirstEmpty) {
                this.ontologyToolBars.get(i).disableController(true);
                //System.out.println("DISABLING CONTROLLER " + i);
            }

            if (!this.ontologyToolBars.get(i).isLoaded() && !enabledFirstEmpty) {
                this.ontologyToolBars.get(i).disableController(false);
                //System.out.println("ENABLING CONTROLLER " + i);
                enabledFirstEmpty = true;
            }

        }
    }

    private void fixAndReloadToolBarOntologyNumbers(Stage primaryStage) {
        //System.out.println("FIX AND RELOAD");
        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            int index = this.ontToolBarHBox.getChildren().indexOf(this.ontologyToolBars.get(i));
            this.ontToolBarHBox.getChildren().remove(index);

            boolean disableRemoveButton = false;
            if (this.ontologyToolBars.size() == 1) {
                disableRemoveButton = true;
            }
            this.ontologyToolBars.get(i).set_ont_no(primaryStage, this, i, disableRemoveButton);

        }

        enableFirstEmptyOntToolBarAndDisableTheRest();

        for (int i = this.ontologyToolBars.size() - 1; i >= 0; i--) {
            this.ontToolBarHBox.getChildren().add(0, this.ontologyToolBars.get(i));
        }
    }

    public void removeButtonAction(int ont_no, Stage primaryStage) {
        boolean removedToolBarWasLoaded = false;
        if (this.ontologyToolBars.get(ont_no).isLoaded()) {
            this.runToolBar.setDisableButtonMorphChain(true);
            removedToolBarWasLoaded = true;
        }
        seeDriftCore.clearOntologies();
        this.ontToolBarHBox.getChildren().remove(this.ontologyToolBars.get(ont_no));
        this.ontologyToolBars.remove(ont_no);

        if (removedToolBarWasLoaded) {
            enableMeasureDrift();
        }

        fixAndReloadToolBarOntologyNumbers(primaryStage);
        refreshOntologies();
    }

    private void printOntologyToolBarsOnt_Nos() {
        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            System.out.println("[" + i + "]:" + this.ontologyToolBars.get(i).get_ont_no() + "  " + this.ontologyToolBars.get(i).getOntFilePath());
        }
    }

    // WITHOUT THREAD
    /*
     private void swapOntologyToolBars(int ont_no, int swapIndex, Stage primaryStage){  
     ArrayList<OntologyToolBar> ontToolBar = new ArrayList<>();
     ontToolBar.add(  this.ontologyToolBars.get(swapIndex)  );
     this.ontologyToolBars.set(swapIndex, ontologyToolBars.get(ont_no));
     this.ontologyToolBars.set(ont_no, ontToolBar.get(0));
     fixAndReloadToolBarOntologyNumbers(primaryStage);
     refreshOntologies();
     } */
    //===============================================================================
    //=============================WITH SPINNER SUPPORT =============================
    private void swapOntologyToolBars(int ont_no, int swapIndex, Stage primaryStage) {
        System.out.println("A");
        ArrayList<OntologyToolBar> ontToolBar = new ArrayList<>();
        System.out.println("B");
        ontToolBar.add(this.ontologyToolBars.get(swapIndex));
        System.out.println("C");
        this.ontologyToolBars.set(swapIndex, ontologyToolBars.get(ont_no));
        System.out.println("D");
        this.ontologyToolBars.set(ont_no, ontToolBar.get(0));
        System.out.println("E");

    }

    private void leftRightButtonTASK(int ont_no, int swapIndex, Stage primaryStage) {
        this.scene.setCursor(Cursor.WAIT);
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                swapOntologyToolBars(ont_no, swapIndex, primaryStage);
                return null;
            }
        };
        task.setOnSucceeded(e -> System.out.println("SUCCESS"));
        task.setOnFailed(e -> System.out.println("FAILURE"));
        myThread = new Thread(task);
        myThread.start();
    }
    //===============================================================================
    //===============================================================================

    public void leftButtonAction(int ont_no, Stage primaryStage) {

        if (this.ontologyToolBars.size() > 1 && this.ontologyToolBars.get(ont_no).isLoaded()) {
            seeDriftCore.clearOntologies();

            int swapIndex = ont_no - 1;
            if (swapIndex == -1) {
                swapIndex = this.ontologyToolBars.size() - 1;
            }

            while (!this.ontologyToolBars.get(swapIndex).isLoaded()) {
                swapIndex--;
                if (swapIndex == -1) {
                    swapIndex = this.ontologyToolBars.size() - 1;
                }
            }
            //swapOntologyToolBars(ont_no, swapIndex, primaryStage); // without Thread (Spinner Support)
            //========== SPINNER SUPPORT =====================//
            //=====================================================
            leftRightButtonTASK(ont_no, swapIndex, primaryStage);
            try {
                myThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaDriftFx.class.getName()).log(Level.SEVERE, null, ex);
            }
            fixAndReloadToolBarOntologyNumbers(primaryStage);
            //System.out.println("F");
            refreshOntologies();
            //System.out.println("G");
            scene.setCursor(Cursor.DEFAULT);
            //====================================================
            //====================================================

            this.runToolBar.setDisableButtonMorphChain(true);
            enableMeasureDrift();
        }
    }

    public void rightButtonAction(int ont_no, Stage primaryStage) {

        if (this.ontologyToolBars.size() > 1 && this.ontologyToolBars.get(ont_no).isLoaded()) {
            seeDriftCore.clearOntologies();

            int swapIndex = ont_no + 1;
            if (swapIndex == this.ontologyToolBars.size()) {
                swapIndex = 0;
            }

            while (!this.ontologyToolBars.get(swapIndex).isLoaded()) {
                swapIndex++;
                if (swapIndex == this.ontologyToolBars.size()) {
                    swapIndex = 0;
                }
            }

            //swapOntologyToolBars(ont_no, swapIndex, primaryStage);
            //========== SPINNER SUPPORT =====================//
            //=====================================================
            leftRightButtonTASK(ont_no, swapIndex, primaryStage);
            try {
                myThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SemaDriftFx.class.getName()).log(Level.SEVERE, null, ex);
            }
            fixAndReloadToolBarOntologyNumbers(primaryStage);
            //System.out.println("F");
            refreshOntologies();
            //System.out.println("G");
            scene.setCursor(Cursor.DEFAULT);
            //====================================================
            //====================================================

            this.runToolBar.setDisableButtonMorphChain(true);
            enableMeasureDrift();
        }
    }

    private void addOntButtonAction(Stage primaryStage) {
        int positionToInsertNewOnt = this.ontologyToolBars.size();
        this.ontologyToolBars.add(new OntologyToolBar(primaryStage, this, 0, positionToInsertNewOnt));
        if (thereAreMoreThan1EmptyOntologyToolBars()) {
            this.ontologyToolBars.get(this.ontologyToolBars.size() - 1).disableController(true);
        }
        if (this.ontologyToolBars.size() == 2 && this.ontologyToolBars.get(0).removeButtonIsDisabled()) {
            this.ontologyToolBars.get(0).setDisableRemoveButton(false);
        }
        this.ontToolBarHBox.getChildren().add(positionToInsertNewOnt, this.ontologyToolBars.get(this.ontologyToolBars.size() - 1));
    }

    public VBox createPage(int pageIndex) {
        VBox myVBox = new VBox();
        myVBox.getChildren().add(this.tablePanels.get(pageIndex));
        return myVBox;
    }

    private boolean thereAreMoreThan1EmptyOntologyToolBars() {
        int emptyOntologyToolBars = 0;
        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            if (!this.ontologyToolBars.get(i).isLoaded()) {
                emptyOntologyToolBars++;
            }
        }
        if (emptyOntologyToolBars > 1) {
            return true;
        }
        return false;
    }

    public void detachButtonAction() {
        // SeeDriftFXNew seeDriftFX = this;
        StackPane root = new StackPane();
        root.getChildren().add(this.tablePanelPagination);
        root.setPadding(new Insets(0, 15, 0, 15));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        stage.setTitle("Drift Measures");

        stage.setScene(new Scene(root, 800, 600));
        stage.getScene().getStylesheets().add(SemaDriftFx.class.getResource("/css/styles.css").toExternalForm());
        stage.show();
        /*  stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         @Override
         public void handle(WindowEvent event) {
         //seeDriftFX.vboxRun.getChildren().add(seeDriftFX.tablePanelPagination);
         for ( int i = 0 ; i < seeDriftFX.tablePanels.size(); i++){
         seeDriftFX.tablePanels.get(i).setStage(seeDriftFX.primaryStage);
         }
         }
         }); */

        for (int i = 0; i < this.tablePanels.size(); i++) {
            this.tablePanels.get(i).setStage(stage);
        }
    }

    private ArrayList<String> getCurrentOntDetails() {
        ArrayList<String> currentOntDetails = new ArrayList<>();
        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            if (this.ontologyToolBars.get(i).isLoaded()) {
                currentOntDetails.add(this.ontologyToolBars.get(i).getOntDetails().getText());
            }
        }
        return currentOntDetails;
    }

    public void morphChainButtonAction() {
        this.runToolBar.setHighlighted(this.runToolBar.getButtonMorphChain(), false);
        MorphingChainGUI morphChainGUI = new MorphingChainGUI(getCurrentOntDetails(), this.pairData);

    }

    private void onResizeAction() {
        this.scrollPane.setPrefWidth(this.scene.getWidth() - 60);
    }

    private HBox buildHBoxMain(Stage primaryStage) {//Main HBox

        HBox hbox_main = new HBox();
        hbox_main.setPadding(new Insets(15, 0, 30, 30)); //15 30 30 30
        //hbox_main.setSpacing(18);
        hbox_main.setStyle("-fx-background-color: " + ColorTheme.MainBg + ";"
                + "    -fx-background-insets: 0;");

        VBox vboxButtons = new VBox();
        vboxButtons.setPadding(new Insets(20, 0, 0, 0));
        vboxButtons.setSpacing(18);

        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(970);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
//        sPane.setStyle("-fx-border-color:#A3D9A3");
        scrollPane.setStyle("-fx-border-color: " + ColorTheme.MainBorder);
        this.ontToolBarHBox = new HBox();
        ontToolBarHBox.setPadding(new Insets(15, 18, 30, 18)); //15 30 30 30
        ontToolBarHBox.setSpacing(18);

        //Add Ontology ToolBars
        for (int i = 0; i < 2; i++) {
            this.ontologyToolBars.add(new OntologyToolBar(primaryStage, this, 0, i));
            if (thereAreMoreThan1EmptyOntologyToolBars()) {
                this.ontologyToolBars.get(this.ontologyToolBars.size() - 1).disableController(true);
            }

            ontToolBarHBox.getChildren().add(ontologyToolBars.get(ontologyToolBars.size() - 1));
            // hbox_main.getChildren().add(ontologyToolBars.get(ontologyToolBars.size()-1));
        }
        ontToolBarHBox.setStyle("-fx-background-color: #FDFFFB;");
        //ontToolBarHBox.setStyle("-fx-border-color:#E61D46;");
        ontToolBarHBox.setMaxWidth(Double.MAX_VALUE);

        scrollPane.setContent(ontToolBarHBox);
        hbox_main.getChildren().add(scrollPane);

        //Add "ADD ONTOLOGY TOOLBAR BUTTON"
        Image imgAddOnt = new Image(getClass().getResourceAsStream("/graphics/1482423588_sign-add.png"));
        ImageView imgAddOntImgView = new ImageView(imgAddOnt);
        imgAddOntImgView.setFitHeight(32);
        imgAddOntImgView.setFitWidth(32);
        MyButton addOntButton = new MyButton("", imgAddOntImgView, false);
        addOntButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addOntButtonAction(primaryStage);
            }
        });
        addOntButton.setTooltip(getToolTip("Add Ontology"));
        vboxButtons.setPadding(new Insets(27, 0, 0, 0)); //to align with OntologyToolBar Label insets
        vboxButtons.getChildren().add(addOntButton);
        ontToolBarHBox.getChildren().add(vboxButtons);

        //Add DETACH BUTTON
       /* Image imgDetach = new Image(getClass().getResourceAsStream("/graphics/1482810616_09_Expand.png"));
         this.buttonDetach = new Button("", new ImageView(imgDetach));
         buttonDetach.setDisable(true);
         buttonDetach.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
         detachButtonAction();
         }
         });
         buttonDetach.setTooltip(getToolTip("View Tables"));
         vboxButtons.getChildren().add(buttonDetach);
         */
        //hbox_main.getChildren().add(vboxButtons);
        // vboxRun is a vertical box containing the run toolbar and the drift tables
//        this.vboxRun = new VBox();
//        this.vboxRun.setSpacing(10);
        //Add Tables
//        this.initialTablePanel = new TablePanel();
        // this.vboxRun.getChildren().add(initialTablePanel);
//        hbox_main.getChildren().add(this.vboxRun);
//        hbox_main.setHgrow(this.vboxRun, Priority.ALWAYS);
        return hbox_main;
    }

    public void refreshButtonAction(int ont_no, Stage primaryStage) {
        if (this.ontologyToolBars.get(ont_no).isLoaded()) {
            this.runToolBar.setDisableButtonMorphChain(true);
            enableMeasureDrift();
        }

        this.seeDriftCore.clearOntologies();
        //both cores need to be refreshed
        refreshOntologies();
        //refresh tree

        this.ontologyToolBars.get(ont_no).buildOntTreeView(seeDriftCore);
    }

    private static void configureOwlFileChooser(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Select an Ontology");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OWL", "*.owl"),
                new FileChooser.ExtensionFilter("RDF", "*.rdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
    }

    /*
     private void fillDummyTable(int randSize, TableView table) {

     //generate columns
     //(the schema)
     for (int i = 0; i < randSize; i++) {
     final int j = i;
     String name = "%"; //header column
     if (i != 0) {
     name = "Concept" + i;
     }
     TableColumn col = new TableColumn(name);
     col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
     public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
     return new SimpleStringProperty(param.getValue().get(j).toString());
     }

     });
     table.getColumns().add(col);
     }

     //generate rows
     //(the data)
     Random rand = new Random();
     ObservableList<ObservableList> tableConData = FXCollections.observableArrayList();
     for (int i = 1; i < randSize; i++) { //rows
     ObservableList<String> row = FXCollections.observableArrayList();
     row.add("Concept" + i); //row header
     for (int j = 0; j < randSize; j++) //columns
     {
     row.add(String.valueOf(rand.nextDouble()));
     }
     tableConData.add(row);
     }
     table.setItems(tableConData);
     } */
    private void fillTablePairsAndReadjustHeight(ArrayList<VersionPairs> pairs, MyTable guiTable, int pairIndex) {
        //construct data and update
        fillTablePairs(guiTable, pairs, pairIndex);
        TableUtils.reAdjustTableHeight(guiTable);
    }

    private void clearTablePanelPaginationFromGUI() {
        if (this.initialTablePanel != null && this.vboxRun != null) {
            if (this.vboxRun.getChildren().indexOf(this.initialTablePanel) > -1) {
                this.vboxRun.getChildren().remove(this.initialTablePanel);
            }
        }
        if (this.tablePanelPagination != null && this.vboxRun != null) {
            if (this.vboxRun.getChildren().indexOf(this.tablePanelPagination) > -1) {
                this.vboxRun.getChildren().remove(this.tablePanelPagination);
            }
        }
    }

    public void runButtonAction() {

        this.runToolBar.setHighlighted(this.runToolBar.getButtonRun(), false);

        //Table Avg
        //TODO fro multiple
        //assumption that there's one table (tableAvg) and one ontology pair (getAverageDrift.get(0)!)
        ArrayList<AverageDrift> avgDrift = seeDriftCore.getAverageDrift();
        System.out.println("AVERAGE DRIFT SIZE:" + avgDrift.size());
        if (avgDrift.size() < 1) {
            showErrorDialog("Error! No average drifts to show", new Exception("avgDrift.size < 1"));
        }
        if (avgDrift.size() > 0) {
            //this.buttonDetach.setDisable(false);
            this.runToolBar.setDisableButtonMorphChain(false);
        }

        clearTablePanelPaginationFromGUI();

        this.tablePanels = new ArrayList<>();

        this.pairData = new ArrayList<>();

        for (int i = 0; i < avgDrift.size(); i++) {
            this.tablePanels.add(new TablePanel(this.ontologyToolBars.get(i).getOntDetails().getText() + " - " + this.ontologyToolBars.get(i + 1).getOntDetails().getText(), this.primaryStage));

            AverageDrift ontPair = avgDrift.get(i);
            ObservableList<ConceptMetricsSchema> data = FXCollections.observableArrayList();
            data.add(new ConceptMetricsSchema(ontPair.getLabel(), ontPair.getIntension(), ontPair.getExtension(), ontPair.getWhole()));
            this.tablePanels.get(i).getTableAvg().setItems(data);

            //Table Concept-per-Concept Pairs
            //run metrics on API Core
            ArrayList<VersionPairs> labelPairs = seeDriftCore.getLabelVersionPairs();
            ArrayList<VersionPairs> intensionPairs = seeDriftCore.getIntensionVersionPairs();
            ArrayList<VersionPairs> extensionPairs = seeDriftCore.getExtensionVersionPairs();
            ArrayList<VersionPairs> wholePairs = seeDriftCore.getWholeVersionPairs();

            //construct data and update
            fillTablePairsAndReadjustHeight(labelPairs, this.tablePanels.get(i).getTableLabelPairs(), i);
            fillTablePairsAndReadjustHeight(intensionPairs, this.tablePanels.get(i).getTableIntensionPairs(), i);
            fillTablePairsAndReadjustHeight(extensionPairs, this.tablePanels.get(i).getTableExtensionPairs(), i);
            fillTablePairsAndReadjustHeight(wholePairs, this.tablePanels.get(i).getTableWholePairs(), i);

            //get Data for Morphing Chain GUI
            this.pairData.add(new PairData(getPairData(labelPairs, i), getPairData(intensionPairs, i), getPairData(extensionPairs, i), getPairData(wholePairs, i)));
        }

        this.tablePanelPagination = new Pagination(avgDrift.size());
        this.tablePanelPagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });
        //this.vboxRun.getChildren().add(this.tablePanelPagination);
        detachButtonAction();

    }

    /**
     * If two or more ontology paths are active (non-empty), it enables the run
     * button
     */
    private void updateRunButtonAbility() {
        int countActive = 0;
        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            if (!this.ontologyToolBars.get(i).getOntFilePath().isEmpty()) {
                countActive++;
            }
        }
        if (countActive >= 2) {
            this.runToolBar.setDisableButtonRun(false);
        }
    }

    private void enableMeasureDrift() {
        int loadedToolBars = 0;
        for (int i = 0; i < this.ontologyToolBars.size(); i++) {
            if (this.ontologyToolBars.get(i).isLoaded()) {
                loadedToolBars++;
            }
        }
        if (loadedToolBars > 1) {
            this.runToolBar.setDisableButtonRun(false);
        } else {
            this.runToolBar.setDisableButtonRun(true);
        }
    }

    public void openButtonAction(int ont_no, Stage primaryStage) {

        this.ontologyToolBars.get(ont_no).setHighLightedOpenButton(false);
        FileChooser fileChooser = new FileChooser();
        //Open button action
        configureOwlFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            this.runToolBar.setDisableButtonMorphChain(true);
            enableMeasureDrift();
            //setFileToOntTASK(file, ont_no);
            setFiletoOnt(file, ont_no);
            this.ontologyToolBars.get(ont_no).disableController(false);
            if (this.ontologyToolBars.size() > ont_no + 1 && this.ontologyToolBars.get(ont_no + 1).isDisabled_()) {
                // this.ontologyToolBars.get(ont_no+1).disableController(false);
                this.ontologyToolBars.get(ont_no + 1).disableOpenButton(false);
                this.ontologyToolBars.get(ont_no + 1).disableTreePanel(false);
            }
        }

    }

    private void setFileToOntTASK(File file, int ont_no) {
        this.scene.setCursor(Cursor.WAIT);
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                setFiletoOnt(file, ont_no);
                return null;
            }
        };
        task.setOnSucceeded(e -> scene.setCursor(Cursor.DEFAULT));
        new Thread(task).start();
    }

    private void setFiletoOnt(File file, int ont_no) {

        //refresh Ont reference
        this.ontologyToolBars.get(ont_no).setOntFilePath(file.getAbsolutePath());
        //always clear and add both
        this.seeDriftCore.clearOntologies();
        refreshOntologies();
        //refresh tree

        this.ontologyToolBars.get(ont_no).buildOntTreeView(this.seeDriftCore);

        //add Details
        String details = file.getName();
        //TODO possibly add more details
        this.ontologyToolBars.get(ont_no).setOntDetails(details);

        //update buttonRun
        updateRunButtonAbility();
    }

    private void showErrorDialog(String msg, Exception ex) {
        System.err.println(msg);
        ExceptionDialog.showDialog(msg, ex);
        if (DEBUG) {
            ex.printStackTrace();
        }
    }

    private void fillTablePairs(MyTable table, ArrayList<VersionPairs> pairs, int pairIndex) {

        //TODO for multiple
        //Assuming first and only pair exists
        VersionPairs pair = pairs.get(pairIndex);

        //columns (schema)
        ArrayList<String> columnNames = pair.getXAxis();
        for (int i = -1; i < columnNames.size(); i++) {
            final int k = i + 1;
            String columnName;
            if (i == -1) {
                columnName = "%";
            } else {
                columnName = columnNames.get(i);
            }
            TableColumn col = new TableColumn<>();//(columnName);
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(k).toString());
                }
            });
            Label labelCol = new Label(columnName);
            labelCol.setTextFill(Color.BLACK);
            labelCol.setTooltip(getToolTip(columnName));
            col.setGraphic(labelCol);
            table.addColumnName(labelCol.getText());

            table.getColumns().add(col);
        }

        //rows (data)
        ObservableList<ObservableList> tablePairData = FXCollections.observableArrayList();

        ArrayList<String> rowNames = pair.getYAxis();

        for (int i = 0; i < rowNames.size(); i++) { //rows
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(rowNames.get(i));
            for (int j = 0; j < columnNames.size(); j++) {
                row.add(pair.getStabilityForPair(rowNames.get(i), columnNames.get(j)));
                //System.out.println( pair.getStabilityForPair(rowNames.get(i), columnNames.get(j) ));
            }
            tablePairData.add(row);
        }
        table.setItems(tablePairData);

    }

    private StabilityData getPairData(ArrayList<VersionPairs> pairs, int pairIndex) {

        //TODO for multiple
        //Assuming first and only pair exists
        VersionPairs pair = pairs.get(pairIndex);

        //columns (schema)
        ArrayList<String> columnNames = pair.getXAxis();

        //rows (data)
        ArrayList<String> rowNames = pair.getYAxis();

        ArrayList<ArrayList<String>> tableValues = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < rowNames.size(); i++) { //rows
            ArrayList<String> tableRow = new ArrayList<String>();
            for (int j = 0; j < columnNames.size(); j++) {
                tableRow.add(pair.getStabilityForPair(rowNames.get(i), columnNames.get(j)));
                //System.out.println( pair.getStabilityForPair(rowNames.get(i), columnNames.get(j) ));
            }
            tableValues.add(tableRow);
        }

        StabilityData stabilityData = new StabilityData();
        stabilityData.colNames = columnNames;
        stabilityData.rowNames = rowNames;
        stabilityData.tableValues = tableValues;

        return stabilityData;

    }
}
