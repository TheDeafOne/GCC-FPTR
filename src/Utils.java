/** Author:  Brayden Stuchell, Keegan Woodburn
 * Course:  COMP 342 Data Communications and Networking
 * Date:    16 March 2022
 * Description: Utility class for FTP server and client.
 */

import java.io.*;

public class Utils {
    public static final String ACK = "ACK";

    /**
     * Send bytes to output stream
     * @param in - input stream
     * @param n - number of bytes to read
     * @return - byte array
     * @throws IOException - if there is an error reading bytes
     */
    public static byte[] readBytes(DataInputStream in, int n) throws IOException {
        byte[] buffer = new byte[n];
        int numberOfBytesRead = 0;
        // continue reading bytes till byte array is filled
        while (numberOfBytesRead != n) {
            int readBytes = in.read(buffer, numberOfBytesRead, n-numberOfBytesRead);
            numberOfBytesRead += readBytes;
        }
        return buffer;
    }
}
