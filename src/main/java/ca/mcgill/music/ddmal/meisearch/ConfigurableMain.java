package ca.mcgill.music.ddmal.meisearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class ConfigurableMain {

    public ConfigurableMain(String propertiesFile) throws FileNotFoundException, IOException {
        Properties p = new Properties();
        p.load(new FileInputStream(propertiesFile));
        
        String stratStr = p.getProperty("searchStrategy");
        p.getProperty("documentSearcher");
        
        p.getProperty("fileLocation");
        p.getProperty("iterations");
        
        try {
            Class klass = Class.forName(stratStr);
            Constructor declaredConstructor = klass.getDeclaredConstructor(null);
            SearchStrategy strategy = ((SearchStrategy)declaredConstructor.newInstance(null));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public void run() {
        // From config, read searcher, search strategy, directory
        // read number of times to iterate?
        // Read all files from directory
        // Make search strategy (tell it the searcher class)
        // Pass MeiDocumentList to strategy
        // loop n times
        // Print Time, and memory stats
    }
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: Main <conf file>");
            System.exit(1);
        }
        try {
            ConfigurableMain m;
            m = new ConfigurableMain(args[0]);
            m.run();
        } catch (FileNotFoundException e) {
            System.out.println("Error finding file " + args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
