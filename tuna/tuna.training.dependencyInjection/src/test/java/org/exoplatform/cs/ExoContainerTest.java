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
import static org.junit.Assert.assertEquals;

import org.exoplatform.container.*;
import org.junit.Test;

public class ExoContainerTest {
  
  @Test
  public void shouldGetSimpleComponent() throws Exception {
    StandaloneContainer container = StandaloneContainer.getInstance();;    
    Drinkable water = (Drinkable) 
      container.getComponentInstanceOfType(Drinkable.class);
    assertEquals(water.drunk(), "drink water"); 
  }
  
  @Test
  public void shouldUseExternalConfigToLoadComponent() throws Exception { 
    StandaloneContainer container = StandaloneContainer.getInstance();;    
    Juice juice = (Juice)
        container.getComponentInstance(Juice.class);
    assertEquals(juice.drunk(), "drink orange juice"); 
  }
}
