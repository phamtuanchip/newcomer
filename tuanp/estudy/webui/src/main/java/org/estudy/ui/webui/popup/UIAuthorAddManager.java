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
package org.estudy.ui.webui.popup;

import java.io.Writer;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 18, 2012  
 */
@ComponentConfig(
  lifecycle = UIContainerLifecycle.class
)

public class UIAuthorAddManager extends UIContainer implements UIPopupComponent
{
  public UIAuthorAddManager() throws Exception
  {
    //addChild(UIAuthorAddForm.class, null, null);
  }

  @Override
  public void activate() throws Exception { }

  @Override
  public void deActivate() throws Exception { }

  @Override
  public void processRender(WebuiRequestContext context) throws Exception 
  {
    Writer w = context.getWriter();
    w.write("<div id=\"UIAuthorAddManager\" class=\"UIAuthorAddManager\">");
    renderChildren();
    w.write("</div>");
  }
}
