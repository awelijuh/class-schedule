package com.example.awelijuh.classschedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {
    private static String url = "http://mftab.brsu.by/?curs=1&download";
    private Context context;
    private Downloaded downloaded;

    public Downloader(Context context, Downloaded downloaded) {
        this.context = context;
        this.downloaded = downloaded;
    }

    private static void saveUrl(Context context, final String urlString)
            throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        String path = context.getCacheDir() + "/" + "schedule";
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            fout = new FileOutputStream(path);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }

    public void download() {
        Download download = new Download();
        download.execute();
    }




    @SuppressLint("StaticFieldLeak")
    class Download extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                saveUrl(context, url);
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            super.onPostExecute(ok);
            downloaded.onDownloaded(ok);
        }
    }


}
