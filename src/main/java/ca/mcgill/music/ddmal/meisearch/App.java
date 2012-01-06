package ca.mcgill.music.ddmal.meisearch;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

/**
 * Hello world!
 *
 */
public class App
{
    private final String filename;
    private final String query;
    private DocSearcher searcher;

    public App(String filename, String query) {
        this.filename = filename;
        this.query = query;
        MeiDocument doc = MeiXmlReader.loadFile(filename);
        searcher = new NaiveDocSearcher(doc);
    }

    public void run() {
        searcher.find(query);
    }

    public static void main( String[] args )
    {
        if (args.length < 2) {
            System.err.println("Usage: App <mei file> <search query>");
            System.exit(1);
        }
        App a = new App(args[0], args[1]);
        a.run();
        System.out.println( "Hello World!" );
    }
}
