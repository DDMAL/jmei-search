package ca.mcgill.music.ddmal.hadoopsearch;

import static org.junit.Assert.*;

import org.junit.Test;

public class DemoDriverTest {

    @Test
    public void test() throws Exception {
      JobConf conf = new JobConf();
      conf.set("fs.default.name", "file:///");
      conf.set("mapred.job.tracker", "local");

      Path input = new Path("input/ncdc/micro");
      Path output = new Path("output");

      FileSystem fs = FileSystem.getLocal(conf);
      fs.delete(output, true); // delete old output

      MaxTemperatureDriver driver = new MaxTemperatureDriver();
      driver.setConf(conf);

      int exitCode = driver.run(new String[] {
          input.toString(), output.toString() });
      assertThat(exitCode, is(0));

      checkOutput(conf, output);
    }

}
