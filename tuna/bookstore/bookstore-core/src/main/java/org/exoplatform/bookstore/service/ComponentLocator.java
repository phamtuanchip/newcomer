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

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.query.QueryManager;
import javax.jcr.version.VersionException;

import org.exoplatform.bookstore.common.BookstoreConstant;
import org.exoplatform.bookstore.domain.Author;
import org.exoplatform.bookstore.domain.Book;
import org.exoplatform.bookstore.domain.NoAuthor;
import org.exoplatform.bookstore.storage.BookStorage;
import org.exoplatform.bookstore.storage.impl.BookStorageImpl;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 2, 2012  
 */

public class ComponentLocator {
  
  private static final Log log = (Log) ExoLogger.getExoLogger(ComponentLocator.class);
  
  private static ExoContainer eContainer = null;
  
  private static Session session = null;
  
  private static Node rootNode = null;
  
  private static BookStorage bookStorage = null;
    
  public static void setContainer(ExoContainer container)
  {
    eContainer = container;
  }
  
  public static ExoContainer getContainer() 
  {
    log.info("--- getContainer ---");
    
    if (eContainer == null) throw new RuntimeException("init container exception");
      
    return eContainer;
  }
  
  
  public static Session getSession() 
  {
    log.info("--- getSession ---");
    
    if (session != null) return session;
    
    RepositoryService repoSer = (RepositoryService)
        getContainer().getComponentInstance(RepositoryService.class);
    
    String username = null;
    String password = null;
    
    if (getContainer() instanceof StandaloneContainer) {
      log.info("Standalone Container used");
      
      username = BookstoreConstant.USERNAME_TEST_REPOSITORY;
      password = BookstoreConstant.PASSWORD_TEST_REPOSITORY;
      
      try {
        Repository repo = repoSer.getRepository("repository");
        String[] workspaces = repoSer.getRepository("repository").getWorkspaceNames();
        int size = workspaces.length;
        for (int i=0; i<size; i++)
        {
          log.info("workspace " + workspaces[i]);
        }
         session = repo.login(new SimpleCredentials(
                                     username, 
                                     password.toCharArray()),
                                 BookstoreConstant.BOOKSTORE_TEST_WORKSPACE
                                 );
      } 
      catch (Exception e) 
      {
        log.error("--- get Session exception ---" + e.getMessage());
      }
    }
    else if (getContainer() instanceof PortalContainer)
    {
      log.info("Portal Container used");

      username = BookstoreConstant.USERNAME_REPOSITORY;
      password = BookstoreConstant.PASSWORD_REPOSITORY;
    
      try {
        Repository repo = repoSer.getRepository("repository");
        String[] workspaces = repoSer.getRepository("repository").getWorkspaceNames();
        int size = workspaces.length;
        for (int i=0; i<size; i++)
        {
          log.info("workspace " + workspaces[i]);
        }
         session = repo.login(new SimpleCredentials(
                                     username, 
                                     password.toCharArray()),
                                 BookstoreConstant.BOOKSTORE_WORKSPACE
                                 );
      } 
      catch (Exception e) 
      {
        log.error("--- get Session exception ---" + e.getMessage());
      }
    
    }
     
    
    return session;
  }
  
  
  public static Node getRootNode() 
  {
    log.info("--- getRootNode ---");
    
    if (rootNode != null) return rootNode;
    
    try {
      rootNode = getSession().getRootNode();
    } 
    catch (Exception e) 
    {
      log.error("--- getRootNode exception ---" + e.getMessage());
    }
      
    return rootNode;
  }
   
  
  public static void initDefaultNodes() 
  {
    log.info("--- init default nodes ---");
    
    try 
    {
      Node bookstoreNode = getRootNode().addNode("exo:bookstore");
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
  
  
  public static BookStorage initBookstore()
  {
    log.info("--- initBookstore ---");
    
    try {
      BookStorage bookStorage = (BookStorage) getContainer()
          .getComponentInstanceOfType(BookStorage.class);
      
      Author martinFowler = new Author("Martin Fowler");
      Author ericEvans    = new Author("Eric Evans");
      Author exo          = new Author("Exo Platform");
      
      bookStorage.addAuthor(martinFowler).addAuthor(ericEvans).addAuthor(exo);
      
      bookStorage.insertBook(new Book("1000", "Design Pattern", martinFowler))
                 .insertBook(new Book("1001", "Domain Driven Design", ericEvans))
                 .insertBook(new Book("1002", "Head First Java", null))
                 .insertBook(new Book("1003", "eXo JCR", exo))
                 .insertBook(new Book("1004", "eXo WebUI", exo));
    }
      catch (Exception e)
    {
      log.error("init bookstore exception " + e.getMessage());
    }
    
    return bookStorage;
  }
  
  
  public static void emptyDefaultNodes()
  {
    log.info("--- empty default nodes ---");

    try 
    {
      Node bookstoreNode = getRootNode().getNode("exo:bookstore");
      bookstoreNode.remove();
      getSession().save();
    } 
    catch (PathNotFoundException e) 
    {
      log.error("node exo:bookstore does not exist " + e.getMessage());
    }
    catch (RepositoryException e)
    {
      log.error("repository exception " + e.getMessage());
    }
  }
  
  public static QueryManager getQueryManager() throws Exception
  {
    return getSession().getWorkspace().getQueryManager();
  }
}
