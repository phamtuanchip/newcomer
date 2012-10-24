// import common library
eXo.require('eXo.core.DOMUtil');

/**
 * define the namespace for eXo bookstore
 */
if (!eXo.bookstore) {
	eXo.bookstore = {};
}

if (!eXo.bookstore.webservice) {
	eXo.bookstore.webservice = {};
}

/**
 * constructor for UIBookstorePortlet object
 */
function UIBookstorePortlet() {
} ;


UIBookstorePortlet.prototype.makeAjaxRequest = function(RESTurl, idComponentToShowResult, idSourceAjax, objectAttribute) {
  
  if (value.length==0) { 
    document.getElementById(idComponentToShowResult).innerHTML="";
    document.getElementById(idComponentToShowResult).style.border="0px";
    return;
  }

  if (window.XMLHttpRequest) {
  // code for IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp = new XMLHttpRequest();
  }
  else {
  // code for IE6, IE5
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  
  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState==4 && xmlhttp.status==200) {

      var response = eval ("(" + xmlhttp.responseText + ")");
      var responseHTML = "<table>";
      
      if (objectAttribute == "name") {
        for (var i=0; i<response.length; i++) {
          responseHTML = responseHTML + "<tr><td"
            + " onmouseover=\"this.style.backgroundColor='#EBEBDA'\""
            + " onmouseout=\"this.style.backgroundColor='white'\""
            + " onclick=\"document.getElementById('" + idSourceAjax + "').value='"+ response[i].name + "';"
            + " document.getElementById('" + idComponentToShowResult + "').style.visibility='hidden';\">"
            + response[i].name + "</td></tr>";
        }
      responseHTML = responseHTML + "</table>";
      }
      
      if (objectAttribute == "title") {
        for (var i=0; i<response.length; i++) {
          responseHTML = responseHTML + "<tr><td"
            + " onmouseover=\"this.style.backgroundColor='#EBEBDA'\""
            + " onmouseout=\"this.style.backgroundColor='white'\""
            + " onclick=\"document.getElementById('" + idSourceAjax + "').value='"+ response[i].title + "';"
            + " document.getElementById('" + idComponentToShowResult + "').style.visibility='hidden';\">"
            + response[i].title + "</td></tr>";
        }
        responseHTML = responseHTML + "</table>";
      }

      document.getElementById(idComponentToShowResult).style.border="1px solid #A5ACB2";
      document.getElementById(idComponentToShowResult).style.visibility="visible";
      document.getElementById(idComponentToShowResult).innerHTML=responseHTML;
    }
  }
  
  xmlhttp.open("GET", RESTurl, true);
  xmlhttp.send();
} ;


UIBookstorePortlet.prototype.updateAuthor = function() {
  
  var bookAuthorElement = document.getElementById("bookAuthor");
  var ajaxResult_bookAuthor = document.createElement("div");
  ajaxResult_bookAuthor.id="ajaxResult_bookAuthor";
  ajaxResult_bookAuthor.style.visibility="hidden";
  bookAuthorElement.parentNode.appendChild(ajaxResult_bookAuthor);

  bookAuthorElement.onkeyup = function() {
    UIBookstorePortlet.prototype.makeAjaxRequest("/bookstore/rest/bookstore/searchAuthorByName/" + bookAuthorElement.value, 
      "ajaxResult_bookAuthor", "bookAuthor", "name");
  };  
} ;


UIBookstorePortlet.prototype.updateBook = function() {
  var DOMUtil = eXo.core.DOMUtil;

  // insert element into UIBookstorePortlet
  var portletElement = document.getElementById("UIBookstorePortlet");
  var searchElement = DOMUtil.findFirstDescendantByClass(portletElement, "div", "Input Search");
  if (!searchElement) alert("searchElement null");
  var ajaxResult_bookTitle = document.createElement("div");
  ajaxResult_bookTitle.id="ajaxResult_bookTitle";
  ajaxResult_bookTitle.style.visibility="hidden";
  searchElement.parentNode.appendChild(ajaxResult_bookTitle);

  searchElement.onkeyup = function() {
    UIBookstorePortlet.prototype.makeAjaxRequest("/bookstore/rest/bookstore/searchBookByTitle/" + document.getElementById("value").value, 
      "ajaxResult_bookTitle", "value", "title");
  };  
} ;


// instanciate the object
eXo.bookstore.UIBookstorePortlet = new UIBookstorePortlet();