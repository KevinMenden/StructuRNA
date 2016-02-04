package Model2D;

import Model.Nucleotide;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Created by Kevin Menden on 25.11.2015.
 */
public class Node extends Circle {

    private int nucleotideNumber;

    private Nucleotide nucleotide;

    private double radius = 5;

    FillTransition fillTransition;

    public Node(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
    }


    public Node(double centerX, double centerY, double radius, Nucleotide nucleotide) {
        super(centerX, centerY, radius);
        this.nucleotide = nucleotide;
        this.setFill(getColor(nucleotide.getNucleotide()));
        this.radius = radius;
        fillTransition = new FillTransition(Duration.millis(500),this , Color.CYAN, Color.BROWN);
        fillTransition.setCycleCount(Animation.INDEFINITE);
        fillTransition.setAutoReverse(true);

    }

    public void switchOn(){
        this.fillTransition.play();
    }
    public void switchOff(){
        this.fillTransition.stop();
        this.setFill(getColor(nucleotide.getNucleotide()));
    }
    //Swith on without animation
    public void switchOnStatic(){
        this.setFill(Color.CYAN);
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


    /*
    GETTER AND SETTER
     */
    public int getNucleotideNumber() {
        return nucleotideNumber;
    }

    public void setNucleotideNumber(int nucleotideNumber) {
        this.nucleotideNumber = nucleotideNumber;
    }


}
