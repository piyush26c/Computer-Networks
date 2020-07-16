
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class testclient {

    public static void main(String args[]) throws IOException {

        Socket s = new Socket("localhost", 1500);

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        Scanner scr = new Scanner(System.in);// this will be used to accept i/p from console

        System.out.println(".......Client........");
        System.out.println("Connected to server");
        System.out.println("Enter the number of frames to be requested to the server");
        int sf = scr.nextInt();

        dout.write(sf);           //[1]
        dout.flush();

        System.out.println("Enter the type of trans. Error=1 ; No Error=0");
        int choice = scr.nextInt();
        dout.write(choice);      //[2]

        int check = 0;
        int i = 0;
        int j = 0;

        if (choice == 0) {
            for (j = 0; j < sf; ++j) {
                i = din.read();            //[3]
                System.out.println("received frame no: " + i);  //aalela i parat pathavne
                System.out.println("Sending acknowledgement for frame no: " + i);
                dout.write(i);          //[4]
                dout.flush();
            }
            dout.flush();
        } else {
            for (j = 0; j < sf; ++j) {
                i = din.read();           //[5]

                if (i == check) {
                    System.out.println("received frame no: " + i);
                    System.out.println("Sending acknowledgement for frame no: " + i);
                    dout.write(i);  //[6]
                    ++check;
                } else {
                    --j;            //no waste in frames
                    System.out.println("Discarded frame no: " + i);
                    System.out.println("Sending NEGATIVE ack");
                    dout.write(-1); //[7]
                }
                dout.flush();
            }
        }//end of else for error

        din.close();
        dout.close();
        System.out.println("Quiting");

    }// end of main method
}// end of main class
