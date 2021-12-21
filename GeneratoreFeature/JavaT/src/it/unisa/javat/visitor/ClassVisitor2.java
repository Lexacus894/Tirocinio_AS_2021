package it.unisa.javat.visitor;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.core.nd.field.Field;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import dataset.Feature;
import dataset.Feature3;
import dataset.strutturaVariabile;
import it.unisa.javat.Utils;

public class ClassVisitor2 extends ASTVisitor {
	
	
	
  private static final int i = 0;

	CompilationUnit _compilation;
	Document _document;
	ASTRewrite _rewriter;
	Stack<Scope> _scope;
    ArrayList<Feature3> listaFeature3;

    String nomeClasseAnalizzata;
    ArrayList<String> elementiDichiaratiSubject = new ArrayList<String>();
    
	
	
	//verificare se la chiamata arriva dopo un changeState
	boolean ChangeState2=false;
	
	//verifica se è presente una dipendenza 
	boolean SubDip1;


	public ClassVisitor2(CompilationUnit compilation, Document document, ASTRewrite rewriter, ArrayList<Feature3> listaFeature) {
		_compilation = compilation;
		_document = document;
		_rewriter = rewriter;
		_scope = new Stack<Scope>();
		listaFeature3=listaFeature;

	} // fine Costruttore
	
	
	
	// Compilation Unit
		@Override
		public boolean visit(CompilationUnit node) {

			Utils.print("Ritorno qui");
			_scope.push(new Scope(ScopeType.COMPILATIONUNIT));
			Utils.print("(VISITOR2)[CU " + node.getClass().getSimpleName());
			

			return true;
		}

		@Override
		public void endVisit(CompilationUnit node) {
	     
			Utils.print(" ]CU(VISITOR2)");
			
			_scope.pop();
   
	       }
		
		
		
		
		@Override
		public boolean visit(TypeDeclaration node) {
			ITypeBinding binding = node.resolveBinding();
			if (binding == null) {
				Utils.print("[TD NOBIND]");
				return false;
			}
			
			
			nomeClasseAnalizzata= binding.getName();
			
			
			
			ITypeBinding superclass = binding.getSuperclass();
			
		     
		      
			Utils.print(" (VISITOR2) [TD" + printModifiers(binding.getModifiers()) + " INTERFACE " + node.getClass().getSimpleName() + " " + binding.getName());
			
		     for(int i=0;i<listaFeature3.size();i++) {
		    	
		    	Feature3 riga= listaFeature3.get(i);

		    	String concreteSubject = riga.getNomeConcreteSubject();
		 

		    	
		    	if(concreteSubject.equals(nomeClasseAnalizzata)) {
		    	
		    	if(riga.getControlloSubRel()==true) {
		   	    	 Utils.print("SONO ENTRATO BUONO 1 .............................................................");

		    	     String Subject= riga.getNomeSubject();
		    	     if (superclass!=null ) {
		    	    	 	//Utils.print("Eccomi......................................................................");
				        	//String nomeSuperclasse = superclass.getName();
				        	//if(nomeSuperclasse.equals(Subject)) {
		    	    	 	if(superclass.getName().contentEquals(Subject)) {

				        		
				        		riga.setSubjectsRelationship(1);
				        		
				        		listaFeature3.remove(i);   		
				        		listaFeature3.add(i, riga);
				        		
				        	}
			        	} else{     
			        		Utils.print("SONO ENTRATO BUONO 2 .............................................................");
			        		ITypeBinding[] interfaces = binding.getInterfaces();
			    	                	for (ITypeBinding sInterface : interfaces) {
			    			              //String interfaccia=  sInterface.getName();
			    			              //if (interfaccia.equals(Subject)) {
			    	                		if (sInterface.getName().equals(Subject)) {
			    	                			
			    			            	riga.setSubjectsRelationship(2);
			  				        		
			  				        		listaFeature3.remove(i);   		
			  				        		listaFeature3.add(i, riga);
			    			              
			    			              }// FINE IF INTERFACCIA
			    			              
			    		                   }//FINE FOR INTERFACCIA
			        		
			        	} // FINE ELSE (SE E' EXTEND O IMPLEMENT)
		    	} // FINE controllo se bisogna settare il campo SubjectRelationship
		    	}
		    	

		    	
		    	String concreteObserver = riga.getNomeConcreteObserver();
		    	

		    	if(concreteObserver.equals(nomeClasseAnalizzata)) {

		    	if(riga.getControlloObsRel()==true) {
		    			 Utils.print("SONO ENTRATO BUONO 3 .............................................................");
			    	     String Observer= riga.getNomeObserver();

			    	     if (superclass!=null ) {

					        	//String nomeSuperclasse = superclass.getName();

					        	//if(nomeSuperclasse.equals(Observer)) {
			    	    	 	if(superclass.getName().contentEquals(Observer)) {

					        		
					        		riga.setObserversRelationship(1);
					        		
					        		listaFeature3.remove(i);   		
					        		listaFeature3.add(i, riga);
					        		
					        	}
				        	} else{    Utils.print("SONO ENTRATO BUONO 4 .............................................................");


				        		           ITypeBinding[] interfaces = binding.getInterfaces();
				    	                	for (ITypeBinding sInterface : interfaces) {
				    			              //String interfaccia=  sInterface.getName();
				    			              //if (interfaccia.equals(Observer)) {
				    	                		if (sInterface.getName().contentEquals(Observer)) {
				    			            	
				    			            	riga.setObserversRelationship(2);
				  				        		
				  				        		listaFeature3.remove(i);   		
				  				        		listaFeature3.add(i, riga);
				    			              
				    			              }// FINE IF INTERFACCIA
				    			              
				    		                   }//FINE FOR INTERFACCIA
				        		
				        	} // FINE ELSE (SE E' EXTEND O IMPLEMENT)
			    	} // FINE controllo se bisogna settare il campo ObserverRelationship
		    	}
		     } //FINE for 
		     

		      
		return true ;
		      
		}
		
		
		@Override
		public void endVisit(TypeDeclaration node) {
			Utils.print("  ]TD (VISITOR2)");
		}
		 

		
		
