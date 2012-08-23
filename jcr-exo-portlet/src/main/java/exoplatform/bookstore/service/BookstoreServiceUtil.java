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
package exoplatform.bookstore.service;

import java.util.List;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;

import exoplatform.BookStoreService;
import exoplatform.entity.Book;
import exoplatform.exception.BookNotFoundException;
import exoplatform.exception.DuplicateBookException;



/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Aug 6, 2012  
 */
public class BookstoreServiceUtil {
  
  public static BookStoreService getBookstoreService() {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    return (BookStoreService) container.getComponentInstanceOfType(BookStoreService.class);
  }
  
  public static List<Book> getAllBook() {
    return getBookstoreService().getAllBook();
  }
  
  public void deleteBook(String bookId) throws BookNotFoundException {
    getBookstoreService().deleteBook(bookId);
  }
  
  public void getBookById(String bookId) {
    getBookstoreService().getBook(bookId);
  }
  
  public void editBook(Book book) throws BookNotFoundException {
    getBookstoreService().editBook(book);
  }
  
  public Book addBookWithout(Book book) throws DuplicateBookException {
    return getBookstoreService().addBookWithout(book);
  }
  
  public List<Book> getBookByName(String bookName) {
    return getBookstoreService().searchBookByName(bookName);
  }
  
}
