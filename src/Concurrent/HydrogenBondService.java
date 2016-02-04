package Concurrent;

import HBondInference.HydrogonBonds;
import PDBParser.Atom;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by kevin_000 on 03.02.2016.
 */
public class HydrogenBondService extends Service<HydrogonBonds>{

    private Atom[] atoms;
    private HydrogonBonds hydrogonBonds;

    @Override
    protected void succeeded() {
        super.succeeded();
        System.out.println("succeeded");
        System.out.println(this.getProgress());
        System.out.println(this.isRunning());
    }

    @Override
    protected void failed() {
        super.failed();
        System.out.println("failed");
    }

    @Override
    protected Task<HydrogonBonds> createTask() {
        return new Task<HydrogonBonds>() {
            @Override
            protected HydrogonBonds call() throws Exception {
                hydrogonBonds.inferHydrogenBonds(atoms);

                return null;
            }
        };
    }

    public void updateService(Atom[] atoms, HydrogonBonds hydrogonBonds){
        this.atoms = atoms;
        this.hydrogonBonds = hydrogonBonds;
    }
}
