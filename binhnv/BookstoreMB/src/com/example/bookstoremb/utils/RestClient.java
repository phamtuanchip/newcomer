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
package com.example.bookstoremb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Base64;

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 27, 2012  
 */
public class RestClient {
  
  //define request method
  public static enum RequestMethod {
    GET, POST, PUT, DELETE;
  }
  
  //define authorization for header of request
  private final String AUTHORIZATION = "Authorization";
  //define list of params
  private List<NameValuePair> params;
  //define list of header
  private List<NameValuePair> headers;
  //define url
  private String url;
  //define response code which contain code of respone(ex: 200)
  private int responseCode;
  //define error message of web service
  private String errorMessage;
  //define response string
  private String responseStr;
  //define respone
  private HttpResponse respone;
  
  /**
   * RestClient constructor
   */
  public RestClient(String url) {
    this.url = url;
    params = new ArrayList<NameValuePair>();
    headers = new ArrayList<NameValuePair>();
  }

  /**
   * check search type and execute request
   * 
   * @param method The type of request method(ex: GET, POST...)
   * @throws UnsupportedEncodingException
   * @throws ClientProtocolException
   * @throws IOException
   */
  public void execute(RequestMethod method) throws UnsupportedEncodingException, ClientProtocolException, IOException {
    switch (method) {
      //case request is get
      case GET:
        {
          String combinedParams = "";
          if (!params.isEmpty()) {
            combinedParams += "?";
            for (NameValuePair p : params) {
              String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
              if (combinedParams.length() > 1) {
                combinedParams += "&" + paramString;
              } else {
                combinedParams += paramString;
              }
            }
          }
          HttpGet request = new HttpGet(url + combinedParams);
          request.setHeader(AUTHORIZATION, getAuthorization());
          for (NameValuePair h : headers) {
            request.addHeader(h.getName(), h.getValue());
          }
          executeRequest(request, url);
          break;
        }
      //case request is post
      case POST:
        {
          HttpPost request = new HttpPost(url);
          for (NameValuePair h : headers) {
            request.addHeader(h.getName(), h.getValue());
            request.setHeader(AUTHORIZATION, getAuthorization());
          }
          if (!params.isEmpty()) {
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
          }
          executeRequest(request, url);
          break;
        }
      //case request is delete
      case DELETE:
        {
          HttpDelete request = new HttpDelete(url);
          request.setHeader(AUTHORIZATION, getAuthorization());
          for (NameValuePair h : headers) {
            request.addHeader(h.getName(), h.getValue());
          }
          executeRequest(request, url);
          break;
        }
      //case request is put
      case PUT:
        {
          HttpPut request = new HttpPut();
          request.setHeader(AUTHORIZATION, getAuthorization());
          for (NameValuePair h : headers) {
            request.addHeader(h.getName(), h.getValue());
          }
          executeRequest(request, url);
          break;
        }
    }
  }
  
  /**
   * add param to param list
   * 
   * @param name
   * @param value
   */
  public void addParam(String name, String value) {
    params.add(new BasicNameValuePair(name, value));
  }
  
  /**
   * add header to header list
   * 
   * @param name
   * @param value
   */
  public void addHeader(String name, String value) {
    headers.add(new BasicNameValuePair(name, value));
  }
  
  /**
   * @return the responseCode
   */
  public int getResponseCode() {
    return responseCode;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @return the response
   */
  public String getResponseStr() {
    return responseStr;
  }
  
  /**
   * @return the respone
   */
  public HttpResponse getRespone() {
    return respone;
  }
  
  /**
   * execute request and get response
   * 
   * @param request
   * @param url
   * @throws ClientProtocolException
   * @throws IOException
   */
  private void executeRequest(HttpUriRequest request, String url) throws ClientProtocolException, IOException {
    DefaultHttpClient client = new DefaultHttpClient();
    HttpResponse respone;
    respone = client.execute(request);
    responseCode = respone.getStatusLine().getStatusCode();
    errorMessage = respone.getStatusLine().getReasonPhrase();
    HttpEntity entity = respone.getEntity();
    if (entity != null) {
      InputStream is = entity.getContent();
      responseStr = convertStreamToString(is);
      is.close();
    }
  }
  
  /**
   * convert input stream to string
   * 
   * @param is
   * @return
   */
  private String convertStreamToString(InputStream is) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = "";
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb.toString();
  }
  
  /**
   * create authorization for header of request
   * 
   * @return
   */
  private String getAuthorization() {
    return "Basic " + Base64.encodeToString((Constants.USERNAME + ":" + Constants.PASSWORD).getBytes(), Base64.NO_WRAP);
  }

}
