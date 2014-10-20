package com.example.assignment9;
/** This class contains mechansism for capturing the JSON data and converting it to appropriate JAVA object
 * 
 * @author Apurv
 *
 */
import java.util.*;

import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.*;
public class StockDataBean 
{
	//{"MarketCapitalization":"368.0B","ChangeInPercent":"2.07%","Open":"542.76","LastTradePriceOnly":"547.57","YearLow":"518.46","Bid":"547.18",
	//"AverageDailyVolume":"2,633,070.00","ChangeType":"+","Ask":"547.57","PreviousClose":"536.44","Change":"11.13","OneYearTargetPrice":"0.00",
	//"DaysLow":"540.00","Volume":"1,911,118.00","YearHigh":"604.83","DaysHigh":"549.90"}}
	
	private String marketCapitalization;
	private String changeInPercent;
	private String open;
	private String lastTradePriceOnly;
	private String yearLow;
	private String bid;
	private String averageDailyVolume;
	private String changeType;
	private String ask;
	private String previousClose;
	private String change;
	private String oneYearTargetPrice;
	private String daysLow;
	private String volume;
	private String yearHigh;
	private String daysHigh;
	private String symbol;
	private String name;
	ArrayList<StockFeedsBean> stockFeeds;
	private String imgURL;
	
	
	public StockDataBean()
	{
		
	}

	public void extractJSON(String jsonString) throws Throwable
	{
		JSONObject dataTotal= new JSONObject(jsonString);
		JSONObject data = (JSONObject)dataTotal.get("result");
		
		imgURL = (String) data.get("StockChartImageURL");
		
		symbol = (String) data.get("Symbol");
		name = (String) data.get("Name");
		JSONObject quotes = (JSONObject)data.get("Quote");
		marketCapitalization = (String) quotes.get("MarketCapitalization");
		changeInPercent = (String) quotes.get("ChangeInPercent");
		open = (String) quotes.get("Open");
		lastTradePriceOnly = (String) quotes.get("LastTradePriceOnly");
		yearLow = (String) quotes.get("YearLow");
		bid = (String) quotes.get("Bid");
		averageDailyVolume = (String) quotes.get("AverageDailyVolume");
		changeType = (String) quotes.get("ChangeType");
		ask = (String) quotes.get("Ask");
		previousClose = (String) quotes.get("PreviousClose");
		change = (String) quotes.get("Change");
		oneYearTargetPrice = (String) quotes.get("OneYearTargetPrice");
		volume = (String) quotes.get("Volume");
		yearHigh = (String) quotes.get("YearHigh");
		daysHigh = (String) quotes.get("DaysHigh");
		daysLow = (String) quotes.get("DaysLow");
	
		JSONObject feeds = (JSONObject)data.get("News");
		JSONArray feedList =  (JSONArray)feeds.get("Item");
		ArrayList<StockFeedsBean> objStockFeeds = new ArrayList<StockFeedsBean>();
		
		for(int i = 0;i<feedList.length();i++  ) {
			JSONObject feed = feedList.getJSONObject(i);
			StockFeedsBean stockFeed = new StockFeedsBean();
			stockFeed.setTitle((String) feed.get("Title"));
			stockFeed.setLink((String) feed.get("Link"));
			objStockFeeds.add(stockFeed);
		}
		stockFeeds = objStockFeeds;
		
		
		
	}
	
	
	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public String getMarketCapitalization() {
		return marketCapitalization;
	}

	public void setMarketCapitalization(String marketCapitalization) {
		this.marketCapitalization = marketCapitalization;
	}

	public String getChangeInPercent() {
		return changeInPercent;
	}

	public void setChangeInPercent(String changeInPercent) {
		this.changeInPercent = changeInPercent;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getLastTradePriceOnly() {
		return lastTradePriceOnly;
	}

	public void setLastTradePriceOnly(String lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}

	public String getYearLow() {
		return yearLow;
	}

	public void setYearLow(String yearLow) {
		this.yearLow = yearLow;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getAverageDailyVolume() {
		return averageDailyVolume;
	}

	public void setAverageDailyVolume(String averageDailyVolume) {
		this.averageDailyVolume = averageDailyVolume;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getAsk() {
		return ask;
	}

	public void setAsk(String ask) {
		this.ask = ask;
	}

	public String getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(String previousClose) {
		this.previousClose = previousClose;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getOneYearTargetPrice() {
		return oneYearTargetPrice;
	}

	public void setOneYearTargetPrice(String oneYearTargetPrice) {
		this.oneYearTargetPrice = oneYearTargetPrice;
	}

	public String getDaysLow() {
		return daysLow;
	}

	public void setDaysLow(String daysLow) {
		this.daysLow = daysLow;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getYearHigh() {
		return yearHigh;
	}

	public void setYearHigh(String yearHigh) {
		this.yearHigh = yearHigh;
	}

	public String getDaysHigh() {
		return daysHigh;
	}

	public void setDaysHigh(String daysHigh) {
		this.daysHigh = daysHigh;
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

	public ArrayList<StockFeedsBean> getStockFeeds() {
		return stockFeeds;
	}

	public void setStockFeeds(ArrayList<StockFeedsBean> stockFeeds) {
		this.stockFeeds = stockFeeds;
	}

	@Override
	public String toString() {
		return "StockDataBean [marketCapitalization=" + marketCapitalization
				+ ", changeInPercent=" + changeInPercent + ", open=" + open
				+ ", lastTradePriceOnly=" + lastTradePriceOnly + ", yearLow="
				+ yearLow + ", bid=" + bid + ", averageDailyVolume="
				+ averageDailyVolume + ", changeType=" + changeType + ", ask="
				+ ask + ", previousClose=" + previousClose + ", change="
				+ change + ", oneYearTargetPrice=" + oneYearTargetPrice
				+ ", daysLow=" + daysLow + ", volume=" + volume + ", yearHigh="
				+ yearHigh + ", daysHigh=" + daysHigh + ", symbol=" + symbol
				+ ", name=" + name + ", stockFeeds=" + stockFeeds + ", imgURL="
				+ imgURL + "]";
	}

	
	
	
	

}

