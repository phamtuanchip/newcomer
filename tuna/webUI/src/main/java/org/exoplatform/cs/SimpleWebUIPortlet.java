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
package org.exoplatform.cs;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 28, 2012  
 */

import java.util.Iterator;

import javax.portlet.PortletMode;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

@ComponentConfig
(
  lifecycle = UIApplicationLifecycle.class,
  template  = "app://groovy/webui/SimpleWebUIPortlet.gtmpl",
  events    = {@EventConfig(listeners = SimpleWebUIPortlet.OpenPopupActionListener.class)}
)

public class SimpleWebUIPortlet extends UIPortletApplication 
{

  private static final Log log = ExoLogger.getLogger(SimpleWebUIPortlet.class) ;
  
  public SimpleWebUIPortlet() throws Exception 
  {
    log.info("\n--- SimpleWebUIPortlet constructor ---\n");
  }

  public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    
    log.info("\n List of events configured: \n"); 
    
    Iterator it = (Iterator) this.getComponentConfig().getEvents().iterator();
    
    while (it.hasNext()) {
      org.exoplatform.webui.config.Event event = 
          (org.exoplatform.webui.config.Event) it.next();
      log.info("\n event: " + event.getName() + "\n");
      log.info("\n event listener: " + 
          this.getComponentConfig().getUIComponentEventListeners(event.getName()).toString() +
      "\n");
    }
    
    PortletRequestContext pContext = (PortletRequestContext) context ;
    PortletMode currentMode = pContext.getApplicationMode() ;
    
    log.info("\n current mode: " + currentMode.toString() + "\n");
    
    if (PortletMode.VIEW.equals(currentMode)) {
        
      if (this.getChild(SimpleUIForm.class) == null) 
        this.addChild(SimpleUIForm.class, null, null);
    } 
     
    super.processRender(app, context);
  }
  
  
  static public class OpenPopupActionListener extends EventListener<SimpleWebUIPortlet>
  {
    @Override
    public void execute(Event<SimpleWebUIPortlet> event) throws Exception {
      log.info("\n event " + event.getName() + " received \n");
    } 
  }
}
