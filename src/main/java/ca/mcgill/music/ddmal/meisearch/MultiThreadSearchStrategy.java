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
     *          since we need to make n instances of it.
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

        double numPerThread = Math.ceil(fileList.size() / (numThreads * 1.0));
        for (int i = 0; i < numThreads; i++) {
            int start = i * (int)numPerThread;
            int end = Math.min(fileList.size(), start+(int)numPerThread);
            for (String f : fileList.subList(start, end)) {
                docs.get(i).add(MeiXmlReader.loadFile(f));
            }
        }
    }

    private DocSearcher makeSearcher(String searcherClass) {
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        List<Response> res = new ArrayList<Response>();
        // Could also use CyclicBarrier, or have a synchronised list that
        // gets passed into each thread and does wait/notify on it.
        for (int i = 0; i < numThreads; i++) {
            try {
                threads.get(i).join();
                res.addAll(workers.get(i).getResults());
            } catch (InterruptedException e) {
                e.printStackTrace();
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
