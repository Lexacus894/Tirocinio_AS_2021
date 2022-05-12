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
import dataset.FeatureCommandRoles;
import dataset.strutturaVariabile;
import it.unisa.javat.Utils;

public class ClassVisitorCommand extends ASTVisitor {
	
	//private static final int i = 0;

	CompilationUnit _compilation;
	Document _document;
	ASTRewrite _rewriter;
	Stack<Scope> _scope;
	
	ArrayList<FeatureCommandRoles> arrayListFeature;
	FeatureCommandRoles feat;
	ArrayList<FeatureCommandRoles> arrayListTemp;
	String folder;
	String nomeProgetto;
	static ArrayList<String> listaClassiInExecute = new ArrayList<String>();
	int tempval;
	
	//ArrayList<String> importedCommands = new ArrayList<String>();

	
	public ClassVisitorCommand(CompilationUnit compilation, Document document, ASTRewrite rewriter, ArrayList<FeatureCommandRoles> arrayListFeatureVisitor, String folder,String nomeProgetto) {
		_compilation = compilation;
		_document = document;
		_rewriter = rewriter;
		_scope = new Stack<Scope>();
		

		this.arrayListFeature = arrayListFeatureVisitor;
	    
	    this.arrayListTemp = new ArrayList<FeatureCommandRoles>();
	    this.folder = folder;
	    this.nomeProgetto = nomeProgetto;
	    
	}

	// Compilation Unit
	@Override
	public boolean visit(CompilationUnit node) {

		@SuppressWarnings("unchecked")
		List<Comment> comments = node.getCommentList();
		for (Comment c : comments) {
			try {
				String codeComment = _document.get(c.getStartPosition(), c.getLength());
				//Utils.print("[CO " + codeComment + " ]CO");
			} 
			catch (BadLocationException e) {
			}
		}
		_scope.push(new Scope(ScopeType.COMPILATIONUNIT));
		return true;
	}

	@Override
	public void endVisit(CompilationUnit node) {
		ArrayList<FeatureCommandRoles> arrayListScissione = new ArrayList<FeatureCommandRoles>();
		for(int i=0; i<arrayListTemp.size();i++) {
				arrayListFeature.add(arrayListTemp.get(i));
		}
		for(int i=0; i<arrayListScissione.size();i++) {
			arrayListFeature.add(arrayListScissione.get(i));
		}
		
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
		//Utils.print("  [PD " + node.getClass().getSimpleName() + " " + binding.getName() + " ]");
		
		return true;
	}

	@Override
	public void endVisit(PackageDeclaration node) {
		// Utils.print(" ]PD");
	}

	// Type Declaration - FNQClass, ClassType, ClassDeclarationKeyword, HasSuperclass, ImplementsInterface
	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding binding = node.resolveBinding();
		if (binding == null) {
			//Utils.print("[TD NOBIND]");
			return false;
		}
	    feat = new FeatureCommandRoles(nomeProgetto,folder,"",1,1,1,1,1,1,1,1);
		feat.setFQNClass(binding.getName());
		
		//ClassType
		if (binding.isInterface()) {
			feat.setClassType(3);
			//Utils.print("  [TD" + printModifiers(binding.getModifiers()) + " INTERFACE " + node.getClass().getSimpleName() + " " + binding.getQualifiedName());
		} 
		else if(Modifier.isAbstract(binding.getModifiers())) {
				feat.setClassType(2);
			}
		else {
				feat.setClassType(1);
		}
		
		//ClassDeclarationKeyword
		if (binding.getName().toLowerCase().equals("command")) {
			feat.setClassDeclarationKeyword(3);
		}
		else if (binding.getName().toLowerCase().contains("command") || binding.getName().toLowerCase().contains("action")) {
			feat.setClassDeclarationKeyword(2);
		}
		else {
			feat.setClassDeclarationKeyword(1);
		}
		
		//HasSuperclass - Controllo superclasse e nome superclasse
	    ITypeBinding superclass = binding.getSuperclass();
		if (superclass != null && !superclass.getName().equalsIgnoreCase("Object")) {
			if (superclass.getName().toLowerCase().contains("command")) {
				feat.setHasSuperclass(3);
			}
			else {
				feat.setHasSuperclass(2);
			}	
		} 

		//ImplementsInterfaces(1);
		ITypeBinding[] interfaces = binding.getInterfaces();
		for (ITypeBinding sInterface : interfaces) {
			if (sInterface.getName().toLowerCase().contains("command")) {
				feat.setImplementsInterfaces(3);
			}
			else {
				feat.setImplementsInterfaces(2);
			}
			//Utils.print("   [IMP" + printModifiers(sInterface.getModifiers()) + " " + sInterface.getName() + " ]");
		}

