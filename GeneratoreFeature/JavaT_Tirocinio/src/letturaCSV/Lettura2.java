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

	    try (BufferedReader br = new BufferedReader(new FileReader(dir + fileName))) {
	    	
	    	line = br.readLine();	
	        while ((line = br.readLine()) != null) {
	           
	        	nomiCombinazioni rigaNomi = new nomiCombinazioni(line.substring(0, line.indexOf(",")), line.substring(line.indexOf(",") + 2), "", "");
	        	listaCombinazioni.add(rigaNomi);
	        
	        //System.out.println(line);	
	        //System.out.println("\n");	
	      
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return listaCombinazioni;
	    
	}
	
}//fine class
