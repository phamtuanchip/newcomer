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
package org.exoplatform.bookstore.storage;

import java.util.List;

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;


/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 2, 2012  
 */

public interface BookStorage {
  
  public void insertBook(Book bookToInsert) throws Exception;
    
  public Author addAuthor(Author authorToAdd) throws Exception;
  
  public Book getBookByIsbn(String isbn) throws Exception;
  
  public Author getAuthorByName(String authorName) throws Exception;
  
  public boolean hasBook(String isbn) throws Exception;
  
  public boolean hasAuthor(String authorName) throws Exception;
  
  public void removeBook(String isbn) throws Exception;
  
  public List<Book> getAllBooks() throws Exception;
  
  public List<Book> getBooksFromAuthor(Author anAuthor) throws Exception;
}
