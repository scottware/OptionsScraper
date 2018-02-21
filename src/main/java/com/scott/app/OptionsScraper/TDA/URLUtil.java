package com.scott.app.OptionsScraper.TDA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class URLUtil {

	public static File getLogDir() {

		return new File("c:\\temp\\");

	}

	public static String getfromURL(String str) throws IOException {
		URL url = new URL(str);
		return StringHelper.inputStreamtoString(url.openStream());
	}

	public static String sendURLPostRequest(String urlstr, LinkedHashMap<String, String> paramOHM) throws IOException {

		URL url = new URL(urlstr);
		URLConnection urlConn = url.openConnection();
		urlConn.setDoInput(true); // Let the run-time system (RTS) know that we
									// want input.
		urlConn.setDoOutput(true); // Let the RTS know that we want to do
									// output.
		urlConn.setUseCaches(false); // No caching, we want the real thing.
		urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// Specify
																						// the
																						// content
																						// type.
		// Send POST output.
		DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());

		StringBuffer data = new StringBuffer();

		Iterator<String> iterator = paramOHM.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			String value = paramOHM.get(key);
			System.out.println(key + " " + value);

			data.append(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
			data.append("&");
		}

		printout.writeBytes(data.toString());
		printout.flush();
		printout.close();
		// Get response data.

		String resp = StringHelper.inputStreamtoString(urlConn.getInputStream());
		return resp;
	}

}