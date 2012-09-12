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
package exoplatform.bookstore.webui;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTextAreaInput;
import org.exoplatform.webui.form.validator.MandatoryValidator;

import exoplatform.bookstore.utils.Constants;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Aug 8, 2012  
 */
public class UIBookInformation extends UIFormInputSet {
  
  public static final String BOOKID = "bookId";
  public static final String BOOKNAME = "name";
  public static final String CATEGORY = "category";
  public static final String CONTENT = "content";
  
  public UIBookInformation(String name) throws Exception {
    super(name);
    //create select box
    SelectItemOption<String> categoryNOVEL = new SelectItemOption<String>(Constants.CATEGORY_NOVEL, Constants.CATEGORY_NOVEL_VALUE);
    SelectItemOption<String> categoryMANGA = new SelectItemOption<String>(Constants.CATEGORY_MANGA, Constants.CATEGORY_MANGA_VALUE);
    SelectItemOption<String> categoryCOMICS = new SelectItemOption<String>(Constants.CATEGORY_COMICS, Constants.CATEGORY_COMICS_VALUE);
    SelectItemOption<String> categoryTECHNICAL = new SelectItemOption<String>(Constants.CATEGORY_TECHNICAL, Constants.CATEGORY_TECHNICAL_VALUE);
    SelectItemOption<String> categoryMATHS = new SelectItemOption<String>(Constants.CATEGORY_MATHS, Constants.CATEGORY_MATHS_VALUE);
    SelectItemOption<String> categoryHISTORY = new SelectItemOption<String>(Constants.CATEGORY_HISTORY, Constants.CATEGORY_HISTORY_VALUE);
    
    List<SelectItemOption<String>> categoryList = new ArrayList<SelectItemOption<String>>();
    categoryList.add(categoryNOVEL);
    categoryList.add(categoryMANGA);
    categoryList.add(categoryCOMICS);
    categoryList.add(categoryTECHNICAL);
    categoryList.add(categoryMATHS);
    categoryList.add(categoryHISTORY);
    
    UIFormSelectBox uiFormSelectBox = new UIFormSelectBox(CATEGORY, CATEGORY, categoryList);
    
    addUIFormInput(new UIFormStringInput(BOOKNAME, BOOKNAME, null).addValidator(MandatoryValidator.class));
    
    addUIFormInput(uiFormSelectBox);
    
    addUIFormInput(new UIFormTextAreaInput(CONTENT, CONTENT, null).addValidator(MandatoryValidator.class));
  }
  
}
