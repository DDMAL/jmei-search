package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;

public interface DocSearcher {
    public List<Response> find(MeiDocument doc, String query);
}
