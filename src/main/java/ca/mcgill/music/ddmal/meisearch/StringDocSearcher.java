package ca.mcgill.music.ddmal.meisearch;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * A naive document searcher that uses Java's string searching methods to attempt
 * to speed up the naive implementation.
 */
public class StringDocSearcher implements DocSearcher {

    public List<Response> find(MeiDocument doc, String query) {
        StringBuilder sb = new StringBuilder();
        List<MeiElement> neumes = doc.getElementsByName("neume");
        for (MeiElement e : neumes) {
            sb.append(e.getAttribute("pname"));
        }
        String haystack = sb.toString();
        int i = 0;
        List<Response> res = new ArrayList<Response>();
        while (i > 0) {
            i = haystack.indexOf(query, 0);
            if (i > 0) {
                Response r = Response.makeResponse(neumes.subList(i, i + query.length()));
                res.add(r);
            }
        }
        return res;
    }

}
