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
package exoplatform.utils;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import exoplatform.BookNodeTypes;
import exoplatform.entity.Author;
import exoplatform.entity.Book;
import exoplatform.entity.User;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 5, 2012  
 */
public class Utils {

  /** 
   * Define a static variable which contain id of book 
   * id of book will increase 1 value when a new book create
   * 
   **/
  public static int bookId = 0;
  
  /** 
   * Define a static variable which contain id of author 
   * id of author will increase 1 value when a new author create
   * 
   **/
  public static int authorId = 0;
  
  /** 
   * Define a static variable which contain id of user 
   * id of user will increase 1 value when a new user create
   * 
   **/
  public static int userId = 0;
  
  /**
   * Convert enum value to String value 
   * 
   * @param category Enum value of category
   * @return String value of category
   */
  public static String bookCategoryEnumToString(Book.CATEGORY category) {
    if (category != null) {
      return category.toString();
    }
    return null;
  }
  
  /**
   * Convert String value to enum value
   * 
   * @param category String value of category
   * @return Book.CATEGORY of category
   */
  public static Book.CATEGORY bookCategoryStringToEnum(String category) {
    return category != null ? Book.CATEGORY.valueOf(category) : null;
  }
  
  /**
   * Create book by node data
   * 
   * @param node
   * @return
   * @throws RepositoryException
   */
  public static Book createBookByNode(Node node) throws RepositoryException {
    if (node != null) {
      Book book = new Book();
      try {
        book.setBookId(node.getName());
      } catch (Exception e) {
        return null;
      }
      PropertyReader reader = new PropertyReader(node);
      book.setCategory(Utils.bookCategoryStringToEnum(reader.string(BookNodeTypes.EXO_BOOK_CATEGORY)));
      book.setName(reader.string(BookNodeTypes.EXO_BOOK_NAME));
      book.setContent(reader.string(BookNodeTypes.EXO_BOOK_CONTENT));
      return book;
    }
    return null;
  }
  
  /**
   * Create user by node data
   * 
   * @param node
   * @return
   * @throws RepositoryException
   */
  public static User createUserByNode(Node node) throws RepositoryException {
    if (node != null) {
      User user = new User();
      try {
        user.setUserId(node.getName());
      } catch (RepositoryException re) {
        return null;
      }
      PropertyReader reader = new PropertyReader(node);
      user.setUsername(reader.string(BookNodeTypes.EXO_USER_NAME));
      user.setPassword(reader.string(BookNodeTypes.EXO_USER_PASSWORD));
      user.setFullname(reader.string(BookNodeTypes.EXO_USER_FULLNAME));
      user.setAddress(reader.string(BookNodeTypes.EXO_USER_ADDRESS));
      user.setPhone(reader.string(BookNodeTypes.EXO_USER_PHONE));
      return user;
    }
    return null;
  }
  
  /**
   * Create author by node data
   * 
   * @param node
   * @return
   * @throws RepositoryException
   */
  public static Author createAuthorByNode(Node node) throws RepositoryException {
    if (node != null) {
      Author author = new Author();
      try {
        author.setAuthorId(node.getName());
      } catch (RepositoryException re) {
        return null;
      }
      PropertyReader reader = new PropertyReader(node);
      author.setName(reader.string(BookNodeTypes.EXO_AUTHOR_NAME));
      author.setPhone(reader.string(BookNodeTypes.EXO_AUTHOR_PHONE));
      author.setAddress(reader.string(BookNodeTypes.EXO_AUTHOR_ADDRESS));
      return author;
    }
    return null;
  }
  
}
