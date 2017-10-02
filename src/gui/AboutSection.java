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
package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author staxos
 */
public class AboutSection {

    private Stage stage;
    
    VBox getImageWithCaptionBox(String imgpath, String caption) {
        VBox imageWithCaptionBox = new VBox();        
        HBox imgBox = new HBox();
        Image img = new Image(imgpath);        
        ImageView imgView = new ImageView(img);
        imgBox.getChildren().add(imgView);
        imgBox.setAlignment(Pos.CENTER);
        
        imageWithCaptionBox.setMaxWidth(1.6 * img.getWidth());
        Label labelMkLab = new Label(caption);
        labelMkLab.setFont(Font.font("Verdana", FontWeight.NORMAL, 10));
        labelMkLab.setAlignment(Pos.CENTER);
        labelMkLab.setMaxWidth(Double.MAX_VALUE);
        labelMkLab.wrapTextProperty().setValue(Boolean.TRUE);
        imageWithCaptionBox.getChildren().addAll(imgBox, labelMkLab);
        return imageWithCaptionBox;
    }
    
    AboutSection() {
        this.stage = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(35, 35, 35, 35));
        root.setSpacing(40);
        Scene scene = new Scene(root, 990, 590);
        this.stage.setScene(scene);
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/graphics/icon.png")));
        this.stage.setTitle("About");
        
        root.setStyle("-fx-background-color: #FFFFFF");
                
        HBox imagesBox = new HBox();
        imagesBox.setMaxWidth(Double.MAX_VALUE);
        imagesBox.setSpacing(20);

        //VBox leftImageBox = getImageWithCaptionBox("/graphics/mklablogo.png", "Multimedia - Knowledge & Social Media Analytics Laboratory");
        VBox leftImageBox = new VBox();
        HBox imgBox = new HBox();
        imgBox.setSpacing(15);
        Image img = new Image("/graphics/mklablogo.png");        
        HyperLinkImage imgView = new HyperLinkImage(img, "http://mklab.iti.gr/");
        imgView.setFitHeight(95);
        imgView.setFitWidth(95);
        imgBox.getChildren().add(imgView);
        imgBox.setAlignment(Pos.CENTER);
        leftImageBox.getChildren().add(imgBox);
        Label mkLabLabel = new HyperLinkLabel("http://mklab.iti.gr/", "Multimedia Knowledge\nand Social Media\nAnalytics Laboratory");
        mkLabLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        mkLabLabel.setTextFill(Color.web("404040"));
        imgBox.getChildren().add(mkLabLabel);
        
        imagesBox.getChildren().add(leftImageBox);

        // VBox middleImageBox = getImageWithCaptionBox("/graphics/itilogo.png", "Information Tecnologies Intitute");
        VBox middleImageBox = new VBox();
        HBox imgBox2 = new HBox();
        Image img2 = new Image("/graphics/itilogo.png");
        HyperLinkImage imgView2 = new HyperLinkImage(img2, "http://www.iti.gr/iti/index.html");
        imgView2.setFitHeight(81);
        imgView2.setFitWidth(255);
        imgBox2.getChildren().add(imgView2);
        imgBox2.setAlignment(Pos.CENTER);
        middleImageBox.getChildren().add(imgBox2);
        
        imagesBox.getChildren().add(middleImageBox);

        // VBox rightImageBox = getImageWithCaptionBox("/graphics/certhlogo.jpg", "Centre for Research and Technology Hellas");
        VBox rightImageBox = new VBox();
        HBox imgBox3 = new HBox();
        Image img3 = new Image("/graphics/certhLogo.jpg");
        HyperLinkImage imgView3 = new HyperLinkImage(img3, "http://www.certh.gr/root.en.aspx");
        imgView3.setFitHeight(81);
        imgView3.setFitWidth(255);
        imgBox3.getChildren().add(imgView3);
        imgBox3.setAlignment(Pos.CENTER);
        rightImageBox.getChildren().add(imgBox3);
        
        imagesBox.getChildren().add(rightImageBox);
        
        leftImageBox.setAlignment(Pos.CENTER_LEFT);
        middleImageBox.setAlignment(Pos.CENTER);
        rightImageBox.setAlignment(Pos.CENTER_RIGHT);
        
        imagesBox.setHgrow(leftImageBox, Priority.ALWAYS);
        imagesBox.setHgrow(middleImageBox, Priority.ALWAYS);
        imagesBox.setHgrow(rightImageBox, Priority.ALWAYS);
        
