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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.impl.core.query.QueryImpl;
import org.exoplatform.services.jcr.util.IdGenerator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import exoplatform.entity.Author;
import exoplatform.entity.Book;
import exoplatform.entity.User;
import exoplatform.exception.BookNotFoundException;
import exoplatform.exception.DuplicateBookException;
import exoplatform.utils.PropertyReader;
import exoplatform.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 6, 2012  
 */
public class JCRDataStorage {

  /* Define a exo log */
  private static final Log log = ExoLogger.getLogger(JCRDataStorage.class);
  /* Define a constant of parent path */
  public static final String DEFAULT_PARENT_PATH = "/exostore:bookstore";
  /* Define a constant of book path */
  public static final String DEFAULT_PARENT_BOOK_PATH = "/exostore:book";
  /* Define a constant of author path */
  public static final String DEFAULT_PARENT_AUTHOR_PATH = "/exostore:author";
  /* Define a constant of user path */
  public static final String DEFAULT_PARENT_USER_PATH = "/exostore:user";
  /* Define a constant of workspace path */
  public static final String DEFAULT_WORKSPACE_NAME = "bookstore";
  /* Define a RepositoryService which support integrate with repository */
  private RepositoryService repoService;
  
  /**
   * JCRDataStorage constructor
   * @param repositoryService
   */
  public JCRDataStorage(RepositoryService repositoryService) {
      this.repoService = repositoryService;
  }
  
