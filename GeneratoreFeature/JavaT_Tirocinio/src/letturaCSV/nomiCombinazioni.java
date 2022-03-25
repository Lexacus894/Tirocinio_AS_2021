package letturaCSV;


public class nomiCombinazioni {
	

	
	public nomiCombinazioni(String classe1, String classe2, String classe3, String classe4) {
		super();
		this.classe1 = classe1;
		this.classe2 = classe2;
		this.classe3 = classe3;
		this.classe4 = classe4;
	}
	
	
	public String getClasse1() {
		return classe1;
	}
	public void setClasse1(String classe1) {
		this.classe1 = classe1;
	}
	public String getClasse2() {
		return classe2;
	}
	public void setClasse2(String classe2) {
		this.classe2 = classe2;
	}
	public String getClasse3() {
		return classe3;
	}
	public void setClasse3(String classe3) {
		this.classe3 = classe3;
	}
	public String getClasse4() {
		return classe4;
	}
	public void setClasse4(String classe4) {
		this.classe4 = classe4;
	}
	
	
	@Override
	public String toString() {
		return "nomiCombinazioni [classe1 = " + classe1 + ", classe2 = " + classe2 + ", classe3 = " + classe3 + ", classe4 = "
				+ classe4 + "]";
	}
	
	
	private String classe1;
	private String classe2;
	private String classe3;
	private String classe4;
	


}
