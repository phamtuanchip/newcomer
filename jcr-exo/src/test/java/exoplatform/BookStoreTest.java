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

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import junit.framework.TestCase;

import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.rest.ContainerResponseWriter;
import org.exoplatform.services.rest.RequestHandler;
import org.exoplatform.services.rest.impl.ContainerRequest;
import org.exoplatform.services.rest.impl.ContainerResponse;
import org.exoplatform.services.rest.impl.EnvironmentContext;
import org.exoplatform.services.rest.impl.InputHeadersMap;
import org.exoplatform.services.rest.impl.MultivaluedMapImpl;
import org.exoplatform.services.rest.tools.ByteArrayContainerResponseWriter;

import exoplatform.entity.Author;
import exoplatform.entity.Book;
import exoplatform.exception.DuplicateBookException;
import exoplatform.rest.BookStoreRestService;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 17, 2012  
 */
public class BookStoreTest extends TestCase {
  
  private static BookStoreRestService restService;
  private static BookStoreService service;
  private static StandaloneContainer container;
  private static RequestHandler requestHandler;
  
  static final String baseURI = "";
  
  static {
    initContainer();
  }
  
  public BookStoreTest() throws Exception {
    super();
  }
  
  private static void initContainer() {
    try {
      String configFile = BookStoreTest.class.getResource("/conf/portal/test-configuration.xml").toString();
      StandaloneContainer.addConfigurationURL(configFile);
      container = StandaloneContainer.getInstance();
      service = (BookStoreService) container.getComponentInstanceOfType(BookStoreService.class);
      requestHandler = (RequestHandler) container.getComponentInstanceOfType(RequestHandler.class);
      restService = (BookStoreRestService) container.getComponentInstanceOfType(BookStoreRestService.class);
    } catch (Exception e) {
      throw new RuntimeException("fail to initialize container: " + e.getMessage(), e);
    }
  }
  
  @Override
  protected void setUp() throws Exception {
    
  }
  
  /**
   * test search book by name function
   */
  public void testSearchBookByName() {
    String bookName = "";
    List<Book> books = new ArrayList<Book>();
    books = service.searchBookByName(bookName);
    assertNotNull(books);
  }
  
  /**
   * add new author to datastorage
   * 
   * @param authorName
   * @param authorAddress
   * @param authorPhone
   * @return
   */
  public void testAddAuthor() throws DuplicateBookException {
    
  }
  
  /**
   * add new user
   * 
   * @param user
   * @param nodes
   * @return
   * @throws DuplicateBookException
   */
  public void testAddUser() throws DuplicateBookException {
    
  }
  
  /**
   * get user by name with sql statement
   * 
   * @param username
   * @return
   */
  public void testWSSearchBookByName() {
    
    String username = "binhnv";
    String extURI = "/rest/private/bookstore/searchBookByName/" + username;
    
    MultivaluedMap<String, String> values = new MultivaluedMapImpl();
    values.putSingle("username", username);
    ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
    try {
      ContainerResponse response = service("GET", extURI, baseURI, values, null, writer);
      assertNotNull(response.getEntity());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * get user by name with sql statement
   * 
   * @param username
   * @return
   */
  public void testWSSearchBookByAuthor() {
    
    String authorName = "Mario%20Puzzo";
    String extURI = "/rest/private/bookstore/searchBookByAuthor/" + authorName;
    
    MultivaluedMap<String, String> values = new MultivaluedMapImpl();
    values.putSingle("authorName", authorName);
    ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
    try {
      ContainerResponse response = service("GET", extURI, baseURI, values, null, writer);
      assertNotNull(response.getEntity());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  private ContainerResponse service(String method,
                                    String requestURI,
                                    String baseURI,
                                    MultivaluedMap<String, String> headers,
                                    byte[] data,
                                    ContainerResponseWriter writer) throws Exception {
     RequestLifeCycle.begin(container);
     if (headers == null) {
       headers = new MultivaluedMapImpl();
     }

     ByteArrayInputStream in = null;
     if (data != null) {
       in = new ByteArrayInputStream(data);
     }

     EnvironmentContext envctx = new EnvironmentContext();
     HttpServletRequest httpRequest = new MockHttpServletRequest(in,
                                                                 in != null ? in.available() : 0,
                                                                 method,
                                                                 new InputHeadersMap(headers));
     envctx.put(HttpServletRequest.class, httpRequest);
     EnvironmentContext.setCurrent(envctx);
     ContainerRequest request = new ContainerRequest(method,
                                                     new URI(requestURI),
                                                     new URI(baseURI),
                                                     in,
                                                     new InputHeadersMap(headers));
     ContainerResponse response = new ContainerResponse(writer);
     requestHandler.handleRequest(request, response);
     RequestLifeCycle.end();
     return response;
   }
  
}
