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

import gui.ColorTheme;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author staxos
 */
public class GraphNode {

    public Circle circle;
    public Label label;
    public Label labelOntDetail;

    private String strokeColor;
    private double transparencyStroke;

    private ArrayList<GraphEdge> edgesThatStartHere;
    private ArrayList<GraphEdge> edgesThatEndHere;

    private EventHandler<MouseEvent> onMouseDraggedEvent;
    private EventHandler<MouseEvent> onMouseEnteredEvent;
    private EventHandler<MouseEvent> onMouseExitEvent;
    private EventHandler<MouseEvent> onMouseClickedEvent;

    private boolean dragged;
    private double transparencyFill;
    private String fillColor;
    private int radius;
    private boolean selected;
    private int level;

    private boolean isPressed;

    public boolean isSelected() {
        return this.selected;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public GraphEdge getEdgeThatEndsHereAndStartsOnNode(GraphNode gNode) {
        for (int i = 0; i < this.edgesThatEndHere.size(); i++) {
            if (gNode.edgeStartsOnThisNode(this.edgesThatEndHere.get(i).getStartX(), this.edgesThatEndHere.get(i).getStartY())) {
                return this.edgesThatEndHere.get(i);
            }
        }
        return null;
    }

    public int getLevel() {
        return this.level;
    }

    public boolean hasEdges() {
        return !((edgesThatStartHere == null || edgesThatStartHere.size() == 0) && (edgesThatEndHere == null || edgesThatEndHere.size() == 0));
    }

    public void addEdge(GraphEdge e) {
        if (edgeStartsOnThisNode(e.getStartX(), e.getStartY())) {
            this.edgesThatStartHere.add(e);
        }
        if (edgeEndsOnThisNode(e.getEndX(), e.getEndY())) {
            this.edgesThatEndHere.add(e);
        }
    }

    public boolean edgeStartsOnThisNode(double startNodeX, double startNodeY) {
        if (Math.abs(this.circle.getCenterX() - (double) startNodeX) < 3 && Math.abs(this.circle.getCenterY() - (double) startNodeY) < 3) {
            return true;
        }
        return false;
    }

    public boolean edgeEndsOnThisNode(double endNodeX, double endNodeY) {
        if (Math.abs(this.circle.getCenterX() - (double) endNodeX) < 3 && Math.abs(this.circle.getCenterY() - (double) endNodeY) < 3) {
            return true;
        }
        return false;
    }

    public void onMouseEnteredAction() {
//        String strokeColor = "DARKORANGE";
        String strokeColor = ColorTheme.graphEdgeHover;
        double transparencyStroke = 0.7;
        this.circle.setStroke(Color.web(strokeColor, transparencyStroke));

        //show hide label
        this.label.setVisible(true);
    }

    public void onMouseExitedAction() {
        this.reccoverNodeFromHover();
    }

    public void onMouseDraggedAction(double mouseSceneX, double mouseSceneY) {
        this.dragged = true;
        setCenter(mouseSceneX, mouseSceneY + 2);
    }

    public void setCenter(double newCenterX, double newCenterY) {
        this.circle.setCenterX(newCenterX);
        this.circle.setCenterY(newCenterY);
        //high labels
//        this.label.setTranslateX(newCenterX);
//        this.label.setTranslateY(newCenterY);
        this.label.setTranslateX(newCenterX - radius);
        this.label.setTranslateY(newCenterY - 2.6 * radius);
        this.labelOntDetail.setTranslateX(newCenterX - radius);
//        this.labelOntDetail.setTranslateX(newCenterX);
//        this.labelOntDetail.setTranslateY(newCenterY+this.label.getHeight()+2);
        this.labelOntDetail.setTranslateY(newCenterY + this.label.getHeight() + 2 - 2.6 * radius);

        for (int i = 0; i < this.edgesThatStartHere.size(); i++) {
            this.edgesThatStartHere.get(i).setStart((int) this.circle.getCenterX(), (int) this.circle.getCenterY());
        }

        for (int i = 0; i < this.edgesThatEndHere.size(); i++) {
            this.edgesThatEndHere.get(i).setEnd((int) this.circle.getCenterX(), (int) this.circle.getCenterY());
        }

    }

    public void setMouseDragEvent(boolean value) {
        if (value) {
            this.circle.setOnMouseDragged(this.onMouseDraggedEvent);
            this.circle.setOnMouseEntered(this.onMouseEnteredEvent);
            this.circle.setOnMouseExited(this.onMouseExitEvent);
        } else {
            this.circle.setOnMouseDragged(null);
            this.circle.setOnMouseEntered(null);
            this.circle.setOnMouseExited(null);
        }
    }

    private void onMouseClickedAction() {
        if (this.dragged == true) {
            this.dragged = false;
            return;
        }

        if (!this.selected) {
//          this.circle.setFill(Color.web("DARKORANGE", transparencyFill));
//          this.circle.setStroke(Color.DARKSEAGREEN);
            this.circle.setFill(Color.web(ColorTheme.graphNodeSelected, transparencyFill));
            this.circle.setStroke(Color.web(ColorTheme.graphNodeStrokeSelected));
            this.selected = true;
        } else if (this.selected) {
            this.circle.setFill(Color.web(this.fillColor, this.transparencyFill)); //as constructed
            circle.setStroke(Color.web(strokeColor, transparencyStroke));
            this.selected = false;
        }

    }

    public void edgeIsSelected() {
        this.label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        this.circle.setStroke(Color.web("FIREBRICK"));
        this.circle.setStroke(Color.web(ColorTheme.graphEdgeHover));
    }

    public void reccoverNodeFromHover() {
        this.label.setFont(Font.font("Arial", 14));
        if (this.isSelected()) {
            circle.setStroke(Color.web(ColorTheme.graphNodeStrokeSelected));
        } else {
            circle.setStroke(Color.web(strokeColor, transparencyStroke));
        }

        //show hide label
        this.label.setVisible(true);
    }

    public GraphNode getCopy() {
        GraphNode node = new GraphNode(this.label.getText(), this.radius, this.circle.getCenterX(), this.circle.getCenterY(), this.fillColor, this.strokeColor, this.level, this.labelOntDetail.getText());
        return node;
    }

    public void onMouseReleaseAction() {
        this.isPressed = false;
    }

    public void setSelected(boolean val) {
        onMouseClickedAction();
    }

    public void onMousePressedAction() {
        this.isPressed = true;
    }

    public ArrayList<GraphNode> getFirstDescendants() {
        ArrayList<GraphNode> firstDescendants = new ArrayList<>();

        for (int i = 0; i < this.edgesThatStartHere.size(); i++) {
            firstDescendants.add(this.edgesThatStartHere.get(i).getEndNode());
        }
        return firstDescendants;
    }

    GraphNode(String labelText, int radius, double X, double Y, String fillColor, String strokeColor, int level, String ontDetailStr) {
        this.radius = radius;
        this.dragged = false;
        this.selected = false;
        this.level = level;

        this.edgesThatStartHere = new ArrayList<>();
        this.edgesThatEndHere = new ArrayList<>();
        this.strokeColor = strokeColor;

        this.fillColor = fillColor;
        this.transparencyFill = 0.67;
        this.transparencyStroke = 0.16;
        this.circle = new Circle(radius, Color.web(fillColor, transparencyFill));
        circle.setCenterX(X);
        circle.setCenterY(Y);
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStroke(Color.web(strokeColor, transparencyStroke));
        circle.setStrokeWidth(4);

        this.label = new Label();
        this.label.setText(labelText);
        //high label
//        this.label.setTranslateX(X);
//        this.label.setTranslateY(Y);
        this.label.setTranslateX(X - radius);
        this.label.setTranslateY(Y - 2.6 * radius);
        this.label.setFont(Font.font("Arial", 14));

        this.labelOntDetail = new Label(ontDetailStr);
        this.labelOntDetail.setFont(Font.font("Arial", FontWeight.NORMAL, 9));
        //high label
//        this.labelOntDetail.setTranslateX(X);
//        this.labelOntDetail.setTranslateY(Y + 15/*this.label.getHeight()*/ + 2);
        this.labelOntDetail.setTranslateX(X - radius);
        this.labelOntDetail.setTranslateY(Y + 15/*this.label.getHeight()*/ + 2 - 2.6 * radius);

        this.onMouseEnteredEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseEnteredAction();
            }
        };

        this.onMouseExitEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseExitedAction();
            }
        };

        this.onMouseDraggedEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseDraggedAction(event.getSceneX(), event.getSceneY());
            }
        };

        this.onMouseClickedEvent = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    onMouseClickedAction();
                }
            }
        };
        this.circle.setOnMouseClicked(this.onMouseClickedEvent);

        this.circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMouseReleaseAction();
            }
        });

        this.circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onMousePressedAction();
            }
        });

        //show hide label
        this.labelOntDetail.visibleProperty().bind(this.label.visibleProperty()); //bind once
//        this.labelOntDetail.rotateProperty().bind(this.label.rotateProperty());
        this.label.setVisible(true);
//        this.label.setRotate(-90);
    }
}
