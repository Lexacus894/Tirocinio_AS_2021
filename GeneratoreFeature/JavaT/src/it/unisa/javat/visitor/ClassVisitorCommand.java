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
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import dataset.Feature;
import dataset.strutturaVariabile;
import it.unisa.javat.Utils;

public class ClassVisitorCommand extends ASTVisitor {
	
  private static final int i = 0;

	CompilationUnit _compilation;
	Document _document;
	ASTRewrite _rewriter;
	Stack<Scope> _scope;
	ArrayList<Feature> arrayListFeature;

	
	boolean obs=false , cobs=false; 
	boolean ccobs=false , cccobs=false; 


	Feature feat;

    boolean controllo=false;
	ArrayList<String> nomeVariabiliLista;
	boolean inStatement=false;
	boolean astrattoTrovato=false;
	ArrayList<strutturaVariabile> listaVariabili;
	Stack<String> cicloDopo;
	
	private ArrayList<String> listaClassiListener;
	
	
	//verificare se la chiamata arriva dopo un changeState
	boolean ChangeState2=false;


	public ClassVisitorCommand(CompilationUnit compilation, Document document, ASTRewrite rewriter, ArrayList<Feature> arrayListFeatureVisitor, String folder,String nomeProgetto) {
		_compilation = compilation;
		_document = document;
		_rewriter = rewriter;
		_scope = new Stack<Scope>();
		
		cicloDopo= new Stack<String>();
		this.arrayListFeature=arrayListFeatureVisitor;
		
	    listaClassiListener = new ArrayList<String>();
	    
		
	    feat = new Feature(folder,"c",1,1,1,0,0,0,1,1,0,0,1,1);
	    feat.setSfotwareName(nomeProgetto);


	}

	// Compilation Unit
	@Override
	public boolean visit(CompilationUnit node) {

		@SuppressWarnings("unchecked")
		List<Comment> comments = node.getCommentList();
		for (Comment c : comments) {
			try {
				String codeComment = _document.get(c.getStartPosition(), c.getLength());
				Utils.print("[CO " + codeComment + " ]CO");
			} catch (BadLocationException e) {
			}
		}
		_scope.push(new Scope(ScopeType.COMPILATIONUNIT));
		Utils.print("[CU " + node.getClass().getSimpleName());
		
		
		
		nomeVariabiliLista = new ArrayList<String>();
		listaVariabili=new ArrayList<strutturaVariabile>();

		return true;
	}

	@Override
	public void endVisit(CompilationUnit node) {
       for (strutturaVariabile var : listaVariabili) {
        	
        	System.out.println("NOME :  "+var.getNomeVariabile()+"    BOOL:"+var.getlocaleGlobale());
        }
		Utils.print(" ]CU");
		arrayListFeature.add(feat);
		
		
		_scope.pop();
	}

	// Package Declaration
	@Override
	public boolean visit(PackageDeclaration node) {
		IPackageBinding binding = node.resolveBinding();
		if (binding == null) {
			Utils.print("[PD NOBIND]");
			return false;
		}
		Utils.print("  [PD " + node.getClass().getSimpleName() + " " + binding.getName() + " ]");
		
		
		return true;
	}

	@Override
	public void endVisit(PackageDeclaration node) {
		// Utils.print(" ]PD");
	}

	// Import Declaration
	@Override
	public boolean visit(ImportDeclaration node) {
		Utils.print("  [ID " + node.getClass().getSimpleName() + " " + node.getName() + " ]");
		return true;
	}

	@Override
	public void endVisit(ImportDeclaration node) {
		// Utils.print(" ]ID");
	}

	// Type Declaration
	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding binding = node.resolveBinding();
		if (binding == null) {
			Utils.print("[TD NOBIND]");
			return false;
		}

	
		
		
		ITypeBinding superclass = binding.getSuperclass();
		
		
        
        
       ITypeBinding[] interfaccie= binding.getInterfaces() ;
       
     
       		
       
		
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


		if (binding.isInterface()) {
			feat.setClassType(3);
			Utils.print("  [TD" + printModifiers(binding.getModifiers()) + " INTERFACE " + node.getClass().getSimpleName() + " " + binding.getQualifiedName());
		} else
			feat.setClassType(1);
			Utils.print("  [TD" + printModifiers(binding.getModifiers()) + " " + node.getClass().getSimpleName() + " " + binding.getQualifiedName());

			
			String nomeClasse=binding.getQualifiedName();
			
			boolean bool;
			