  /**
   * Prepare node and data for repository
   */
  public void init() {
    /* create SessionProvider */
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    Node node = null;
    try {
      /* if node exist, get node from parent path */
      node = getNodeByPath(DEFAULT_PARENT_PATH, sProvider);
    } catch (PathNotFoundException e) {
      /*if node doesn't exist, create new node */
      try {
        /* get node */
        node = getNodeByPath(DEFAULT_PARENT_PATH, sProvider);
        node.getSession().save();
      } catch (RepositoryException re) {
        log.error("Failed to init BookStore jcr node's path", re);
      }
    } catch (RepositoryException e) {
      log.error("Failed to init BookStore jcr node's path", e);
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get node from path  
   * 
   * @param nodePath The path of node by String type
   * @param sessionProvider The session 
   * @return The node
   * @throws RepositoryException
   */
  private Node getNodeByPath(String nodePath, SessionProvider sessionProvider) throws RepositoryException {
    return (Node) getSession(sessionProvider).getItem(nodePath);
  }
  
  /**
   * get session provider and return a session of workspace
   * 
   * @param sessionProvider
   * @return The session
   * @throws RepositoryException
   */
  private Session getSession(SessionProvider sessionProvider) throws RepositoryException {
    ManageableRepository currentRepo = repoService.getCurrentRepository();
    return sessionProvider.getSession(DEFAULT_WORKSPACE_NAME, currentRepo);
  }
  
  /**
   * get book by book id
   * 
   * @param id The id of book
   * @return Book
   */
  public Book getBook(String id) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_BOOK_PATH + "/" + id, sProvider);
      return Utils.createBookByNode(node);
    } catch (RepositoryException re) {
      log.error("Can not find this book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get book by book id
   * 
   * @param id The id of book
   * @return Book
   */
  public Node getBookNode(String id) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_BOOK_PATH + "/" + id, sProvider);
      return node;
    } catch (RepositoryException re) {
      log.error("Can not find this book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get user by user id
   * 
   * @param id
   * @return
   */
  public User getUser(String id) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_USER_PATH + "/" + id, sProvider);
      return Utils.createUserByNode(node);
    } catch (RepositoryException re) {
      log.error("Can not find this user", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get user by user id
   * 
   * @param id
   * @return
   */
  public Node getUserNode(String id) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_USER_PATH + "/" + id, sProvider);
      return node;
    } catch (RepositoryException re) {
      log.error("Can not find this user", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get author by author id
   * 
   * @param id
   * @return
   */
  public Author getAuthor(String id) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_AUTHOR_PATH + "/" + id, sProvider);
      return Utils.createAuthorByNode(node);
    } catch (RepositoryException re) {
      log.error("Can not find this author", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get author by book id
   * 
   * @param id
   * @return
   */
  public Author getAuthorByBookId(String bookId) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node book = getBookNode(bookId);
      Property property = book.getProperty(BookNodeTypes.EXO_BOOK_AUTHOR);
      String uuid = property.getString();
      Author author = getAuthorByUUID(uuid);
      return author;
    } catch (RepositoryException re) {
      log.error("Can not find this author", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get user by name with sql statement
   * 
   * @param username
   * @return
   */
  public Author getAuthorByUUID(String uuid) {
    uuid.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    uuid = uuid.trim();
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    StringBuffer queryString = new StringBuffer("Select * from " + BookNodeTypes.EXO_AUTHOR);
    queryString.append(" where jcr:uuid = '" + uuid + "'");
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(queryString.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        return Utils.createAuthorByNode(node);
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not get user by name", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get author by author id
   * 
   * @param id
   * @return
   */
  public Node getAuthorNode(String id) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_AUTHOR_PATH + "/" + id, sProvider);
      return node;
    } catch (RepositoryException re) {
      log.error("Can not find this author", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * add new book to workspace
   * 
   * @param book The new book which want to add
   * @return Book
   * @throws DuplicateBookException
   */
  public Node addBook(Book book, String authorId) throws DuplicateBookException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    
    /* Check exist of book */
    if (isExistBookName(book.getName(), sProvider)) {
      throw new DuplicateBookException(String.format("Book %s is existed", book.getName()));
    }
    
    String nodeId = IdGenerator.generate();
    book.setBookId(nodeId);
    
    try {
      /* execute set data to node and save to workspace */
      Node parentNode = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_BOOK_PATH, sProvider);
      Node bookNode = parentNode.addNode("" + book.getBookId(), BookNodeTypes.EXO_BOOK);
      bookNode.setProperty(BookNodeTypes.EXO_BOOK_NAME, book.getName());
      bookNode.setProperty(BookNodeTypes.EXO_BOOK_CATEGORY, Utils.bookCategoryEnumToString(book.getCategory()));
      bookNode.setProperty(BookNodeTypes.EXO_BOOK_CONTENT, book.getContent());
      bookNode.setProperty(BookNodeTypes.EXO_BOOK_AUTHOR, getAuthorNode(authorId));
      parentNode.getSession().save();
      return bookNode;
    } catch (RepositoryException e) {
      log.error("Failed to add book", e);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * add new book to workspace
   * 
   * @param book The new book which want to add
   * @return Book
   * @throws DuplicateBookException
   */
  public Book addBookWithout(Book book) throws DuplicateBookException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    
    /* Check exist of book */
    if (isExistBookName(book.getName(), sProvider)) {
      throw new DuplicateBookException(String.format("Book %s is existed", book.getName()));
    }
    
    String nodeId = IdGenerator.generate();
    book.setBookId(nodeId);
    
    try {
      /* execute set data to node and save to workspace */
      Node parentNode = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_BOOK_PATH, sProvider);
      Node bookNode = parentNode.addNode("" + book.getBookId(), BookNodeTypes.EXO_BOOK);
      bookNode.setProperty(BookNodeTypes.EXO_BOOK_NAME, book.getName());
      bookNode.setProperty(BookNodeTypes.EXO_BOOK_CATEGORY, Utils.bookCategoryEnumToString(book.getCategory()));
      bookNode.setProperty(BookNodeTypes.EXO_BOOK_CONTENT, book.getContent());
      parentNode.getSession().save();
      return Utils.createBookByNode(bookNode);
    } catch (RepositoryException e) {
      log.error("Failed to add book", e);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * add new author
   * 
   * @param author
   * @return
   * @throws DuplicateBookException
   */
  public Node addAuthor(Author author) throws DuplicateBookException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    if (isExistAuthorName(author.getName(), sProvider)) {
      throw new DuplicateBookException(String.format("Author %s is existed", author.getName()));
    }
    String nodeId = IdGenerator.generate();
    author.setAuthorId(nodeId);
    try {
      Node parentNode = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_AUTHOR_PATH, sProvider);
      Node authorNode = parentNode.addNode("" + author.getAuthorId(), BookNodeTypes.EXO_AUTHOR);
      authorNode.setProperty(BookNodeTypes.EXO_AUTHOR_NAME, author.getName());
      authorNode.setProperty(BookNodeTypes.EXO_AUTHOR_ADDRESS, author.getAddress());
      authorNode.setProperty(BookNodeTypes.EXO_AUTHOR_PHONE, author.getPhone());
      parentNode.getSession().save();
      return authorNode;
    } catch (RepositoryException re) {
      log.error("Failed to add author", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * add new user
   * 
   * @param user
   * @param nodes
   * @return
   * @throws DuplicateBookException
   */
  public Node addUser(User user) throws DuplicateBookException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    if (isExistUserName(user.getUsername(), sProvider)) {
      throw new DuplicateBookException(String.format("User %s is existed", user.getUsername()));
    }
    String nodeId = IdGenerator.generate();
    user.setUserId(nodeId);
    try {
      Node parentNode = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_USER_PATH, sProvider);
      Node userNode = parentNode.addNode("" + user.getUserId(), BookNodeTypes.EXO_USER);
      userNode.setProperty(BookNodeTypes.EXO_USER_NAME, user.getUsername());
      userNode.setProperty(BookNodeTypes.EXO_USER_PASSWORD, user.getPassword());
      userNode.setProperty(BookNodeTypes.EXO_USER_FULLNAME, user.getFullname());
      userNode.setProperty(BookNodeTypes.EXO_USER_ADDRESS, user.getAddress());
      userNode.setProperty(BookNodeTypes.EXO_USER_PHONE, user.getPhone());
      parentNode.getSession().save();
      return userNode;
    } catch (RepositoryException re) {
      log.error("Failed to add user", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  public Node addUserReference(String userId, String bookId) throws DuplicateBookException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node bookNode = getBookNode(bookId);
      Node userNode = getUserNode(userId);
      List<Value> values = new ArrayList<Value>();
      PropertyIterator pros = userNode.getProperties(BookNodeTypes.EXO_BOOK);
      while (pros.hasNext()) {
        Property pro = pros.nextProperty();
        Value[] vals = pro.getValues();
        for (Value value : vals) {
          values.add(value);
        }
      }
      Value val = userNode.getSession().getValueFactory().createValue(getNodeByPath(bookNode.getPath(), sProvider));
      if (values.contains(val)) {
        throw new DuplicateBookException(String.format("Book %s is existed", bookId));
      }
      values.add(val);
      userNode.setProperty(BookNodeTypes.EXO_BOOK, values.toArray(new Value[values.size()]));
      userNode.getSession().save();
      return userNode;
    } catch (RepositoryException re) {
      log.error("Fail to add reference for user", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get user by name with sql statement
   * 
   * @param username
   * @return
   */
  public User getUserByNameSQL(String username) {
    username.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    username = username.trim();
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    StringBuffer queryString = new StringBuffer("Select * from " + BookNodeTypes.EXO_USER);
    queryString.append(" where " + BookNodeTypes.EXO_USER_NAME + " = '" + username + "'");
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(queryString.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        return Utils.createUserByNode(node);
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not get user by name", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get user by book name
   * 
   * @param bookName
   * @return
   */
  public User getUserByBookQuery(String bookName) {
    bookName.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    bookName = bookName.trim();
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    StringBuffer queryString = new StringBuffer("Select * from " + BookNodeTypes.EXO_USER);
    queryString.append(" where /" + BookNodeTypes.EXO_BOOK + "/" + BookNodeTypes.EXO_BOOK_NAME + "=" + bookName + "'");
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(queryString.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        return Utils.createUserByNode(node);
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not get user by name", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get user by name with xpath
   * 
   * @param username
   * @return
   */
  public User getUserByNameXPath(String username) {
    username.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    username = username.trim();
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    StringBuffer queryString = new StringBuffer("//element(*, " + BookNodeTypes.EXO_USER
        + ")[(jcr:like(@" + BookNodeTypes.EXO_USER_NAME + ",'%" + username + "%'" + ") and @" + BookNodeTypes.EXO_USER_ADDRESS + "='Hanoi'" + ")]");
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(queryString.toString(), Query.XPATH);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        PropertyReader reader = new PropertyReader(node);        
        return Utils.createUserByNode(node);
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not get user by name(XPath)", re);
      return null;
    } finally {
      sProvider.close();
    }    
  }
  
  /**
   * get user by name in range with sql statement 
   * 
   * @param username
   */
  public User getUserByNameLimtSQL(String username) {
    username.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    username = username.trim();
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    StringBuffer queryString = new StringBuffer("Select * from " + BookNodeTypes.EXO_USER);
    queryString.append(" where " + BookNodeTypes.EXO_USER_NAME + " = '" + username + "'");
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      QueryImpl query = (QueryImpl) queryManager.createQuery(queryString.toString(), Query.SQL);
      query.setOffset(0L);
      query.setLimit(3L);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        return Utils.createUserByNode(node);
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not get user by name", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get user by name in range with xpath statement 
   * 
   * @param username
   * @return
   */
  public User getUserByNameLimtXPath(String username) {
    username.replaceAll("\"", "\\\"").replace("-", StringUtils.EMPTY);
    username = username.trim();
    StringBuffer sb = new StringBuffer("//element(*," + BookNodeTypes.EXO_USER + ")");
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      QueryImpl query = (QueryImpl) queryManager.createQuery(sb.toString(), Query.XPATH);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        return Utils.createUserByNode(node);
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not get user by name(XPath)", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * delete a book by book id
   * 
   * @param id The id of book
   * @throws BookNotFoundException
   */
  public void deleteBook(String id) throws BookNotFoundException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_BOOK_PATH + "/" + id, sProvider);
      node.remove();
      node.getSession().save();
    } catch (RepositoryException re) {
      log.error("Failed to delete book by id", re);
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * delete a author by authorName
   * 
   * @param authorName The name of author
   * @throws BookNotFoundException
   */
  public void deleteAuthor(String authorId) throws BookNotFoundException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_AUTHOR_PATH + "/" + authorId, sProvider);
      node.remove();
      node.getSession().save();
    } catch (RepositoryException re) {
      log.error("Failed to delete book by id", re);
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * delete all of book in workspace
   */
  public void deleteAll() {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node parentNode = getNodeByPath(DEFAULT_PARENT_PATH, sProvider);
      parentNode.remove();
      parentNode.getSession().save();
    } catch (RepositoryException e) {
      log.error("Failed to delete all book", e);
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * edit a exist book
   * 
   * @param book The book want to delete
   * @throws BookNotFoundException
   */
  public void editBook(Book book) throws BookNotFoundException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      /* execute modify data and set to workspace */
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_BOOK_PATH + "/" + book.getBookId(), sProvider);
      node.setProperty(BookNodeTypes.EXO_BOOK_NAME, book.getName());
      node.setProperty(BookNodeTypes.EXO_BOOK_CONTENT, book.getContent());
      node.setProperty(BookNodeTypes.EXO_BOOK_CATEGORY, Utils.bookCategoryEnumToString(book.getCategory()));
      node.getSession().save();
    } catch (RepositoryException re) {
      log.error(String.format("Book %s is not found", book.getBookId()), re);
      throw new BookNotFoundException(String.format("Book %s is not found", book.getBookId()));
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * edit a exist book
   * 
   * @param book The book want to delete
   * @throws BookNotFoundException
   */
  public void editAuthor(Author author) throws BookNotFoundException {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      /* execute modify data and set to workspace */
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + DEFAULT_PARENT_AUTHOR_PATH + "/" + author.getAuthorId(), sProvider);
      if (!StringUtils.isEmpty(author.getName())) {
        node.setProperty(BookNodeTypes.EXO_AUTHOR_NAME, author.getName());
      }
      if (!StringUtils.isEmpty(author.getAddress())) {
        node.setProperty(BookNodeTypes.EXO_AUTHOR_ADDRESS, author.getAddress());
      }
      if (!StringUtils.isEmpty(author.getPhone())) {
        node.setProperty(BookNodeTypes.EXO_AUTHOR_PHONE, author.getPhone());
      }
      node.getSession().save();
    } catch (RepositoryException re) {
      log.error(String.format("Author %s is not found", author.getName()), re);
      throw new BookNotFoundException(String.format("Author %s is not found", author.getName()));
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * get all book in workspace 
   * 
   * @return List<Book>
   */
  public List<Book> getAllBook() {
    /* create query string */
    StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_BOOK);
    sb.append(" order by " + BookNodeTypes.EXO_BOOK_NAME);
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      /* create QueryManager from session */
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      /* create Query object */
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      /* execute query and return result to QueryResult */
      QueryResult result = query.execute();
      /* transfer data to NodeIterator */
      NodeIterator nodes = result.getNodes();
      List<Book> books = new ArrayList<Book>();
      while (nodes.hasNext()) {
        Node node = nodes.nextNode();
        books.add(Utils.createBookByNode(node));
      }
      return books;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search book by book name
   * 
   * @param bookName The name of book
   * @return List<Book> The list of book
   */
  public List<Book> searchBookByName(String bookName) {
    /* create query string */
    StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_BOOK);
    if (!StringUtils.isEmpty(bookName)) {
      /* replace "" sign and - sign */
      bookName = bookName.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
      sb.append(" where CONTAINS(" + BookNodeTypes.EXO_BOOK_NAME + ",'" + bookName + "')");
    }
    sb.append(" order by " + BookNodeTypes.EXO_BOOK_NAME);
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      /* create QueryManager from session */
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      /* create Query object */
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      /* execute query and return result to QueryResult */
      QueryResult result = query.execute();
      /* transfer data to NodeIterator */
      NodeIterator nodes = result.getNodes();
      List<Book> books = new ArrayList<Book>();
      while (nodes.hasNext()) {
        Node node = nodes.nextNode();
        Book book = Utils.createBookByNode(node);
        books.add(book);
      }
      return books;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search book by author name
   * 
   * @param authorName The name of author
   * @return List<Book> The list of book
   */
  public List<Book> searchBookByAuthor(String authorName) {
    authorName.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    authorName = authorName.trim();
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = searchAuthorByName(authorName);
      String uuid = "";
      if (node != null) {
        uuid = node.getUUID();
      }
      StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_BOOK);
      sb.append(" where " + BookNodeTypes.EXO_AUTHOR + " like '%" + uuid + "%'");
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      List<Book> books = new ArrayList<Book>();
      while (nodes.hasNext()) {
        Node n = nodes.nextNode();
        Book book = new Book();
        book = Utils.createBookByNode(n);
        books.add(book);
      }
      return books;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search book by book name
   * 
   * @param bookName The name of book
   * @param username The name of user
   * @return List<Book> The list of book
   */
  public List<Book> searchBookByNameWithUser(String bookName, String username) {
    /* replace "" sign and - sign */
    bookName = bookName.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    bookName = bookName.trim();
    /* replace "" sign and - sign */
    username = username.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    username = username.trim();
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      
      /* create query string */
      StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_BOOK);
      sb.append(" where " + BookNodeTypes.EXO_BOOK_NAME + " = '" + bookName + "'");
      /* create QueryManager from session */
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      /* create Query object */
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      /* execute query and return result to QueryResult */
      QueryResult result = query.execute();
      /* transfer data to NodeIterator */
      NodeIterator nodes = result.getNodes();
      List<Book> books = new ArrayList<Book>();
      while (nodes.hasNext()) {
        Node node = nodes.nextNode();
        Book book = Utils.createBookByNode(node);
        books.add(book);
      }
      return books;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search author by author name
   * 
   * @param authorName The name of author
   * @return Node The node author
   */
  private Node searchAuthorByName(String authorName) {
    authorName.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    authorName = authorName.trim();
    StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_AUTHOR);
    sb.append(" where " + BookNodeTypes.EXO_AUTHOR_NAME + " like '%" + authorName + "%'");
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        return node;
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search author by author name
   * 
   * @param authorName The name of author
   * @return Node The node author
   */
  private Node searchUserByName(String username) {
    username.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    username = username.trim();
    StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_USER);
    sb.append(" where " + BookNodeTypes.EXO_USER_NAME + " like '%" + username + "%'");
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      if (nodes.getSize() > 0) {
        Node node = nodes.nextNode();
        return node;
      }
      return null;
    } catch (RepositoryException re) {
      log.error("Can not find user", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search book by name with limit of result(using SQL)
   * 
   * @param name The name of book
   * @return List<Book>
   */
  public List<Book> searchBookByNameLimitSQL(String name) {
    /* replace "" sign and - sign */
    name = name.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    /* create query string */
    StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_BOOK);
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      /* create QueryManager from session */
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      /* create QueryImpl object */
      QueryImpl query = (QueryImpl) queryManager.createQuery(sb.toString(), QueryImpl.SQL);
      /* set offset(which first record will be get) */
      query.setOffset(2);
      /* set limit(how many record will be get after offset) */
      query.setLimit(3);
      /* execute query and return result to QueryResult */
      QueryResult result = query.execute();
      /* transfer data to NodeIterator */
      NodeIterator nodes = result.getNodes();
      List<Book> books = new ArrayList<Book>();
      while (nodes.hasNext()) {
        Node node = nodes.nextNode();
        books.add(Utils.createBookByNode(node));
      }
      return books;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search book by name with like condition(using SQL)
   * 
   * @param name The name of book
   * @return List<Book>
   */
  public List<Book> searchBookByNameLikeSQL(String name) {
    /* replace "" sign and - sign */
    name.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    /* create query string */
    StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_BOOK);
    sb.append(" where " + BookNodeTypes.EXO_BOOK_NAME + " like '%" + name + "%'");
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      /* create QueryManager from session */
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      /* create Query object */
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      /* execute query and return result to QueryResult */
      QueryResult result = query.execute();
      /* transfer data to NodeIterator */
      NodeIterator nodes = result.getNodes();
      List<Book> books = new ArrayList<Book>();
      while (nodes.hasNext()) {
        Node node = nodes.nextNode();
        books.add(Utils.createBookByNode(node));        
      }
      return books;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * search book by name with like condition(using XPath)
   * 
   * @param name The name of book
   * @return List<Book>
   */
  public List<Book> searchBookByNameLikeXPath(String name) {
    /* replace "" sign and - sign */
    name.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    /* create query string */
    StringBuffer sb = new StringBuffer("//element(*," + BookNodeTypes.EXO_BOOK + ")[jcr:like(@"
        + BookNodeTypes.EXO_BOOK_NAME + ",'%" + name + "%')]");
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      /* create QueryManager from session */
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      /* create Query object */
      Query query = queryManager.createQuery(sb.toString(), Query.XPATH);
      /* execute query and return result to QueryResult */
      QueryResult result = query.execute();
      /* transfer data to NodeIterator */
      NodeIterator nodes = result.getNodes();
      List<Book> books = new ArrayList<Book>();
      while (nodes.hasNext()) {
        Node node = nodes.nextNode();
        books.add(Utils.createBookByNode(node));        
      }
      return books;
    } catch (RepositoryException re) {
      log.error("Can not find book", re);
      return null;
    } finally {
      sProvider.close();
    }
  }
  
  /**
   * check book exist by name
   * 
   * @param bookName The name of book
   * @param sProvider
   * @return boolean
   */
  private boolean isExistBookName(String bookName, SessionProvider sProvider) {
    /* replace "" sign and - sign */
    bookName.replace("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    /* create query string */
    StringBuffer queryString = new StringBuffer("select * from " + BookNodeTypes.EXO_BOOK);
    queryString.append(" where " + BookNodeTypes.EXO_BOOK_NAME + " = '" + bookName + "'");
    try {
      /* create QueryManager from session */
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      /* create Query object */
      Query query = queryManager.createQuery(queryString.toString(), Query.SQL);
      /* execute query and return result to QueryResult */
      QueryResult result = query.execute();
      /* transfer data to NodeIterator */
      NodeIterator iterator = result.getNodes();
      return iterator.hasNext();
    } catch (RepositoryException re) {
      log.error("Failed to check exist book name", re);
      return false;
    }
  }
  
  /**
   * check author exist by name
   * 
   * @param authorName
   * @param sProvider
   * @return
   */
  private boolean isExistAuthorName(String authorName, SessionProvider sProvider) {
    authorName.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    StringBuffer sb = new StringBuffer("Select * from " + BookNodeTypes.EXO_AUTHOR);
    sb.append(" where " + BookNodeTypes.EXO_AUTHOR_NAME + " = '" + authorName + "'");
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(sb.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      return nodes.hasNext();
    } catch (RepositoryException re) {
      log.error("Failed to check exist author name", re);
      return false;
    } 
  }
  
  /**
   * check user exist by name
   * 
   * @param username
   * @param sProvider
   * @return
   */
  private boolean isExistUserName(String username, SessionProvider sProvider) {
    username.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);
    StringBuffer queryString = new StringBuffer("Select * from " + BookNodeTypes.EXO_USER);
    queryString.append(" where " + BookNodeTypes.EXO_USER_NAME + " = '" + username + "'");
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(queryString.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator nodes = result.getNodes();
      return nodes.hasNext();
    } catch (RepositoryException re) {
      log.error("Failed to check exist user name", re);
      return false;
    }
  }
  
}
