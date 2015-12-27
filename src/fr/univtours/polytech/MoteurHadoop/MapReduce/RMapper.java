package fr.univtours.polytech.MoteurHadoop.MapReduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/***
 * le mapper de la phase de recherche
 * 
 * @author hduser
 * 
 */
public class RMapper extends Mapper<LongWritable, Text, Text, Text> {

	// *****************************function*********************************

	/***
	 * reecrire le function de map
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String[] list;
		try {
			String line = value.toString();
			String[] content = line.split("\t");

			list = content[2].split("\\$");
			for (String s : list) {
				String[] str = s.split("=>");

				context.write(new Text(str[0]), new Text(content[0] + "-"
						+ content[1] + "-" + str[1]));
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
}