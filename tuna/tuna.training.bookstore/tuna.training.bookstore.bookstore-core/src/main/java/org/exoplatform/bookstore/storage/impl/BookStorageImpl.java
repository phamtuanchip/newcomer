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
package org.exoplatform.bookstore.storage.impl;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.domain.NoAuthor;
import org.exoplatform.bookstore.exception.DuplicateAuthorException;
import org.exoplatform.bookstore.exception.DuplicateBookException;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 2, 2012  
 */

public class BookStorageImpl implements BookStorage 
{
  private static Log log = (Log) ExoLogger.getExoLogger(BookStorageImpl.class);
  
  private Session session;
  
  private Node rootNode;
  
  public BookStorageImpl()
  {
    log.info("--- init BookStorageImpl ---");
    
    session  = ComponentLocator.getSession();
    rootNode = ComponentLocator.getRootNode();  
    initDefaultNode();  
    
  }
  
  private void initDefaultNode() 
  {
    log.info("--- init default nodes ---");
    
    try 
    {
      Node bookstoreNode = rootNode.addNode("exo:bookstore");
      bookstoreNode.addNode("exo:books");
      Node authorsNode   = bookstoreNode.addNode("exo:authors");
      Node noAuthorNode  = authorsNode.addNode("exo:author");
      noAuthorNode.addMixin("mix:referenceable");
      noAuthorNode.setProperty("exo:authorname", "No Author");
      log.info("NoAuthor Id " + noAuthorNode.getUUID());
      NoAuthor.id = noAuthorNode.getUUID();
      
      session.save();
    }
    catch (Exception e) 
    {
      log.error("--- init default node exception ---" + e.getMessage());
    }
  }
  
  public void insertBook(Book bookToInsert) throws Exception
  {
    log.info("--- insert Book ---");
    
    if (hasBook(bookToInsert.getIsbn())) throw 
      new DuplicateBookException("Duplicate book " + bookToInsert.getIsbn());
    
    Node booksNode = rootNode.getNode("exo:bookstore/exo:books");

    Node bookNode  = booksNode.addNode("exo:book");
    bookNode.setProperty("exo:isbn"  , bookToInsert.getIsbn());
    bookNode.setProperty("exo:title" , bookToInsert.getTitle());  
    
    bookNode.setProperty("exo:author", bookToInsert.getAuthor().getId() );
    
    session.save();  
    log.info("--- insert Book: OK ---");
  }

  
  public Author addAuthor(Author authorToAdd) throws Exception
  {
    log.info("--- add author ----");
    
    if (hasAuthor(authorToAdd.getName())) throw
      new DuplicateAuthorException("Duplicate author " + authorToAdd.getName());
    
    Node authorsNode = rootNode.getNode("exo:bookstore/exo:authors");
    Node authorNode  = authorsNode.addNode("exo:author"); 
    authorNode.addMixin("mix:referenceable");
    authorNode.setProperty("exo:authorname", authorToAdd.getName());
      
    authorToAdd.setId(authorNode.getUUID().toString());  
    log.info("author node uuid " + authorNode.getUUID().toString());
    
    session.save();
    return authorToAdd;
  }
  
  public Book getBookByIsbn(String Isbn) throws Exception
  {
    log.info("--- get book by isbn ---");
    
    String searchBookIsbnSQLQuery =
        "SELECT * FROM nt:base WHERE exo:isbn = '" + Isbn + "'";
    
    Workspace ws = session.getWorkspace();
    QueryManager qm = ws.getQueryManager();
    Query query = qm.createQuery( searchBookIsbnSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) {
      log.info("--- get book by isbn: OK ---");  
      return restoreBookFromNode(iterator.nextNode());
    }
    
    log.info("--- get book by isbn: NO result ---");
    return null;
  }
  
  private Book restoreBookFromNode(Node bookNode) throws Exception
  {
    log.info("--- restore book from node ---");
    
    Author newAuthor = new Author();
    if (bookNode.getProperty("exo:author").getString().equals(NoAuthor.id))
      newAuthor = new NoAuthor(); 
    newAuthor.setId(bookNode.getProperty("exo:author").getString());
    Node authorNode = session.getNodeByUUID(bookNode.getProperty("exo:author").getString());
    
    newAuthor.setName(authorNode.getProperty("exo:authorname").getString());
    
    log.info("--- restore book from node: OK ---");
    return new Book(bookNode.getProperty("exo:isbn").getString(),
                    bookNode.getProperty("exo:title").getString(),
                    newAuthor );
  }
  
  
  public boolean hasBook(String isbn) throws Exception  
  {
    log.info("--- has book with isbn ---");

    
    String searchBookIsbnSQLQuery =
        "SELECT * FROM nt:base WHERE exo:isbn = '" + isbn + "'";
    
    Workspace ws = session.getWorkspace();
    QueryManager qm = ws.getQueryManager();
    Query query = qm.createQuery( searchBookIsbnSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) { 
      log.info("--- has book with isbn: HAS book ---");
      return true; 
    }
    
    log.info("--- has book with isbn: NO book ---");
    return false;
  }
  
  public boolean hasAuthor(String authorName) throws Exception
  {
    log.info("--- has book with author name ---");

    String searchBookIsbnSQLQuery =
        "SELECT * FROM nt:base WHERE exo:authorname = '" + authorName + "'";
    
    Workspace ws = session.getWorkspace();
    QueryManager qm = ws.getQueryManager();
    Query query = qm.createQuery( searchBookIsbnSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) return true;
    
    return false;
  }

  public Author getAuthorByName(String authorName) throws Exception 
  {
    log.info("--- get author by name ---");
    
    String searchAuthorSQLQuery =
        "SELECT * FROM nt:base WHERE exo:authorname = '" + authorName + "'";
    
    Workspace ws = session.getWorkspace();
    QueryManager qm = ws.getQueryManager();
    Query query = qm.createQuery( searchAuthorSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) {
      Node node = iterator.nextNode();
      
      if (node.getProperty("exo:authorname").getString().equals("No Author"))
        return new NoAuthor();
      
      return new Author(node.getProperty("exo:authorname").getString(),
                        node.getUUID().toString());
    }
    
    return new NoAuthor();  
  }
  
  public void removeBook(String isbn) throws Exception 
  {
    log.info("--- remove book ---");
    
    String selectBookSQLQuery =
        "SELECT * FROM nt:base WHERE exo:isbn = '" + isbn + "'";
    
    Workspace ws = session.getWorkspace();
    QueryManager qm = ws.getQueryManager();
    Query query = qm.createQuery( selectBookSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) 
    {
      Node node = iterator.nextNode();
      node.remove();
      
      session.save();
      log.info("--- remove book: OK ---");
    } 
  }
  
  
  public List<Book> getAllBooks() throws Exception
  {
    log.info("--- get all books ---");
    
    Node booksNode = rootNode.getNode("exo:bookstore/exo:books") ;
    NodeIterator iterator = booksNode.getNodes();
    
    log.info(" result size: " + booksNode.getNodes().getSize());
    
    List<Book> allBooks = new ArrayList<Book>();
    
    if (iterator.hasNext() == false) return null;
    
    while (iterator.hasNext()) 
      allBooks.add(restoreBookFromNode(iterator.nextNode()));
    
    return allBooks;
  }

}
