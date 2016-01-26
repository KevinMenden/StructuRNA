package Model3D;

import PDBParser.Atom;

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


    public void setAtoms(Atom[] atoms) {
        this.atoms = atoms;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Check an array of Atoms for H-bonds
     * Return the dot-bracket format of the structure
     * @param atoms
     * @return dotBracket String
     */
    public String inferHydrogenBonds(Atom[] atoms){
        //make a sequence of dots
        StringBuilder dotBracket = new StringBuilder(sequence.length());
        for (int i = 0; i < sequence.length(); i++){
            dotBracket.append(".");
        }
        //Array of HBondBuilers
        HBondBuilder[] hBondBuilders = new HBondBuilder[sequence.length()];

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
            if (isHbondBuilderAtom(atom)) {
                hBondBuilders[index].setAtom(atom);
            }
        }

        //Iterate through HBondBuilder array and infer Hbonds
        int indexOne = 0;
        int indexTwo = 0;

        //First HBondBuilder
        for (HBondBuilder builderA : hBondBuilders){
            indexTwo = 0;
            //Second HBondBuilder
            for (HBondBuilder builderB : hBondBuilders){
                //Check H bond
                if (HBondBuilder.isHbond(builderA, builderB)){
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
        if (dots == 0 && close == 0 && open == 0){
            return false;
        } else if (open == close) {
            bool = true;
        }
        return bool;
    }

    /**
     * Check if atom is a possible H-bond builder
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


}
