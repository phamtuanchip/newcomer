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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.exception.NoBookFoundException;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.specification.BookIsbnMatches;
import org.exoplatform.bookstore.specification.BookSpecification;
import org.exoplatform.bookstore.specification.BookTitleMatches;
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

  public static void addAuthorToStorage(Author author) throws Exception
  {
    getBookStorage().addAuthor(author);
  }
  
  public static Set<Book> getAllBooksFromStorage() throws Exception
  {
    return getBookStorage().getAllBooks();
  }
  
  public static Set<Author> getAllAuthorFromStorage() throws Exception
  {
    return getBookStorage().getAllAuthors();
  }
  
  public static List<Book> searchBookWithTitleLike(String wordToSearch) throws Exception
  {
    return getBookStorage().searchBookWithTitleLike(wordToSearch);
  }
  
  public static Set<Book> searchBookByAuthorName(String authorName, Integer resultLimit) throws Exception
  {
    return getBookStorage().searchBookByAuthorName(authorName, resultLimit);
  }
  
  public static Set<Book> searchBookBySpecification(BookSpecification spec, Integer resultLimit) throws Exception
  {
    return getBookStorage().searchBookBySpecification(spec, resultLimit);
  }
  
  public static List<Book> convertToList(Set<Book> books)
  {
    List<Book> listBooks = new ArrayList<Book>();
    
    java.util.Iterator<Book> it = books.iterator();
    while(it.hasNext())
      listBooks.add(it.next());
    
    return listBooks;
  }
  
  public static Set<Book> searchBookByAllProperties(String wordToSearch) throws Exception
  {
    return getBookStorage().searchBookByAllProperties(wordToSearch);
  }
  
  public static void updateBookToStorage(Book bookToUpdate) throws Exception
  {
    getBookStorage().updateBook(bookToUpdate);
  }
}