			//CAMBIATO - TIPO DI CLASSE
			if(bool=cercaSottostringaClasse(nomeClasse,"Command")) {
				feat.setClassDeclarationKeyword(1);
			} 
			else if (bool=cercaSottostringaClasse(nomeClasse,"Receiver")) {
				feat.setClassDeclarationKeyword(2);
			}
			else if (bool=cercaSottostringaClasse(nomeClasse,"Invoker")) {
				feat.setClassDeclarationKeyword(3);
			} 
			else if (bool=cercaSottostringaClasse(nomeClasse,"Client")) {
				feat.setClassDeclarationKeyword(4);
			} 
			else {
				feat.setClassDeclarationKeyword(5);
			}

			if(Modifier.isAbstract(binding.getModifiers())) {
				feat.setClassType(2);
			}
			
			feat.setFQNClass(binding.getName()+".java");

			
		if (superclass != null) {
			feat.setHasSuperclass(2);
			Utils.print("   [EXT" + printModifiers(superclass.getModifiers()) + " " + superclass.getQualifiedName() + " ]");
		} else { feat.setHasSuperclass(1);}

		feat.setImplementsInterfaces(1);
		ITypeBinding[] interfaces = binding.getInterfaces();
		for (ITypeBinding sInterface : interfaces) {
			feat.setImplementsInterfaces(2);
			Utils.print("   [IMP" + printModifiers(sInterface.getModifiers()) + " " + sInterface.getName() + " ]");
		}

