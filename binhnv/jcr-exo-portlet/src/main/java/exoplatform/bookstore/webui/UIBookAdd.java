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

import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInputSet;

import exoplatform.BookStoreService;
import exoplatform.bookstore.portlet.UIBookManagementPortlet;
import exoplatform.bookstore.service.BookstoreServiceUtil;
import exoplatform.entity.Book;
import exoplatform.exception.DuplicateBookException;
import exoplatform.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Aug 9, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template = "system:/groovy/webui/form/UIForm.gtmpl",
  events = {
    @EventConfig(listeners = UIBookAdd.AddActionListener.class)
  }
)
public class UIBookAdd extends UIForm implements UIPopupComponent {

  private Book book;
  
  /**
   * @param name
   * @throws Exception
   */
  public UIBookAdd() throws Exception {
    UIFormInputSet uiBookInformation;
    uiBookInformation = new UIBookInformation("UIBookInformation");
    addChild(uiBookInformation);
    setActions(new String[] {"Add"});
  }
  
  public static class AddActionListener extends EventListener<UIBookAdd> {

    @Override
    public void execute(Event<UIBookAdd> event) throws Exception {
      UIBookAdd uiBookAdd = event.getSource();
      UIBookInformation uiBookInformation = uiBookAdd.getChild(UIBookInformation.class);
      BookStoreService service = BookstoreServiceUtil.getBookstoreService();
      Book book = new Book();
      book.setCategory(Utils.bookCategoryStringToEnum(uiBookInformation.getUIFormSelectBox(UIBookInformation.CATEGORY).getValue()));
      book.setName(uiBookInformation.getUIStringInput(UIBookInformation.BOOKNAME).getValue());
      book.setContent(uiBookInformation.getUIFormTextAreaInput(UIBookInformation.CONTENT).getValue());
      try {
        service.addBookWithout(book);
        UIBookList uiBookList = uiBookAdd.getAncestorOfType(UIBookManagementPortlet.class)
            .getChild(UIBookManagement.class)
            .getChild(UIBookList.class);
        uiBookList.setBooks(service.getAllBook());
      } catch (DuplicateBookException e) {
        WebuiRequestContext ctx = event.getRequestContext();
        UIApplication uiApplication = ctx.getUIApplication();
        uiApplication.addMessage(new ApplicationMessage("Duplicate book", null, ApplicationMessage.WARNING));
      }
      UIPopupAction uiPopupAction = uiBookAdd.getParent().getParent();
      uiPopupAction.deActive();
    }
    
  }

  @Override
  public void activate() throws Exception {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deActivate() throws Exception {
    // TODO Auto-generated method stub
    
  }

  /**
   * @return the book
   */
  public Book getBook() {
    return book;
  }

  /**
   * @param book the book to set
   */
  public void setBook(Book book) {
    this.book = book;
  }

}
