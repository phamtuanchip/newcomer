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
package org.exoplatform.bookstore.domain;

import org.exoplatform.services.jcr.util.IdGenerator;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 2, 2012  
 */
public class Book {
  
  private String isbn; // used for book identification
  
  private String title;
  
  private Author author;
  
  public Book(String isbn, String title, Author author) 
  {
    this.isbn = isbn;
    this.title = title;
    if (author == null) 
      this.author = new NoAuthor();
    else 
      this.author = author;
  }

  public String getIsbn() 
  {
    return isbn;
  }

  public void setIsbn(String isbn)
  {
    this.isbn = isbn;
  }
  
  public String getTitle() 
  {
    return title;
  }

  public void setTitle(String title) 
  {
    this.title = title;
  }
  
  public Author getAuthor() 
  {
    return author;
  }

  
  public void setAuthor(Author author) 
  {
    this.author = author;
  }
}
