package it.unisa.javat.visitor;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
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
import dataset.FeatureCommandInstances;
import dataset.strutturaVariabile;
import it.unisa.javat.Utils;

public class ClassVisitorCommandInstances extends ASTVisitor {
	
	//private static final int i = 0;

	CompilationUnit _compilation;
	Document _document;
	ASTRewrite _rewriter;
	Stack<Scope> _scope;
    ArrayList<FeatureCommandInstances> listaFeatureCommandInstances;
    static ArrayList<String> prova = new ArrayList<String>();;
    //String classeAnalizzata;
    


	public ClassVisitorCommandInstances(CompilationUnit compilation, Document document, ASTRewrite rewriter, ArrayList<FeatureCommandInstances> listaFeature) {
		_compilation = compilation;
		_document = document;
		_rewriter = rewriter;
		_scope = new Stack<Scope>();
		listaFeatureCommandInstances = listaFeature;
		

	}
	
	
	
	// Compilation Unit
	@Override
	public boolean visit(CompilationUnit node) {

		//Utils.print("Ritorno qui");
		_scope.push(new Scope(ScopeType.COMPILATIONUNIT));
		//Utils.print("(VISITOR2)[CU " + node.getClass().getSimpleName());
		

		return true;
	}

	@Override
	public void endVisit(CompilationUnit node) {
     
		//Utils.print(" ]CU(VISITOR2)");
			
			_scope.pop();
   
	       }
		
		
		
		
		@Override
		public boolean visit(TypeDeclaration node) {
			ITypeBinding binding = node.resolveBinding();
			if (binding == null) {
				//Utils.print("[TD NOBIND]");
			return false;
		}
		
		String classeAnalizzata = binding.getName();
		ITypeBinding superclass = binding.getSuperclass();
		
	    for (int i=0;i<listaFeatureCommandInstances.size();i++) {
	    	
	    	FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
	    	
	    	if (riga.toString().contains(classeAnalizzata)) {
	    		String classe1 = "";
		    	
		    	classe1 = ricercaClasse(riga,"CC");
		    	
		    	if (!classe1.equals("") && classe1 != null) {
		    		//System.out.println("PROVA PROVA PROVA - Classe analizzata: " + classeAnalizzata + ", classe1: " + classe1.substring(0,classe1.length()-5) + " " + riga);
			    	if (classeAnalizzata.equals(classe1)) {
			    		//System.out.println("PROVA PROVA PROVA - ENTRATO - classe1:" + classe1);
			    		String classe2 = "";
			    		
			    		classe2 = ricercaClasse(riga,"CI");
			    		
			    		ITypeBinding tempsuperclass = superclass;
			    		//COMMANDRELATIONSHIP 2 - CONTROLLA SE LA CLASSE CONCRETECOMMAND HA COME GENITORE O COME ANTENATO L'INTERFACCIA COMMAND
			    		while (tempsuperclass != null && !tempsuperclass.getName().equals("Object")) {
			    			if (!classe2.equals("") && classe2 != null) {
			    				//System.out.println("PROVA PROVA PROVA PROVA PROVA - Superclasse: " + tempsuperclass.getName() + ", classe2: " + classe2.substring(0,classe2.length()-5));
				    			if (tempsuperclass.getName().equals(classe2)) {
				    				//System.out.println("OK ESTENSIONE");
				    				listaFeatureCommandInstances.get(i).setCommandRelationship(2);
				    			}
			    			}
			    			tempsuperclass = tempsuperclass.getSuperclass();
			    		}
			    		
			    		//COMMANDRELATIONSHIP 3 - CONTROLLA SE LA CLASSE CONCRETECOMMAND IMPLEMENTA L'INTERFACCIA COMMAND
			    		ITypeBinding[] interfaces = binding.getInterfaces();
			    		for (ITypeBinding sInterface : interfaces) {
			    			if (!classe2.equals("") && classe2 != null) {
			    				if (sInterface.getName().equals(classe2)) {
				    				//System.out.println("OK IMPLEMENTAZIONE");
				    				listaFeatureCommandInstances.get(i).setCommandRelationship(3);
				    			}
			    			}
			    			//Utils.print("   [IMP" + printModifiers(sInterface.getModifiers()) + " " + sInterface.getName() + " ]");
			    		}
			    	}
		    	}
	    	}
	    	
	    		
	    }		
	          
	return true ;
	      
	}
		
