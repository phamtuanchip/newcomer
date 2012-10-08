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

import java.util.List;

import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.bookstore.storage.impl.BookStorageImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 4, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template = "app:/groovy/bookstore/webui/UIBookList.gtmpl"
)

public class UIBookList extends UIForm 
{
  private static Log log = ExoLogger.getExoLogger(UIBookList.class);
    
  public static List<Book> getAllBooks() 
  {
    log.info("--- get All Books ---");
    
    List<Book> allBooks = null;
    
    // should not use portal container here 
    // this code will reload another Root container then a portal container
    // because when we lauch the cs, it runs with csdemo portal container
    // so we just need to re-use it
    //PortalContainer pContainer = PortalContainer.getInstance();
    // this code produces exception
    // SEVERE: --- exception getting Container ---Two top level containers created, but must be only one.
    // they say two top level container created - it means another root container is created
    // but only once is used, so the newly created container is discarded
    // our pContainer will be null
    
    // what we suggests here is to use:
    
    ExoContainer eContainer = ExoContainerContext.getCurrentContainer();
    // to get the current container, which is csdemo container
    
    log.info("Container name " + ((PortalContainer) eContainer).getName());
    
    ComponentLocator.setContainer(eContainer);
    
    BookStorage bookStorage = (BookStorage) eContainer
        .getComponentInstance(BookStorage.class);
    
    if (bookStorage == null) log.info("book storage null");    
    
    try 
    {
      initBookstore(bookStorage);
      allBooks = bookStorage.getAllBooks();
    }
    catch(Exception e)
    {
      log.error("get all books exception " + e.getMessage());
    }
    
    return allBooks;
  }

  
  public static BookStorage initBookstore(BookStorage bookStorage)
  {
    log.info("--- initBookstore ---");
  
    try {
    
      Author martinFowler = new Author("Martin Fowler");
      Author ericEvans    = new Author("Eric Evans");
    
      bookStorage.addAuthor(martinFowler);
      bookStorage.addAuthor(ericEvans);
    
      bookStorage.insertBook(
        new Book("1201999914000", "Design Pattern", martinFowler)
      );
      bookStorage.insertBook(
        new Book("1201999914111", "Domain Driven Design", ericEvans)
      );
      bookStorage.insertBook(
        new Book("1201999914222", "Head First Java", null)
      );
    }
      catch (Exception e)
    {
      log.error("init bookstore exception " + e.getMessage());
    }
  
    return bookStorage;
  }
}
