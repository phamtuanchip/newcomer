<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<portlet:defineObjects/>
 
 <div>
    <p>Use the form below to create a contact. The data is sent over to other portlets.</p>
    <%-- The action name must be passed as a param, not as an actionURL attribute. Fix will come with Liferay 5.0.2 --%>
    <form method="post" action="<portlet:actionURL>
    
    <portlet:param name="javax.portlet.action" value="savecontact" />
    </portlet:actionURL>">
    <%-- define savecontact ACTION, this action is processed by ProcessAction savecontact --%>
    
    <fieldset>
    <table>
    <tbody>
    	<tr>
    	<th><label>Contact name:</label></th>
        <td><input name="name" /></td>
        </tr>
        <tr>
        <th><label>Contact e-mail:</label></th>
        <td><input name="email" /></td>
        </tr>
        <tr>
        <td colspan="2"><input type="submit" value="Save contact" /></td>
        </tr>
        </tbody>
        </table>
    </fieldset>
   </form>
</div>