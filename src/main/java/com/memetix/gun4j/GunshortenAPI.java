/*
 * Copyright 2011 Memetix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memetix.gun4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 * GunshortenAPI
 *
 * Makes the generic Gunshorten API calls. Concrete service implementations then extend this Abstract class
 * to make the specific service calls.
 * 
 * @author Jonathan Griggs <jonathan.griggs @ gmail.com>
 * @date Aug 18, 2011
 */
public abstract class GunshortenAPI {
     //Encoding type
    protected static final String ENCODING = "UTF-8";
    private static String referrer;
    protected static final String BASE_SERVICE_URL = System.getProperty("gunshorten.api.url")!=null ? System.getProperty("gunshorten.api.url") : "http://gunshorten.cloudfoundry.com/api";
    protected static final String SLASH = "/";
    protected static final String AMPERSAND = "&";
    protected static final String EQUALS = "=";
    
    public static void setReferrer(final String pReferrer) {
    	referrer = pReferrer;
    }
    
    protected static JSONObject post(final String serviceUrl, final String paramsString) throws Exception {
        final URL url = new URL(serviceUrl);
        final HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        if(referrer!=null)
            uc.setRequestProperty("referer", referrer);
        
        uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=" + ENCODING);
        uc.setRequestProperty("Accept-Charset",ENCODING);
        uc.setRequestMethod("POST");
        uc.setDoOutput(true);

        final PrintWriter pw = new PrintWriter(uc.getOutputStream());
        pw.write(paramsString);
        pw.close();
        uc.getOutputStream().close();

        try {
            final int responseCode = uc.getResponseCode();
            final String result = inputStreamToString(uc.getInputStream());
            if(responseCode!=200) {
                throw new Exception("Error from Gunshorten API: " + result);
            }
            return parseJSON(result);
        } finally { 
                uc.getInputStream().close();
                if (uc.getErrorStream() != null) {
                        uc.getErrorStream().close();
                }
        }
    }
    /**
     * Reads an InputStream and returns its contents as a String.
     * Also effects rate control.
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     * @throws Exception on error.
     */
    private static String inputStreamToString(final InputStream inputStream) throws Exception {
    	final StringBuilder outputBuilder = new StringBuilder();
    	
    	try {
    		String string;
    		if (inputStream != null) {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
    			while (null != (string = reader.readLine())) {
    				outputBuilder.append(string).append('\n');
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[gun4j] Error reading response stream.", ex);
    	}
    	
    	return outputBuilder.toString();
    }
    
    private static JSONObject parseJSON(final String jsonResponse) throws ParseException {
        return (JSONObject)JSONValue.parse(jsonResponse);
    }
}
