package it.unisa.javat;

import letturaCSV.Lettura;
import letturaCSV.Lettura2;
import letturaCSV.nomiCombinazioni;
import letturaCSV.provaLettura;

import java.awt.FileDialog;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;

import dataset.Feature;
import dataset.Feature3;
import dataset.FeatureCommandRoles;
import dataset.FeatureCommandInstances;

public class Process {

	private Parameters _params;	
	private Project _project;
	private Parser _parser;	
	ArrayList<Feature> listaFeature;
	ArrayList<FeatureCommandRoles> listaFeatureCommandRoles;
	private String filename;
	static boolean bool=false;
	static String dptype="";
	ArrayList<nomiCombinazioni> listaCombinazioni;
	ArrayList<Feature3> listaFeature3;
	ArrayList<FeatureCommandInstances> listaFeatureCommandInstances;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";

	//CSV file header (Ruoli Observer)
	private static final String FILE_HEADER_OBSERVER_ROLES = "SoftwareName;FQNClass;CollectionVariable;AddListenerMethod;RemoveListenerMethod;ClassDeclarationKeyword;" 
			+ "MethodDeclarationKeyword;ClassType;ScanCollectionMethod;SCMCallAbsMethod;HasSuperclass;ImplementsInterfaces;ChangeState;AfterChangeStateIterateOverList";
	
	//CSV file header (Ruoli Command)
	private static final String FILE_HEADER_COMMAND_ROLES = "SoftwareName;FileName;FQNClass;ClassType;ClassDeclarationKeyword;MethodDeclarationKeyword;ExecutesCommand;"
			+ "InstantiatesCommand;HasSuperclass;ImplementsInterfaces;IsPartOfExecute";
	 
	//CSV file header (Combinazioni Observer)
	private static final String FILE_HEADER_OBSERVER_INSTANCES = "Classes;HasSubject;HasObserver;"
 	  		+ "SubjectsRelationship;SubObsDependencies;CSubObsDependencies;ObserversRelationship;CallListeners;CObsAccessSubject;NoC";
	
	//CSV file header (Combinazioni Command)
	private static final String FILE_HEADER_COMMAND_INSTANCES = "Class1;Class2;Class3;Class4;Class5;CommandRelationship;CCRERelationship;InvokeMethod;ExecutesWithContext;UsesCommandList";
     
	
	public Process(String[] args) throws IOException, InterruptedException {
		
		//Estrazione feature dei ruoli (Observer)
		if (bool==false && dptype.equals("obs")) {
		try {
			Info(true);
			
			//Crea ArrayList per inserire i vettori di Feature 
			//DOBBIAMO PORTARLO NEL CLASSVISITOR DEVO SOLO CAPIRE COME 
		    listaFeature=new ArrayList<Feature>();
		    
		    System.out.println("-------------ArrayList Feature  CREATO -----------------");
		    
		    _params = new Parameters(args, this.getClass().getName());
			_params.print();
			
			//SELEZIONE CARTELLA
			try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    }
			catch(Exception e) {
		        e.printStackTrace();
		    }
			JFrame jf = new JFrame("Dialog"); // added
	        jf.setAlwaysOnTop( true );
			JFileChooser f = new JFileChooser(System.getProperty("user.home") + "\\Desktop");
	        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
	        f.showOpenDialog(jf);

	        String path = f.getSelectedFile().getPath();
	        //System.out.println(path);
	        
			String _projectDir;
			String _projectName;
			
			int pos = path.lastIndexOf(File.separator);
			if (pos > -1) {
				_projectDir = path.substring(0, pos);
				_projectName = path.substring(pos + 1);
			} else {
				_projectDir = ".";
				_projectName = path;
			}
			
			_project = new Project(path,_params.getOutputPath());
			_project.print();
						
			List<String> files = _project.getSourceFiles();
			for(String s: files) {
				Utils.print("Source file:"+s);
			}

			_parser = new Parser(_params.getJavaVersion());
			_parser.addClasspath(_project.getSourcePath());
			_parser.addClasspaths(_project.getBinaryPath());
			_parser.addClasspaths(_project.getLibraryPath());
			_parser.print();
			
			//System.out.println("-------------DOVE SONO ?? -----------------");

			for (String s : files) {
				try {	
					//System.out.println("-------------E POI IL PARSER RITORNA AL PROCESS-----------------");

							_parser.compile(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s);
							
							String folder = s;
							String nomeProgetto=_project.getProjectName();

							//INSERISCO L'ARRAYLIST DI FEATURE NEI PARAMETRI DELLA FUNZIONE PARSE DELL'OGGETTO PARSER
							_parser.parse(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s, _params.getOutputPath(),listaFeature,folder,false,listaFeature3,nomeProgetto);
							//break;
						
				} 
				catch (LocalException e) {
					Utils.print(e);
				}			
			}	
			System.out.println("-------------FINE Process -----------------");

			creaCSV("DATASET_OBS_" + _project.getProjectName() + ".csv");

			Info(false);
		} 
		catch (LocalException e) {
			Utils.print(e);
			System.exit(1);
		}
	}
		
		//Estrazione feature dei ruoli (Command)
		else if (bool == false && dptype.equals("com")) {
			try {
				Info(true);
				
				//Crea ArrayList per inserire i vettori di Feature COMMAND
			    listaFeatureCommandRoles = new ArrayList<FeatureCommandRoles>();
			    
			    System.out.println("-------------ArrayList Feature  CREATO -----------------");
			    
			    _params = new Parameters(args, this.getClass().getName());
				_params.print();
				
				//SELEZIONE CARTELLA
				try {
			        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    }
				catch(Exception e) {
			        e.printStackTrace();
			    }
				JFrame jf = new JFrame("Dialog"); // added
		        jf.setAlwaysOnTop( true );
				JFileChooser f = new JFileChooser(System.getProperty("user.home") + "\\Desktop");
		        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		        f.showOpenDialog(jf);

		        String path = f.getSelectedFile().getPath();
		        //System.out.println(path);
		        
				String _projectDir;
				String _projectName;
				
				int pos = path.lastIndexOf(File.separator);
				if (pos > -1) {
					_projectDir = path.substring(0, pos);
					_projectName = path.substring(pos + 1);
				} 
				else {
					_projectDir = ".";
					_projectName = path;
				}
				
				_project = new Project(path,_params.getOutputPath());
				_project.print();
							
				List<String> files = _project.getSourceFiles();
				for(String s: files) {
					Utils.print("Source file:"+s);
				}

				_parser = new Parser(_params.getJavaVersion());
				_parser.addClasspath(_project.getSourcePath());
				_parser.addClasspaths(_project.getBinaryPath());
				_parser.addClasspaths(_project.getLibraryPath());
				_parser.print();
				
				//System.out.println("-------------DOVE SONO ?? -----------------");
				
				for (String s : files) {
					try {	
						//System.out.println("-------------E POI IL PARSER RITORNA AL PROCESS-----------------");

								_parser.compile(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s);
								
								String folder = s;
								String nomeProgetto=_project.getProjectName();

								//INSERISCO L'ARRAYLIST DI FEATURE NEI PARAMETRI DELLA FUNZIONE PARSE DELL'OGGETTO PARSER
								_parser.parseCommand(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s, _params.getOutputPath(),listaFeatureCommandRoles,folder,false,listaFeatureCommandInstances,nomeProgetto);
								//break;
							
					} 
					catch (LocalException e) {
						Utils.print(e);
					}			
				}	

				System.out.println("-------------FINE Process -----------------");

				creaCSV("DATASET_COM_" + _project.getProjectName() + ".csv");

				Info(false);
			} 
			catch (LocalException e) {
				Utils.print(e);
				System.exit(1);
			}
		}
		
