package fr.univtours.polytech.MoteurHadoop.filter;

/**
 * Classe de filtre réalisant un stemming.
 * @author Chao JIANG, Yuxin SHENG
 */
public class StemmingFilter implements Filter {

	private boolean step2 = true;

  /**
   * {@inheritDoc}
   * @see fr.univtours.polytech.di.multimedia.filters.Filter#filter(java.lang.String)
   * only Standard suffix removal
   */
  @Override
  public String filter(final String sign) {
	  
	  //seulement pour les mots assez long. ici on definit longeur > 4.
	  //il y a totalement 7 étapes.
	  if(sign.length() > 4) {
		  String str1, str2, str3, str4, str5, str6, str7;
		  boolean step3 = false, step4 = false;
		  try {
			  str1 = step1(sign);
		  } catch (Exception e) {
			  System.out.println(this.getClass().getName() + ".StemmingFilter - step1 error: " + e.toString()); 
			  throw new RuntimeException("StemmingFilter error", e);
		  }
		  if(str1.equals(sign)){
			  step3 = true;
			  step2 = true;
		  }
		  //Do step 2a if either no ending was removed by step 1, or if one of endings amment, emment, ment, ments was found. 
		  if(step2){
			  step3 = false;
			  try {
				  str2 = step2a(str1);
				  if(str2.equals(str1)) str2 = step2b(str1);
				  if(!str2.equals(str1)) step3 = true;		
			  } catch (Exception e) {
				  System.out.println(this.getClass().getName() + ".StemmingFilter - step2 error: " + e.toString()); 
				  throw new RuntimeException("StemmingFilter error", e);
			  }
		  }
		  else
			  str2 = str1;
		  //If the last step to be obeyed — either step 1, 2a or 2b — altered the word, do step 3 
		  if(step3) {
			  try {
				  str3 = step3(str2);
			  } catch (Exception e) {
				  System.out.println(this.getClass().getName() + ".StemmingFilter - step3 error: " + e.toString()); 
				  throw new RuntimeException("StemmingFilter error", e);
			  }
		  }
		  else{
			  str3 = str2;
			  step4 = true;
		  }
		  //Alternatively, if the last step to be obeyed did not alter the word, do step 4 
		  if(step4) {
			  try {
				  str4 = step4(str3);
			  } catch (Exception e) {
				  System.out.println(this.getClass().getName() + ".StemmingFilter - step4 error: " + e.toString()); 
				  throw new RuntimeException("StemmingFilter error", e);
			  }
		  }
		  else
			  str4 = str3;
		  try {
			  str5 = step5(str4);
		  } catch (Exception e) {
			  System.out.println(this.getClass().getName() + ".StemmingFilter - step5 error: " + e.toString()); 
			  throw new RuntimeException("StemmingFilter error", e);
		  }
		  
		  try {
			  str6 = step6(str5);
		  } catch (Exception e) {
			  System.out.println(this.getClass().getName() + ".StemmingFilter - step6 error: " + e.toString()); 
			  throw new RuntimeException("StemmingFilter error", e);
		  }
		  
		  try {
			  str7 = step7(str6);
		  } catch (Exception e) {
			  System.out.println(this.getClass().getName() + ".StemmingFilter - step7 error: " + e.toString()); 
			  throw new RuntimeException("StemmingFilter error", e);
		  }
		  return str7;
	  }
	  return sign;
  }
  
  /***
   * If the word begins with two vowels, 
   * RV is the region after the third letter, 
   * otherwise the region after the first vowel not at the 
   * beginning of the word, or the end of the word if these positions
   * cannot be found. (Exceptionally, par, col or tap, at the begining 
   * of a word is also taken to define RV as the region to their right.)
   * @param str
   * @return RV
   */
  public String getRV(String str){
	  if(str.equals("")) return "";
	  
	  if(str.startsWith("par") || str.startsWith("col") || str.startsWith("tap"))
		  return str.substring(3);
	  
	  if(str.length() > 3 && (isVowel(str.charAt(0)) || isVowel(str.charAt(1))))
		  return str.substring(2);
	  
	  for(int i = 1; i < str.length(); i ++){
		  if(isVowel(str.charAt(i)))
				  return str.substring(i);
	  }
	  return "";
  }
  
