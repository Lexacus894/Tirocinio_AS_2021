package dataset;

public class FeatureCommandRoles {
	
	private String SoftwareName;
	private String FileName;
	private String FQNClass;
	private int ClassDeclarationKeyword;
	private int MethodDeclarationKeyword;
	private int ClassType;
	private int ExecutesCommand;
	private int InstantiatesCommand;
	private int HasSuperclass;
	private int ImplementsInterfaces;
	private int isPartOfExecute;

	public FeatureCommandRoles(String softwareName, String fileName, String fqnClass, int classDeclaration, int methodDeclaration, int classType, int executesCommand, 
			int instantiatesCommand, int hasSuperclass, int implementsInterfaces, int isPartOfExecute) {
		this.SoftwareName = softwareName;
		this.FileName = fileName;
		this.FQNClass = fqnClass;
		this.ClassDeclarationKeyword = classDeclaration;
		this.MethodDeclarationKeyword = methodDeclaration;
		this.ClassType = classType;
		this.ExecutesCommand = executesCommand;
		this.InstantiatesCommand = instantiatesCommand;
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
	
	public String getFileName() {
		return FileName;
	}
	
	public void setFileName(String fileName) {
		FileName = fileName;
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

	public int getInstantiatesCommand() {
		return InstantiatesCommand;
	}

	public void setInstantiatesCommand(int instantiatesCommand) {
		InstantiatesCommand = instantiatesCommand;
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
		  		+ ", ClassType = " + this.ClassType + ", ExecutesCommand = " + this.ExecutesCommand + ", InstantiatesCommand = " + this.InstantiatesCommand
		  		+ ", HasSuperclass = "+ this.HasSuperclass + ", ImplementsInterfaces = " + this.ImplementsInterfaces + ", isPartOfExecute = " + this.isPartOfExecute + 
		  		", hasMain = "  +"]";
	  
	  }
}
