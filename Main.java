import com.sun.xml.internal.ws.util.StringUtils;

import java.util.*;

public class Main {
    ArrayList<String> cosetLeaders = new ArrayList<>(); // stores all the coset leaders

    TreeMap<String, String> syndromeMap = new TreeMap<>(); // stores the syndrome map

    // parity check matrix
    int[][] pcMatrix =
            {
                    {1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0},
                    {1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0},
                    {1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0},
                    {0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0},
                    {0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0},
                    {0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1}
            };

    public static void main(String[] args) {
        Main main = new Main();

        // find the syndrome table
        main.generateCosetLeaders(12);
        main.calcSyndrome();

        // execute syndrome decoding for the given values
        main.syndromeDecode("111111111111");
        main.syndromeDecode("000011100000");
        main.syndromeDecode("001100110000");
    }

    private void generateCosetLeaders(int pow) {

        int total = (int) Math.pow(2, pow);
        for (int i = 0; i < total; i++) { // generates all the coset leaders from 1 to the given power 12
            String result = Integer.toBinaryString(i);
            String resultFormatted = String.format("%12s", result).replaceAll(" ", "0");
            cosetLeaders.add(resultFormatted);
        }

    }


    private void calcSyndrome() {
        int weight = 0;

        ArrayList<String> syndromeList = new ArrayList<>();
        // iterate for all 12 weights
        for (int i = 0; i <= 12; i++) {
            ArrayList<String> filteredCosetLeaders = new ArrayList<>();
            // filter coset leaders starting weight 0
            for (String bnry : cosetLeaders) {
                if (bnry.chars().filter(ch -> ch == '1').count() == weight) {
                    filteredCosetLeaders.add(bnry);
                }
            }
            // iterate for each coset leader in the filtered list
            for (String cosetLeader : filteredCosetLeaders) {
                StringBuilder syndrome = new StringBuilder(" ");
                // take each vector in pc matrix
                for (int[] matrix : pcMatrix) {
                    int vectorMultSum = 0;
                    // multiply bits of the vector with the bits in current coset leader in order
                    for (int k = 0; k < matrix.length; k++) {
                        char chr = cosetLeader.charAt(k);
                        vectorMultSum += matrix[k] * Integer.parseInt(String.valueOf(chr));
                    }
                    syndrome.append(String.valueOf(vectorMultSum % 2));
                }
                // save the calculated syndrome with coset leader if the syndrome does not exist
                if (!syndromeList.contains(syndrome.toString())) {
                    syndromeList.add(syndrome.toString());
                    syndromeMap.put(cosetLeader, String.valueOf(syndrome));
                }
            }
            weight++;

        }

        // print syndrome table
        System.out.println(" Coset Leader  |  Syndrome ");
        System.out.println("---------------------------");
        for (Map.Entry<String, String> entry : syndromeMap.entrySet()) {
            System.out.println(" " + entry.getKey() + "  |  " + entry.getValue());
        }
        System.out.println("Table size: " + syndromeMap.size());
    }

    private void syndromeDecode(String received) {
        StringBuilder syn = new StringBuilder(" ");
        String cosetLeaderE = "";
        StringBuilder decodedResult = new StringBuilder(" ");
        // find the syn of received vector
        for (int[] matrix : pcMatrix) {
            int vectorMultSum = 0;
            // multiply bits of the vector with the bits in received word in order
            for (int k = 0; k < matrix.length; k++) {
                char chr = received.charAt(k);
                vectorMultSum += matrix[k] * Integer.parseInt(String.valueOf(chr));
            }
            syn.append(String.valueOf(vectorMultSum % 2));
        }

        // find the matching coset leader 'e' from syndrome table map
        for(Map.Entry<String, String> entry: syndromeMap.entrySet()) { 
            if(Objects.equals(entry.getValue(), String.valueOf(syn))) {
                cosetLeaderE = entry.getKey();
                break;
            }
        }
        //find the decoding result by subtracting coset leader from received word
        for (int i = 0; i < cosetLeaderE.length(); i++) {
            int y = Integer.parseInt(String.valueOf(received.charAt(i)));
            int e = Integer.parseInt(String.valueOf(cosetLeaderE.charAt(i)));
            decodedResult.append(String.valueOf(Math.abs(y - e)));
        }

        // print the decoding result summary
        System.out.println();
        System.out.println("Y = " + received + " | E = " + cosetLeaderE + " | Syndrome = " + syn);
        System.out.println("Decoded " + received + " = " + decodedResult);

    }

}
