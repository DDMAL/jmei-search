/* Author: Anton Khelou 2011
   MUMT 301
*/


package ca.mcgill.music.ddmal.hadoopsearch;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import ca.mcgill.music.ddmal.meisearch.NaiveDocSearcher;
import ca.mcgill.music.ddmal.meisearch.Response;

/*
 * DocumentSearchMapper is the class used for the map function.
 * This class uses the XOM library to do its .xml processing.
 *
 */
public class DocumentSearchMapper extends
		Mapper<Text, Text, Text, Text> {
	//the first two generic types indicate what are the types of the <key,value> pairs coming into this class
	//and the last two generic represent what the type of the <key,value> pairs this class will output to the reducer
	//(or combiner if you have one)

	@Override
	/*
	 * The map() method is called once for each input taken from the input path. This
	 * method will be receiving a string representation of the entire .xml file which
	 * is then converted into a tree using the XOM library. The purpose of this method
	 * is to get all the "note" elements in the .mei file and retrieve their note "dur"
	 * duration attributes. The output of this method will be of the form key:noteduration
	 * value:1, which is then sent for local aggregation by the combiner, which will local
	 * aggregation of results for each map() call.
	 *
	 * note that the two first arguments are of the same type as the first two generics of the class def
	 * -> they need to match
	 *
	 */
	public void map(Text key, Text value1, Context context)
			throws IOException, InterruptedException {

	    String query = context.getConfiguration().get("query");

		String xmlString = value1.toString();

		NaiveDocSearcher ds = new NaiveDocSearcher();
		MeiDocument doc = MeiXmlReader.loadDocument(xmlString);

		List<Response> find = ds.find(doc, query);
		StringBuilder sb = new StringBuilder("[");
		String sep = "";
		for (Response r : find) {
		    sb.append(sep).append(r.toString());
		    sep = ",";
		}

		context.write(key, new Text(sb.toString()));//input for the combiner/reducer

	}

}