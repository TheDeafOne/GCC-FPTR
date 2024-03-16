/** Author:  Brayden Stuchell, Keegan Woodburn
 * Course:  COMP 342 Data Communications and Networking
 * Date:    16 March 2022
 * Description: put a description here
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FtpServer {
    public static final int PORT = 9001;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String user_name = scanner.nextLine().trim();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening to " + PORT);
            Socket socket = serverSocket.accept();
            System.out.println("Connected: socket " + socket);

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            do {
                String message_in = inputStream.readUTF();
                System.out.println(message_in);

                System.out.println(user_name + ": ");
                String message_out = scanner.nextLine().trim();
                message_out = user_name + ": " + message_out;
                outputStream.writeUTF(message_out);

            }while(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
