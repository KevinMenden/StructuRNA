package Model2D;

import javafx.scene.shape.Line;

/**
 * Created by Kevin Menden on 25.11.2015.
 */
public class Bond extends Line {
    private Node startNode;
    private Node endNode;

    public Bond(double startX, double startY, double endX, double endY, Node startNode, Node endNode) {
        super(startX, startY, endX, endY);
        this.startNode = startNode;
        this.endNode = endNode;
        bindStartAndEnd(startNode, endNode);
    }

    public Bond(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }

    private void bindStartAndEnd(Node start, Node end) {
        this.startXProperty().bind(start.centerXProperty().add(start.translateXProperty()));
        this.startYProperty().bind(start.centerYProperty().add(start.translateYProperty()));

        this.endXProperty().bind(end.centerXProperty().add(end.translateXProperty()));
        this.endYProperty().bind(end.centerYProperty().add(end.translateYProperty()));
    }


}
