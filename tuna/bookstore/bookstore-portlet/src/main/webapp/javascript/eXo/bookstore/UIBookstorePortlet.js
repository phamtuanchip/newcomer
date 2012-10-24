/**
 * import common library
 */
eXo.require('eXo.core.DOMUtil');

/**
 * define the namespace for eXo bookstore
 */
if (!eXo.bookstore) {
	eXo.bookstore = {};
} ;

if (!eXo.bookstore.webservice) {
	eXo.bookstore.webservice = {};
} ;

/**
 * constructor for UIBookstorePortlet object
 */
function UIBookstorePortlet() {
} ;

/**
 * object that encapsulates Ajax request
 */
function BookstoreAjaxRequest() {
  this.idSourceAjax = null;
  this.url = null;
  this.idResultElement = null;
  return this;
} ;

BookstoreAjaxRequest.prototype.fromElementId = function(idSourceAjax) {
  this.idSourceAjax = idSourceAjax;
  return this;
} ;

BookstoreAjaxRequest.prototype.toURL = function(url) {
  this.url = url;
  return this;
} ;

BookstoreAjaxRequest.prototype.updateResultOnElement = function(idResultElement) {
  this.idResultElement = idResultElement;
  return this;
} ;


UIBookstorePortlet.prototype.makeAjaxRequest = function(ajaxRequest, objectAttribute) {
  
  if (value.length==0) { 
    document.getElementById(ajaxRequest.idResultElement).innerHTML="";
    document.getElementById(ajaxRequest.idResultElement).style.border="0px";
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
            + " onclick=\"document.getElementById('" + ajaxRequest.idSourceAjax + "').value='"+ response[i].name + "';"
            + " document.getElementById('" + ajaxRequest.idResultElement + "').style.visibility='hidden';\">"
            + response[i].name + "</td></tr>";
        }
      responseHTML = responseHTML + "</table>";
      }
      if (objectAttribute == "title") {
        for (var i=0; i<response.length; i++) {
          responseHTML = responseHTML + "<tr><td"
            + " onmouseover=\"this.style.backgroundColor='#EBEBDA'\""
            + " onmouseout=\"this.style.backgroundColor='white'\""
            + " onclick=\"document.getElementById('" + ajaxRequest.idSourceAjax + "').value='"+ response[i].title + "';"
            + " document.getElementById('" + ajaxRequest.idResultElement + "').style.visibility='hidden';\">"
            + response[i].title + "</td></tr>";
        }
        responseHTML = responseHTML + "</table>";
      }

      document.getElementById(ajaxRequest.idResultElement).style.border="1px solid #A5ACB2";
      document.getElementById(ajaxRequest.idResultElement).style.visibility="visible";
      document.getElementById(ajaxRequest.idResultElement).innerHTML=responseHTML;
    }
  } ;
  
  xmlhttp.open("GET", ajaxRequest.url, true);
  xmlhttp.send();
} ;


UIBookstorePortlet.prototype.updateAuthor = function() {
  
  var bookAuthorElement = document.getElementById("bookAuthor");
  var ajaxResult_bookAuthor = document.createElement("div");
  ajaxResult_bookAuthor.id="ajaxResult_bookAuthor";
  ajaxResult_bookAuthor.style.visibility="hidden";
  bookAuthorElement.parentNode.appendChild(ajaxResult_bookAuthor);

  bookAuthorElement.onkeyup = function() {
    
    var updateAuthorRequest = new BookstoreAjaxRequest();
    updateAuthorRequest.fromElementId("bookAuthor")
        .toURL("/bookstore/rest/bookstore/searchAuthorByName/" + bookAuthorElement.value)
        .updateResultOnElement("ajaxResult_bookAuthor");

    UIBookstorePortlet.prototype.makeAjaxRequest(updateAuthorRequest, "name");
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
    var updateBookRequest = new BookstoreAjaxRequest();
    updateBookRequest.fromElementId("value")
        .toURL("/bookstore/rest/bookstore/searchBookByTitle/" + document.getElementById("value").value)
        .updateResultOnElement("ajaxResult_bookTitle");

    UIBookstorePortlet.prototype.makeAjaxRequest(updateBookRequest, "title");
  };  
} ;


// instanciate the object
eXo.bookstore.UIBookstorePortlet = new UIBookstorePortlet();