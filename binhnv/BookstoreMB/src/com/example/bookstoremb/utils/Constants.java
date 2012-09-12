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

import android.view.Menu;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 27, 2012  
 */
public class Constants {
  
  //define menu constant
  public static final int MENU_EDIT = Menu.FIRST + 1;
  public static final int MENU_BACK = Menu.FIRST + 2;
  public static final int MENU_CLOSE = Menu.FIRST + 3;
  public static final int MENU_SEARCH = Menu.FIRST + 4;
  public static final int MENU_ADD_IP = Menu.FIRST + 5;
  
  //define few name of view
  public static final String BOOK_ID = "bookId";
  public static final String BOOK_NAME = "name";
  public static final String BOOK_CATEGORY = "category";
  public static final String BOOK_CONTENT = "content";
  public static final String SEARCH_CONDITION = "search_condition";
  
  //define url
  public static final String HTTP = "http://";
  public static final String SEARCH_ALL_BOOK_URL = "/rest/private/bookstore/searchAllBook";
  public static final String SEARCH_BOOK_BY_NAME = "/rest/private/bookstore/searchBookByName/";
  public static final String SEARCH_AUTHOR = "/rest/private/bookstore/searchAuthorByBookId/";
  
  //define username and password for web service
  public static final String USERNAME = "root";
  public static final String PASSWORD = "gtn";
  
  public static final String PREFS_IP = "prefs_ip";
  public static final String PREFS_IP_VALUE = "prefs_ip_value";
  
  public static final String SEARCHING = "searching";
  public static final String SEARCHING_VAL = "searching_val";
  
}
