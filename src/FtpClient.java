/** Author:  Brayden Stuchell, Keegan Woodburn
 * Course:  COMP 342 Data Communications and Networking
 * Date:    16 March 2022
 * Description: A simple FTP client that allows users to upload and download files, list files, and get the current working directory.
 */

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FtpClient {
    public static final int PORT = 9001;
    public static final String HOST = "127.0.0.1";
    public static  String currentDirectory = System.getProperty("user.dir") + "/client_folder/";
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        try {
            // create socket and connect to server
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Welcome to GCC FTP client!");

            // initialize input and output streams
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                // get user command
                System.out.print("command: ");
                String message = scnr.nextLine().trim();
                String[] input = message.split(" ");
                String command = input[0].toUpperCase();
                String data = input.length > 1 ? input[1] : "";
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
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("There was an error with the server connection. Quitting the program...");
        }
    }

    /**
     * Sends file to server
     * @param filename - name of file to send
     * @throws IOException - if there is an error reading bytes
     */
    private static void PUT(String filename) throws IOException {
        try {
            // read file bytes and send to server
            Path path = Path.of(currentDirectory + filename);
            byte[] fileBytes = Files.readAllBytes(path);
            outputStream.writeUTF(Utils.ACK);
            outputStream.writeUTF(filename);
            outputStream.writeInt(fileBytes.length); // send file byte array length
            outputStream.write(fileBytes, 0, fileBytes.length); // send file bytes
            System.out.println("Uploaded " + filename + " successfully");
        } catch (Exception e) {
            // send client exception to server
            outputStream.writeUTF("Client Exception");
            System.out.println("There was an error reading the file or the file does not exist.");
        }
    }

    /**
     * Receives file from server and writes to client
     * @param data - name of file to get
     * @throws IOException - if there is an error reading bytes
     */
    private static void GET(String data) throws IOException {
        // send file name to server that we want to get
        String ack = inputStream.readUTF();
        if (!ack.equals(Utils.ACK)) {
            System.out.println("The file " + data + " does not exist.");
            return;
        }
        // get file bytes and write to file
        int n = inputStream.readInt();
        byte[] fileBytes = Utils.readBytes(inputStream, n);
        try (FileOutputStream fos = new FileOutputStream(currentDirectory + data)) {
            fos.write(fileBytes);
        } catch (Exception e) {
            System.out.println("There was an error");
        }
        System.out.println("Downloaded " + data + " correctly");
    }

    /**
     * Sends PWD command to server and prints out the received current directory
     * @throws IOException - if there is an error reading bytes
     */
    private static void PWD() throws IOException {
        // get current directory
        String response = inputStream.readUTF();
        System.out.println(response);
    }

    /**
     * Sends LS command to server and prints out the received list of files in the current directory
     * @throws IOException - if there is an error reading bytes
     */
    private static void LS() throws IOException {
        // get number of files, then read and print out each file name
        int n = inputStream.readInt();
        for (int i = 0; i < n; i++) {
            System.out.println(inputStream.readUTF());
        }
    }

}
