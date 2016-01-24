package Presenter;

import Model.Nucleotide;
import Model2D.*;
import PDBParser.PDBFile;
import View.View;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Created by kevin_000 on 19.01.2016.
 * Class contains all important functions necessary for
 * creating the RNA secondary structure represantation
 */
public class Presenter2D {

    //pdbFile
    private PDBFile pdbFile;
    //Graph Model and Group for 2D representation
    private Graph graphModel = new Graph();
    private Group graphGroup = new Group();

    //Max and Min values to define the 2D representation
    private int X_MIN_2D = 50;
    private int X_MAX_2D = 200;
    private int Y_MIN_2D = 50;
    private int Y_MAX_2D = 200;


    public Presenter2D(){}
    //Set te pdb file
    public void setPdbFile(PDBFile pdbFile) {
        this.pdbFile = pdbFile;
    }
    //Get the graph Structure
    public Group getGraphGroup() {
        graphGroup.setOpacity(1.0);
        return graphGroup;
    }

    /**
     * Use the given dotBracket notation to draw the secondary structure
     */
    public void buildSecondaryStructureGraph(){
        Nussinov nussinov = new Nussinov(pdbFile.sequence);
        nussinov.apply();
        graphModel.parseNotation(nussinov.getBracketNotation());
        double[][] embedding = SpringEmbedder.computeSpringEmbedding(10, this.graphModel.getNumberOfNodes(), this.graphModel.getEdges(), null);
        SpringEmbedder.centerCoordinates(embedding, 50, 200, 50, 200);
        graphGroup.getChildren().clear();
        drawGraph(embedding, this.graphModel.getEdges(), this.graphModel.getNumberOfNodes(), this.graphModel.getNumberOfEdges());
    }

    /**
     * Draw the nodes and the edges computed by the SpringEmbedder
     * @param embedding
     * @param edges
     */
    private void drawGraph(double[][] embedding, int[][] edges, int numberOfNodes, int numberOfEdges) {

        //draw the nodes and add tooltips for every node
        int index = 0;
        Node[] nodes = new Node[numberOfNodes];
        for (double[] point : embedding) {
            this.graphModel.setSequence(pdbFile.sequence);
            Nucleotide nucleotide = new Nucleotide(this.graphModel.getSequence().charAt(index));
            Node node = new Node(point[0],point[1],5, nucleotide);
            StringBuilder nuc = new StringBuilder();
            nuc.append(nucleotide.getNucleotide());
            Tooltip tip = new Tooltip(nuc.toString() + " #" + Integer.toString(index+1));
            tip.install(node, tip);
            nodes[index] = node;
            index++;
        }
        //draw the edges
        Bond[] bonds = new Bond[numberOfEdges];
        int counter = 0;
        for (int[] edge : edges) {
            double x1 = embedding[edge[0]][0];
            double y1 = embedding[edge[0]][1];
            double x2 = embedding[edge[1]][0];
            double y2 = embedding[edge[1]][1];
            Bond bond = new Bond(x1, y1, x2, y2, nodes[edge[0]], nodes[edge[1]]);
            if (counter>=numberOfNodes) {
                bond.setStroke(Color.DARKRED);
            }
            bonds[counter] = bond;
            counter++;
        }

        //Add edges
        for (Bond b : bonds){
            this.graphGroup.getChildren().add(b);
        }
        //Add nodes
        for (Node n : nodes) {
            this.graphGroup.getChildren().add(n);
        }

    }

























