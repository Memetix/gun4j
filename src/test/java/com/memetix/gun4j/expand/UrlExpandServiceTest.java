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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * Unit tests for the Gunshorten Expand service
 * 
 * @author Jonathan Griggs <jonathan.griggs @ gmail.com>
 * @date Aug 18, 2011
 */
public class UrlExpandServiceTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    public UrlExpandServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getServiceUrl method, of class MetricService.
     */
    @Test
    public void testGetServiceUrl() {
        String expResult = System.getProperty("gunshorten.api.url")!=null ? System.getProperty("gunshorten.api.url") + "/expand?" : "http://gunshorten.cloudfoundry.com/api/expand?";
        String result = UrlExpandService.SERVICE_URL;
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testExpandUrl() throws Exception {
        String shortUrl = "http://t.co/UdEWcNm";
        String result = UrlExpandService.expand(shortUrl);
        String expResult = "http://twitcaps.com/search?q=dust+storm";
        assertNotNull(result);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testExpandUnshortUrl() throws Exception {
        String shortUrl = "http://twitcaps.com/search?q=dust+storm";
        String result = UrlExpandService.expand(shortUrl);
        String expResult = "http://twitcaps.com/search?q=dust+storm";
        assertNotNull(result);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testExpandMultipleUrlsSet() throws Exception {
        Set<String> shortUrls = new HashSet<String>();
        
        String shortUrl1 = "http://t.co/UdEWcNm";
        String shortUrl2 = "http://t.co/g5GuXeQ";
        
        shortUrls.add(shortUrl1);
        shortUrls.add(shortUrl2);
        
        Map<String,String> results = UrlExpandService.expand(shortUrls);
        String expResult1 = "http://twitcaps.com/search?q=dust+storm";
        String expResult2 = "http://twitcaps.com/search?q=waterpop";
        
        assertNotNull(results);
        assertEquals(2,results.size());
        assertEquals(expResult1, results.get(shortUrl1));
        assertEquals(expResult2, results.get(shortUrl2));
    }
    
    @Test
    public void testExpandMultipleUrlsList() throws Exception {
        List<String> shortUrls = new ArrayList<String>();
        
        String shortUrl1 = "http://t.co/UdEWcNm";
        String shortUrl2 = "http://t.co/g5GuXeQ";
        String shortUrl3 = "http://t.co/g5GuXeQ";
        
        shortUrls.add(shortUrl1);
        shortUrls.add(shortUrl2);
        shortUrls.add(shortUrl3);
        
        Map<String,String> results = UrlExpandService.expand(shortUrls);
        String expResult1 = "http://twitcaps.com/search?q=dust+storm";
        String expResult2 = "http://twitcaps.com/search?q=waterpop";
        
        assertNotNull(results);
        assertEquals(2,results.size());
        assertEquals(expResult1, results.get(shortUrl1));
        assertEquals(expResult2, results.get(shortUrl2));
        assertEquals(expResult2, results.get(shortUrl3));
    }
}
