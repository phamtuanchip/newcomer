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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.estudy.learning.domain.Book;
import org.estudy.learning.exception.NoBookFoundException;
import org.estudy.learning.specification.BookIsbnMatches;
import org.estudy.learning.specification.BookTitleMatches;
import org.estudy.ui.portlet.UIBookstorePortlet;
import org.estudy.ui.util.BookstoreUtil;
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
import org.exoplatform.webui.form.validator.SpecialCharacterValidator;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 4, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template  = "app:/groovy/bookstore/webui/UIBookSearchForm.gtmpl",
  events = {
      @EventConfig(listeners = UIBookSearchForm.SearchActionListener.class)
  }
)
public class UIBookSearchForm extends UIForm {

  public static Log log = ExoLogger.getExoLogger(UIBookSearchForm.class);
  
  private static final String FIELD_SEARCHVALUE = "value";
  
  private static final String MESSAGE           = "message";
  
  private static final String SEARCH_OPTION     = "searchOptions";
  
  public UIBookSearchForm() throws Exception
  {
    addChild(new UIFormStringInput(FIELD_SEARCHVALUE, FIELD_SEARCHVALUE, null)
        .addValidator(SpecialCharacterValidator.class));
    
    addMessageBox();
    addSelectBox();
  }
  
  public String getSearchValue() 
  {
    return getUIStringInput(FIELD_SEARCHVALUE).getValue();
  }
  
  private void addMessageBox()
  {
    UIBookMessage messageBox = new UIBookMessage(MESSAGE, MESSAGE, null);
    messageBox.setValue("This is a message");
    messageBox.setRendered(false);
    addChild(messageBox);
  }
  
  public void showMessageBoxWith(String messageToDisplay)
  {
    getChild(UIBookMessage.class).setValue(messageToDisplay);
    getChild(UIBookMessage.class).setRendered(true);
  }
  
  public void disableMessageBox()
  {
    getChild(UIBookMessage.class).setRendered(false);
  }
  
  private void addSelectBox()
  {
    List<SelectItemOption<String>> searchOptions = new ArrayList<SelectItemOption<String>>();
    searchOptions.add(new SelectItemOption<String>("title" , "bookTitle"));
    searchOptions.add(new SelectItemOption<String>("author", "bookAuthor"));
    searchOptions.add(new SelectItemOption<String>("isbn"  , "bookIsbn"));
    searchOptions.add(new SelectItemOption<String>("all"   , "all"));
    addChild(new UIFormSelectBox(SEARCH_OPTION, SEARCH_OPTION, searchOptions));
  }
  
  static public class SearchActionListener extends EventListener<UIBookSearchForm>
  {
    public void execute(Event<UIBookSearchForm> event) throws Exception
    {
      log.info("--- search action received ---");
      
      UIBookSearchForm searchBookComponent = event.getSource();
      String wordsToSearch = searchBookComponent.getSearchValue();
      searchBookComponent.disableMessageBox();
      event.getRequestContext().addUIComponentToUpdateByAjax(searchBookComponent);
      
      if (wordsToSearch == null) 
      {  
        searchBookComponent.showMessageBoxWith("No keyword to search, enter one");
        return ;
      }
      
      log.info("selected values: " + searchBookComponent.getChild(UIFormSelectBox.class).getSelectedValues()[0]);
      String searchChoice = searchBookComponent.getChild(UIFormSelectBox.class).getSelectedValues()[0];
      
      Set<Book> books = null;
      
      try 
      {
        if (searchChoice.equals("bookAuthor")) 
          books = BookstoreUtil.searchBookByAuthorName(wordsToSearch, null);
        if (searchChoice.equals("bookTitle"))
          books = BookstoreUtil.searchBookBySpecification(new BookTitleMatches(wordsToSearch), null);
        if (searchChoice.equals("bookIsbn"))
          books = BookstoreUtil.searchBookBySpecification(new BookIsbnMatches(wordsToSearch), null);
        if (searchChoice.equals("all")) 
        {
          books = BookstoreUtil.searchBookByAllProperties(wordsToSearch);
        }
        
        log.info("number of books found: " + String.valueOf(books.size()));
        
        UIBookListManager listBookContainer = 
            searchBookComponent.getAncestorOfType(UIBookstorePortlet.class)
            .findFirstComponentOfType(UIBookListManager.class);
        listBookContainer.updateListBookComponent(books);
        listBookContainer.disableEditBookComponent();
        listBookContainer.enableHomeButton();
        event.getRequestContext().addUIComponentToUpdateByAjax(listBookContainer);
      }
      catch (NoBookFoundException e)
      {
        if (searchChoice.equals("all"))
          searchBookComponent.showMessageBoxWith("No Book Found");
        else 
        searchBookComponent.showMessageBoxWith("No Book Found with " + searchChoice + " matches " + wordsToSearch);
      }
      catch (Exception e)
      {
        log.error("search book exception: " + e.getMessage());
      }
      
    }
    
  }
}
