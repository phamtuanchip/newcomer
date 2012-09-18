<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<portlet:defineObjects/>
 
<div>
    <p>Enter your full name and your location. Info will be passed to other portlets </p>
        <form method="post" 
        	action="<portlet:actionURL>
        							<portlet:param name="javax.portlet.action" value="saveinfo" />
        					</portlet:actionURL>">
        <fieldset>
            <table>
                <tbody>
                <tr>
                    <th><label>Fullname:</label></th>
                    <td><input name="fullname" /></td>
                </tr>
                <tr>
                    <th><label>Location:</label></th>
                    <td><input name="location" /></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Save Info" /></td>
                </tr>
                </tbody>
            </table>
        </fieldset>
    </form>
</div>