/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
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
package org.estudy.ui.view;

import java.util.Collection;

import org.estudy.learning.model.ESession;
import org.estudy.learning.storage.DataStorage;
import org.estudy.learning.storage.impl.JcrDataStorage;
import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * May 6, 2013  
 */

@ComponentConfig(
                 template =  "app:/groovy/estudy/webui/UILessionList.gtmpl", 
                 events = {
                     @EventConfig(listeners = UILessionList.AddLessionActionListener.class),
                     @EventConfig(listeners = UILessionList.TestActionListener.class)
                 }
    )

public class UILessionList extends UIContainer {
  Collection<ESession> list;

  public UILessionList() {
    DataStorage service = getApplicationComponent(JcrDataStorage.class);
    try {
      list = service.getSessions();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static public class AddLessionActionListener extends EventListener<UILessionList> {
    @Override
    public void execute(Event<UILessionList> event) throws Exception {
      UILessionList listview = event.getSource() ;

    }
  }

  static public class TestActionListener extends EventListener<UILessionList> {
    @Override
    public void execute(Event<UILessionList> event) throws Exception {
      UILessionList listview = event.getSource() ;

    }
  }
}
