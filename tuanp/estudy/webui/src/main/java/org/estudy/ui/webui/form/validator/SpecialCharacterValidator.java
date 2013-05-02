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
package org.estudy.ui.webui.form.validator;

import org.estudy.ui.webui.UIBookSearchForm;
import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.form.UIFormInput;
import org.exoplatform.webui.form.validator.Validator;

/**
 * Created by The eXo Platform SAS
 * Author : Anh-Tu Nguyen
 *          tuna@exoplatform.com
 * Oct 16, 2012  
 */
@Serialized
public class SpecialCharacterValidator implements Validator
{
  public void validate(@SuppressWarnings("rawtypes") UIFormInput uiInput) throws Exception
  {
     if (uiInput.getValue() == null || ((String)uiInput.getValue()).trim().length() == 0)
        return;
     UIComponent uiComponent = (UIComponent) uiInput;
     
     String s = (String)uiInput.getValue();
     for (int i = 0; i < s.length(); i++)
     {
        char c = s.charAt(i);
        if (Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == '-' || Character.isSpaceChar(c))
        {
           continue;
        }
        
        uiComponent.getAncestorOfType(UIBookSearchForm.class).showMessageBoxWith("You should enter alphabet character ");
        
        //throw new MessageException(new ApplicationMessage("SpecialCharacterValidator.msg.Invalid-char", args,
        //   ApplicationMessage.WARNING));
        
     }
  }
}
