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

//import org.picocontainer.DefaultPicoContainer;
//import org.picocontainer.MutablePicoContainer;
//import org.picocontainer.PicoCompositionException;

import junit.framework.TestCase;
import org.exoplatform.container.*;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 19, 2012  
 */

public class ExoContainerTest extends TestCase {
  //private Footballer footballer;
  private FootballMatch match;
  private StandaloneContainer container;
  
  public void setUp() throws Exception {
    container = StandaloneContainer.getInstance();
    match = (FootballMatch) 
        container.getComponentInstanceOfType(FootballMatch.class);
  }
  
  public void testShouldGetComponentFromExternalPlugin() {
    System.out.println(match.kickOff());
    assertEquals(true, match.kickOff()
                 .equals("- Kick off the match -- Messi is kicking the ball -"));
  }
  
  public void testShouldGetSimpleComponent() {
    Footballer cro = (Footballer) 
        container.getComponentInstanceOfType(Footballer.class);
    match.addFootballer(cro);
   
    System.out.println(match.kickOff());
    assertEquals(true, match.kickOff()
                 .equals("- Kick off the match -- CRo is kicking the ball -"));
  }
  
  
  
  /**
  @Test(expected=PicoCompositionException.class)
  public void shouldProvokeExceptionForDuplicateKey() {
    MutablePicoContainer container = new DefaultPicoContainer();
    container.addComponent(FootballMatch.class)
             .addComponent(Footballer.class, CRo.class)
             .addComponent(Footballer.class, Messi.class);
  }
  
  @Test
  public void shouldSeeParentComponentInChildContainer() {
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
  */
}
