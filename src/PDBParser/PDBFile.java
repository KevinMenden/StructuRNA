package PDBParser;

import PDBParser.Atom;
import javafx.geometry.Point3D;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
                    //System.out.println(atom.getResidueNumber() + "   " + atom.getIdentity() + "  " + atom.getCoordinates()[0] + " " +
                    //atom.getCoordinates()[1] + "  " + atom.getCoordinates()[2]);
                }
                //Append remarks
                else if (line.startsWith("REMARK")){
                    remarks.append(line);
                    line = br.readLine();
                }
                //Append header
                else if (line.startsWith("HEADER")){
                    header.append(line);
                    line = br.readLine();
                }
                //Append title
                else if (line.startsWith("TITLE")){
                    title.append(line);
                    line = br.readLine();
                }
                //Append comund
                else if (line.startsWith("COMPND")){
                    compound.append(line);
                    line = br.readLine();
                }
                //Append source
                else if (line.startsWith("SOURCE")){
                    source.append(line);
                    line = br.readLine();
                }
                //Append author
                else if (line.startsWith("AUTHOR")){
                    author.append(line);
                    line = br.readLine();
                }
                //Append journal
                else if (line.startsWith("JRNL")){
                    journal.append(line);
                    line = br.readLine();
                }
                //Append revdat
                else if (line.startsWith("REVDAT")){
                    revdat.append(line);
                    line = br.readLine();
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

        //Center all the atoms
        centerAllAtoms(atoms);
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
                sequenceBuilder.append(atom.getBase().substring(0,1));
            }
            residueNumber = atom.getResidueNumber();
        }
        this.sequence = sequenceBuilder.toString();
    }

    /**
     * Center Points around the middle
     *important to for rotation
     * @param points
     */
    public static ArrayList<Point3D> center(ArrayList<Point3D> points) {
        ArrayList<Point3D> result=new ArrayList<>(points.size());
        if (points.size() > 0) {
            double[] center = {0, 0, 0};

            for (Point3D point : points) {
                center[0] += point.getX();
                center[1] += point.getY();
                center[2] += point.getZ();
            }
            center[0] /= points.size();
            center[1] /= points.size();
            center[2] /= points.size();

            for (Point3D point : points) {
                result.add(point.subtract(new Point3D(center[0], center[1], center[2])));
            }
        }
        return result;
    }

    /**
     * Apply center() for all elements of atoms
     * @param atoms
     */
    private void centerAllAtoms(Atom[] atoms){
        ArrayList<Point3D> points = new ArrayList<>(atoms.length);
        //Add all points to the ArrayList
        for (Atom atom : atoms){
            points.add(atom.getPoint());
        }
        //Center the points
        points = center(points);
        int i = 0;
        for (Point3D point3D : points){
            atoms[i].setPoint(point3D);
            i++;
        }
        //Update the Array of Atoms
        //this.atoms = atoms;
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
