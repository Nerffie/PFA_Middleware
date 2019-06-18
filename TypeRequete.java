package connexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TypeRequete implements Runnable {

	private Socket socket;
	private int requete;
	private String body;
	private String method;
	private String path;
	
	
	public TypeRequete(Socket socket) {
		super();
		this.socket = socket;
	}

	public int getRequete() {
		return requete;
	}

	public void setRequete(int requete) {
		this.requete = requete;
	}
	

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	


	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader rd;
		String[] header = {};
		try {
			//String line;
			//rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//int i = 0;
			//while ((line = rd.readLine()) != null) {
				//System.out.println(line);
				//System.out.println("Requete Recu "+line);
		       
		        /*header = line.split(" ");
		        setMethod(header[0]);
		        setPath(header[1]);
		        
		        
		    
		        
		        break;*/
		     
			//}
			//setRequete(12);
			//socket is an instance of Socket
			InputStream is = socket.getInputStream();
			InputStreamReader isReader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isReader);

			//code to read and print headers
			String headerLine = new String();
			int i =0;
			    while((headerLine = br.readLine()).length() != 0){
			        if (i==0) {
			        	header = headerLine.split(" ");
				    	this.setMethod(header[0]);
				        this.setPath(header[1]);
				        i++;
			        }
			        
			    }
			//code to read the post payload data
			StringBuilder payload = new StringBuilder();
			        while(br.ready()){
			            payload.append((char) br.read());
			            }
			//System.out.println("Payload data is: "+payload.toString());
			this.setBody(payload.toString());
			
			System.out.println( "Method "+getMethod());
			System.out.println("Path "+getPath());
			System.out.println("Body" +getBody());
			
			switch(getMethod()) {
			case  "GET" : 
							switch(getPath()) {
							case "/Produits/Stock" : setRequete(11);break;
							case "/Produits" :setRequete(12);break;
							default : setRequete(0);break;
							}
							break;
			case "POST" : 
				switch(getPath()) {
				case "/Produits" : setRequete(2);break;
				default : setRequete(0);break;
				}
				break;
			
			//System.out.println("POST");break;
			case "PUT" : 
				switch(getPath()) {
				case "/Produits" : setRequete(3);break;
				default : setRequete(0);break;
				}
				break;
			case "DELETE" : setRequete(4);break;
			default : setRequete(0);break;
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
