package dataset;

public class FeatureCommand {
	
	private String SoftwareName;
	private String FQNClass;
	private int ClassDeclarationKeyword;
	private int MethodDeclarationKeyword;
	private int ClassType;
	private int ExecutesCommand;
	private int AddsCommandMethod;
	private int HasSuperclass;
	private int ImplementsInterfaces;
	private int isPartOfExecute;

	public FeatureCommand(String softwareName, String fqnClass, int classDeclaration, int methodDeclaration, int classType, int executesCommand, 
			int addsCommand, int hasSuperclass, int implementsInterfaces, int isPartOfExecute ) {
		this.SoftwareName = softwareName;
		this.FQNClass = fqnClass;
		this.ClassDeclarationKeyword = classDeclaration;
		this.MethodDeclarationKeyword = methodDeclaration;
		this.ClassType = classType;
		this.ExecutesCommand = executesCommand;
		this.AddsCommandMethod = addsCommand;
		this.HasSuperclass = hasSuperclass;
		this.ImplementsInterfaces = implementsInterfaces;
		this.isPartOfExecute = isPartOfExecute;
	}

	public String getSoftwareName() {
		return SoftwareName;
	}

	public void setSoftwareName(String softwareName) {
		SoftwareName = softwareName;
	}

	public String getFQNClass() {
		return FQNClass;
	}

	public void setFQNClass(String fQNClass) {
		FQNClass = fQNClass;
	}

	public int getClassDeclarationKeyword() {
		return ClassDeclarationKeyword;
	}

	public void setClassDeclarationKeyword(int classDeclarationKeyword) {
		ClassDeclarationKeyword = classDeclarationKeyword;
	}

	public int getMethodDeclarationKeyword() {
		return MethodDeclarationKeyword;
	}

	public void setMethodDeclarationKeyword(int methodDeclarationKeyword) {
		MethodDeclarationKeyword = methodDeclarationKeyword;
	}

	public int getClassType() {
		return ClassType;
	}

	public void setClassType(int classType) {
		ClassType = classType;
	}

	public int getExecutesCommand() {
		return ExecutesCommand;
	}

	public void setExecutesCommand(int executesCommand) {
		ExecutesCommand = executesCommand;
	}

	public int getAddsCommandMethod() {
		return AddsCommandMethod;
	}

	public void setAddsCommandMethod(int addCommandMethod) {
		AddsCommandMethod = addCommandMethod;
	}

	public int getHasSuperclass() {
		return HasSuperclass;
	}

	public void setHasSuperclass(int hasSuperclass) {
		HasSuperclass = hasSuperclass;
	}

	public int getImplementsInterfaces() {
		return ImplementsInterfaces;
	}

	public void setImplementsInterfaces(int implementsInterfaces) {
		ImplementsInterfaces = implementsInterfaces;
	}
	
	public int getIsPartOfExecute() {
		return isPartOfExecute;
	}

	public void setIsPartOfExecute(int isPartOfExecute) {
		this.isPartOfExecute = isPartOfExecute;
	}
	
	@Override
	  public String toString() {
		  
		  return " VettoreFeatureCommand [SoftwareName = " + this.SoftwareName + ", FQNClass = " + this.FQNClass
		  		+ ", ClassDeclarationKeyword = " + this.ClassDeclarationKeyword + ", MethodDeclarationKeyword = " + this.MethodDeclarationKeyword
		  		+ ", ClassType = " + this.ClassType + ", ExecutesCommand = " + this.ExecutesCommand + ", AddsCommand = " + this.AddsCommandMethod
		  		+ ", HasSuperclass = "+ this.HasSuperclass + ", ImplementsInterfaces = " + this.ImplementsInterfaces + ", isPartOfExecute = " + this.isPartOfExecute +"]";
	  
	  }
}
