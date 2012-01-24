package ca.mcgill.music.ddmal.meisearch;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

public class SingleThreadSearchStrategy implements SearchStrategy {

    private final DocSearcher searcher;
    private final List<String> fileList;

    public SingleThreadSearchStrategy(List<String> fileList, DocSearcher searcher) {
        this.fileList = fileList;
        this.searcher = searcher;
    }

    public List<Response> search(String query) {
        List<Response> res = new ArrayList<Response>();
        for (String fn : fileList) {
            String fileContents = S3Tool.readFile(fn);
            MeiDocument d = MeiXmlReader.loadDocument(fileContents);
            res.addAll(searcher.find(d, query));
        }
        return res;
    }
}
