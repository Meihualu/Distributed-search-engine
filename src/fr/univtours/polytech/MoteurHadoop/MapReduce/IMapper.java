package fr.univtours.polytech.MoteurHadoop.MapReduce;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import fr.univtours.polytech.MoteurHadoop.filter.Filter;
import fr.univtours.polytech.MoteurHadoop.signextractors.SignExtractor;

/***
 * Le mapper de la phase d'indexation
 * 
 * @author hduser
 * 
 */
public class IMapper extends Mapper<LongWritable, Text, Text, Text> {

	// **************************function**************************************
	/***
	 * reecrire le function map
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		
		String parameter = context.getJobName();
		
		String[] lines = parameter.split(";");
		
		SignExtractor extractor = getSignExtractor(lines[0]);

		FileSplit split = (FileSplit) context.getInputSplit();
		
		String fileName = split.getPath().getName();

		String line = value.toString();
		extractor.setContent(line);
		String signs = extractor.nextToken();
		
		while (signs != null) {
			String signFiltered = filterSign(lines[1], signs);
			if (signFiltered.compareTo("") != 0) {
				Text word = new Text();
				word.set(signFiltered);
				context.write(new Text(word), new Text(fileName));
			}
			signs = extractor.nextToken();
		}

	}

	/***
	 * 
	 * @param extractorClassName
	 * @return
	 */
	private SignExtractor getSignExtractor(String extractorClassName) {
		String[] param = extractorClassName.split("-");
		SignExtractor extract = null;
		if (param.length == 2) {
			extractorClassName = param[0];
			Class<?> clazz;
			try {
				clazz = Class.forName(extractorClassName);
				extract = (SignExtractor) clazz.getConstructor(int.class)
						.newInstance(param[1]);
			} catch (Exception e) {
				System.out
						.println("Unable to load extractor, configuration error");
			}
		} else {
			try {
				extract = (SignExtractor) Class.forName(extractorClassName)
						.newInstance();
			} catch (Exception e) {
				System.out.println("Unable to load extractor \""
						+ extractorClassName + "\", configuration error");
			}
		}
		return extract;
	}
	
	/***
	 * la filtre
	 * @param classList
	 * @param sign
	 * @return le sign filtré
	 */
	private String filterSign(String classList, String sign) {
		String str = classList.substring(1, classList.length() - 1);
		String[] filter = str.split(",");

		ArrayList<Filter> lists = new ArrayList<Filter>();
		
		for (String tmp : filter) {
			try {
				lists.add((Filter) Class.forName(tmp).newInstance());
			} catch (Exception e) {
				System.out.println("cannot load filter \"" + tmp
						+ "\", configuration error ");
			}
		}

		String out = null;
		
		if (lists.size() == 0) {
			out = sign;
		} else {
			String res = sign;
			for (int i = 0; i < lists.size(); ++i) {
				res = lists.get(i).filter(res);
				if (res == null) {
					break;
				}
			}
			out = res;
		}
		return out;
	}
	
	/***
	 * le function run
	 */
	public void run(Context context) throws IOException, InterruptedException {
		setup(context);
		while (context.nextKeyValue()) {
			map(context.getCurrentKey(), context.getCurrentValue(), context);
		}
		cleanup(context);
	}
}
