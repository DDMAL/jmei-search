package ca.mcgill.music.ddmal.meisearch;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

public class MultiThreadSearchStrategy implements SearchStrategy {

    private final int numThreads;
    private final String searcherClass;
    private List<List<MeiDocument> > docs;

    /**
     *
     * @param searcherClass
     *          A string of the FQ class name of the searcher to use. This is a string
     *          since we need n instances of it.
     * @param numThreads
     *          The number of threads to spawn
     */
    public MultiThreadSearchStrategy(List<String> fileList, String searcherClass, int numThreads) {
        this.searcherClass = searcherClass;
        this.numThreads = numThreads;

        docs = new ArrayList<List<MeiDocument>>();
        for (int i = 0; i < numThreads; i++) {
            List<MeiDocument> d = new ArrayList<MeiDocument>();
            docs.add(d);
        }
        int counter = 0;
        int index = 0;
        int numPerThread = fileList.size() / numThreads + 1;
        for (String f : fileList) {
            if (counter > numPerThread) {
                counter = 0;
                index++;
            }
            docs.get(index).add(MeiXmlReader.loadFile(f));
            counter++;
        }
    }

    public DocSearcher makeSearcher(String searcherClass) {
        try {
            Class klass = Class.forName(searcherClass);
            Constructor declaredConstructor = klass.getDeclaredConstructor(null);
            DocSearcher searcher = ((DocSearcher)declaredConstructor.newInstance(null));
            return searcher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Response> search(String query) {
        List<MultiThreadSearchWorker> workers = new ArrayList<MultiThreadSearchWorker>();
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < numThreads; i++) {
            workers.add(new MultiThreadSearchWorker(docs.get(i), makeSearcher(searcherClass), query));
            threads.add(new Thread(workers.get(i)));
            threads.get(i).start();
        }

        List<Response> res = new ArrayList<Response>();
        boolean finished = false;
        while (!finished) {
            boolean stillAlive = false;
            for (int i = 0; i < numThreads; i++) {
                if (threads.get(i).isAlive()) {
                    stillAlive = true;
                } else {
                    res.addAll(workers.get(i).getResults());
                }
            }
            finished = stillAlive;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        return res;
    }


    private class MultiThreadSearchWorker implements Runnable {

        private final List<MeiDocument> files;
        private final DocSearcher searcher;
        private List<Response> results;
        private final String query;

        public MultiThreadSearchWorker(List<MeiDocument> files, DocSearcher searcher, String query) {
            this.files = files;
            this.searcher = searcher;
            this.query = query;
            results = new ArrayList<Response>();
        }

        public void run() {
            for (MeiDocument d : files) {
                results.addAll(searcher.find(d, query));
            }
        }

        public List<Response> getResults() {
            return results;
        }

    }
}