  /***
   * R1 is the region after the first 
   * non-vowel following a vowel, or is the
   * null region at the end of the word if there is no such non-vowel. (not first letter)
   * @param str
   * @return R1
   */
  public String getR1(String str){
	  for (int i = 1; i < str.length() - 1; i ++){
		  if(!isVowel(str.charAt(i)) && isVowel(str.charAt(i + 1))){
			  return str.substring(i + 1);
		  }
	  }
	  return "";
  }
  
  /***
   * R2 is the region after the first non-vowel
   * following a vowel in R1, or is the null region
   * at the end of the word if there is no such non-vowel. 
   * @param str R1
   * @return R2
   */
  public String getR2(String str){
	  for (int i = 1; i < str.length() - 1; i ++){
		  if(!isVowel(str.charAt(i)) && isVowel(str.charAt(i + 1))){
			  return str.substring(i + 1);
		  }
	  }
	  return "";  
  }
  
  /***
   * 
   * @param str
   */
  public void getUpperCase(String str){
	  //put into upper case u or i preceded and followed by a vowel
	  for (int i = 1; i < str.length() -1; i++){
		  if(isVowel(str.charAt(i - 1)) && isVowel(str.charAt(i + 1)) &&
				  (str.charAt(i) == 'u' || str.charAt(i) == 'i')){
			  Character.toUpperCase(str.charAt(i));
		  }
	  }
	  //put into upper case y followed by a vowel, q followed by u
	  for (int i = 0; i < str.length() - 1; i ++){
		  if(isVowel(str.charAt(i + 1)) &&
				  str.charAt(i) == 'y'){
			  Character.toUpperCase(str.charAt(i));
		  }
		  if(str.charAt(i + 1) == 'u' &&
				  str.charAt(i) == 'q'){
			  Character.toUpperCase(str.charAt(i));
		  }		  
	  }
	  //put into upper case y preceded by a vowel
	  for (int i = 1; i < str.length(); i ++){
		  if(isVowel(str.charAt(i - 1)) &&
				  str.charAt(i) == 'y'){
			  Character.toUpperCase(str.charAt(i));
		  }		  
	  }
  }
  
  /***
   * 
   * @param ch
   * @return
   */
  public boolean isVowel(char ch){
	  switch(ch)
	  {
	  	case('a'):return true;
	  	case('e'):return true;
	  	case('i'):return true;
	  	case('o'):return true;
	  	case('u'):return true;
	  	case('y'):return true;
	  	case('â'):return true;
	  	case('à'):return true;
	  	case('é'):return true;
	  	case('è'):return true;
	  	case('ê'):return true;
	  	case('î'):return true;
	  	case('ô'):return true;
	  	case('û'):return true;
	  	case('ù'):return true;
	  	case('ë'):return true;
	  	case('ï'):return true;
	  }
	  return false;
  }
  
  /***
   * replacer un surfix.
   * @param sign
   * 				le string à traiter.
   * @param target
   * 				le surfix à rechercher.
   * @param str
   * 				le string à remplacer le target.
   * @return
   */
  private String replaceLast(String sign, String target, String str){
	  if(sign.endsWith(target)){
		  int pos = sign.lastIndexOf(target);
		  String Res = sign.substring(0, pos) + str;
		  return Res;
	  }
	  return sign;
  }
  
  /***
   * verifier la lettre avant un sous string dans un string.
   * @param sign
   * 				le string.
   * @param str
   * 				le sous string. 
   * @param before
   * 				la lettre avant sous string.
   * @return
   */
  private boolean beforeBy(String sign, String str, String before){
	  if(!sign.endsWith(str))
		  return false;
	  if(sign.indexOf(str) < before.length())
		  return false;
	  else{
		  int pos = sign.indexOf(str);
		  if(sign.substring(pos - before.length(), pos).equals(before))
			  return true;
		  else
			  return false;
	  }
  }
  
  /***
   *  verifier un sous string est apres un vowel ou pas.
   * @param sign
   * @param str
   * @return
   */
  private boolean beforeByNoVowel(String sign, String str){
	  if(!sign.endsWith(str))
		  return false;
	  if(sign.indexOf(str) < 1)
		  return false;
	  else{
		  int pos = sign.indexOf(str);
		  if(!isVowel(sign.charAt(pos - 1)))
			  return true;
		  else
			  return false;
	  }
  }
  
