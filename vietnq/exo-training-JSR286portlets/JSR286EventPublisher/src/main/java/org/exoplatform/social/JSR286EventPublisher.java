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
package org.exoplatform.social;

import javax.portlet.GenericPortlet;
import java.io.IOException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.xml.namespace.QName;
import javax.portlet.ProcessAction;
import javax.portlet.RenderMode;
/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          vietnq@exoplatform.com
 * Apr 20, 2012  
 */
public class JSR286EventPublisher extends GenericPortlet {
  @RenderMode(name="view")
  public void viewRender(RenderRequest request, RenderResponse response) throws IOException, PortletException {
    String fullname = (String)request.getParameter("fullname");
    String location = (String)request.getParameter("location");

    if(fullname!=null && location!=null){
      request.setAttribute("fullname", fullname);
      request.setAttribute("location", location);
      getPortletContext().getRequestDispatcher("/view2.jsp").include(request, response);

    } else {
    getPortletContext().getRequestDispatcher("/view.jsp").include(request, response);
    }
  }
  @ProcessAction(name="saveinfo")
  public void saveinfo(ActionRequest request, ActionResponse response){
    String fullname = (String)request.getParameter("fullname");
    String location = (String)request.getParameter("location");
    String info = fullname + "," + location;
    response.setRenderParameter("fullname",fullname);
    response.setRenderParameter("location", location);
    response.setEvent(new QName("http://quocviet.net/events","info"), info);
  }
}
