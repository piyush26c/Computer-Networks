import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

class Server_UDP
{
	public static void main(String args[]) throws Exception
	{	
		DatagramSocket ds1=new DatagramSocket(9500);	//creating an datgram socket		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		InetAddress in=InetAddress.getLocalHost();	//to get ip address of the local host
                String str="" ;
			
		while(!str.equalsIgnoreCase("stop"))
		{
	                byte buffer_send[]=new byte[100];
	                byte buffer_recive[]=new byte[100]; //bufferes to store data    
		        //receiving
		        //datagram object for receivig the data
			DatagramPacket packet_recive=new DatagramPacket(buffer_recive,buffer_recive.length);
			ds1.receive(packet_recive);	//packet received
			buffer_recive=packet_recive.getData();	//move it into a buffer or returns the buffer
			//convert into string byte->string
			String str2=new String(buffer_recive,StandardCharsets.UTF_8);
			str2 = str2.trim();
			System.out.println("Client says: " + str2);		
			if(str2.equalsIgnoreCase("stop"))
			break;
			
			//Sending		
			System.out.println("To Client: ");
			str=br.readLine();			//taking from client
			buffer_send=str.getBytes();	//convert into byts and stores it into buffer
			//sending packet to receiver 
			DatagramPacket packet_send=new DatagramPacket(buffer_send,buffer_send.length,in,5300); 
			ds1.send(packet_send);			
			str = str.trim();
			if(str.equalsIgnoreCase("stop"))
			break;
		}		
		ds1.close(); //closing
		br.close();
	}
}
