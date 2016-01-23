package com.tsd.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by PC on 21/07/2015.
 */
public class MergeFilesUtility {

    public static void mergeFiles(File destination, File[] sources)
            throws IOException {
        OutputStream output = null;
        try {
            output = createAppendableStream(destination);
            for (File source : sources) {
                appendFile(output, source);
            }
        }
        finally {
            output.close();
        }
    }

    private static BufferedOutputStream createAppendableStream(File destination)
            throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(destination, true));
    }

    private static void appendFile(OutputStream output, File source) throws IOException {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(source));

            byte[] buf = new byte[1024];
            int len;
            while((len=input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }
        }
        finally {
            input.close();
        }
    }
}
