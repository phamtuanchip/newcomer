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
package org.exoplatform.bookstore.specification;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 16, 2012  
 */
public class BookSpecification 
{  
  private String baseQuery = "SELECT * FROM nt:base WHERE ";
  
  private String conditionQuery;
  
  public BookSpecification(String conditionQuery)
  {
    this.conditionQuery = conditionQuery;
  }
  
  public String getConditionQuery()
  {
    return conditionQuery;
  }
  
  public String getQuery()
  {
    return baseQuery + conditionQuery;
  }
  
  public BookSpecification or(BookSpecification spec)
  {
    return new BookSpecification(this.getConditionQuery() + "OR " + spec.getConditionQuery());
  }
}
