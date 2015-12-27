package fr.univtours.polytech.MoteurHadoop.filter;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;


/**
 * Classe de filtrage implémentant un filtre de mots vides.
 * @author Chao JIANG, Yuxin SHENG
 */
public class StopWordFilter implements Filter {

	final private boolean isCaseFilterApplied;
	final private boolean isAccentFilterApplied;
	private Set<String> stopWordSet = null;
	private Set<String> stopWordSetFilted = null;
  /**
   * Le constructeur.
   * @param caseFilterApplied indique si les signes ont été filtrés en minuscule
   * @param accentFilterApplied indique si les signes ont été filtrés sans
   *          accent et sans caractères spéciaux
   */
  public StopWordFilter(final boolean caseFilterApplied,
      final boolean accentFilterApplied) {
	  
    // 100 signs le plus frequents dans les documents
	  isCaseFilterApplied = caseFilterApplied;
	  isAccentFilterApplied = accentFilterApplied;
      stopWordSet = new HashSet<String>();
      stopWordSetFilted = new HashSet<String>(); 
	  stopWordSet.add("de");
	  stopWordSet.add("la");
	  stopWordSet.add("et");
	  stopWordSet.add("le");
	  stopWordSet.add("à");
	  stopWordSet.add("l");
	  stopWordSet.add("des");
	  stopWordSet.add("les");
	  stopWordSet.add("d");
	  stopWordSet.add("en");
	  stopWordSet.add("du");
	  stopWordSet.add("un");
	  stopWordSet.add("est");
	  stopWordSet.add("a");
	  stopWordSet.add("une");
	  stopWordSet.add("pour");
	  stopWordSet.add("dans");
	  stopWordSet.add("que");
	  stopWordSet.add("qui");
	  stopWordSet.add("au");
	  stopWordSet.add("par");
	  stopWordSet.add("sur");
	  stopWordSet.add("plus");
	  stopWordSet.add("il");
	  stopWordSet.add("pas");
	  stopWordSet.add("avec");
	  stopWordSet.add("Le");
	  stopWordSet.add("son");
	  stopWordSet.add("s");
	  stopWordSet.add("se");	  
	  stopWordSet.add("ce");
	  stopWordSet.add("n");
	  stopWordSet.add("La");
	  stopWordSet.add("qu");
	  stopWordSet.add("Les");
	  stopWordSet.add("ou");
	  stopWordSet.add("ne");
	  stopWordSet.add("aux");
	  stopWordSet.add("sont");
	  stopWordSet.add("ont");
	  stopWordSet.add("Il");
	  stopWordSet.add("ses");
	  stopWordSet.add("été");
	  stopWordSet.add("L");
	  stopWordSet.add("sa");
	  stopWordSet.add("on");
	  stopWordSet.add("cette");
	  stopWordSet.add("comme");
	  stopWordSet.add("deux");
	  stopWordSet.add("mais");
	  stopWordSet.add("fait");
	  stopWordSet.add("nous");
	  stopWordSet.add("leur");
	  stopWordSet.add("tout");
	  stopWordSet.add("année");
	  stopWordSet.add("c");
	  stopWordSet.add("h");
	  stopWordSet.add("même");
	  stopWordSet.add("ans");
	  stopWordSet.add("En");
	  stopWordSet.add("être");
	  stopWordSet.add("très");
	  stopWordSet.add("elle");
	  stopWordSet.add("aussi");
	  stopWordSet.add("France");
	  stopWordSet.add("C");
	  stopWordSet.add("y");
	  stopWordSet.add("bien");
	  stopWordSet.add("vous");
	  stopWordSet.add("autres");
	  stopWordSet.add("entre");
	  stopWordSet.add("je");
	  stopWordSet.add("film");
	  stopWordSet.add("groupe");	  
	  stopWordSet.add("était");
	  stopWordSet.add("faire");
	  stopWordSet.add("lui");
	  stopWordSet.add("dont");
	  stopWordSet.add("Pour");
	  stopWordSet.add("années");
	  stopWordSet.add("premier");
	  stopWordSet.add("ème");
	  stopWordSet.add("où");
	  stopWordSet.add("A");
	  stopWordSet.add("ces");
	  stopWordSet.add("recherche");
	  stopWordSet.add("ils");
	  stopWordSet.add("après");  
	  stopWordSet.add("musique");
	  stopWordSet.add("monde");
	  stopWordSet.add("avait");
	  stopWordSet.add("encore");
	  stopWordSet.add("peut");
	  stopWordSet.add("rock");
	  stopWordSet.add("metal");
	  stopWordSet.add("depuis");
	  stopWordSet.add("si");
	  stopWordSet.add("M");
	  stopWordSet.add("avril");
	  stopWordSet.add("avant");
  }

  /**
   * {@inheritDoc}
   * @see fr.univtours.polytech.di.multimedia.filters.Filter#filter(java.lang.String)
   */
  @Override
  public String filter(final String sign) {

	  //signe est deja traite par AccentFilter et CaseFilter, maintenant, on fait le meme pour l'ensemble de
	  //stopwords
	  try {
		  if (isAccentFilterApplied == true){
				for (String str : stopWordSet){
					str = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll(
							"\\p{InCombiningDiacriticalMarks}+", "");
					if(isCaseFilterApplied)
						str = str.toLowerCase();
					stopWordSetFilted.add(str);
				}
			} else {
				for (String str : stopWordSet) {
					if(isCaseFilterApplied)
						str = str.toLowerCase();
					stopWordSetFilted.add(str);
				}
			}
	  } catch(Exception e) {
		  System.out.println(this.getClass().getName() + ".StopWordFilter - filter error" + e.toString());
		  throw new RuntimeException("StopWordFilter error", e);
	  }

	if(stopWordSetFilted.contains(sign))
		return null;
	else
		return sign;
  }

}