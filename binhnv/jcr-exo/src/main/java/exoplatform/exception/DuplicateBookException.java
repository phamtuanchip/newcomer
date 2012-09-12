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
public class DuplicateBookException extends Exception {

  /**
   * The serialVersionUID generate
   * 
   */
  private static final long serialVersionUID = 5782743207363254715L;

  /**
   * DuplicateBookException constructor
   */
  public DuplicateBookException() {
    super();
  }

  /**
   * @param message
   */
  public DuplicateBookException(String message) {
    super(message);
  }

  /**
   * @param cause
   */
  public DuplicateBookException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   * @param cause
   */
  public DuplicateBookException(String message, Throwable cause) {
    super(message, cause);
  }

}