		return true;
	}

	@Override
	public void endVisit(TypeDeclaration node) {
		Utils.print("  ]TD");
	}

	// Enum Declaration
	@Override
	public boolean visit(EnumDeclaration node) {
		ChangeState2 = false;

		String stringa="NO";
		cicloDopo.push(stringa);
		ITypeBinding binding = node.resolveBinding();
		if (binding == null) {
			Utils.print("[ED NOBIND]");
			return false;
		}

		if (binding.isEnum()) {
			Utils.print("  [ED" + printModifiers(binding.getModifiers()) + " " + node.getClass().getSimpleName() + " " + binding.getQualifiedName());
		}

		return true;
	}

	@Override
	public void endVisit(EnumDeclaration node) {
		Utils.print("  ]ED");
	}

	// Anonymous Class Declaration
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		ChangeState2 = false;

		String stringa="NO";
		cicloDopo.push(stringa);
		ITypeBinding binding = node.resolveBinding();
		if (binding == null) {
			Utils.print("[AD NOBIND]");
			return false;
		}

		if (binding.isAnonymous()) {
			Utils.print("  [AD" + printModifiers(binding.getModifiers()) + " " + node.getClass().getSimpleName() + " " + binding.getBinaryName());
		}

		return true;
	}

	@Override
	public void endVisit(AnonymousClassDeclaration node) {
		Utils.print("  ]AD");
	}

	// Method Declaration
	@Override
	public boolean visit(MethodDeclaration node) {
		
		
		String stringa="NO";
		cicloDopo.push(stringa);
		IMethodBinding binding = node.resolveBinding();
		if (binding == null) {
			Utils.print("[MD NOBIND]");
			return false;
		}
		
		Utils.print("    [MD" + printModifiers(binding.getModifiers()) + " " + node.getClass().getSimpleName() + " " + binding.toString() + " ]");
		
		String nomeMetodo=binding.toString();
		
		boolean bool;
		if(controllo==false) {
			if(bool=cercaSottostringaClasse(nomeMetodo,"Subject")) {
				feat.setMethodDeclarationKeyword(1);
				controllo=true;
			} 
			else if (bool=cercaSottostringaClasse(nomeMetodo,"Observer")) {
				feat.setMethodDeclarationKeyword(2);
				controllo=true;
			} 
			else {
				feat.setMethodDeclarationKeyword(3);
			}
		} 

		if(Modifier.isAbstract(binding.getModifiers())) {
		feat.setClassType(2);
		
		}
		
		return true;
	}

	@Override
	public void endVisit(MethodDeclaration node) {
		// Utils.print(" ]MD");
	}

	// Field Declaration
	@Override
	public boolean visit(FieldDeclaration node) {
		
		ChangeState2 = false;

		
		String stringa="NO";
		cicloDopo.push(stringa);

		String nomeQuadre;
		
		Utils.print("    [FD " + node.getClass().getSimpleName() + " " + node.toString() + " ]");
		
		
		int u;
		String c1,appoggio1="";
		
		nomeQuadre= node.fragments().toString();
	
        u=nomeQuadre.length();
		
		//tolgo le quadre al nome della variabile
		String nomeVariabile1 = nomeQuadre.substring(1,u-1);
		
		
		for (int j=0;j<nomeVariabile1.length();j++) {
		      
		    c1=nomeVariabile1.substring(j, j+1);
		    
		    if(c1.equals("=")) {
		    	break;
		    } else {
                  appoggio1=appoggio1.concat(c1);
		    } //fine else (dentro ciclo)
		    
    		} // FINE CICLO 
		
		strutturaVariabile var = new strutturaVariabile(appoggio1,false);
		listaVariabili.add(var);
		
		
		//////////////////////////////////////////////////////////////////////////////////////
		String tipoVariabile = node.getType().toString();
			
		//LO SCOPO : PRELEVARE IL NOME DELLA VARIABILE SE ESSA E' DI TIPO COLLEZIONE 
		// ED AGGIUNGERLA ALL'AarrayList NomeVariabiliLista , UTILIZZATO SUCCESSIVAMENTE NELLE VISITE DEI FOR 
		// PER VERIFICARE SE C'E' UN ITERAZIONE  SULLA VARIABILE DI TIPO COLLEZIONE 
		
		if (cercaSottostringaClasse(tipoVariabile,"ArrayList") ||(cercaSottostringaClasse(tipoVariabile,"List")) ) {
    	
			nomeQuadre= node.fragments().toString();
			
			if (cercaSottostringaClasse(nomeQuadre,"new")) {
	    		//SE IL NOME DELLA VARIABILE HA IL NEW ALLORA BISOGNA CANCELLARE LE QUADRE E TAGLIARE LA STRINGA 
	    		// FINO AL CARATTARE =
	    		//ELIMINO LE PARTENTISE QUADRE
	    		int i = nomeQuadre.length();
	    		
	    		//tolgo le quadre al nome della variabile
	    		String nomeVariabile = nomeQuadre.substring(1,i-1);
				

	    		//BISOGNA TAGLIARE TUTTI I CARATTERI DELLA STRINGA DOPO L'UGUALE COMPRESO QUEST'ULTIMO
	    		String c;
	    		
	    		
	    		//isolo il nome della variabile con una concatenazione finchè
	    		//non incontra il carattere "="
	    		String appoggio="";
	    		for (int j=0;j<nomeVariabile.length();j++) {
      
    		    c=nomeVariabile.substring(j, j+1);
    		    
    		    if(c.equals("=")) {
    		    	break;
    		    } else {
                      appoggio=appoggio.concat(c);
    		    } //fine else (dentro ciclo)
    		    
	    		} // FINE CICLO 
	    		
    		    nomeVariabiliLista.add(appoggio);
    		    
	    		
	    		} else { //inizio else della condizion : se è presente la stringa "new"
	    			     //nella dichiarazione delle variabili	
	                     	int i = nomeQuadre.length();
	                     	String nomeVariabile = nomeQuadre.substring(1,i-1);
		                    nomeVariabiliLista.add(nomeVariabile);

	    		        } //fine else sottostringa "new"
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	

		String dichiarazione = node.toString();
		boolean verifica = cercaCollezione(dichiarazione);
		
		if(verifica) {
			feat.setCollectionVariables(2);
		}else feat.setCollectionVariables(1);
		
				
		return true;
	}

	@Override
	public void endVisit(FieldDeclaration node) {
		// Utils.print(" ]FD");
	}
	
	
	
	
	//@Override
	//public boolean visit(VariableDeclarationFragment node) {	
	//	Utils.print("    [VARDECL " +node.getName().toString());
	//	return true ;
	//}
	
	//@Override
	//public void endVisit(VariableDeclarationFragment node) {
		// Utils.print(" ]FD");
	//}
	
	

	// Method Invocation
	@Override
	public boolean visit(MethodInvocation node) {
		String stringa="NO";
		cicloDopo.push(stringa);
		
		String methodInvoker = null;
		String classInvoker = null;

		MethodDeclaration mnode = getMethodDeclaration(node);
		if (mnode != null) {
			IMethodBinding mbinding = mnode.resolveBinding();
			
			if (mbinding != null) {
				classInvoker = mbinding.getDeclaringClass().getQualifiedName();
			}

			methodInvoker = mnode.getName().getFullyQualifiedName();
		} else
			methodInvoker = "STATIC";

		if (classInvoker == null) {
			TypeDeclaration tnode = getTypeDeclaration(node);
			if (tnode != null) {
				ITypeBinding tbinding = tnode.resolveBinding();
				if (tbinding != null) {
					classInvoker = tbinding.getQualifiedName();
				}

			}
		}
		if(inStatement==false) {

		IMethodBinding binding = node.resolveMethodBinding();
		if (binding == null) {
			Utils.print("      [MI " + node.getClass().getSimpleName() + " " + classInvoker + " " + methodInvoker + " -> " + node.toString() + " NOBIND]");
			return false;
		}
		Utils.print("      [MI " + node.getClass().getSimpleName() + " " + classInvoker + " " + methodInvoker + " -> "
				+ binding.getDeclaringClass().getQualifiedName() + " " + node.toString() + " ]");
		} else if(inStatement==true){
		   //POICHE' inStatement E' INIZIALIZZATA A TRUE ALLORA VUOL DIRE CHE SIAMO
           // ALL'INTERNO DEL FOR STATEMENT PER LA COLLEZIONE
			
			IMethodBinding binding = node.resolveMethodBinding();
			if (binding == null) {
				Utils.print("      [MI " + node.getClass().getSimpleName() + " " + classInvoker + " " + methodInvoker + " -> " + node.toString() + " NOBIND]");
				return false;
			}
			
			IMethodBinding mbinding2 = node.resolveMethodBinding();
			Utils.print("METHOD BINDING :"+mbinding2);
			if (astrattoTrovato==false) {  // se il modificatore astratto è presente è inutile rifare i controlli
			int modificatore = mbinding2.getModifiers(); 
	        if (Modifier.isAbstract(modificatore)) {
	        	// il metodo invocato è di tipo astratto 
	        	feat.setSCMCallAbsMethod(2);
	        	astrattoTrovato=true;
	        } // fine if controllo se il modificatore del metodo è astratto
			} //fine if astrattoTrovato
			
				
			Utils.print("      [MI " + node.getClass().getSimpleName() + " " + classInvoker + " " + methodInvoker + " -> "
					+ binding.getDeclaringClass().getQualifiedName() + " " + node.toString() + " ]");
		}
		
		String istruzioneChiamata =node.toString();
		boolean bool=cercaAdd(istruzioneChiamata);
		
		if (bool) { feat.setAddListenerMethod(2); };
		
		istruzioneChiamata =node.toString();
	    bool=cercaRemove(istruzioneChiamata);
	    
	    if (bool) { feat.setRemoveListenerMethod(2);; };
	  
		
			return true;
	}
	
	

	@Override
	public void endVisit(MethodInvocation node) {
		// Utils.print(" ]MI");
	}
	
	
	
	@Override
	public boolean visit(ForStatement node){
		//Restituisce la stringa del costrutto For 
		Utils.print("[FOR " + node.getClass().getSimpleName() + "   " + node.toString());
		String ciclo = node.toString();
		
		String contenutoStack = cicloDopo.pop();
		
		Utils.print(" CONTENUTO STACK :"+contenutoStack);
		if(contenutoStack.equals("SI")) {
			
			 for (String nomeVariabile : nomeVariabiliLista) {
		        	//CONTROLLO SE NEL FOR STATEMENT MIGLIORATO E' 
		        	//UN ITERAZIONE SU DI UNA VARIABILE DI TIPO COLLEZIONE DICHIARATA PRECEDENTEMENTE 
		        	if (cercaSottostringaClasse(ciclo,nomeVariabile)) {
		        		 feat.setAfterChangeStateIterateOverList(2);
		        		 break;
		        	}		
		}
		}
		
		
		//CONTROLLO SE NEL FOR STATEMENT E' 
    	//UN ITERAZIONE SU DI UNA VARIABILE DI TIPO COLLEZIONE DICHIARATA PRECEDENTEMENTE 
        for (String nomeVariabile : nomeVariabiliLista) {
        	if (cercaSottostringaClasse(ciclo,nomeVariabile)) {
        		feat.setScanCollectionMethod(2);
        		inStatement=true;
        	}
        }
		return true;
	}
	
	
	
	
	@Override
	public void endVisit(ForStatement node) {
		inStatement=false;
		Utils.print(" ]FOR");
	}
	
	
	@Override
	public boolean visit(FieldAccess node) {
		Utils.print("     [fieldACCESS " + node.getClass().getSimpleName() + "   " + node.toString());
		String nomeAccesso=node.getName().toString();
		for(strutturaVariabile var: listaVariabili) {
			String contenuto= var.getNomeVariabile();
			if(nomeAccesso.equals(contenuto)) {
				feat.setChangeState(2);
				String stringa="SI";
				cicloDopo.push(stringa);
				ChangeState2 = true;
			} else {
				String stringa="NO";
				cicloDopo.push(stringa);
			}
		}
       // IVariableBinding f=  node.resolveFieldBinding();
		
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 
	
		
		
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		
		return true;
	}
	
	@Override
	public void endVisit(FieldAccess node) {
		Utils.print("    ]fieldACCESS");
		
	}
	

	@Override
	public boolean visit(EnhancedForStatement node) {
		
		ChangeState2 = false;

		
		//VISITA IN UN FOR STATEMENT MIGLIORATO 
			
		Utils.print("    [FOREN " + node.getClass().getSimpleName() + "   " + node.toString());
		

        String ciclo = node.toString();
        
		String contenutoStack = cicloDopo.pop();
	
		Utils.print(" CONTENUTO STACK :"+contenutoStack);
		if(contenutoStack.equals("SI")) {
			
			 for (String nomeVariabile : nomeVariabiliLista) {
		        	//CONTROLLO SE NEL FOR STATEMENT MIGLIORATO E' 
		        	//UN ITERAZIONE SU DI UNA VARIABILE DI TIPO COLLEZIONE DICHIARATA PRECEDENTEMENTE 
		        	if (cercaSottostringaClasse(ciclo,nomeVariabile)) {
		        		 feat.setAfterChangeStateIterateOverList(2);
		        	}
			
			feat.setAfterChangeStateIterateOverList(2);
		}
		
		
        for (String nomeVariabile : nomeVariabiliLista) {
        	//CONTROLLO SE NEL FOR STATEMENT MIGLIORATO E' 
        	//UN ITERAZIONE SU DI UNA VARIABILE DI TIPO COLLEZIONE DICHIARATA PRECEDENTEMENTE 
        	if (cercaSottostringaClasse(ciclo,nomeVariabile)) {
        		feat.setScanCollectionMethod(2);
        		inStatement=true;
        	}
        }
	   
	}
		return true;
	}
	
	@Override
	public void endVisit(EnhancedForStatement node) {
		inStatement=false;
		Utils.print("    ]FOREN");
	}
	
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		
		ChangeState2 = false;

		
		String stringa="NO";
		cicloDopo.push(stringa);
		
		Utils.print("[VAR " + node.getClass().getSimpleName() + "   " + node.toString());
        String nomeQuadre ,c ,appoggio="";
        int i;
        

        nomeQuadre= node.fragments().toString();
        
        i=nomeQuadre.length();
		
		//tolgo le quadre al nome della variabile
		String nomeVariabile = nomeQuadre.substring(1,i-1);
		
		
		for (int j=0;j<nomeVariabile.length();j++) {
		      
		    c=nomeVariabile.substring(j, j+1);
		    
		    if(c.equals("=")) {
		    	break;
		    } else {
                  appoggio=appoggio.concat(c);
		    } //fine else (dentro ciclo)
		    
    		} // FINE CICLO 
		
		strutturaVariabile var = new strutturaVariabile(appoggio,true);
		listaVariabili.add(var);
    		
//		strutturaVariabile var = new strutturaVariabile();

		return true;
	}
	
	@Override
	public void endVisit(VariableDeclarationStatement node) {
		Utils.print(" ]VAR");
	}
	
	
	@Override
	public boolean visit(ArrayAccess node) {
		
		ChangeState2 = false;

		
		String stringa="NO";
		cicloDopo.push(stringa);
		
		Utils.print("[ArrayACCESS " + node.getClass().getSimpleName() + "   " + node.toString());
		return true;
	}
	
	@Override
	public void endVisit(ArrayAccess node) {
		Utils.print(" ]ArrayACCESS");
	}
	
	
	

	// Class Instance Creation
	@Override
	public boolean visit(ClassInstanceCreation node) {
		
		ChangeState2 = false;

		
		String stringa="NO";
		cicloDopo.push(stringa);
		
		IMethodBinding binding = node.resolveConstructorBinding();
		if (binding == null) {
			Utils.print("[CIC NOBIND]");
			return false;
		}
		if (binding.isConstructor()) {
			if (this.isTypeDeclaration(binding.getDeclaringClass(), "javax.swing.JFrame")) {

				Utils.print("    [CIC " + node.getClass().getSimpleName() + " " + binding.getName() + " " + node + "]");
				ASTNode parent = node.getParent().getParent();
				if (parent instanceof VariableDeclarationStatement) {
					VariableDeclarationStatement vnode = (VariableDeclarationStatement) parent;
					List<?> fragments = vnode.fragments();
					Utils.print("" + vnode.toString());
				} else if (parent instanceof Block) {
					Block bnode = (Block) parent;
					List<?> fragments = bnode.statements();
					Utils.print("" + bnode.toString());
				}
			}
		}
		return true;
	}

	@Override
	public void endVisit(ClassInstanceCreation node) {
		// Utils.print(" ]CIC");
	}

	/*
	 * @Override public boolean visit(Assignment node) { AST ast = node.getAST();
	 * MethodInvocation setter = ast.newMethodInvocation();
	 * 
	 * setter.setName(ast.newSimpleName("setField"));
	 * 
	 * Expression expr = node.getRightHandSide(); Utils.print(">>>>>>>>>>>>>>>>>" +
	 * expr.toString());
	 * 
	 * setter.arguments().add(_rewriter.createMoveTarget(node.getRightHandSide()));
	 * _rewriter.replace(node, setter, null);
	 * 
	 * Utils.print(">>>>>>>>>>>>>>>>>" + node.toString()); return true; }
	 */

	/****/

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

	private boolean isTypeDeclaration(ITypeBinding node, String className) {
		boolean found = false;
		if (node.getQualifiedName().equals(className)) {
			found = true;
		}

		if (!found) {
			ITypeBinding superclass = node.getSuperclass();
			if (superclass != null)
				if (isTypeDeclaration(superclass, className)) {
					found = true;
				}
		}

		if (!found) {
			ITypeBinding[] interfaces = node.getInterfaces();
			for (ITypeBinding sInterface : interfaces) {
				found = isTypeDeclaration(sInterface, className);
				if (found)
					break;
			}
		}
		return found;
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

	private void print(ASTNode node) {
		List properties = node.structuralPropertiesForType();
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			Object descriptor = iterator.next();
			if (descriptor instanceof SimplePropertyDescriptor) {
				SimplePropertyDescriptor simple = (SimplePropertyDescriptor) descriptor;
				Object value = node.getStructuralProperty(simple);
				System.out.println(simple.getId() + " (" + value.toString() + ")");
			} else if (descriptor instanceof ChildPropertyDescriptor) {
				ChildPropertyDescriptor child = (ChildPropertyDescriptor) descriptor;
				ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
				if (childNode != null) {
					System.out.println("Child (" + child.getId() + " " + childNode.getNodeType() + ") {");
					print(childNode);
					System.out.println("}");
				}
			} else {
				ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) descriptor;
				System.out.println("List (" + list.getId() + "){");
				print((List) node.getStructuralProperty(list));
				System.out.println("}");
			}
		}
	}

	private void print(List nodes) {
		for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
			print((ASTNode) iterator.next());
		}
	}
	
	public boolean cercaCollezione(String dichiarazione) {
		
		boolean bool=false;
		String sottoStringa="List";
		
		int max = dichiarazione.length() - sottoStringa.length();
		
		
		 test:
			    for (int i = 0; i <= max; i++) {
			      int n = sottoStringa.length();
			      int j = i;
			      int k = 0;
			      while (n-- != 0) {
			        if (dichiarazione.charAt(j++) != sottoStringa.charAt(k++)) {
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
	
	public boolean cercaAdd(String istruzione) {
		
		
		boolean bool = false;
		String sottoStringa=".add";
		
		int max = istruzione.length() - sottoStringa.length();
		
		
		 test:
			    for (int i = 0; i <= max; i++) {
			      int n = sottoStringa.length();
			      int j = i;
			      int k = 0;
			      while (n-- != 0) {
			        if (istruzione.charAt(j++) != sottoStringa.charAt(k++)) {
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
	
	public boolean cercaRemove(String istruzione) {
		
		
		boolean bool = false;
		String sottoStringa=".remove";
		
		int max = istruzione.length() - sottoStringa.length();
		
		
		 test:
			    for (int i = 0; i <= max; i++) {
			      int n = sottoStringa.length();
			      int j = i;
			      int k = 0;
			      while (n-- != 0) {
			        if (istruzione.charAt(j++) != sottoStringa.charAt(k++)) {
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
	
	
	public boolean cercaSottostringaClasse(String classe , String sottoStringa) {
	
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
}
