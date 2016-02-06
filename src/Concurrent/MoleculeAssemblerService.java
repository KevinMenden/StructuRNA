package Concurrent;

import Model3D.MoleculeAssembler;
import PDBParser.Atom;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by Kevin Menden on 04.02.2016.
 *
 * Service can be used to assemble the 3D Structure in
 * extra Thread
 *
 *
 */
public class MoleculeAssemblerService extends Service {

    private MoleculeAssembler moleculeAssembler;
    private Atom[] atoms;
    private int sequenceLength;

    public MoleculeAssemblerService(Atom[] atoms, int sequenceLength){
        this.atoms = atoms;
        this.sequenceLength = sequenceLength;
    }


    /*
    If service starts, use MoleculeAssembler to create all structures
     */
    @Override
    protected Task createTask() {

        return new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println("Assembling started: " + System.currentTimeMillis());
                moleculeAssembler = new MoleculeAssembler(atoms, sequenceLength);
                moleculeAssembler.assembleMolecules();

                return null;
            }
        };
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        System.out.println("Molecule assembler succeded. Time: " + System.currentTimeMillis());
    }

    /*
        GETTER AND SETTER
         */
    public Atom[] getAtoms() {
        return atoms;
    }

    public void setAtoms(Atom[] atoms) {
        this.atoms = atoms;
    }

    public MoleculeAssembler getMoleculeAssembler() {
        return moleculeAssembler;
    }

    public void setMoleculeAssembler(MoleculeAssembler moleculeAssembler) {
        this.moleculeAssembler = moleculeAssembler;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }
}
