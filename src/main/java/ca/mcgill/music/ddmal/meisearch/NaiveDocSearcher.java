package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;

/**
 * A naive document searcher that searches for a string sequentially through a document.
 * This search intentionally has no optimisations as an indicator for upper runtime.
 */
public class NaiveDocSearcher extends DocSearcher {

    public NaiveDocSearcher(MeiDocument meiDoc) {
        super(meiDoc);
    }

    @Override
    public List<Response> find(String query) {
        // TODO Auto-generated method stub
        return null;
    }

}
