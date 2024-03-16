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
    static DataInputStream inputStream;
    static DataOutputStream outputStream;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening to " + PORT);
            Socket socket = serverSocket.accept();
            System.out.println("Connected: socket " + socket);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            while (true){
                String[] input = inputStream.readUTF().split(" "); // get client request
                String command = input[0], data = input.length > 1 ? input[1] : "";
                switch (command) {
                    case "GET":
                        GET(data);
                        break;
                    case "PUT":
                        PUT();
                        break;
                    case "LS":
                        LS();
                        break;
                    case "PWD":
                        PWD();
                        break;
                    case "QUIT":
                        //TODO: say goodbye or something
                        return;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void PUT() {

    }

    private static void GET(String data) {

    }

    private static void PWD() {

    }

    private static void LS() {

    }


}
