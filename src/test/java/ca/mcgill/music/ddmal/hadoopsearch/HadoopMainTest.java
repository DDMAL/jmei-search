package ca.mcgill.music.ddmal.hadoopsearch;

import static org.junit.Assert.assertThat;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import static org.hamcrest.core.Is.is;
import org.junit.Test;

public class HadoopMainTest {

    @Test
    public void test() throws Exception {
      JobConf conf = new JobConf();
      conf.set("fs.default.name", "file:///");
      conf.set("mapred.job.tracker", "local");

      Path input = new Path("input/ncdc/micro");
      Path output = new Path("output");

      FileSystem fs = FileSystem.getLocal(conf);
      fs.delete(output, true); // delete old output

      LargeFileMain driver = new LargeFileMain();
      driver.setConf(conf);

      int exitCode = driver.run(new String[] {
          input.toString(), output.toString() });
      assertThat(exitCode, is(0));

      checkOutput(conf, output);
    }

    private void checkOutput(Configuration conf, Path output) {

    }

}
