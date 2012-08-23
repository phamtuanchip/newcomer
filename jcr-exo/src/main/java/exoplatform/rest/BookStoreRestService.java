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
package exoplatform.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.jcr.RepositoryException;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import exoplatform.BookStoreService;
import exoplatform.entity.Author;
import exoplatform.entity.Book;
import exoplatform.entity.Book.CATEGORY;
import exoplatform.entity.User;
import exoplatform.exception.BookNotFoundException;
import exoplatform.exception.DuplicateBookException;
import exoplatform.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 20, 2012  
 */

@Path("/bookstore")
public class BookStoreRestService implements ResourceContainer {

  private static final Log log = ExoLogger.getLogger(BookStoreRestService.class);
  private static final CacheControl cc;
  private BookStoreService service;
  
  static {
    cc = new CacheControl();
    cc.setNoCache(true);
    cc.setNoStore(true);
  }
  
  /**
   * 
   */
  public BookStoreRestService(BookStoreService service) {
    this.service = service;
  }

  /**
   * get author by book id
   * 
   * @param id
   * @return
   */
  @GET
  @RolesAllowed("users")
  @Path("/searchAuthorByBookId/{bookId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchAuthorByBookId(@PathParam("bookId") String bookId) {
    Author author = new Author();
    author = service.getAuthorByBookId(bookId);
    if (author == null) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(author, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * get all book exist in datastore
   */
  @GET
  @RolesAllowed("users")
  @Path("/searchAllBook")
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchAllBook(){
    List<Book> books = new ArrayList<Book>();
    books = service.getAllBook();
    if (books == null || books.size() <= 0) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * get book by book name
   * 
   * @param bookName The name of book
   * @return Response
   */
  @GET
  @RolesAllowed("users")
  @Path("/searchBookByName/{bookName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchBookByName(@PathParam("bookName") String bookName) {
    List<Book> books = new ArrayList<Book>();
    books = service.searchBookByName(bookName);
    if (books == null || books.size() <= 0) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * get user by name with sql statement
   * 
   * @param username The name of user
   * @return
   */
  @GET
  @RolesAllowed("users")
  @Path("/searchBookByAuthor/{authorName}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response searchBookByAuthor(@PathParam("authorName") String authorName) {
    List<Book> books = new ArrayList<Book>();
    books = service.searchBookByAuthor(authorName);
    if (books == null || books.size() <= 0) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * add new author to datastorage
   * 
   * @param authorName
   * @param authorAddress
   * @param authorPhone
   * @return
   */
  @POST
  @RolesAllowed("users")
  @Path("/addAuthor")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addAuthor(@FormParam("authorName") String authorName, 
                            @FormParam("authorAddress") String authorAddress, 
                            @FormParam("authorPhone") String authorPhone) throws DuplicateBookException, RepositoryException {
      Author author = Utils.createAuthorByNode(service.addAuthor(authorName, authorAddress, authorPhone));
      if (author == null) {
        return Response.status(Status.NO_CONTENT).build();
      }
      return Response.ok(author, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * add new user to datastorage
   * 
   * @param username
   * @param password
   * @param fullname
   * @param address
   * @return
   */
  @POST
  @RolesAllowed("users")
  @Path("/addUser")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addUser(@FormParam("username") String username,
                          @FormParam("password") String password,
                          @FormParam("fullname") String fullname,
                          @FormParam("address") String address,
                          @FormParam("phone") String phone) throws DuplicateBookException, RepositoryException {
    User user = Utils.createUserByNode(service.addUser(username, password, fullname, address, phone));
    if (user == null) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(user, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * add new book to datastorage
   * 
   * @param bookName
   * @param category
   * @param content
   * @param authorId
   * @return
   */
  @POST
  @RolesAllowed("users")
  @Path("/addBook")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addBook(@FormParam("bookName") String bookName, 
                      @FormParam("category") CATEGORY category, 
                      @FormParam("content") String content, 
                      @FormParam("authorId") String authorId) throws DuplicateBookException, RepositoryException {
    Book book = Utils.createBookByNode(service.addBook(new Book(bookName, category, content), authorId));
    if (book == null) {
      return Response.status(Status.NO_CONTENT).build();
    }
    return Response.ok(book, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  /**
   * delete author from datastorage
   * 
   * @param authorName
   * @param authorAddress
   * @param authorPhone
   * @return
   */
  @DELETE
  @RolesAllowed("users")
  @Path("/deleteAuthor")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteAuthor(@FormParam("authorId") String authorId) throws BookNotFoundException, RepositoryException {
    try {
      service.deleteAuthor(authorId);
      return Response.ok().cacheControl(cc).build();
    } catch (Exception e) {
      return Response.status(Status.NO_CONTENT).build();
    }
  }
  
  /**
   * delete author from datastorage
   * 
   * @param authorName
   * @param authorAddress
   * @param authorPhone
   * @return
   */
  @PUT
  @RolesAllowed("users")
  @Path("/editAuthor")
  @Produces(MediaType.APPLICATION_JSON)
  public Response editAuthor(@FormParam("authorId") String authorId,
                             @FormParam("authorName") String authorName, 
                             @FormParam("authorAddress") String authorAddress, 
                             @FormParam("authorPhone") String authorPhone ) throws BookNotFoundException, RepositoryException {
    try {
      Author author = new Author();
      author.setAuthorId(authorId);
      author.setName(authorName);
      author.setAddress(authorAddress);
      author.setPhone(authorPhone);
      service.editAuthor(author);
      return Response.ok().cacheControl(cc).build();
    } catch (Exception e) {
      return Response.status(Status.NO_CONTENT).build();
    }
  }
  
  @PUT
  @RolesAllowed("users")
  @Path("/addUserReference")
  @Produces(MediaType.APPLICATION_JSON)
  public Response addUserReference(@FormParam("userId") String userId, 
                               @FormParam("bookId") String bookId) {
    try {
      service.addUserReference(userId, bookId);
      return Response.ok().cacheControl(cc).build();
    } catch (DuplicateBookException de) {
      return Response.status(Status.NO_CONTENT).build();
    }
  }
  
}