		//Estrazione feature delle combinazioni (Observer)
		else if (bool==true && dptype.equals("obs")) {
			
		    try {
				Info(true);
				
				 listaFeature3 = new ArrayList<Feature3>();
					 
			     Lettura lettura = new Lettura();
			     listaCombinazioni= lettura.procedura("combinations_to_test_observer.csv");
			     
			     for (nomiCombinazioni nomi : listaCombinazioni) {
			    	 Feature3 elemento= analisi(nomi);
			    	 listaFeature3.add(elemento);
			    	 System.out.println(nomi.toString());
			     }
			     listaFeature=new ArrayList<Feature>();
				    
				    listaFeature=new ArrayList<Feature>();

				    for (nomiCombinazioni nomi : listaCombinazioni) {
				    	Utils.print(nomi.toString());
				    }
				   
			        System.out.println("-------------ArrayList Feature  CREATO -----------------");
				    
				   
			     

				    _params = new Parameters(args, this.getClass().getName());
					_params.print();
					
					
					for(int i=0;i<listaFeature3.size();i++) {
				    	Feature3 riga= listaFeature3.get(i);
				    	Utils.print(riga.toString());
					}
					
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

					//SELEZIONE CARTELLA
					try {
				        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				    }
					catch(Exception e) {
				        e.printStackTrace();
				    }
					JFrame jf = new JFrame("Dialog"); // added
			        jf.setAlwaysOnTop( true );
					JFileChooser f = new JFileChooser();
			        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
			        f.showOpenDialog(jf);

			        String path = f.getSelectedFile().getPath();
			        //System.out.println(path);
String _projectDir;
String _projectName;

int pos = path.lastIndexOf(File.separator);
if (pos > -1) {
	_projectDir = path.substring(0, pos);
	_projectName = path.substring(pos + 1);
} 
else {
	_projectDir = ".";
	_projectName = path;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

					_project = new Project(path,_params.getOutputPath());
					_project.print();
								
					List<String> files = _project.getSourceFiles();
					for(String s: files) {
						Utils.print("Source file:"+s);
					}

					_parser = new Parser(_params.getJavaVersion());
					_parser.addClasspath(_project.getSourcePath());
					_parser.addClasspaths(_project.getBinaryPath());
					_parser.addClasspaths(_project.getLibraryPath());
					_parser.print();
				 
					for (String s : files) {
						try {	
							//System.out.println("-------------E POI IL PARSER RITORNA AL PROCESS-----------------");

									_parser.compile(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s);
									
									String folder = s;
									String nomeProgetto=_project.getProjectName();


									//INSERISCO L'ARRAYLIST DI FEATURE NEI PARAMETRI DELLA FUNZIONE PARSE DELL'OGGETTO PARSER
									_parser.parse(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s, _params.getOutputPath(),listaFeature,folder,true,listaFeature3,nomeProgetto);
									//break;
								
						} 
						catch (LocalException e) {
							Utils.print(e);
						}			
					}	
				} 
		    	catch (LocalException e) {
		    		Utils.print(e);
		    	}//fine Catch
		    
			filename="Combination_to_test_MIO.csv";
            creaCSV3(filename);
		    
		} //fine else bool=true
		
		//Estrazione feature delle combinazioni (Command)
		else if (bool==true && dptype.equals("com")) {
			
		    try {
		    	Info(true);
				
		    	listaFeatureCommandRoles = new ArrayList<FeatureCommandRoles>();
				listaFeatureCommandInstances = new ArrayList<FeatureCommandInstances>();
					 
			    Lettura2 lettura = new Lettura2();
			    listaCombinazioni = lettura.procedura("combinations_to_test_Command.csv");
			 
			    for (nomiCombinazioni nomi : listaCombinazioni) {
			    	FeatureCommandInstances elemento = new FeatureCommandInstances(nomi.getClasse1(),nomi.getClasse2(),nomi.getClasse3(),nomi.getClasse4(),nomi.getClasse5(),1,1,1,1,1);
			    	listaFeatureCommandInstances.add(elemento);
			    	//System.out.println(nomi.toString());
			    }
			      
			    System.out.println("-------------ArrayList Feature  CREATO -----------------");

				_params = new Parameters(args, this.getClass().getName());
				_params.print();
					
				for(int i=0;i<listaFeatureCommandRoles.size();i++) {
				    	FeatureCommandInstances riga= listaFeatureCommandInstances.get(i);
				    	//Utils.print(riga.toString());
				
				}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

				//SELEZIONE CARTELLA
				try {
			        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    }
				catch(Exception e) {
			        e.printStackTrace();
			    }
				JFrame jf = new JFrame("Dialog"); // added
		        jf.setAlwaysOnTop( true );
				JFileChooser f = new JFileChooser();
		        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		        f.showOpenDialog(jf);

		        String path = f.getSelectedFile().getPath();
		        //System.out.println(path);
String _projectDir;
String _projectName;

int pos = path.lastIndexOf(File.separator);
if (pos > -1) {
	_projectDir = path.substring(0, pos);
	_projectName = path.substring(pos + 1);
} 
else {
	_projectDir = ".";
	_projectName = path;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

					_project = new Project(path,_params.getOutputPath());
					_project.print();
								
					List<String> files = _project.getSourceFiles();
					for(String s: files) {
						Utils.print("Source file:"+s);
					}

					_parser = new Parser(_params.getJavaVersion());
					_parser.addClasspath(_project.getSourcePath());
					_parser.addClasspaths(_project.getBinaryPath());
					_parser.addClasspaths(_project.getLibraryPath());
					_parser.print();
				 
					for (String s : files) {
						try {	
							//System.out.println("-------------E POI IL PARSER RITORNA AL PROCESS-----------------");

									_parser.compile(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s);
									
									String folder = s;
									String nomeProgetto=_project.getProjectName();

									//INSERISCO L'ARRAYLIST DI FEATURE NEI PARAMETRI DELLA FUNZIONE PARSE DELL'OGGETTO PARSER
									_parser.parseCommand(_project.getProjectPath(), _project.getProjectName(), _project.getSourcePath(), s, _params.getOutputPath(),listaFeatureCommandRoles,folder,true,listaFeatureCommandInstances,nomeProgetto);		
									//break;
								
						} 
						catch (LocalException e) {
							Utils.print(e);
						}			
					}	
		    } 
		    catch (LocalException e) {
		    	Utils.print(e);
		    }//fine Catch
		    
            creaCSV3("Combination_mockup_Command.csv");
		    
		}
		
		
	}	//fineProcess
	
	
	
	//Metodo per la creazione del Dataset con le feature dei ruoli per OBSERVER e COMMAND
	public void creaCSV(String fileName) {
		
		FileWriter fileWriter = null;
	        
        	try {
	        
        		fileWriter = new FileWriter(fileName);
	        	            
	        if (dptype.equals("obs")) {
	        	//Write the CSV file header
	        	fileWriter.append(FILE_HEADER_OBSERVER_ROLES.toString());
	        	 
	        	//Add a new line separator after the header
		        fileWriter.append(NEW_LINE_SEPARATOR);
		          
		         for (Feature vettoreFeat : listaFeature) {
		        	
		        	  fileWriter.append(vettoreFeat.getSfotwareName());	 
		              fileWriter.append(COMMA_DELIMITER);
		        	  
		        	  fileWriter.append(vettoreFeat.getFQNClass());	 
		              fileWriter.append(COMMA_DELIMITER);
		        	  
		        	  fileWriter.append(String.valueOf(vettoreFeat.getCollectionVariables()));	 
		              fileWriter.append(COMMA_DELIMITER);
		        	 
		              fileWriter.append(String.valueOf(vettoreFeat.getAddListenerMethod()));	 
		        	  fileWriter.append(COMMA_DELIMITER);
		        	
		        	  fileWriter.append(String.valueOf(vettoreFeat.getRemoveListenerMethod()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getClassDeclarationKeyword()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getMethodDeclarationKeyword()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getClassType()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getScanCollectionMethod()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getSCMCallAbsMethod()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getHasSuperclass()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getImplementsInterfaces()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getChangeState()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getAfterChangeStateIterateOverList()));	 
		        	
		              fileWriter.append(NEW_LINE_SEPARATOR);
		          }
	         }
	         else if (dptype.equals("com")) {
	        	 
	        	 ArrayList<String> lista = _parser.getListaNomiInExecute();
	        	//Write the CSV file header
	        	 fileWriter.append(FILE_HEADER_COMMAND_ROLES.toString());
	        	 
	        	 //Add a new line separator after the header
		         fileWriter.append(NEW_LINE_SEPARATOR);
		          
		         for (FeatureCommandRoles vettoreFeat : listaFeatureCommandRoles) {
		        	
		        	  fileWriter.append(vettoreFeat.getSoftwareName());	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(vettoreFeat.getFileName());	 
		              fileWriter.append(COMMA_DELIMITER);
		            		        	  
		        	  fileWriter.append(vettoreFeat.getFQNClass());	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getClassType()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getClassDeclarationKeyword()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getMethodDeclarationKeyword()));	 
		              fileWriter.append(COMMA_DELIMITER);
		             
		              fileWriter.append(String.valueOf(vettoreFeat.getExecutesCommand()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getInstantiatesCommand()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getHasSuperclass()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              fileWriter.append(String.valueOf(vettoreFeat.getImplementsInterfaces()));	 
		              fileWriter.append(COMMA_DELIMITER);
		              
		              //Ricerca delle classi che dichiarano metodi invocati in un metodo execute() di un Concrete Command
		              for (int j = 0; j < lista.size(); j++) {
							if (vettoreFeat.getFQNClass().replaceAll(".java", "").contentEquals(lista.get(j))) {
								vettoreFeat.setIsPartOfExecute(2);
								break;
							}
		              }
		              fileWriter.append(String.valueOf(vettoreFeat.getIsPartOfExecute()));	 
		              
		              fileWriter.append(NEW_LINE_SEPARATOR);
		         }
	         }
	          System.out.println("Il file CSV (" + fileName + ") contentente le feature dei ruoli è stato creato con successo!");

		} 
	        catch(Exception e) {
	        	System.out.println("Errore nel CsvFileWriter !!!");
	        	e.printStackTrace();
	        } 
	        finally {
			
	        	try {
	        		fileWriter.flush();
	        		fileWriter.close();
	        	} 
	        	catch (IOException e) {
	        		System.out.println("Errore nel flush o nella chiusura !!!");
	        	}
			}
		
	}
	

	public void creaCSV3(String fileName) {
		
		FileWriter fileWriter = null;
	        
		try {
			if (dptype.contains("obs")) {
				fileWriter = new FileWriter(fileName);
	            
		         //Write the CSV file header
		         fileWriter.append(FILE_HEADER_OBSERVER_INSTANCES.toString());
		       
		         //Add a new line separator after the header
		         fileWriter.append(NEW_LINE_SEPARATOR);
		          
		         for (Feature3 vettoreFeat : listaFeature3) {
		        	 
		        	 fileWriter.append(vettoreFeat.getClasses());	 
		             fileWriter.append(COMMA_DELIMITER);
		        	  
		        	 fileWriter.append(String.valueOf(vettoreFeat.getHasSubject()));	 
		             fileWriter.append(COMMA_DELIMITER);
		        	 
		             fileWriter.append(String.valueOf(vettoreFeat.getHasObserver()));	 
		        	 fileWriter.append(COMMA_DELIMITER);
		        	
		        	 fileWriter.append(String.valueOf(vettoreFeat.getSubjectsRelationship()));	 
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(String.valueOf(vettoreFeat.getSubObsDepedencies()));	 
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(String.valueOf(vettoreFeat.getCSubObsDependencies()));	 
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(String.valueOf(vettoreFeat.getObserversRelationship()));	 
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(String.valueOf(vettoreFeat.getCallListeners()));	 
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(String.valueOf(vettoreFeat.getCObsAccessSubject()));	 
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(String.valueOf(vettoreFeat.getNoc()));	 
	        	
		             fileWriter.append(NEW_LINE_SEPARATOR);
		          }
			}
			else if (dptype.equals("com")) {
				fileWriter = new FileWriter(fileName);
	            
		         //Write the CSV file header
		         fileWriter.append(FILE_HEADER_COMMAND_INSTANCES.toString());
		       
		         //Add a new line separator after the header
		         fileWriter.append(NEW_LINE_SEPARATOR);
		          
		         for (FeatureCommandInstances vettoreFeat : listaFeatureCommandInstances) {
		        	 
		        	 fileWriter.append(vettoreFeat.getClass1());	 
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(vettoreFeat.getClass2());
		             fileWriter.append(COMMA_DELIMITER);
		             
		             fileWriter.append(vettoreFeat.getClass3());
		             fileWriter.append(COMMA_DELIMITER);
		             
		             fileWriter.append(vettoreFeat.getClass4());
		             fileWriter.append(COMMA_DELIMITER);
		             
		             fileWriter.append(vettoreFeat.getClass5());
		             fileWriter.append(COMMA_DELIMITER);
		              
		             fileWriter.append(String.valueOf(vettoreFeat.getCommandRelationship()));
		             fileWriter.append(COMMA_DELIMITER);
		             
		             fileWriter.append(String.valueOf(vettoreFeat.getCCRERelationship()));
		             fileWriter.append(COMMA_DELIMITER);
		             
		             fileWriter.append(String.valueOf(vettoreFeat.getInvokeMethod()));
		             fileWriter.append(COMMA_DELIMITER);
		             
		             fileWriter.append(String.valueOf(vettoreFeat.getExecutesWithContext()));
		             fileWriter.append(COMMA_DELIMITER);
		             
		             fileWriter.append(String.valueOf(vettoreFeat.getUsesCommandList()));
		             fileWriter.append(NEW_LINE_SEPARATOR);
		         }
			}
	         
			System.out.println("Il file CSV ("+ fileName +") contenente le predizioni delle istanze è stato creato con successo!");

		} 
		catch(Exception e) {
			System.out.println("Errore nel CsvFileWriter !!!");
			e.printStackTrace();
		} 
		finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} 
			catch (IOException e) {
				System.out.println("Errore nel flush o nella chiusura !!!");
			}
		}
	}


	private void Info(boolean start) throws LocalException {
		if (start) {
			String version = Constants.version + "." + VersionBuild.buildnum;
			Utils.print("*** " + Constants.appName + " ***");
			Utils.print("*** " + Constants.appAcro + " " +version+ " "+VersionBuild.builddate+" ***");
			Utils.print("*** " + Constants.authors + " ***");
		} 
		else { // FINE DEL PROGRAMMA GENERALE
			Utils.print("*** End " + Constants.appAcro + " ***");
		}
	}	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner sc = new Scanner(System.in);
		String r="";
		Process proc = new Process(args);
		
		while(!r.equals("9")) {
			Utils.print("Inserire numero corrispondente alla fase da eseguire\n"
					+ "1 - Estrazione feature dei ruoli (Observer)\n"
					+ "2 - Classificazione dei ruoli (Observer)\n"
					+ "3 - Estrazione feature delle combinazioni (Observer)\n"
					+ "4 - Classificazione delle istanze (Observer)\n"
					+ "5 - Estrazione feature dei ruoli (Command)\n"
					+ "6 - Classificazione dei ruoli (Command)\n"
					+ "7 - Estrazione feature delle combinazioni (Command)\n"
					+ "8 - Classificazione delle istanze (Command)\n"
					+ "9 - Fine esecuzione\n"
					);
			r=sc.nextLine();
			
			//OBSERVER - ESTRAZIONE FEATURE DEI RUOLI - OBSERVER
			if (r.equals("1")) { 
				bool=false;
				dptype="obs";
				new Process(args);
			}
			
			//OBSERVER - CLASSIFICAZIONE DEI RUOLI - OBSERVER
			else if (r.equals("2")) {
				BatchCommand bc = new BatchCommand();
				bc.execCommand("C:\\Users\\alex8\\AppData\\Local\\Programs\\Python\\Python37\\python.exe ./OBSClassifier/tester/RolesClassifierTester.py");
			}
			
			//OBSERVER - ESTRAZIONE FEATURE DELLE COMBINAZIONI - OBSERVER
			else if (r.equals("3")) {
				bool=true;
				dptype="obs";
			    new Process(args);
			}
			
			//OBSERVER - CLASSIFICAZIONE DELLE ISTANZE - OBSERVER
			else if (r.equals("4")) {
				BatchCommand bc2 = new BatchCommand();
				bc2.execCommand("C:\\Users\\alex8\\AppData\\Local\\Programs\\Python\\Python37\\python.exe ./OBSClassifier/tester/InstancesClassifierTester.py");
			}
			
			//COMMAND - ESTRAZIONE FEATURE DEI RUOLI - COMMAND
			else if (r.equals("5")) {
				bool=false;
				dptype="com";
				new Process(args);
			}
			
			//COMMAND - CLASSIFICAZIONE DEI RUOLI - COMMAND
			else if (r.equals("6")) {
				BatchCommand bc = new BatchCommand();
				bc.execCommand("C:\\Users\\alex8\\AppData\\Local\\Programs\\Python\\Python37\\python.exe ./OBSClassifier/tester/RolesClassifierTesterCommand.py");
			}
			
			//COMMAND - ESTRAZIONE FEATURE DELLE COMBINAZIONI - COMMAND
			else if (r.equals("7")) {
				bool=true;
				dptype="com";
			    new Process(args);
			}
			
			//COMMAND - CLASSIFICAZIONE DELLE ISTANZE - COMMAND
			else if (r.equals("8")) {
				BatchCommand bc2 = new BatchCommand();
				bc2.execCommand("C:\\Users\\alex8\\AppData\\Local\\Programs\\Python\\Python37\\python.exe ./OBSClassifier/tester/InstancesClassifierTesterCommand.py");
			}
		}
		
		sc.close();
		Utils.print("----------------------EXIT---------------------       ");
		System.exit(0);
		
	}
	
	public Feature3 analisi2(nomiCombinazioni riga) {
		
		Feature3 elemento = new Feature3("",1,"",1,"",3,4,4,3,5,1,0,"","",false,false,false,false);
		String classe1 = riga.getClasse1();
		String classe2 = riga.getClasse2();
		return elemento;
	}
	
	public Feature3 analisi(nomiCombinazioni riga) {
		
		Feature3 elemento = new Feature3("",1,"",1,"",3,4,4,3,5,1,0,"","",false,false,false,false);
		
		int count=0;
		
		if (!riga.getClasse1().equals("")) {
			count=1;
		}
		
		if (!riga.getClasse2().equals("")) {
			count=2;
		}
		if (!riga.getClasse3().equals("")) {
			count=3;
		}
		if (!riga.getClasse4().equals("")) {
			count=4;
		}
		
		switch(count) {
		case 1: {
			     System.out.println(count);
			     String nome1 = riga.getClasse1();

			     break;
	         	}
		case 2:
			int i=0;
			int j=1;
			String c1,c2;
		     
			  System.out.println(count);
		      String nome1 = riga.getClasse1();
		      String nome2= riga.getClasse2();
		    
		      String c=nome1.substring(1,2);
		      
		      if (c.equals("-")){
		    	  

		    	   c1= nome1.substring(0,1);
		    	  
		      } else {
		    	  
		    	   c1= nome1.substring(0,2);
	  
		      }
		      
             String cc2=nome2.substring(1,2);
		      
		      if (cc2.equals("-")){

		    	  
		    	   c2= nome2.substring(0,1);
		    	  
		      } else {
		    	  
		    	   c2= nome2.substring(i,j+1);

	  
		      }
		      
		      
		      Utils.print("SOTTOSTRINGA CARATTERE1 ----------------"+c1);
		      Utils.print("SOTTOSTRINGA CARATTERE2 ----------------"+c2);
		      
		      if (c1.equals("S")) {
		    	  elemento.setHasSubject(2);
		       	 elemento = prelevaNome(c1,elemento, nome1);
		      } else if (c2.equals("S")){
		    	  elemento.setHasSubject(2);
			      elemento = prelevaNome(c2,elemento, nome1);

		      }
		      
		      
		      if (c1.equals("O")) {
		    	  elemento.setHasObserver(2);
			      elemento = prelevaNome(c1,elemento, nome1);

		      } else if (c2.equals("O")){
		    	  elemento.setHasObserver(2);
			      elemento = prelevaNome(c2,elemento, nome1);
		      }
		      
		      if (c1.equals("CS")) {
		 
			      elemento = prelevaNome(c1,elemento, nome1);

		      } else if (c2.equals("CS")){
			      elemento = prelevaNome(c2,elemento, nome1);
		      }
		      
		      if (c1.equals("CO")) {
		 		 
			      elemento = prelevaNome(c1,elemento, nome1);

		      } else if (c2.equals("CO")){
			      elemento = prelevaNome(c2,elemento, nome1);
		      }
		      
		      
	   /////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-CS (viceversa) oppure O-CO (viceversa) 

		      if((c1.equals("S")) && (c2.equals("CS"))) {
		    	  
		    	  elemento.setControlloSubRel(true);
		      }
		      if(c1.equals("CS") && (c2.equals("S"))) {  elemento.setControlloSubRel(true);}
 		      
              if((c1.equals("O")) && (c2.equals("CO"))) {
		    	  
		    	  elemento.setControlloObsRel(true);;
		      }
		      if(c1.equals("CO") && (c2.equals("O"))) {  elemento.setControlloObsRel(true);;}
		      
		      
		     if(elemento.getControlloSubRel()==false ) {
		    	 elemento.setSubjectsRelationship(3);
		     }
		     
		     if(elemento.getControlloObsRel()==false ) {
		    	 elemento.setObserversRelationship(3);
		     }
		     
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		     
	/////////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-O (viceversa) oppure S-CO (viceversa) 
		     if((c1.equals("S")) && (c2.equals("O"))) {
		    	  
		    	  elemento.setControlloDipSub(true);;
		      }
		      if(c1.equals("S") && (c2.equals("CO"))) {  elemento.setControlloDipSub(true);}
		      
              if((c1.equals("O")) && (c2.equals("S"))) {
		    	  
		    	  elemento.setControlloDipSub(true);;
		      }
		      if(c1.equals("CO") && (c2.equals("S"))) {  elemento.setControlloDipSub(true);}
		     
		      
		     


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			     
////////////////////////////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-O (viceversa) oppure S-CO (viceversa) 
		      
             if((c1.equals("CS")) && (c2.equals("O"))) {
		    	  
		    	  elemento.setControlloDipCSub(true);;
		      }
		      if(c1.equals("CS") && (c2.equals("CO"))) {  elemento.setControlloDipCSub(true);}
		      
              if((c1.equals("O")) && (c2.equals("CS"))) {
		    	  
		    	  elemento.setControlloDipCSub(true);;
		      }
		      if(c1.equals("CO") && (c2.equals("CS"))) {  elemento.setControlloDipCSub(true);}
		      
		     
		      
		      
		      
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



		        
		      elemento.setNoc(count);
		      
		      elemento.setClasses(riga.getClasse1()+","+riga.getClasse2());


			break;
		case 3:
			
		      System.out.println(count);
		      String nome13 = riga.getClasse1();
		      String nome23= riga.getClasse2();
		      String nome33= riga.getClasse3();
		      String c3;
		       
		        
				String c13,c23;
		
				
			      String cc13=nome13.substring(1,2);
			      
			      if (cc13.equals("-")){
			    	  

			    	   c1= nome13.substring(0,1);
			    	  
			      } else {
			    	  
			    	   c1= nome13.substring(0,2);
		  
			      }
			      
	             String cc23=nome23.substring(1,2);
			      
			      if (cc23.equals("-")){

			    	  
			    	   c2= nome23.substring(0,1);
			    	  
			      } else {
			    	  
			    	   c2= nome23.substring(0,2);

		  
			      }
			      
		             String cc33=nome33.substring(1,2);
				      
				      if (cc33.equals("-")){

				    	  
				    	   c3= nome33.substring(0,1);
				    	  
				      } else {
				    	  
				    	   c3= nome33.substring(0,2);

			  
				      }
			      
			      
			      Utils.print("SOTTOSTRINGA CARATTERE1 ----------------"+c1);
			      Utils.print("SOTTOSTRINGA CARATTERE2 ----------------"+c2);
			      Utils.print("SOTTOSTRINGA CARATTERE2 ----------------"+c3);
			      
			      if (c1.equals("S")) {
			    	  elemento.setHasSubject(2);
				   	 elemento = prelevaNome(c1,elemento, nome13);

			      } else if (c2.equals("S")){
			    	  elemento.setHasSubject(2);
					  elemento = prelevaNome(c2,elemento, nome23);

			      } else if (c3.equals("S")){
			    	  elemento.setHasSubject(2);
					  elemento = prelevaNome(c3,elemento, nome33);

			      } 
			      
			      
			      if (c1.equals("O")) {
			    	  elemento.setHasObserver(2);
					  elemento = prelevaNome(c1,elemento, nome13);

			      } else if (c2.equals("O")){
			    	  elemento.setHasObserver(2);
					  elemento = prelevaNome(c2,elemento, nome23);

			      }else if (c3.equals("O")){
			    	  elemento.setHasObserver(2);
					  elemento = prelevaNome(c3,elemento, nome33);
			      }
			      
			      
			      
			      if (c1.equals("CO")) {
					  elemento = prelevaNome(c1,elemento, nome13);

			      } else if (c2.equals("CO")){
					  elemento = prelevaNome(c2,elemento, nome23);

			      }else if (c3.equals("CO")){
					  elemento = prelevaNome(c3,elemento, nome33);
			      }
			      
			      
			      
			      if (c1.equals("CS")) {
					  elemento = prelevaNome(c1,elemento, nome13);

			      } else if (c2.equals("CS")){
					  elemento = prelevaNome(c2,elemento, nome23);

			      }else if (c3.equals("CS")){
					  elemento = prelevaNome(c3,elemento, nome33);
			      }

				     /////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-CS (viceversa) oppure O-CO (viceversa) 

			      
			      if((c1.equals("S")) && (c2.equals("CS"))) {
			    	  
			    	  elemento.setControlloSubRel(true);
			      }
			      if(c1.equals("CS") && (c2.equals("S"))) {  elemento.setControlloSubRel(true);}
	 		      
	              if((c1.equals("O")) && (c2.equals("CO"))) {
			    	  
			    	  elemento.setControlloObsRel(true);;
			      }
			      if(c1.equals("CO") && (c2.equals("O"))) {  elemento.setControlloObsRel(true);}
			      
			      
			      if((c1.equals("S")) && (c3.equals("CS"))) {
			    	  
			    	  elemento.setControlloSubRel(true);
			      }
			      if(c1.equals("CS") && (c3.equals("S"))) {  elemento.setControlloSubRel(true);}
                
			      if((c1.equals("O")) && (c3.equals("CO"))) {
			    	  
			    	  elemento.setControlloObsRel(true);;
			      }
			      if(c1.equals("CO") && (c3.equals("O"))) {  elemento.setControlloObsRel(true);}
			      
			      
                  if((c2.equals("S")) && (c3.equals("CS"))) {
			    	  
			    	  elemento.setControlloSubRel(true);
			      }
			      if(c2.equals("CS") && (c3.equals("S"))) {  elemento.setControlloSubRel(true);}
                
			      if((c2.equals("O")) && (c3.equals("CO"))) {
			    	  
			    	  elemento.setControlloObsRel(true);;
			      }
			      if(c2.equals("CO") && (c3.equals("O"))) {  elemento.setControlloObsRel(true);}
	 		      
			    
			      if(elemento.getControlloSubRel()==false ) {
				    	 elemento.setSubjectsRelationship(3);
				     }
			      
			      
				     
				     if(elemento.getControlloObsRel()==false ) {
				    	 elemento.setObserversRelationship(3);
				     }
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
				     
				     
				 		     
		/////////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-O (viceversa) oppure S-CO (viceversa) 
				     if((c1.equals("S")) && (c2.equals("O"))) {
				    	  
				    	  elemento.setControlloDipSub(true);
				      }
				      if(c1.equals("S") && (c2.equals("CO"))) {  elemento.setControlloDipSub(true);}
		 		      
		              if((c1.equals("O")) && (c2.equals("S"))) {
				    	  
		            	  elemento.setControlloDipSub(true);
				      }
				      if(c1.equals("CO") && (c2.equals("S"))) {  elemento.setControlloDipSub(true);}
				      
				      
				      if((c1.equals("S")) && (c3.equals("O"))) {
				    	  
				    	  elemento.setControlloDipSub(true);
				    	  
				      }
				      if(c1.equals("S") && (c3.equals("CO"))) { elemento.setControlloDipSub(true);}
	                
				      if((c1.equals("O")) && (c3.equals("S"))) {
				    	  
				    	  elemento.setControlloDipSub(true);
				    	  }
				      if(c1.equals("CO") && (c3.equals("S"))) {  elemento.setControlloDipSub(true);}
				      
				      
	                  if((c2.equals("S")) && (c3.equals("O"))) {
	                	  
	                	  
	                	  elemento.setControlloDipSub(true);
				    	  
				      }
				      if(c2.equals("S") && (c3.equals("CO"))) {  elemento.setControlloDipSub(true);}
	                
				      if((c2.equals("O")) && (c3.equals("S"))) {
				    	  
				    	  elemento.setControlloDipSub(true);
				      }
				      if(c2.equals("CO") && (c3.equals("S"))) {  elemento.setControlloDipSub(true);}			     
				     
				     
				     
				     
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
					     
					     
			 		     
	/////////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-O (viceversa) oppure S-CO (viceversa) 
				      
								     if((c1.equals("CS")) && (c2.equals("O"))) {
								    	  
								    	  elemento.setControlloDipCSub(true);
								      }
								      if(c1.equals("CS") && (c2.equals("CO"))) {  elemento.setControlloDipCSub(true);}
						 		      
						              if((c1.equals("O")) && (c2.equals("CS"))) {
								    	  
						            	  elemento.setControlloDipCSub(true);
								      }
								      if(c1.equals("CO") && (c2.equals("CS"))) {  elemento.setControlloDipCSub(true);}
								      
								      
								      if((c1.equals("CS")) && (c3.equals("O"))) {
								    	  
								    	  elemento.setControlloDipCSub(true);
								    	  
								      }
								      if(c1.equals("CS") && (c3.equals("CO"))) { elemento.setControlloDipCSub(true);}
					                
								      if((c1.equals("O")) && (c3.equals("CS"))) {
								    	  
								    	  elemento.setControlloDipCSub(true);
								    	  }
								      if(c1.equals("CO") && (c3.equals("CS"))) {  elemento.setControlloDipCSub(true);}
								      
								      
					                  if((c2.equals("CS")) && (c3.equals("O"))) {
					                	  
					                	  
					                	  elemento.setControlloDipCSub(true);
								    	  
								      }
								      if(c2.equals("CS") && (c3.equals("CO"))) {  elemento.setControlloDipCSub(true);}
					                
								      if((c2.equals("O")) && (c3.equals("CS"))) {
								    	  
								    	  elemento.setControlloDipCSub(true);
								      }
								      if(c2.equals("CO") && (c3.equals("CS"))) {  elemento.setControlloDipCSub(true);}			     
								     
								     
								 
								     
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		        

			      elemento.setNoc(count);
			      
			      elemento.setClasses(riga.getClasse1()+","+riga.getClasse2()+","+riga.getClasse3());


			        
			break;
		
		case 4:
		      System.out.println(count);
		      String nome14= riga.getClasse1();
		      String nome24= riga.getClasse2();
		      String nome34= riga.getClasse3();
		      String nome44= riga.getClasse4();
		      
		      
		      String cc14=nome14.substring(1,2);
		      
		      if (cc14.equals("-")){
		    	  

		    	   c1= nome14.substring(0,1);
		    	  
		      } else {
		    	  
		    	   c1= nome14.substring(0,2);
	  
		      }
		      
             String cc24=nome24.substring(1,2);
		      
		      if (cc24.equals("-")){

		    	  
		    	   c2= nome24.substring(0,1);
		    	  
		      } else {
		    	  
		    	   c2= nome24.substring(0,2);

	  
		      }
		      
	             String cc34=nome34.substring(1,2);
			      
			      if (cc34.equals("-")){

			    	  
			    	   c3= nome34.substring(0,1);
			    	  
			      } else {
			    	  
			    	   c3= nome34.substring(0,2);

		  
			      }
			      
                      String c4;
		             String cc44=nome44.substring(1,2);
				      
				      if (cc44.equals("-")){

				    	  
				    	   c4= nome44.substring(0,1);
				    	  
				      } else {
				    	  
				    	   c4= nome44.substring(0,2);

			  
				      }
				      
				      
				      Utils.print("SOTTOSTRINGA CARATTERE1 ----------------"+c1);
				      Utils.print("SOTTOSTRINGA CARATTERE2 ----------------"+c2);
				      Utils.print("SOTTOSTRINGA CARATTERE3 ----------------"+c3);
				      Utils.print("SOTTOSTRINGA CARATTER42 ----------------"+c4);

				      if (c1.equals("S")) {
				    	  elemento.setHasSubject(2);
						  elemento = prelevaNome(c1,elemento, nome14);

				      } else if (c2.equals("S")){
				    	  elemento.setHasSubject(2);
						  elemento = prelevaNome(c2,elemento, nome24);

				      } else if (c3.equals("S")){
				    	  elemento.setHasSubject(2);
						  elemento = prelevaNome(c3,elemento, nome34);

				      } else if (c4.equals("S")){
				    	  elemento.setHasSubject(2);
						  elemento = prelevaNome(c4,elemento, nome44);

				      } 
				      
				      if (c1.equals("O")) {
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c1,elemento, nome14);

				      } else if (c2.equals("O")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c2,elemento, nome24);

				      } else if (c3.equals("O")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c3,elemento, nome34);

				      } else if (c4.equals("O")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c4,elemento, nome44);

				      } 
				      
				      
				      if (c1.equals("CS")) {
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c1,elemento, nome14);

				      } else if (c2.equals("CS")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c2,elemento, nome24);

				      } else if (c3.equals("CS")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c3,elemento, nome34);

				      } else if (c4.equals("CS")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c4,elemento, nome44);

				      } 
				      
				      
				      if (c1.equals("CO")) {
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c1,elemento, nome14);

				      } else if (c2.equals("CO")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c2,elemento, nome24);

				      } else if (c3.equals("CO")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c3,elemento, nome34);

				      } else if (c4.equals("CO")){
				    	  elemento.setHasObserver(2);
						  elemento = prelevaNome(c4,elemento, nome44);

				      } 
				      
				     /////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-CS (viceversa) oppure O-CO (viceversa) 
				      
				      if((c1.equals("S")) && (c2.equals("CS"))) {
				    	  
				    	  elemento.setControlloSubRel(true);
				      }
				      if(c1.equals("CS") && (c2.equals("S"))) {  elemento.setControlloSubRel(true);}
		 		      
		              if((c1.equals("O")) && (c2.equals("CO"))) {
				    	  
				    	  elemento.setControlloObsRel(true);;
				      }
				      if(c1.equals("CO") && (c2.equals("O"))) {  elemento.setControlloObsRel(true);}
				      
				      
				      if((c1.equals("S")) && (c3.equals("CS"))) {
				    	  
				    	  elemento.setControlloSubRel(true);
				      }
				      if(c1.equals("CS") && (c3.equals("S"))) {  elemento.setControlloSubRel(true);}
	                
				      if((c1.equals("O")) && (c3.equals("CO"))) {
				    	  
				    	  elemento.setControlloObsRel(true);;
				      }
				      if(c1.equals("CO") && (c3.equals("O"))) {  elemento.setControlloObsRel(true);}
				      
				      
	                  if((c2.equals("S")) && (c3.equals("CS"))) {
				    	  
				    	  elemento.setControlloSubRel(true);
				      }
				      if(c2.equals("CS") && (c3.equals("S"))) {  elemento.setControlloSubRel(true);}
	                
				      if((c2.equals("O")) && (c3.equals("CO"))) {
				    	  
				    	  elemento.setControlloObsRel(true);;
				      }
				      if(c2.equals("CO") && (c3.equals("O"))) {  elemento.setControlloObsRel(true);}
		 		      
				      
                      if((c1.equals("S")) && (c4.equals("CS"))) {
				    	  
				    	  elemento.setControlloSubRel(true);
				      }
				      if(c1.equals("CS") && (c4.equals("S"))) {  elemento.setControlloSubRel(true);}
	                
				      if((c1.equals("O")) && (c4.equals("CO"))) {
				    	  
				    	  elemento.setControlloObsRel(true);;
				      }
				      if(c1.equals("CO") && (c4.equals("O"))) {  elemento.setControlloObsRel(true);}
				      
                      
				      if((c2.equals("S")) && (c4.equals("CS"))) {
				    	  
				    	  elemento.setControlloSubRel(true);
				      }
				      if(c2.equals("CS") && (c4.equals("S"))) {  elemento.setControlloSubRel(true);}
	                
				      if((c2.equals("O")) && (c4.equals("CO"))) {
				    	  
				    	  elemento.setControlloObsRel(true);;
				      }
				      if(c2.equals("CO") && (c4.equals("O"))) {  elemento.setControlloObsRel(true);}
				      
                      
				      if((c3.equals("S")) && (c4.equals("CS"))) {
				    	  
				    	  elemento.setControlloSubRel(true);
				      }
				      if(c3.equals("CS") && (c4.equals("S"))) {  elemento.setControlloSubRel(true);}
	                
				      if((c3.equals("O")) && (c4.equals("CO"))) {
				    	  
				    	  elemento.setControlloObsRel(true);;
				      }
				      if(c3.equals("CO") && (c4.equals("O"))) {  elemento.setControlloObsRel(true);}
		 		      
				      
				      if(elemento.getControlloSubRel()==false ) {
					    	 elemento.setSubjectsRelationship(3);
					     }
					     
					     if(elemento.getControlloObsRel()==false ) {
					    	 elemento.setObserversRelationship(3);
					     }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
					     
			 		     
