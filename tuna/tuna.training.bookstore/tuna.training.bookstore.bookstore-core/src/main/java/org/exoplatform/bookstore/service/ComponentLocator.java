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
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.exoplatform.bookstore.common.BookstoreConstant;
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
  
  private static StandaloneContainer sContainer = null;
  
  private static Session session = null;
  
  private static Node rootNode = null;
  
  private static final Log log = (Log) ExoLogger.getExoLogger(ComponentLocator.class);
  
  public static StandaloneContainer getContainer() 
  {
    log.info("--- getContainer ---");
    
    if (sContainer != null) return sContainer;
    
    String containerConfig = ComponentLocator.class.getResource(
      BookstoreConstant.CONTAINER_CONFIGURATION).toString();
    
    try 
    {
      StandaloneContainer.addConfigurationURL(containerConfig);

      String loginConfig = ComponentLocator.class.getResource(
        BookstoreConstant.LOGIN_CONFIGURATION).toString(); 
        
      System.setProperty("java.security.auth.login.config", loginConfig);
    
      sContainer = StandaloneContainer.getInstance();
    }  
    catch (Exception e)
    {
      log.error("--- exception getting Container ---" + e.getMessage());
    }  
      
    return sContainer;
  }
  
  
  public static Session getSession() 
  {
    log.info("--- getSession ---");
    
    if (session != null) return session;
    
    RepositoryService repoSer = (RepositoryService)
        getContainer().getComponentInstance(RepositoryService.class);
    
    try {
      session = repoSer.getRepository("repository")
                        .login(new SimpleCredentials(
                                   BookstoreConstant.USERNAME_REPOSITORY, 
                                   BookstoreConstant.PASSWORD_REPOSITORY.toCharArray()),
                               BookstoreConstant.BOOKSTORE_REPOSITORY
                               );
    } 
    catch (Exception e) 
    {
      log.error("--- get Session exception ---" + e.getMessage());
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
    
    if (rootNode == null) 
      log.info("--- error: rootNode null ---");
      
    return rootNode;
  }
  
}
