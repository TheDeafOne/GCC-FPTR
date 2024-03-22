/** Author:  Brayden Stuchell, Keegan Woodburn
 * Course:  COMP 342 Data Communications and Networking
 * Date:    16 March 2022
 * Description: A simple FTP server that allows clients to upload and download files, list files, and get the current working directory.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class FtpServer {
    public static final int PORT = 9001;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;
    public static  String currentDirectory = System.getProperty("user.dir") + "/server_folder/";

    public static void main(String[] args) {
        try {
            // create server socket and accept client connection
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Welcome to GCC FTP Service!");
            Socket socket = serverSocket.accept();
            System.out.println("Waiting for client commands...");

            // initialize input and output streams
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            while (true){
                String message = inputStream.readUTF();
                String[] input = message.split(" "); // get client request
                // split input into command and data, data is empty string if not provided
                String command = input[0].toUpperCase();
                String data = input.length > 1 ? input[1] : "";

                // route command to get server to send/receive proper data
                switch (command) {
                    case "GET" -> GET(data);
                    case "PUT" -> PUT();
                    case "LS" -> LS(currentDirectory);
                    case "PWD" -> PWD();
                    case "QUIT" -> {
                        outputStream.close();
                        inputStream.close();
                        System.out.println("Connection to the client terminated...");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error with the client connection. Quitting the program...");
        }
    }

    /**
     * Receives file from client and writes to server
     * @throws IOException - if there is an error reading bytes
     */
    private static void PUT()  throws IOException {
        String ack = inputStream.readUTF();
        if (!ack.equals(Utils.ACK)) {
            return; // clientside exception
        }
        // read file bytes and write to server
        String filename = inputStream.readUTF();
        int n = inputStream.readInt();
        byte[] fileBytes = Utils.readBytes(inputStream, n);
        try (FileOutputStream fos = new FileOutputStream(currentDirectory + filename)) {
            fos.write(fileBytes);
        } catch (Exception e) {
            System.out.println("There was an error");
        }
    }

    /**
     * Sends file to client
     * @param filename - name of file to send
     * @throws IOException - if there is an error reading bytes
     */
    private static void GET(String filename) throws Exception {
        try {
            // read file bytes and send to client
            Path path = Path.of(currentDirectory + filename);
            byte[] fileBytes = Files.readAllBytes(path);
            outputStream.writeUTF(Utils.ACK);
            outputStream.writeInt(fileBytes.length); // send file byte array length
            outputStream.write(fileBytes, 0, fileBytes.length); // send file bytes
        } catch (Exception e) {
            outputStream.writeUTF("There was an error reading the file or the file does not exist.");
        }

    }

    /**
     * Sends current directory to client
     * @throws IOException - if there is an error reading bytes
     */
    private static void PWD() throws IOException {
        // send current directory to client
        outputStream.writeUTF(currentDirectory);
    }

    /**
     * Sends list of files in current directory to client
     * @param dir - current directory
     * @throws IOException - if there is an error reading bytes
     */
    private static void LS(String dir) throws IOException {
        // send list of files in current directory to client
        List<String> filenames = Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory()) // only files
                .map(File::getName) // get file name
                .toList(); // convert to list

        // send number of files to client, then the corresponding file names
        outputStream.writeInt(filenames.size());
        for (String filename : filenames) {
            outputStream.writeUTF(filename);
        }

    }


}
