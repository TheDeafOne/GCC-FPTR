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

        System.out.println("Welcome to GCC Chatting Room");
        System.out.println("Please enter your name");
        Scanner scanner = new Scanner(System.in);
        String user_name = scanner.nextLine().trim();

        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Succeed: socket: " + socket);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            while (true) {
                System.out.println(user_name + ": ");
                String message = scanner.nextLine().trim();
                message = user_name + ": " + message;
                outputStream.writeUTF(message);
                String response = inputStream.readUTF();
                System.out.println(response);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
