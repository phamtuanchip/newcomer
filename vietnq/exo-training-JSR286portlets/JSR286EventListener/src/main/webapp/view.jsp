
<%@page import="org.exoplatform.social.InfoBean"%>
<%!InfoBean info = null;%>
<%
//retrieve the object from the session
info = (InfoBean) request.getAttribute("info");

if (info != null) {
 
%>
    <div>
        <table>
        <tbody>
            <tr>
                <th>Name:</th>
                <td><%=info.getFullName() %></td>
            </tr>
            <tr>
                <th>Location:</th>
                <td><%=info.getLocation() %></td>
            </tr>
        </tbody>
        </table>
    </div>
<% } else {
    %><p>No information.</p><%
}
%>