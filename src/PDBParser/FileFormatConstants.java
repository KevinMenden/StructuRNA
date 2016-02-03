package PDBParser;

/**
 * Created by kevin_000 on 24.01.2016.
 *
 * Constants of the pdb file format
 * Needed for parsing
 */
public class FileFormatConstants {

    public static final int NUMBER_START = 7;
    public static final int NUMBER_END = 11;

    public static final int IDENTITY_START = 12;
    public static final int IDENTITY_END = 16;

    public static final int BASE_START = 17;
    public static final int BASE_END = 20;

    public static final int RESIDUE_NUMBER_START = 22;
    public static final int RESIDUE_NUMBER_END = 26;

    public static final int ELEMENT_START = 77;
    public static final int ELEMENT_END = 78;

    public static final int X_START = 30;
    public static final int X_END = 38;

    public static final int Y_START = 38;
    public static final int Y_END = 46;

    public static final int Z_START = 46;
    public static final int Z_END = 54;


}
