package com.example.group3.csci3130_group3_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UrlDownload
{

    public static String readUrl(String myurl)throws IOException
    {
        String data="";
        InputStream inputStream=null;
        HttpsURLConnection urlConnection=null;

        try {
            URL url=new URL(myurl);
            urlConnection=(HttpsURLConnection)url.openConnection();
            urlConnection.connect();

            inputStream=urlConnection.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer=new StringBuffer();
            String line="";
            while ((line=br.readLine())!=null)
            {
                stringBuffer.append(line);

            }

            data=stringBuffer.toString();
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            inputStream.close();
            urlConnection.disconnect();
        }

        return data;
    }
}
