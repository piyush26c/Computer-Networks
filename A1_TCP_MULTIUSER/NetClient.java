
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class NetClient {

    public static void main(String args[]) throws IOException {
        // InetAddress address=InetAddress.getLocalHost(); //this class represents an IP address using the methods 			getLocalHost,getByName,or getAllByName to create a new InetAddress instance

        String str1 = null;
        String str2 = null;

        Socket s = new Socket("localhost", 4456);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        DataInputStream din = new DataInputStream(s.getInputStream());

        System.out.println("******** TCP MULTIUSER CHATTING **********");
        System.out.println("Initially Client will send message");
        try {
            while (true) {
                System.out.println("Enter Message to server (Enter QUIT to end) :");
                str1 = br.readLine();
                dout.writeUTF(str1);
                dout.flush();

                if (str1.equalsIgnoreCase("QUIT")) {
                    break;
                }

                str2 = din.readUTF();
                System.out.println("Message from Server : " + str2);

                if (str2.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
            System.out.println("Chat Ended");
        } catch (IOException ie) {
            System.out.println("Socket Close Error in client before finally");
        }

    }
}
