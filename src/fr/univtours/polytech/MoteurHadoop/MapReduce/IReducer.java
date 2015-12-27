package fr.univtours.polytech.MoteurHadoop.MapReduce;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import fr.univtours.polytech.MoteurHadoop.writable.DocSumWritable;

/***
 * le reducer
 * @author hduser
 *
 */
public class IReducer extends Reducer<Text, Text, Text, DocSumWritable> {

	//****************************function***********************************
	
	/***
	 * le constructeur
	 */
	public IReducer() {
		System.out.println("indexe reducer called");
	}

	private HashMap<String, Integer> Res;

	private void add(String tag) {
		Integer val;
		if (Res.get(tag) != null) {
			val = Res.get(tag);
			Res.remove(tag);
		} else {
			val = 0;
		}
		Res.put(tag, val + 1);
	}

	/***
	 * reecrire le function reduce
	 */
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		Res = new HashMap<String, Integer>();
		for (Text val : values) {
			String sign = val.toString();
			add(sign);
		}

		context.write(key, new DocSumWritable(Res));

	}
}
