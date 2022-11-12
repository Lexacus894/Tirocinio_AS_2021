package dataset;

public class FeatureCommandInstances {

	private String class1;
	private String class2;
	private String class3;
	private String class4;
	private String class5;
	private int HasExecutorClient;
	private int HasInvoker;
	private int HasConcreteCommand;
	private int HasReceiver;
	private int HasCCRERelationship;
	private int HasExecutorCCRelationship;
	// private int ExecutionRelationship;
	private int NumC;

	public FeatureCommandInstances(String class1, String class2, String class3, String class4, String class5,
			int hasCCRERelationship, int hasExecutorCCRelationship, int hasExecutor, int hasConcreteCommand,
			int hasReceiver, int hasExecutorClient, int hasInvoker, /* int executionRelationship, */
			int numC) {
		this.class1 = class1;
		this.class2 = class2;
		this.class3 = class3;
		this.class4 = class4;
		this.class5 = class5;
		this.HasExecutorClient = hasExecutorClient;
		this.HasInvoker = hasInvoker;
		this.HasConcreteCommand = hasConcreteCommand;
		this.HasReceiver = hasReceiver;
		this.HasCCRERelationship = hasCCRERelationship;
		this.HasExecutorCCRelationship = hasExecutorCCRelationship;
		// this.ExecutionRelationship = executionRelationship;
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

	public int getHasExecutorCCRelationship() {
		return HasExecutorCCRelationship;
	}

	public void setHasExecutorCCRelationship(int hasExecutorCCRelationship) {
		HasExecutorCCRelationship = hasExecutorCCRelationship;

	}

	public int getHasCCRERelationship() {
		return HasCCRERelationship;
	}

	public void setHasCCRERelationship(int hasCCRERelationship) {
		HasCCRERelationship = hasCCRERelationship;
	}

	public int getHasExecutorClient() {
		return HasExecutorClient;
	}

	public void setHasExecutorClient(int hasExecutorClient) {
		HasExecutorClient = hasExecutorClient;
	}

	/*
	 * public int getExecutionRelationship() {
	 * return ExecutionRelationship;
	 * }
	 * 
	 * public void setExecutionRelationship(int executionRelationship) {
	 * ExecutionRelationship = executionRelationship;
	 * }
	 */

	public int getHasConcreteCommand() {
		return HasConcreteCommand;
	}

	public void setHasConcreteCommand(int hasConcreteCommand) {
		HasConcreteCommand = hasConcreteCommand;
	}

	public int getHasReceiver() {
		return HasReceiver;
	}

	public void setHasReceiver(int hasReceiver) {
		HasReceiver = hasReceiver;
	}

	public void setNumC(int numC) {
		NumC = numC;
	}

	public int getNumC() {
		return NumC;
	}

	@Override
	// TODO: AGGIORNARE
	public String toString() {
		return ("Classe 1: " + class1 + ", Classe 2: " + class2 + ", Classe 3: " + class3 + ", Classe 4: " + class4
				+ ", Classe 5: " + class5);
	}

	public int getHasInvoker() {
		return HasInvoker;
	}

	public void setHasInvoker(int hasInvoker) {
		HasInvoker = hasInvoker;
	}

}
