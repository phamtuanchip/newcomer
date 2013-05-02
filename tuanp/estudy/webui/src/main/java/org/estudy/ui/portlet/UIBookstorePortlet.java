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
package org.estudy.ui.portlet;

import org.estudy.learning.service.ComponentLocator;
import org.estudy.ui.webui.UIBookstoreContainer;
import org.estudy.ui.webui.action.UIPopupAction;
import org.estudy.ui.webui.popup.UIBookAddManager;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 4, 2012  
 */

@ComponentConfig(
  lifecycle = UIApplicationLifecycle.class,
  template  = "app:/groovy/bookstore/webui/UIBookstorePortlet.gtmpl"
)
public class UIBookstorePortlet extends UIPortletApplication 
{
  public static Log log = ExoLogger.getExoLogger(UIBookstorePortlet.class);
  
  public UIBookstorePortlet() throws Exception 
  {
    log.info("--- UIBookstorePortlet constructor ---");
    
    initApplication();
    addMainContainer();
    addPopupManager();
  }
  
  private void initApplication() throws Exception
  {
    ExoContainer eContainer = ExoContainerContext.getCurrentContainer();
    ComponentLocator.setContainer(eContainer);
    ComponentLocator.emptyDefaultNodes();
    ComponentLocator.initDefaultNodes();
    ComponentLocator.initBookstore();
  }
  
  private void addMainContainer() throws Exception
  {
    addChild(UIBookstoreContainer.class, null, null);
  }
  
  private void addPopupManager() throws Exception
  {
    UIPopupAction uiPopup = addChild(UIPopupAction.class, null, "UIBookstorePopupAction");
    uiPopup.getChild(UIPopupWindow.class).setId("UIBookAddPopup");
    uiPopup.addChild(UIBookAddManager.class, null, "UIBookAddManager").setRendered(false);
  }
  
}
