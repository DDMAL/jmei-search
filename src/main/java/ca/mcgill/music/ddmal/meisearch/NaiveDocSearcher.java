package ca.mcgill.music.ddmal.meisearch;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * A naive document searcher that searches for a string sequentially through a document.
 * This search intentionally has no optimisations as an indicator for upper runtime.
 */
public class NaiveDocSearcher implements DocSearcher {

    public List<Response> find(MeiDocument doc, String query) {
        String[] qparts = query.split(".");
        List<MeiElement> neumes = doc.getElementsByName("neume");
        List<Response> res = new ArrayList<Response>();
        for (int i = 0; i < neumes.size() - query.length(); i++) {
            boolean match = true;
            for (int j = 0; j < query.length(); j++) {
                if (qparts[j].equals(neumes.get(i + j).getAttribute("pname"))) {
                    match = true;
                } else {
                    match = false;
                    break;
                }
            }
            if (match) {
                Response r = Response.makeResponse(neumes.subList(i, i + query.length()));
                res.add(r);
            }
        }
        return res;
    }

}
