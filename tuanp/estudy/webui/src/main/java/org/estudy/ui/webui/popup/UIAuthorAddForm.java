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

import org.estudy.learning.domain.Author;
import org.estudy.learning.exception.DuplicateAuthorException;
import org.estudy.ui.util.BookstoreUtil;
import org.estudy.ui.webui.UIBookMessage;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 18, 2012  
 */
@ComponentConfig(
  lifecycle = UIFormLifecycle.class,
  template  = "system:/groovy/webui/form/UIForm.gtmpl",
  events    = {
    @EventConfig(listeners = UIAuthorAddForm.AddAuthorActionListener.class)
  }
)

public class UIAuthorAddForm extends UIForm implements UIPopupComponent
{
  private static final Log log = ExoLogger.getExoLogger(UIAuthorAddForm.class);
  
  private static final String ADD_AUTHOR_NAME = "authorName";
  
  private static final String MESSAGE         = "message";
  
  public UIAuthorAddForm() throws Exception
  {
    addChild(new UIFormStringInput(ADD_AUTHOR_NAME , ADD_AUTHOR_NAME, null ));
    
    addMessageBox();
  }
  
  private void addMessageBox() throws Exception 
  {
    UIBookMessage messageBox = new UIBookMessage(MESSAGE, MESSAGE, null);
    messageBox.setRendered(false);
    addChild(messageBox);
  }
  
  public String getAuthorNameFromUI() throws Exception
  {
    return getUIStringInput(ADD_AUTHOR_NAME).getValue();
  }
  
  public void resetForm() throws Exception
  {
    getUIStringInput(ADD_AUTHOR_NAME).reset();
  }
  
  public void showMessageBoxWith(String messageToDisplay)
  {
    getChild(UIBookMessage.class).setValue(messageToDisplay);
    getChild(UIBookMessage.class).setRendered(true);
  }
  
  @Override
  public void activate() throws Exception {}

  @Override
  public void deActivate() throws Exception {}
  
  static public class AddAuthorActionListener extends EventListener<UIAuthorAddForm>
  {
    @Override
    public void execute(Event<UIAuthorAddForm> event) throws Exception {
      log.info("--- Add Author Event received ---");
      
      UIAuthorAddForm authorAddForm = event.getSource();
      
      try 
      {
        BookstoreUtil.addAuthorToStorage( new Author(authorAddForm.getAuthorNameFromUI()) );
      }
      catch (DuplicateAuthorException e)
      {
        authorAddForm.showMessageBoxWith("Author " + authorAddForm.getAuthorNameFromUI() +
            " already exists, add a new one");
        return ;
      }
      catch (Exception e)
      {
        log.error("Add author exception " + e.getMessage());
      }
      
      UIBookAddForm bookAddForm = authorAddForm.getAncestorOfType(UIBookAddManager.class)
          .getChild(UIBookAddForm.class);
      bookAddForm.updateUIAuthorList();
      event.getRequestContext().addUIComponentToUpdateByAjax(bookAddForm);
      
      authorAddForm.showMessageBoxWith("Author " + authorAddForm.getAuthorNameFromUI() +
          " added successfully");
      authorAddForm.resetForm();
      event.getRequestContext().addUIComponentToUpdateByAjax(authorAddForm);
    }
    
  }  
}
