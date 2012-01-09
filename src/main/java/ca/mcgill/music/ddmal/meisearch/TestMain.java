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
    private DocSearcher searcher;
    private List<MeiDocument> docs;

    public TestMain(List<String> files, String searchClass, String query) throws Exception {
        this.query = query;
        docs = new ArrayList<MeiDocument>();
        for (String f : files) {
            docs.add(MeiXmlReader.loadFile(f));
        }

        Class klass = Class.forName(searchClass);
        Constructor declaredConstructor = klass.getDeclaredConstructor(null);
        searcher = ((DocSearcher)declaredConstructor.newInstance(null));
    }

    public void run(int iters) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iters; i++) {
            for (MeiDocument d : docs) {
                //System.out.println(d);
                List<Response> res = searcher.find(d, query);
                for (Response r : res) {
                    System.out.println(d.getFilename() + ": " + r);
                }
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
        String file = "";
        if (cmd.hasOption("f")) {
            file = cmd.getOptionValue("f");
        }
        int iters = 1;
        if (cmd.hasOption("n")) {
            iters = Integer.parseInt(cmd.getOptionValue("n"));
        }
        String fileList = "";
        if (cmd.hasOption("l")) {
            fileList = cmd.getOptionValue("l");
        }
        String searcherClass = "ca.mcgill.music.ddmal.meisearch.NaiveDocSearcher";
        if (cmd.hasOption("s")) {
            searcherClass = cmd.getOptionValue("s");
            // If not a FQ class name, add the package
            if (searcherClass.indexOf(".") == -1) {
                searcherClass = "ca.mcgill.music.ddmal.meisearch." + searcherClass;
            }
        }

        String[] qArgs = cmd.getArgs();
        if (qArgs.length != 1) {
            System.err.println(">> Missing required query parameter");
            System.exit(1);
        }
        if (file.equals("") && fileList.equals("")) {
            System.err.println(">> Missing either -f (file) or -l (fileList) option");
            System.exit(1);
        }
        // if there's a file list, load it. Otherwise the list just has the -l file in it.
        List<String> testFiles;
        if (!fileList.equals("")) {
            testFiles = FileUtils.readLines(new File(fileList));
        } else {
            testFiles = new ArrayList<String>();
            testFiles.add(file);
        }

        TestMain a = new TestMain(testFiles, searcherClass, qArgs[0]);
        a.run(iters);
    }
}
