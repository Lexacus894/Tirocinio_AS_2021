package dataset;

public class strutturaVariabile {
	
	public strutturaVariabile(String n, boolean l) {
		
		this.nomeVar= n;
		this.localeGlobale=l;
		
	}
	
	
	public String getNomeVariabile() {
		return nomeVar;
	}
	public void setNomeVariabile(String nomeVar) {
		this.nomeVar = nomeVar;
	}
	public boolean isLocaleGlobale() {
		return localeGlobale;
	}
	public void setLocaleGlobale(boolean localeGlobale) {
		this.localeGlobale = localeGlobale;
	}
	
	public boolean getlocaleGlobale() {
		return this.localeGlobale;
	}

	 @Override
	  public String toString() {
		  
		  return " strutturaVariabile [NomeVariabile=" + this.nomeVar + ", localeGlobale="+this.localeGlobale + "]";
	  
	  }
	
	private String nomeVar;
	
	//la variabile è settata a TRUE se la variabile è locale , FALSE 
	// se risulta essere globale
	private boolean localeGlobale;
}
