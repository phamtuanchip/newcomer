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

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 21, 2012  
 */

public class PicoContainerTest {

  @Test(expected=PicoCompositionException.class)
  public void testShouldProvokeExceptionForDuplicateKey() {
    MutablePicoContainer container = new DefaultPicoContainer();
    try {
      container.addComponent(FootballMatch.class)
               .addComponent(Footballer.class, CRo.class)
               .addComponent(Footballer.class, Messi.class);
    } catch (PicoCompositionException e) {}
  }
  
  
  public void testShouldSeeParentComponentInChildContainer() {
    MutablePicoContainer container = new DefaultPicoContainer();
    
    container.addComponent(FootballMatch.class)
             .addComponent(Footballer.class, CRo.class);
    
    MutablePicoContainer childContainer = 
      container.addChildContainer(new DefaultPicoContainer());
    
    FootballMatch footballMatch = (FootballMatch) childContainer.getComponent(FootballMatch.class);
    footballMatch.kickOff();
    
    assertTrue("Same class", FootballMatch.class.getName().equals(
      footballMatch.getClass().getName()));
  }
 
  @Test
  public void shouldAllowChildToAddDependentComponent() {
    MutablePicoContainer container = new DefaultPicoContainer();
    MutablePicoContainer childContainer = 
        container.addChildContainer(new DefaultPicoContainer());
    
    container.addComponent(Footballer.class, CRo.class);
    childContainer.addComponent(FootballMatch.class);
    
    FootballMatch footballMatch = (FootballMatch) childContainer.getComponent(FootballMatch.class);
    footballMatch.kickOff();
    
    assertTrue("Match should kick off", true);
  }
}
