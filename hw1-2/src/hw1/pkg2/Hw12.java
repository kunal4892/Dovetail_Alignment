package hw1.pkg2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Breakdown the sequences into subsequences of varied length randomly selecting
 * a random number between a min and max value as the length of the current
 * subsequence.
 * 
 * @author kumar Kunal
 */
public class Hw12 {

    private static void writeInFasta(String str, BufferedWriter writer) throws IOException {
        int charCount = 0;
        String startStr = "|>\n";
//        System.out.println(str);
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

    private static ArrayList<String> split(String str, int min, int max) {
        double randomD = Math.random();
        randomD = randomD * (max - min) + min;
        int rand_normalized = (int) randomD;

        ArrayList<String> strList = new ArrayList<>();
        while (rand_normalized <= str.length()) {
            String subStr = str.substring(0, rand_normalized);
            strList.add(subStr);
            str = str.substring(rand_normalized);

            randomD = Math.random();
            randomD = randomD * (max - min) + min;
            rand_normalized = (int) randomD;
        }
        return strList;
    }

    public static void main(String[] args) {
        if (args.length == 4) {
            //Extract the inputs
            String inputFile = args[0];
            int min_len = Integer.parseInt(args[1]);
            int max_len = Integer.parseInt(args[2]);
            String outputFile = args[3];

            Scanner reader;
            String line;
            try {
                File file = new File(outputFile);
                if (file.exists() && file.isFile()) {
                    file.delete();
                    file.createNewFile();
                }
                
                StringBuilder str = new StringBuilder();
                reader = new Scanner(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
                while (reader.hasNextLine()) {
                    line = reader.nextLine().trim();
                    str.append(line);
                }
                String result[] = str.toString().trim().split("\\|\\>");
                for (String seq : result) {
                    ArrayList<String> strList = split(seq.trim(), min_len, max_len);
                    for (String s : strList) {
                        writeInFasta(s, writer);
                    }
                }
                
                reader.close();
                writer.close();
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            System.out.println("Wrong inputs. Please follow the input format.");
            System.out.println("Please restart the Program");
        }
    }
}