		// Field Declaration
		@Override
		public boolean visit(FieldDeclaration node) {

			
			for(int i=0;i<listaFeature3.size();i++) {
		     Feature3 riga= listaFeature3.get(i);
		     
		     if(nomeClasseAnalizzata.equals(riga.getNomeConcreteObserver())) {

			 String Subject= riga.getNomeSubject();
			 if(Subject.equals(null)) { 
				 //non fai nulla 
			 } else { 
				 //vuol dire che il subject è presente nelle dichiarazioni quindi lo salviamo nell'array
				 // per andare poi a verificare se la classe accedde ad esso
				boolean verifica =cercaSottostringa(node.getType().toString(),Subject);
				  if(verifica==true) { //la variabile dichiarate è del tipo Subject
					  //elementiDichiaratiSubject.add();
					 String nomeField=trovaNomeField(node.toString());
					 elementiDichiaratiSubject.add(nomeField);			 
				  }
					 
		     }//fine else  
		     }	// fine controllo NomeClasseAnalizzata
			} //fine for
			
			
			
/////////////////////////	 SET CAMPO SubObsDependencies ///////////////////////////////////////
			
			for(int i=0 ; i<listaFeature3.size();i++) {
			     Feature3 riga= listaFeature3.get(i);
			     
			     
			     if(riga.getControlloDipSub()==true) {
			    	 
			    	 String Subject = riga.getNomeSubject();
			    	 if(nomeClasseAnalizzata.equals(Subject)) {
			    		 String Observer = riga.getNomeObserver();
			    		 String ConcreteObserver = riga.getNomeConcreteObserver();
						 boolean verifica =cercaSottostringa(node.getType().toString(),Observer);
						 boolean verifica2 =cercaSottostringa(node.getType().toString(),ConcreteObserver);
						 if(verifica==true) {
							 if(SubDip1==true) {
								 
								    riga.setSubObsDepedencies(3);


					        		listaFeature3.remove(i);   		
					        		listaFeature3.add(i, riga);
								 
							 }else {
								 
							    riga.setSubObsDepedencies(1);
				        		
				        		listaFeature3.remove(i);   		
				        		listaFeature3.add(i, riga);
							 }
							 
						 } //fine if Verifica
						 if(verifica2==true) {
							 if(SubDip1==true) {
								 
								    riga.setSubObsDepedencies(3);

					        		listaFeature3.remove(i);   		
					        		listaFeature3.add(i, riga);
								 
							 }else {
								 
							    
							    riga.setSubObsDepedencies(2);
				        		
				        		listaFeature3.remove(i);   		
				        		listaFeature3.add(i, riga);
							 }
							 
						 } //fine if Verifica2
						 

			    	 }
			    	 
			     }

			}
			
			
/////////////////////////	 SET CAMPO ConcreteSubObsDependencies        ///////////////////////////////////////////////////////////
			
			for(int i=0 ; i<listaFeature3.size();i++) {
			     Feature3 riga= listaFeature3.get(i);
			     
			     
			     if(riga.getControlloDipCSub()==true) {
			    	 
			    	 String ConcreteSubject = riga.getNomeConcreteSubject();
			    	 if(nomeClasseAnalizzata.equals(ConcreteSubject)) {
			    		 String Observer = riga.getNomeObserver();
			    		 String ConcreteObserver = riga.getNomeConcreteObserver();
						 boolean verifica =cercaSottostringa(node.getType().toString(),Observer);
						 boolean verifica2 =cercaSottostringa(node.getType().toString(),ConcreteObserver);
						 if(verifica==true) {
							 if(SubDip1==true) {
								 
								    riga.setCSubObsDependencies(3);

					        		listaFeature3.remove(i);   		
					        		listaFeature3.add(i, riga);

								 
							 }else {
								 
							    riga.setCSubObsDependencies(1);

				        		
				        		listaFeature3.remove(i);   		
				        		listaFeature3.add(i, riga);
							 }
							 
						 } //fine if Verifica
						 if(verifica2==true) {
							 if(SubDip1==true) {
								 
								    riga.setCSubObsDependencies(3);

					        		listaFeature3.remove(i);   		
					        		listaFeature3.add(i, riga);

								 
							 }else {
								 
							    
								    riga.setCSubObsDependencies(2);

				        		
				        		listaFeature3.remove(i);   		
				        		listaFeature3.add(i, riga);
							 }
							 
						 } //fine if Verifica2
						 

			    	 }
			    	 
			     }

			}
			
			
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			
				
			Utils.print("    (VISITOR2)[FD " + node.getClass().getSimpleName() + " " + node.getType().toString() + " ]");
				
			return true;
		}
		
		
		@Override
		public void endVisit(FieldDeclaration node) {
			 Utils.print(" ]FD (VISITOR2)");
		}
		
		
		
		
		
