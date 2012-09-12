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
package exoplatform;

import java.util.List;

import javax.jcr.Node;

import exoplatform.entity.Author;
import exoplatform.entity.Book;
import exoplatform.entity.Book.CATEGORY;
import exoplatform.exception.BookNotFoundException;
import exoplatform.exception.DuplicateBookException;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 19, 2012  
 */
public interface BookStoreService {

  /**
   * search book by book name
   * 
   * @param bookName The name of book
   * @return List<Book> The list of book
   */
  public List<Book> searchBookByName(String bookName);
  
  /**
   * search book by author name
   * 
   * @param authorName The name of author
   * @return List<Book> The list of book
   */
  public List<Book> searchBookByAuthor(String authorName);
  
  /**
   * add new book to datastorage
   * 
   * @param bookName
   * @param category
   * @param content
   * @return
   * @throws DuplicateBookException
   */
  public Node addBook(Book book, String authorId) throws DuplicateBookException;
  
  /**
   * add new author to datastorage
   * 
   * @param authorName
   * @param authorAddress
   * @param authorPhone
   * @return
   */
  public Node addAuthor(String authorName, String authorAddress, String authorPhone) throws DuplicateBookException;
  
  /**
   * add new user to datastorage
   * 
   * @param username
   * @param password
   * @param fullname
   * @param address
   * @param phone
   * @param nodes
   * @return
   * @throws DuplicateBookException
   */
  public Node addUser(String username,
                      String password,
                      String fullname,
                      String address,
                      String phone) throws DuplicateBookException;
  
  /**
   * delete a author by authorName
   * 
   * @param authorName The name of author
   * @throws BookNotFoundException
   */
  public void deleteAuthor(String authorId) throws BookNotFoundException;
  
  /**
   * delete a book by book id
   * 
   * @param id The id of book
   * @throws BookNotFoundException
   */
  public void deleteBook(String id) throws BookNotFoundException;
  
  /**
   * edit a exist book
   * 
   * @param book The book want to delete
   * @throws BookNotFoundException
   */
  public void editAuthor(Author author) throws BookNotFoundException;
  
  public Node addUserReference(String userId, String bookId) throws DuplicateBookException;
  
  /**
   * get all book exist in datastore
   */
  public List<Book> getAllBook();
  
  /**
   * get author by book id
   * 
   * @param id
   * @return
   */
  public Author getAuthorByBookId(String bookId);
  
  /**
   * get book by book id
   * 
   * @param id
   * @return
   */
  public Book getBook(String id);
  
  /**
   * edit a exist book
   * 
   * @param book The book want to delete
   * @throws BookNotFoundException
   */
  public void editBook(Book book) throws BookNotFoundException;
  
  /**
   * add new book without to workspace
   * 
   * @param book The new book which want to add
   * @return Book
   * @throws DuplicateBookException
   */
  public Book addBookWithout(Book book) throws DuplicateBookException;
  
}
