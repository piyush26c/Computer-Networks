
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String args[]) throws IOException {
        System.out.println("\n_________________C L I E N T  S I D E________________\n");

        int i, n = 0;   //IMPORTANT

        Socket s = new Socket("localhost", 5000);

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        int arrlen = din.read();          //[1.C]
        int a[] = new int[arrlen];

        System.out.println("\nNo. of frames to be received are : " + arrlen);
        System.out.println("Receiving frames ....");

        System.out.println("Receiving Frames ....... ");
        for (i = 0; i < arrlen; i++) {
            a[i] = din.read();        //[2.C]
            System.out.print(a[i]);
            System.out.print(" ");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {
            }

        }
        System.out.println();

        a[5] = -1;    //adding error

        System.out.println("Received Frame (after adding error):");
        for (i = 0; i < arrlen; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();

        System.out.println("Finding the error position :");
        //finding the index position of error by scanning entire array
        for (i = 0; i < arrlen; i++) {
            if (a[i] == -1) {
                System.out.println("\nSending NAK for frame no." + (i + 1));//sending request to resend the frame
                n = i;		//n is used further
                dout.write(i);	//sending that index containing error  [3.C]
                dout.flush();
            }
        }

        a[n] = din.read();	//receiving the correct packet at that error index     [4.S]
        System.out.println("Retransmitted packet received is : " + a[n]);

        System.out.println("Received Frame (after receiving retransmitted packet):");
        for (i = 0; i < arrlen; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
        din.close();
        dout.close();
    }
}
