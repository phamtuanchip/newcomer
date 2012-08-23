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
package com.example.bookstoremb.models;


/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 5, 2012  
 */
public class Book {

  /* Define book category */
  public enum CATEGORY {
    NOVEL, MANGA, COMICS, TECHNICAL, MATHS, HISTORY
  }
  /* id of book */
  private String bookId;
  /* name of book */
  private String name;
  /*category of book*/
  private CATEGORY category;
  /* content of book */
  private String content;
  
  /**
   * Book constructor
   * 
   */
  public Book() {
    
  }
  
  /**
   * Book constructor
   * 
   * @param name The name of book
   * @param category The category of book
   * @param content The content of book
   * 
   */
  public Book(String name, CATEGORY category, String content) {
    this.name = name;
    this.category = category;
    this.content = content;
  }
  
  
  /**
   * @return the name
   */
  public String getName() {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * @return the category
   */
  public CATEGORY getCategory() {
    return category;
  }
  /**
   * @param category the category to set
   */
  public void setCategory(CATEGORY category) {
    this.category = category;
  }
  /**
   * @return the content
   */
  public String getContent() {
    return content;
  }
  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the bookId
   */
  public String getBookId() {
    return bookId;
  }

  /**
   * @param bookId the bookId to set
   */
  public void setBookId(String bookId) {
    this.bookId = bookId;
  }
  
}
