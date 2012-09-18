package org.exoplatform.cs;

/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


import java.io.IOException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.ProcessAction;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          tuna@exoplatform.com
 * Sept 18, 2012  
 */

public class Jsr286EventPublisherPortlet extends GenericPortlet {

    @RenderMode(name="help") // when having a help page request click on Help, give back help.jsp -- MAPPING purpose - tested
    protected void doHelp( RenderRequest request, RenderResponse response )
        throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/xhtml/help.jsp").include(request, response);
    }
    
    @RenderMode(name="view") // this annotation indicates that the render is for viewing purpose
    public void viewNormal( RenderRequest request, RenderResponse response )
        throws PortletException, IOException {
    	
    	getPortletContext().getRequestDispatcher("/xhtml/view.jsp").forward(request, response);
    }

    /**
     * This method processes the "savecontact" action.
     */
    @ProcessAction(name="savecontact")
    public void saveContact(ActionRequest request, ActionResponse response) throws PortletException, IOException {
 
        // Get form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        //send event
        String contactInfo = name + "," + email;
 
        // Send the event using the appropriate QName
        response.setEvent(new QName("http://exoplatform.com/events", "contactInfo"), contactInfo);
        // send contactInfo event to http://exoplatform.com/events, ListenerPortlet will retrieve the event from this
    }

}