	@Override
	public void endVisit(TypeDeclaration node) {
		//Utils.print("  ]TD (VISITOR2)");
	}
	
	
	@Override
	public boolean visit(MethodDeclaration node) {
		/*for (int i=0;i<listaFeatureCommandInstances.size();i++) {
			FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
			
			String classe1 = ricercaRuolo(riga,"CC");
			
			String classe2 = ricercaRuolo(riga,"IN");
			
			if (!classe1.equals("") && classe1 != null && !classe2.equals("") && classe2 != null) {
				//System.out.println(node.toString());
				if (node.toString().contains("new " + classe1.substring(0,classe1.length()-5))) {
					//prova.add(classe1);
					if (node.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "").equals(classe2.substring(0,classe2.length()-5))) {
						listaFeatureCommandInstances.get(i).setInvokeMethod(2);
					}
					//System.out.println("AGGIUNTO " + node.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "") + " ALL'ARRAY - " + node.toString());
				}
			}
		}*/
	    	
	    	
		return true;
	}
	
	@Override
	public void endVisit(MethodDeclaration node) {
	}
	
	@Override
	public boolean visit(ClassInstanceCreation node) {
		
		return true;
	}
	
	@Override
	public void endVisit(ClassInstanceCreation node) {
	}
	
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		String dichiarazione = node.toString().toLowerCase();
		if (dichiarazione.contains("new")) {
			String classeAnalizzata = getTypeDeclaration(node).getName().toString();
			
			for (int i=0;i<listaFeatureCommandInstances.size();i++) {
				FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
				
				if (riga.toString().contains(classeAnalizzata + " - IN")) {
					String classe2 = ricercaClasse(riga, "CC");
					
					//INVOKEMETHOD = 2 - CONTROLLA SE L'INVOKER ISTANZIA IL CONCRETECOMMAND OLTRE AD ESEGUIRLO
					for(int j=0;j<node.fragments().size();j++) {
						if (dichiarazione.contains("new " + classe2.toLowerCase())) {
							if (getMethodDeclaration(node)!=null) {
								if (getMethodDeclaration(node).toString().contains(node.fragments().get(j).toString().substring(0,node.fragments().get(j).toString().indexOf("=")) + ".execute")) {
									System.out.println(getMethodDeclaration(node).toString());
									listaFeatureCommandInstances.get(i).setInvokeMethod(2);
								}
							}
						}
					}
					
				}
			}
		}

