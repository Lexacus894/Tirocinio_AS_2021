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

	// private static final int i = 0;

	CompilationUnit _compilation;
	Document _document;
	ASTRewrite _rewriter;
	Stack<Scope> _scope;
	ArrayList<FeatureCommandInstances> listaFeatureCommandInstances;
	// TODO: Probabilmente può essere static per e il controllo può fermarsi se è =
	// 1
	int programHasReceivers = 0;

	public ClassVisitorCommandInstances(CompilationUnit compilation, Document document, ASTRewrite rewriter,
			ArrayList<FeatureCommandInstances> listaFeature) {
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
		ITypeBinding binding = node.resolveBinding();
		if (binding == null) {
			return false;
		}

		String classeAnalizzata = binding.getName().toString();
		ITypeBinding superclass = binding.getSuperclass();
		ITypeBinding[] interfaces = binding.getInterfaces();

		ITypeBinding superSuperclass = null;
		ITypeBinding[] superInterfaces = null;
		if (superclass != null && !superclass.getName().equalsIgnoreCase("Object")) {
			superSuperclass = superclass.getSuperclass();
			superInterfaces = superclass.getInterfaces();
		}

		for (int i = 0; i < listaFeatureCommandInstances.size(); i++) {
			FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
			String commandInterface = ricercaClasse(riga, "CI");
			if (riga.toString().contains(classeAnalizzata + " - CC")
					|| riga.toString().contains(classeAnalizzata + " (Command) - CC")) {
				if (superclass != null && !superclass.getName().equalsIgnoreCase("Object")) {
					/*
					 * System.out.println(
					 * "COMMAND INTERFACE: " + commandInterface + " - SUPERCLASS:" +
					 * superclass.getName());
					 */

					if (superclass.getName().equalsIgnoreCase(commandInterface.toLowerCase())) {
						listaFeatureCommandInstances.get(i).setHasCommandRelationship(2);
					} else if (superSuperclass != null && !superSuperclass.getName().equalsIgnoreCase("Object")) {
						if (superSuperclass.getName().equalsIgnoreCase(commandInterface.toLowerCase())) {
							listaFeatureCommandInstances.get(i).setHasCommandRelationship(2);
						}
					}
				}
				for (ITypeBinding sInterface : interfaces) {
					/*
					 * System.out.println(
					 * "COMMAND INTERFACE: " + commandInterface + " - INTERFACE: "
					 * + sInterface.getName().toLowerCase());
					 */
					if (sInterface.getName().toLowerCase().equals(commandInterface.toLowerCase())) {
						listaFeatureCommandInstances.get(i).setHasCommandRelationship(2);
					} /*
						 * else if (superSuperclass != null &&
						 * !superSuperclass.getName().equalsIgnoreCase("Object")) {
						 * 
						 * if
						 * (sInterface.getName().equalsIgnoreCase(superSuperclass.getName().toLowerCase(
						 * ))) {
						 * listaFeatureCommandInstances.get(i).setHasCommandRelationship(2);
						 * 
						 * }
						 * }
						 */
				}
				if (superSuperclass != null /* && !superSuperclass.getName().equalsIgnoreCase("Object") */) {
					for (ITypeBinding superInterface : superInterfaces) {

						/*
						 * System.out.println(
						 * "COMMAND INTERFACE: " + commandInterface + " - SUPER INTERFACE: "
						 * + superInterface.getName().toLowerCase());
						 */

						if (superInterface.getName().toLowerCase().equals(commandInterface.toLowerCase())) {
							listaFeatureCommandInstances.get(i).setHasCommandRelationship(2);
						}
					}
				}
			}

		}

		return true;

	}

	@Override
	public void endVisit(TypeDeclaration node) {
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (getTypeDeclaration(node) == null) {
			return false;
		}
		String classeAnalizzata = getTypeDeclaration(node).resolveBinding().getName().toString();

		for (int i = 0; i < listaFeatureCommandInstances.size(); i++) {
			FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);

			if (riga.toString().contains(classeAnalizzata + " - IN")
					|| riga.toString().contains(classeAnalizzata + " (Invoker) - IN")) {
				listaFeatureCommandInstances.get(i).setHasInvoker(2);
			} else if (riga.toString().contains(classeAnalizzata + " - CL")
					|| riga.toString().contains(classeAnalizzata + " (Client) - CL")) {
				if (node.toString().toLowerCase().contains(".execute(")) {
					listaFeatureCommandInstances.get(i).setHasExecutorClient(2);
				}
			}
			if (riga.toString().contains(classeAnalizzata + " - CC")
					|| riga.toString().contains(classeAnalizzata + " (ConcreteCommand) - CC")) {
				listaFeatureCommandInstances.get(i).setHasConcreteCommand(2);
			}
			if (riga.toString().contains(classeAnalizzata + " - RE")
					|| riga.toString().contains(classeAnalizzata + " (Receiver) - RE")) {
				listaFeatureCommandInstances.get(i).setHasReceiver(2);
			}
			if (riga.toString().contains(classeAnalizzata + " - CI")
					|| riga.toString().contains(classeAnalizzata + " (CommandInterface) - CI")) {
				listaFeatureCommandInstances.get(i).setHasCommandInterface(2);
			}
		}

		return true;
	}

	@Override
	public void endVisit(MethodDeclaration node) {
	}

	@Override
	public boolean visit(MethodInvocation node) {

		/*
		 * if (getTypeDeclaration(node) == null) {
		 * return false;
		 * }
		 */
		String istruzioneChiamata = node.toString().toLowerCase();
		String classeAnalizzata = getTypeDeclaration(node).resolveBinding().getName().toString();

		for (int i = 0; i < listaFeatureCommandInstances.size(); i++) {
			FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
			// System.out.println(riga.toString());
			String receiver = ricercaReceiver(riga);
			String concretecommand = ricercaClasse(riga, "CC");
			String invoker = ricercaClasse(riga, "IN"); // CERCA CC E IN

			if (riga.toString().contains(classeAnalizzata + " - CC")
					|| riga.toString().contains(classeAnalizzata + " (ConcreteCommand) - CC")) {
				// CLASSE DICHIARANTE E' RECEIVER DELLA COMBINAZIONE
				if (node.resolveMethodBinding() != null) {

					String classeDichiarante = node.resolveMethodBinding().getDeclaringClass().getQualifiedName();

					if (!receiver.equals("") && receiver != null) {
						// System.out.println("CD: " + classeDichiarante.replaceAll(".+\\.", "") + " -
						// REC: " + receiver);
						if (classeDichiarante.replaceAll(".+\\.", "").equals(receiver)) {
							listaFeatureCommandInstances.get(i).setHasCCRERelationship(2);
						}
					}

					// EXECUTIONRELATIONSHIP
					/*
					 * if (riga.toString().contains(classeAnalizzata + " - CL")
					 * || riga.toString().contains(classeAnalizzata + " (Client) - CL")) {
					 */ // SE CONTIENE IL CLIENT
					// System.out.println(istruzioneChiamata);

					if (!concretecommand.equals("") && concretecommand != null) {// SE LA COMBINAZIONE CONTIENE UN CC
						if ((istruzioneChiamata.contains(".add") || istruzioneChiamata.contains(".put"))
								&& istruzioneChiamata.contains(concretecommand.toLowerCase())) {

							if (!invoker.equals("") && invoker != null) { // SE LA COMBINAZIONE CONTIENE UN INVOKER
								if (classeDichiarante.replaceAll(".+\\.", "").equals(invoker)
										|| (classeDichiarante.replaceAll(".+\\.", "") + " (Invoker)")
												.equals(invoker)) { // SE LA CLASSE CHE DICHIARA IL METODO E'
																	// L'INVOKER
									listaFeatureCommandInstances.get(i).setHasExecutorCCRelationship(2);
									// INVOKER ESEGUE CC
								}
								/*
								 * if (receiver.equals("") || receiver == null) {
								 * listaFeatureCommandInstances.get(i).setExecutionRelationship(2);
								 * }
								 */
							} else if (classeDichiarante.contains("javax.swing")
									|| classeDichiarante.contains("java.awt")) {
								listaFeatureCommandInstances.get(i).setHasExecutorCCRelationship(2);
								listaFeatureCommandInstances.get(i).setHasInvoker(2);
								// INVOKER ESTERNO ESEGUE CC
							}
						}
						/*
						 * if (istruzioneChiamata.contains(".execute") ||
						 * istruzioneChiamata.contains(".dispatch")) {
						 * if (classeDichiarante.replaceAll(".+\\.", "").equals(invoker)
						 * || (classeDichiarante.replaceAll(".+\\.", "") + " (Invoker)")
						 * .equals(invoker)) {
						 * 
						 * }
						 */
					}
					/* } */
				}
			}
		}

		return true;

	}

	@Override
	public void endVisit(MethodInvocation node) {
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		String classeAnalizzata = getTypeDeclaration(node).resolveBinding().getName().toString();
		String dichiarazione = node.toString().toLowerCase();
		if (dichiarazione.contains("new")) {

			for (int i = 0; i < listaFeatureCommandInstances.size(); i++) {
				FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);

				if (riga.toString().contains(classeAnalizzata + " - CL")
						|| riga.toString().contains(classeAnalizzata + " (Client) - CL")) {
					String concretecommand = ricercaClasse(riga, "CC");
					String invoker = ricercaClasse(riga, "IN");
					if (invoker.equals("") || invoker == null) {
						if (!concretecommand.equals("") && concretecommand != null) {
							if (dichiarazione.contains("new " + concretecommand.toLowerCase())) {
								listaFeatureCommandInstances.get(i).setHasExecutorCCRelationship(2);
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

	/*
	 * @Override
	 * public boolean visit(MethodDeclaration node) {
	 * return true;
	 * }
	 * 
	 * @Override
	 * public void endVisit(MethodDeclaration node) {
	 * }
	 * 
	 * @Override
	 * public boolean visit(ClassInstanceCreation node) {
	 * return true;
	 * }
	 * 
	 * @Override
	 * public void endVisit(ClassInstanceCreation node) {
	 * }
	 * 
	 * @Override
	 * public boolean visit(VariableDeclarationStatement node) {
	 * String classeAnalizzata =
	 * getTypeDeclaration(node).resolveBinding().getName().toString();
	 * String dichiarazione = node.toString().toLowerCase();
	 * if (dichiarazione.contains("new")) {
	 * 
	 * for (int i=0;i<listaFeatureCommandInstances.size();i++) {
	 * FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
	 * 
	 * if (riga.toString().contains(classeAnalizzata + " - IN")) {
	 * String classe2 = ricercaClasse(riga, "CC");
	 * 
	 * //INVOKEMETHOD = 2 - CONTROLLA SE L'INVOKER ISTANZIA IL CONCRETECOMMAND OLTRE
	 * AD ESEGUIRLO
	 * for(int j=0;j<node.fragments().size();j++) {
	 * if (dichiarazione.contains("new " + classe2.toLowerCase())) {
	 * if (getMethodDeclaration(node)!=null) {
	 * if (getMethodDeclaration(node).toString().contains(node.fragments().get(j).
	 * toString().substring(0,node.fragments().get(j).toString().indexOf("=")) +
	 * ".execute")) {
	 * System.out.println(getMethodDeclaration(node).toString());
	 * listaFeatureCommandInstances.get(i).setInvokeMethod(2);
	 * }
	 * }
	 * }
	 * }
	 * 
	 * }
	 * }
	 * }
	 * 
	 * return true;
	 * 
	 * }
	 * 
	 * @Override
	 * public void endVisit(VariableDeclarationStatement node) {
	 * }
	 * 
	 * 
	 * // Method Invocation - InvokeMethod
	 * 
	 * @Override
	 * public boolean visit(MethodInvocation node) {
	 * String istruzioneChiamata = node.toString().toLowerCase();
	 * 
	 * if (getTypeDeclaration(node) != null) {
	 * String classeAnalizzata =
	 * getTypeDeclaration(node).resolveBinding().getName().toString();
	 * 
	 * for (int i=0;i<listaFeatureCommandInstances.size();i++) {
	 * FeatureCommandInstances riga = listaFeatureCommandInstances.get(i);
	 * 
	 * //INVOKE METHOD + HAS EXTERNAL INVOKER
	 * if (riga.toString().contains(classeAnalizzata + " - CL")) {
	 * 
	 * String invoker = ricercaClasse(riga,"CC");
	 * String classe2 = ricercaClasse(riga,"IN");
	 * 
	 * if (!classe1.equals("") && classe1 != null) {
	 * 
	 * //HAS EXTERNAL INVOKER = 2
	 * if (istruzioneChiamata.contains(".add") &&
	 * istruzioneChiamata.contains(classe1.toLowerCase())) {
	 * if (node.resolveMethodBinding() != null) {
	 * String classeDichiarante =
	 * node.resolveMethodBinding().getDeclaringClass().getQualifiedName();
	 * if (classeDichiarante.contains("javax.swing") ||
	 * classeDichiarante.contains("java.awt")) {
	 * //listaFeatureCommandInstances.get(i).setHasExternalInvoker(2);
	 * }
	 * //INVOKE METHOD = 4 - CONTROLLA SE IL CLIENT CONTIENE UNA RIGA CHE ISTANZIA
	 * CONCRETECOMMAND E LO AGGIUNGE AD UN INVOKER TRAMITE IL METODO ADD DI
	 * QUEST'ULTIMO
	 * else if (!classe2.equals("") && classe2 != null) {
	 * if (classeDichiarante.replaceAll(".+\\.", "").equals(classe2)) {
	 * listaFeatureCommandInstances.get(i).setInvokeMethod(4);
	 * }
	 * }
	 * 
	 * }
	 * }
	 * 
	 * //INVOKE METHOD = 3 - CONTROLLA SE IL CLIENT CONTIENE UNA RIGA CHE ISTANZIA
	 * SIA INVOKER CHE CONCRETECOMMAND
	 * else if (!classe2.equals("") && classe2 != null) {
	 * if (istruzioneChiamata.contains("new " + classe1) &&
	 * istruzioneChiamata.contains("new " + classe2) &&
	 * listaFeatureCommandInstances.get(i).getInvokeMethod()==1) {
	 * listaFeatureCommandInstances.get(i).setInvokeMethod(3);
	 * }
	 * }
	 * 
	 * }
	 * }
	 * 
	 * else if (riga.toString().contains(classeAnalizzata + " - IN")) {
	 * String classe1 = ricercaClasse(riga,"CC");
	 * if (!classe1.equals("") && classe1 != null) {
	 * //INVOKEMETHOD = 2 - CONTROLLA SE L'INVOKER CONTIENE NEW COMMAND.execute
	 * if (istruzioneChiamata.contains("new " + classe1) &&
	 * istruzioneChiamata.contains(".execute") &&
	 * listaFeatureCommandInstances.get(i).getInvokeMethod()!=3) {
	 * listaFeatureCommandInstances.get(i).setInvokeMethod(2);
	 * }
	 * //CCRERELATIONSHIP = 3 - CONTROLLA SE IL METODO INVOCATO E' UN EXECUTE
	 * if (!istruzioneChiamata.contains(".execute(")) {
	 * if (node.resolveMethodBinding() != null) {
	 * String classeDichiarante =
	 * node.resolveMethodBinding().getDeclaringClass().getQualifiedName().replaceAll
	 * (".+\\.", "");
	 * if (classeDichiarante.equals(classe1)) {
	 * listaFeatureCommandInstances.get(i).setCCRERelationship(3);
	 * }
	 * }
	 * }
	 * 
	 * 
	 * }
	 * }
	 * 
	 * //CCRERELATIONSHIP = 2 - CONTROLLA SE IL METODO INVOCATO DALLA CLASSE
	 * CONCRETECOMMAND VIENE DICHIARATO DALLA CLASSE RECEIVER
	 * if (riga.toString().contains(classeAnalizzata + " - CC")) {
	 * String classe1 = ricercaClasse(riga,"RE");
	 * 
	 * if (node.resolveMethodBinding() != null) {
	 * String classeDichiarante =
	 * node.resolveMethodBinding().getDeclaringClass().getQualifiedName();
	 * if (!classe1.equals("") && classe1 != null) {
	 * //CLASSE DICHIARANTE E' RECEIVER DELLA COMBINAZIONE
	 * if (classeDichiarante.replaceAll(".+\\.", "").equals(classe1)) {
	 * listaFeatureCommandInstances.get(i).setCCRERelationship(2);
	 * }
	 * 
	 * }
	 * //CLASSE DICHIARANTE FA PARTE DI JAVAX SWING O DI JAVA AWT
	 * else if (classeDichiarante.contains("javax.swing") ||
	 * classeDichiarante.contains("java.awt")) {
	 * //listaFeatureCommandInstances.get(i).setHasExternalReceiver(2);
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return true;
	 * 
	 * }
	 * 
	 * 
	 * @Override
	 * public void endVisit(MethodInvocation node) {
	 * }
	 * 
	 * // Field Declaration
	 * 
	 * @Override
	 * public boolean visit(FieldDeclaration node) {
	 * return true;
	 * }
	 * 
	 * 
	 * @Override
	 * public void endVisit(FieldDeclaration node) {
	 * }
	 * 
	 * // Field Declaration
	 * 
	 * @Override
	 * public boolean visit(ArrayCreation node) {
	 * return true;
	 * }
	 * 
	 * 
	 * @Override
	 * public void endVisit(ArrayCreation node) {
	 * }
	 * 
	 * @Override
	 * public boolean visit(FieldAccess node) {
	 * return true;
	 * }
	 * 
	 * @Override
	 * public void endVisit(FieldAccess node) {
	 * }
	 */

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

		String rigaAb = "";
		if (riga.getClass1().contains(" - " + ruolo)) {
			rigaAb = riga.getClass1().substring(0, riga.getClass1().length() - 5);
		} else if (riga.getClass2().contains(" - " + ruolo)) {
			rigaAb = riga.getClass2().substring(0, riga.getClass2().length() - 5);
		} else if (riga.getClass3().contains(" - " + ruolo)) {
			rigaAb = riga.getClass3().substring(0, riga.getClass3().length() - 5);
		} else if (riga.getClass4().contains(" - " + ruolo)) {
			rigaAb = riga.getClass4().substring(0, riga.getClass4().length() - 5);
		} else if (riga.getClass5().contains(" - " + ruolo)) {
			rigaAb = riga.getClass5().substring(0, riga.getClass5().length() - 5);
		}

		if (rigaAb.contains("(Client)")) {
			rigaAb = rigaAb.substring(0, rigaAb.length() - 9);

		}
		if (rigaAb.contains("(ConcreteCommand)")) {
			rigaAb = rigaAb.substring(0, rigaAb.length() - 18);

		}
		if (rigaAb.contains("(Invoker)")) {
			rigaAb = rigaAb.substring(0, rigaAb.length() - 10);

		}

		if (rigaAb.contains("(CommandInterface)")) {
			rigaAb = rigaAb.substring(0, rigaAb.length() - 11);

		}

		return rigaAb;
	}

	private String ricercaReceiver(FeatureCommandInstances riga) {
		String rigaAb = "";
		// System.out.println(riga.toString());
		if (riga.toString().contains(" - RE") || riga.toString().contains(" - RE,")) {
			if (riga.getClass1().contains(" - RE")) {
				rigaAb = riga.getClass1().substring(0, riga.getClass1().length() - 5);
			} else if (riga.getClass2().contains(" - RE")) {
				rigaAb = riga.getClass2().substring(0, riga.getClass2().length() - 5);
			} else if (riga.getClass3().contains(" - RE")) {
				rigaAb = riga.getClass3().substring(0, riga.getClass3().length() - 5);
			} else if (riga.getClass4().contains(" - RE")) {
				rigaAb = riga.getClass4().substring(0, riga.getClass4().length() - 5);
			} else if (riga.getClass5().contains(" - RE")) {
				rigaAb = riga.getClass5().substring(0, riga.getClass5().length() - 5);
			}
			if (rigaAb.contains("(Receiver)")) {
				rigaAb = rigaAb.substring(0, rigaAb.length() - 11);

			}
			// System.out.println(rigaAb);

		}
		return rigaAb;
	}

	public Integer getProgramHasReceivers() {
		return programHasReceivers;
	}

}
