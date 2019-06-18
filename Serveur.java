package connexion;



import java.io.IOException;

import java.net.*;


public class Serveur {
	public static int portNumber;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		portNumber = 6006;
		
		try(ServerSocket socketServeur = new ServerSocket(portNumber)){
			while(true) {
				Socket socketSOAP = socketServeur.accept();
				TypeRequete typeRequete  = new TypeRequete(socketSOAP);
				Thread traiterRequete;
				
					Thread typeRequeteThread = new Thread(typeRequete);
					typeRequeteThread.start();
					typeRequeteThread.join();
					
					switch(typeRequete.getRequete()) {
					case 11 : //Produits/Stock
							//System.out.println("case 11");
							traiterRequete = new Thread(new GetProduitsStockThread(socketSOAP));
							traiterRequete.start();
							
							 break;
							 
					case 12 : //Produits break;
						//System.out.println("case 12");
							traiterRequete = new Thread(new GetProduitsThread(socketSOAP));
							traiterRequete.start();
							break;
					case 2 : //Post Produits 
							traiterRequete = new Thread(new PostProduitThread(socketSOAP,typeRequete.getBody()));
							traiterRequete.start();
							break;
					case 3 : //PUT Produits 
						traiterRequete = new Thread(new PutProduitThread(socketSOAP,typeRequete.getBody()));
						traiterRequete.start();
						break;
					case 4 ://DELETE PRoduits 
						break;
					
					case 0 : 
						traiterRequete= new Thread(new ConnexionThread(socketSOAP));
						traiterRequete.start();
						//System.out.println("case 0 ");
						break;
					}
					
				
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
