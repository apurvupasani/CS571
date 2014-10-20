<?php
 
    /** This class is developed for storing the YAHOO RSS feed object. This class stores the link and the title of the feed currently
    *
    */
    class YahooRSS
    {
        private $title;
        private $link;
        public function __construct($title,$link)
        {
            $this->title = $title;
            $this->link = $link;
        }
        
        public function getTitle()
        {
            return $this->clean($this->title); 
        }
         public function getLink()
        {
            return $this->clean($this->link); 
        }
        
        public function clean($strValue)
        {
            if($strValue || $strValue==="")
                return $strValue;
             else return 'N/A';   
        }
        
    }
/** This class is used to store the Yahoo Stock information. This class contains all stock information and is used to store the class details
*
*/
   class YahooStock
    {
        private $yahooXML;
        private $rssXml;
        private $rssList;
        public function __construct($yahooXML,$rssXml) {
        $this->rssXml= $rssXml;
        $this->yahooXML = $yahooXML;
        }
        public function validateRSSXML()
         {
            if((string)$this->rssXml->channel->title ==="Yahoo! Finance: RSS feed not found")
                return false;
             return true;
         }
        
        public function processRSSFeed()
        {
            if($this->validateRSSXML())
            {
                $this->rssList = array();    
                $rssItems = $this->rssXml->channel->children();
                foreach($rssItems as $posItems)
                {
                    if($posItems->getName()==="item")
                    {
                        $yahooRSS = new YahooRSS(htmlEntities($posItems->title, ENT_XML1, 'UTF-8'),htmlEntities($posItems->link, ENT_XML1, 'UTF-8'));
                    // $yahooRSS = new YahooRSS($posItems->title,$posItems->link);
                        array_push($this->rssList,$yahooRSS);    
                    }
                    
                }
                
                
            }
            else
            {
                $this->rssList = NULL;
            }
            

        }
        
  
        
        public function getRSSList()
        {
            return $this->rssList;
        }
        
         public function getBid()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->Bid);
         }  
          public function getVolume()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->Volume);
         }  
          public function getChange()
         {
             return $this->yahooXML->results->quote->Change;
         }
          public function getOpen()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->Open);
         }         
         public function getDaysLow()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->DaysLow);
         }
         public function getDaysHigh()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->DaysHigh);
         }
         public function getYearLow()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->YearLow);
         }
         public function getYearHigh()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->YearHigh);
         }
         public function getMarketCapitalization()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->MarketCapitalization);
         }
public function getLastTradePriceOnly()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->LastTradePriceOnly);
         }
public function getName()
         {
 
            return  $this->clean($this->yahooXML->results->quote->Name);
         }
public function getPreviousClose()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->PreviousClose);
         }
         public function getChangeInPercent()
         {
           return  $this->clean2($this->yahooXML->results->quote->ChangeinPercent);
         }         
          public function getSymbol()
         {
            
            return  $this->clean($this->yahooXML->results->quote->Symbol);
         }
            public function getOneyrTargetPrice()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->OneyrTargetPrice);
         }
