package letturaCSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Lettura2 {
public ArrayList<nomiCombinazioni> listaCombinazioni = new  ArrayList<nomiCombinazioni>();
	
	
	//costruttore 
	
	public Lettura2() {
		super();
	}
		
	public  ArrayList<nomiCombinazioni> procedura(String fileName) throws IOException, InterruptedException{
		String dir = "./OBSClassifier/tester/predictions/";
	    String line = "";
	    int i = 0;
	    
	    try (BufferedReader br = new BufferedReader(new FileReader(dir + fileName))) {
	    	
	    	line = br.readLine();	
	        while ((line = br.readLine()) != null) {
	        	String classe1,classe2,classe3 = "",classe4 = "",classe5 = "";
	        	classe1 = line.substring(0, line.indexOf(","));
	        	String newline = line.substring(line.indexOf(",")+2);
	        	
	        	if (newline.indexOf(",") != -1) {
	        		classe2 = newline.substring(0, newline.indexOf(","));
	        		newline = newline.substring(newline.indexOf(",")+2);
		        	
		        	if (newline.indexOf(",") != -1) {
		        		classe3 = newline.substring(0, newline.indexOf(","));
		        		newline = newline.substring(newline.indexOf(",")+2);

			        	if (newline.indexOf(",") != -1) {
				        	classe4 = newline.substring(0, newline.indexOf(","));
			        		newline = newline.substring(newline.indexOf(",")+2);
				        	classe5 = newline.substring(0);
			        	}
			        	else {
			        		classe4 = newline.substring(0);
			        	}
		        	}
		        	else {
		        		classe3 = newline.substring(0);
		        	}
	        	}
	        	else {
	        		classe2 = newline.substring(0);
	        	}
	        	
	        	nomiCombinazioni rigaNomi = new nomiCombinazioni(classe1,classe2,classe3,classe4,classe5);
	        	listaCombinazioni.add(rigaNomi);
	       
	        	i++;
	        	System.out.println("CLASSI CLASSI CLASSI " + i + ": classe1:" + classe1 + ", classe2: " + classe2 + ", classe3: " + classe3 + ", classe 4: " + classe4 + ", classe 5: " + classe5);
	        	System.out.println("\n");	
	      
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return listaCombinazioni;
	    
	}
	
}//fine class
