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
    private static final String clientDirectory = "./client_folder/";
    private static final String ack = "ACK";

    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Succeed: socket: " + socket);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            while (true) {
                // get user command
                String message = scanner.nextLine().trim();
                String[] input = inputStream.readUTF().split(" ");
                String command = input[0], data = input.length > 1 ? input[1] : "";

                // pass to server and parse ack
                outputStream.writeUTF(message);

                // route command to get client to send proper data
                switch (command) {
                    case "GET" -> GET(data);
                    case "PUT" -> PUT(data);
                    case "LS" -> LS();
                    case "PWD" -> PWD();
                    case "QUIT" -> {
                        inputStream.close();
                        outputStream.close();
                        return;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void PUT(String filename) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Path.of(clientDirectory + filename));
        outputStream.writeUTF(filename); // send filename
        outputStream.writeInt(fileBytes.length); // send file byte array length
        outputStream.write(fileBytes, 0, fileBytes.length); // send file bytes
    }

    private static void GET(String data) {

    }

    private static void PWD() {

    }

    private static void LS() {

    }

}