		public boolean cercaSottostringa(String classe , String sottoStringa) {
			
			boolean bool=false;
			

			int max = classe.length() - sottoStringa.length();
			
			
			 test:
				    for (int i = 0; i <= max; i++) {
				      int n = sottoStringa.length();
				      int j = i;
				      int k = 0;
				      while (n-- != 0) {
				        if (classe.charAt(j++) != sottoStringa.charAt(k++)) {
				          continue test;
				        }
				      }

				      // a questo punto è stata effettuata una ricerca
				      // sarà possibile produrre un output			
				      bool = true;
				      break test;
				    }
			return bool;
		}
		
		
		
		public String trovaNomeField (String fieldDeclaration) {
		     
			  //node.fieldDeclaration senza Virgola
			  String nomeField= fieldDeclaration.substring(0,(fieldDeclaration.length()-1));
			 
			  int  max = nomeField.length();
			  ArrayList<String> caratteri = new ArrayList<String>();
			  while((nomeField.equals(" "))==false && (max>0)) {
				  String carattere = nomeField.substring(max-1, max);
				  caratteri.add(carattere);
				  max--;
				  }
			  
			 String appoggio="";
			 
			 for(int i=caratteri.size()-1;i>=0;i--) {
				 
				 String c = caratteri.get(i);
				 appoggio=appoggio.concat(c);
				 
			 }
			
			
			return appoggio;
		}
		
		
		@Override
		public boolean visit(FieldAccess node) {
			Utils.print(" (VISITOR2) [fieldACCESS " + node.getClass().getSimpleName() + "   " + node.toString());
			

			for(int i=0;i<listaFeature3.size();i++) {
		     Feature3 riga= listaFeature3.get(i);
		     
		     if(nomeClasseAnalizzata.equals(riga.getNomeConcreteObserver())) {
		     
		      
		    	 for(int y=0;y<elementiDichiaratiSubject.size();y++) {
		    	  String elemento =  elementiDichiaratiSubject.get(y);
		    	  boolean verifica = cercaSottostringa(node.toString(),elemento);
		    	  if(verifica==true) {
		    		  
		    		  riga.setCObsAccessSubject(2);
		        		
		              listaFeature3.remove(i);   		
		        	  listaFeature3.add(i, riga);
		        	  
		        	 
		    	  }
		    		  
		    	  }
		     }
		     }
			
			return true;
		}
		
		@Override
		public void endVisit(FieldAccess node) {
			Utils.print("    ]fieldACCESS (VISITOR2)");
			
		}
		
		
		

		private String printModifiers(int mod) {
			String modifier = "";
			if (Modifier.isPublic(mod))
				modifier += " PUBLIC";
			if (Modifier.isProtected(mod))
				modifier += " PROTECTED";
			if (Modifier.isPrivate(mod))
				modifier += " PRIVATE";
			if (Modifier.isStatic(mod))
				modifier += " STATIC";
			if (Modifier.isInterface(mod))
				modifier += " INTERFACE";
			if (Modifier.isAbstract(mod))
				modifier += " ABSTRACT";
			return modifier;
		}
		
	
	
}
