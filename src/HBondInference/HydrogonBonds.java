package HBondInference;

import PDBParser.Atom;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;


/**
 * Created by Kevin Menden on 24.01.2016.
 *
 * Class extracts data from a 3D molecular data set
 * and infers H bonds from it
 * Can construct a Dot-Bracket string for secondary struture
 */
public class HydrogonBonds {

    //Atom array of the molecule
    private Atom[] atoms;

    //Secondary structure in Dot Bracket format
    private String dotBracket;

    //Sequence of the molecule
    private String sequence = "";

    //Sequence length
    private int sequenceLength;

    //ArrayList of hbonds
    private ArrayList<Cylinder> hbonds;

    //ArrayList of hbond building atoms
    private ArrayList<Sphere> bondAtoms;

    //ArrayList of atom connections
    private ArrayList<Cylinder> hbondAtomConnections;

    //Array of all nucleotides as HBondBuilder objects
    private HBondBuilder[] hBondBuilders;

    public HydrogonBonds(int sequenceLength){
        this.sequenceLength = sequenceLength;
    }

    /**
     * Check an array of Atoms for H-bonds
     * Return the dot-bracket format of the structure
     * @param atoms
     * @return dotBracket String
     */
    public String inferHydrogenBonds(Atom[] atoms){
        //make a sequence of dots
        StringBuilder dotBracket = new StringBuilder(sequenceLength);
        for (int i = 0; i < sequenceLength; i++){
            dotBracket.append(".");
        }
        //Array of HBondBuilers
        hBondBuilders = new HBondBuilder[sequenceLength];
        HBondBuilder generalBuilder = new HBondBuilder();

        //For all nucleotides, save the elements necessar for H bond infrence
        //i.e. create HBondBuilder objects for each nucleotide
        int currentReside = atoms[0].getResidueNumber();
        int index = 0;
        //Set up first element 0
        hBondBuilders[0] = new HBondBuilder();
        hBondBuilders[0].setBaseType(atoms[0].getBase());
        hBondBuilders[0].setResdieNumber(atoms[0].getResidueNumber());
        //Iterate through atom list
        for (Atom atom : atoms){
            if (atom.getResidueNumber() != currentReside){
                index++;
                hBondBuilders[index] = new HBondBuilder();
                hBondBuilders[index].setBaseType(atom.getBase());
                hBondBuilders[index].setResdieNumber(atom.getResidueNumber());
            }
            currentReside = atom.getResidueNumber();
            hBondBuilders[index].setAtom(atom);

        }

        //Iterate through HBondBuilder array and infer Hbonds
        int indexOne = 0;
        int indexTwo = 0;
        //First HBondBuilder
        for (HBondBuilder builderA : hBondBuilders){
            indexTwo = 0;
            //Second HBondBuilder
            for (HBondBuilder builderB : hBondBuilders){
                //Check if H bond
                if (generalBuilder.isHbond(builderA, builderB)){
                    //Set indices for hbuilders, set makesHbond to true
                    builderA.setBondIndices(indexOne, indexTwo);
                    builderB.setBondIndices(indexTwo, indexOne);
                    builderA.setMakesHbond(true);
                    builderB.setMakesHbond(true);

                    //Set brackets in dot bracket notation
                    if (indexOne < indexTwo){
                        dotBracket.setCharAt(indexOne, '(');
                        dotBracket.setCharAt(indexTwo, ')');
                    } else {
                        dotBracket.setCharAt(indexOne, ')');
                        dotBracket.setCharAt(indexTwo, '(');
                    }
                }
                indexTwo++;
            }
            indexOne++;
        }
        //Detect and delete pseudo knots
        PseudoKnots.deletePseudoKnots(hBondBuilders, dotBracket);
        this.hbonds = generalBuilder.getHbonds();
        this.bondAtoms = generalBuilder.getBondAtoms();
        this.hbondAtomConnections = generalBuilder.getBondAtomConnections();
        this.dotBracket = dotBracket.toString();
        return dotBracket.toString();
    }

