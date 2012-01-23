//
// run with
//hadoop jar job.jar SmallFilesToSequenceFileConverter \
//-conf conf/hadoop-localhost.xml -D mapred.reduce.tasks=2 input/smallfiles output

// A main that takes a directory of small files and combines them together
// before passing on to a mapper.

package ca.mcgill.music.ddmal.hadoopsearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.google.common.io.Files;

public class SmallFileMain extends Configured
        implements Tool {

    public int run(String[] args) throws IOException {
        Configuration conf = getConf();
        conf.set(
                "io.serializations",
                "org.apache.hadoop.io.serializer.JavaSerialization,org.apache.hadoop.io.serializer.WritableSerialization");
        conf.set("query", args[2]);

        Job job = new Job(conf, "jobName");

        File sequenceFileTemp = Files.createTempDir();
        String tempPath = "/Users/alastairlocal/seqFile";
        try {
            meiToSequenceFile(args[0], tempPath, conf);
            FileInputFormat.setInputPaths(job, tempPath);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        job.setJarByClass(SmallFileMain.class);
        job.setMapperClass(DocumentSearchMapper.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);

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

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new SmallFileMain(), args);
        System.exit(exitCode);
    }

    /** Pack all files in a directory into a Hadoop SequenceFile
     * From http://passingtimeby.blogspot.com/2010/06/hadoop-sequencefile.html */
    public void meiToSequenceFile(String input, String output, Configuration conf) throws Exception {
        File inputDirectory = new File(input);

        Path outputPath = new Path(output);
        FileSystem fileSystem = FileSystem.get(conf);
        SequenceFile.Writer swriter = SequenceFile.createWriter(fileSystem, conf, outputPath,
                LongWritable.class, Text.class, SequenceFile.CompressionType.NONE);
        /*
Uncompressed key/value records.
Record compressed key/value records - only 'values' are compressed here.
Block compressed key/value records - both keys and values are collected in 'blocks' separately and compressed. The size of the 'block' is configurable.
         */
        try {
            File[] files = inputDirectory.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".mei")) {
                    //String filename = files[i].getName();
                    String content = IOUtils.toString(new FileInputStream(files[i]));
                    LongWritable l = new LongWritable(i);
                    //swriter.append(new Text(filename), new Text(content));
                    swriter.append(l, new Text(content));
                }
            }
        } finally {
            if (swriter != null) {
                swriter.close();
            }
        }
    }
}
