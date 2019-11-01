/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw1.pkg3;

/**
 * 
 * @author Kumar Kunal
 */
public class DovetailAlignment {

    private int score;
    private int penalty_delete;
    private int penalty_replace;
    private SequencePair sp;

    public DovetailAlignment(int score, int penalty_delete, int penalty_replace, SequencePair sp) {
        this.score = score;
        this.penalty_delete = penalty_delete;
        this.penalty_replace = penalty_replace;
        this.sp = sp;
    }

    private int getReplacementPenalty(Character c1, Character c2) {
        if (c1.equals(c2)) {
            return this.score;
        } else {
            return this.penalty_replace;
        }
    }

    private void computeScore() {
        String s1 = sp.getStr1();
        String s2 = sp.getStr2();
        int dp[][] = sp.getDp();
        
        int rows = dp.length;
        int cols = dp[0].length;

        for (int i = 0; i < rows; ++i) {
            dp[i][0] = 0;
        }

        for (int j = 0; j < cols; ++j) {
            dp[0][j] = 0;
        }

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                dp[i][j] = Math.max(dp[i - 1][j - 1] + getReplacementPenalty(s1.charAt(i - 1), s2.charAt(j - 1)),
                        Math.max(dp[i - 1][j] + this.penalty_delete, dp[i][j - 1] + this.penalty_delete));
            }
        }
        
        sp.updateAligmentScore();
    }

    public void align() {
        computeScore();
    }

}
