/* Author: Anton Khelou 2011
   MUMT 301
*/


package ca.mcgill.music.ddmal.hadoopsearch;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import ca.mcgill.music.ddmal.mei.MeiDocument;
import ca.mcgill.music.ddmal.mei.MeiXmlReader;
import ca.mcgill.music.ddmal.meisearch.NaiveDocSearcher;
import ca.mcgill.music.ddmal.meisearch.Response;

/*
 * Perform a search on a document.
 */
// Generic types are key/val input, key/val output
public class DocumentSearchMapper extends
		Mapper<LongWritable, Text, Text, Text> {

	@Override
	public void map(LongWritable key, Text value1, Context context)
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
		    sep = "/";
		}
		sb.append("]");
		String pageNo = doc.getElementsByName("page").get(0).getAttribute("n");

		context.write(new Text(pageNo), new Text(sb.toString()));//input for the combiner/reducer

	}

}