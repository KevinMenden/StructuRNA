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

    //PDB file information
    private String remarks;
    private String header;
    private String title;
    private String compound;
    private String source;
    private String journal;
    private String author;
    private String revdat;



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
        //StringBuilder for additional information
        StringBuilder remarks = new StringBuilder();
        StringBuilder header = new StringBuilder();
        StringBuilder title = new StringBuilder();
        StringBuilder compound = new StringBuilder();
        StringBuilder source = new StringBuilder();
        StringBuilder journal = new StringBuilder();
        StringBuilder author = new StringBuilder();
        StringBuilder revdat = new StringBuilder();

        String line;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            //Read the lines of the file
            line = br.readLine();
            int index = 0;
            while (line != null && line != "END") {
                Atom atom = new Atom();
                //If atom, save all necessary information
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
                }
                //Append remarks
                else if (line.startsWith("REMARK")){
                    remarks.append(line);
                }
                //Append header
                else if (line.startsWith("HEADER")){
                    header.append(line);
                }
                //Append title
                else if (line.startsWith("TITLE")){
                    title.append(line);
                }
                //Append comund
                else if (line.startsWith("COMPND")){
                    compound.append(line);
                }
                //Append source
                else if (line.startsWith("SOURCE")){
                    source.append(line);
                }
                //Append author
                else if (line.startsWith("AUTHOR")){
                    author.append(line);
                }
                //Append journal
                else if (line.startsWith("JRNL")){
                    journal.append(line);
                }
                //Append revdat
                else if (line.startsWith("REVDAT")){
                    revdat.append(line);
                }
                else line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("System can't find PDB file. (PDBFile.readFile())");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //If available, save information of the file
        if (header != null) this.header = header.toString();
        if (title != null) this.title = title.toString();
        if (remarks != null) this.remarks = remarks.toString();
        if (compound != null) this.compound = compound.toString();
        if (author != null) this.author = author.toString();
        if (journal != null) this.journal = journal.toString();
        if (source != null) this.source = source.toString();
        if (revdat != null) this.revdat = revdat.toString();

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCompound() {
        return compound;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRevdat() {
        return revdat;
    }

    public void setRevdat(String revdat) {
        this.revdat = revdat;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
