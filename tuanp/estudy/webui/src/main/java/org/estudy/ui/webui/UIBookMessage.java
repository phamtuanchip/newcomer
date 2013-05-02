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
package org.estudy.ui.webui;

import java.io.Writer;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.commons.utils.HTMLEntityEncoder;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.form.UIFormInputBase;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 16, 2012  
 */

@Serialized
public class UIBookMessage extends UIFormInputBase<String>
{
  private static final Log log = ExoLogger.getExoLogger(UIBookMessage.class);
      
  public UIBookMessage(String name, String bindingExpression, String value)
  {
    super(name, bindingExpression, String.class);
    this.value_ = value;
    readonly_ = true;
  }

  @SuppressWarnings("unused")
  public void decode(Object input, WebuiRequestContext context) throws Exception
  {
  }

  public void processRender(WebuiRequestContext context) throws Exception
  {
     Writer w = context.getWriter();
     w.append("<div id=\"").append(getId()).append("\" class=\"").append(getId()).append("\">");
     String value = getValue();
     if (value != null)
     {
        value = HTMLEntityEncoder.getInstance().encode(value);
        w.write(value);
     }
     w.write("</div>");
  }
}
