package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * A naive document searcher that uses Java's string searching methods to attempt
 * to speed up the naive implementation.
 *
 *
 */
public class StringDocSearcher extends DocSearcher {

    private List<MeiElement> neumes;

    public StringDocSearcher(MeiDocument meiDoc) {
        super(meiDoc);

        neumes = this.meiDoc.getElementsByName("neume");
    }

    @Override
    public List<Response> find(String query) {
        // 1. turn list of neumes into string of note names
        // 2. use i=String.indexOf(x, k) until k = len(neumes)
        // 3. neumes.at(i) -> neumes.at(i + len(query)) is the bounding box
        return null;
    }

}
