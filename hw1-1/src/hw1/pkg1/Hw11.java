package hw1.pkg1;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Generate main Sequence and mutate the main sequence multiple times.
 * Write to file all the sequences generated.
 * 
 * @author Kumar Kunal - 51005964
 * 
 */
public class Hw11 {
    
    private static String buildFirstSeq(int a, int c, int g, int t, int n){
        StringBuilder str = new StringBuilder("");
        Random rand = new Random();
        while(n>0){
            int r =  rand.nextInt(t+1);
            if(r > g ){
                str.append("T");
            }
            else if(r > c){
                str.append("G");
            }
            else if(r > a){
                str.append("C");
            }
            else{
                str.append("A");
            }
        --n;
        }
        return str.toString();
    }
    
    /**
     * 
     * @param str Original String
     * @param prob_mut probability of Mutation.
     * @return Mutated string.
     * 
     * Generates mutations(replace, delete) with the probability @param prob_mut
     */
    private static String generateNextSequences(String str, float prob_mut){
        StringBuilder sBuild = new StringBuilder(str);
        
        // Storing replacement options for a character.
        HashMap<Character, Character[]> replacementMap = new HashMap<Character, Character[]>();
        replacementMap.put('A', new Character[]{'C','G','T'});
        replacementMap.put('C', new Character[]{'A','G','T'});
        replacementMap.put('G', new Character[]{'A','C','T'});
        replacementMap.put('T', new Character[]{'A', 'C','G'});
        
        // Probability of Modificaion 
        prob_mut*= 100;
        Random rand = new Random();
        
        for(int i=0; i<sBuild.length(); ++i){
            // nextInt is not inclusive -> 101 for 100 percent
            if(rand.nextInt(101)<= prob_mut){
                // Simulate a coin-flip for modification, no modification.
                int j=rand.nextInt(2);
                // 50% probability of mutation.
                if(j == 0){
                    // Replacement
                    sBuild.setCharAt(i, replacementMap.get(sBuild.charAt(i))[rand.nextInt(3)]);
                }else{
                    // Deletion
                    sBuild.deleteCharAt(i);
                    --i;
                }
            }
        }
        return sBuild.toString();
    }
    
    /**
     * 
     * @param str String value 
     * @param outputFile File to write to
     * @param isDelReq false when appending is desired.
     * @throws IOException 
     * Write @param str to @param outputFile
     */
    private static void writeInFasta(String str, String outputFile, boolean isDelReq ) throws IOException{
        
        System.out.println(str);
        File file = new File(outputFile);
        BufferedWriter writer;
        
        if(file.exists() && file.isFile() && isDelReq){
            file.delete();
            file.createNewFile();
        }
        
        writer = new BufferedWriter(new FileWriter(outputFile, true));
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
        
        writer.close();
    }
    
    
    private static void sequenceToFile(int n, int a, int c, int g, int t, int k, float prob_mut, String outputFile) throws IOException{
        String str = buildFirstSeq(a, c, g, t, n);
        writeInFasta(str, outputFile, true);
        // Generate the k-1 sequences
        while(k >1)
        {
            String str_next = generateNextSequences(str, prob_mut);
            writeInFasta(str_next, outputFile, false);
            --k;
        }
    }
    
    /**
     * 
     * @param args
     * @throws IOException 
     * 
     * Reads the frequencies of A, C, G, T in a cumulative fashion.
     * It's monotonically increasing.
     * We normalize this range to (0,100) for sampling with effective
     * probabilities.
     */
    public static void main(String[] args) throws IOException{
        if(args.length == 8){
            
            //Extract the inputs
            int n = Integer.parseInt(args[0]);
            
            // Cumulative count for sampling
            int a = Integer.parseInt(args[1]);
            int c = Integer.parseInt(args[2]) + a;
            int g = Integer.parseInt(args[3]) + c;
            int t = Integer.parseInt(args[4]) + g;
            
            // Rest of the arguments
            int k = Integer.parseInt(args[5]);
            float prob_mut = Float.parseFloat(args[6]);
            String outputFile = args[7];
            
            if(prob_mut>0 && prob_mut<1 ){
                try{
                sequenceToFile(n, a, c, g, t, k, prob_mut, outputFile);
                }catch(IOException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
        else{
            System.out.println("Wrong inputs. Please follow the input format.");
            System.out.println("Please restart the Program");
        }
        
    }
    
}
