
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class server {

    public static void main(String args[]) throws Exception {
        ServerSocket SS = new ServerSocket(6969);
        Socket S = SS.accept();
        BufferedInputStream in = new BufferedInputStream(S.getInputStream());
        DataOutputStream out = new DataOutputStream(S.getOutputStream());
        Scanner SC = new Scanner(System.in);
        int p, pc, i, a;
        p = in.read();
        boolean f[] = new boolean[p];
        pc = in.read();
        if (pc == 0) {
            for (i = 0; i < p; i++) {
                System.out.println("Sending Frame : " + i);
                out.write(i);
                System.out.println("Waiting For Acknowledgement");
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                a = in.read();
                System.out.println("Received Acknowledgement " + a + " For Frame " + i);
            }
        } else {
            for (i = 0; i < p; i++) {
                if (i == 2) {
                    System.out.println("Sending Frame " + i);
                } else {
                    if (i < 2) {
                        System.out.println("Sending Frame " + i);
                    }
                    if (i > 2) {
                        for (int k = i; k < p; k++) {
                            System.out.println("Sending Frame " + k);
                        }
                    }
                    out.write(i);
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                    a = in.read();
                    if (a != 255) {
                        System.out.println("Received Positive Acknowledgement " + a + " for Frame " + i);
                        f[a] = true;
                    } else {
                        for (i = 0; i < p; i++) {
                            if (f[i] == false) {
                                if (i == 2) {
                                    System.out.println("Received Negative Acknowledgement for Frame " + i);
                                }
                                System.out.println("Retransmitting Frame " + i);
                                out.write(i);
                                System.out.println("Waiting Acknowledgement");
                                i = in.read();
                                System.out.println("Received Acknowledgement for Frame " + i);
                                f[i] = true;
                            }
                        }
                    }

                }
            }
        }

    }
}
