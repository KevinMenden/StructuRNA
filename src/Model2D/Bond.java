package Model2D;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Created by Kevin Menden on 25.11.2015.
 */
public class Bond extends Line {
    private Node startNode;
    private Node endNode;

    private int startNucleotide;
    private int endNucleotide;

    public Bond(double startX, double startY, double endX, double endY, Node startNode, Node endNode) {
        super(startX, startY, endX, endY);
        this.startNode = startNode;
        this.endNode = endNode;
        bindStartAndEnd(startNode, endNode);
        this.startNucleotide = startNode.getNucleotideNumber();
        this.endNucleotide = endNode.getNucleotideNumber();
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
