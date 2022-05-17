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
		    	
		    	classe1 = ricercaRuolo(riga,"CC");
		    	
		    	if (!classe1.equals("") && classe1 != null) {
		    		//System.out.println("PROVA PROVA PROVA - Classe analizzata: " + classeAnalizzata + ", classe1: " + classe1.substring(0,classe1.length()-5) + " " + riga);
			    	if (classeAnalizzata.equals(classe1.substring(0,classe1.length()-5))) {
			    		//System.out.println("PROVA PROVA PROVA - ENTRATO - classe1:" + classe1);
			    		String classe2 = "";
			    		
			    		classe2 = ricercaRuolo(riga,"CI");
			    		
			    		ITypeBinding tempsuperclass = superclass;
			    		//Confronto estensione
			    		while (tempsuperclass != null && !tempsuperclass.getName().equals("Object")) {
			    			if (!classe2.equals("") && classe2 != null) {
			    				//System.out.println("PROVA PROVA PROVA PROVA PROVA - Superclasse: " + tempsuperclass.getName() + ", classe2: " + classe2.substring(0,classe2.length()-5));
				    			if (tempsuperclass.getName().equals(classe2.substring(0,classe2.length()-5))) {
				    				//System.out.println("OK ESTENSIONE");
				    				listaFeatureCommandInstances.get(i).setCommandRelationship(2);
				    			}
			    			}
			    			tempsuperclass = tempsuperclass.getSuperclass();
			    		}
			    		
			    		//Confronto implementazione
			    		ITypeBinding[] interfaces = binding.getInterfaces();
			    		for (ITypeBinding sInterface : interfaces) {
			    			if (!classe2.equals("") && classe2 != null) {
			    				if (sInterface.getName().equals(classe2.substring(0,classe2.length()-5))) {
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
		
	
	// Method Invocation - ExecuteRelationship
	@Override
	public boolean visit(MethodInvocation node) {
		
		if (node.resolveMethodBinding() != null) {
			String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "");
			String classeAnalizzata = getTypeDeclaration(node).getName().toString();
			//MethodDeclaration mnode = getMethodDeclaration(node);
			String istruzioneChiamata = node.toString().toLowerCase();
			
			for (int i=0;i<listaFeatureCommandInstances.size();i++) {
		    	
		    	FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
		    	
		    	if (riga.toString().contains(classeAnalizzata)) {
		    		String classe1 = "";
			    	
			    	classe1 = ricercaRuolo(riga,"CC");
			    	
			    	if (!classe1.equals("") && classe1 != null) {
			    		//System.out.println("PROVA PROVA PROVA - Classe analizzata: " +classeDichiarante + ", classe1: " + classe1.substring(0,classe1.length()-5) + " - " + riga);
				    	//if (classeDichiarante.equals(classe1.substring(0,classe1.length()-5))) {
				    		
				    		String classe2 = "";
				    		String classe3 = "";
				    		
				    		classe2 = ricercaRuolo(riga,"RE");
				    		
				    		//System.out.println("PROVA PROVA PROVA " + i + " EXECUTERELATIONSHIP - classe1: " + classe1 + ", classe2: " + classe2);
				    		
				    		if (classe2.equals(classeDichiarante + " - RE")) {
				    			//System.out.println("PROVA PROVA PROVA " + i + " EXECUTERELATIONSHIP - CLASSE2 DICHIARA IL METODO DI CLASSE1");
				    			listaFeatureCommandInstances.get(i).setExecuteRelationship(2);
				    		}
				    		
				    		classe2 = ricercaRuolo(riga,"IN");
				    		
				    		classe3 = ricercaRuolo(riga,"CL");
				    		
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
		
						    			listaFeatureCommandInstances.get(i).setInvokeMethod(2);
					    			}
					    		}
				    		}
					    		
				    	//}
			    	}
		    	}
		    	
			}
		}
			
		
		
		//mnode.toString() restituisce il metodo per intero dalla dichiarazione all'ultima parentesi graffa.
		//MethodDeclaration mnode = getMethodDeclaration(node);
		//Assignment anode = getAssignment(node);
		
		/*if (mnode != null) {
				IMethodBinding mbinding = mnode.resolveBinding(); 
				
				//mnode.resolveBinding().getDeclaringClass().getQualifiedName() restituisce il nome della classe che ha dichiarato il metodo in mnode.toString
				//node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "") restituisce il nome della classe che ha dichiarato il metodo in questo node
				
				//ricerca delle classi che dichiarano metodi invocati in un metodo execute() o actionPerformed(Action event e) di un possibile Concrete Command
				if (node.resolveMethodBinding() != null) {
					String nomeClasseDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", ""); 
					String primariga = mnode.toString().substring(0, mnode.toString().indexOf("{")).toLowerCase();
					
					boolean bool = false;
					if (mbinding != null && ((primariga.contains("execute") || primariga.contains("actionperformed")) && !primariga.contains("executequery") && !primariga.contains("abstract"))) { //IF il metodo è chiamato execute e non è astratto(?)
						//System.out.println(mnode.toString());
						for (int i=0;i<listaClassiInExecute.size();i++) { 
							if (nomeClasseDichiarante.equals(listaClassiInExecute.get(i))) {
								bool = true;
								break;
							}
						}
						if (bool == false && !mnode.resolveBinding().getDeclaringClass().getQualifiedName().equals("") && !mnode.resolveBinding().getDeclaringClass().getQualifiedName().equals(node.resolveMethodBinding().getDeclaringClass().getQualifiedName())) { //IF il nome del metodo non è già presente nella lista, aggiungilo
							listaClassiInExecute.add(nomeClasseDichiarante);
							System.out.println("ECCOMI ECCOMI ECCOMI - " + mnode.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "")
									+ " usa la classe " + nomeClasseDichiarante + " - ECCOMI ECCOMI ECCOMI");
						}
					}
					
				}
				
				
/*
					//Ricerca classe
					for(int i=0;i<arrayListTemp.size();i++) {
						String FQN = arrayListTemp.get(i).getFQNClass();
						if (mnode.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "").equals(FQN)) {
							arrayListTemp.get(i).setExecutesCommand(2);
						}
					}
*/
		return true;
		
		}


	@Override
	public void endVisit(MethodInvocation node) {
		// Utils.print(" ]MI");
	}
	
	// Field Declaration
	@Override
	public boolean visit(FieldDeclaration node) {

		return true;
	}
	
	
	@Override
	public void endVisit(FieldDeclaration node) {
		 //Utils.print(" ]FD (VISITOR2)");
	}
	
	// Field Declaration
	@Override
	public boolean visit(ArrayCreation node) {
		System.out.println("prova " + node.toString());
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
	
	private String ricercaRuolo(FeatureCommandInstances riga, String ruolo) {
		if (riga.getClass1().contains(" - " + ruolo)) {
    		return riga.getClass1();
    	}
    	else if (riga.getClass2().contains(" - " + ruolo)) {
    		return riga.getClass2();
    	}
    	else if (riga.getClass3().contains(" - " + ruolo)) {
    		return riga.getClass3();
    	}
    	else if (riga.getClass4().contains(" - " + ruolo)) {
    		return riga.getClass4();
    	}
    	else if (riga.getClass5().contains(" - " + ruolo)) {
    		return riga.getClass5();
    	}
    	else {
    		return "";
    	}
	}
	
}
