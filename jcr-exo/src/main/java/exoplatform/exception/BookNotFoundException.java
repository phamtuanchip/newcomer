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
package exoplatform.exception;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 5, 2012  
 */
public class BookNotFoundException extends Exception {

  /**
   * The serialVersionUID generate
   */
  private static final long serialVersionUID = -6563284439822341457L;

  /**
   * BookNotFoundException constructor
   */
  public BookNotFoundException() {
    super();
  }

  /**
   * @param message
   */
  public BookNotFoundException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public BookNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public BookNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
