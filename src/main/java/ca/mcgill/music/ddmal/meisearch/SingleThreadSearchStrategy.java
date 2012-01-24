package ca.mcgill.music.ddmal.meisearch;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

public class SingleThreadSearchStrategy implements SearchStrategy {

    private final DocSearcher searcher;
    private final List<String> fileList;
    
    // for split files
    private int startPos = 0;
    private int endPos = 0;
    private String allContents;

    public SingleThreadSearchStrategy(List<String> fileList, DocSearcher searcher) {
        this.fileList = fileList;
        this.searcher = searcher;
    }

    public List<Response> search(String query) {
        List<Response> res = new ArrayList<Response>();
        for (String fn : fileList) {
            String fileContents = S3Tool.readFile(fn);
            if (fileContents.indexOf("meiCorpus") >= 0) {
                System.out.println("corpus!");
                init(fileContents);
                while (hasNext()) {
                    String individualContents = getNext();
                    MeiDocument d = MeiXmlReader.loadDocument(individualContents);
                    res.addAll(searcher.find(d, query));
                }
            } else {
                MeiDocument d = MeiXmlReader.loadDocument(fileContents);
                res.addAll(searcher.find(d, query));
            }
        }
        return res;
    }
    
    /*package*/ void init(String contents) {
        allContents = contents;
    }
    
    /*package*/ boolean hasNext() {
        String TAG_BEGIN = "<mei ";
        String TAG_END = "</mei>";
        startPos = allContents.indexOf(TAG_BEGIN, startPos);
        endPos = allContents.indexOf(TAG_END, startPos) + TAG_END.length();
        if (startPos >= 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /*package*/ String getNext() {
        String ret = allContents.substring(startPos, endPos);
        startPos = endPos;
        return ret;
    }
}
