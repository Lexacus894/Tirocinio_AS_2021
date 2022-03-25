package dataset;

public class FeatureCommandInstances {
	
	private String Class1;
	private String Class2;
	private int CommandRelationship;
	private int ExecuteRelationship;
	private int IsPartOfExecute;
	private int AddNewRelationship;
	
	public FeatureCommandInstances(String class1, String class2, int commandRelationship, int executeRelationship, int isPartOfExecute, int addNewRelationship) {
		Class1 = class1;
		Class2 = class2;
		CommandRelationship = commandRelationship;
		ExecuteRelationship = executeRelationship;
		IsPartOfExecute = isPartOfExecute;
		AddNewRelationship = addNewRelationship;
	
	}

	public String getClass1() {
		return Class1;
	}

	public void setClass1(String class1) {
		Class1 = class1;
	}

	public String getClass2() {
		return Class2;
	}

	public void setClass2(String class2) {
		Class2 = class2;
	}

	public int getCommandRelationship() {
		return CommandRelationship;
	}

	public void setCommandRelationship(int commandRelationship) {
		CommandRelationship = commandRelationship;
	}

	public int getExecuteRelationship() {
		return ExecuteRelationship;
	}

	public void setExecuteRelationship(int executeRelationship) {
		ExecuteRelationship = executeRelationship;
	}

	public int getIsPartOfExecute() {
		return IsPartOfExecute;
	}

	public void setIsPartOfExecute(int isPartOfExecute) {
		IsPartOfExecute = isPartOfExecute;
	}

	public int getAddNewRelationship() {
		return AddNewRelationship;
	}

	public void setAddNewRelationship(int addNewRelationship) {
		AddNewRelationship = addNewRelationship;
	}
	
	@Override
	public String toString() {
		return ("Classe 1: " + Class1 + ", Classe 2: " + Class2 + ", CommandRelationship: " + CommandRelationship + ", ExecuteRelationship: " + ExecuteRelationship 
				+ ", IsPartOfExecute: " + IsPartOfExecute + ", AddNewRelationship: " + AddNewRelationship);
	}
	
}