public function getAsk()
         {
            
            return  $this->clean2($this->yahooXML->results->quote->Ask);
         }         
    public function getAverageDailyVolume()
         {
            return $this->clean2($this->yahooXML->results->quote->AverageDailyVolume);
            
         }
    
    public function clean($strValue)
        {
            if($strValue || $strValue!=="")
                return $strValue;
             else return 'N/A';   
        }
    public function clean2($strValue)
        {
            if($strValue || $strValue==="")
                return $strValue;
             else return '0';   
        }
    
    }

      $result = new SimpleXMLElement('<result/>');
     
      
    try
    {
      
       if($_SERVER['REQUEST_METHOD']=== 'GET')
        {
       
            if(isset($_GET['symbol']))
            {
                $symbol = $_GET['symbol'];
                //Check if query 1 returns something
                $content = @file_get_contents("http://query.yahooapis.com/v1/public/yql?q=Select%20Name%2C%20Symbol%2C%20LastTradePriceOnly%2C%20Change%2C%20ChangeinPercent%2C%20PreviousClose%2C%20DaysLow%2C%20DaysHigh%2C%20Open%2C%20YearLow%2C%20YearHigh%2C%20Bid%2C%20Ask%2C%20AverageDailyVolume%2C%20OneyrTargetPrice%2C%20MarketCapitalization%2C%20Volume%2C%20Open%2C%20YearLow%20from%20yahoo.finance.quotes%20where%20symbol%3D%22".$symbol."%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
                if($content === false || strlen($content) === 0) 
                { 
                    throw new Exception("Could not retrieve page. Please fix your query,refresh the page and try again");
                }
                $yahooStockDetails = new SimpleXMLElement($content); 
            
                if($yahooStockDetails === false) 
                {  
                   throw new Exception("Malformed XML.Please fix your query and try again.");
                }
                
               
                 $yahooData =$yahooStockDetails; 
       
                $RSSFeedURI = "http://feeds.finance.yahoo.com/rss/2.0/headline?s=".$symbol."&region=US&lang=en-US"; 
                 $content = @file_get_contents($RSSFeedURI);
                 if($content === false || strlen($content) === 0) 
                 { 
                    throw new Exception("Could not retrieve page. Please fix your query,refresh the page and try again");
                 } 
                 $yahooRSSDetails = new SimpleXMLElement($content); 
                 if($yahooRSSDetails === false)
                 { 
                 throw new Exception("Malformed XML.Please fix your query and try again.");
                 }
                 $yahooStock  = new YahooStock($yahooData,$yahooRSSDetails);
                 $yahooStock->processRSSFeed();
                 $outputList = $yahooStock->getRSSList();
                
                if($yahooStock->getChange() === NULL || strcmp($yahooStock->getChange(),"")==0)
                {
                    throw new Exception("Stock information not found!");
                }
                
                //Set the Quote details here
                $result->addChild("Name",$yahooStock->getName());    
                $result->addChild("Symbol",$yahooStock->getSymbol());    
                $quote = $result->addChild("Quote");
                $val = number_format(floatVal($yahooStock->getChange()),2,".",",");
                if($val >= 0)
                $quote->addChild("ChangeType","+");
                else
                $quote->addChild("ChangeType","-");
                $quote->addChild("Change",abs($val));
                $quote->addChild("ChangeInPercent",number_format(abs(floatVal($yahooStock->getChangeInPercent())),2,'.',',')."%");
                $quote->addChild("LastTradePriceOnly",number_format(floatVal($yahooStock->getLastTradePriceOnly()),2,'.',','));
                $quote->addChild("PreviousClose",number_format(floatVal($yahooStock->getPreviousClose()),2,'.',','));
                $quote->addChild("DaysLow",number_format(floatVal($yahooStock->getDaysLow()),2,'.',','));
                $quote->addChild("DaysHigh",number_format(floatVal($yahooStock->getDaysHigh()),2,'.',','));
                $quote->addChild("Open",number_format(floatVal($yahooStock->getOpen()),2,'.',','));
                $quote->addChild("YearLow",number_format(floatVal($yahooStock->getYearLow()),2,'.',','));
                $quote->addChild("YearHigh",number_format(floatVal($yahooStock->getYearHigh()),2,'.',','));
                $quote->addChild("Bid",number_format(floatVal($yahooStock->getBid()),2,'.',','));
                $quote->addChild("Volume",number_format(floatVal($yahooStock->getVolume()),2,'.',','));
                $quote->addChild("Ask",number_format(floatVal($yahooStock->getAsk()),2,'.',','));
                $quote->addChild("AverageDailyVolume",number_format(floatVal($yahooStock->getAverageDailyVolume()),2,'.',','));
                $quote->addChild("OneYearTargetPrice",number_format(floatVal($yahooStock->getOneyrTargetPrice()),2,'.',','));
                $marketCap =  $yahooStock->getMarketCapitalization();
                $marketCap = number_format(floatVal(substr($marketCap,0,strlen($marketCap)-1)),1,'.',',').substr($marketCap,strlen($marketCap)-1,strlen($marketCap));
                $quote->addChild("MarketCapitalization",$marketCap);                
                
                //Set the rss feeds details here
                 $news = $result->addChild("News");
             
            if($outputList != NULL || count($outputList) > 0)
            { 
                foreach($outputList as $list) 
                {
                    $item = $news->addChild("Item");
                    $item->addChild("Link",$list->getLink());
                    $item->addChild("Title", $list->getTitle());
                } 
                    
     
            }
            else
            {
                $item = $news->addChild("Item");
                $item->addChild("Link","");
                $item->addChild("Title","No records found");
            }
                //Set the graph details here
                 $result->addChild("StockChartImageURL",htmlEntities("http://chart.finance.yahoo.com/t?s=".$symbol."&lang=enUS&width=300&height=180",ENT_XML1, 'UTF-8'));
                
            }
            
        }
    
    }catch(Exception $e)    
    { 
           $result->addChild("Error",$e->getMessage());
           
    } 
     Header('Content-type: text/xml');
     print($result->asXML());
    
    
     ?>
    