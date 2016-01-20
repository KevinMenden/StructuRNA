package Presenter;

import Model.Nucleotide;
import Model2D.Bond;
import Model2D.Graph;
import Model2D.Node;
import Model2D.SpringEmbedder;
import PDBParser.PDBFile;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

/**
 * Created by Kevin Menden on 20.01.2016.
 * Holds all important objects and distributes them to the
 * right classes
 */

public class Presenter {

    //The PDBFile that is currently loaded
    public PDBFile pdbFile = null;

    //The Group that contains the 3D structure
    public Group structureGroup = new Group();

    //The Group that contains the 2D structure and the corresponding Graph
    public Group graphGroup = new Group();
    public Graph graphModel = new Graph();

    public Presenter(){};

    /*
    Initialize the PDBFile with a given path
     */
    public void loadFile(String filePath){
        this.pdbFile = new PDBFile(filePath);
    }



    /**
     * Use the given dotBracket notation to draw the secondary structure
     * @param dotBracket
     */
    public void buildSecondaryStructureGraph(String dotBracket){
        graphModel.parseNotation(dotBracket);
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
            this.graphModel.setSequence("AAGTCGACTGACTGACTGACTGACTGACTGACTGACTGAAGTCGACTGACTGACTGACTGACTGACTGACTGACTGAAGTCGACTGACTGACTGACTGACTGACTGACTGACTG");
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

}
