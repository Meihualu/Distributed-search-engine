package fr.univtours.polytech.MoteurHadoop.filter;

/**
 * Classe permettant de convertir un signe en minuscule.
 * @author Chao JIANG, Yuxin SHENG
 */
public class CaseFilter implements Filter {
  
	  /**
	   * {@inheritDoc}
	   * @see fr.univtours.polytech.di.multimedia.filters.Filter#filter(java.lang.String)
	   */
	  @Override
	  public String filter(final String sign) {
	    
	    return sign.toLowerCase();
	  }

}
