package fr.univtours.polytech.MoteurHadoop.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Text.Comparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

/***
 * le travail dans la phase de recherche
 * 
 * @author hduser
 * 
 */
public class SecondJob extends Configured implements Tool {

	// *******************variable*****************************************

	private String question;

	// *******************function******************************************

	/***
	 * le constructeur
	 * 
	 * @param str
	 */
	public SecondJob(String str) {
		this.question = str;
	}

	/***
	 * la function run
	 */
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();

		Job runningJob = Job.getInstance(conf, "making question");

		runningJob.setOutputKeyClass(Text.class);
		runningJob.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(runningJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(runningJob, new Path(args[1]));

		Path inputPath = new Path("input/invertedindex/");
		FileSystem fs = inputPath.getFileSystem(conf);

		FileStatus[] stat = fs.listStatus(inputPath);

		runningJob.setInputFormatClass(TextInputFormat.class);
		runningJob.setOutputFormatClass(TextOutputFormat.class);

		runningJob.setMapperClass(RMapper.class);
		runningJob.setReducerClass(RReducer.class);

		runningJob.setMapOutputKeyClass(Text.class);
		runningJob.setMapOutputValueClass(Text.class);

		runningJob.setOutputKeyClass(Comparator.class);

		runningJob.setJobName(question + ";" + stat.length);

		runningJob.submit();

		return runningJob.waitForCompletion(true) ? 0 : 1;
	}

}
