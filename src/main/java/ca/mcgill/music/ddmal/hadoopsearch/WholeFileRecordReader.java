package ca.mcgill.music.ddmal.hadoopsearch;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.RecordReader;


class WholeFileRecordReader implements RecordReader<NullWritable, BytesWritable> {

  private FileSplit fileSplit;
  private Configuration conf;
  private boolean processed = false;

  public WholeFileRecordReader(FileSplit fileSplit, Configuration conf)
      throws IOException {
    this.fileSplit = fileSplit;
    this.conf = conf;
  }

  public NullWritable createKey() {
    return NullWritable.get();
  }

  public BytesWritable createValue() {
    return new BytesWritable();
  }

  public long getPos() throws IOException {
    return processed ? fileSplit.getLength() : 0;
  }

  public float getProgress() throws IOException {
    return processed ? 1.0f : 0.0f;
  }

  public boolean next(NullWritable key, BytesWritable value) throws IOException {
    if (!processed) {
      byte[] contents = new byte[(int) fileSplit.getLength()];
      Path file = fileSplit.getPath();
      FileSystem fs = file.getFileSystem(conf);
      FSDataInputStream in = null;
      try {
        in = fs.open(file);
        IOUtils.readFully(in, contents, 0, contents.length);
        value.set(contents, 0, contents.length);
      } finally {
        IOUtils.closeStream(in);
      }
      processed = true;
      return true;
    }
    return false;
  }

  public void close() throws IOException {
    // do nothing
  }
}