package connexion;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import MappingObjObj.Facade;

public class ConnexionThread implements Runnable {

	public Socket socketSOAP;
	public Socket socketREST;
	private static final int portNumberREST = 8080;
	
	//private static final String OUTPUT = "<html><head><title>Example</title></head><body><p>Worked!!!</p></body></html>";
	private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
	    "Content-Type: application/xml\r\n" + 
	    "Content-Length: ";
	private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";
	
	public ConnexionThread(Socket socketSOAP) {
		// TODO Auto-generated constructor stub
		this.socketSOAP = socketSOAP;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		socketREST();
		Facade.convertAndPersistWADL2WSDL("wadl.xml", "wsdl.xml");
		// à completer par l'autre groupe 
		// En ce moment d'éxecution nous avons un fichier wsdl correspondant au wadl du serveur REST.
		// Ce fichier est crée sur le dossier du middlware "wsdl.xml"
		// il suffit d'envoyer ce fichier comme réponse à travers le socket "socketSOAP", peut etre lire le fichier en un String et l'envoyer
		socketSOAP();// à implementer 
		try {
			socketREST.close();
			socketSOAP.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void socketREST() {
		try {
			socketREST = new Socket(InetAddress.getLocalHost(), portNumberREST);
			String path = "/Pfa/WADL";
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socketREST.getOutputStream(), "UTF8"));
            wr.write("GET "+path+" HTTP/1.0\r\n");
            wr.write("\r\n");
            wr.flush();
            System.out.println("Requete INVOKE envoyé au serveur REST");
			
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(socketREST.getInputStream()));
            String line;
            PrintWriter out = new PrintWriter("wadl.xml");
            int i = 0;
            while ((line = rd.readLine()) != null) {
            	if(i>=6) out.println(line); // on ecrit pas le header de la requette HTTP, on veut juste ecrire le body qui est le wadl.xml
            	i++;
                //System.out.println(line);
                
            }
            out.println("\r\n");
            out.close();
            wr.close();
            rd.close();
            socketREST.close();
            System.out.println("Réception du fichier wadl.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void socketSOAP() {
		//à implementer
		
		String OUTPUT;
        BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("wsdl.xml")));
			String line;
		    StringBuilder sb = new StringBuilder();

		    
			while((line=br.readLine())!= null){
					sb.append(line);
					sb.append("\n");
					//System.out.println(line);
			}
			OUTPUT  = sb.toString();
			System.out.println(OUTPUT);
			
			BufferedWriter out;
			out = new BufferedWriter(
				    new OutputStreamWriter(
				        new BufferedOutputStream(socketSOAP.getOutputStream()), "UTF-8"));
				    out.write(OUTPUT_HEADERS + OUTPUT.length() + OUTPUT_END_OF_HEADERS + OUTPUT);
					out.flush();
					//out.close();
					//socketSOAP.close();
					System.out.println("Envoie de la réponse contenant le fichier wsdl.xml");
		}catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
}
