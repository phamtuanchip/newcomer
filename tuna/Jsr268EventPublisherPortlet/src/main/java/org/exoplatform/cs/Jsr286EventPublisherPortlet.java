package org.exoplatform.cs;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
// what the fck is that??

public class Jsr286EventPublisherPortlet extends GenericPortlet {

    @RenderMode(name="help")
    protected void doHelp( RenderRequest request, RenderResponse response )
        throws PortletException, IOException {
    	getPortletContext().getRequestDispatcher("/xhtml/help.jsp").include(request, response);
        //helpView.include( request, response );
    }
    
    @RenderMode(name="view") // this annotation indicates that the render is for viewing purpose
    public void viewNormal( RenderRequest request, RenderResponse response )
        throws PortletException, IOException {

    	getPortletContext().getRequestDispatcher("/xhtml/view.jsp").forward(request, response);
    }

    /**
     * This method processes the "savecontact" action.
     * The form parameters are added to the Hashtable object that will be sent as the event value
     */
    @ProcessAction(name="savecontact")
    public void saveContact(ActionRequest request, ActionResponse response) throws PortletException, IOException {
 
        // Get form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
 
        //send event
        String contactInfo = name + "," + email;
 
        // Send the event using the appropriate QName
        response.setEvent(new QName("http:mycompany.com/events", "contactInfo"), contactInfo);
    }

}