    /**
     * draw a graph in animated style
     * @param initialCoordinates
     * @param embedding
     * @param edges
     * @param numberOfNodes
     * @param numberOfEdges
     */
    private void drawGraphAnimated(double[][] initialCoordinates, double[][] embedding, int[][] edges, int numberOfNodes, int numberOfEdges){
        //draw initial points with tooltips
        Node[] nodes = new Node[numberOfNodes];
        Node[] endNodes = new Node[numberOfNodes];
        int index = 0;
        for (double[] point : initialCoordinates) {
            Nucleotide nucleotide = new Nucleotide(this.graphModel.getSequence().charAt(index));
            Node node = new Node(point[0],point[1],5, nucleotide);
            nodes[index] = node;
            StringBuilder nuc = new StringBuilder();
            nuc.append(nucleotide.getNucleotide());
            Tooltip tip = new Tooltip(nuc.toString() + " #" + Integer.toString(index+1));
            tip.install(node, tip);
            index++;
        }

        //Make final nodes (only coordinates)
        index = 0;
        for (double[] point : embedding) {
            Node node = new Node(point[0],point[1],5);
            endNodes[index] = node;
            index++;
        }
        Bond[] bonds = new Bond[numberOfEdges];

        //set the edges
        int counter = 0;
        for (int[] edge : edges) {
            //Start coordiniates
            double x1 = initialCoordinates[edge[0]][0];
            double y1 = initialCoordinates[edge[0]][1];
            double x2 = initialCoordinates[edge[1]][0];
            double y2 = initialCoordinates[edge[1]][1];

            Bond bond = new Bond(x1, y1, x2, y2, nodes[edge[0]], nodes[edge[1]]);

            if (counter>=numberOfNodes) {
                bond.setStroke(Color.DARKRED);
            }
            bonds[counter] = bond;
            counter++;
        }

        //Add edges
        for (Bond b : bonds){
            this.graphGroup.getChildren().add(b);
        }
        //Add nodes
        for (Node n : nodes) {
            this.graphGroup.getChildren().add(n);
        }



        //Enable node-dragging - NOT IN THIS PROGRAM
        //setDragNode(nodes);

        //Create Timeline with KeyFrame and KeyValues and play it
        KeyValue[] keysEnd = createEndKeyValues (numberOfNodes, nodes, endNodes);
        KeyFrame keyFrameEnd = new KeyFrame(Duration.millis(1000), keysEnd);
        Timeline timeline = new Timeline(keyFrameEnd);
        timeline.play();


    }

    /**
     * Create a KeyValue Array with all KeyValues needed
     * @param numberOfNodes
     * @param nodes
     * @param endNodes
     * @return
     */
    private KeyValue[] createEndKeyValues(int numberOfNodes, Circle[] nodes, Circle[] endNodes){
        //Create EndKeyFrame
        KeyValue[] keysEndX = new KeyValue[numberOfNodes+1];
        for (int i = 0; i < numberOfNodes; i++) {
            keysEndX[i] = new KeyValue(nodes[i].centerXProperty(), endNodes[i].getCenterX());
        }
        KeyValue[] keysEndY = new KeyValue[numberOfNodes+1];
        for (int i = 0; i < numberOfNodes; i++) {
            keysEndY[i] = new KeyValue(nodes[i].centerYProperty(), endNodes[i].getCenterY());
        }
        //Finalize keysEnd array
        KeyValue[] keysEnd = new KeyValue[keysEndX.length+keysEndY.length];
        for (int i = 0; i < keysEndX.length; i++) {
            keysEnd[i] = keysEndX[i];
        }
        for (int i = keysEndX.length; i < keysEndX.length+keysEndY.length; i++) {
            keysEnd[i] = keysEndY[i-keysEndX.length];
        }
        return keysEnd;
    }



    /**
     * Set all nodes on a circle
     * @param numberOfNodes
     * @return coordinates
     */
    private double[][] setCoordniatesOnCircle(int numberOfNodes){
        double coordinates[][] = new double[numberOfNodes][2];
        int count=0;
        for(int v=0;v<numberOfNodes;v++) {
            coordinates[v][0] = (float) (100 * Math.sin(2.0 * Math.PI * (double) count / (double) numberOfNodes));
            coordinates[v][1] = (float) (100 * Math.cos(2.0 * Math.PI * (double) count / (double) numberOfNodes));
            count++;
        }
        return coordinates;
    }
}
