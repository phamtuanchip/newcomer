<%@page import="org.exoplatform.cs.ContactInfoBean"%>

<%!ContactInfoBean contactInfo = null;%>
<%
//retrieve the object from the session
contactInfo = (ContactInfoBean) request.getAttribute("info");
 
if ((contactInfo.getName() == null) && (contactInfo.getEmail() ==null)) {
%>
	<div><h4>No contact information.</h4></div>
    
<% } else {
%>
	<div>
        <table>
        <tbody>
            <tr>
                <th>Name:</th>
                <td><%=contactInfo.getName() %></td>
            </tr>
            <tr>
                <th>Email:</th>
                <td><%=contactInfo.getEmail() %></td>
            </tr>
        </tbody>
        </table>
    </div>
<%
}
%>