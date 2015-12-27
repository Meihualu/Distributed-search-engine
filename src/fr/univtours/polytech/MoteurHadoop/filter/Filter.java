package fr.univtours.polytech.MoteurHadoop.filter;

public interface Filter {
  /**
   * Filtre le parametre.
   * @param sign la suite de caracteres a filtrer
   * @return la chaine de caracteres filtree ou null si le signe doit etre
   *         elimine.
   */
  String filter(String sign);
}
