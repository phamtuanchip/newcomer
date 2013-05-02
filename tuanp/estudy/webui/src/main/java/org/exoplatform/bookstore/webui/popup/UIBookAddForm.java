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
package org.exoplatform.bookstore.webui.popup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.exception.DuplicateBookException;
import org.exoplatform.bookstore.portlet.UIBookstorePortlet;
import org.exoplatform.bookstore.util.BookstoreUtil;
import org.exoplatform.bookstore.webui.UIBookListManager;
import org.exoplatform.bookstore.webui.UIBookMessage;
import org.exoplatform.bookstore.webui.action.UIPopupAction;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 18, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template  = "system:/groovy/webui/form/UIForm.gtmpl",
  events    = {
    @EventConfig(listeners = UIBookAddForm.AddBookActionListener.class),
    @EventConfig(listeners = UIBookAddForm.OpenAddAuthorPopupActionListener.class)
  }
)

public class UIBookAddForm extends UIForm
{
  private static final Log log = ExoLogger.getExoLogger(UIBookAddForm.class);
  
  private static final String ADD_BOOK_ISBN   = "bookIsbn";
  
  private static final String ADD_BOOK_TITLE  = "bookTitle";
    
  private static final String MESSAGE         = "message";
  
  private static final String AUTHORS_OPTION  = "authorsOptions";
  
  public UIBookAddForm() throws Exception
  {
    log.info("--- UIBookAddForm constructor ---");
    
    addUIFormInput(new UIFormStringInput(ADD_BOOK_ISBN  , ADD_BOOK_ISBN  , null ));
    addUIFormInput(new UIFormStringInput(ADD_BOOK_TITLE , ADD_BOOK_TITLE , null ));
    
    addSelectBox();
    addMessageBox();
  }
  
  private void addMessageBox()
  {
    UIBookMessage messageBox = new UIBookMessage(MESSAGE, MESSAGE, null);
    messageBox.setValue("This is a message");
    messageBox.setRendered(false);
    addChild(messageBox);
  }
  
  private void addSelectBox() throws Exception
  {
    addChild(new UIFormSelectBox(AUTHORS_OPTION, AUTHORS_OPTION, getAuthorsOptions()));
  }
  
  public void updateUIAuthorList() throws Exception
  { 
    getChild(UIFormSelectBox.class).setOptions(getAuthorsOptions());
  }
  
  private List<SelectItemOption<String>> getAuthorsOptions() throws Exception
  {
    List<SelectItemOption<String>> authorsOptions = new ArrayList<SelectItemOption<String>>();
    
    Set<Author> allAuthors = BookstoreUtil.getAllAuthorFromStorage();
    Iterator<Author> it = allAuthors.iterator();
    while (it.hasNext())
    {  
      Author author = it.next();
      authorsOptions.add( new SelectItemOption<String>(author.getName(), author.getName()) );
    }
    
    return authorsOptions;
  }
  
  public String getSelectedValue()
  {
    return getChild(UIFormSelectBox.class).getSelectedValues()[0];
  }
  
  public String getBookIsbnFromUI() 
  {
    return getUIStringInput(ADD_BOOK_ISBN).getValue();
  }
  
  public String getBookTitleFromUI() 
  {
    return getUIStringInput(ADD_BOOK_TITLE).getValue();
  }
  
  public void showMessageBoxWith(String messageToDisplay)
  {
    getChild(UIBookMessage.class).setValue(messageToDisplay);
    getChild(UIBookMessage.class).setRendered(true);
  }
  
  public void resetForm()
  {
    getUIStringInput(ADD_BOOK_ISBN).reset();
    getUIStringInput(ADD_BOOK_TITLE).reset();
  }
  
  static public class AddBookActionListener extends EventListener<UIBookAddForm>
  {

    @Override
    public void execute(Event<UIBookAddForm> event) throws Exception 
    {
      log.info("--- Add Book Event received ---");   
      
      UIBookAddForm bookAddForm = event.getSource();
      
      try
      {
        Author author = BookstoreUtil.getAuthorFromStorage(bookAddForm.getSelectedValue());
        BookstoreUtil.insertBookToStorage(
            new Book(bookAddForm.getBookIsbnFromUI(), bookAddForm.getBookTitleFromUI(),
                     author)
          );
        
        UIBookListManager bookListComponent = bookAddForm
            .getAncestorOfType(UIBookstorePortlet.class)
            .findFirstComponentOfType(UIBookListManager.class);
        bookListComponent.updateListBookComponent(BookstoreUtil.getAllBooksFromStorage());
        event.getRequestContext().addUIComponentToUpdateByAjax(bookListComponent);        
      }
      catch (DuplicateBookException e)
      {
        bookAddForm.showMessageBoxWith("Book with Isbn " + bookAddForm.getBookIsbnFromUI() +
            " already exists, add a new one");
        event.getRequestContext().addUIComponentToUpdateByAjax(bookAddForm);
        return;
      }
      catch (Exception e)
      {
        log.error("Add book exception " + e.getMessage());
      }
      
      bookAddForm.showMessageBoxWith("Book with Isbn " + bookAddForm.getBookIsbnFromUI() +
          " added successfully");
      bookAddForm.resetForm();
      event.getRequestContext().addUIComponentToUpdateByAjax(bookAddForm);
    }
  }
  
  static public class OpenAddAuthorPopupActionListener extends EventListener<UIBookAddForm>
  {
    @Override
    public void execute(Event<UIBookAddForm> event) throws Exception {
      log.info("--- OpenAddAuthorPopup Event received ---");      
      
      UIPopupAction bookAddPopupManager = event.getSource().getAncestorOfType(UIBookAddManager.class)
          .getChild(UIPopupAction.class);
      bookAddPopupManager.activate(UIAuthorAddForm.class, 400);
      event.getRequestContext().addUIComponentToUpdateByAjax(bookAddPopupManager);      
    }  
  }
}
