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
package exoplatform.bookstore.webui;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;

import exoplatform.BookStoreService;
import exoplatform.bookstore.portlet.UIBookManagementPortlet;
import exoplatform.bookstore.service.BookstoreServiceUtil;
import exoplatform.entity.Book;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Aug 9, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template="app:/groovy/webui/UISearch.gtmpl",
  events = {
    @EventConfig(listeners = UIBookSearch.SearchActionListener.class)
  }
)
public class UIBookSearch extends UIForm {

  /**
   * 
   */
  public UIBookSearch() {
    addUIFormInput(new UIFormStringInput("txtSearch",null,null));
  }

  public static class SearchActionListener extends EventListener<UIBookSearch> {

    @Override
    public void execute(Event<UIBookSearch> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      UIBookSearch uiBookSearch = event.getSource();
      BookStoreService service = BookstoreServiceUtil.getBookstoreService();
      String bookName = uiBookSearch.getUIStringInput("txtSearch").getValue();
      List<Book> bookList = new ArrayList<Book>();
      bookList = service.searchBookByName(bookName);
      UIBookList uiBookList = ((UIBookManagement) uiBookSearch.getParent()).getChild(UIBookList.class);
      uiBookList.setBooks(bookList);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiBookList);
    }
    
  }

}
