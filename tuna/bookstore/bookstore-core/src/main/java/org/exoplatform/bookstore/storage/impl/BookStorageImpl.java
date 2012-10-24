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
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.domain.NoAuthor;
import org.exoplatform.bookstore.exception.DuplicateAuthorException;
import org.exoplatform.bookstore.exception.DuplicateBookException;
import org.exoplatform.bookstore.exception.NoAuthorFoundException;
import org.exoplatform.bookstore.exception.NoBookFoundException;
import org.exoplatform.bookstore.service.ComponentLocator;
import org.exoplatform.bookstore.specification.AuthorIdMatches;
import org.exoplatform.bookstore.specification.BookSpecification;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.services.jcr.impl.core.query.QueryImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
  }
  
  
  public BookStorage insertBook(Book bookToInsert) throws DuplicateBookException, Exception
  {
    log.info("--- insert Book ---");
    
    log.info("Book: " + bookToInsert.getIsbn() 
             + " -- " + bookToInsert.getTitle());
    Author author = getAuthorByName(bookToInsert.getAuthor().getName());
    if (hasBook(bookToInsert.getIsbn())) throw 
      new DuplicateBookException("Duplicate book " + bookToInsert.getIsbn());
    
    Node booksNode = rootNode.getNode("exo:bookstore/exo:books");

    Node bookNode  = booksNode.addNode("exo:book");
    bookNode.setProperty("exo:isbn"  , bookToInsert.getIsbn());
    bookNode.setProperty("exo:title" , bookToInsert.getTitle());  
    bookNode.setProperty("exo:author", author.getId() );
    
    session.save();  
    log.info("--- insert Book: OK ---");
    return this;
  }

  
  public BookStorage addAuthor(Author authorToAdd) throws DuplicateAuthorException, Exception
  {
    log.info("--- add Author ----");
    
    log.info("Author " + authorToAdd.getName());
    
    if (hasAuthor(authorToAdd.getName())) throw
      new DuplicateAuthorException("Duplicate author " + authorToAdd.getName());
    
    Node authorsNode = rootNode.getNode("exo:bookstore/exo:authors");
    Node authorNode  = authorsNode.addNode("exo:author"); 
    authorNode.addMixin("mix:referenceable");
    authorNode.setProperty("exo:authorname", authorToAdd.getName());
      
    authorToAdd.setId(authorNode.getUUID().toString());  
    
    log.info("Author id " + authorNode.getUUID().toString());
    
    session.save();
    log.info("--- add Author: OK ----");
    return this;
  }
  
  public Book getBookByIsbn(String isbn) throws Exception
  {
    log.info("--- get book by isbn ---");
    
    log.info("Book isbn " + isbn);
    
    String searchBookIsbnSQLQuery =
        "SELECT * FROM nt:base WHERE exo:isbn = '" + isbn + "'";
    
    Query query = ComponentLocator.getQueryManager()
        .createQuery( searchBookIsbnSQLQuery, Query.SQL) ;
    
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
  
  public BookStorage updateBook(Book bookToUpdate) throws Exception
  {
    log.info("--- update book ---");
   
    Author bookAuthor = getAuthorByName(bookToUpdate.getAuthor().getName());
    
    String searchBookIsbnSQLQuery =
        "SELECT * FROM nt:base WHERE exo:isbn = '" + bookToUpdate.getIsbn() + "'";
   
    Query query = ComponentLocator.getQueryManager()
        .createQuery( searchBookIsbnSQLQuery, Query.SQL) ;
   
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    
    if (!iterator.hasNext()) throw
        new NoBookFoundException("No book found with isbn " + bookToUpdate.getIsbn());
    
    Node bookNode = (Node) iterator.next(); 
    bookNode.setProperty("exo:title", bookToUpdate.getTitle());
    bookNode.setProperty("exo:author", bookAuthor.getId());
    session.save();
    log.info("--- update book: OK ---");
    return this;
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
  
  private Author restoreAuthorFromNode(Node authorNode) throws Exception
  {
    log.info("--- restore author from node ---");
    
    Author author = new Author();
    if (authorNode.getProperty("jcr:uuid").getString().equals(NoAuthor.id))
      return new NoAuthor(); 
    author.setId(authorNode.getProperty("jcr:uuid").getString());
    author.setName(authorNode.getProperty("exo:authorname").getString());
    
    return author;
  }
  
  
  public boolean hasBook(String isbn) throws Exception  
  {
    log.info("--- has book with isbn ---");
    
    String searchBookIsbnSQLQuery =
        "SELECT * FROM nt:base WHERE exo:isbn = '" + isbn + "'";
    
    Query query = ComponentLocator.getQueryManager()
        .createQuery( searchBookIsbnSQLQuery, Query.SQL) ;
    
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
    
    Query query = ComponentLocator.getQueryManager()
        .createQuery( searchBookIsbnSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) return true;
    
    return false;
  }

  public Author getAuthorByName(String authorName) throws Exception 
  {
    log.info("--- get author by name ---");
    
    log.info("Author name " + authorName);
    
    String searchAuthorSQLQuery =
        "SELECT * FROM nt:base WHERE exo:authorname = '" + authorName + "'";
    
    Query query = ComponentLocator.getQueryManager()
        .createQuery( searchAuthorSQLQuery, Query.SQL ) ;
    
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) {
      Node node = iterator.nextNode();
      
      if (node.getProperty("exo:authorname").getString().equals("No Author"))
        return new NoAuthor();
      
      log.info("--- get author by name: OK ---");
      return new Author(node.getProperty("exo:authorname").getString(),
                        node.getUUID().toString());
    }
    
    log.info("--- get author by name: OK ---");
    return new NoAuthor();  
  }
  
  public BookStorage removeBook(String isbn) throws Exception 
  {
    log.info("--- remove book ---");
    
    String selectBookSQLQuery =
        "SELECT * FROM nt:base WHERE exo:isbn = '" + isbn + "'";
    
    Query query = ComponentLocator.getQueryManager()
        .createQuery( selectBookSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    if (iterator.hasNext()) 
    {
      Node node = iterator.nextNode();
      node.remove();
      
      session.save();
      log.info("--- remove book: OK ---");
      return this;
    }
    
    return this;
  }
  

  public Set<Book> getAllBooks() throws Exception
  {
    log.info("--- get all books ---");
    
    Node booksNode = rootNode.getNode("exo:bookstore/exo:books") ;
    NodeIterator iterator = booksNode.getNodes();
    
    log.info(" result size: " + booksNode.getNodes().getSize());
    
    Set<Book> allBooks = new HashSet<Book>();
    if (!iterator.hasNext()) throw new NoBookFoundException("No book found");
    while (iterator.hasNext()) allBooks.add(restoreBookFromNode(iterator.nextNode()));
    
    return allBooks;
  }

  public Set<Author> getAllAuthors() throws Exception
  {
    log.info("--- get all authors ---");
    
    Node booksNode = rootNode.getNode("exo:bookstore/exo:authors") ;
    NodeIterator iterator = booksNode.getNodes();
    
    log.info(" result size: " + booksNode.getNodes().getSize());
    
    Set<Author> allAuthors = new HashSet<Author>();
    if (!iterator.hasNext()) throw new NoAuthorFoundException("No author found");
    while (iterator.hasNext()) allAuthors.add(restoreAuthorFromNode(iterator.nextNode()));
    
    return allAuthors;
  }
  
  public List<Book> getBooksFromAuthor(Author anAuthor) throws Exception
  {
    log.info("--- get books from author ---");
    
    log.info("Author " + anAuthor.getName());
      
    String selectBookSQLQuery =
        "SELECT * FROM nt:base WHERE exo:author = '" + anAuthor.getId() + "'";
    
    Query query = ComponentLocator.getQueryManager()
        .createQuery( selectBookSQLQuery, Query.SQL) ;
    
    QueryResult result = query.execute();
    NodeIterator iterator = result.getNodes();
    
    log.info(" result size: " + result.getNodes().getSize());
    
    List<Book> books = new ArrayList<Book>();
    if (!iterator.hasNext()) throw new NoBookFoundException("No book found from author " + anAuthor);
    
    while (iterator.hasNext()) books.add(restoreBookFromNode(iterator.nextNode()));
    
    return books;   
  }
  
  
  public void removeAllBooks() throws Exception
  {
    log.info("--- remove all books ---");
    
    Set<Book> allBooks = getAllBooks();
    if (allBooks == null) return;
    
    Iterator<Book> it = allBooks.iterator();
    while (it.hasNext()) removeBook(it.next().getIsbn());    
  }
  
  
  public List<Book> searchBookWithTitleLike(String bookTitle) throws Exception
  {
    log.info("--- search book with title like ---");
    
    String selectBookSQLQuery =
      "SELECT * FROM nt:base WHERE exo:title LIKE '%" + bookTitle + "%'";
    
    Query query = ComponentLocator.getQueryManager()
        .createQuery( selectBookSQLQuery, Query.SQL) ;
     
    QueryResult result = query.execute();
    NodeIterator iterator = result.getNodes();
    
    log.info(" result size: " + result.getNodes().getSize());
    
    List<Book> books = new ArrayList<Book>();
    if (!iterator.hasNext()) throw new NoBookFoundException("No book found with name like " + bookTitle); 
    
    while (iterator.hasNext()) 
      books.add(restoreBookFromNode(iterator.nextNode()));
    
    return books;   
  }

  
  public Set<Book> searchBookBySpecification(BookSpecification spec, Integer resultLimit) throws Exception
  {
    log.info("--- search book by specification ---");
    
    QueryImpl query = (QueryImpl) ComponentLocator.getQueryManager()
        .createQuery(spec.getQuery(), Query.SQL);
    
    if (resultLimit != null) query.setLimit(resultLimit);
    QueryResult result = query.execute();
    NodeIterator iterator = result.getNodes();
    
    log.info(" result size: " + result.getNodes().getSize());
    
    Set<Book> books = new HashSet<Book>();
    if (!iterator.hasNext()) throw new NoBookFoundException("No book found"); 
    
    while (iterator.hasNext()) 
      books.add(restoreBookFromNode(iterator.nextNode()));
    
    return books; 
  }
  
  public Set<Author> getAuthorWithNameLike(String authorName, Integer resultLimit) throws Exception 
  {
    log.info("--- get author with name like ---");
    
    log.info("Author name " + authorName);
    
    String searchAuthorSQLQuery =
        "SELECT * FROM nt:base WHERE exo:authorname LIKE '%" + authorName + "%'";
    
    QueryImpl query = (QueryImpl) ComponentLocator.getQueryManager()
        .createQuery( searchAuthorSQLQuery, Query.SQL ) ;
    if (resultLimit != null) query.setLimit(resultLimit);
    
    QueryResult result = query.execute();    
    NodeIterator iterator = result.getNodes();
    log.info(" result size: " + result.getNodes().getSize());
    
    Set<Author> authors = new HashSet<Author>();
    
    while (iterator.hasNext()) 
    {
      Node node = iterator.nextNode();
      if (node.getProperty("exo:authorname").getString().equals("No Author"))
         authors.add(new NoAuthor());
      
      authors.add( new Author(node.getProperty("exo:authorname").getString(),
                       node.getUUID().toString()));
    }
    
    return authors;  
  }
  
  public Set<Book> searchBookByAuthorName(String authorName, Integer resultLimit) throws Exception
  {
    log.info("--- search book by authorname ---");
    
    Set<Book> books = new HashSet<Book>();
    
    Set<Author> authors = getAuthorWithNameLike(authorName, null);
    Iterator<Author> it = authors.iterator();
    while (it.hasNext())
    {
      Author author = it.next();
      books.addAll(searchBookBySpecification(new AuthorIdMatches(author.getId()), null));
    }  
    
    return books;  
  }
}
