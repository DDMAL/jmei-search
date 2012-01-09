package ca.mcgill.music.ddmal.meisearch;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

public class StringDocSearcherTest {

    @Test
    public void test() {
        URL url = getClass().getResource("/400.mei");
        MeiDocument doc = MeiXmlReader.loadFile(url.getFile());
        StringDocSearcher sr = new StringDocSearcher();
        sr.find(doc, "abc");

        assertThat(doc.getRootElement().getName(), is("mei"));
    }

}
