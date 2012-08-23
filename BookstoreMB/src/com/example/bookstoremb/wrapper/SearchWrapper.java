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
package com.example.bookstoremb.wrapper;

import com.example.bookstoremb.R;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Aug 1, 2012  
 */
public class SearchWrapper implements OnItemSelectedListener {

  private View base;
  private TextView bookName;
  private String bookNameStr;
  
  /**
   * 
   */
  public SearchWrapper(View base, Context context) {
    this.base = base;
    bookName = (TextView) base.findViewById(R.id.bookName);
  }

  /* (non-Javadoc)
   * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
   */
  @Override
  public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

  }

  /* (non-Javadoc)
   * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
   */
  @Override
  public void onNothingSelected(AdapterView<?> arg0) {
    
  }

  /**
   * @return the bookNameStr
   */
  public String getBookNameStr() {
    if (bookNameStr == null) {
      bookNameStr = bookName.getText().toString();
    }
    return bookNameStr;
  }

}
