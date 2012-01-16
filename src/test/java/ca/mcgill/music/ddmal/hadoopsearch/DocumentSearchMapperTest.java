package ca.mcgill.music.ddmal.hadoopsearch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.junit.Test;

public class DocumentSearchMapperTest {

    @Test
    public void test() throws IOException, InterruptedException, URISyntaxException {
        DocumentSearchMapper mapper = new DocumentSearchMapper();

        Mapper<LongWritable, Text, Text, Text>.Context mapCtx = mock(Context.class);
        Configuration conf = new Configuration();
        conf.set("query", "abc");
        when(mapCtx.getConfiguration()).thenReturn(conf);

        URL url = getClass().getResource("/400.mei");
        String meiContents = FileUtils.readFileToString(new File(url.toURI()));
        Text v = new Text(meiContents);
        mapper.map(new LongWritable(1), v, mapCtx);

        verify(mapCtx).write(new Text("400"), new Text("[(426,1941), (460,1998)/(728,1941), (762,2015)]"));
    }

}
