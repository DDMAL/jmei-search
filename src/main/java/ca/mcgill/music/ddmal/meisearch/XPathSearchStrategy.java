package ca.mcgill.music.ddmal.meisearch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 * Perform a search directly on the XML document using XPath.
 * //note[@pname="a"]/following::note[@pname="b"]
 */
public class XPathSearchStrategy implements SearchStrategy {

    List<InputStream> docs;
    private XPathDocSearcher searcher;

    public XPathSearchStrategy(List<String> fileList) {
        docs = new ArrayList<InputStream>();

        for (String f : fileList) {
            try {
                docs.add(IOUtils.toInputStream(FileUtils.readFileToString(new File(f))));
            } catch (IOException e) {
            }
        }
    }

    public List<Response> search(String query) {
        searcher = new XPathDocSearcher(query);

        List<Response> res = new ArrayList<Response>();
        for (InputStream stream : docs) {

            try {
                res.addAll(searcher.findXpath(stream));
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return res;
    }


}
