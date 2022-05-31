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

		_scope.push(new Scope(ScopeType.COMPILATIONUNIT));

		return true;
	}

	@Override
	public void endVisit(CompilationUnit node) {
			
			_scope.pop();
	}
		
		
		
		
	@Override
	public boolean visit(TypeDeclaration node) {	
	          
	return true ;
	      
	}
		
	@Override
	public void endVisit(TypeDeclaration node) {
	}
	
	
	@Override
	public boolean visit(MethodDeclaration node) { 
		if (getTypeDeclaration(node) != null) {
			String classeAnalizzata = getTypeDeclaration(node).resolveBinding().getName();
			//System.out.println(node.toString());
			String primariga = "";
			if (node.toString().indexOf("{") != -1) {
				primariga = node.toString().substring(0,node.toString().indexOf("{")).toLowerCase();
			}
			else if (node.toString().indexOf(" {") != -1) {
				primariga = node.toString().substring(0,node.toString().indexOf(" {")).toLowerCase();
			}
			
			if (!primariga.contains(" execute") || !primariga.contains("actionperformed")) {
				for(int i = 0; i<listaFeatureCommandInstances.size(); i++) {
					
					FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
					
					if (riga.toString().contains(classeAnalizzata + " - CC")) {
						listaFeatureCommandInstances.get(i).setCCHasNoExecute(2);
					}	
				}
			}
		}
			
		
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
			String classeAnalizzata = getTypeDeclaration(node).resolveBinding().getName().toString();
			
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
									listaFeatureCommandInstances.get(i).setInvokeMethod(3);
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
			String classeAnalizzata = getTypeDeclaration(node).resolveBinding().getName().toString();
			
			for (int i=0;i<listaFeatureCommandInstances.size();i++) {
				FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
				
				//INVOKE METHOD + HAS EXTERNAL INVOKER
				if (riga.toString().contains(classeAnalizzata + " - CL")) {
					
					String classe1 = ricercaClasse(riga,"CC");
					String classe2 = ricercaClasse(riga,"IN");
					
					if (!classe1.equals("") && classe1 != null) {
						
						//HAS EXTERNAL INVOKER = 2
						if (istruzioneChiamata.contains(".add") && istruzioneChiamata.contains(classe1.toLowerCase())) {
							if (node.resolveMethodBinding() != null) {
								String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName();
								if (classeDichiarante.contains("javax.swing") || classeDichiarante.contains("java.awt")) {
									listaFeatureCommandInstances.get(i).setInvokeMethod(2);
								}
								//INVOKE METHOD = 4 - CONTROLLA SE IL CLIENT CONTIENE UNA RIGA CHE ISTANZIA CONCRETECOMMAND E LO AGGIUNGE AD UN INVOKER TRAMITE IL METODO ADD DI QUEST'ULTIMO
								else if (!classe2.equals("") && classe2 != null) {
									if (classeDichiarante.replaceAll(".+\\.", "").equals(classe2)) {
										listaFeatureCommandInstances.get(i).setInvokeMethod(3);
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
			
				else if (riga.toString().contains(classeAnalizzata + " - IN")) {
					String classe1 = ricercaClasse(riga,"CC");
					if (!classe1.equals("") && classe1 != null) {
						//INVOKEMETHOD = 2 - CONTROLLA SE L'INVOKER CONTIENE NEW COMMAND.execute
						if (istruzioneChiamata.contains("new " + classe1) && istruzioneChiamata.contains(".execute") && listaFeatureCommandInstances.get(i).getInvokeMethod()!=3) {
							listaFeatureCommandInstances.get(i).setInvokeMethod(3);
						}
						/*//CCRERELATIONSHIP = 3 - CONTROLLA SE IL METODO INVOCATO E' UN EXECUTE
						if (!istruzioneChiamata.contains(".execute(")) {
							if (node.resolveMethodBinding() != null) {
								String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll(".+\\.", "");
								if (classeDichiarante.equals(classe1)) {
									listaFeatureCommandInstances.get(i).setCCRERelationship(3);
								}
							}
						}*/
						
						
					}
				}
				
				//CCRERELATIONSHIP = 2 - CONTROLLA SE IL METODO INVOCATO DALLA CLASSE CONCRETECOMMAND VIENE DICHIARATO DALLA CLASSE RECEIVER
				if (riga.toString().contains(classeAnalizzata + " - CC")) {
					String classe1 = ricercaClasse(riga,"RE");
					
						if (node.resolveMethodBinding() != null) {
							String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName();
							if (!classe1.equals("") && classe1 != null) {
								//CLASSE DICHIARANTE E' RECEIVER DELLA COMBINAZIONE
								if (classeDichiarante.replaceAll(".+\\.", "").equals(classe1)) {
									listaFeatureCommandInstances.get(i).setCCRERelationship(3);
								}
								
							}
							//CLASSE DICHIARANTE FA PARTE DI JAVAX SWING O DI JAVA AWT
							else if (classeDichiarante.contains("javax.swing") || classeDichiarante.contains("java.awt")) {
								listaFeatureCommandInstances.get(i).setCCRERelationship(2);
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
	
	// Field Declaration
	@Override
	public boolean visit(FieldDeclaration node) {
		return true;
	}
	
	
	@Override
	public void endVisit(FieldDeclaration node) {
	}
	
	// Field Declaration
	@Override
	public boolean visit(ArrayCreation node) {
		return true;
	}
	
	
	@Override
	public void endVisit(ArrayCreation node) {
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		return true;
	}
	
	@Override
	public void endVisit(FieldAccess node) {	
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
