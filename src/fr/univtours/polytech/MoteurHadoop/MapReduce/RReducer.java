package fr.univtours.polytech.MoteurHadoop.MapReduce;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/***
 * le reducer de la phase de recherche
 * 
 * @author hduser
 * 
 */
public class RReducer extends Reducer<Text, Text, Text, Text> {

	// *****************************function*********************************

	/***
	 * le constructeur
	 */
	RReducer() {
		System.out.println("Query reduce called");
	}

	/***
	 * reecrire le function reduce
	 */
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		
		double document = 0, number = 0.0, idf = 0.0, tfidf = 0.0,
				scalaire = 0.0, questionInput = 0.0;
		
		int df = 0, tf = 0, total = 0;

		String parameter = context.getJobName();
		String[] subParameter = parameter.split(";");
		total = Integer.parseInt(subParameter[1]);

		HashMap<String, Double> question = null;

		for (Text text : values) {
			String val = text.toString();

			if (question == null) {
				question = new HashMap<String, Double>();
				String[] questionValues = subParameter[0].split(",");
				for (String item : questionValues) {
					String[] keyVal = item.split("->");
					question.put(keyVal[0], Double.parseDouble(keyVal[1]));
				}
			}

			String[] WordAnddfAndtf = val.split("-");
			df = Integer.parseInt(WordAnddfAndtf[1]);
			tf = Integer.parseInt(WordAnddfAndtf[2]);
			idf = Math.log10(total / df);
			tfidf = tf * idf;

			if (question.containsKey(WordAnddfAndtf[0])) {
				double tfQuestion = question.get(WordAnddfAndtf[0])
						/ question.size();
				double tfidfQuestion = tfQuestion * idf;

				questionInput = questionInput + Math.pow(tfidfQuestion, 2);

				scalaire = scalaire + (tfidf * tfidfQuestion);
			}

			document = document + Math.pow(tfidf, 2);
		}
		if (document != 0 && questionInput != 0) {
			questionInput = Math.sqrt(questionInput);
			document = Math.sqrt(document);
			number = scalaire / (questionInput * document);
		}
		context.write(key, new Text(number + ""));

	}
}