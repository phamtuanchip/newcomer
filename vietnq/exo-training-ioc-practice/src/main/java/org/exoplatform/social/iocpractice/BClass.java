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

import org.exoplatform.container.xml.InitParams;


/**
 * Created by The eXo Platform SAS
 * Author : vietnq
 *          vietnq@exoplatform.com
 * May 4, 2012  
 */
public class BClass {
  public String test;
  private int b1;
  private int b2;
  
  public BClass(InitParams params) {
    test = "TEST";
    this.b1 = Integer.valueOf(params.getValueParam("B1").getValue());
    this.b2 = Integer.valueOf(params.getValueParam("B2").getValue());
  }
  
  public int getB1() {
    return b1;
  }
  
  public int getB2() {
    return b2;
  }
}
