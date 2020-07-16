
import java.net.InetAddress;
import java.net.UnknownHostException;

// Print out DNS Record for an Internet Address
//import java.net.*;
public class DNS {

    public static void main(String args[]) {
        // explain what program does and how to use it
        if (args.length != 1) {
            System.err.println("Enter command line argument as a a Internet Address or URL");
            System.exit(-1);
        }
        try {
            InetAddress inetAddress;
            // if first character is a digit then assume is an ip address
            if (Character.isDigit(args[0].charAt(0))) {
                byte[] b = new byte[4];         //creating byte array
                String[] bytes = args[0].split("[.]");
                for (int i = 0; i < bytes.length; i++) {	// convert address from string representation to byte array
                    b[i] = new Integer(bytes[i]).byteValue();           //[1]
                }

                inetAddress = InetAddress.getByAddress(b); // get Internet Address of this host address [2]
            } else {
                inetAddress = InetAddress.getByName(args[0]); // get host name of this Internet Address  [3]
            }

            System.out.println(inetAddress.getHostName() + "/" + inetAddress.getHostAddress());   //[4]
            // show the Internet Address as name/address

        } catch (UnknownHostException exception) {
            System.err.println("ERROR: No Internet Address for '" + args[0] + "'");
        }

    }
}
