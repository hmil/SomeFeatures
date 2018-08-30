package com.appdynamics.appd_test.hadrienmilano.androidagenttest;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class QuoteService {

    public void getQuote(final IQuoteReceiver receiver) {
        new AsyncTask<Void, Void, String>() {

            private Exception exception;

            @Override
            protected String doInBackground(Void... params) {
                try {
                    return this.doWork();
                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            private String doWork() throws IOException, JSONException {
                URL url = new URL("http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.connect();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    return parseJSONAndExtractQuote(readStream(in));
                } finally {
                    urlConnection.disconnect();
                }
            }

            protected void onPostExecute(String quote) {
                if (quote == null) {
                    receiver.onError(exception);
                } else {
                    receiver.onQuote(quote);
                }
            }
        }.execute();
    }

    private String parseJSONAndExtractQuote(String json) throws JSONException {
        JSONArray root = new JSONArray(json);
        JSONObject obj = (JSONObject) root.get(0);
        return obj.get("content").toString();
    }

    private String readStream(InputStream in) {
        Scanner scan = new Scanner(in);
        StringBuilder str = new StringBuilder();
        while (scan.hasNext())
            str.append(scan.nextLine());
        scan.close();
        return str.toString();
    }

    public interface IQuoteReceiver {
        void onQuote(String quote);

        void onError(Exception e);
    }

}
