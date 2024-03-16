/** Author:  Brayden Stuchell, Keegan Woodburn
 * Course:  COMP 342 Data Communications and Networking
 * Date:    16 March 2022
 * Description: put a description here
 */

import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class FtpClient {
    public static final int PORT = 9001;

    public static final String HOST = "127.0.0.1";
    //public static final String HOST = "10.37.106.205";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String user_name = scanner.nextLine().trim();

        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Succeed: socket: " + socket);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String message = scanner.nextLine().trim(); // get user input (command)
                outputStream.writeUTF(message); // pass to server
                String response = inputStream.readUTF(); // TODO get response - possibly initial to determine what to do, then another response to actually do that
                System.out.println(response); // TODO: parse response
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
