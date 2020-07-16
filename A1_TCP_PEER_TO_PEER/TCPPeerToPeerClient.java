
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class TCPPeerToPeerClient {

    public static void main(String args[]) throws IOException {
//one string for sending the data one for receiving the data..
        String str1 = "", str2 = "";   //this strings will be added to the String pool

//creating client socket with ip address and port number
        Socket socket = new Socket("localhost", 6630);
//this is for sending the data from client to server
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
        //this is for receiving the data from the server
        DataInputStream din = new DataInputStream(socket.getInputStream());
//for taking input from user
        BufferedReader inBufferReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("******** TCP PeerToPeer CHATTING **********");
        System.out.println("-------CLIENT-------");
        System.out.println("Connection Established.....");
        try {
            while (true) {
                //client sending message to the server
                System.out.print("Client msg : ");
                str1 = inBufferReader.readLine();
                if (str1.equals("stop")) {
                    break;
                }
                dout.writeUTF(str1);

                //accepting message from server and display
                str2 = din.readUTF();
                if (str2.equals("stop")) {
                    break;
                }
                System.out.println("server msg : " + str2);
            }
        } catch (IOException ie) {
            System.out.println("Socket Close Error in client before finally");
        }
        dout.close();
        din.close();
        socket.close();
    }
}
