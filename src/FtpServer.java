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

            while (true){
                String input = inputStream.readUTF(); // get client request
                commandRouter(input); // TODO: parse input - send to method
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void commandRouter(String input) {

    }
}
