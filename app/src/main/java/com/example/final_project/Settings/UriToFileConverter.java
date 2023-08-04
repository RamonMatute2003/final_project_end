package com.example.final_project.Settings;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UriToFileConverter{
    public static File convertUriToFile(Context context, Uri uri, String ext) throws IOException {
        File file = createTempFile(context, ext);
        copyUriToFile(context, uri, file);
        return file;
    }

    private static File createTempFile(Context context, String ext) throws IOException {
        String fileName = "temp_" + System.currentTimeMillis();
        return File.createTempFile(fileName, ext, context.getCacheDir());
    }

    private static void copyUriToFile(Context context, Uri uri, File file) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Error opening InputStream for Uri: " + uri);
        }

        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(Uri.fromFile(file));
            if (outputStream == null) {
                throw new IOException("Error opening OutputStream for File: " + file.getAbsolutePath());
            }
            byte[] buffer = new byte[20 * 1024]; // 4KB buffer (you can adjust this as needed)
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            inputStream.close();
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
