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

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 19, 2012  
 */
public class WatchFootballMatch {
  private MutablePicoContainer container;
  
  public void watch() {
    initializeContainer();
    registrerComponent();
    
    FootballMatch footballMatch = (FootballMatch)
        container.getComponent(FootballMatch.class);
    footballMatch.kickOff();
  }
  
  private void registrerComponent() {
    container.addComponent(FootballMatch.class);
    container.addComponent(Footballer.class, CRo.class);
    // container.addComponent(Footballer.class, Messi.class); - should provoke Exception for 
    // duplicate key for Footballer.class
    
  }
  
  private void initializeContainer() {
    this.container = new DefaultPicoContainer(); 
  }
  
  public WatchFootballMatch() {
    super();
  }
  
  public static void main(String[] args) {
    WatchFootballMatch w = new WatchFootballMatch();
    w.watch();
  }
}
