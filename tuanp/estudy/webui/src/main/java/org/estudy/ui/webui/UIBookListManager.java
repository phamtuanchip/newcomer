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
package org.estudy.ui.webui;

import java.io.Writer;
import java.util.List;
import java.util.Set;

import org.estudy.learning.domain.Book;
import org.estudy.learning.service.ComponentLocator;
import org.estudy.learning.storage.BookStorage;
import org.estudy.ui.util.BookstoreUtil;
import org.exoplatform.commons.utils.LazyPageList;
import org.exoplatform.commons.utils.ListAccessImpl;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIGrid;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 11, 2012  
 */
@ComponentConfig(
  lifecycle = UIContainerLifecycle.class,
  events = {
    @EventConfig(listeners = UIBookListManager.EditActionListener.class),
    @EventConfig(listeners = UIBookListManager.DeleteActionListener.class, confirm = "UIBookListManager.msg.confirm-delete")
  }  
)

public class UIBookListManager extends UIContainer implements UIPopupComponent 
{
  private static final Log log = ExoLogger.getExoLogger(UIBookListManager.class); 
  
  public static String FIELD_TO_SEND_BY_AJAX = "isbn";

  public static String[] FIELDS_TO_DISPLAY = {"title", "isbn", "author"};
  
  public static String[] ACTIONS = {"Edit", "Delete"};
    
  @Override
  public void activate() throws Exception {}

  @Override
  public void deActivate() throws Exception {}
  
  @Override
  public void processRender(WebuiRequestContext context) throws Exception 
  {
    Writer w = context.getWriter();
    w.write("<div id=\"UIBookListManager\" class=\"UIBookListManager\">");
    renderChildren();
    w.write("</div>");
  }
  
  public UIBookListManager() throws Exception 
  {
    log.info("--- UIBookListManager constructor ---");
        
    this.setName("UIBookListManager") ;  
    addComponentToListBooks();
    addComponentToEditBook();  
    addHomeButton();
    updateListBookComponent(BookstoreUtil.getAllBooksFromStorage());
  }
  
  private void addComponentToEditBook() throws Exception
  {
    addChild(UIBookEditForm.class, null, null);
  }
  
  private void addComponentToListBooks() throws Exception
  {
    UIGrid componentThatListBooks = addChild(UIGrid.class, null, "UIBookList");
    componentThatListBooks.configure(FIELD_TO_SEND_BY_AJAX, 
                                     FIELDS_TO_DISPLAY, ACTIONS);
    // set pagination 
    componentThatListBooks.getUIPageIterator().setId("BookPageIterator");
  }
  
  private void addHomeButton() throws Exception 
  {
    addChild(UIBookButton.class, null, null).setRendered(false);
  }

  public void enableHomeButton() throws Exception
  {
    getChild(UIBookButton.class).setRendered(true);
  }
  
  public void disableHomeButton() throws Exception
  {
    getChild(UIBookButton.class).setRendered(false);
  }
  
  public void updateListBookComponent(Set<Book> books) throws Exception 
  {
    log.info("--- update Grid ---");
    
    // update UI list of books
    UIGrid listBooksComponent = getChild(UIGrid.class) ; 
    LazyPageList<Book> pageList = new LazyPageList<Book>(
        new ListAccessImpl<Book>(Book.class, BookstoreUtil.convertToList(books)), 10);
    listBooksComponent.getUIPageIterator().setPageList(pageList) ;  
  }
  
  public void disableEditBookComponent() throws Exception
  {
    log.info("--- disable edit book component ---");
    getChild(UIBookEditForm.class).setRendered(false);
  }
  
  public void enableEditBookComponent() throws Exception
  {
    log.info("--- enable edit book component ---");
    getChild(UIBookEditForm.class).setRendered(true);
  }
  
  public long getCurrentPage() 
  {
    return getChild(UIGrid.class).getUIPageIterator().getCurrentPage() ;
  }
  
  public long getAvailablePage() 
  {
    return getChild(UIGrid.class).getUIPageIterator().getAvailablePage() ;
  }
  
  public void setCurrentPage(int page) throws Exception 
  {
    getChild(UIGrid.class).getUIPageIterator().setCurrentPage(page) ;
  }
  
  public void resetForm() 
  {
    getChild(UIBookEditForm.class).reset() ;
  }
  
  
  static public class EditActionListener extends EventListener<UIBookListManager> 
  {    
    public void execute(Event<UIBookListManager> event) throws Exception
    {
      log.info("--- edit book event received ---");
      
      UIBookListManager listBookContainer = event.getSource();
      String bookIsbn = event.getRequestContext().getRequestParameter(OBJECTID) ;

      UIBookEditForm editBookComponent = listBookContainer.getChild(UIBookEditForm.class); 
      editBookComponent.updateUI(BookstoreUtil.getBookFromStorage(bookIsbn));
            
      listBookContainer.updateListBookComponent(BookstoreUtil.getAllBooksFromStorage());
      listBookContainer.enableEditBookComponent();
      // update UI by JS
      event.getRequestContext().addUIComponentToUpdateByAjax(listBookContainer);
    }
  }
  
  static public class DeleteActionListener extends EventListener<UIBookListManager>
  {
    public void execute(Event<UIBookListManager> event) throws Exception
    {
      log.info("--- delete book event received ---");
      
      UIBookListManager listBookContainer = event.getSource();
      String bookIsbn = event.getRequestContext().getRequestParameter(OBJECTID) ;
      
      BookstoreUtil.removeBookFromStorage(bookIsbn);
      
      listBookContainer.updateListBookComponent(BookstoreUtil.getAllBooksFromStorage());
      event.getRequestContext().addUIComponentToUpdateByAjax(listBookContainer);
    }
  }
  
}
