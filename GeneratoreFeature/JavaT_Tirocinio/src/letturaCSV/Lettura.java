package letturaCSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Lettura {
public ArrayList<nomiCombinazioni> listaCombinazioni = new  ArrayList<nomiCombinazioni>();
	
	
	//costruttore 
	
	public Lettura() {
		super();
	}
	
	
	public nomiCombinazioni controlloVirgole(String linea , int virgole) {
		  
	      	String appoggio = "";
	      	
	      	System.out.println("SONO DENTRO");
	      	
	      	nomiCombinazioni nomi = new nomiCombinazioni("","","","");
          
	        for	(int i=0;i<linea.length();i++) {
	        	
	        	String c=linea.substring(i, i+1);
			    
			    if(c.equals(",")) {
			    virgole++;
			    
			    if(virgole==1) {
                  
                   nomi.setClasse1(appoggio);

                   //salva appoggio in struttura
                   
                  appoggio="";
			    	
			    }
                if(virgole==2) {
                    
                    nomi.setClasse2(appoggio);

                    appoggio="";

			    }
                
                if(virgole==3) {
                   
                    nomi.setClasse3(appoggio);

                    appoggio="";

			    }
               
			    
			    } else {	
			    	appoggio=appoggio.concat(c);
			    	
			    }
	        	
	        } // fine FOR
	        
	        virgole++;
	        
	        if(virgole==1) {
              
                nomi.setClasse1(appoggio);

                //salva appoggio in struttura
                
               appoggio="";
			    	
			    }
             if(virgole==2) {
               
                 nomi.setClasse2(appoggio);

                 appoggio="";

			    }
             
             if(virgole==3) {
          
                 nomi.setClasse3(appoggio);

                 appoggio="";

			    }
             
             if(virgole==4) {
                 
                 nomi.setClasse4(appoggio);

                 appoggio="";

			    }
	        
            System.out.println(appoggio);
            
           return nomi;
		
	} //FINE FUNZIONE GENERALE
	
	
	public  ArrayList<nomiCombinazioni> procedura(String fileName) throws IOException, InterruptedException {
		String dir = "./OBSClassifier/tester/predictions/";
	    String line = "";

	    try (BufferedReader br = new BufferedReader(new FileReader(dir + fileName))) {
	    	
	    	line = br.readLine();	
	    
	        while ((line = br.readLine()) != null) {
	        	
	        	int vir=0;
	           
	        	nomiCombinazioni rigaNomi = controlloVirgole(line,vir);
	        	System.out.println("ECCOMI ECCOMI ECCOMI " + rigaNomi);
	        	listaCombinazioni.add(rigaNomi);
	      
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return listaCombinazioni;
	    
	}
	
}//fine class

