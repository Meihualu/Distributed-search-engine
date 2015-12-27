package fr.univtours.polytech.MoteurHadoop.signextractors;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hduser
 */
public class NGramExtractor implements SignExtractor {

  private int n;
  private int cursor;
  private List<String> items;
	
  /**
   * Le constructeur.
   * @param size la taille des N-grams
   */
  public NGramExtractor(final int size) {
   n = size;
  }

  @Override
  public String nextToken() {
	  // Si on est a la fin du mot
	  if(cursor == items.size())
		  return null;
	  // Sinon on retourne le prochain 'bloc'
	  else {
		  cursor++;
		  return items.get(cursor-1);
		}
  }

  @Override
  public void setContent(final String content) {
    items = new ArrayList<String>();
    int i = 0;
    
    // Tant que l'on est pas a la fin du mot
    while((i+n)<=content.length())	{
    	// On ajoute a la liste le prochain 'bloc'
    	items.add(content.substring(i, i+n));
    	i++;
    }
    // On initialise le curseur a 0
	cursor = 0;
  }
}