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

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.SpecialCharacterValidator;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 4, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template  = "app:/groovy/bookstore/webui/UIBookSearchForm.gtmpl",
  events = {
      @EventConfig(listeners = UIBookSearchForm.SearchActionListener.class)
  }
)
public class UIBookSearchForm extends UIForm {

  public static Log log = ExoLogger.getExoLogger(UIBookSearchForm.class);
  
  private static final String FIELD_SEARCHVALUE = "value" ;
  
  public UIBookSearchForm() throws Exception
  {
    addChild(new UIFormStringInput(FIELD_SEARCHVALUE, FIELD_SEARCHVALUE, null));
  }
  
  public String getSearchValue() 
  {
    return getUIStringInput(FIELD_SEARCHVALUE).getValue() ;
  }
  
  static public class SearchActionListener extends EventListener<UIBookSearchForm>
  {
    public void execute(Event<UIBookSearchForm> event) throws Exception
    {
      log.info("--- search action received ---");
      
      
    }
    
  }
}