  /***
   * Step 1: Standard suffix removal
   * @param sign
   * @return
   */
  private String step1(String sign){
	  String RV, R1, R2, Res = sign;
	  RV = getRV(sign);
	  R1 = getR1(sign);
	  R2 = getR2(R1);
	  /***
	   * issement   issements
	   * delete if in R1 
	   */
	  if(R1.contains("issements") || R1.contains("issement")){
		  Res = replaceLast(sign, "issements", "");
		  Res = replaceLast(sign, "issement", "");
		  return Res;
	  }
	  /***
	   * amment 
	   * replace with ant if in RV 
	   */
	  if(RV.contains("amment")){
		  Res = replaceLast(sign, "amment", "ant");
		  step2 = true;
		  return Res;
	  }
	  /***
	   * emment 
	   * replace with ent if in RV 
	   */
	  if(RV.contains("emment")){
		  Res = replaceLast(sign, "emment", "ent");
		  step2 = true;
		  return Res;
	  }
	  /***
	   * ance   iqUe   isme   able   iste   eux   ances   iqUes   ismes   ables   istes
	   * delete if in R2 
	   */
	  if(R2.contains("ance") || R2.contains("iqUes") || R2.contains("ismes") || R2.contains("ables") || 
			  R2.contains("istes") || R2.contains("eux") || R2.contains("ances") || 
			  R2.contains("iqUe") || R2.contains("isme") || R2.contains("able") ||
			  R2.contains("iste")){
		  Res = replaceLast(sign, "ance", "");
		  Res = replaceLast(sign, "iqUes", "");
		  Res = replaceLast(sign, "ismes", "");
		  Res = replaceLast(sign, "ables", "");
		  Res = replaceLast(sign, "istes", "");
		  Res = replaceLast(sign, "eux", "");
		  Res = replaceLast(sign, "ances", "");
		  Res = replaceLast(sign, "iqUe", "");
		  Res = replaceLast(sign, "isme", "");
		  Res = replaceLast(sign, "able", "");
		  Res = replaceLast(sign, "iste", "");
				  return Res;
	  }
	  /***
	   * atrice   ateur   ation   atrices   ateurs   ations
	   * delete if in R2
	   * if preceded by ic, delete if in R2, else replace by iqU 
	   */
	  if(beforeBy(sign, "atrices","ic") || beforeBy(sign, "ateurs","ic") || 
			  beforeBy(sign, "ations","ic") || beforeBy(sign, "atrice","ic") || 
			  beforeBy(sign, "ation","ic")){
		  if(R2.contains("atrices") || R2.contains("ateurs") || R2.contains("ations") ||
				  R2.contains("atrice") || R2.contains("ation")){
			  Res = replaceLast(sign, "atrices", "");
			  Res = replaceLast(sign, "ateurs", "");
			  Res = replaceLast(sign, "ations", "");
			  Res = replaceLast(sign, "atrice", "");
			  Res = replaceLast(sign, "ation", "");
					  return Res;
		  }
		  else{
			  Res = replaceLast(sign, "atrices", "iqU");
			  Res = replaceLast(sign, "ateurs", "iqU");
			  Res = replaceLast(sign, "ations", "iqU");
			  Res = replaceLast(sign, "atrice", "iqU");
			  Res = replaceLast(sign, "ation", "iqU");
			  return Res;
		  }
	  }
	  if(R2.contains("atrices") || R2.contains("ateurs") || R2.contains("ations") ||
			  R2.contains("atrice") || R2.contains("ation")){
		  Res = replaceLast(sign, "atrices", "");
		  Res = replaceLast(sign, "ateurs", "");
		  Res = replaceLast(sign, "ations", "");
		  Res = replaceLast(sign, "atrice", "");
		  Res = replaceLast(sign, "ation", "");
				  return Res;
	  }
	  /***
	   * logie   logies
	   * replace with log if in R2 
	   */
	  if(R2.contains("logies") || R2.contains("logie")){
		  Res = replaceLast(sign, "logies", "log");
		  Res = replaceLast(sign, "logie", "log");
				  return Res;
	  }
	  /***
	   * usion   ution   usions   utions
	   * replace with u if in R2 
	   */
	  if(R2.contains("usions") || R2.contains("utions") || R2.contains("usion") || 
			  R2.contains("ution")){
		  Res = replaceLast(sign, "usions", "u");
		  Res = replaceLast(sign, "utions", "u");
		  Res = replaceLast(sign, "usion", "u");
		  Res = replaceLast(sign, "ution", "u");
				  return Res;
	  }
	  /***
	   * ence   ences
	   * replace with ent if in R2 
	   */
	  if(R2.contains("ences") || R2.contains("ence")){
		  Res = replaceLast(sign, "ences", "ent");
		  Res = replaceLast(sign, "ence", "ent");  
				  return Res;
	  }
	  /***
	   * ement   ements
	   * delete if in RV
	   * if preceded by iv, delete if in R2 (and if further preceded by at, delete if in R2), otherwise,
	   * if preceded by eus, delete if in R2, else replace by eux if in R1, otherwise,
	   * if preceded by abl or iqU, delete if in R2, otherwise,
	   * if preceded by ièr or Ièr, replace by i if in RV 
	   */
	  if(beforeBy(sign, "ements", "iv") || beforeBy(sign, "ement", "iv") ){
		  if(R2.contains("ements") || R2.contains("ement")){
			  Res = replaceLast(sign, "ements", "");
			  Res = replaceLast(sign, "ement", "");
			  return Res;
			  }
	  }
	  if(beforeBy(sign, "ements", "eus") || beforeBy(sign, "ement", "eus") ){
		  if(R2.contains("ements") || R2.contains("ement")){
			  Res = replaceLast(sign, "ements", "");
			  Res = replaceLast(sign, "ement", "");
			  return Res;
			  }
		  if(R1.contains("ements") || R1.contains("ement")){
			  Res = replaceLast(sign, "ements", "eux");
			  Res = replaceLast(sign, "ement", "eux");
			  return Res;
			  }
	  }
	  if(beforeBy(sign, "ements", "abl") || beforeBy(sign, "ement", "able") ||
			  beforeBy(sign, "ements", "iqU") || beforeBy(sign, "ement", "iqU") ){
		  if(R2.contains("ements") || R2.contains("ement")){
			  Res = replaceLast(sign, "ements", "");
			  Res = replaceLast(sign, "ement", "");
			  return Res;
			  }
	  }
	  if(beforeBy(sign, "ements", "ièr") || beforeBy(sign, "ement", "ièr") ||
			  beforeBy(sign, "ements", "Ièr") || beforeBy(sign, "ement", "Ièr") ){
		  if(RV.contains("ements") || RV.contains("ement")){
			  Res = replaceLast(sign, "ements", "i");
			  Res = replaceLast(sign, "ement", "i");
			  return Res;
			  }
	  }
	  if(RV.contains("ements") || RV.contains("ement")){
		  Res = replaceLast(sign, "ements", "");
		  Res = replaceLast(sign, "ement", "");
		  return Res;
		  }
	  /***
	   * ment   ments
	   * delete if in RV
	   */
	  if(RV.contains("ments") || RV.contains("ment")){
		  Res = replaceLast(sign, "ments", "");
		  Res = replaceLast(sign, "ment", "");
		  step2 = true;
		  return Res;
	  }
	  /***
	   * ité   ités*
	   * delete if in R2
	   * if preceded by abil, delete if in R2, else replace by abl, otherwise,
	   * if preceded by ic, delete if in R2, else replace by iqU, otherwise,
	   * if preceded by iv, delete if in R2 
	   */
	  if(beforeBy(sign, "ité", "abil") || beforeBy(sign, "ités", "abil") ){
		  if(R2.contains("ité") || R2.contains("ités")){
			  Res = replaceLast(sign, "ités", "");
			  Res = replaceLast(sign, "ités", "");
			  return Res;
			  }
		  else{
			  Res = replaceLast(sign, "ités", "abl");
			  Res = replaceLast(sign, "ités", "abl");
			  return Res;
		  }
	  }
	  if(beforeBy(sign, "ité", "ic") || beforeBy(sign, "ités", "ic") ){
		  if(R2.contains("ité") || R2.contains("ités")){
			  Res = replaceLast(sign, "ités", "");
			  Res = replaceLast(sign, "ités", "");
			  return Res;
			  }
		  else{
			  Res = replaceLast(sign, "ités", "iqU");
			  Res = replaceLast(sign, "ités", "iqU");
			  return Res;
		  }
	  }
	  if(beforeBy(sign, "ité", "iv") || beforeBy(sign, "ités", "iv") ){
		  if(R2.contains("ité") || R2.contains("ités")){
			  Res = replaceLast(sign, "ités", "");
			  Res = replaceLast(sign, "ités", "");
			  return Res;
			  }
	  }
	  if(R2.contains("ité") || R2.contains("ités")){
		  Res = replaceLast(sign, "ités", "");
		  Res = replaceLast(sign, "ité", "");
		  return Res;
	  }
	  /***
	   * if   ive   ifs   ives
	   * delete if in R2
	   * if preceded by at, delete if in R2 
	   * (and if further preceded by ic, delete if in R2, else replace by iqU) 
	   */
	  if(beforeBy(sign, "if", "ic") || beforeBy(sign, "ive", "ic") || beforeBy(sign, "ifs", "ic")
			  || beforeBy(sign, "ives", "ic")){
		  if(R2.contains("if") || R2.contains("ive") || R2.contains("ifs") ||
				  R2.contains("ives")){
			  Res = replaceLast(sign, "ives", "");
			  Res = replaceLast(sign, "ifs", "");
			  Res = replaceLast(sign, "ive", "");
			  Res = replaceLast(sign, "if", "");
			  return Res;
		  }
		  else{
			  Res = replaceLast(sign, "ives", "iqU");
			  Res = replaceLast(sign, "ifs", "iqU");
			  Res = replaceLast(sign, "ive", "iqU");
			  Res = replaceLast(sign, "if", "iqU");
			  return Res;
		  }
	  }
	  if(R2.contains("if") || R2.contains("ive") || R2.contains("ifs") ||
			  R2.contains("ives")){
		  Res = replaceLast(sign, "ives", "");
		  Res = replaceLast(sign, "ifs", "");
		  Res = replaceLast(sign, "ive", "");
		  Res = replaceLast(sign, "if", "");
		  return Res;
	  }
	  /***
	   *eaux replace with eau 
	   */
	  if(RV.contains("eaux")){
		  Res = replaceLast(sign, "eaux", "eau");
		  return Res;
	  }
	  if(R2.contains("eaux")){
		  Res = replaceLast(sign, "eaux", "eau");
		  return Res;
	  }
	  /***
	   *aux replace with al if in R1 
	   */
	  if(R1.contains("aux")){
		  Res = replaceLast(sign, "aux", "al");
		  return Res;
	  }
	  /***
	   *euse   euses 
	   *delete if in R2, else replace by eux if in R1 
	   */
	  if(R2.contains("euses") || R2.contains("euse")){
		  Res = replaceLast(sign, "euses", "");
		  Res = replaceLast(sign, "euse", "");
		  return Res;
	  }
	  if(R1.contains("euses") || R1.contains("euse")){
		  Res = replaceLast(sign, "euses", "eux");
		  Res = replaceLast(sign, "euse", "eux");
		  return Res;
	  }
    return Res;
  }

