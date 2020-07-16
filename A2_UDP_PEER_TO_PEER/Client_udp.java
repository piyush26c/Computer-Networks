import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

class Client_UDP
{
	public static void main(String args[]) throws Exception
	{
		DatagramSocket ds=new DatagramSocket(5300);		
		BufferedReader br=new BufferedReader(new InputStreamRe+ader(System.in));
		InetAddress in=InetAddress.getLocalHost();            //InetAddress Using in UDP
		String str="" ;	
		while(!str.equalsIgnoreCase("stop"))
		{
			//Sending
			byte buffer_send[]=new byte[100] ,   buffer_recive[]=new byte[100];
			System.out.println("To Server: ");
			str=br.readLine();
			buffer_send=str.getBytes();                     //<--
			DatagramPacket packet_send=new DatagramPacket(buffer_send,buffer_send.length,in,9500);
			ds.send(packet_send);					
			str = str.trim();
		        if(str.equalsIgnoreCase("stop"))
			break;
			//Receiving 
			DatagramPacket packet_recive=new DatagramPacket(buffer_recive,buffer_recive.length);
			ds.receive(packet_recive);      //Receives a datagram packet from this socket.
			buffer_recive=packet_recive.getData();  //returns data buffer
			String str2=new String(buffer_recive,StandardCharsets.UTF_8);
			str2 = str2.trim();
			System.out.println("Server says: " + str2);		
			if(str2.equalsIgnoreCase("stop"))
			break;		
		}		
		ds.close();
		br.close();
	}
}
