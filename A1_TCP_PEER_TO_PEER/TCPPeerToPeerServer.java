
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPPeerToPeerServer {

    public static void main(String args[]) throws Exception {
        String str1 = "", str2 = "";//one for reading and one for writing data
        ServerSocket serverSocket = new ServerSocket(6630);//creating server socket with a port number(no quotes)
        Socket socket = serverSocket.accept();//accepting the client request on serverSocket named socket

        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());//acceptig through the client side socket
        DataInputStream din = new DataInputStream(socket.getInputStream());

        BufferedReader inBufferReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("******** TCP PeerToPeer CHATTING **********");
        System.out.println("------------SERVER--------------");
        System.out.println("Connection Established.....");
        try {

            while (true) {
                //server accepting message from client
                str1 = din.readUTF();
                if (str1.equals("stop")) {
                    break;
                }
                System.out.println("Client msg : " + str1);

                //server sending message to the client
                System.out.print("Server msg : ");
                str2 = inBufferReader.readLine();
                if (str2.equals("stop")) {
                    break;
                }
                dout.writeUTF(str2);
            }
        } catch (IOException ie) {
            System.out.println("Socket Close Error before finally in server");
        }
        dout.close();
        din.close();
        serverSocket.close();
        socket.close();
    }
}
