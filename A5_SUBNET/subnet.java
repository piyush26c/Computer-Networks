
import java.util.Scanner;

//204.17.5.0
public class subnet {

    static int cnt = 0;

    static String append(String s) {
        int len = s.length();
        for (int i = 0; i < 8 - len; i++) {
            s = '0' + s;   //append number of '0' before bits.
        }
        return s;
    }

    static int getNumber(int n) {
        double i = 1;
        double o = 2;
        int number;

        while (true) {
            double ii = Math.pow(o, i);
            cnt++;
            i++;
            if ((int) ii >= n) {
                number = (int) ii;
                break;
            }
        }
        return number;
    }

    public static void main(String[] args) {
        System.out.println("Enter the IP address :");
        Scanner sc = new Scanner(System.in);
        String ip = sc.next();

        String[] bip = new String[4];
        int[] intBIp = new int[4];
        bip = ip.split("\\.");   //split ip in "." format and store as a string array

        System.out.println("Following are the Integers extracted from IP address : ");
        for (int i = 0; i < 4; i++) {
            intBIp[i] = Integer.parseInt((bip[i]));  //convert string ip to integer ip
            System.out.println(intBIp[i]);         //display ip in integer format
        }

        String[] binaryIp = new String[4];

        for (int i = 0; i < 4; i++) {
            System.out.println("\n");
            binaryIp[i] = Integer.toBinaryString(intBIp[i]);
            System.out.println("binary format       : " + binaryIp[i]);  //display ip in binary format
            binaryIp[i] = append(binaryIp[i]);  //call to append function
            System.out.println("8-bit binary format : " + binaryIp[i]);  //display ip in 8 bits format
        }

        System.out.println("Enter the number of subnets :");
        int numberOfSubnets = sc.nextInt();
        int num = (getNumber(numberOfSubnets));  //call to getNumber function
        System.out.println(8 - cnt);

        //decide number of host per subnet
        int jj = 8 - cnt;
        double a = 2;
        double b = (double) (jj);
        double ii = Math.pow(a, b);
        int m = ((int) ii);

        for (int k = 0; k < numberOfSubnets; k++) {
            //int [] aa=new int[8];
            System.out.print("Subnet no. " + (k + 1));
            System.out.print(" : " + intBIp[0] + "." + intBIp[1] + "." + intBIp[2] + "." + intBIp[3]);
            intBIp[3] = intBIp[3] + m;
            System.out.print("      Subnet no. " + (k + 1));
            System.out.print(" : " + intBIp[0] + "." + intBIp[1] + "." + intBIp[2] + "." + intBIp[3]);
            System.out.println();
        }
    }
}
