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
package org.exoplatform.bookstore.webui;

import org.exoplatform.bookstore.portlet.UIBookstorePortlet;
import org.exoplatform.bookstore.webui.action.UIPopupAction;
import org.exoplatform.bookstore.webui.popup.UIBookAddManager;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 18, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template  = "system:/groovy/webui/form/UIForm.gtmpl",
  events    = {
      @EventConfig(listeners = UIBookActionBar.OpenAddBookPopupActionListener.class),
  }
)

public class UIBookActionBar extends UIForm
{
  private static final Log log = ExoLogger.getExoLogger(UIBookActionBar.class);
  
  public UIBookActionBar() throws Exception
  {
  }
    
  static public class OpenAddBookPopupActionListener extends EventListener<UIBookActionBar>
  {
    @Override
    public void execute(Event<UIBookActionBar> event) throws Exception {
      log.info("--- OpenAddBookPopup Event received ---");
      
      UIPopupAction uiPopup = event.getSource()
          .getAncestorOfType(UIBookstorePortlet.class).getChild(UIPopupAction.class);
      uiPopup.activate(UIBookAddManager.class, 500);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiPopup);
    }
    
  }
}
