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
package org.exoplatform.social.iocpractice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : vietnq
 *          vietnq@exoplatform.com
 * May 4, 2012  
 */
public class AClass {
  
  private List<SampleListener> listeners = new ArrayList<SampleListener>();
  
  private BClass b;
  
  public AClass(BClass b) {
    this.b = b;
  }
  
  public BClass getB() {
    return b;
  }
  
  /*
   * each time an instance of AClass created, adds a listener
   * see conf/iocpractice-configuration.xml, section external-plugin for more details 
   */
  public void addListener(SampleListener listener) {
    listeners.add(listener);
  }
  
  /*
   * return number of listeners
   */
  public int getNumberOfListener() {
    return listeners.size();
  }
  
  //for each new sample listener added,execute action listener
  public void newListenerAction() {
    for(SampleListener listener : listeners) {
      listener.execute();
    }
  }
}
