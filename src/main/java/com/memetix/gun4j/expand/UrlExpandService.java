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
package com.memetix.gun4j.expand;

import com.memetix.gun4j.GunshortenAPI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * MetricService
 * Concrete implementation of the Chits Metric REST Service. Use this for CRUD ops on the Metric resource
 * 
 * @author Jonathan Griggs <jonathan.griggs @ gmail.com>
 * @date Aug 18, 2011
 */
public class UrlExpandService extends GunshortenAPI {
    public static final String SERVICE_URL = BASE_SERVICE_URL + "/expand?";
    private static final String PARAM_NAME = "shortUrl";
    
    
    public static String expand(final String shortUrl) throws Exception {
        final StringBuilder sb = new StringBuilder();
        sb.append(PARAM_NAME);
        sb.append(EQUALS);
        sb.append(shortUrl);
        
        final Map<String,String> results = parseResponse(post(SERVICE_URL,sb.toString()));
        
        if(results.containsKey(shortUrl))
            return results.get(shortUrl);
        else
            return shortUrl;
    }
    
    public static Map<String,String> expand(final Set<String> shortUrls) throws Exception {
        final StringBuilder sb = new StringBuilder();
        
        int i = 0;
        for(String shortUrl : shortUrls) {
            if(i>0) {
                sb.append(AMPERSAND);
            }
            sb.append(PARAM_NAME);
            sb.append(EQUALS);
            sb.append(shortUrl);
            i++;
        }
        
        final Map<String,String> results = parseResponse(post(SERVICE_URL,sb.toString()));
        return results;
    }
    
    public static Map<String,String> expand(final List<String> shortUrls) throws Exception {
        final Set<String> urlSet = new HashSet<String>();
        urlSet.addAll(shortUrls);
        return expand(urlSet);
    }
    
    private static Map<String,String> parseResponse(final JSONObject json) {
        final Map<String,String> expandedResults = new HashMap<String,String>();
        final JSONObject data = (JSONObject)json.get("data");
        final JSONArray expand = (JSONArray)data.get("expand");
        for(Object result : expand) {
            final JSONObject expandedResult = (JSONObject)result;
            expandedResults.put((String)expandedResult.get("shortUrl"), (String)expandedResult.get("fullUrl"));
        }
        return expandedResults;
    }
}
