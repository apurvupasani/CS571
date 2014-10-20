package com.example.assignment9;

import java.io.Serializable;

public class YahooSuggestOptionBean implements Serializable {
 
    private String symbol,name,exchange;
    public YahooSuggestOptionBean(String symbol, String name,String exchange){
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        
    }
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	@Override
	public String toString() {
		return "YahooSuggestOptionBean [symbol=" + symbol + ", name=" + name
				+ ", exchange=" + exchange + "]";
	}
   
    
    
    
}