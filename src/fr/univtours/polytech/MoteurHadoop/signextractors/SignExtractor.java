package fr.univtours.polytech.MoteurHadoop.signextractors;

public interface SignExtractor {
  /**
   * Permet d'obtenir le signe suivant.
   * @return le signe ou null s'il n'y a plus de signes a extraire
   */
  public String nextToken();

  /**
   * Permet de definir le chaine de caracteres a partir de laquelle les signes
   * seront extraits.
   * @param content la chaine de caractere
   */
  public void setContent(String content);
}
