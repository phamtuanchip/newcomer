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
package org.estudy.learning.domain;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Oct 3, 2012  
 */
public class NoAuthor extends Author {

  public static String id = null;
  
  private static String name = "No Author";

  public NoAuthor() {}
  
  @Override
  public String getId() 
  {
    return id;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
}
