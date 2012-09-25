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
package org.exoplatform.cs;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import junit.framework.TestCase;
import org.exoplatform.container.StandaloneContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.CredentialsImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.Authenticator;
import org.exoplatform.services.security.Credential;
import org.exoplatform.services.security.Identity;
import org.exoplatform.services.security.PasswordCredential;
import org.exoplatform.services.security.UsernameCredential;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 24, 2012  
 */
public class exoJCRTest extends TestCase {
  
  private RepositoryService repoService ;
  private static final Log log = ExoLogger.getLogger(exoJCRTest.class) ;
  
  public void setUp() throws Exception {
    StandaloneContainer container = StandaloneContainer.getInstance()  ;
    repoService = (RepositoryService) container.getComponentInstance(RepositoryService.class) ;
      
    if(System.getProperty("java.security.auth.login.config") == null)
        System.setProperty("java.security.auth.login.config", 
          Thread.currentThread().getContextClassLoader().getResource("login.config").toString()) ;
  }
  
  public void testWorkspaceNames() throws Exception {
    log.info("--- TEST workspace name ---");
    log.info("Should produce anhtu in the result");
    String[] workspaceNames = repoService.getDefaultRepository().getWorkspaceNames();
        
    log.info("Result:");
    for(String s : workspaceNames) {
      log.info("testWorkspaceNames: " + s) ;
    }
  }
  
  public void testLoginUser() throws Exception {
    log.info("--- TEST login user ---");
    log.info("Should allow default user exo with password exo to login to successfully");
    
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;

    assertNotNull(session) ;
    assertEquals(session.getUserID(), "exo") ;
    assertEquals(session.getWorkspace().getName(), "anhtu") ;
    log.info("Login successfully !!!") ;
  }
  
  public void testAddingNode() throws Exception {
    log.info("--- TEST adding node ---") ;
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;
    assertNotNull(session) ;
    
    Node rootNode = session.getRootNode() ;
    Node editor = rootNode.addNode("bookstore:editor") ;
    assertNotNull(editor) ; 
    log.info("add node editor") ;
    
    Node c1 = editor.addNode("bookstore:collection") ;
    c1.setProperty("bookstore:title", "romance") ;
    c1.setProperty("bookstore:description", "romance book") ;
    assertNotNull(c1) ; 
    log.info("add node editor--> collection romance") ;
    
    Node c2 = editor.addNode("bookstore:collection") ;
    c2.setProperty("bookstore:title", "manga") ;
    c2.setProperty("bookstore:description", "romance book") ;
    assertNotNull(c2) ; 
    log.info("add node editor--> collection manga") ;
  
    Node c3 = c2.addNode("bookstore:collection") ;
    c3.setProperty("bookstore:title", "doraemon") ;
    c3.setProperty("bookstore:description", "collection of doraemon") ;
    assertNotNull(c3) ; 
    log.info("add node editor--> collection manga-->collection doraemon") ;
        
    session.save() ;
  }
  
  public void testTraversalAccess() throws Exception {
    log.info("--- TEST travesal access ---") ;
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;
    assertNotNull(session) ;
    
    Node rootNode = session.getRootNode() ;
    Node editor = rootNode.getNode("bookstore:editor") ;
    NodeIterator iterator = editor.getNodes() ;
    
    while(iterator.hasNext()) {
      Node node = iterator.nextNode() ;
      log.info("Node name: " + node.getName()) ;
      log.info("Primary node type: " + node.getPrimaryNodeType().getName()) ;
      log.info("Note path: " + node.getPath()) ;

      log.info("Title property: " + node.getProperty("bookstore:title").getString()) ;
      log.info("Primary node type: " + node.getPrimaryNodeType().getName()) ;
      log.info("Property path: " + node.getProperty("bookstore:title").getPath()) ;
      
      log.info("Description property: " + node.getProperty("bookstore:description").getString()) ;
      log.info("Primary node type: " + node.getPrimaryNodeType().getName()) ;
      log.info("Property path: " + node.getProperty("bookstore:description").getPath()) ;
    }
  }
  
