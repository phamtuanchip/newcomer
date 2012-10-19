/**
 * Test function - module eXo.bookstore.UIBookstorePortlet
 */

// Define the object
/*
var eXo = {
	bookstore : {
		UIBookstorePortlet : {}
	}
} ;
*/

/*
function sayHi(name) {
	alert('hi' + name);
} ;
*/

/**
 * Define constructor for object like Calendar 
 */

/*
function UIBookstorePortlet() {
  this.name = 'bookstore';
} ; 

UIBookstorePortlet.prototype.sayHello = function(name) {
  alert('hello from ' + this.name + ' to '+ name);    
} ;
*/

/*
UIBookstorePortlet.prototype.showResult = function(value) {
  if (value.length==0) { 
    document.getElementById("livesearch").innerHTML="";
    document.getElementById("livesearch").style.border="0px";
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
      document.getElementById("livesearch").innerHTML=xmlhttp.responseText;
      document.getElementById("livesearch").style.border="1px solid #A5ACB2";
    }
  }

  xmlhttp.open("GET", "livesearch.php?q=" + value, true);
  xmlhttp.send();
} ;
*/

// create a simple instance for gtmpl to reference to
// so the var eXo.bookstore.UIBookstorePortlet is one instance of UIBookstorePortlet
//eXo.bookstore.UIBookstorePortlet = new UIBookstorePortlet();