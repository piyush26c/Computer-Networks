
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

class client {

    public static void main(String args[]) throws Exception {
        Socket S = new Socket("localhost", 6969);
        BufferedInputStream in = new BufferedInputStream(S.getInputStream());
        DataOutputStream out = new DataOutputStream(S.getOutputStream());
        Scanner SC = new Scanner(System.in);
        int c, choice, i, j, check;
        System.out.println("Enter Number of Frames for Server to Send");
        c = SC.nextInt();
        out.write(c);
        System.out.println("Enter Choice 0:Errorless 1:With Error");
        choice = SC.nextInt();
        out.write(choice);
        if (choice == 0) {
            for (j = 0; j < c; j++) {
                i = in.read();
                System.out.println("Received Frame : " + i);
                System.out.println("Sending Acknowledgement for Frame : " + i);
                out.write(i);
            }
        } else {
            check = 0;
            for (j = 0; j < c; j++) {
                i = in.read();
                if (i == check) {
                    System.out.println("Received Frame " + i);
                    System.out.println("Sending Acknowledgement for Frame " + i);
                    out.write(i);
                    check++;
                } else {
                    j--;
                    for (int k = i; k < c; k++) {
                        System.out.println("Frame " + k + " Discarded");
                    }
                    out.write(-1);
                }
            }
        }
    }
}
