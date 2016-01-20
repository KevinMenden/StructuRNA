package Model;

/**
 * Created by Kevin Menden on 11.11.2015.
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Kevin Menden on 22.10.2015.
 * Used to store DNA or RNA sequences.
 */
public class Sequence {
    public String sequence;

    public final StringProperty seq = new SimpleStringProperty(this, "seq", null);

    /*
    Constructor
     */
    public Sequence(String seq) {
        this.sequence = seq;
    }

    public Sequence() {
        this.sequence = null;
    }
    /*
    Getter & Setter
     */
    public String getSequence() {
        return sequence;
    }

    public void setSequence(String seq) {
        this.sequence = seq;
    }

    public int length() {
        return this.sequence.length();
    }


    /**
     * Reverse a given sequence
     * @param seq
     * @return
     */
    public static String reverseSequence(String seq) {
        int length = seq.length();
        StringBuffer sequence = new StringBuffer(length);
        char[] seqChar = seq.toCharArray();
        for (int i = length-1; i >= 0; i--) {
            sequence.append(seqChar[i]);
        }
        return sequence.toString();
    }

    /*
Filters sequence for invalid characters
 */
    public static String filterSequence(String seq) {
        int length = seq.length();
        char[] seqChar = seq.toCharArray();
        StringBuffer sequence = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            if (Nucleotide.isNucleotide(seqChar[i])) {
                sequence.append(seqChar[i]);
            }
        }
        return sequence.toString().toUpperCase();

    }

    /**
     * Converts DNA sequence to complement
     * @param seq
     * @return
     */
    public static String toComplementDna(String seq) {
        seq = seq.toUpperCase();
        int length = seq.length();
        char[] seqChar = seq.toCharArray();
        StringBuffer sequence = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            switch (seqChar[i]) {
                case 'A':
                    sequence.append("T");
                    break;
                case 'T':
                    sequence.append("A");
                    break;
                case 'G':
                    sequence.append("C");
                    break;
                case 'C':
                    sequence.append("G");
                    break;
                default: break;

            }
        }
        return sequence.toString();
    }

    public static Double calculateGC(String seq) {
        Double gc = new Double(0);
        Double all = new Double(0);
        int length = seq.length();
        seq = seq.toUpperCase();
        for (int i = 0; i < length; i++) {
            switch (seq.charAt(i)) {
                case 'G':
                    gc++;
                    all++;
                    break;
                case 'C':
                    gc++;
                    all++;
                    break;
                case 'A':
                    all++;
                    break;
                case 'T':
                    all++;
                    break;
                default: break;
            }
        }
        Double content = new Double(gc/all * 100);
        return content;
    }
}

