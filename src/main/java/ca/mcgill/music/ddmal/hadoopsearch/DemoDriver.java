package ca.mcgill.music.ddmal.hadoopsearch;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DemoDriver extends Configured implements Tool {

    public int run(String[] args) throws Exception {
      if (args.length != 2) {
        System.err.printf("Usage: %s [generic options] <input> <output>\n",
            getClass().getSimpleName());
        ToolRunner.printGenericCommandUsage(System.err);
        return -1;
      }

      JobConf conf = new JobConf(getConf(), getClass());
      conf.setJobName("Max temperature");

      FileInputFormat.addInputPath(conf, new Path(args[0]));
      FileOutputFormat.setOutputPath(conf, new Path(args[1]));

      conf.setOutputKeyClass(Text.class);
      conf.setOutputValueClass(IntWritable.class);

      conf.setMapperClass(MaxTemperatureMapper.class);
      conf.setCombinerClass(MaxTemperatureReducer.class);
      conf.setReducerClass(MaxTemperatureReducer.class);

      JobClient.runJob(conf);
      return 0;
    }

    public static void main(String[] args) throws Exception {
      int exitCode = ToolRunner.run(new MaxTemperatureDriver(), args);
      System.exit(exitCode);
    }
  }