  public void testSearchContentWithXPath() throws Exception {
    log.info("--- TEST search content with XPath ---") ;
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;
    
    Workspace ws = session.getWorkspace() ;
    QueryManager qm = ws.getQueryManager() ;
    Query query = qm.createQuery(
      "//bookstore:editor/bookstore:collection[@bookstore:title = 'manga']", Query.XPATH) ;
    
    QueryResult result = query.execute() ;
    NodeIterator iterator = result.getNodes() ;
    while(iterator.hasNext()) {
      Node node = iterator.nextNode() ;
      log.info("Name: " + node.getName()) ;
      log.info("Title: " + node.getProperty("bookstore:title").getString());
    }
  }
  
  public void testSearchContentWithSQL() throws Exception {
    log.info("--- TEST search content with SQL ---") ;
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;
    
    String searchAllNodesWithTitleDoraemon = 
        "SELECT * FROM nt:base WHERE bookstore:title='doraemon'";
    Workspace ws = session.getWorkspace() ;
    QueryManager qm = ws.getQueryManager() ;
    Query query = qm.createQuery(
      searchAllNodesWithTitleDoraemon, Query.SQL) ;
    
    QueryResult result = query.execute() ;
    NodeIterator iterator = result.getNodes() ;
    while(iterator.hasNext()) {
      Node node = iterator.nextNode() ;
      log.info("Name: " + node.getName()) ;
      log.info("Title: " + node.getProperty("bookstore:title").getString()) ;
      log.info("Primary node type: " + node.getPrimaryNodeType().getName()) ;
      log.info("Note path: " + node.getPath()) ;
    }
  }
  
  public void testAddVersioningForNode() throws Exception {
    log.info("--- TEST add versioning for node ---") ;
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;
    
    Node rootNode = session.getRootNode();
    Node doraemon = rootNode.getNode(
      "bookstore:editor/bookstore:collection[2]/bookstore:collection");
    doraemon.addMixin("mix:versionable");
    doraemon.save();
    doraemon.checkin();
  }  
  
  public void testCheckInNewProperty() throws Exception {
    log.info("--- TEST check in new property ---");
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;
    
    Node rootNode = session.getRootNode() ;
    Node doraemon = rootNode.getNode(
      "bookstore:editor/bookstore:collection[2]/bookstore:collection") ;
    
    doraemon.checkout();
    doraemon.setProperty("bookstore:description", "Updated description for the collection");
    doraemon.save();
    doraemon.checkin();
    assertEquals(doraemon.getProperty("bookstore:description").getString(),
      "Updated description for the collection");
    log.info("check in successfully");
  }
  
  public void testBrowseVersionHistory() throws Exception {
    log.info("--- TEST browse version history ---");
    Credentials credentical = new CredentialsImpl("exo", "exo".toCharArray()) ;
    Repository repository = repoService.getRepository("repository") ;
    Session session = repository.login(credentical, "anhtu") ;
    
    Node rootNode = session.getRootNode() ;
    Node doraemon = rootNode.getNode(
      "bookstore:editor/bookstore:collection[2]/bookstore:collection") ;
    
    VersionHistory vh = doraemon.getVersionHistory() ;
    VersionIterator vi = vh.getAllVersions() ;
    
    assertEquals(new Long(vi.getSize()), new Long(3)) ;
    
    vi.skip(1) ; // skip vroot version
    while (vi.hasNext()) {
      Version v = vi.nextVersion() ;
      NodeIterator ni = v.getNodes() ;

      while (ni.hasNext()) {
        Node nv = ni.nextNode() ;
        log.info("Version: " +
        v.getCreated().getTime()) ;

        log.info("Description: " + nv.getProperty("bookstore:description").getString()) ;
      }
      
    }
    
  }
  
  
}
