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

import java.io.ByteArrayInputStream;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import org.exoplatform.bookstore.common.BookstoreConstant;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.ContainerResponseWriter;
import org.exoplatform.services.rest.RequestHandler;
import org.exoplatform.services.rest.impl.ApplicationContextImpl;
import org.exoplatform.services.rest.impl.ContainerRequest;
import org.exoplatform.services.rest.impl.ContainerResponse;
import org.exoplatform.services.rest.impl.EnvironmentContext;
import org.exoplatform.services.rest.impl.InputHeadersMap;
import org.exoplatform.services.rest.impl.MultivaluedMapImpl;
import org.exoplatform.services.rest.impl.ProviderBinder;
import org.exoplatform.services.rest.impl.ResourceBinder;
import org.exoplatform.services.rest.tools.ByteArrayContainerResponseWriter;

import junit.framework.TestCase;

/**
 * Used to test bookstore REST web service
 * 
 * 
 * Created by The eXo Platform SAS
 * 
 * @author Anh-Tu Nguyen<br>
 * <a href="mailto:tuna@exoplatform.com">tuna@exoplatform.com</a><br>
 * Oct 25, 2012  
 */
public class BookstoreWebServiceTest extends TestCase
{
  private static Log log = ExoLogger.getExoLogger(BookstoreWebServiceTest.class);
  
  private static ExoContainer eContainer;
  
  private static BookstoreWebService webService;
  
  private static BookStorage bookStorage;
  
  private static RequestHandler requestHandler;
  
  private static String baseURI = "";

  private static ResourceBinder binder;
  
  static {
    initContainer();
    initMainComponent();
  }
  

  private static void initContainer() 
  {
    log.info("--- init container ---");

    String containerConfig = BookstoreWebServiceTest.class.getResource(
        BookstoreConstant.REST_CONTAINER_CONFIGURATION).toString();
    
    try 
    { 
      StandaloneContainer.addConfigurationURL(containerConfig);
      eContainer = ExoContainerContext.getCurrentContainer();
      String loginConfig = BookstoreWebServiceTest.class.getResource(
                             BookstoreConstant.LOGIN_CONFIGURATION).toString();   
                             System.setProperty("java.security.auth.login.config", loginConfig);
    }
    catch (Exception e)
    {
      log.info("init container exception " + e.getMessage());
    }
    log.info("--- init container: OK ---\n");
  }
  
  @SuppressWarnings("deprecation")
  private static void initMainComponent()
  {
    log.info("--- init main component ---");
    
    ComponentLocator.emptyDefaultNodes();
    ComponentLocator.initDefaultNodes();
    ComponentLocator.initBookstore();
    
    bookStorage = (BookStorage) eContainer.getComponentInstanceOfType(BookStorage.class);
    webService = (BookstoreWebService) eContainer.getComponentInstance(BookstoreWebService.class);  
    requestHandler = (RequestHandler) eContainer.getComponentInstanceOfType(RequestHandler.class);
    binder = (ResourceBinder) eContainer.getComponentInstanceOfType(ResourceBinder.class);
    
    ProviderBinder.setInstance(new ProviderBinder());
    ProviderBinder providers = ProviderBinder.getInstance();
    ApplicationContextImpl.setCurrent(new ApplicationContextImpl(null, null, providers));
    binder.clear();
    
    binder.bind(webService);
    
    if (requestHandler != null) log.info("get request handler OK");
    log.info("--- init main component: OK ---\n");
  }
  
  @Override
  public void setUp() throws Exception {}
  
  public void testSearchBookByTitle()
  {
    log.info("--- test testSearchBookByTitle ---\n");
    
    String restURI = "/bookstore/rest/bookstore/searchBookByTitle/d";
    MultivaluedMap<String, String> values = new MultivaluedMapImpl();
    values.putSingle("username", "root");
    ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
    
    try 
    {
      ContainerResponse response = service("GET", restURI, baseURI, values, null, writer);
      log.info("response status: " + response.getStatus());
      log.info("content type: " + response.getContentType().toString());
      
      //assertEquals(new Integer(response.getStatus()), new Integer(200));
      assertNotNull(response.getEntity());
    } 
    catch (Exception e) 
    {
      log.error("exception testSearchBookByTitle: " + e.getMessage());
    }
    
    log.info("--- test testSearchBookByTitle: OK ---\n");
  }
  
  
  private ContainerResponse service(String method,
                                    String requestURI,
                                    String baseURI,
                                    MultivaluedMap<String, String> headers,
                                    byte[] data,
                                    ContainerResponseWriter writer) throws Exception {
     RequestLifeCycle.begin(eContainer);
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
