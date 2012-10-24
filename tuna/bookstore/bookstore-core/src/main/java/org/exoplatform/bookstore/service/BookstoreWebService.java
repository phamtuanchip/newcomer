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
package org.exoplatform.bookstore.service;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.specification.BookIsbnMatches;
import org.exoplatform.bookstore.specification.BookTitleMatches;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 23, 2012  
 */
@Path("/bookstore")
public class BookstoreWebService implements ResourceContainer
{
  private static final Log log = ExoLogger.getLogger(BookstoreWebService.class);

  private static final CacheControl cc;

  private BookStorage bookStorage;
  
  static 
  {
    cc = new CacheControl();
    cc.setNoCache(true);
    cc.setNoStore(true);
  }

  public BookstoreWebService() 
  {
    log.info("-- Bookstore WebService constructor ---\n");
    
    try {
      ExoContainer eContainer = ExoContainerContext.getCurrentContainer();
      ComponentLocator.setContainer(eContainer);
      ComponentLocator.emptyDefaultNodes();
      ComponentLocator.initDefaultNodes();
      ComponentLocator.initBookstore();
      bookStorage = (BookStorage) eContainer.getComponentInstanceOfType(BookStorage.class);
    } catch (Exception e) {
      log.error("exception init container " + e.getMessage());
    }  
  }
  
  /**
   * search book with title like 'keyword'
   * 
   * @param keyword
   * @return first 5 books that matches 'keywords' in JSON format
   */
  
  @GET
  @Path("/searchBookByTitle/{keyword}")
  public Response searchBookByTitleLike(@PathParam("keyword") String keyword) throws Exception
  {
    log.info("receive searchBookByTitleLike request - keyword " + keyword);
    
    Set<Book> books = null;

    try 
    {
      books = bookStorage.searchBookBySpecification(new BookTitleMatches(keyword), new Integer(5));
    }
    catch (Exception e)
    {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * search book with isbn like 'isbn'
   * 
   * @param isbn
   * @return first 5 books whose isbn that matches 'isbn' in JSON format
   */
  
  @GET
  @Path("/searchBookByIsbn/{isbn}")
  public Response searchBookByIsbnLike(@PathParam("isbn") String isbn) throws Exception
  {
    log.info("receive searchBookByIsbnLike request - isbn " + isbn);
    
    Set<Book> books = null;

    try 
    {
      books = bookStorage.searchBookBySpecification(new BookIsbnMatches(isbn), new Integer(5));
    }
    catch (Exception e)
    {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * search book with author name like 'authorName'
   * 
   * @param authorName
   * @return first 5 books whose author name that matches 'authorName' in JSON format
   */
  
  @GET
  @Path("/searchBookByAuthorName/{authorName}")
  public Response searchBookByAuthorName(@PathParam("authorName") String authorName) throws Exception
  {
    log.info("receive searchBookByAuthorName request - authorName " + authorName);
    
    Set<Book> books = null;

    try 
    {
      books = bookStorage.searchBookByAuthorName(authorName, new Integer(5));
    }
    catch (Exception e)
    {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * search book with all properties like 'keyword'
   * 
   * @param keyword
   * @return first 5 books whose properties that match 'keyword' in JSON format
   */
  
  @GET
  @Path("/searchBookByAllProperties/{keyword}")
  public Response searchBookByAllProperties(@PathParam("keyword") String keyword) throws Exception
  {
    log.info("receive searchBookByAllProperties request - keyword " + keyword);
    
    Set<Book> books = null;

    try 
    {
      books = bookStorage.searchBookByAllProperties(keyword);
    }
    catch (Exception e)
    {
      return Response.status(Status.NO_CONTENT).build();
    }

    return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * search author with name like 'authorName'
   * 
   * @param authorName
   * @return first 5 author that matches 'authorName' in JSON format
   */
  
  @GET
  @Path("/searchAuthorByName/{authorName}")
  public Response searchAuthorByNameLike(@PathParam("authorName") String authorName) throws Exception
  {
    log.info("receive searchAuthorByNameLike request - authorName " + authorName);
    
    Set<Author> authors = null;
    
    try
    {
      authors = bookStorage.getAuthorWithNameLike(authorName, new Integer(5));
    }
    catch (Exception e)
    {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.ok(authors, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
}
