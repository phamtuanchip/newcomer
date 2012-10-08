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
package org.exoplatform.bookstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.exoplatform.bookstore.common.BookstoreConstant;
import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.exception.DuplicateBookException;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.bookstore.storage.impl.BookStorageImpl;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 2, 2012  
 */
public class BookStorageTest extends TestCase 
{
  
  private static BookStorage bookStorage;
  
  private static Log log = (Log) ExoLogger.getLogger(BookStorageTest.class);
  
  private static ExoContainer eContainer;
  
  static 
  {
    initContainer();
  }
  
  private static void initContainer() 
  {
    String containerConfig = ComponentLocator.class.getResource(
      BookstoreConstant.CONTAINER_CONFIGURATION).toString();    
    
    try 
    {
      StandaloneContainer.addConfigurationURL(containerConfig);
      eContainer = StandaloneContainer.getInstance();
      ComponentLocator.setContainer(eContainer);
      
      String loginConfig = BookStorageTest.class.getResource(
                             BookstoreConstant.LOGIN_CONFIGURATION).toString();   
                             System.setProperty("java.security.auth.login.config", loginConfig);
    }
    catch (Exception e)
    {
      log.info("init container exception " + e.getMessage());
    }
  }
  
  
  public void testLoginToWorkSpace() throws Exception
  {
    log.info("--- test Login ---");
    
    RepositoryService repoSer = (RepositoryService) 
        eContainer.getComponentInstance(RepositoryService.class);
    Session session = repoSer.getRepository("repository").login
    (
     new SimpleCredentials( BookstoreConstant.USERNAME_TEST_REPOSITORY, 
                            BookstoreConstant.PASSWORD_TEST_REPOSITORY.toCharArray() ),
     BookstoreConstant.BOOKSTORE_TEST_WORKSPACE
    );
    
    log.info("user id   " + session.getUserID());
    log.info("workspace " + session.getWorkspace().getName());
    
    assertEquals(session.getUserID(), BookstoreConstant.USERNAME_TEST_REPOSITORY);
    assertEquals(session.getWorkspace().getName(), BookstoreConstant.BOOKSTORE_TEST_WORKSPACE);

    log.info("--- test Login successfully ---\n");
  }
  
  
  public void testGettingBookStorageComponent() 
  {
    log.info("--- test Getting Book Storage Component ---");
    
    bookStorage = (BookStorage) eContainer.getComponentInstance(BookStorage.class);
    assertNotNull(bookStorage);
    
    log.info("--- test Getting Book Storage Component successfully ---\n");
  }
 
  
  public void testInsertBook() {
    
    log.info("--- test Insert Book ----");
    
    try 
    {
      bookStorage.insertBook( new Book("9781451648539", "Steve Jobs", null) );
      assertTrue(bookStorage.hasBook("9781451648539"));
    }
    catch (Exception e) 
    {
      log.error("--- insert book exception ---" + e.getMessage());
    }
      
    log.info("--- test Insert Book successfully ----\n");
  }
  
  public void testAddAuthor() 
  {
    log.info("--- test Adding Author ---");
    
    try 
    {
      Author author = bookStorage.addAuthor( new Author("Walter Isaacson") );
    }
    catch(Exception e)
    {
      log.error("exception in adding author: " + e.getStackTrace());
    }
    
    log.info("--- test Adding Author successfully ---\n");
  }
  
  public void testAddBookWithAuthor() 
  {
    log.info("--- test Adding Book with Author ---");
    
    try 
    {
      Author author = bookStorage.addAuthor(new Author("Vu Trong Phung"));
      bookStorage.insertBook(
        new Book("9781451648888", "So Do", author)
      );
    }
    catch (Exception e) 
    {
      log.error("--- adding author exception ---" + e.getMessage());
    }
   
    log.info("--- test Adding Book with Author successfully ---\n");
  }
  
  
  public void testGetBookByIsbn() 
  {
    log.info("--- test Getting Book by Isbn ---");
    
    Book sodoBook = null;
    
    try 
    {
      sodoBook = bookStorage.getBookByIsbn("9781451648888");
    } 
    catch (Exception e) 
    {
      log.error(" finding book exception: " + e.getMessage());
    }
    
    assertEquals(sodoBook.getIsbn(), new String("9781451648888") );
    assertEquals(sodoBook.getAuthor().getName(), new String("Vu Trong Phung"));
    
    log.info("author name " + sodoBook.getAuthor().getName());
    log.info("author id   " + sodoBook.getAuthor().getId());
    log.info("--- test Getting Book by Isbn successfully ---\n");   
  }
  
