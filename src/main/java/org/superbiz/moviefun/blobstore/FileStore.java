package org.superbiz.moviefun.blobstore;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.superbiz.moviefun.albums.AlbumsController;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;


public class FileStore implements BlobStore {
    public FileStore() {
    }

    @Override
    public void put(Blob blob) throws IOException {

        File file = new File(blob.name);
        saveUploadToFile(blob,file);

    }

    private void saveUploadToFile(Blob blob, File targetFile) throws IOException {
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
       // targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(IOUtils.toByteArray(blob.inputStream));
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException, URISyntaxException {
        File coverFile = new File(name);
        InputStream inputStream = new FileInputStream(coverFile);
        if(coverFile.exists()){
            return Optional.of(new Blob(name,inputStream,new Tika().detect(name)));
        }
        else{
            return Optional.empty();
        }
    }


    @Override
    public void deleteAll() {

    }
}