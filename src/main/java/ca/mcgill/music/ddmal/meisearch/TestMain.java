package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

/**
 * Hello world!
 *
 */
public class TestMain
{
    private final String query;
    private DocSearcher searcher;
    private MeiDocument doc;

    public TestMain(String filename, String query) {
        this.query = query;
        doc = MeiXmlReader.loadFile(filename);
        //searcher = new NaiveDocSearcher();
        searcher = new StringDocSearcher();
    }

    public void run() {
        List<Response> res = searcher.find(doc, query);
        for (Response r : res) {
            System.out.println(r);
        }
    }

    public static void main( String[] args )
    {
        if (args.length < 2) {
            System.err.println("Usage: App <mei file> <search query>");
            System.exit(1);
        }
        TestMain a = new TestMain(args[0], args[1]);
        a.run();
    }
}