  public void testHasBook(String isbn) 
  {
    log.info("--- test Having Already A Book With Isbn ---");
    
    try 
    {
      assertTrue(bookStorage.hasBook(new String("9781451648539")));
    } 
    catch (Exception e) 
    {
      log.error("exception: " + e.getMessage());
    }
    
    log.info("--- test Having Already A Book With Isbn successfully ---\n");
  }
  
  public void testAddingDuplicateBook()
  {
    log.info("--- test AddingDuplicateBook ---");

    try
    {
      bookStorage.insertBook(new Book("9781451648539", "Steve", null));
    }
    catch (DuplicateBookException e)
    {
      log.error("Dupplicate Book Exception: " + e.getMessage());
    }
    catch (Exception e)
    {
      log.error("exception: " + e.getMessage());
    }
    
    log.info("--- test AddingDuplicateBook successfully ---\n");
  }
  
  public void testAddingDuplicateAuthor()
  {
    log.info("--- test AddingDuplicateAuthor ---");

    try
    {
      bookStorage.addAuthor(new Author("Vu Trong Phung"));
    }
    catch (Exception e)
    {
      log.error("Dupplicate Author Exception: " + e.getMessage());
    }
    
    log.info("--- test AddingDuplicateAuthor successfully ---\n");
  }
  
  public void testGetAuthorByName() 
  {
    log.info("--- test Getting Author by Name ---");
    
    Author vuTrongPhung = null;
    Author noAuthor     = null;
    
    try 
    {
      vuTrongPhung = bookStorage.getAuthorByName("Vu Trong Phung");
      noAuthor     = bookStorage.getAuthorByName("ABCDEFGH");
    } 
    catch (Exception e) 
    {
      log.error(" finding author exception: " + e.getMessage());
    }
    
    assertEquals(vuTrongPhung.getName(), new String("Vu Trong Phung") );
    assertEquals(noAuthor.getName(), new String("No Author") );
    
    log.info("Author    Id: " + vuTrongPhung.getId());
    log.info("No Author Id: " + noAuthor.getId());

    log.info("--- test Getting Author by Name successfully ---\n");   
  }
  
  
  public void testRemoveBook() 
  {
    log.info("--- test Removing Book ---");
    
    Book doraemon = new Book("1234567890123", "Doraemon", null);
    
    try 
    {
      bookStorage.insertBook(doraemon);
      bookStorage.removeBook(doraemon.getIsbn());
      assertFalse(bookStorage.hasBook(doraemon.getIsbn()));
    } 
    catch (Exception e) 
    {
      log.error("remove book exception " + e.getMessage());
    }
    
    log.info("--- test Removing Book successfully ---\n");
  }
  
  public void testGetAllBook()
  {
    log.info("--- test Getting All Books ---");
    
    List<Book> allBooks = new ArrayList<Book>();
    
    try
    {
      allBooks = bookStorage.getAllBooks();
      assertFalse(allBooks.isEmpty());
    }
    catch (Exception e)
    {
      log.error("get all book exception " + e.getMessage());
    }
    
    Iterator<Book> it = (Iterator<Book>) allBooks.iterator();
    while (it.hasNext()) 
    {
      Book book = (Book) it.next();
      log.info("book isbn  : " + book.getIsbn());
      log.info("book title : " + book.getTitle());
      log.info("book author: " + book.getAuthor().getName());
    }
    
    log.info("--- test Getting Book successfully ---\n");
  }
  
  public void testGetBooksFromAuthor() 
  {
    log.info("--- test Getting Books From Author ---");
    
    Author hoChiMinh = new Author("Ho Chi Minh");
    
    try 
    {
      bookStorage.addAuthor(hoChiMinh);
      bookStorage.insertBook(new Book("1234567800000", "Nhat Ki Trong Tu", hoChiMinh));
      bookStorage.insertBook(new Book("1234567800001", "Tuyen Ngon Doc Lap", hoChiMinh));
      List<Book> hoChiMinhBooks = bookStorage.getBooksFromAuthor(hoChiMinh);
      
      Iterator<Book> it = hoChiMinhBooks.iterator();
      while (it.hasNext()) 
        log.info("Book title " + ((Book) it.next()).getTitle());
      
      assertEquals(new Integer(hoChiMinhBooks.size()), new Integer(2));
    }
    catch (Exception e)
    {
      log.error("getting book from author exception " + e.getMessage());
    }
  
    log.info("--- test Getting Books From Author successfully ---\n");
  }
}
