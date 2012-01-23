package ca.mcgill.music.ddmal.meisearch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

public class XPathDocSearcherTest {

    @Test
    public void test() throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        URL url = getClass().getResource("/400.mei");
        InputStream stream = IOUtils.toInputStream(FileUtils.readFileToString(new File(url.toURI())));

        XPathDocSearcher searcher = new XPathDocSearcher("abc");
        searcher.findXpath(stream);
    }

}
