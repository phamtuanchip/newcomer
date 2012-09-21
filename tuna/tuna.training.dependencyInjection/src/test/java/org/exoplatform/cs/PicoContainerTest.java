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

import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.DefaultPicoContainer;

import static org.junit.Assert.*;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 21, 2012  
 */
public class PicoContainerTest {
  
  @Test
  public void shouldLoadSimpleComponent() {
    MutablePicoContainer container = new DefaultPicoContainer();
    container.addComponent(Apple.class);
    
    Apple apple = (Apple) container.getComponent(Apple.class);

    assertEquals(apple.gotEaten(), "apple is eaten");
  }
  
  @Test
  public void shouldLoadDependentComponent() {
    MutablePicoContainer container = new DefaultPicoContainer();
    container.addComponent(Apple.class);
    container.addComponent(DependOnApple.class);
    
    DependOnApple dApple = (DependOnApple) container.getComponent(DependOnApple.class);
    assertEquals(dApple.say(), "i depend on apple");  
  }
  
  @Test 
  public void shouldAllowInterface() {
    MutablePicoContainer container = new DefaultPicoContainer();
    container.addComponent(Drinkable.class, Water.class);
    
    Drinkable water = (Drinkable) container.getComponent(Drinkable.class);
    assertEquals(water.drunk(), "drink water"); 
  }
  
  @Test
  public void shouldResolveComponentByParentContainer() {
    MutablePicoContainer container = new DefaultPicoContainer();
    
    // child declare his parent
    MutablePicoContainer childContainer = container.makeChildContainer();
    
    container.addComponent(Apple.class);
    childContainer.addComponent(DependOnApple.class);

    Apple apple = (Apple) childContainer.getComponent(Apple.class);
    assertEquals(apple.gotEaten(), "apple is eaten");
    DependOnApple dApple = (DependOnApple) childContainer.getComponent(DependOnApple.class);
    assertEquals(dApple.say(), "i depend on apple");  
  }
}
