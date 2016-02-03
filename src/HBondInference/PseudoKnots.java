package HBondInference;

/**
 * Created by Kevin Menden on 03.02.2016.
 *
 * Class contains methods for detecting pseudo knots
 */
public class PseudoKnots {


    /**
     * Detect pseudoknots and set their makesHbonds values to false
     * @param hBondBuilders
     * @return
     */
    public static void deletePseudoKnots(HBondBuilder[] hBondBuilders, StringBuilder dotBracket){

        for (HBondBuilder builderA : hBondBuilders){
            for (HBondBuilder builderB : hBondBuilders){
                if (builderA.getOwnIndex() < builderB.getOwnIndex()
                        && builderA.getPartnerIndex() > builderB.getOwnIndex()
                        && builderA.getPartnerIndex() < builderB.getPartnerIndex()){
                    dotBracket.setCharAt(builderA.getOwnIndex(), '[');
                    dotBracket.setCharAt(builderA.getPartnerIndex(), ']');
                    System.out.println("Pseudoknot deteced.");
                }
                else if (builderB.getOwnIndex() < builderA.getOwnIndex()
                        && builderB.getPartnerIndex() > builderA.getOwnIndex()
                        && builderB.getPartnerIndex() < builderA.getPartnerIndex()){
                    dotBracket.setCharAt(builderB.getOwnIndex(), '[');
                    dotBracket.setCharAt(builderB.getPartnerIndex(), ']');
                    System.out.println("Pseudoknot detected");
                }
            }
        }
    }
}