		arrayListTemp.add(feat);
		return true;
	}

	@Override
	public void endVisit(TypeDeclaration node) {
	}
	
	// Import Declaration
		@Override
		public boolean visit(ImportDeclaration node) {
			return true;
		}

		@Override
		public void endVisit(ImportDeclaration node) {
		}

	// Method Declaration - ClassType, MethodDeclarationKeyword
	@Override
	public boolean visit(MethodDeclaration node) {

		IMethodBinding binding = node.resolveBinding();
		if (binding == null) {
			return false;
		}
		
		String nomeClasseDichiarante = node.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "");
		
		String metodo = binding.toString().toLowerCase();
		
		if ((metodo.contains("execute") || metodo.contains("actionperformed")) && !metodo.contains("executeQuery") /* || nomeMetodo.contains("run(")*/) {
			//Ricerca classe
			for(int i = 0;i < arrayListTemp.size();i++) {
				String FQN = arrayListTemp.get(i).getFQNClass();
				if (nomeClasseDichiarante.equals(FQN)) {
					arrayListTemp.get(i).setMethodDeclarationKeyword(2);
				}
			}
		}
		
		return true;
	}

	@Override
	public void endVisit(MethodDeclaration node) {
	}
	
	// Method Invocation - Instantiates, ExecutesCommand, Ricerca Classi in execute()
	@Override
	public boolean visit(MethodInvocation node) {
			
		String istruzioneChiamata = node.toString().toLowerCase();
		
		//mnode.toString() restituisce il metodo per intero dalla dichiarazione all'ultima parentesi graffa.
		MethodDeclaration mnode = getMethodDeclaration(node);
		
		
		
			if (mnode != null) {
				IMethodBinding mbinding = mnode.resolveBinding(); 
				
				if (node.resolveMethodBinding() != null) {
					String nomeClasseDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", ""); 
			
				//mnode.resolveBinding().getDeclaringClass().getQualifiedName() restituisce il nome della classe che ha dichiarato il metodo in mnode.toString
				//node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "") restituisce il nome della classe che ha dichiarato il metodo in questo node
			
				//ricerca delle classi che dichiarano metodi invocati in un metodo execute() o actionPerformed(Action event e) di un possibile Concrete Command
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
			
			//ExecutesCommand
			if (istruzioneChiamata.contains(".execute")) {
				if (node.resolveMethodBinding() != null) {
					String nomeClasseDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", ""); 
					if (nomeClasseDichiarante.toLowerCase().contains("command")) {
						if (Modifier.isAbstract(node.resolveMethodBinding().getDeclaringClass().getModifiers())) {
							System.out.println("PROVA PROVA PROVA PROVA NOME CLASSE DICHIARANTE ASTRATTA:" + nomeClasseDichiarante);
							tempval = 4;
						}
						else {
							System.out.println("PROVA PROVA PROVA PROVA NOME CLASSE DICHIARANTE NON ASTRATTA:" + nomeClasseDichiarante);
							tempval = 3;
						}	
					}
				}
				else {
					tempval = 2;
				}
				
				//Ricerca classe
				for(int i=0;i<arrayListTemp.size();i++) {
					String FQN = arrayListTemp.get(i).getFQNClass();
					if (mnode.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "").equals(FQN)) {
						if (tempval > arrayListTemp.get(i).getExecutesCommand()) {
							arrayListTemp.get(i).setExecutesCommand(tempval);
						}
					}
				}
			
			}
			
			//InstantiatesCommand
			if (istruzioneChiamata.contains("new")) {
				String ist = istruzioneChiamata.substring(istruzioneChiamata.indexOf("new"));
				if ((ist.contains("command") || ist.contains("action")) && !ist.contains("actionlistener")) {
					//Ricerca classe
					for(int j=0;j<arrayListTemp.size();j++) {
						String FQN = arrayListTemp.get(j).getFQNClass();
						if (mnode.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "").equals(FQN)) {
							arrayListTemp.get(j).setInstantiatesCommand(2);
						}
					}
				}
			}

		}
		
		return true;
			
	}

	@Override
	public void endVisit(MethodInvocation node) {
	}
	
	//Field Declaration - InstantiatesCommand
	@Override
	public boolean visit(Assignment node) {
		MethodDeclaration mnode = getMethodDeclaration(node);
		String istruzione = node.getRightHandSide().toString().toLowerCase();
		//System.out.println("PROVA PROVA PROVA" + istruzione);
		if (istruzione.contains("new")) {
			String ist = istruzione.substring(istruzione.indexOf("new"));
			if ((ist.contains("command") || ist.contains("action")) && !ist.contains("actionlistener")) {
				//System.out.println("PROVA PROVA PROVA Questa classe istanzia la classe " + importedCommandsTemp.get(i) + " che ha importato, in un'invocazione. PROVA PROVA PROVA");
				System.out.println("ISTRUZIONE CHIAMATA: " + istruzione);
				//Ricerca classe
				for(int j=0;j<arrayListTemp.size();j++) {
					String FQN = arrayListTemp.get(j).getFQNClass();
					if (mnode.resolveBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "").equals(FQN)) {
						arrayListTemp.get(j).setInstantiatesCommand(2);
					}
				}
			}
		}
		return true;
	}

	@Override
	public void endVisit(Assignment node) {
		// Utils.print(" ]FD");
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
	
	//FINE VISITE

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
	
	private Assignment getAssignment(ASTNode node) {
		ASTNode pnode = node;
		while (pnode != null && pnode.getNodeType() != ASTNode.ASSIGNMENT) {
			pnode = pnode.getParent();
		}

		return (Assignment) pnode;
	}
	
	//Metodo getMethodInvocation
	private MethodInvocation getMethodInvocation(ASTNode node) {
		ASTNode pnode = node;
		while (pnode != null && pnode.getNodeType() != ASTNode.METHOD_INVOCATION) {
			pnode = pnode.getParent();
		}

		return (MethodInvocation) pnode;
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

	//getListaClassiInExecute
	public ArrayList<String> getListaClassiInExecute() {
			return listaClassiInExecute;
	}
	
}
