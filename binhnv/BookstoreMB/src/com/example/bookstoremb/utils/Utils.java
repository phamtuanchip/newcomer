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
package com.example.bookstoremb.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.bookstoremb.models.Author;
import com.example.bookstoremb.models.Book;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 5, 2012  
 */
public class Utils {

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
   * create book from json
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static Book createBookFromJSON(JSONObject json) throws JSONException {
    Book book = new Book();
    book.setBookId(json.getString("bookId"));
    book.setName(json.getString("name"));
    book.setCategory(Utils.bookCategoryStringToEnum(json.getString("category")));
    book.setContent(json.getString("content"));
    return book;
  }
  
  /**
   * create author from json
   * 
   * @param json
   * @return
   * @throws JSONException
   */
  public static Author createAuthorFromJSON(JSONObject json) throws JSONException {
    Author author = new Author();
    author.setAuthorId(json.getString("authorId"));
    author.setName(json.getString("name"));
    author.setAddress(json.getString("address"));
    author.setPhone(json.getString("phone"));
    return author;
  }
  
}