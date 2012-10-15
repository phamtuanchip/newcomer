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

import java.io.Writer;
import java.util.List;

import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.bookstore.util.BookstoreUtil;
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
    initApplication();   
    updateListBookComponent();
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
  
  private void initApplication() 
  {
    ComponentLocator.setContainer(ExoContainerContext.getCurrentContainer());
    ComponentLocator.emptyDefaultNodes();
    ComponentLocator.initDefaultNodes();
    ComponentLocator.initBookstore();
  }
  
  
  public void updateListBookComponent() throws Exception 
  {
    log.info("--- update Grid ---");
    
    BookStorage bookStorage = (BookStorage) ComponentLocator.getContainer()
        .getComponentInstanceOfType(BookStorage.class);
    List<Book> allBooks = bookStorage.getAllBooks();
    
    // update UI list of books
    UIGrid listBooksComponent = getChild(UIGrid.class) ; 
    LazyPageList<Book> pageList = new LazyPageList<Book>(new ListAccessImpl<Book>(Book.class, allBooks), 10);
    listBooksComponent.getUIPageIterator().setPageList(pageList) ;  
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
            
      listBookContainer.updateListBookComponent();
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
      
      listBookContainer.updateListBookComponent();
      event.getRequestContext().addUIComponentToUpdateByAjax(listBookContainer);
    }
  }
  
}
