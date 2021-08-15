import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

public class SolutionCryptonitePartII {

    /*
     * Reads all bytes from a file pointed to by path into the given buffer. Make sure your buffer size is big enough!
     * 
     * Returns true on success, or false if an exception was thrown.
     */ 
    private static boolean readBytes(String path, byte[] buffer) {
        try{
            InputStream input = new FileInputStream(path);
            input.read(buffer);
            input.close();
        }
        catch (FileNotFoundException e) {
            System.out.printf("The path you specified was not found: %s/%s\n", 
                                            System.getProperty("user.dir"), path);
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Takes in one argument n, which denotes which bit of the keys we want to crack
    public static void main(String[] args) {
        final int BUFFER_SIZE = 42; // This will suffice as each ciphertext is 42 bytes

        // Read in all the ciphertexts
        byte[] c1 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c1_plain", c1)) return;
        byte[] c2 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c2_plain", c2)) return;
        byte[] c3 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c3_plain", c3)) return;
        byte[] c4 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c4_plain", c4)) return;
        byte[] c5 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c5_plain", c5)) return;
        byte[] c6 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c6_plain", c6)) return;
        byte[] c7 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c7_plain", c7)) return;
        byte[] c8 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c8_plain", c8)) return; 
        byte[] c9 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c9_plain", c9)) return; 
        byte[] c10 = new byte[BUFFER_SIZE];
        if (!readBytes("ciphertexts/p2_c10_plain", c10)) return; 

        int num_ciphertexts = 10; // The number of ciphertexts we want to make use of
        byte[][] all = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10}; // Stores all the ciphertexts

        // The actual keys, for reference:
        // s1 = 0x54f3558fd1f0243684906e0dc1f6fe4c03574c0b99da62dff12d4d19f752172a74ce65f3c7994865f1db
        // s2 = 0x26eca07c084b45075c8428a678bf51079aefddc3b8bf5201c6208c391aefb74aa66e44d87ffd9261f9a6

        // Will store our guesses for what s1 and s2 are. Starting from the least-significant (last) byte, 
        // we will fill in the entries one-by-one. Even though it's 42 iterations, each one should be very fast
        byte[] s1 = {(byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0 };
        byte[] s2 = {(byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0,
                    (byte) 0x0, (byte) 0x0 };

        // All our arithmetic is done with a modulus equal to 2^(BUFFER_SIZE). This is equivalent to 
        // a one followed by BUFFER_SIZE zeroes.
        byte[] modulus = new byte[BUFFER_SIZE + 1];
        modulus[0] = 1;
        BigInteger mod = new BigInteger(1, modulus);

        // The remaining code will brute force through all 256 * 256 = 65536 possible choices of the 
        // nth byte of s1 and s2, and print the decoding of the nth byte of each of the ciphertexts only if
        // the nth byte bit of each ciphertext is an allowed character. Note that you have to have ascertained
        // bits n + 1 through BUFFER_SIZE - 1 for this code to correctly with the given n. Thus, you should
        // first run this code for n = BUFFER_SIZE - 1, ascertain the last byte of s1 and s2, then run it 
        // for n = BUFFER_SIZE - 2, ascertain the 2nd to last byte of s1 and s2, and so on.
        int n = Integer.parseInt(args[0]);
        for (int i = 0; i < 256; i++) {
            s1[n] = (byte) i; // Guess nth byte of s1

            for (int j = 0; j < 256; j++) {
                s2[n] = (byte) j; // Guess nth byte of s2

                BigInteger s1_int = new BigInteger(1, s1);
                BigInteger s2_int = new BigInteger(1, s2);

                byte[] temp = new byte[10];

                boolean accept = true; // A flag that will check if the nth byte of the decoding is allowed

                for (int k = 1; k <= num_ciphertexts; k++) {
                    // Calculate (s1 + k * s2) mod 2^(BUFFER_SIZE)
                    BigInteger guess = s1_int.add(s2_int.multiply(BigInteger.valueOf(k))).remainder(mod);

                    // Put our result back into a byte array so we can retrieve the nth byte
                    byte[] guess_bytes = guess.toByteArray();
                    int len = guess_bytes.length;

                    // The only caveat is that our number could have many leading zeroes, which the BigInteger
                    // library ignores. For example, the number 00000000 10101010 will be stored in one byte
                    // instead of two. To extract the nth byte in fixed-width precision, we do the following:
                    int index = len - (BUFFER_SIZE - n);
                    byte plain = index >= 0 ? (byte) guess_bytes[index] : 0;

                    // XOR with the kth ciphertext to decode the nth byte, and check if it is an allowed character
                    plain = (byte) (plain ^ all[k - 1][n]); 
                    temp[k - 1] = plain;
                    if ( !(plain == 32 || plain == 33 || plain == 39 || plain == 44 || plain == 46 || 
                        plain == 63 || (plain >= 97 && plain <= 122)) ) {
                        accept = false;
                    }
                }

                // In most cases, we are actually only left with one choice of the nth byte of s1 and s2

                if (accept) {
                    System.out.printf("s1[n] = 0x%x, s2[n] = 0x%x\n", i, j);

                    System.out.printf("The Last %d characters of the resulting Decoded Plaintexts:\n", BUFFER_SIZE - n);
                    for (int k = 1; k <= num_ciphertexts; k++) {
                        System.out.print((char) temp[k - 1]);

                        // For convenience, print the rest of what we've decoded so far so we can see
                        // if this guess of s1[n] and s2[n] makes sense
                        BigInteger guess = s1_int.add(s2_int.multiply(BigInteger.valueOf(k))).remainder(mod);
                        byte[] guess_bytes = guess.toByteArray();
                        int len = guess_bytes.length;

                        for (int l = n + 1; l < BUFFER_SIZE; l++) {
                            int index = len - (BUFFER_SIZE - l);
                            byte keyByte = index >= 0 ? (byte) guess_bytes[index] : 0;
                            System.out.print((char) (keyByte ^ all[k - 1][l]));
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
        }
    }
}
