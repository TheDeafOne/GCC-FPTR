/** Author:  Brayden Stuchell, Keegan Woodburn
 * Course:  COMP 342 Data Communications and Networking
 * Date:    16 March 2022
 * Description: put a description here
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Stream;

public class FtpServer {
    public static final int PORT = 9001;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;
    public static  String currentDirectory = System.getProperty("user.dir") + "/server_folder/";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Welcome to GCC FTP Service!");
            Socket socket = serverSocket.accept();
            System.out.println("Waiting for client commands...");

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            while (true){
                String message = inputStream.readUTF();
                String[] input = message.split(" "); // get client request
                String command = input[0].toUpperCase();
                String data = input.length > 1 ? input[1] : "";
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

    private static void PUT()  throws IOException {
        String ack = inputStream.readUTF();
        if (!ack.equals(Utils.ACK)) {
            return; // clientside exception
        }
        String filename = inputStream.readUTF();
        int n = inputStream.readInt();
        byte[] fileBytes = Utils.readBytes(inputStream, n);
        try (FileOutputStream fos = new FileOutputStream(currentDirectory + filename)) {
            fos.write(fileBytes);
        } catch (Exception e) {
            System.out.println("There was an error");
        }
    }

    private static void GET(String filename) throws Exception {
        try {
            Path path = Path.of(currentDirectory + filename);
            byte[] fileBytes = Files.readAllBytes(path);
            outputStream.writeUTF(Utils.ACK);
            outputStream.writeInt(fileBytes.length); // send file byte array length
            outputStream.write(fileBytes, 0, fileBytes.length); // send file bytes
        } catch (Exception e) {
            outputStream.writeUTF("There was an error reading the file or the file does not exist.");
        }

    }

    private static void PWD() throws IOException {
        outputStream.writeUTF(System.getProperty("user.dir"));
    }

    private static void LS(String dir) throws IOException {
        List<String> filenames = Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .toList();
        outputStream.writeInt(filenames.size());
        for (String filename : filenames) {
            outputStream.writeUTF(filename);
        }

    }


}
