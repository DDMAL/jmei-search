/* Author: Anton Khelou 2011
   MUMT 301
*/

package ca.mcgill.music.ddmal.hadoopsearch;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
 *
 */
public class HadoopMain {

	public static void main(String[] args) {
		try {
			runJob(args[0], args[1], args[2]);

		} catch (IOException ex) {
			Logger.getLogger(HadoopMain.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/*
	 * This method is responsible for starting up the Hadoop application.
	 * It sets all the necessary configuration and job information required by the
	 * Hadoop framework for execution.
	 *
	 */
	public static void runJob(String input, String output, String query) throws IOException {

		Configuration conf = new Configuration();

		//This parameter is used by XmlInputFormat to indicate where to start taking in xml content
		conf.set("xmlinput.start", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		conf.set("xmlinput.end", "</mei>"); //Same as before but for where it ends.
		conf.set(
				"io.serializations",
				"org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
		conf.set("query", query);

		Job job = new Job(conf, "jobName");

		FileInputFormat.setInputPaths(job, input);

		job.setJarByClass(HadoopMain.class); //determines which jar to run by finding the location of the class specified
		job.setMapperClass(DocumentSearchMapper.class);

		//the combiner and the reducer are the same class for this particular problem:
		//the combiner will essentially be doing local aggregation of the output of the input files
		//and the reducer will be aggregated the results of all outputs of combiners (not local - mixed from different combiners)
		job.setCombinerClass(DocumentSearchReducer.class);

		// Since our keys are files, and each file is done by a single thing, we don't need to reduce?
		//job.setReducerClass(DocumentSearchReducer.class);

		job.setInputFormatClass(XmlInputFormat.class); //InputFormat class taken from Mahout for xml processing

		job.setOutputKeyClass(Text.class);//Keys will be Text
		job.setOutputValueClass(Text.class);//Values will be Integers

		Path outPath = new Path(output);
		FileOutputFormat.setOutputPath(job, outPath);
		FileSystem dfs = FileSystem.get(outPath.toUri(), conf);

		//the following checks if the output folder already exists, if so, it will delete it
		if (dfs.exists(outPath)) {
			dfs.delete(outPath, true);
		}

		try {
			job.waitForCompletion(true);

		} catch (InterruptedException ex) {
			Logger.getLogger(HadoopMain.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(HadoopMain.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
}