    /**
     * Validate a given dotBracket notation
     * @param dotBracket
     * @return boolean
     */
    public static boolean isValidDotBracketNotation(String dotBracket){
        boolean bool = false;
        int open = 0;
        int close = 0;
        int dots = 0;
        char c;
        for (int i = 0; i < dotBracket.length(); i++){
            c = dotBracket.charAt(i);
            switch (c){
                case '(': open++; break;
                case ')': close++; break;
                case '.': dots++; break;
                default: break;
            }
        }
        //If no bonds are found, report invalid structure
        if (close == 0){
            return false;
        } else if (open == close) {
            bool = true;
        }
        return bool;
    }

    /**
     * Check if atom is a possible H-bond builder
     * CURRENTLY UNUSED METHOD
     * @param atom
     * @return boolean bool
     */
    private boolean isHbondBuilderAtom(Atom atom){
        String id = atom.getIdentity();
        String base = atom.getBase();
        boolean bool = false;
        //Base is Adenine
        if (base.equals("A")){
            switch (id){
                case "N6":
                    bool = true;
                    break;
                case "H62":
                    bool = true;
                    break;
                case "N1":
                    bool = true;
                    break;
                default:
                    break;
            }
        }
        //Base is Uracil
        else if (base.equals("U")){
            switch (id){
                case "H3":
                    bool = true;
                    break;
                case "N3":
                    bool = true;
                    break;
                case "O4":
                    bool = true;
                    break;
                default: break;
            }
        }
        //Base is Guanine
        else if (base.equals("G")){
            switch (id){
                case "H1":
                    bool = true;
                    break;
                case "H21":
                    bool = true;
                    break;
                case "N1":
                    bool = true;
                    break;
                case "N2":
                    bool = true;
                    break;
                case "O6":
                    bool = true;
                    break;
                default: break;
            }
        }
        //If base is cytosine
        else if (base.equals("C")){
            switch (id){
                case "N3":
                    bool = true;
                    break;
                case "N4":
                    bool = true;
                    break;
                case "H41":
                    bool = true;
                    break;
                case "O2":
                    bool = true;
                    break;
                default: break;
            }
        }
        return bool;
    }

    /*
    Method returns the indices of all H-bond building nucleotides
     */
    public int[] getHBondIndices(){
        //Count number of hbonds building nucleotides
        int counter = 0;
        for (HBondBuilder builder : hBondBuilders){
            if (builder.isMakesHbond()) counter++;
        }
        int[] indices = new int[counter];
        counter = 0;
        //Get the indices of all hbond building nucleotides
        for (HBondBuilder builder : hBondBuilders){
            if (builder.isMakesHbond()){
                indices[counter] = builder.getOwnIndex();
                counter++;
            }
        }
        return indices;
    }


    public Atom[] getAtoms() {
        return atoms;
    }

    public void setHbonds(ArrayList<Cylinder> hbonds) {
        this.hbonds = hbonds;
    }

    public void setBondAtoms(ArrayList<Sphere> bondAtoms) {
        this.bondAtoms = bondAtoms;
    }

    public String getDotBracket() {
        return dotBracket;
    }

    public void setDotBracket(String dotBracket) {
        this.dotBracket = dotBracket;
    }

    public String getSequence() {
        return sequence;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public ArrayList<Sphere> getBondAtoms() {return bondAtoms;}

    public void setAtoms(Atom[] atoms) {
        this.atoms = atoms;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public ArrayList<Cylinder> getHbonds() {return hbonds;}

    public ArrayList<Cylinder> getHbondAtomConnections() {
        return hbondAtomConnections;
    }

    public void setHbondAtomConnections(ArrayList<Cylinder> hbondAtomConnections) {
        this.hbondAtomConnections = hbondAtomConnections;
    }

    public HBondBuilder[] gethBondBuilders() {        return hBondBuilders;    }

    public void sethBondBuilders(HBondBuilder[] hBondBuilders) {        this.hBondBuilders = hBondBuilders;    }
}