////////////////////////////////// CONTROLLO SE PRESENTE LE RELAZIONI S-O (viceversa) oppure S-CO (viceversa) ////////////////////////////////////////
					     if((c1.equals("S")) && (c2.equals("O"))) {
					    	  
					    	 elemento.setControlloDipSub(true);
					    	 }
					      if(c1.equals("S") && (c2.equals("CO"))) {  elemento.setControlloDipSub(true);}
			 		      
			              if((c1.equals("O")) && (c2.equals("S"))) {
					    	  
			            	  elemento.setControlloDipSub(true);
			            	  }
					      if(c1.equals("CO") && (c2.equals("S"))) {  elemento.setControlloDipSub(true);}
					      
					      
					      if((c1.equals("S")) && (c3.equals("O"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					    	  }
					      if(c1.equals("S") && (c3.equals("CO"))) {  elemento.setControlloDipSub(true);}
		                
					      if((c1.equals("O")) && (c3.equals("S"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					    	  
					     }
					      if(c1.equals("CO") && (c3.equals("S"))) {  elemento.setControlloDipSub(true);}
					      
					      
		                  if((c2.equals("S")) && (c3.equals("O"))) {
					    	  
		                	  elemento.setControlloDipSub(true);
		                	  }
					      if(c2.equals("S") && (c3.equals("CO"))) {  elemento.setControlloDipSub(true);}
		                
					      if((c2.equals("O")) && (c3.equals("S"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					      }
					      if(c2.equals("CO") && (c3.equals("S"))) {  elemento.setControlloDipSub(true);}
			 		      
					      
	                      if((c1.equals("S")) && (c4.equals("O"))) {
					    	  
	                    	  elemento.setControlloDipSub(true);
					      }
					      if(c1.equals("S") && (c4.equals("CO"))) {  elemento.setControlloDipSub(true);}
		                
					      if((c1.equals("O")) && (c4.equals("S"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					      }
					      if(c1.equals("CO") && (c4.equals("S"))) {  elemento.setControlloDipSub(true);}
					      
	                      
					      if((c2.equals("S")) && (c4.equals("O"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					      }
					      if(c2.equals("S") && (c4.equals("CO"))) {  elemento.setControlloDipSub(true);}
		                
					      if((c2.equals("O")) && (c4.equals("S"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					      }
					      if(c2.equals("CO") && (c4.equals("S"))) {  elemento.setControlloDipSub(true);}
					      
	                      
					      if((c3.equals("S")) && (c4.equals("CO"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					      }
					      if(c3.equals("S") && (c4.equals("O"))) {  elemento.setControlloDipSub(true);}
		                
					      if((c3.equals("O")) && (c4.equals("S"))) {
					    	  
					    	  elemento.setControlloDipSub(true);
					      }
					      if(c3.equals("CO") && (c4.equals("S"))) {  elemento.setControlloDipSub(true);}
					      
					      
					  
			 		         
					     
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
						     
				 		     
//////////////////////////////////CONTROLLO SE PRESENTE LE RELAZIONI S-O (viceversa) oppure S-CO (viceversa) ////////////////////////////////////////
if((c1.equals("CS")) && (c2.equals("O"))) {

elemento.setControlloDipCSub(true);
}
if(c1.equals("CS") && (c2.equals("CO"))) {  elemento.setControlloDipCSub(true);}

if((c1.equals("O")) && (c2.equals("CS"))) {

elemento.setControlloDipCSub(true);
}
if(c1.equals("CO") && (c2.equals("CS"))) {  elemento.setControlloDipCSub(true);}


if((c1.equals("CS")) && (c3.equals("O"))) {

elemento.setControlloDipCSub(true);
}
if(c1.equals("CS") && (c3.equals("CO"))) {  elemento.setControlloDipCSub(true);}

if((c1.equals("O")) && (c3.equals("CS"))) {

elemento.setControlloDipCSub(true);

}
if(c1.equals("CO") && (c3.equals("CS"))) {  elemento.setControlloDipCSub(true);}


if((c2.equals("CS")) && (c3.equals("O"))) {

elemento.setControlloDipCSub(true);
}
if(c2.equals("CS") && (c3.equals("CO"))) {  elemento.setControlloDipCSub(true);}

if((c2.equals("O")) && (c3.equals("CS"))) {

elemento.setControlloDipCSub(true);
}
if(c2.equals("CO") && (c3.equals("CS"))) {  elemento.setControlloDipCSub(true);}


if((c1.equals("CS")) && (c4.equals("O"))) {

elemento.setControlloDipCSub(true);
}
if(c1.equals("CS") && (c4.equals("CO"))) {  elemento.setControlloDipCSub(true);}

if((c1.equals("O")) && (c4.equals("CS"))) {

elemento.setControlloDipCSub(true);
}
if(c1.equals("CO") && (c4.equals("CS"))) {  elemento.setControlloDipCSub(true);}


if((c2.equals("CS")) && (c4.equals("O"))) {

elemento.setControlloDipCSub(true);
}
if(c2.equals("CS") && (c4.equals("CO"))) {  elemento.setControlloDipCSub(true);}

if((c2.equals("O")) && (c4.equals("CS"))) {

elemento.setControlloDipCSub(true);
}
if(c2.equals("CO") && (c4.equals("CS"))) {  elemento.setControlloDipCSub(true);}


if((c3.equals("CS")) && (c4.equals("CO"))) {

elemento.setControlloDipCSub(true);
}
if(c3.equals("CS") && (c4.equals("O"))) {  elemento.setControlloDipCSub(true);}

if((c3.equals("O")) && (c4.equals("CS"))) {

elemento.setControlloDipCSub(true);
}
if(c3.equals("CO") && (c4.equals("CS"))) {  elemento.setControlloDipCSub(true);}




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	


				      

				      elemento.setNoc(count);
				      elemento.setClasses(riga.getClasse1()+","+riga.getClasse2()+","+riga.getClasse3()+","+riga.getClasse4());

 
			          
			break;
		default:
	}
		
		System.out.println(elemento.toString());
		return elemento;
		
		
	}
	
	
	public Feature3 prelevaNome (String carattere , Feature3 elementoLista ,String nome ) {
		
		if(carattere.equals("S")) {
			
			boolean bool=false;
			boolean bool2=true;
			String appoggio="";
			String appoggio2="";
			
			for(int i=0;i<nome.length();i++) {
				
				String singoloCarattere= nome.substring(i, i+1);
				if(singoloCarattere.equals("-")) {
					bool=true;
				} else {
					if (bool==true) {
					 appoggio=appoggio.concat(singoloCarattere);
					}
				}
				
			} // fineFor
			
			
			
            for(int i=0;i<appoggio.length();i++) {
				
				String singoloCarattere= appoggio.substring(i, i+1);
				if(singoloCarattere.equals(".")) {
					bool2=false;
				} else {
					if (bool2==true) {
					 appoggio2=appoggio2.concat(singoloCarattere);
					}
				}
				
			}
			
		 elementoLista.setNomeSubject(appoggio2);	
			
			
		}
		
		
	if(carattere.equals("O")) {
		boolean bool=false;
		boolean bool2=true;
		String appoggio="";
		String appoggio2="";
		
		for(int i=0;i<nome.length();i++) {
			
			String singoloCarattere= nome.substring(i, i+1);
			if(singoloCarattere.equals("-")) {
				bool=true;
			} else {
				if (bool==true) {
				 appoggio=appoggio.concat(singoloCarattere);
				}
			}
			
		} // fineFor
		
		
		
        for(int i=0;i<appoggio.length();i++) {
			
			String singoloCarattere= appoggio.substring(i, i+1);
			if(singoloCarattere.equals(".")) {
				bool2=false;
			} else {
				if (bool2==true) {
				 appoggio2=appoggio2.concat(singoloCarattere);
				}
			}
			
		}	
		
			
		 elementoLista.setNomeObserver(appoggio2);	
			
			
		}
	
	if(carattere.equals("CS")) {
		boolean bool=false;
		boolean bool2=true;
		String appoggio="";
		String appoggio2="";
		
		for(int i=0;i<nome.length();i++) {
			
			String singoloCarattere= nome.substring(i, i+1);
			if(singoloCarattere.equals("-")) {
				bool=true;
			} else {
				if (bool==true) {
				 appoggio=appoggio.concat(singoloCarattere);
				}
			}
			
		} // fineFor
		
		
		
        for(int i=0;i<appoggio.length();i++) {
			
			String singoloCarattere= appoggio.substring(i, i+1);
			if(singoloCarattere.equals(".")) {
				bool2=false;
			} else {
				if (bool2==true) {
				 appoggio2=appoggio2.concat(singoloCarattere);
				}
			}
			
		}	
		
			
		 elementoLista.setNomeConcreteSubject(appoggio2);	
			
			
		}
		
	if(carattere.equals("CO")) {
		boolean bool=false;
		boolean bool2=true;
		String appoggio="";
		String appoggio2="";
		
		for(int i=0;i<nome.length();i++) {
			
			String singoloCarattere= nome.substring(i, i+1);
			if(singoloCarattere.equals("-")) {
				bool=true;
			} else {
				if (bool==true) {
				 appoggio=appoggio.concat(singoloCarattere);
				}
			}
			
		} // fineFor
		
		
		
        for(int i=0;i<appoggio.length();i++) {
			
			String singoloCarattere= appoggio.substring(i, i+1);
			if(singoloCarattere.equals(".")) {
				bool2=false;
			} else {
				if (bool2==true) {
				 appoggio2=appoggio2.concat(singoloCarattere);
				}
			}
			
		}	
		
			
		 elementoLista.setNomeConcreteObserver(appoggio2);;	
			
			
		}
		
		
		
		
		
		return elementoLista;
		
	}
	

}
