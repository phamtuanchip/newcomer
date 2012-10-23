// import common library
eXo.require('eXo.core.JSON');
eXo.require('eXo.core.DOMUtil');
eXo.require('eXo.portal.PortalHttpRequest')

// define the namespace for eXo bookstore
if (!eXo.bookstore) {
	eXo.bookstore = {};
}

if (!eXo.bookstore.webservice) {
	eXo.bookstore.webservice = {};
}

// defining simple constructor for the object
function UIBookstorePortlet() {
  this.name = "UIBookstorePortlet";
} ;

UIBookstorePortlet.prototype.showResult = function(value) {

  if (value.length==0) { 
    document.getElementById("ajaxResult").innerHTML="";
    document.getElementById("ajaxResult").style.border="0px";
    return;
  }

  if (window.XMLHttpRequest) {
  // code for IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp=new XMLHttpRequest();
  }
  else {
  // code for IE6, IE5
    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
  }
  
  xmlhttp.onreadystatechange=function() {
    if (xmlhttp.readyState==4 && xmlhttp.status==200) {
      alert("receive ajax response");
      var response = eval ("(" + xmlhttp.responseText + ")");
      var responseHTML = "<table>";
      for (var i=0; i<response.length; i++) {
        responseHTML = responseHTML + "<tr><td>" + response[i].name + "</td></tr>";
      }
      responseHTML = responseHTML + "</table>";

      document.getElementById("ajaxResult").style.border="1px solid #A5ACB2";
      document.getElementById("ajaxResult").innerHTML=responseHTML;
    }
  }
  
  alert("making ajax request to bookstore with value " + value);
  xmlhttp.open("GET", "/bookstore/rest/bookstore/searchAuthorByName/" + value, true);
  xmlhttp.send();
} ;

UIBookstorePortlet.prototype.sayHello = function() {
  var DOMUtil = eXo.core.DOMUtil; 
  
  // insert element into UIBookstorePortlet
  var portletElement = document.getElementById("UIBookstorePortlet");
  var ajaxResult = document.createElement("div");
  ajaxResult.innerHTML = "result ajax";
  ajaxResult.id="ajaxResult";
  portletElement.appendChild(ajaxResult);

  var bookAuthorElement = document.getElementById("bookAuthor");

  bookAuthorElement.onkeyup = function() {
    alert("text: " + bookAuthorElement.value);
    UIBookstorePortlet.prototype.showResult(bookAuthorElement.value);
  };  
} ;

// instanciate the object
eXo.bookstore.UIBookstorePortlet = new UIBookstorePortlet();