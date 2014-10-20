            var data = null;
      
             // call back function
              var YAHOO = {
            Finance: {
                SymbolSuggest: {}
            }
        };
        
        var myResp = myResp || {};
        
    // Create a new YUI instance and populate it with the required modules.
    YUI().use('tabview','node','event','autocomplete-highlighters', 'autocomplete-filters','autocomplete','datasource-get','json-parse', function (Y) {
         var tabview = new Y.TabView({
            srcNode: '#demo'
        });

        tabview.render();
        
        
           function locateModules(response) {
        var results = (response && response.data && response.data.results) || [];

        return Y.Array.filter(results, function (result) {
          return result._type === 'module';
        });
      }

      Y.one('#symbol').plug(Y.Plugin.AutoComplete, {
        resultHighlighter: 'phraseMatch',
        resultListLocator: YAHOO.Finance.SymbolSuggest.ssCallback,
        //resultTextLocator: 'name',
        activateFirstItem: true,
        source: function (request, response) {  
                        myResp = response;
                             query = Y.one('#symbol').get('value');        
                              if (query.length > 0)
                            {

                                  $.ajax({
                                  type: "GET",
                  url: "http://autoc.finance.yahoo.com/autoc",
                  data: {query: query},
                  dataType: "jsonp",
                  jsonp : "callback",
                  crossDomain:true,
                  jsonpCallback: "YAHOO.Finance.SymbolSuggest.ssCallback",
              });
 
 
              YAHOO.Finance.SymbolSuggest.ssCallback = function (response)
              {
                          
                var suggestions = [];
                data = response.ResultSet.Result;
                $.each(data, function(i, val) {
                    var symbol1  =  val.symbol.replace(/[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi, '');
                    var name1  =  val.name.replace(/[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi, '');
                    var exch1  =  val.exch.replace(/[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi, '');
                    var ip = symbol1 + ", "+name1+"("+exch1+")";
                    suggestions.push(ip);
                });
                myResp(suggestions);
              }
              
 
          } 
    }      
                  
      });
      
        Y.one('#symbol').ac.on("select", function (e) {setTimeout(function(){ doClickStuff();},1000);});
  
  
      	$('#symbol').keypress(function (e) 
        {
            if (e.which == 13) 
            {
                $('#searchButton').click();
                return false;  
            }
        });
        
    function doClickStuff()
  {
	$("#searchButton").click();
  }
  
  
    
      
      
      Y.one("#searchButton").on("click", function (e) {
             Y.one("#displayPart").hide();
              Y.one("#noStockDiv").hide();
            e.preventDefault();
             //var regex = /^[a-zA-Z1-9]{6,}$/;
            var regex = /^\w{2,}\,\w{1,}\(\w{1,}\)$/;
             
             var symbolValue = Y.one('#symbol').get('value');
             symbolValue = symbolValue.toUpperCase();
             //Check whether the symbol has been entered
            if(symbolValue == null || symbolValue=="")
                    {
                        alert("Enter company/symbol");
                        $("symbol").focus();
                        return false;
                    }
             //Basic validations to check whether the symbol is proper or not
           /* if(regex.test(symbolValue)==false)
            {
                alert("Enter correct company/symbol");
               $("symbol").focus();
                return false;
            }*/
            if(symbolValue.indexOf(",")!=-1)
            {
                    symbolValue = symbolValue.split(",")[0].trim();
            }
            
            
            
            //Now the main work begins. Here I call the servlet and get the JSON and parse the JSON
        
        var uri = 'http://cs-server.usc.edu:13562/examples/SearchStockServlet';
        $.ajax({
                  type: "GET",
                  url: uri,
                  data: {symbol: symbolValue},
                  dataType: "json",
                 error: function(xhr, status, error) {  
                        alert(error);
                    },
                 success: function(data){
                    
                    parseData(data);
                    
                 }
              });
             
      
      });
      
      function parseData(jsonString)
      {
        
           data = Y.JSON.parse(JSON.stringify(jsonString));
           if(data.error != null)
           {
                Y.one("#noStockDiv").show();
                Y.one("#noStockDiv").set("innerHTML",'<font size="+3" color="#FFFFFF">'+data.message+'</font>');
                Y.one("#displayPart").hide();
           }
           else
           {
           var quote = data.result.Quote;
           var news = data.result.News;
           var imageURL = data.result.StockChartImageURL;
           
           //Start setting up quote information
           Y.one("#PrevClose").set('innerHTML',quote.PreviousClose);
           Y.one("#DaysRange").set('innerHTML',(quote.DaysLow+" - "+quote.DaysHigh));
           Y.one("#Open").set('innerHTML',quote.Open);
           Y.one("#YearRange").set('innerHTML',(quote.YearLow+" - "+quote.YearHigh));
           Y.one("#Bid").set('innerHTML',quote.Bid);
           Y.one("#Volume").set('innerHTML',quote.Volume);
           Y.one("#Ask").set('innerHTML',quote.Ask);
           Y.one("#AvgVolume").set('innerHTML',quote.AverageDailyVolume);
           Y.one("#TargetEst").set('innerHTML',quote.OneYearTargetPrice);
           Y.one("#MarketCap").set('innerHTML',quote.MarketCapitalization);
           Y.one("#companyNameNSymbol").set('innerHTML',data.result.Name+"&nbsp;("+data.result.Symbol+")");
          
           var img = "up_g.gif";
           var color ="#00ff00";
           if(quote.ChangeType=="-")
           {
                    color = "#ff0000";
                    img = "down_r.gif";
           }
           Y.one("#LastTradePrice").set('innerHTML','<font size="+4" color="#FFFFFF">'+quote.LastTradePriceOnly+'</font>&nbsp;<img src = "http://www-scf.usc.edu/~csci571/2014Spring/hw6/'+img+'"/>&nbsp;<font color='+color+' size="+2">'+
       quote.Change+"&nbsp;("+quote.ChangeInPercent+")</font>");
           
           //Start setting image information
            Y.one("#stockChartImage").set('src',imageURL);
            
           //Start setting news information
             var linkHTMLOutput = "<div>";   
             
             if(news.Item.length == 1 && news.Item[0].Title=="No records found")
             {
                linkHTMLOutput+="<p align='center'>News Information Not Available</p>";
             }
             else
             {
            for(var i=0;i<news.Item.length;i++)
            {
                linkHTMLOutput+="<li><a style='color:#000000;' target='_blank' href='"+news.Item[i].Link+"'>"+news.Item[i].Title+"</a></li>";
            }
            }
            linkHTMLOutput+="</div>";
            Y.one("#newsFeeds").set('innerHTML',linkHTMLOutput);
              Y.one("#noStockDiv").hide();
            Y.one("#displayPart").show();
            }
      }
      
      
      
      // Following is the code for facebook integration 
      
      Y.one("#fb-post").on("click", function(e) {
		message = null;
     
			message = {
				title: data.result.Name,
                subtitle:('Stock Information of ').concat(data.result.Name, '(', data.result.Symbol,')'),
                            picture: data.result.StockChartImageURL,
				description: ('Last Trade Price:').concat(data.result.Quote.LastTradePriceOnly,',Change: ',data.result.Quote.ChangeType,data.result.Quote.Change,' (',data.result.Quote.ChangeInPercent,')')
			
			}; 
    
		FB.ui({
			method: 'feed',
			name: message.title,
            picture: message.picture,
		    description: message.description,
		    caption: message.subtitle,
		    link: 'http://finance.yahoo.com/q?s='+data.result.Symbol
		  },
		  
		  function(response) {
		    if (response && response.post_id) {
		      alert('Post was published.');
		    }   
		  });

	});
      //End of method
      
      
    });