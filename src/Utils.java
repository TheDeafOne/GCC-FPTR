import java.io.*;

public class Utils {
    public static final String ACK = "ACK";

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
