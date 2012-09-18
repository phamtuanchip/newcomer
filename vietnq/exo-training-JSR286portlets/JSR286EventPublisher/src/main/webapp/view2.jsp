<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<portlet:defineObjects/>

<div align="center">
   Welcome, <%= request.getAttribute("fullname") %> from <%= request.getAttribute("location") %>!
   <br/>
   <a href="<portlet:renderURL></portlet:renderURL>">Back</a>
</div>