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
package org.exoplatform.bookstore.webui.popup;

import java.io.Writer;

import org.exoplatform.bookstore.webui.action.UIPopupAction;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 18, 2012  
 */
@ComponentConfig(
  lifecycle = UIContainerLifecycle.class
)

public class UIBookAddManager extends UIContainer implements UIPopupComponent
{
  private static final Log log = ExoLogger.getExoLogger(UIBookAddManager.class);
  
  public UIBookAddManager() throws Exception
  {
    log.info("--- UIBookAddManager constructor ---");
    
    addBookAddForm();
    addPopupManager();
  }
  
  public void addBookAddForm() throws Exception
  {
   addChild(UIBookAddForm.class, null, "UIBookAddForm");
  }
  
  private void addPopupManager() throws Exception
  {
    UIPopupAction uiPopup = addChild(UIPopupAction.class, null, "UIAuthorAddPopupAction");
    uiPopup.getChild(UIPopupWindow.class).setId("UIAuthorAddPopup");
    uiPopup.addChild(UIAuthorAddForm.class, null, "UIAuthorAddForm").setRendered(false);
  }
  
  @Override
  public void activate() throws Exception { }

  @Override
  public void deActivate() throws Exception { }

  @Override
  public void processRender(WebuiRequestContext context) throws Exception 
  {
    Writer w = context.getWriter();
    w.write("<div id=\"UIBookAddManager\" class=\"UIBookAddManager\">");
    renderChildren();
    w.write("</div>");
  }
}
