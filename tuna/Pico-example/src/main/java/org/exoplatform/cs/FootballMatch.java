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
package org.exoplatform.cs;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 19, 2012  
 */
public class FootballMatch {
  private ArrayList<Footballer> footballers = new ArrayList<Footballer>();
  
  public FootballMatch() { super(); }
  
  public String kickOff() {
    Iterator<Footballer> it = footballers.iterator();
    String match = new String("- Kick off the match -");
    
    while (it.hasNext()) {
      match = match + it.next().play();    
    }
    
    return match;
  }
  
  public void addFootballer(Footballer footballer) {
    footballers.add(footballer);
  }
  
}
