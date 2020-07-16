
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String args[]) throws IOException {
        int i;

        System.out.println("\n_________________S E R V E R  S I D E_________________\n");

        int a[] = {10, 20, 30, 40, 50, 60, 70, 80, 90};

        ServerSocket ss = new ServerSocket(5000);

        System.out.println("\nWaiting for connection... \n");
        Socket s = ss.accept();

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        System.out.println("The number of frames to be send is :" + a.length);
        System.out.println("-----------------------------------------------------\n");
        dout.write(a.length);           //[1.S]
        dout.flush();

        System.out.println("Sending frames ....");
        for (i = 0; i < a.length; i++) {
            dout.write(a[i]);       //[2.S]
            dout.flush();
        }
        System.out.println("Frames sent successfully");

        System.out.println("Retransmitting the lost frame");
        i = din.read();	//receiving the array index at which error is present ie., 'i'  [3.S]
        dout.write(a[i]);	//sending a[i]    [4.S]
        dout.flush();

        din.close();
        dout.close();
    }
}
