package connexion;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class GetProduitsThread implements Runnable {
	
	public Socket socketSOAP;
	public Socket socketREST;
	private String OUTPUT;
	private static final int portNumberREST = 8080;
	
	
	private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
		    "Content-Type: application/json\r\n" + 
		    "Content-Length: ";
		private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";
	
	
	
	public GetProduitsThread(Socket socketSOAP) {
		// TODO Auto-generated constructor stub
		this.socketSOAP = socketSOAP;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		socketREST();
		socketSOAP();
		try {
			socketSOAP.close();
			socketREST.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void socketSOAP() {
		// TODO Auto-generated method stub
		BufferedWriter out;
		try {
			out = new BufferedWriter(
				    new OutputStreamWriter(
				        new BufferedOutputStream(socketSOAP.getOutputStream()), "UTF-8"));
			out.write(OUTPUT_HEADERS + OUTPUT.length() + OUTPUT_END_OF_HEADERS + OUTPUT);
			out.flush();
			System.out.println("Envoie de la réponse contenant le json Produits/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void socketREST() {
		// TODO Auto-generated method stub
		try {
			socketREST = new Socket(InetAddress.getLocalHost(), portNumberREST);
			String path = "/Pfa/Produits";
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socketREST.getOutputStream(), "UTF8"));
            wr.write("GET "+path+" HTTP/1.0\r\n");
            wr.write("\r\n");
            wr.flush();
            System.out.println("Requete GET Produits/ envoyé au serveur REST");
			
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(socketREST.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while ((line = rd.readLine()) != null) {
            	if (i>=5) sb.append(line); // on ecrit pas le header de la requette HTTP, on veut juste ecrire le body qui est le wadl.xml
            	i++;
            	//sb.append(line);
                //System.out.println(line);
                
            }
            
            OUTPUT = sb.toString();
            System.out.println("Response : "+OUTPUT);
            //out.println("\r\n");
            //out.close();
            wr.close();
            rd.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
