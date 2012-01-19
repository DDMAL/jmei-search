package ca.mcgill.music.ddmal.hadoopsearch;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

// A main that takes a single <meiCorpus> and splits the mei documents
// out of it.

/*
 *
 */
public class LargeFileMain extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new LargeFileMain(), args);
        System.exit(exitCode);
    }

    public int run(String[] args) throws Exception {
        Configuration conf = getConf();

        // This parameter is used by XmlInputFormat to indicate where to start
        // taking in xml content
        conf.set("xmlinput.start", "<mei ");
        conf.set("xmlinput.end", "</mei>");
        conf.set(
                "io.serializations",
                "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
        conf.set("query", args[2]);

        Job job = new Job(conf, "jobName");

        FileInputFormat.setInputPaths(job, args[0]);

        job.setJarByClass(LargeFileMain.class);
        job.setMapperClass(DocumentSearchMapper.class);
        job.setCombinerClass(DocumentSearchReducer.class);
        job.setReducerClass(DocumentSearchReducer.class);

        job.setInputFormatClass(XmlInputFormat.class);

        job.setOutputKeyClass(Text.class);// Keys will be Text
        job.setOutputValueClass(Text.class);// Values will be Integers

        Path outPath = new Path(args[1]);
        FileOutputFormat.setOutputPath(job, outPath);
        FileSystem dfs = FileSystem.get(outPath.toUri(), conf);

        // the following checks if the output folder already exists, if so, it
        // will delete it
        if (dfs.exists(outPath)) {
            dfs.delete(outPath, true);
        }

        try {
            job.waitForCompletion(true);

        } catch (InterruptedException ex) {
            Logger.getLogger(LargeFileMain.class.getName()).log(Level.SEVERE,
                    null, ex);
            return -1;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LargeFileMain.class.getName()).log(Level.SEVERE,
                    null, ex);
            return -1;
        }
        return 0;
    }
}