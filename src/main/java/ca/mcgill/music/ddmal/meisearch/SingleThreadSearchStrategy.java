package ca.mcgill.music.ddmal.meisearch;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

public class SingleThreadSearchStrategy implements SearchStrategy {

    private final DocSearcher searcher;
    private final List<MeiDocument> docs;

    public SingleThreadSearchStrategy(List<String> fileList, DocSearcher searcher) {
        this.searcher = searcher;
        docs = new ArrayList<MeiDocument>();
        for (String f : fileList) {
            docs.add(MeiXmlReader.loadFile(f));
        }
    }

    public List<Response> search(String query) {
        List<Response> res = new ArrayList<Response>();
        for (MeiDocument d : docs) {
            res.addAll(searcher.find(d, query));
        }
        return res;
    }
}