  /***
   * Do step 2a if either no ending was removed by step 1,
   * or if one of endings amment, emment, ment, ments was found. 
   * @param sign
   * @return
   */
  private String step2a(String sign){
	  String Res = sign;
	  if(beforeByNoVowel(sign, "îmes") || beforeByNoVowel(sign, "ît") || beforeByNoVowel(sign, "îtes") || 
			  beforeByNoVowel(sign, "i") || beforeByNoVowel(sign, "ie") ||
			  beforeByNoVowel(sign, "ies") || beforeByNoVowel(sign, "ir") ||
			  beforeByNoVowel(sign, "ira") || beforeByNoVowel(sign, "irai") ||
			  beforeByNoVowel(sign, "iraIent") || beforeByNoVowel(sign, "irais") ||
			  beforeByNoVowel(sign, "irait") || beforeByNoVowel(sign, "iras") ||
			  beforeByNoVowel(sign, "irent") || beforeByNoVowel(sign, "irez") ||
			  beforeByNoVowel(sign, "iriez") || 
			  beforeByNoVowel(sign, "irions") || beforeByNoVowel(sign, "irons") ||
			  beforeByNoVowel(sign, "iront") || beforeByNoVowel(sign, "is") ||
			  beforeByNoVowel(sign, "issaIent") || beforeByNoVowel(sign, "issais") ||
			  beforeByNoVowel(sign, "issait") || beforeByNoVowel(sign, "issant") ||
			  beforeByNoVowel(sign, "issante") || beforeByNoVowel(sign, "issantes") ||
			  beforeByNoVowel(sign, "issants") || beforeByNoVowel(sign, "isse") || 
			  beforeByNoVowel(sign, "issent") || 
			  beforeByNoVowel(sign, "isses") || beforeByNoVowel(sign, "issez") ||
			  beforeByNoVowel(sign, "issiez") || beforeByNoVowel(sign, "issions") ||
			  beforeByNoVowel(sign, "issons") || beforeByNoVowel(sign, "it")){
		  Res = replaceLast(sign, "issaIent", "");
		  Res = replaceLast(sign, "issantes", "");
		  Res = replaceLast(sign, "issante", "");
		  Res = replaceLast(sign, "issants", "");
		  Res = replaceLast(sign, "issions", "");
		  Res = replaceLast(sign, "iraIent", "");
		  Res = replaceLast(sign, "issons", "");
		  Res = replaceLast(sign, "issant", "");
		  Res = replaceLast(sign, "issiez", "");
		  Res = replaceLast(sign, "issait", "");
		  Res = replaceLast(sign, "issant", "");
		  Res = replaceLast(sign, "issent", "");
		  Res = replaceLast(sign, "irions", "");
		  Res = replaceLast(sign, "issais", "");
		  Res = replaceLast(sign, "issez", "");
		  Res = replaceLast(sign, "isses", "");
		  Res = replaceLast(sign, "iront", "");
		  Res = replaceLast(sign, "irons", "");
		  Res = replaceLast(sign, "iriez", "");
		  Res = replaceLast(sign, "irent", "");
		  Res = replaceLast(sign, "irait", "");
		  Res = replaceLast(sign, "iras", "");
		  Res = replaceLast(sign, "irez", "");
		  Res = replaceLast(sign, "isse", "");
		  Res = replaceLast(sign, "irai", "");
		  Res = replaceLast(sign, "îmes", "");
		  Res = replaceLast(sign, "îtes", "");
		  Res = replaceLast(sign, "ies", "");
		  Res = replaceLast(sign, "ira", "");
		  Res = replaceLast(sign, "ie", "");
		  Res = replaceLast(sign, "ir", "");
		  Res = replaceLast(sign, "is", "");
		  Res = replaceLast(sign, "it", "");
		  Res = replaceLast(sign, "ît", "");
		  Res = replaceLast(sign, "i", "");
	  }
	  return Res;
  }
  
  
  private String step2b(String sign){
	  String Res = sign;
	  Res = replaceLast(Res, "eraIent", "");
	  Res = replaceLast(Res, "erions", "");
	  Res = replaceLast(Res, "erons", "");
	  Res = replaceLast(Res, "èrent", "");
	  Res = replaceLast(Res, "eriez", "");
	  Res = replaceLast(Res, "eront", "");
	  Res = replaceLast(Res, "erait", "");
	  Res = replaceLast(Res, "erais", "");
	  Res = replaceLast(Res, "erai", "");
	  Res = replaceLast(Res, "erez", "");
	  Res = replaceLast(Res, "eras", "");
	  Res = replaceLast(Res, "ées", "");
	  Res = replaceLast(Res, "era", "");
	  Res = replaceLast(Res, "iez", "");
	  Res = replaceLast(Res, "er", "");
	  Res = replaceLast(Res, "ez", "");
	  Res = replaceLast(Res, "és", "");
	  Res = replaceLast(Res, "ée", "");
	  Res = replaceLast(Res, "é", "");
  

	  Res = replaceLast(Res, "assions", "");
	  Res = replaceLast(Res, "assiez", "");
	  Res = replaceLast(Res, "assent", "");
	  Res = replaceLast(Res, "aIent", "");
	  Res = replaceLast(Res, "antes", "");
	  Res = replaceLast(Res, "asses", "");
	  Res = replaceLast(Res, "âmes", "");
	  Res = replaceLast(Res, "âtes", "");
	  Res = replaceLast(Res, "ante", "");
	  Res = replaceLast(Res, "asse", "");
	  Res = replaceLast(Res, "ants", "");
	  Res = replaceLast(Res, "ait", "");
	  Res = replaceLast(Res, "ais", "");
	  Res = replaceLast(Res, "ant", "");
	  Res = replaceLast(Res, "ât", "");
	  Res = replaceLast(Res, "as", "");
	  Res = replaceLast(Res, "ai", "");
	  Res = replaceLast(Res, "a", "");
	  
      Res = replaceLast(Res, "ions", "");
	  return Res;	  
  }
  /***
   * Step 3
   * Replace final Y with i or final ç with c
   * Alternatively, if the last step to be obeyed did not alter the word, do step 4 
   * @param sign
   * @return
   */
  private String step3(String sign){
	  String Res = sign;
	  if(sign.length() >2) {
		  if(sign.endsWith("Y"))
			  return sign.substring(0, sign.length() - 2) + "i";
		  if(sign.endsWith("ç"))
			  return sign.substring(0, sign.length() - 2) + "c";
	  }
	  return Res;
  }
  /***
   * Step 4: Residual suffix
   * If the word ends s, not preceded by a, i, o, u, è or s, delete it. 
   * In the rest of step 4, all tests are confined to the RV region. 
   * If the word ends s, not preceded by a, i, o, u, è or s, delete it. 
   * @param sign
   * @return
   */
  private String step4(String sign){
	  String Res = sign;
	  if(sign.length() >2 && sign.endsWith("s") && sign.charAt(sign.length() - 2) != 'a' &&
			  sign.charAt(sign.length() - 2) != 'i' &&
			  sign.charAt(sign.length() - 2) != 'o' &&
			  sign.charAt(sign.length() - 2) != 'u' &&
			  sign.charAt(sign.length() - 2) != 'è' &&
			  sign.charAt(sign.length() - 2) != 's' ){
		  Res = sign.substring(0, sign.length() - 2);
	  }
	  String RV = getRV(Res);
	  /***
	   * ion
	   * delete if in R2 and preceded by s or t 
	   */
	  if(beforeBy(Res, "ion", "s") || beforeBy(Res, "ion", "s") ||
			  beforeBy(Res, "ion", "t") || beforeBy(Res, "ion", "t")){
		  if(RV.contains("ion")){
			  Res = replaceLast(sign, "ion", "");
			  return Res;
		  }
	  }
	  /***
	   * ier   ière   Ier   Ière
	   * replace with i
	   */
	  if(RV.endsWith("ier") || RV.endsWith("ière") || RV.endsWith("Ier") || 
			  RV.endsWith("Ière")){
		  Res = replaceLast(sign, "ion", "");
		  Res = replaceLast(sign, "ière", "");
		  Res = replaceLast(sign, "Ier", "");
		  Res = replaceLast(sign, "Ière", "");
		  return Res;
	  }
	  if(RV.endsWith("e")){
		  Res = replaceLast(sign, "e", "");
		  return Res;
	  }
	  if(RV.contains("ë")){
		  Res = replaceLast(sign, "ë", "");
		  return Res;
	  }
	  return Res;
  }
  
