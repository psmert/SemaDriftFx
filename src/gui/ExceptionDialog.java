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

package gui;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.PopupWindow;

/**
 *
 * @author Smert
 */
public class ExceptionDialog {

    public static void showDialog(String msg, Exception ex) {

        //Alert alert = new Alert(AlertType.ERROR);
        Alert alert = new Alert(AlertType.ERROR);

        alert.setTitle("Exception Occured");
        alert.setHeaderText("Look, something bad happened");
        alert.setContentText(msg);

        //dummy
//        Exception ex = new FileNotFoundException("Could not find file blabla.txt");
        
// Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);

        textArea.setEditable(
                false);
        textArea.setWrapText(
                true);

        textArea.setMaxWidth(Double.MAX_VALUE);

        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);

        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();

        expContent.setMaxWidth(Double.MAX_VALUE);

        expContent.add(label,
                0, 0);
        expContent.add(textArea,
                0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane()
                .setExpandableContent(expContent);

        alert.showAndWait();
    }
}
