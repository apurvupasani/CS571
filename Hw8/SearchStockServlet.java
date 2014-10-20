package servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.*;

import java.net.*;
import java.util.*;
/** This class is the servlet implementation of stock servlet. This class contains implementation of doGet and doPost methods which internally pass to a doSearchProcess method
 *  which is a subhelper method and does lot of operations internally to return a JSON
 *
 *  @author: Apurv Upasani
 */
public class SearchStockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor (unused). 
     */
    public SearchStockServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doSearchProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doSearchProcess(request, response);
	}
	
	/** This method is used to perform the process of fetching the url response from the PHP page
	 *  deployed on Amazon AWS server. Then parse the XML response into an XML DOM object and convert
	 *  the parsed XML object into the JSON string. Return the JSON string back to the response.
	 *  Set the response as text/JSON and return the response to the calling function
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	private void doSearchProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String output="";
		//Fetch the request parameter symbol from the request. TBD
		try
		{
		String symbol = request.getParameter("symbol");
		if(symbol == null||"".equals(symbol))
		{
			throw new Exception("No Symbol found");
		}

		String input  = fetchURLContents(symbol);
		System.out.println(input);
		Document doc = parseXML(input);
		JSONObject object = createJSONResponse(doc);
		response.setContentType("text/json");
		
		output = object.toString();
		
		
		}
		catch(Throwable objThrowable)
		{
			JSONObject exceptionJSON = new JSONObject();
			exceptionJSON.put("error", "exception");
			exceptionJSON.put("message", objThrowable.getMessage());
			output = exceptionJSON.toString();
		}
		finally
		{
			//Write the response and send the response back to the calling method
			PrintWriter out = response.getWriter();
			String correctedOutput = output.replaceAll("\\\\", "");
			System.out.println(correctedOutput);
			out.println(correctedOutput);
		}
	}
	/** This method is used to fetch the contents of the URL on the remote location.
	 * 
	 * @param String
	 * @return String
	 * @throws Throwable
	 */
	private String fetchURLContents(String strSymbol) throws Throwable
	{
		InputStream urlStream = null;
		StringBuffer sb = new StringBuffer(); 
		if(strSymbol!=null)
		{
			strSymbol = strSymbol.trim();
			String urlString = "http://default-environment-ypirrxkyfv.elasticbeanstalk.com/?symbol="+strSymbol;
			URL objURL = new URL(urlString);
			URLConnection objURLConnection = objURL.openConnection();
			objURLConnection.setAllowUserInteraction(false);
			urlStream = objURL.openStream();
			BufferedReader br  = new BufferedReader(new InputStreamReader(urlStream));
			String s = "";
			 while((s=br.readLine())!=null)
				 sb.append(s);
			 urlStream.close();
			 

		}
	
		return sb.toString();
	}
	//End of method
	
	/** This is a private method which takes the url stream as the input and returns the domfactory
	 *  object.
	 * 
	 * @param InputStream
	 * @return XML Document
	 * @throws Throwable
	 */
	private Document parseXML(String xml) throws Throwable
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
		SAXBuilder xmlBuilder = new SAXBuilder();
		Document doc = xmlBuilder.build(stream);
		return doc;
	}
	//End of method
	/** This method is used to create a JSON response given the xml document. It creates the JSON object and it returns it to the calling method
    *   @param XML Document
    *   @return JSONObject
    *   @throws Throwable
    */
	private JSONObject createJSONResponse(Document document) throws Throwable
	{
		Element result = document.getRootElement();
		List children  = result.getChildren();
		
		try
		{
		if(children.isEmpty())
		{
			JSONObject errorJSON = new JSONObject();
			errorJSON.put("error", "Zero results found!");
			return errorJSON;
		}
		else
		{
			//Check for errors. If there exists, then return error JSON
			for(int i = 0;i<children.size();i++)
			{
				Element e = (Element)children.get(i);
				if(e.getName().equalsIgnoreCase("Error"))
				{
					throw new Exception(e.getText());
				}
			}
			
			//Fetch children element nodes
			Element objName = (Element)children.get(0);
            Element objQuote = (Element)children.get(2);
			Element arrObjNews = (Element)children.get(3);
			Element objSymbol = (Element)children.get(1);
			Element objStockImageURL = (Element)children.get(4);
			//Create the JSON object for result
			JSONObject resultJSON = new JSONObject();
			resultJSON.put("Name", objName.getText());
			resultJSON.put("Symbol", objSymbol.getText());
			
			//Create the JSON object for quote
			JSONObject quoteJSON = new JSONObject();
			quoteJSON.put("ChangeType", objQuote.getChildText("ChangeType"));
			quoteJSON.put("Change", objQuote.getChildText("Change"));
			quoteJSON.put("DaysLow", objQuote.getChildText("DaysLow"));
            quoteJSON.put("ChangeInPercent", objQuote.getChildText("ChangeInPercent"));
			quoteJSON.put("LastTradePriceOnly", objQuote.getChildText("LastTradePriceOnly"));
			quoteJSON.put("Open", objQuote.getChildText("Open"));
			quoteJSON.put("Bid", objQuote.getChildText("Bid"));
			quoteJSON.put("YearLow", objQuote.getChildText("YearLow"));
			quoteJSON.put("YearHigh", objQuote.getChildText("YearHigh"));
			quoteJSON.put("Volume", objQuote.getChildText("Volume"));
			quoteJSON.put("OneYearTargetPrice", objQuote.getChildText("OneYearTargetPrice"));
            quoteJSON.put("DaysHigh", objQuote.getChildText("DaysHigh"));
			quoteJSON.put("Ask", objQuote.getChildText("Ask"));
			quoteJSON.put("AverageDailyVolume", objQuote.getChildText("AverageDailyVolume"));
			quoteJSON.put("PreviousClose", objQuote.getChildText("PreviousClose"));
			quoteJSON.put("MarketCapitalization", objQuote.getChildText("MarketCapitalization"));
			
			//Create the JSON object for News
			JSONObject newsJSON = new JSONObject();
			JSONArray objJSONArray = new JSONArray();
			List obj = (List)arrObjNews.getChildren();
			for(int i = 0; i<obj.size();i++)
			{
				Element objItems = (Element)obj.get(i);
				JSONObject itemObject = new JSONObject();
				itemObject.put("Title", objItems.getChildText("Title").replaceAll("\"", ""));
				itemObject.put("Link", objItems.getChildText("Link"));
				objJSONArray.put(itemObject);
			}
			newsJSON.put("Item", objJSONArray);
			resultJSON.put("StockChartImageURL", objStockImageURL.getText());
            resultJSON.put("Quote", quoteJSON);
			resultJSON.put("News", newsJSON);
			
			JSONObject outputObject = new JSONObject();
			outputObject.put("result", resultJSON);
			return outputObject;

		}
		}catch(Exception e)
		{

			JSONObject exceptionJSON = new JSONObject();
			exceptionJSON.put("error", "exception");
			exceptionJSON.put("message", e.getMessage());
			return exceptionJSON;
			
		}
		finally
		{
			//Do nothing
		}
	}
	//End of method
	
	
}
//End of class