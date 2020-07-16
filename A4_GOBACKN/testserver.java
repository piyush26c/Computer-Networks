
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class testserver {

    public static void main(String args[]) throws IOException {
        System.out.println("...........Server..........");
        System.out.println("Waiting for connection....");
        ServerSocket ss = new ServerSocket(1500);

        Socket s = ss.accept();

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        System.out.println("Received request for sending frames");
        int sf = din.read();  //client sended number of frames      [1]

        boolean f[] = new boolean[sf];  //boolean array of size number of frames

        int pc = din.read();    //receiving choice with or without error   [2]
        System.out.println("Sending....");

        if (pc == 0) {
            for (int i = 0; i < sf; ++i) {
                System.out.println("sending frame number " + i);
                dout.write(i);           //[3]
                dout.flush();
                System.out.println("Waiting for acknowledgement");

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                }

                int a = din.read();     //[4]
                System.out.println("received acknowledgement for frame " + i + " as " + a);
            }
            dout.flush();
        } else {
            for (int i = 0; i < sf; ++i) {
                if (i == 2) {
                    System.out.println("sending frame no " + i);
                } else {
                    System.out.println("sending frame no " + i);
                    dout.write(i);      //[5]
                    dout.flush();
                    System.out.println("Waiting for acknowledgement ");

                    try {
                        Thread.sleep(7000);
                    } catch (Exception e) {
                    }

                    int a = din.read();         //[6]

                    if (a != 255) //checking whether it is -1
                    {
                        System.out.println("received ack for frame no: " + i + " as " + a);
                        f[i] = true;
                    }
                }// end of inner else
            }// end of for

            // check which frames have not been ack
            for (int a = 0; a < sf; ++a) {
                if (f[a] == false) {
                    System.out.println("Resending frame " + a);
                    dout.write(a);              //[5]
                    dout.flush();
                    System.out.println("Waiting for ack ");

                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                    }

                    int b = din.read();
                    System.out.println("received ack for frame no: " + a + " as " + b);
                    f[a] = true;
                }
            }
            dout.flush();
        }// end of else which is for error

        din.close();
        dout.close();
        s.close();
        ss.close();
        System.out.println("Quiting");

    }// end main method
}// end main class

