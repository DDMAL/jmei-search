package ca.mcgill.music.ddmal.meisearch;

import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;

public abstract class DocSearcher {

    protected final MeiDocument meiDoc;

    public DocSearcher(MeiDocument meiDoc) {
        this.meiDoc = meiDoc;

    }

    public abstract List<Response> find(String query);
}
