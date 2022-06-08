package dataset;

public class FeatureCommandInstances {
	
	private String class1;
	private String class2;
	private String class3;
	private String class4;
	private String class5;
	private int CCRERelationship;
	private int InvokeMethod;
	private int NumC;
	
	public FeatureCommandInstances(String class1, String class2, String class3, String class4, String class5,
			int ccRERelationship, int invokeMethod, int numC) {
		this.class1 = class1;
		this.class2 = class2;
		this.class3 = class3;
		this.class4 = class4;
		this.class5 = class5;
		this.CCRERelationship = ccRERelationship;
		this.InvokeMethod = invokeMethod;
		this.NumC = numC;
	}

	public String getClass1() {
		return class1;
	}

	public void setClass1(String class1) {
		this.class1 = class1;
	}

	public String getClass2() {
		return class2;
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}
	
	public String getClass3() {
		return class3;
	}

	public void setClass3(String class3) {
		this.class3 = class3;
	}
	
	public String getClass4() {
		return class4;
	}

	public void setClass4(String class4) {
		this.class4 = class4;
	}
	
	public String getClass5() {
		return class5;
	}

	public void setClass5(String class5) {
		this.class5 = class5;
	}

	public int getCCRERelationship() {
		return CCRERelationship;
	}

	public void setCCRERelationship(int ccRERelationship) {
		CCRERelationship = ccRERelationship;
	}

	public int getInvokeMethod() {
		return InvokeMethod;
	}

	public void setInvokeMethod(int invokeMethod) {
		InvokeMethod = invokeMethod;
	}

	public int getNumC() {
		return NumC;
	}

	public void setNumC(int numC) {
		NumC = numC;
	}

	@Override
	public String toString() {
		return ("Classe 1: " + class1 + ", Classe 2: " + class2 + ", Classe 3: " + class3 + ", Classe 4: " + class4 + ", Classe 5: " + class5 + ", CCRERelationship: " + CCRERelationship 
				+ ", InvokeMethod: " + InvokeMethod  + ", N Classes: " + NumC);
	}
	
}
