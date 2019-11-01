package hw1.pkg3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Takes sequences pairwise and calculates alignment scores and merge
 * until only a single sequence is left.
 * Writes this sequence into a file in FASTA format
 * @author kumar kunal
 */
public class Hw13 {

    /**
     * 
     * @param writer The BufferedWriter object
     * @param outputFile The file to write to 
     * @param outputList The stuff to write in the file.
     */
    public static void writeInFasta(BufferedWriter writer, String outputFile, List<String> outputList) throws IOException{
        for (String str : outputList) {
            int charCount = 0;
            String startStr = "|>\n";
            writer.write(startStr);
            for (Character ch : str.toCharArray()) {
                if (charCount == 80) {
                    writer.write("\n" + ch);
                    charCount = 1;
                } else {
                    writer.write(ch);
                    charCount++;
                }
            }
            writer.write("\n");
        }
    }

 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 5) {
            //Extract the inputs
            String inputFile = args[0];
            int score_match = Integer.parseInt(args[1]);
            int penalty_replace = Integer.parseInt(args[2]);
            int penalty_delete = Integer.parseInt(args[2]);
            String outputFile = args[4];

            Scanner reader;
            String line;
            try {
                File file = new File(outputFile);
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false));

                if (file.exists() && file.isFile()) {
                    file.delete();
                    file.createNewFile();
                }

                StringBuilder str = new StringBuilder();
                reader = new Scanner(new FileReader(inputFile));
                while (reader.hasNextLine()) {
                    line = reader.nextLine().trim();
                    str.append(line);
                }

                String result[] = str.toString().trim().split("\\|\\>");

                int maxScore = Integer.MIN_VALUE;
                SequencePair spBest = null;
                List<String> resultList = new LinkedList<String>(Arrays.asList(result));
                List<String> outputList = new LinkedList<String>();
                
                if (resultList.get(0).isEmpty()) {
                    resultList.remove(0);
                }
                
                int fragment1_Idx = Integer.MIN_VALUE;
                int fragment2_Idx = Integer.MIN_VALUE;

                String best_merged_string = "";
                for (int i = 0; i < resultList.size() - 1; ++i) {
                    for (int j = i + 1; j < resultList.size(); ++j) {
                        SequencePair sp_temp = new SequencePair(result[i].trim(), result[j].trim());
                        new DovetailAlignment(score_match, penalty_delete, penalty_replace, sp_temp).align();
                        if (maxScore < sp_temp.getAlignmentScore()) {
                            maxScore = sp_temp.getAlignmentScore();
                            spBest = sp_temp;
                            best_merged_string = spBest.getMergedString();
                            fragment1_Idx = i;
                            fragment2_Idx = j;
                        }
                    }
                }

                if (maxScore >= 0) {
                    resultList.remove(fragment1_Idx);
                    resultList.remove(fragment2_Idx);
                }

                //maxScore = Integer.MIN_VALUE;
                while (resultList.size() > 0 && maxScore >= 0) {
                    boolean found = false;
                    maxScore = Integer.MIN_VALUE;
                    for (int i = 0; i < resultList.size(); ++i) {
                        SequencePair sp_recur = new SequencePair(best_merged_string.trim(), resultList.get(i));
                        new DovetailAlignment(score_match, penalty_delete, penalty_replace, sp_recur).align();
                        if (maxScore < sp_recur.getAlignmentScore()) {
                            maxScore = sp_recur.getAlignmentScore();
                            spBest = sp_recur;
                            found = true;
                            fragment1_Idx = i;
                        }
                    }
                    if (found) {
                        best_merged_string = spBest.getMergedString();
                        resultList.remove(fragment1_Idx);
                    }
                }
                outputList.add(best_merged_string);
                if (!resultList.isEmpty()) {
                    outputList.addAll(resultList);
                    resultList.clear();
                }
                
                writeInFasta(writer,outputFile, outputList);
                reader.close();
                writer.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Please Enter the correct file to read from");
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println("IO Exception caught. Something unexpected happened.");
                System.out.println(ex);
            }
        } else {
            System.out.println("Wrong input format. Please follow the correct format");
        }

    }

}
