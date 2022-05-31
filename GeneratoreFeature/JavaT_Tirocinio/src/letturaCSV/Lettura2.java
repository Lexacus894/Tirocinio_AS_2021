package letturaCSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dataset.FeatureCommandInstances;

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
	        	
	        	ArrayList<String> templist = new ArrayList<String>();
	        	templist.add(classe1);
	        	templist.add(classe2);
	        	templist.add(classe3);
	        	templist.add(classe4);
	        	templist.add(classe5);
	        	//nomiCombinazioni rigaNomi = new nomiCombinazioni(classe1,classe2,classe3,classe4,classe5);
	        	listaCombinazioni.add(ordinaClassi(templist));
	       
	        	i++;
	        	System.out.println("CLASSI CLASSI CLASSI " + i + ": classe1:" + classe1 + ", classe2: " + classe2 + ", classe3: " + classe3 + ", classe 4: " + classe4 + ", classe 5: " + classe5);
	        	System.out.println("\n");	
	      
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return listaCombinazioni;
	    
	}
	
	private nomiCombinazioni ordinaClassi(ArrayList<String> listaClassi) {
		String cc="",ci="",re="",cl="",in="";
		for(int i=0;i<5;i++) {
			if (!listaClassi.get(i).equals("")) {
				if (listaClassi.get(i).contains(" - CC")) {
					cc = listaClassi.get(i);
				}
				if (listaClassi.get(i).contains(" - CI")) {
					ci = listaClassi.get(i);
				}
				if (listaClassi.get(i).contains(" - RE")) {
					re = listaClassi.get(i);
				}
				if (listaClassi.get(i).contains(" - CL")) {
					cl = listaClassi.get(i);
				}
				if (listaClassi.get(i).contains(" - IN")) {
					in = listaClassi.get(i);
				}
			}
		}
		nomiCombinazioni rigaNomi = new nomiCombinazioni(cl,cc,re,in,ci);
		return rigaNomi;
	}
	
}//fine class
