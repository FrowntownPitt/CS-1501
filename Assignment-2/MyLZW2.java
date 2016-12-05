/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW {
    private static final int C = 4;          // width of compression type string
    private static final int R = 256;        // number of input chars
    private static final int L = 4096;       // number of codewords = 2^W
    private static final int W = 12;         // codeword width
    private static final int MAXW = 16;
    private static final int MINW = 9;
    private static final int MAXL = 65536;   // number of codewords for range 9-16
    private static int currentW = 9;

    private static final int FIRST = 250;
    private static final int LAST = FIRST+8;

    public static void compress(int mode) { 
        /*if(mode == 0){
            BinaryStdOut.write(0, C);
            String input = BinaryStdIn.readString();
            TST<Integer> st = new TST<Integer>();
            for (int i = 0; i < R; i++)
                st.put("" + (char) i, i);
            int code = R+1;  // R is codeword for EOF

            while (input.length() > 0) {
                String s = st.longestPrefixOf(input);  // Find max prefix match s.
                BinaryStdOut.write(st.get(s), currentW);      // Print s's encoding.
                int t = s.length();
                if (t < input.length() && code < MAXL){    // Add s to symbol table.
                    st.put(input.substring(0, t + 1), code++);
                    if(code > Math.pow(2, currentW) && currentW < MAXW){
                        currentW++;
                    }
                }
                input = input.substring(t);            // Scan past s in input.

            }
            BinaryStdOut.write(R, currentW);
            BinaryStdOut.close();
            return;
        } else if(mode == 1){*/
        int oldRatioNum = 0;
        int oldRatioDen = 0;
        int newRatioNum = 0;
        int newRatioDen = 0;

        BinaryStdOut.write(1, C);
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), currentW);      // Print s's encoding.
            int t = s.length();

            if (t < input.length() && code < MAXL){    // Add s to symbol table.

                newRatioNum += t*8;
                newRatioDen += currentW;
                if(mode == 2 && calcRatio(oldRatioNum, oldRatioDen)/calcRatio(newRatioNum, newRatioDen) >= 1.1){
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;  // R is codeword for EOF
                    newRatioNum = oldRatioNum = 0;
                    newRatioDen = oldRatioDen = 0;
                    currentW = MINW;
                }
                
                st.put(input.substring(0, t + 1), code++);
                if(code > Math.pow(2, currentW) && currentW < MAXW){
                    currentW++;
                }
            } else if(mode == 1){ 
	            if(code >= MAXL){ // Reset the codebook
	                st = new TST<Integer>();
	                for (int i = 0; i < R; i++)
	                    st.put("" + (char) i, i);
	                code = R+1;  // R is codeword for EOF
	                currentW = MINW;
	            }
	        } else if(mode == 2){
                oldRatioNum = newRatioNum += t*8;
                oldRatioDen = newRatioDen += currentW;
	        }

            if (mode == 2 && t < input.length() && code < MAXL){    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
                if(code >= Math.pow(2, currentW) && currentW < MAXW){
                    currentW++;
                }
            } 

            input = input.substring(t);            // Scan past s in input.

        }
        BinaryStdOut.write(R, currentW);
        BinaryStdOut.close();
        return;
         
        /*else if(mode == 2){
            int oldRatioNum = 0;
            int oldRatioDen = 0;
            int newRatioNum = 0;
            int newRatioDen = 0;

            BinaryStdOut.write(2, C);
            String input = BinaryStdIn.readString();
            TST<Integer> st = new TST<Integer>();
            for (int i = 0; i < R; i++)
                st.put("" + (char) i, i);
            int code = R+1;  // R is codeword for EOF

            int writtenCodes = 0;

            while (input.length() > 0) {
                String s = st.longestPrefixOf(input);  // Find max prefix match s.
                BinaryStdOut.write(st.get(s), currentW);      // Print s's encoding.
                writtenCodes++;
                int t = s.length();
                

                // THIS IS WHERE RATIOS SHOULD BE CALCULATED
                if(t < input.length() && code >= MAXL){ // Reset the codebook
                    newRatioNum += t*8;
                    newRatioDen += currentW;
                    if(calcRatio(oldRatioNum, oldRatioDen)/calcRatio(newRatioNum, newRatioDen) >= 1.1){
                        st = new TST<Integer>();
                        for (int i = 0; i < R; i++)
                            st.put("" + (char) i, i);
                        code = R+1;  // R is codeword for EOF
                        newRatioNum = oldRatioNum = 0;
                        newRatioDen = oldRatioDen = 0;
                        currentW = MINW;
                    }
                } else if(code < MAXL){
                    oldRatioNum = newRatioNum += t*8;
                    oldRatioDen = newRatioDen += currentW;
                }

                if (t < input.length() && code < MAXL){    // Add s to symbol table.
                    st.put(input.substring(0, t + 1), code++);
                    if(code >= Math.pow(2, currentW) && currentW < MAXW){
                        currentW++;
                    }
                } 

                input = input.substring(t);            // Scan past s in input.

            }
            BinaryStdOut.write(R, currentW);
            BinaryStdOut.close();
            return;
        } 
        return;*/
    } 


    public static void expand() {
        int mode = BinaryStdIn.readInt(C);
        if(mode == 0){
            String[] st = new String[MAXL];
            int i; // next available codeword value

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(currentW);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];

            while (true) {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(currentW);
                if (codeword == R) break;
                String s = st[codeword];
                if (i == codeword) s = val + val.charAt(0);   // special case hack

                if (i < MAXL) st[i++] = val + s.charAt(0);

                if(i >= Math.pow(2, currentW) && currentW < MAXW){
                    //System.out.println("\n\nChanging width to " + (currentW+1) + ".  Current code: " + i + "\n\n");
                    currentW++;
                }



                //System.out.println("Codeword: " + codeword + "," + i + ":" + st[codeword] + ".");

                val = s;
            }
            BinaryStdOut.close();
        } else if(mode == 1){
            String[] st = new String[MAXL];
            int i; // next available codeword value

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(currentW);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];

            while (true) {
                BinaryStdOut.write(val);
                codeword = BinaryStdIn.readInt(currentW);
                if (codeword == R) break;
                String s = st[codeword];
                if (i == codeword) s = val + val.charAt(0);   // special case hack

                if (i < MAXL) st[i++] = val + s.charAt(0);
                else {
                    st = new String[MAXL];
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = ""; 
                    currentW = MINW;
                                           // (unused) lookahead for EOF
                }

                if(i >= Math.pow(2, currentW) && currentW < MAXW){
                    currentW++;
                }

                val = s;
            }
            BinaryStdOut.close();
            return;
        } else if(mode == 2){
            int oldRatioNum = 0;
            int oldRatioDen = 0;
            int newRatioNum = 0;
            int newRatioDen = 0;

            String[] st = new String[MAXL];
            int i; // next available codeword value

            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            //System.err.println("Currentw: " + currentW);

            int codeword = BinaryStdIn.readInt(currentW);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];
            int writtenCodes = 0;

            while (true) {
                BinaryStdOut.write(val);
                writtenCodes++;


                if(i < MAXL){
                    oldRatioNum = newRatioNum += val.length()*8;
                    oldRatioDen = newRatioDen += currentW;
                } else {
                    newRatioNum += val.length()*8;
                    newRatioDen += currentW;
                }
                if(i >= MAXL && oldRatioDen > 0 && newRatioDen > 0 
                    && calcRatio(oldRatioNum, oldRatioDen)/calcRatio(newRatioNum, newRatioDen) >= 1.1){
                    st = new String[MAXL];
                    i = 0;
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = ""; 
                    newRatioNum = oldRatioNum = 0;
                    newRatioDen = oldRatioDen = 0;
                    currentW = MINW;
                }

                if(i >= Math.pow(2, currentW)-1 && currentW < MAXW){
                    currentW++;
                }

                codeword = BinaryStdIn.readInt(currentW);
                if (codeword == R) {
                    break;
                }
                String s = st[codeword];
                if (i == codeword){
                    s = val + val.charAt(0);   // special case hack
                }

                if (i < MAXL) {
                    st[i++] = val + s.charAt(0);
                }

                val = s;

            }
            BinaryStdOut.close();
            return;

        }
        return;
    }

    public static double calcRatio(int n1, int d1){
        double r1 = ((double)n1)/((double)d1);
        return r1;
    }

    public static void main(String[] args) {
        if (args[0].equals("-")){
            //System.out.println("Mode: " + getMode(args[1]));
            compress(getMode(args[1]));
            //System.out.println("\n\n\nNumber of codes: " + codes);
        } 
        else if (args[0].equals("+")){
            expand();
        }
        else throw new IllegalArgumentException("Illegal command line argument");
    }

    /**
     *  @return 0 if mode is "Do Nothing"
     *  @return 1 if mode is "Reset"
     *  @return 2 if mode is "Monitor"
     *  @return -1 if mode is invalid
    **/
    public static int getMode(String m){
        if     (m.equals("n")) return 0;
        else if(m.equals("r")) return 1;
        else if(m.equals("m")) return 2;
        return -1;
    }

}