        root.getChildren().add(imagesBox);
        
        Font txtFont = Font.font("Verdana", FontWeight.NORMAL, 12);
        
        VBox textVBox = new VBox();
        
        
        Label labelAbout = new Label("SemaDriftFx Version " + SemaDriftFx.version);
        labelAbout.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        root.getChildren().add(labelAbout);
        
        Label textDevelopedBy = new Label("");
        textDevelopedBy.setText("Developed by:");
        textDevelopedBy.setPadding(new Insets(0, 0, 8, 0));
        textDevelopedBy.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        textVBox.getChildren().add(textDevelopedBy);
        
        ContactLine thanosHBox = new ContactLine("Thanos G. Stavropoulos", "athstavr@iti.gr");        
        textVBox.getChildren().add(thanosHBox);
        
        ContactLine stavrosHBox = new ContactLine("Stavros Tachos", "staxos@iti.gr");
        textVBox.getChildren().add(stavrosHBox);
        
        ContactLine steliosHBox = new ContactLine("Stelios Andreadis", "andreadisst@iti.gr");
        textVBox.getChildren().add(steliosHBox);
        
        ContactLine stratosHBox = new ContactLine("Efstratios Kontopoulos", "skontopo@iti.gr");
        textVBox.getChildren().add(stratosHBox);
        
        ContactLine ikomHBox = new ContactLine("Ioannis Kompatsiaris", "ikom@iti.gr");
        textVBox.getChildren().add(ikomHBox);
        
        Label teamText = new Label("The CERTH-ITI PERICLES team");
        teamText.setFont(txtFont);
        teamText.setPadding(new Insets(4, 0, 4, 0));
        textVBox.getChildren().add(teamText);        
        
        HBox buufTextHBox = new HBox();
        buufTextHBox.setPadding(new Insets(16, 0, 4, 0));
        Label buufText = new Label("Buuf icons created by mattahan ");
        buufText.setFont(txtFont);        
        buufTextHBox.getChildren().add(buufText);
        HyperLinkLabel buufTextHPL = new HyperLinkLabel("http://mattahan.deviantart.com", "http://mattahan.deviantart.com");
        buufTextHBox.getChildren().add(buufTextHPL);        
        textVBox.getChildren().add(buufTextHBox);
        
        HBox semaHomeHBox = new HBox();
        semaHomeHBox.setPadding(new Insets(4, 0, 4, 0));
        Label semaHomeText = new Label("More information on the SemaDrift Homepage: ");
        semaHomeText.setFont(txtFont);        
        semaHomeHBox.getChildren().add(semaHomeText);
        HyperLinkLabel semaHomeHPL = new HyperLinkLabel("http://mklab.iti.gr/project/semadrift-measure-semantic-drift-ontologies", "http://mklab.iti.gr/project/semadrift-measure-semantic-drift-ontologies");
        semaHomeHBox.getChildren().add(semaHomeHPL);        
        textVBox.getChildren().add(semaHomeHBox);
        
        root.getChildren().add(textVBox);
        
        HBox periclesHBox = new HBox();
        periclesHBox.setSpacing(10);
        //periclesHBox.setStyle("-fx-background-color: #ABB0B0;");
        Image imgPericles = new Image("/graphics/pericles_logo.png");
        HyperLinkImage imgViewPericles = new HyperLinkImage(imgPericles, "http://pericles-project.eu");
//        ImageView imgViewPericles = new ImageView (imgPericles);
        imgViewPericles.setFitHeight(70);
        imgViewPericles.setFitWidth(280);
        periclesHBox.getChildren().add(imgViewPericles);
        
        VBox fundLabelVBox = new VBox();
        Label fundLabel = new Label();
        fundLabel.setPadding(new Insets(0, 0, 0, 10));
        fundLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        fundLabel.setText("This work has received funding from EU-FP7 \"PERICLES\", under gr agreement no. 601138");
        fundLabel.setWrapText(true);
        fundLabelVBox.getChildren().add(fundLabel);
        periclesHBox.getChildren().add(fundLabelVBox);
        
        Image imgEuLogo = new Image("/graphics/euLogo.png");        
        ImageView imgEuLogoIV = new ImageView(imgEuLogo);
        imgEuLogoIV.setFitHeight(66);
        imgEuLogoIV.setFitWidth(199);
        periclesHBox.getChildren().add(imgEuLogoIV);
        
        root.getChildren().add(periclesHBox);
        
        stage.show();
    }
    
}