		return true;

	}
	
	@Override
	public void endVisit(VariableDeclarationStatement node) {
	}
	
	
	// Method Invocation - InvokeMethod
	@Override
	public boolean visit(MethodInvocation node) {
		String istruzioneChiamata = node.toString().toLowerCase();
		
		if (getTypeDeclaration(node) != null) {
			String classeAnalizzata = getTypeDeclaration(node).getName().toString();
			
			for (int i=0;i<listaFeatureCommandInstances.size();i++) {
				FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
				
				//INVOKE METHOD = 3/4/5
				if (riga.toString().contains(classeAnalizzata + " - CL")) {
					
					String classe1 = ricercaClasse(riga,"CC");
					String classe2 = ricercaClasse(riga,"IN");
					
					if (!classe1.equals("") && classe1 != null) {
						//INVOKE METHOD = 4 - CONTROLLA SE IL CLIENT CONTIENE UNA RIGA CHE ISTANZIA CONCRETECOMMAND E LO AGGIUNGE AD UN INVOKER TRAMITE IL METODO ADD DI QUEST'ULTIMO
						if (istruzioneChiamata.contains(".add") && istruzioneChiamata.contains(classe1.toLowerCase())) {
							if (node.resolveMethodBinding() != null) {
								String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName();
								if (classeDichiarante.contains("javax.swing") || classeDichiarante.contains("java.awt")) {
									listaFeatureCommandInstances.get(i).setInvokeMethod(5);
								}
								else if (!classe2.equals("") && classe2 != null) {
									if (classeDichiarante.replaceAll(".+\\.", "").equals(classe2)) {
										listaFeatureCommandInstances.get(i).setInvokeMethod(4);
									}
								}
								
							}	
						}
						
						//INVOKE METHOD = 3 - CONTROLLA SE IL CLIENT CONTIENE UNA RIGA CHE ISTANZIA SIA INVOKER CHE CONCRETECOMMAND
						else if (!classe2.equals("") && classe2 != null) {
							if (istruzioneChiamata.contains("new " + classe1) && istruzioneChiamata.contains("new " + classe2) /*&& listaFeatureCommandInstances.get(i).getInvokeMethod()==1*/) {
								listaFeatureCommandInstances.get(i).setInvokeMethod(3);
							}
						}
					}
				}	
				
				//INVOKEMETHOD = 2 - CONTROLLA SE L'INVOKER CONTIENE NEW COMMAND.execute
				else if (riga.toString().contains(classeAnalizzata + " - IN")) {
					String classe1 = ricercaClasse(riga,"CC");
					if (!classe1.equals("") && classe1 != null) {
						if (istruzioneChiamata.contains("new " + classe1) && istruzioneChiamata.contains(".execute") && listaFeatureCommandInstances.get(i).getInvokeMethod()!=3) {
							listaFeatureCommandInstances.get(i).setInvokeMethod(2);
						}
					}
				}
				
				//CCRERELATIONSHIP = 2 - CONTROLLA SE IL METODO INVOCATO DALLA CLASSE CONCRETECOMMAND VIENE DICHIARATO DALLA CLASSE RECEIVER
				if (riga.toString().contains(classeAnalizzata + " - CC")) {
					String classe1 = ricercaClasse(riga,"RE");
					if (!classe1.equals("") && classe1 != null) {
						if (node.resolveMethodBinding() != null) {
							String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "");
							if (classeDichiarante.equals(classe1)) {
								listaFeatureCommandInstances.get(i).setCCRERelationship(2);
							}
						}	
					}
				}
			}
		}
			
		
		//ArrayList<String> tempprova = prova;
		
		
		/*if (node.resolveMethodBinding() != null) {
			String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "");
			if (getTypeDeclaration(node) != null) {
				String classeAnalizzata = getTypeDeclaration(node).getName().toString();
				//MethodDeclaration mnode = getMethodDeclaration(node);
				String istruzioneChiamata = node.toString().toLowerCase();
				
				for (int i=0;i<listaFeatureCommandInstances.size();i++) {
			    	
			    	FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
			    	
			    	//CCRERELATIONSHIP - INVOKEMETHOD
			    	if (riga.toString().contains(classeAnalizzata + " - CC")) {
			    		String classe1 = ricercaRuolo(riga,"CC");;
				    	
				    	if (!classe1.equals("") && classe1 != null) {
				    		//System.out.println("PROVA PROVA PROVA - Classe analizzata: " +classeDichiarante + ", classe1: " + classe1.substring(0,classe1.length()-5) + " - " + riga);
					    	//if (classeDichiarante.equals(classe1.substring(0,classe1.length()-5))) {
					    		
					    		String classe2 = ricercaRuolo(riga,"RE");
					    		
					    		if (classe2.equals(classeDichiarante + " - RE")) {
					    			//System.out.println("PROVA PROVA PROVA " + i + " CCRERELATIONSHIP - CLASSE2 DICHIARA IL METODO DI CLASSE1");
					    			listaFeatureCommandInstances.get(i).setCCRERelationship(2);
					    		}
					    		
					    		classe2 = ricercaRuolo(riga,"IN");
					    		String classe3 = ricercaRuolo(riga,"CL");
					    		
					    		if (!classe2.equals("") && classe2 != null) {
						    		//System.out.println("PROVA PROVA PROVA " + i + " INVOKEMETHOD - classe1: " + classe1.substring(0,classe1.length()-5).toLowerCase() + ", classe2: " + classe2.substring(0,classe2.length()-5).toLowerCase() + " classeDichiarante: " + classeDichiarante + " istruzione: " + istruzioneChiamata);
					    			
						    		if (!classe3.equals("") && classe3 != null) {
						    			if (istruzioneChiamata.contains("new") && istruzioneChiamata.contains(classe1.substring(0,classe1.length()-5).toLowerCase()) && classeDichiarante.equals(classe2.substring(0,classe2.length()-5))) {
							    			System.out.println("PROVA PROVA PROVA " + i + " INVOKEMETHOD CLIENT ISTANZIA SIA " + classe1 + " CHE " + classe2 + " istruzione: " + istruzioneChiamata);
							    			
							    			listaFeatureCommandInstances.get(i).setInvokeMethod(3);
							    		}	
					    			}
					    			else if (istruzioneChiamata.contains("new " + classe1.substring(0,classe1.length()-5).toLowerCase())) {
						    			if (classeAnalizzata.equals(classe3.substring(0,classe3.length()-5))) {
						    				System.out.println("PROVA PROVA PROVA " + i + " INVOKEMETHOD " + classeAnalizzata + " ISTANZIA SOLO CLASSE1 istruzione: " + istruzioneChiamata);
								    		//System.out.println("PROVA PROVA PROVA " + i + " INVOKEMETHOD - classe1: " + classe1.substring(0,classe1.length()-5).toLowerCase() + ", classe2: " + classe1.substring(0,classe1.length()-5).toLowerCase() + " classeAnalizzata2: " + classeAnalizzata + " istruzione: " + istruzioneChiamata);
			
							    			listaFeatureCommandInstances.get(i).setInvokeMethod(3);
						    			}
						    		}
						    		
						    		else if (istruzioneChiamata.contains("new " + classe1.substring(0,classe1.length()-5).toLowerCase())) {
						    			if (classeAnalizzata.equals(classe2.substring(0,classe2.length()-5))) {
						    				listaFeatureCommandInstances.get(i).setInvokeMethod(2);
						    			}
						    		}
						    		else {
						    			
						    		}
					    		}
						    		
					    	//}
				    	}
			    	}
			    	//EXECUTES WITH CONTEXT
			    	else if (riga.toString().contains(classeAnalizzata + " - IN")) {
			    		
			    		
		    				//}
		    				
		    			}
			    		
			    		if (istruzioneChiamata.contains(".execute")) {
			    			if (node.arguments().size()>0) {
			    				boolean flag = false;
				    			for (int j = 0;j<node.arguments().size();j++) {
				    				if (node.arguments().get(j).toString().toLowerCase() .contains("context")) {
				    					listaFeatureCommandInstances.get(i).setExecutesWithContext(3);
				    					flag = true;
				    					break;
				    				}
				    			}
				    			if (flag == false) {
				    				listaFeatureCommandInstances.get(i).setExecutesWithContext(2);
				    			}
			    			}
			    		}
			    		
			    	}
			    	
				}
			}
			
		}*/
			
		return true;
		
	}


	@Override
	public void endVisit(MethodInvocation node) {
		// Utils.print(" ]MI");
	}
	
	// Field Declaration
	@Override
	public boolean visit(FieldDeclaration node) {
		if (getTypeDeclaration(node) != null) {
			String classeAnalizzata = getTypeDeclaration(node).getName().toString();
			
			for (int i=0;i<listaFeatureCommandInstances.size();i++) {
				FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
				
				if (riga.toString().contains(classeAnalizzata)) {
					//System.out.println(node.toString());
					if (node.getType().toString().contains("ArrayList") || node.getType().toString().contains("Map")) {
						if (node.toString().toLowerCase().contains("command")) {
							System.out.println("CONTIENE ARRAY E COMMAND " + node.getType().toString());
							//listaFeatureCommandInstances.get(i).setUsesCommandList(3);
						}
						else {
							System.out.println("CONTIENE SOLO ARRAY " + node.getType().toString());
							//listaFeatureCommandInstances.get(i).setUsesCommandList(2);
						}
					}
				}		
			}
		
		}

		return true;
	}
	
	
	@Override
	public void endVisit(FieldDeclaration node) {
		 //Utils.print(" ]FD (VISITOR2)");
	}
	
	// Field Declaration
	@Override
	public boolean visit(ArrayCreation node) {
		//System.out.println("prova " + node.toString());
		return true;
	}
	
	
	@Override
	public void endVisit(ArrayCreation node) {
		 //Utils.print(" ]FD (VISITOR2)");
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		
		return true;
	}
	
	@Override
	public void endVisit(FieldAccess node) {
		//Utils.print("    ]fieldACCESS (VISITOR2)");
		
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
		
	private MethodDeclaration getMethodDeclaration(ASTNode node) {
		ASTNode pnode = node;
		while (pnode != null && pnode.getNodeType() != ASTNode.METHOD_DECLARATION) {
			pnode = pnode.getParent();
		}
		
		return (MethodDeclaration) pnode;
		
	}
	
	private TypeDeclaration getTypeDeclaration(ASTNode node) {
		ASTNode pnode = node;
		while (pnode != null && pnode.getNodeType() != ASTNode.TYPE_DECLARATION) {
			pnode = pnode.getParent();
		}

		return (TypeDeclaration) pnode;
	}
	
	private MethodInvocation getMethodInvocation(ASTNode node) {
		ASTNode pnode = node;
		while (pnode != null && pnode.getNodeType() != ASTNode.METHOD_INVOCATION) {
			pnode = pnode.getParent();
		}

		return (MethodInvocation) pnode;
	}
	
	private String ricercaClasse(FeatureCommandInstances riga, String ruolo) {
		if (riga.getClass1().contains(" - " + ruolo)) {
    		return riga.getClass1().substring(0,riga.getClass1().length()-5);
    	}
    	else if (riga.getClass2().contains(" - " + ruolo)) {
    		return riga.getClass2().substring(0,riga.getClass2().length()-5);
    	}
    	else if (riga.getClass3().contains(" - " + ruolo)) {
    		return riga.getClass3().substring(0,riga.getClass3().length()-5);
    	}
    	else if (riga.getClass4().contains(" - " + ruolo)) {
    		return riga.getClass4().substring(0,riga.getClass4().length()-5);
    	}
    	else if (riga.getClass5().contains(" - " + ruolo)) {
    		return riga.getClass5().substring(0,riga.getClass5().length()-5);
    	}
    	else {
    		return "";
    	}
	}
	
}
