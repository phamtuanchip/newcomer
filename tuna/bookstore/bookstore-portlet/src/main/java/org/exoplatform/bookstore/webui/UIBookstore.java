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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.webui.config.annotation.ComponentConfig;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 4, 2012  
 */

@ComponentConfig(
  template = "app:/groovy/bookstore/webui/UIBookstore.gtmpl"
)

public class UIBookstore extends UIContainer {
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public UIBookstore() throws Exception {
    List sections = getSections();
    Iterator iterator = sections.iterator();
    
    while(iterator.hasNext()) {
      addChild((Class) iterator.next(), null, null);
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  private List getSections() {
    List sections = new ArrayList();
    sections.add(UIBookSearch.class);
    sections.add(UIBookList.class);
    return sections;
  }
  
}
