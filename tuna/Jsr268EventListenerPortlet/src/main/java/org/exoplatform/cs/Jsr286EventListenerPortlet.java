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
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.ProcessEvent;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          tuna@exoplatform.com
 * Sept 18, 2012  
 */

public class Jsr286EventListenerPortlet extends GenericPortlet {
	private ContactInfoBean infoBean = new ContactInfoBean();
	
	@RenderMode(name="view")
	public void viewNormal(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		request.setAttribute("info", infoBean);
		getPortletContext().getRequestDispatcher("/xhtml/view.jsp").forward(request, response);
	}

	/** This method processes received events with the following QName
	 */
	@ProcessEvent(qname="{http://exoplatform.com/events}contactInfo")
	public void processContactInfoEvent(EventRequest request, EventResponse response) throws PortletException, IOException {

		String contactInfo = (String) request.getEvent().getValue();

		try {
			infoBean.setName(contactInfo.split(",")[0]);
			infoBean.setEmail(contactInfo.split(",")[1]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}