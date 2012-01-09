package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

public interface SearchStrategy {
    public List<Response> search(String query);
}
