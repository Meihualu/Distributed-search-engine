package fr.univtours.polytech.MoteurHadoop.filter;

import java.text.Normalizer;

/**
 * Classe de filtre permettant d'éliminer les accents et les caractères spéciaux
 * d'une chaîne de caractères.
 * 
 * @author Chao JIANG, Yuxin SHENG
 */
public class AccentFilter implements Filter {

	/**
	 * retourner le resulat dans un nouveau string {@inheritDoc}
	 * 
	 * @see fr.univtours.polytech.di.multimedia.filters.Filter#filter(java.lang.String)
	 */
	@Override
	public String filter(final String sign) {
		try {
			final String result = Normalizer.normalize(sign,
					Normalizer.Form.NFD);
			return result.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		} catch (final NullPointerException e) {
			System.out.println(this.getClass().getName()
					+ ".filter - sign vide: " + e.toString());
			throw new RuntimeException("filter error", e);
		}
	}

}
