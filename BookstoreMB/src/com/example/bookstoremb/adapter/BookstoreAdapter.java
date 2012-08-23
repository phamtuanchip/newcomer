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
package com.example.bookstoremb.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookstoremb.R;
import com.example.bookstoremb.models.Book;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 30, 2012  
 */
public class BookstoreAdapter extends BaseAdapter {

  //define context
  private Context context;
  //define list of book which will be show book name to screen
  private List<Book> arr = new ArrayList<Book>();
  //define layout inflater which allow create view
  private static LayoutInflater inflater;
  
  /**
   * BookstoreAdapter constructor
   */
  public BookstoreAdapter(Context context, List<Book> arr) {
    this.context = context;
    this.arr = arr;
    inflater = LayoutInflater.from(context);
  }

  /* (non-Javadoc)
   * @see android.widget.Adapter#getCount()
   */
  @Override
  public int getCount() {
    return arr.size();
  }

  /* (non-Javadoc)
   * @see android.widget.Adapter#getItem(int)
   */
  @Override
  public Object getItem(int position) {
    return position;
  }

  /* (non-Javadoc)
   * @see android.widget.Adapter#getItemId(int)
   */
  @Override
  public long getItemId(int position) {
    return position;
  }

  /* (non-Javadoc)
   * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
   */
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.row, null);
      holder = new ViewHolder();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.textView = (TextView) convertView.findViewById(R.id.label);
    holder.textView.setText(arr.get(position).getName());
    RelativeLayout listRow = (RelativeLayout) convertView.findViewById(R.id.list_row); 
//    if (position == 0) {
//      listRow.setBackgroundResource(R.drawable.celltop);
//      listRow.setBackgroundColor(Color.RED);
//    }
//    if (position == (arr.size() - 1)) {
//      listRow.setBackgroundResource(R.drawable.cellbottom);
//      listRow.setBackgroundColor(Color.GREEN);
//    }
    return convertView;
  }
  
  /**
   * define view holder class which contain text view for list
   * 
   * @author binhnv
   *
   */
  public static class ViewHolder {
    public TextView textView;
  }

}
