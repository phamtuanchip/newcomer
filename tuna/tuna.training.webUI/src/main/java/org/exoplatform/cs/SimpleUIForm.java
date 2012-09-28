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

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInputInfo;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu NGUYEN
 *          tuna@exoplatform.com
 * Sep 28, 2012  
 */

@ComponentConfig
(
   lifecycle = UIFormLifecycle.class,
   template  = "app://groovy/webui/SimpleUIForm.gtmpl"
)

public class SimpleUIForm extends UIForm {
  
  private static final Log log = ExoLogger.getLogger(SimpleUIForm.class) ;
  
  public SimpleUIForm() throws Exception 
  {
    try {
      this.addChild(new UIFormInputInfo(
        "Input Info", "Input Info", "This is a message"                                         
        ));
    }
    catch (Exception e) {
      log.info("\n SimpleUIForm constructor exception: \n");
    }
  }
  
  @Override
  public void processRender(WebuiRequestContext context) throws Exception {
    
    log.info("\n SimpleUIForm: processRender \n");
    
    super.processRender(context);
  }
}
