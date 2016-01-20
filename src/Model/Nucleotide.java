package Model;

/**
 * Created by Kevin Menden on 11.11.2015.
 */

/**
 * Created by Kevin Menden on 22.10.2015.
 */
public class Nucleotide {
    char nucleotide;

    public Nucleotide(char n) {
        if (this.isNucleotide(n)) {
            this.nucleotide = n;
        } else{
            System.out.println("Character " + n + " is not a nucleotide");
            this.nucleotide = 'N';}
    }

    public char getNucleotide() {
        return this.nucleotide;
    }

    public void setNucleotide(char n) {
        this.nucleotide = n;
    }

    public boolean isGap(Nucleotide n) {
        if (n.getNucleotide()=='-') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNucleotide(char n) {
        switch (n) {
            case 'A':
                return true;
            case 'T':
                return true;
            case 'G':
                return true;
            case 'C':
                return true;
            case 'U':
                return true;
            case 'a':
                return true;
            case 't':
                return true;
            case 'g':
                return true;
            case 'c':
                return true;
            case 'u':
                return true;
            default:
                return false;
        }
    }

}

