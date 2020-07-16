
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

class Server_UDP {

    public static void main(String args[]) throws Exception {
        DatagramSocket ds1 = new DatagramSocket(9500);	//creating an datgram socket
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        InetAddress in = InetAddress.getLocalHost();	//to get ip address of the local host
        String str = "";

        while (!str.equalsIgnoreCase("stop")) {
            byte buffer_send[] = new byte[100], buffer_recive[] = new byte[100]; //bufferes to store data
            //receiving
            //datagram object for receivig the data
            DatagramPacket packet_recive = new DatagramPacket(buffer_recive, buffer_recive.length);
            ds1.receive(packet_recive);	//packet received
            buffer_recive = packet_recive.getData();	//move it into a buffer
            //convert into string byte->string
            String str2 = new String(buffer_recive, StandardCharsets.UTF_8);
            System.out.println("Client says: " + str2);

            int a = packet_recive.getPort();    //taking the port number
            if (str2.equalsIgnoreCase("stop")) {
                System.exit(0);
            }
            //Sending
            System.out.println("To Client: ");
            str = br.readLine();		//taking from client
            buffer_send = str.getBytes();	//convert into byts and stores it into buffer
            //sending packet to receiver
            DatagramPacket packet_send = new DatagramPacket(buffer_send, buffer_send.length, in, a);
            ds1.send(packet_send);
            if (str.equalsIgnoreCase("stop")) {
                System.exit(0);
            }
        }
        ds1.close(); //closing
        br.close();
    }
}
