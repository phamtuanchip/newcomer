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
package org.exoplatform.bookstore.util;

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.storage.BookStorage;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 5, 2012  
 */
public class BookstoreUtil {
  
  private static BookStorage getBookStorage() throws Exception
  {
    return (BookStorage) ComponentLocator.getContainer()
        .getComponentInstanceOfType(BookStorage.class);
  }
  
  public static Book getBookFromStorage(String bookIsbn) throws Exception
  {
    return getBookStorage().getBookByIsbn(bookIsbn);
  }  
  
  public static void removeBookFromStorage(String bookIsbn) throws Exception
  {
    getBookStorage().removeBook(bookIsbn);
  }
  
  public static Author getAuthorFromStorage(String authorName) throws Exception
  {
    return getBookStorage().getAuthorByName(authorName);
  }
  
  public static void insertBookToStorage(Book bookToInsert) throws Exception
  {
    getBookStorage().insertBook(bookToInsert);
  }
}
