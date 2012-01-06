package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;

/**
 * Knuth-Morris-Pratt string search
 */
public class KMPDocSearcher extends DocSearcher {

    public KMPDocSearcher(MeiDocument meiDoc) {
        super(meiDoc);
    }

    @Override
    public List<Response> find(String query) {
        return null;
    }

}
