package ca.mcgill.music.ddmal.meisearch;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;

/**
 * Hello world!
 *
 */
public class TestMain
{
    private final String query;
    private final SearchStrategy strategy;

    public TestMain(List<String> files, String searchClass, boolean multithread, String query) throws Exception {
        this.query = query;

        Class klass = Class.forName(searchClass);
        Constructor declaredConstructor = klass.getDeclaredConstructor(null);
        DocSearcher searcher = ((DocSearcher)declaredConstructor.newInstance(null));

        if (multithread) {
            // TODO: Configuration option for number threads
            int numThreads = 2; // 2 for ec2
            strategy = new MultiThreadSearchStrategy(files, searchClass, numThreads);
        } else {
            strategy = new SingleThreadSearchStrategy(files, searcher);
        }
    }

    public void run(int iters) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iters; i++) {
            List<Response> res = strategy.search(query);
            for (Response r : res) {
                    System.out.println(r.getDocument().getFilename() + ": " + r);
            }
        }

        long endTime = System.currentTimeMillis();

        float total = (endTime - startTime) / 1000.0f;
        System.out.println("Total time: " + total);
    }

    @SuppressWarnings("static-access")
    public static void main( String[] args ) throws Exception
    {
        Options options = new Options();
        options.addOption(OptionBuilder.withArgName("file").hasArg().withDescription("MEI file to search; or").create("f"));
        options.addOption(OptionBuilder.withArgName("filelist").hasArg().withDescription("File containing files to search").create("l"));
        options.addOption(OptionBuilder.withArgName("bucketname").hasArg().withDescription("S3 Bucket to read files from").create("b"));
        options.addOption(OptionBuilder.withArgName("n").hasArg().withDescription("Number of times to search").create("n"));
        options.addOption(OptionBuilder.withArgName("DocSearcher").hasArg().withDescription("FQ class of a DocSearcher implementation").create("s"));
        options.addOption("m", false, "Run multithreaded");

        if (args.length < 2) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("TestMain", options);
            System.exit(1);
        }

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse( options, args);
        String file = null;
        if (cmd.hasOption("f")) {
            file = cmd.getOptionValue("f");
        }
        int iters = 1;
        if (cmd.hasOption("n")) {
            iters = Integer.parseInt(cmd.getOptionValue("n"));
        }
        String fileList = null;
        if (cmd.hasOption("l")) {
            fileList = cmd.getOptionValue("l");
        }
        String bucketName = null;
        if (cmd.hasOption("b")) {
            bucketName = cmd.getOptionValue("b");
        }
        String searcherClass = "ca.mcgill.music.ddmal.meisearch.StringDocSearcher";
        if (cmd.hasOption("s")) {
            searcherClass = cmd.getOptionValue("s");
            // If not a FQ class name, add the package
            if (searcherClass.indexOf(".") == -1) {
                searcherClass = "ca.mcgill.music.ddmal.meisearch." + searcherClass;
            }
        }
        boolean multithread = cmd.hasOption("m");

        String[] qArgs = cmd.getArgs();
        if (qArgs.length != 1) {
            System.err.println(">> Missing required query parameter");
            System.exit(1);
        }
        if (file == null && fileList == null && bucketName == null) {
            System.err.println(">> Missing either -f (file) or -l (fileList) or -b (bucketName) option");
            System.exit(1);
        }
        List<String> testFiles;
        if (bucketName != null) {
            // If there's an s3 bucket name, load the contents of the bucket
            testFiles = S3Tool.listBucket(bucketName);
        } else if (fileList != null) {
            // else if there's a file that contains all the files to load
            testFiles = FileUtils.readLines(new File(fileList));
        } else {
            // else it's just a single file (with -l)
            testFiles = new ArrayList<String>();
            testFiles.add(file);
        }
        System.out.println("testing with " + testFiles.size() + " files");

        TestMain a = new TestMain(testFiles, searcherClass, multithread, qArgs[0]);
        a.run(iters);
    }
}
