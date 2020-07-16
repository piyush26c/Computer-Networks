
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class NetServer {

    public static void main(String args[]) throws IOException {
        ServerSocket ss = new ServerSocket(4456);
        System.out.println("Server Listening......");

        while (true) {
            Socket s = ss.accept();
            System.out.println("Connection Established with Client");
            ServerThread st = new ServerThread(s);
            st.start();

        }
    }
}

class ServerThread extends Thread {

    String str1 = null;
    String str2 = null;

    BufferedReader br = null;
    DataOutputStream dout = null;
    DataInputStream din = null;

    Socket s = null;

    public ServerThread(Socket s) {
        this.s = s;  //invoke or initiate current class constructor
    }

    public void run() {
        try {

            br = new BufferedReader(new InputStreamReader(System.in));
            dout = new DataOutputStream(s.getOutputStream());
            din = new DataInputStream(s.getInputStream());

            while (true) {
                str1 = din.readUTF();
                System.out.println("Message from Client : " + str1);

                if (str1.equalsIgnoreCase("QUIT")) //if client says quit
                {
                    break;
                }

                dout.flush();
                System.out.println("Enter message to Client :");
                str2 = br.readLine();
                dout.writeUTF(str2);  //writing to that particular client only

                if (str2.equalsIgnoreCase("QUIT")) //if client says stop
                {
                    break;
                }
            }
        } catch (IOException ie) {
            System.out.println("Socket Close Error before finally in server");
        }

    }
}
