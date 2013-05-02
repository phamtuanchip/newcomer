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
import org.exoplatform.bookstore.exception.NoBookFoundException;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.bookstore.util.BookstoreUtil;
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
import org.exoplatform.webui.form.validator.SpecialCharacterValidator;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 11, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template  = "app:/groovy/bookstore/webui/UIForm.gtmpl",
  events    = 
    @EventConfig(listeners = UIBookEditForm.SaveActionListener.class)
)

public class UIBookEditForm extends UIForm 
{
  private static final Log log = ExoLogger.getExoLogger(UIBookEditForm.class);
  
  private static String EDIT_BOOK_ISBN   = "bookIsbn";
  
  private static String EDIT_BOOK_TITLE  = "bookTitle";
  
  private static String EDIT_BOOK_AUTHOR = "bookAuthor"; 
      
  public UIBookEditForm() throws Exception 
  {
    addUIFormInput(new UIFormStringInput(EDIT_BOOK_ISBN, EDIT_BOOK_ISBN, null));
    getUIStringInput(EDIT_BOOK_ISBN).setReadOnly(true);
        //.addValidator(SpecialCharacterValidator.class));    
    
    addUIFormInput(new UIFormStringInput(EDIT_BOOK_TITLE, EDIT_BOOK_TITLE, null)
        .addValidator(SpecialCharacterValidator.class));
    
    addUIFormInput(new UIFormStringInput(EDIT_BOOK_AUTHOR, EDIT_BOOK_AUTHOR, null)
        .addValidator(SpecialCharacterValidator.class));    
  }
  
  protected String getBookIsbnFromUI() 
  {
    return getUIStringInput(EDIT_BOOK_ISBN).getValue();
  }
  
  protected void setBookIsbnInUI(String isbn)
  {
    getUIStringInput(EDIT_BOOK_ISBN).setValue(isbn);
  }
  
  protected String getBookTitleFromUI() 
  { 
    return getUIStringInput(EDIT_BOOK_TITLE).getValue();  
  }
   
  protected void setBookTitleInUI(String title)
  {
    getUIStringInput(EDIT_BOOK_TITLE).setValue(title);
  }
  
  protected void setAuthorNameInUI(String authorName)
  {
    getUIStringInput(EDIT_BOOK_AUTHOR).setValue(authorName);
  }
  
  protected String getAuthorNameFromUI()
  {
    return getUIStringInput(EDIT_BOOK_AUTHOR).getValue();
  }
  
  protected void updateUI(Book bookToEdit)
  {
    setBookIsbnInUI   (bookToEdit.getIsbn());
    setBookTitleInUI  (bookToEdit.getTitle());
    setAuthorNameInUI (bookToEdit.getAuthor().getName());
  }
  
  public static class SaveActionListener extends EventListener<UIBookEditForm>
  {
    public void execute(Event<UIBookEditForm> event) throws Exception
    {
      log.info("--- save action execute ---");
      
      UIBookEditForm editForm = event.getSource();
            
      try
      {   
        //Author anAuthor = BookstoreUtil.getAuthorFromStorage(editForm.getAuthorNameFromUI());     
        BookstoreUtil.updateBookToStorage(
            new Book(editForm.getBookIsbnFromUI(),
                     editForm.getBookTitleFromUI(),
                     new Author(editForm.getAuthorNameFromUI())  ));
      }
      catch (NoBookFoundException e)
      {
        // show popup if duplicate book found
        event.getRequestContext().getUIApplication().addMessage(
          new ApplicationMessage("UIBookEditForm.msg.duplicate-book", null)
        );
      }
      
      ((UIBookListManager) editForm.getParent())
          .updateListBookComponent(BookstoreUtil.getAllBooksFromStorage());
      event.getRequestContext().addUIComponentToUpdateByAjax(editForm.getParent());
    }
    
  }
}
