package ca.mcgill.music.ddmal.meisearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiElement;

/**
 * A naive document searcher that searches for a string sequentially through a document.
 * This search intentionally has no optimisations as an indicator for upper runtime.
 */
public class NaiveDocSearcher implements DocSearcher {

    public List<Response> find(MeiDocument doc, String query) {
        String[] parts = query.split("");
        String[] qparts = Arrays.copyOfRange(parts, 1, parts.length);
        List<MeiElement> notes = doc.getElementsByName("note");
        List<Response> res = new ArrayList<Response>();
        for (int i = 0; i < notes.size() - query.length(); i++) {
            boolean match = true;
            for (int j = 0; j < query.length(); j++) {
                if (qparts[j].equals(notes.get(i + j).getAttribute("pname"))) {
                    match = true;
                } else {
                    match = false;
                    break;
                }
            }
            if (match) {
                Response r = Response.makeResponse(doc, notes.subList(i, i + query.length()));
                res.add(r);
            }
        }
        return res;
    }

}
