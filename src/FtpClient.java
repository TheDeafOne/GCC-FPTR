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
        Scanner scnr = new Scanner(System.in);
        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Welcome to GCC FTP client!");

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                // get user command
                System.out.print("command: ");
                String message = scnr.nextLine().trim();
                String[] input = message.split(" ");
                String command = input[0], data = input.length > 2 ? input[1] : "";

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

    private static void LS() throws IOException {
        outputStream.writeUTF("LS");
        int n = inputStream.readInt();
        for (int i = 0; i < n; i++) {
            System.out.println(inputStream.readUTF());
        }
    }

}
