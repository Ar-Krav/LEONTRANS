package leontrans.leontranstm.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SiteDataListener extends AsyncTask<Void, Void, String> {

    private HttpURLConnection urlConnection = null;
    private BufferedReader reader = null;
    private String resultJson = "";
    private String urlAddress;

    public SiteDataListener(String urlAddress) {
        this.urlAddress = urlAddress;
    }


    @Override
    protected String doInBackground(Void... params) {

        try {
            URL url = new URL(urlAddress);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        urlConnection.disconnect();
        return resultJson;
    }
}
