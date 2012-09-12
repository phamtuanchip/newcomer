/**
 * 
 */
package com.example.bookstoremb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bookstoremb.models.Author;
import com.example.bookstoremb.models.Book;
import com.example.bookstoremb.utils.Constants;
import com.example.bookstoremb.utils.RestClient;
import com.example.bookstoremb.utils.Utils;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 5, 2012  
 */
public class ContentActivity extends Activity {

  //create book for show information
  private Book book;
  //create search condition for keep condition for return to search screen
  private String searchCondition;
  //The ip address
  private String ip;
  //The name of book
  private String bookName;
  
  /**
   * create main screen for app
   */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		
		//create view for content activity
		TextView name = (TextView) findViewById(R.id.name);
		TextView content = (TextView) findViewById(R.id.content);
		TextView category = (TextView) findViewById(R.id.category);
		TextView author = (TextView) findViewById(R.id.author);
		
		SharedPreferences settings = getSharedPreferences(Constants.PREFS_IP, 0);
    ip = settings.getString(Constants.PREFS_IP_VALUE, null);
    
		//check extras from intent
		Bundle extras = getIntent().getExtras();
		book = new Book();
		if (extras != null) {
		  book.setBookId(extras.getString(Constants.BOOK_ID));
		  book.setName(extras.getString(Constants.BOOK_NAME));
		  book.setCategory(Utils.bookCategoryStringToEnum(extras.getString(Constants.BOOK_CATEGORY)));
		  book.setContent(extras.getString(Constants.BOOK_CONTENT));
		  searchCondition = extras.getString(Constants.SEARCH_CONDITION);
		  bookName = extras.getString(Constants.SEARCHING);
    }
		
		//set data for view
		name.setText(book.getName());
		content.setText(book.getContent());
		category.setText(Utils.bookCategoryEnumToString(book.getCategory()));
		content.setMovementMethod(new ScrollingMovementMethod());
		
		//get author from web service
		Author au = new Author();
		try {
		  RestClient rest = new RestClient(Constants.HTTP + ip.trim() + Constants.SEARCH_AUTHOR + book.getBookId());
      rest.execute(RestClient.RequestMethod.GET);
      if (rest.getResponseCode() == 200) {
        JSONObject json = new JSONObject(rest.getResponseStr());
        au = Utils.createAuthorFromJSON(json);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
		author.setText(au.getName());
	}

	/* (non-Javadoc)
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    //define back menu
    menu.add(menu.NONE, Constants.MENU_BACK, menu.NONE, R.string.menu_back);
    return super.onCreateOptionsMenu(menu);
  }

  /* (non-Javadoc)
   * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
   */
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    //add action for menu
    //execute return to search screen
    Intent intent = new Intent(this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    if (searchCondition != null && !"".equals(searchCondition)) {
      intent.putExtra(Constants.SEARCH_CONDITION, searchCondition);
    }
    if (bookName != null && !"".equals(bookName)) {
      intent.putExtra(Constants.SEARCHING, bookName);
      intent.putExtra(Constants.BOOK_NAME, bookName);
    }
    startActivity(intent);
    finish();
    return super.onMenuItemSelected(featureId, item);
  }

}
