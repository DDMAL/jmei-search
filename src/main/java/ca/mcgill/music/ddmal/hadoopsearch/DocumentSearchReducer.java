package ca.mcgill.music.ddmal.hadoopsearch;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class DocumentSearchReducer extends
		Reducer<Text, Text, Text, Text> {

	/*
	 * No reduce step for MEI search. Can we get by with leaving this out?
	 */
	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		Iterator<Text> valuesIterator = values.iterator();

		while (valuesIterator.hasNext()) {
		    context.write(key,valuesIterator.next());
		}
	}
}
