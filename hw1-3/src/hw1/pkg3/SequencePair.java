package hw1.pkg3;

import java.util.ArrayList;

/**
 *
 * @author Kumar Kunal
 */
public class SequencePair {

    private String str1;
    private String str2;
    private String mergedString;
    private int alignmentScore;
    private int[][] dp;

    public SequencePair(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;
        this.dp = new int[str1.length() + 1][str2.length() + 1];
    }

    public String getMergedString() {
        return mergedString;
    }

    public int getAlignmentScore() {
        return alignmentScore;
    }
    
    /**
     * Backtrack and compute the merged string. Update the members.
     */
    public void updateAligmentScore() {
        int lastRowMax = Integer.MIN_VALUE;
        int lastColMax = Integer.MIN_VALUE;
        int lastColMaxIdx = -1;
        int lastRowMaxIdx = -1;

        for (int i = 1; i < dp.length; ++i) {
            if (dp[i][str2.length()] > lastColMax) {
                lastColMax = dp[i][str2.length()];
                lastColMaxIdx = i; // rowEndIdx, lastCol
            }
        }
        for (int j = 1; j < dp[0].length; ++j) {
            if (dp[str1.length()][j] > lastRowMax) {
                lastRowMax = dp[str1.length()][j];
                lastRowMaxIdx = j;
            }
        }
        alignmentScore = (lastRowMax > lastColMax) ? lastRowMax : lastColMax;
        
        //Merge the two strings
        int k = traceTopRight(dp, lastColMaxIdx);
        int l = traceBottomLeft(dp, lastRowMaxIdx);

        if (lastColMax>lastRowMax) {
            //Order str2 + str1
            mergedString = str2.substring(0,k).concat(str1);
        } else {
            //Order str1 + str2
            mergedString = str1.substring(0,l).concat(str2);
        }
    }
    
    public int[][] getDp() {
        return this.dp;
    }

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }

    private int traceTopRight(int[][] dp, int lastColMaxIdx) {
        int j = str2.length();
        while (lastColMaxIdx > 0 && j>0) {
            if (dp[lastColMaxIdx - 1][j - 1] >= dp[lastColMaxIdx][j - 1]) {
                --j;
                --lastColMaxIdx;
            } else {
                --lastColMaxIdx;
            }
        }
        return j;
    }

    private int traceBottomLeft(int[][] dp, int lastRowMaxIdx) {
        int i = str1.length();
        while (lastRowMaxIdx > 0 && i>0) {
            if (dp[i - 1][lastRowMaxIdx - 1] >= dp[i][lastRowMaxIdx - 1]) {
                --i;
                --lastRowMaxIdx;
            } else {
                --lastRowMaxIdx;
            }
        }
        return i;
    }
}
