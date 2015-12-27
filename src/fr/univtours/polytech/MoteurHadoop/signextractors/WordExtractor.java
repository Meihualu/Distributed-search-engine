package fr.univtours.polytech.MoteurHadoop.signextractors;


public class WordExtractor implements SignExtractor {

  private String[] items;
  private int cursor;
	
  @Override
  public String nextToken() {
	// Si le curseur est arrive a la fin de la chaine de caracteres
	if(cursor == items.length)
	  return null;
	// Sinon on retourne le prochain mot
	else {
	  cursor++;
	  return items[cursor-1];
	}
  }

  @Override
  public void setContent(final String content) {
	// On coupe la phrase quand on rencontre un espace ou un caractere special
    items = content.split("[^a-zàâéèêîôûùA-Z0-9]+");
    
    // On initialise le curseur a 0
    cursor = 0;
  }
}