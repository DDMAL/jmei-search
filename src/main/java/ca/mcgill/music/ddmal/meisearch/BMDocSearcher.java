package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;

/**
 * Boyer-Moore string search
 */
public class BMDocSearcher extends DocSearcher {

    public BMDocSearcher(MeiDocument meiDoc) {
        super(meiDoc);
    }

    @Override
    public List<Response> find(String query) {
        return null;
    }

}