  /***
   * Step 5: Undouble
   * If the word ends enn, onn, ett, ell or eill, delete the last letter
   * @param sign
   * @return
   */
  private String step5(String sign){
	  if(sign.endsWith("enn")) return sign.substring(0, sign.length() - 1);
	  if(sign.endsWith("onn")) return sign.substring(0, sign.length() - 1);
	  if(sign.endsWith("ett")) return sign.substring(0, sign.length() - 1);
	  if(sign.endsWith("ell")) return sign.substring(0, sign.length() - 1);
	  if(sign.endsWith("eill")) return sign.substring(0, sign.length() - 1);
	  return sign;
  }
  
  /***
   * Step 6: Un-accent
   * If the words ends é or è followed by at least one non-vowel, remove the accent from the e.
   * @param sign
   * @return
   */
  private String step6(String sign){
	  String Res = sign;
	  boolean change = false;
	  int pos = sign.indexOf("é");
	  if(pos == -1)
		  pos = sign.indexOf("è");
	  if(pos != -1){
		  String tmp = sign.substring(pos, sign.length());
		  for(int i = 0; i < tmp.length(); i++){
			  if(isVowel(tmp.charAt(i)))
				  change = true;
		  }
	  }
	  if(change){
		  Res = replaceLast(sign, "é", "e");
		  Res = replaceLast(sign, "è", "e");
	  }
	  return Res;
  }
  
  /***
   * Finally, Turn any remaining I, U and Y letters in the word back into lower case.
   * @param sign
   * @return
   */
  private String step7(String sign){
	  String Res = sign.toLowerCase();
	  return Res;
  }
}
