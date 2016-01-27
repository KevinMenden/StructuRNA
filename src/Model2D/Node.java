package Model2D;

import Model.Nucleotide;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kevin Menden on 25.11.2015.
 */
public class Node extends Circle {

    private int nucleotideNumber;

    private Nucleotide nucleotide;

    private double radius = 5;

    public Node(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
    }

    private final Rectangle selectionBox = new Rectangle(2*radius, 2*radius, Color.TRANSPARENT);


    public Node(double centerX, double centerY, double radius, Nucleotide nucleotide) {
        super(centerX, centerY, radius);
        this.nucleotide = nucleotide;
        this.setFill(getColor(nucleotide.getNucleotide()));
        this.radius = radius;
        this.selectionBox.setLayoutY(centerY - radius);
        this.selectionBox.setLayoutX(centerX - radius);
        this.selectionBox.setStroke(Color.RED);
        this.selectionBox.setVisible(false);
    }



    //Set the color of the node according to the nucleotide its representing
    private Color getColor(char n) {
        Color color = Color.BLACK;
        switch (n) {
            case 'a':
            case 'A':
                color = Color.RED;
                break;
            case 'u':
            case 'U':
                color = Color.GREEN;
                break;
            case 'g':
            case 'G':
                color = Color.YELLOW;
                break;
            case 'c':
            case 'C':
                color = Color.BLUE;
                break;
        }
        return color;
    }

    public void showSelectionBox(Boolean bool){
        if (bool){
            this.selectionBox.setVisible(true);
        } else {
            this.selectionBox.setVisible(false);
            System.out.println("Still see the thang?");
        }
    }

    /*
    GETTER AND SETTER
     */
    public int getNucleotideNumber() {
        return nucleotideNumber;
    }

    public void setNucleotideNumber(int nucleotideNumber) {
        this.nucleotideNumber = nucleotideNumber;
    }

    public Rectangle getSelectionBox() {
        return selectionBox;
    }


}
