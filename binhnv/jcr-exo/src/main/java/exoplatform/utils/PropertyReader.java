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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 6, 2012  
 */
public class PropertyReader {

  Node node = null;
  
  /**
   * PropertyReader constructor
   * @param node 
   * 
   */
  public PropertyReader(Node node) {
    this.node = node;
  }
  
  /**
   * get property of node 
   * and change to Double type 
   * 
   * @param name The name of node
   * @return Double value
   * @throws RepositoryException
   */
  public Double d(String name) throws RepositoryException {
    return node.getProperty(name).getDouble();
  }
  
  /**
   * get property of node 
   * and change to Long type
   * 
   * @param name The name of node
   * @return Long value
   * @throws RepositoryException
   */
  public Long l(String name) throws RepositoryException {
    return node.getProperty(name).getLong();
  }
  
  /**
   * get property of node
   * and change to String type
   * 
   * @param name The name of node
   * @return String value
   * @throws RepositoryException
   */
  public String string(String name) throws RepositoryException {
    return node.getProperty(name).getString();
  }
  
  /**
   * get property of node
   * and change to Date type
   * 
   * @param name The name of node
   * @return Date value
   * @throws RepositoryException
   */
  public Date date(String name) throws RepositoryException {
    return node.getProperty(name).getDate().getTime();
  }
  
  /**
   * get property of node
   * and change to Boolean type
   * 
   * @param name The name of node
   * @return Boolean value
   * @throws RepositoryException
   */
  public Boolean bool(String name) throws RepositoryException {
    return node.getProperty(name).getBoolean();
  }
  
  /**
   * get value properties of node 
   * and change to String array
   * 
   * @param name The name of node
   * @return String array
   * @throws RepositoryException
   */
  public String[] strings(String name) throws RepositoryException {
      return valueToArray(node.getProperty(name).getValues());
  }
  
  /**
   * get value properties of node
   * and change to List<String>
   * 
   * @param name The name of node
   * @return List<String>
   * @throws RepositoryException
   */
  public List<String> list(String name) throws RepositoryException {
    Value[] values = node.getProperty(name).getValues();
    return valueToList(values);
  }
  
  /**
   * get value properties of node
   * and change to Set<String>
   * 
   * @param name The name of node
   * @return Set<String>
   * @throws RepositoryException
   */
  public Set<String> set(String name) throws RepositoryException {
      Value[] values = node.getProperty(name).getValues();
      Set<String> result = new HashSet<String>();
      result.addAll(valueToList(values));
      return result;
  }
  
  /**
   * get value properties of node
   * and change to String array
   * 
   * @param values The array of value
   * @return String array
   * @throws RepositoryException
   */
  private String[] valueToArray(Value[] values) throws RepositoryException {
    if (values.length < 1) {
      return new String[] {};
    }
    List<String> list = valueToList(values);
    return list.toArray(new String[list.size()]);
  }
  
  /**
   * get value properties of node
   * and return List<String>
   * @param values The Value array
   * @return List<String>
   * @throws RepositoryException
   */
  private List<String> valueToList(Value[] values) throws RepositoryException {
    List<String> list = new ArrayList<String>();
    if (values.length < 1) {
      return list;
    }
    String s = "";
    for (Value value : values) {
      s = value.getString();
      if (s != null && s.trim().length() > 0) {
        list.add(s);
      }
    }
    return list;
  }

}
