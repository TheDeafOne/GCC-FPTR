/** Author:  Brayden Stuchell, Keegan Woodburn
 * Course:  COMP 342 Data Communications and Networking
 * Date:    16 March 2022
 * Description: put a description here
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
                    case "GET" -> GET(data);
                    case "PUT" -> PUT();
                    case "LS" -> LS();
                    case "PWD" -> PWD();
                    case "QUIT" -> {
                        //TODO: say goodbye or something
                        return;
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void PUT()  throws IOException {
        String filename = inputStream.readUTF();
        int n = inputStream.readInt();
        byte[] fileBytes = Utils.readBytes(inputStream, n);
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(fileBytes);
        } catch (Exception e) {
            System.out.println("There was an error");
        }
        outputStream.writeUTF("ACK");
    }

    private static void GET(String filename) {

    }

    private static void PWD() {

    }

    private static void LS() {

    }


}
