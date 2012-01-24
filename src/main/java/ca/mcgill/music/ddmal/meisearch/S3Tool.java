package ca.mcgill.music.ddmal.meisearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Tool {

    private static S3Tool instance = null;
    private AmazonS3 s3;
    
    private S3Tool() {
        try {
            s3 = new AmazonS3Client(new PropertiesCredentials(
                    getClass().getResourceAsStream("/AwsCredentials.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static AmazonS3 getInstance() {
        if (instance == null) {
            instance = new S3Tool();
        }
        return instance.s3;
    }
    
    public static boolean isS3File(String filename) {
        return filename.startsWith("s3://");
    }
    
    /**
     * Read a file from s3 or from the filesystem
     * @param filename
     * @return
     */
    public static String readFile(String filename) {
        try {
            if (isS3File(filename)) {
                String[] parts = filename.split("/");
                assert parts.length == 4;
                S3Object object = getInstance().getObject(new GetObjectRequest(parts[2], parts[3]));
                return IOUtils.toString(object.getObjectContent());
            } else {
                return FileUtils.readFileToString(new File(filename));
            }
        } catch (IOException e) {
            return null;
        }
    }
    
    public static List<String> listBucket(String bucketName, String prefix) {
        List<String> ret = new ArrayList<String>();
        ObjectListing listing = getInstance().listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(prefix));
        for (S3ObjectSummary s : listing.getObjectSummaries()) {
            StringBuilder sb = new StringBuilder();
            sb.append("s3://").append(bucketName).append("/").append(s.getKey());
            ret.add(sb.toString());
        }
        while (listing.isTruncated()) {
            listing = getInstance().listNextBatchOfObjects(listing);
            for (S3ObjectSummary s : listing.getObjectSummaries()) {
                StringBuilder sb = new StringBuilder();
                sb.append("s3://").append(bucketName).append("/").append(s.getKey());
                ret.add(sb.toString());
            }
        }
        return ret;
    }
   
}
