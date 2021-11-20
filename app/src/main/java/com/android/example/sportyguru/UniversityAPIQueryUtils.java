package com.android.example.sportyguru;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class UniversityAPIQueryUtils {
    public static final String LOG_TAG = UniversityAPIQueryUtils.class.getName();
    private static String JSON_RESPONSE;

    public UniversityAPIQueryUtils() {
    }

    public List<University> extractUniversity(String stringurl) {


        // Create an empty ArrayList that we can start adding earthquakes to
        List<University> universityList = new ArrayList<>();
        try {
            JSON_RESPONSE = makeHTTPrequest(stringurl);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            JSONArray rootJASONObject = new JSONArray(JSON_RESPONSE);

            for (int i = 0; i < rootJASONObject.length(); i++) {
                JSONObject current = rootJASONObject.getJSONObject(i);
                /**
                 * "state-province":"Punjab",
                 *       "country":"India",
                 *       "web_pages":[
                 *          "http://www.davietjal.org/"
                 *       ],
                 *       "name":"DAV Institute of Engineering & Technology",
                 *       "alpha_two_code":"IN",
                 *       "domains":[
                 *          "davietjal.org"
                 *       ]*/
                String name = current.getString("name");
                String state_province = current.getString("state-province");
                JSONArray web_pages = current.getJSONArray("web_pages");
                String web_page = null;
                if (web_pages.length() > 0) {
                    web_page = web_pages.getString(0);
                }
                String country = current.getString("country");
                String alpha_two_code = current.getString("alpha_two_code");

                JSONArray domains = current.getJSONArray("domains");
                String domain = null;
                if (domains.length() > 0) {
                    domain = domains.getString(0);
                }

                universityList.add(new University(name, state_province, country, web_page, domain, alpha_two_code));

            }

        } catch (JSONException e) {

            Log.e(LOG_TAG, "Problem parsing the api JSON results", e);
        }

        // Return the list of University
        return universityList;
    }

    public String makeHTTPrequest(String stringURl) throws IOException {
        URL url = createUrl(stringURl);
        String jsonResponce = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /*millisecond*/);
            urlConnection.setConnectTimeout(10000 /*millisecond*/);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponce = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {

            Log.e(LOG_TAG, "return exceptin" + e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponce;

    }

    private URL createUrl(String stringURL) {
        URL urls = null;
        try {
            urls = new URL(stringURL);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "reutn MalformedURLException");
            return null;
        }
        return urls;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

}
