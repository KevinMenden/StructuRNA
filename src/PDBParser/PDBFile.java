package PDBParser;

import PDBParser.Atom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by kevin_000 on 14.12.2015.
 */
public class PDBFile {

    //path of the pdb file
    String filePath = null;

    //Store all atoms
    private Atom[] atoms;

    //Number of atoms in PDB file
    private int numberOfAtoms;

    //Sequence of the RNA Structure
    private String sequence = "";

    //Create instance of PDB file with correctly sized atoms array
    public PDBFile(String filePath) {
        this.filePath = filePath;
        this.numberOfAtoms = getAtomNumber();
        this.atoms = new Atom[this.numberOfAtoms];
        readFile();
        initializeTheSeqeunce();
    }

    /**
     * Read through the file and count the number of atoms
     * @return number
     */
    private int getAtomNumber() {
        int number = 0;
        String line;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            while (line != null) {
                String lineContent = line.split("\\s+")[0];
                if (lineContent.equals("ATOM")) {
                    number += 1;
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("System can't find PDB file. (PDBFile.getAtomNumber())");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    /**
     * Read the PDB file and store the information about the atoms
     * in the atoms array
     */
    private void readFile() {
        String line;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            //Read the lines of the file
            line = br.readLine();
            int index = 0;
            while (line != null) {
                Atom atom = new Atom();
                //String[] splitString = line.split("\\s+");
                //If atom, save stuff
                if (line.startsWith("ATOM")) {
                    atom.setNumber(Integer.parseInt(line.substring(FileFormatConstants.NUMBER_START, FileFormatConstants.NUMBER_END).replace(" ", "")));
                    atom.setIdentity(line.substring(FileFormatConstants.IDENTITY_START, FileFormatConstants.IDENTITY_END).replace(" ", ""));
                    atom.setBase(line.substring(FileFormatConstants.BASE_START, FileFormatConstants.BASE_END).replace(" ",""));
                    atom.setResidueNumber(Integer.parseInt(line.substring(FileFormatConstants.RESIDUE_NUMBER_START, FileFormatConstants.RESIDUE_NUMBER_END).replace(" ","")));
                    atom.setCoordinates(new float[] {
                            Float.parseFloat(line.substring(FileFormatConstants.X_START,FileFormatConstants.X_END).replace(" ","")),
                            Float.parseFloat(line.substring(FileFormatConstants.Y_START,FileFormatConstants.Y_END).replace(" ", "")),
                            Float.parseFloat(line.substring(FileFormatConstants.Z_START, FileFormatConstants.Z_END).replace(" ",""))
                    });
                    atom.make3DPoint();
                    atom.setElement(line.substring(FileFormatConstants.ELEMENT_START,FileFormatConstants.ELEMENT_END).replace(" ",""));
                    //Store atom in atom array
                    this.atoms[index] = atom;
                    index++;
                    line = br.readLine();
                } else line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("System can't find PDB file. (PDBFile.readFile())");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Get the RNA sequence of the molecule
     */
    private void initializeTheSeqeunce(){
        int residueNumber = atoms[0].getResidueNumber();
        StringBuilder sequenceBuilder = new StringBuilder();
        sequenceBuilder.append(atoms[0].getBase());

        for (Atom atom : atoms){
            if (atom.getResidueNumber() > residueNumber){
                sequenceBuilder.append(atom.getBase());
            }
            residueNumber = atom.getResidueNumber();

        }
        this.sequence = sequenceBuilder.toString();
    }


    /*
    GETTER & SETTER
     */

    public Atom[] getAtoms() {
        return atoms;
    }

    public void setAtoms(Atom[] atoms) {
        this.atoms = atoms;
    }

    public int getNumberOfAtoms() {
        return numberOfAtoms;
    }

    public void setNumberOfAtoms(int numberOfAtoms) {
        this.numberOfAtoms = numberOfAtoms;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
