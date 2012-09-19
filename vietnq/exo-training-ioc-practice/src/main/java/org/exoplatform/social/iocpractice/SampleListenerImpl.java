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

/**
 * Created by The eXo Platform SAS
 * Author : vietnq
 *          vietnq@exoplatform.com
 * May 4, 2012  
 */
public class SampleListenerImpl implements SampleListener {

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setName(String s) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDescription(String s) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void execute() {
    System.out.println("+==============================================+");
    System.out.println("+         One More Sample Listener added       +");
    System.out.println("+==============================================+");
    
  }

}
