import java.net.*;
import java.io.*;
import java.nio.charset.*;

public class server{
	public static void main(String args[]) throws Exception
	{
		DatagramSocket s = new DatagramSocket(2206);
		InetAddress addr = InetAddress.getLocalHost();
		BufferedReader br = new BufferedReader(new InputStreamReader((System.in)));	
		
		String str1 = "";
		
		
		while(!str1.equalsIgnoreCase("Bye"))
		{
			byte[] buf1 = new byte[50];
			byte[] buf = new byte[50];
					
			DatagramPacket P2 = new DatagramPacket(buf1, buf1.length);
			s.receive(P2);
			buf1 = P2.getData();
			String str2 = new String(buf1, StandardCharsets.UTF_8);
			System.out.println("Client says : "+str2);
		
			str1 = br.readLine();
			buf = str1.getBytes();
		
			DatagramPacket P1 = new DatagramPacket(buf, buf.length, addr, 1106);
			s.send(P1);
		
		
		}
	
	
	}
	
}
