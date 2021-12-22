package dataset;

public class Feature3 {
	
	
	
	
	
	public Feature3(String classes, int hasSubject,String nomeSub, int hasObserver,String nomeObs ,int subjectsRelationship,
			int subObsDepedencies, int cSubObsDependencies, int observersRelationship, int callListeners,
			int cObsAccessSubject, int noc, String cs , String co, boolean SubRel,  boolean ObsRel , boolean dip1 , boolean dip2) {
		super();
		Classes = classes;
		HasSubject = hasSubject;
		HasObserver = hasObserver;
		SubjectsRelationship = subjectsRelationship;
		SubObsDepedencies = subObsDepedencies;
		CSubObsDependencies = cSubObsDependencies;
		ObserversRelationship = observersRelationship;
		CallListeners = callListeners;
		CObsAccessSubject = cObsAccessSubject;
		Noc = noc;
		nomeSubject=nomeSub;
		nomeObserver=nomeObs;
	    nomeConcreteObserver=co;
	    nomeConcreteSubject=cs;
	    controlloSubRel=SubRel;
		controlloObsRel=ObsRel;
		this.controlloDipSub=dip1;
		this.controlloDipCSub=dip2;
		
	}
	
	//_____________________________________________________________________________________________________________
	
	
	
	public String getClasses() {
		return Classes;
	}
	public void setClasses(String classes) {
		Classes = classes;
	}
	public int getHasSubject() {
		return HasSubject;
	}
	public void setHasSubject(int hasSubject) {
		HasSubject = hasSubject;
	}
	public int getHasObserver() {
		return HasObserver;
	}
	public void setHasObserver(int hasObserver) {
		HasObserver = hasObserver;
	}
	public int getSubjectsRelationship() {
		return SubjectsRelationship;
	}
	public void setSubjectsRelationship(int subjectsRelationship) {
		SubjectsRelationship = subjectsRelationship;
	}
	public int getSubObsDepedencies() {
		return SubObsDepedencies;
	}
	public void setSubObsDepedencies(int subObsDepedencies) {
		SubObsDepedencies = subObsDepedencies;
	}
	public int getCSubObsDependencies() {
		return CSubObsDependencies;
	}
	public void setCSubObsDependencies(int cSubObsDependencies) {
		CSubObsDependencies = cSubObsDependencies;
	}
	public int getObserversRelationship() {
		return ObserversRelationship;
	}
	public void setObserversRelationship(int observersRelationship) {
		ObserversRelationship = observersRelationship;
	}
	public int getCallListeners() {
		return CallListeners;
	}
	public void setCallListeners(int callListeners) {
		CallListeners = callListeners;
	}
	public int getCObsAccessSubject() {
		return CObsAccessSubject;
	}
	public void setCObsAccessSubject(int cObsAccessSubject) {
		CObsAccessSubject = cObsAccessSubject;
	}
	public int getNoc() {
		return Noc;
	}
	public void setNoc(int noc) {
		Noc = noc;
	}
	
	public void setNomeSubject(String nomeSubject) {
		this.nomeSubject = nomeSubject;
	}

	public String getNomeObserver() {
		return nomeObserver;
	}

	public void setNomeObserver(String nomeObserver) {
		this.nomeObserver = nomeObserver;
	}	
	
	public String getNomeSubject() {
		return nomeSubject;
	}
	
	public String getNomeConcreteObserver() {
		return nomeConcreteObserver;
	}

	public void setNomeConcreteObserver(String nomeConcreteObserver) {
		this.nomeConcreteObserver = nomeConcreteObserver;
	}

	public String getNomeConcreteSubject() {
		return nomeConcreteSubject;
	}

	public void setNomeConcreteSubject(String nomeConcreteSubject) {
		this.nomeConcreteSubject = nomeConcreteSubject;
	}
	
	public boolean getControlloSubRel() {
		return controlloSubRel;
	}

	public void setControlloSubRel(boolean controlloSubRel) {
		this.controlloSubRel = controlloSubRel;
	}

	public boolean getControlloObsRel() {
		return controlloObsRel;
	}

	public void setControlloObsRel(boolean controlloObsRel) {
		this.controlloObsRel = controlloObsRel;
	}
	
	public void setControlloDipSub(boolean controlloDipSub) {
		this.controlloDipSub = controlloDipSub;
	}


	public void setControlloDipCSub(boolean controlloDipCSub) {
		this.controlloDipCSub = controlloDipCSub;
	}
	
	public boolean getControlloDipSub() {
		return this.controlloDipSub;
	}
	
	public boolean getControlloDipCSub() {
		return this.controlloDipCSub;
	}

	
	
	@Override
	public String toString() {
		return "Feature3 [Classes=" + Classes + ", HasSubject=" + HasSubject + ", HasObserver=" + HasObserver
				+ ", SubjectsRelationship=" + SubjectsRelationship + ", SubObsDepedencies=" + SubObsDepedencies
				+ ", CSubObsDependencies=" + CSubObsDependencies + ", ObserversRelationship=" + ObserversRelationship
				+ ", CallListeners=" + CallListeners + ", CObsAccessSubject=" + CObsAccessSubject + ", Noc=" + Noc
				+ ", nomeSubject=" + nomeSubject + ", nomeObserver=" + nomeObserver + ", nomeConcreteObserver="
				+ nomeConcreteObserver + ", nomeConcreteSubject=" + nomeConcreteSubject + ", controlloSubRel="
				+ controlloSubRel + ", controlloObsRel=" + controlloObsRel + "]";
	}
	

	
	
	
	

	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////



	private String Classes;
	private int HasSubject;
	private int HasObserver;
	private int SubjectsRelationship;
	private int SubObsDepedencies;
	private int CSubObsDependencies;
	private int ObserversRelationship;
	private int CallListeners;
	private int CObsAccessSubject;
	private int Noc;
	private String nomeSubject;
	private String nomeObserver;
	private String nomeConcreteObserver;
	private String nomeConcreteSubject;
	private boolean controlloSubRel;
	private boolean controlloObsRel;
	private boolean controlloDipSub;
	private boolean controlloDipCSub;
	public boolean isControlloDipSub() {
		return controlloDipSub;
	}

	

	


}
