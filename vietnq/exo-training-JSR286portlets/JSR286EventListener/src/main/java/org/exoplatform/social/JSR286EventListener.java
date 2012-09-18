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
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.xml.namespace.QName;
import javax.portlet.ProcessEvent;
import javax.portlet.RenderMode;
/**
 * Created by The eXo Platform SAS
 * Author : Nguyen Quoc Viet
 *          vietnq@exoplatform.com
 * Apr 20, 2012  
 */
public class JSR286EventListener extends GenericPortlet {
  private InfoBean infoBean = new InfoBean();
  
  @RenderMode(name="view")
  public void viewRender(RenderRequest request, RenderResponse response) throws IOException, PortletException{
    request.setAttribute("info", infoBean);
    getPortletContext().getRequestDispatcher("/view.jsp").forward(request, response);
  }
  @ProcessEvent(qname="{http://quocviet.net/events}info")
  public void processInfo(EventRequest request,EventResponse response){
    String info = (String)request.getEvent().getValue();
    InfoBean infobean = new InfoBean();
    infobean.setFullName("No name");
    infobean.setLocation("No location");
    try{
      infobean.setFullName(info.split(",")[0]);
      infobean.setLocation(info.split(",")[1]);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    this.infoBean = infobean;
  }
}

