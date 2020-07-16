
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

class Client_UDP {

    public static void main(String args[]) throws Exception {
        DatagramSocket ds = new DatagramSocket(5301);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        InetAddress in = InetAddress.getLocalHost();
        String str = "";

        while (!str.equalsIgnoreCase("stop")) {
            byte buffer_send[] = new byte[100], buffer_recive[] = new byte[100];
            //Sending		
            System.out.println("To Server: ");
            str = br.readLine();
            buffer_send = str.getBytes();
            DatagramPacket packet_send = new DatagramPacket(buffer_send, buffer_send.length, in, 9500);
            ds.send(packet_send);
            str = str.trim();
            if (str.equalsIgnoreCase("stop")) {
                System.exit(0);//break;	
            }
            //Receiving 
            DatagramPacket packet_recive = new DatagramPacket(buffer_recive, buffer_recive.length);
            ds.receive(packet_recive);
            buffer_recive = packet_recive.getData();
            String str2 = new String(buffer_recive, StandardCharsets.UTF_8);
            System.out.println("Server says: " + str2);
            str2 = str2.trim();
            if (str2.equalsIgnoreCase("stop")) {
                System.exit(0);
            }
        }
        ds.close();
        br.close();
    }
}
