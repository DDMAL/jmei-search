//
// run with
//hadoop jar job.jar SmallFilesToSequenceFileConverter \
//-conf conf/hadoop-localhost.xml -D mapred.reduce.tasks=2 input/smallfiles output

// A main that takes a directory of small files and combines them together
// before passing on to a mapper.

package ca.mcgill.music.ddmal.hadoopsearch;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SmallFileMain extends Configured
        implements Tool {

    public int run(String[] args) throws IOException {
        Configuration conf = getConf();
        conf.set(
                "io.serializations",
                "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
        conf.set("query", args[2]);

        Job job = new Job(conf, "jobName");

        FileInputFormat.setInputPaths(job, args[0]);

        job.setJarByClass(SmallFileMain.class);
        job.setMapperClass(DocumentSearchMapper.class);
        //job.setCombinerClass(DocumentSearchReducer.class);
        //job.setReducerClass(DocumentSearchReducer.class);

        // XXX This one is important
        //job.setOutputFormat(SequenceFileOutputFormat.class);
        
        job.setInputFormatClass(WholeFileInputFormat.class);

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

        /*
        conf.setOutputFormat(SequenceFileOutputFormat.class);
        */
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new SmallFileMain(), args);
        System.exit(exitCode);
    }
}
