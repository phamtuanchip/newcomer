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

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 5, 2012  
 */
public interface BookNodeTypes {

  /* define parent node of book */
  public static final String EXO_BOOK = "exostore:book";
  /* exostore:bookname is a property of exostore:book */
  public static final String EXO_BOOK_NAME = "exostore:bookname";
  /* exostore:bookcategory is a property of exostore:book */
  public static final String EXO_BOOK_CATEGORY = "exostore:bookcategory";
  /* exostore:bookcontent is a property of exostore:book */
  public static final String EXO_BOOK_CONTENT = "exostore:bookcontent";
  /* exostore:bookcontent is a property of exostore:book */
  public static final String EXO_BOOK_AUTHOR = "exostore:author";
  
  /* define parent node of author */
  public static final String EXO_AUTHOR = "exostore:author";
  /* exostore:authorname is a property of exostore:author */
  public static final String EXO_AUTHOR_NAME = "exostore:authorname";
  /* exostore:authoraddress is a property of exostore:author */
  public static final String EXO_AUTHOR_ADDRESS = "exostore:authoraddress";
  /* exostore:authorphone is a property of exostore:author */
  public static final String EXO_AUTHOR_PHONE = "exostore:authorphone";
  
  /* define parent node of user */
  public static final String EXO_USER = "exostore:user";
  /* exostore:username is a property of exostore:user */
  public static final String EXO_USER_NAME = "exostore:username";
  /* exostore:password is a property of exostore:user */
  public static final String EXO_USER_PASSWORD = "exostore:password";
  /* exostore:fullname is a property of exostore:user */
  public static final String EXO_USER_FULLNAME = "exostore:fullname";
  /* exostore:useraddress is a property of exostore:user */
  public static final String EXO_USER_ADDRESS = "exostore:useraddress";
  /* exostore:userphone is a property of exostore:user */
  public static final String EXO_USER_PHONE = "exostore:userphone";
  
}
