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

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.exception.DuplicateBookException;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 11, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template  = "system:/groovy/webui/form/UIForm.gtmpl",
  events    = 
    @EventConfig(listeners = UIBookListForm.SaveActionListener.class)
)

public class UIBookListForm extends UIForm 
{
  private static final Log log = ExoLogger.getExoLogger(UIBookListForm.class);
  
  private static String BOOK_ISBN   = "bookIsbn";
  
  private static String BOOK_TITLE  = "bookTitle";
  
  private static String BOOK_AUTHOR = "bookAuthor"; 
  
  private static boolean isAddNew = true;
    
  public UIBookListForm() throws Exception 
  {
    addUIFormInput(new UIFormStringInput(BOOK_ISBN, BOOK_ISBN, null));
    addUIFormInput(new UIFormStringInput(BOOK_TITLE, BOOK_TITLE, null));
    addUIFormInput(new UIFormStringInput(BOOK_AUTHOR, BOOK_AUTHOR, null));
  }
  
  protected String getBookIsbn() 
  {
    return getUIStringInput(BOOK_ISBN).getValue();
  }
  
  protected void setBookIsbn(String isbn)
  {
    getUIStringInput(BOOK_ISBN).setValue(isbn);
  }
  
  protected String getBookTitle() 
  { 
    return getUIStringInput(BOOK_TITLE).getValue();  
  }
   
  protected void setBookTitle(String title)
  {
    getUIStringInput(BOOK_TITLE).setValue(title);
  }
  
  protected void setAuthorName(String authorName)
  {
    getUIStringInput(BOOK_AUTHOR).setValue(authorName);
  }
  
  protected String getAuthorName()
  {
    return getUIStringInput(BOOK_AUTHOR).getValue();
  }
  
  public static class SaveActionListener extends EventListener<UIBookListForm>
  {
    public void execute(Event<UIBookListForm> event) throws Exception
    {
      log.info("--- save action execute ---");
      
      UIBookListForm uiBookList = event.getSource();
            
      BookStorage bookStorage = (BookStorage) ComponentLocator.getContainer()
          .getComponentInstanceOfType(BookStorage.class);
      
      try
      {   
        Author anAuthor = (Author) bookStorage.getAuthorByName(uiBookList.getAuthorName());
        
        bookStorage.insertBook(new Book(
          uiBookList.getBookIsbn(),
          uiBookList.getBookTitle(),
          anAuthor
        ));
      }
      catch (DuplicateBookException e)
      {
        // show popup if duplicate book found
        event.getRequestContext().getUIApplication().addMessage(
          new ApplicationMessage("UIBookListForm.msg.duplicate-book", null)
        );
      }
      
      // makes call to UIBookListManager
      // 1> update content of grid
      ((UIBookListManager) uiBookList.getParent()).updateGrid();
      // 2> update UI by ajax
      event.getRequestContext().addUIComponentToUpdateByAjax(uiBookList.getParent());
    }
    
  }
}
