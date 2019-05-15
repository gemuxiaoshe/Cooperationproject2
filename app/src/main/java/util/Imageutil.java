package util;

import android.app.Notification;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Imageutil {
    public String imageurl="";
    public String enen = "";
    public String sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                URL url = null;
                try {
                    url = new URL("https://cn.bing.com/HPImageArchive.aspx?idx=0&n=1");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in=connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                  imageurl=parseXML(response.toString());
                    enen=imageurl;
                    Log.d("333", enen);
                    Log.d("3333", imageurl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.d("33", enen);
        Log.d("999","4545");
      return imageurl;
    }
    public  String parseXML(String xmldata) {
        String url="";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int event=xmlPullParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String nodename=xmlPullParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                    {
                        if ("url".equals(nodename)) {
                            url=xmlPullParser.nextText();
                            break;
                        }
                    }

                }
                event=xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;

    }

}
