    /** This method is the basic XML method called from the HTML submit. It first checks whther the file is input or not. If so, it calls
    * methods to load the XML file
    */
    function fetchXMLDetails()
    {
        var inputText = document.getElementById('urlText').value;
        inputText = inputText.trim();
        if(inputText==null || inputText == "")
        {
            printMessage("Please enter the URL to continue");
        }
        else if(inputText.indexOf('.xml')==-1&&inputText.indexOf('.XML')==-1)
        {
            printMessage("Please enter a file with .xml extension");
        }
        else
        {
            loadXML(inputText);
        }
    }
    //End of method
    /** This method contains the mechanism to load the XML file, creates a new window and dumps the XML file into an HTML content as 
    *   required.
    */
    function processxmlDoc(xmlDoc)
    {
        var elementTags = fetchElementTags(xmlDoc);
        if(elementTags.length == 0)
        {   
            printMessage("Error in XML file");
            return;
        }
        for(var elementCounter = 0 ; elementCounter<elementTags.length;elementCounter++)
        {    
        var x = xmlDoc.getElementsByTagName(elementTags[elementCounter]);
        if (x.length < 1)
        {
            printMessage("Error in XML file");
        }
        else {
          
            var headerTags = fetchTags(xmlDoc,3);
            
            var htmlContent = "";
            htmlContent +="<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Assignment 4</title><link href='Hw4.css' rel='stylesheet' type='text/css'></head><body>";

            htmlContent +="<table border=1 align='left'><tr>";
            //htmlContent +="<col width=150px><col width=160px><col width=80px><col width=150px><col width=100px><col width=200px>";
            for(var i=0;i<headerTags.length;i++)
            {
                 htmlContent +="<th class='t1'>"+headerTags[i]+"</th>";
            }
            htmlContent+="</tr>";
            for (i = 0; i < x.length; i++)
            {
                htmlContent +="<tr style='text-align:center'>";
                 for(var j = 0; j<headerTags.length;j++)
                {
                    htmlContent+="<td class='td1'>";
                    node = x[i].getElementsByTagName(headerTags[j]);
                if (node.length > 0)
                {
                    nodeValue = getNodeValue(node);
                    if(headerTags[j].indexOf("IMAGE")==-1)
                    {
                        htmlContent +=nodeValue;
                    }
                    else
                    {
                        htmlContent +="<img class='imgspecs' src='"+nodeValue+"'/>";
                    }
                }
                else
                {
                    htmlContent +="none";
                }
                    htmlContent += "</td>";
                }
                htmlContent += "</tr>";
            }
            }
            htmlContent += "</table></body></html>";
            //open a new window 
            var newWindow = window.open();
            newWindow.document.write(htmlContent);
            newWindow.document.close();
        }
    }
    //End of method
    
    /** This method performs a cleanup on the node value to give proper output
    */
    function getNodeValue(node){
            if( (node == null) || (typeof(node) === "undefined")||(typeof(node[0].childNodes[0])=="undefined"))
                return "";
            else
                return node[0].childNodes[0].nodeValue;
        }

    /** This method is used to fetch the tags based on the node types. For this mechanism, we simply use it to extract text data
    *
    */
    function fetchTags(xmlDoc,nodeType)
    {
        var node = xmlDoc.documentElement;
        var nodes = node.querySelectorAll("*");
        var childrenNodes =[];
        for (var i = 0; i < nodes.length; i++) 
        {
         var text = null;
        if (nodes[i].childNodes.length == 1 && nodes[i].childNodes[0].nodeType == nodeType) //if nodeType == text node
            if(doesNotExist(nodes[i].tagName,childrenNodes))
                childrenNodes.push(nodes[i].tagName);
        }
       return childrenNodes;
    }   
    //End of method
    /** This element is used to fetch the parent tag of all the text type or other type nodes. This makes the code more generic
     * and user friendly
    */
    function fetchElementTags(xmlDoc)
    {
     var node = xmlDoc.documentElement;
     var nodes = node.querySelectorAll("*");
      var childrenNodes =[];
        for (var i = 0; i < nodes.length; i++) 
        {
         var text = null;
        if (nodes[i].childNodes.length > 1 ) //if nodeType == text node
        {    if(doesNotExist(nodes[i].tagName,childrenNodes))
                childrenNodes.push(nodes[i].tagName);
                }
        }
       return childrenNodes;
    }

    /** This method converts the bag of elements into a set of elements. Eliminates duplicates
    */
    function doesNotExist(tagName,childrenNodes)
    {
        for(var i=0;i<childrenNodes.length;i++)
            if(childrenNodes[i] == tagName)
                return false;
                
         return true;       
    }

    /** This method takes the URL as a parameter and tries to fetch the XML file from the request. Once it gets the file, it
     *  parses the file. If file not found, or file is not parsable,it throws an error, else it calls the method to process the
     *  XML file
     */
    function loadXML(url)
    {

        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        }
        else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        try
        {
            xmlhttp.open("GET", url, false); //GET method,URL described and synchronous..hence false
            xmlhttp.send(); //call XML
            var xmlDoc = xmlhttp.responseXML;
             alert(xmlDoc);   
            if (xmlDoc==null) 
            {
                 printMessage("XML file does not exist.");
            }
            else if(xmlhttp.status==404)
            {
                printMessage("File not found");
            }
            //Firefox specific code
            else if (xmlDoc.documentElement.nodeName == "parsererror")
            {
                printMessage("Error in XML file");
            }
            else
            {
                processxmlDoc(xmlDoc);
            }
        }
        catch(exception)
        {
               if(exception.message.indexOf('restricted')!=-1)
               {
                    printMessage("Error in XML file");
               }
               else
               {
                    printMessage(exception.message);
               }
        }

    }
    //Generic method to display the errors or messages
    function printMessage(message)
    {
        alert(message);
    }