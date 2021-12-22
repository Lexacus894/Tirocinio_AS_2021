package dataset;

public class Feature {
	
	private String SfotwareName;
	private String FQNClass;
	private int CollectionVariables;
	private int AddListenerMethod;
	private int RemoveListenerMethod;
	private int ClassDeclarationKeyword;
	private int MethodDeclarationKeyword;
	private int ClassType;
	private int ScanCollectionMethod;
	private int SCMCallAbsMethod;
	private int HasSuperclass;
	private int ImplementsInterfaces;
	private int ChangeState;
	private int AfterChangeStateIterateOverList;
	
	public Feature (String software,String FQN,int collection,int add,int remove , int classDeclaration,int methodDeclaration,int classtype,
			        int scan , int scm ,int superclass ,int implement ,int change , int after ) {
		
		this.SfotwareName=software;
		this.FQNClass=FQN;
		this.CollectionVariables=collection;
		this.AddListenerMethod =add;
		this.RemoveListenerMethod=remove;
		this.ClassDeclarationKeyword=classDeclaration;
		this.MethodDeclarationKeyword=methodDeclaration;
		this.ClassType=classtype;
		this.ScanCollectionMethod=scan;
		this.SCMCallAbsMethod= scm;
		this.HasSuperclass=superclass;
		this.ImplementsInterfaces =implement;
		this.ChangeState= change;
	    this.AfterChangeStateIterateOverList= after;
		
	    // 12 ELEMENTI DEL VETTORE DI FEATURE
		
	}
	
	
	
	public String getSfotwareName() {
		return SfotwareName;
	}



	public void setSfotwareName(String sfotwareName) {
		SfotwareName = sfotwareName;
	}
	
	
	public String getFQNClass() {
		return this.FQNClass;
	}
	
	public void setFQNClass(String f) {
		this.FQNClass=f;
	}
	
	
	
	public int getCollectionVariables() {
		return CollectionVariables;
	}
	public void setCollectionVariables(int collectionVariables) {
		CollectionVariables = collectionVariables;
	}
	public int getAddListenerMethod() {
		return AddListenerMethod;
	}
	public void setAddListenerMethod(int addListenerMethod) {
		AddListenerMethod = addListenerMethod;
	}
	public int getRemoveListenerMethod() {
		return RemoveListenerMethod;
	}
	public void setRemoveListenerMethod(int removeListenerMethod) {
		RemoveListenerMethod = removeListenerMethod;
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
	public int getScanCollectionMethod() {
		return ScanCollectionMethod;
	}
	public void setScanCollectionMethod(int scanCollectionMethod) {
		ScanCollectionMethod = scanCollectionMethod;
	}
	public int getSCMCallAbsMethod() {
		return SCMCallAbsMethod;
	}
	public void setSCMCallAbsMethod(int sCMCallAbsMethod) {
		SCMCallAbsMethod = sCMCallAbsMethod;
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
	public int getChangeState() {
		return ChangeState;
	}
	public void setChangeState(int changeState) {
		ChangeState = changeState;
	}
	public int getAfterChangeStateIterateOverList() {
		return AfterChangeStateIterateOverList;
	}
	public void setAfterChangeStateIterateOverList(int afterChangeStateIterateOverList) {
		AfterChangeStateIterateOverList = afterChangeStateIterateOverList;
	}
	
	
	
	

	  @Override
	  public String toString() {
		  
		  return " VettoreFeature [Collection=" + this.CollectionVariables + ", Add=" + this.AddListenerMethod + ",Remove =" + this.RemoveListenerMethod + "" + "]";
	  
	  }





}
