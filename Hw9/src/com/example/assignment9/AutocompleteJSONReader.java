package com.example.assignment9;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
 
import org.json.JSONArray;
import org.json.JSONObject;
 
public class AutocompleteJSONReader {
     double current_latitude,current_longitude;
     public AutocompleteJSONReader(){}
    
     public List<YahooSuggestOptionBean> getParseJsonWCF(String sName)
        {
    	 
         List<YahooSuggestOptionBean> ListData = new ArrayList<YahooSuggestOptionBean>();
         try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL("http://autoc.finance.yahoo.com/autoc?query="+temp+"&callback=YAHOO.Finance.SymbolSuggest.ssCallback");
            System.out.println("http://autoc.finance.yahoo.com/autoc?query="+temp+"&callback=YAHOO.Finance.SymbolSuggest.ssCallback");
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String jsonp = reader.readLine();
            String json = jsonp.substring(jsonp.indexOf("(") + 1, jsonp.lastIndexOf(")"));
            JSONObject jsonResponse = new JSONObject(json);
            JSONObject jsonResultSet = jsonResponse.getJSONObject("ResultSet");
            JSONArray jsonArray = jsonResultSet.getJSONArray("Result");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject r = jsonArray.getJSONObject(i);
                ListData.add(new YahooSuggestOptionBean(r.getString("symbol"),r.getString("name"),r.getString("exch")));
            }
            System.out.println(ListData);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
         System.out.println(e1.getMessage());
        }
         return ListData;
 
        }
     
    
}