package fr.univtours.polytech.MoteurHadoop.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Text.Comparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

import fr.univtours.polytech.MoteurHadoop.writable.DocSumWritable;

/***
 * Le travail d'index
 * @author hduser
 *
 */
public class FirstJob extends Configured implements Tool {
	
	//***************************variable***************************************

	//l'extractor
	private String extractor;
	//les filtres
	private String listFilter;
	
	//*******************************function************************************
	
	//le constructeur
	public FirstJob(String extractorClassName, String listFilter)	{
		this.extractor = extractorClassName;
		this.listFilter = listFilter;
	}
	
	//la funtion run.
	public int run(String[] args) throws Exception {
		
		//*****************configuration de classe de mapreduce******************

		Configuration conf = getConf();

		Job runningJob = Job.getInstance(conf, "making index");

		runningJob.setOutputKeyClass(Text.class);
		runningJob.setOutputValueClass(DocSumWritable.class);
		
		runningJob.setMapperClass(IMapper.class);
		runningJob.setReducerClass(IReducer.class);
		
		runningJob.setInputFormatClass(TextInputFormat.class);
		runningJob.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(runningJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(runningJob, new Path(args[1]));
		
	    
		runningJob.setMapOutputKeyClass(Text.class);
		runningJob.setMapOutputValueClass(Text.class);		

		runningJob.setOutputKeyClass(Comparator.class);
			
		runningJob.setJobName(extractor+";"+listFilter);
		
		runningJob.submit();

		return runningJob.waitForCompletion(true) ? 0 : 1;
	}

}
