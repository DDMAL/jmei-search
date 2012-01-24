package ca.mcgill.music.ddmal.meisearch;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

public class SingleThreadSearchStrategyTest {

    @Test
    public void testSplitCorpus() {
        SingleThreadSearchStrategy strat = new SingleThreadSearchStrategy(null, null);
        String docText = "<meiCorpus xmlns=\"http://www.music-encoding.org/ns/mei\" meiversion=\"2012\">" +
                "<mei xmlns=\"http://www.music-encoding.org/ns/mei\" meiversion=\"2012\"><note xml:id=\"anote\"/></mei>" +
                "<mei xmlns=\"http://www.music-encoding.org/ns/mei\" meiversion=\"2012\"><neume xml:id=\"aneume\"/></mei></meiCorpus>";
        
        strat.init(docText);
        while (strat.hasNext()) {
            String individualContents = strat.getNext();
            System.out.println(individualContents);
        }
    }

}
