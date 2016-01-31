package Presenter;

import HBondInference.HydrogonBonds;
import Model.Nucleotide;
import Model2D.*;
import PDBParser.Atom;
import PDBParser.PDBFile;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by Kevin Menden on 20.01.2016.
 * Holds all important objects and distributes them to the
 * right classes
 * Controls instances of
 * - Presenter2D
 * - Presenter3D
 *
 */

public class Presenter {

    //Presenters
    private Presenter2D presenter2D = new Presenter2D();
    private Presenter3D presenter3D = new Presenter3D();

    //The PDBFile that is currently loaded
    private PDBFile pdbFile;

    private String sequence;

    private Atom[] atoms;

    //3D structure pane
    private Pane structurePane;

    //2D structure pane
    Pane secondaryStructurePane;

    public Presenter(){};

    /*
    Initialize the PDBFile with a given path
     */
    public void loadFile(String filePath){
        this.pdbFile = new PDBFile(filePath);
        this.sequence = pdbFile.getSequence();
        this.atoms = pdbFile.getAtoms();
    }
    //Getter for pdbFile
    public PDBFile getPdbFile() {
        return pdbFile;
    }
    //Setter for pdbFile
    public void setPdbFile(PDBFile pdbFile) {
        this.pdbFile = pdbFile;
    }


    /**
     * Set up a 2D Structure and add it to the secondary structure pane
     */
    public void setUp2DStructure(){
        //Make the secondary structure
        //Infer base pairing from PDB file
        presenter2D.setPdbFile(pdbFile);
        HydrogonBonds hydrogonBonds = new HydrogonBonds(sequence.length());
        System.out.println(hydrogonBonds.inferHydrogenBonds(presenter3D.getAtoms()));
        presenter2D.buildSecondaryStructureGraph(hydrogonBonds.inferHydrogenBonds(atoms));
        secondaryStructurePane.getChildren().clear();
        secondaryStructurePane.getChildren().add(presenter2D.getGraphGroup());

    }


    /**
     * Set up a 3D structure and add it to the structure pane
     */
    public void setUp3DStructure(){
        presenter3D.setAtoms(this.atoms);
        presenter3D.make3DStructure();
        //presenter3D.makeBallAndStickModel();
        structurePane.getChildren().clear();
        structurePane.getChildren().add(presenter3D.subScene);
    }

    /*
    GETTER AND SETTER SECTION
     */


    public Presenter2D getPresenter2D() {
        return presenter2D;
    }

    public void setPresenter2D(Presenter2D presenter2D) {
        this.presenter2D = presenter2D;
    }

    public Presenter3D getPresenter3D() {
        return presenter3D;
    }

    public void setPresenter3D(Presenter3D presenter3D) {
        this.presenter3D = presenter3D;
    }

    public Pane getSecondaryStructurePane() {
        return secondaryStructurePane;
    }

    public void setSecondaryStructurePane(Pane secondaryStructurePane) {
        this.secondaryStructurePane = secondaryStructurePane;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }


    public Pane getStructurePane() {
        return structurePane;
    }

    public void setStructurePane(Pane structurePane) {
        this.structurePane = structurePane;
    }
}
