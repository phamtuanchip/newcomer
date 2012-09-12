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

import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.event.EventListener;

import exoplatform.BookStoreService;
import exoplatform.bookstore.portlet.UIBookManagementPortlet;
import exoplatform.bookstore.service.BookstoreServiceUtil;
import exoplatform.entity.Book;
import exoplatform.exception.BookNotFoundException;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Aug 6, 2012  
 */
@ComponentConfig(
    template = "app:/groovy/webui/UIBookList.gtmpl",
    events = {
      @EventConfig(listeners=UIBookList.EditActionListener.class),
      @EventConfig(listeners=UIBookList.DeleteActionListener.class),
      @EventConfig(listeners=UIBookList.AddActionListener.class)
    }
)
public class UIBookList extends UIComponent {

  private List<Book> books = new ArrayList<Book>();
  
  public UIBookList() {
    books = BookstoreServiceUtil.getAllBook(); 
  }
  
  public static class EditActionListener extends EventListener<UIBookList> {

    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      String bookId = ctx.getRequestParameter(OBJECTID);
      UIBookList uiBookList = event.getSource();
      UIPopupAction popupAction = uiBookList.getAncestorOfType(UIBookManagementPortlet.class)
                                            .getChild(UIPopupAction.class);
      popupAction.activate(UIBookEdit.class, 600, 400);
      UIBookEdit uiBookEdit = (UIBookEdit) popupAction.getChild(UIPopupWindow.class).getUIComponent();
      Book book = new Book();
      book = BookstoreServiceUtil.getBookstoreService().getBook(bookId);
      uiBookEdit.setBook(book);
      uiBookEdit.fillBookInformation();
      ctx.addUIComponentToUpdateByAjax(popupAction);
    }
    
  }
  
  public static class DeleteActionListener extends EventListener<UIBookList> {

    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      UIBookList uiBookList = event.getSource();
      String bookId = ctx.getRequestParameter(OBJECTID);
      try {
        BookStoreService service = BookstoreServiceUtil.getBookstoreService();
        service.deleteBook(bookId);
        uiBookList.setBooks(service.getAllBook());
      } catch (BookNotFoundException e) {
        UIApplication uiApplication = ctx.getUIApplication();
        uiApplication.addMessage(new ApplicationMessage("Can not delete book", null, ApplicationMessage.ERROR));
      }
      ctx.addUIComponentToUpdateByAjax(uiBookList);
    }
    
  }
  
  public static class AddActionListener extends EventListener<UIBookList> {

    @Override
    public void execute(Event<UIBookList> event) throws Exception {
      WebuiRequestContext ctx = event.getRequestContext();
      UIBookList uiBookList = event.getSource();
      UIPopupAction uiPopupAction = uiBookList.getAncestorOfType(UIBookManagementPortlet.class)
                                              .getChild(UIPopupAction.class);
      uiPopupAction.activate(UIBookAdd.class, 600, 400);
      UIBookAdd uiBookAdd = (UIBookAdd) uiPopupAction.getChild(UIPopupWindow.class).getUIComponent();
      ctx.addUIComponentToUpdateByAjax(uiPopupAction);
    }
    
  }
  
  /**
   * @return the books
   */
  public List<Book> getBooks() {
    return books;
  }

  /**
   * @param books the books to set
   */
  public void setBooks(List<Book> books) {
    this.books = books;
  }
  
}
