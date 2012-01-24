package ca.mcgill.music.ddmal.meisearch;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class S3ToolTest {

    @Test
    public void testFileName() {
        assertThat(S3Tool.isS3File("/some/file"), is(false));
        assertThat(S3Tool.isS3File("s3://some/file"), is(true));
    }
    
    @Test
    public void testList() {
        List<String> list = S3Tool.listBucket("ddmal_liber", "");
        assertThat(list.size(), is(2327));
    }
    
    @Test
    public void testOpen() {
        String contents = S3Tool.readFile("s3://ddmal_liber/2331_corr.mei");
        assertThat(contents.startsWith("<?xml version"), is(true));
    }

}
