package com.odilogy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.odilogy.ebookstore.object.Item;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class Utils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	public static void createJSONCachedFile(JSONObject object,String filename){
		FileOutputStream outputfile;
		try {
				outputfile = new FileOutputStream(new File(AppChecker.DATA_DIR,filename));
				String objString = object.toString();
				outputfile.write(objString.getBytes());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void createJSONCachedFile(JSONArray jsonArray,String filename){
		FileOutputStream outputfile;
		try {
			outputfile = new FileOutputStream(new File(AppChecker.DATA_DIR,filename));
			String objString = jsonArray.toString();
			outputfile.write(objString.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Item> createBookList(JSONObject jsonObj){
		ArrayList<Item> bookList = new ArrayList<Item>();
		JSONArray jsonArr = null;
		boolean status;
		if(jsonObj != null){
			try {
				status = jsonObj.getBoolean("status");
				if(status){
					jsonArr = jsonObj.getJSONArray("data");
					for(int i=0;i<jsonArr.length();i++){
							JSONObject bookItem = jsonArr.getJSONObject(i);
							int id = bookItem.getInt("book_id");
							String title = bookItem.getString("book_book_title");
							String description= bookItem.getString("book_book_description");
							String file = bookItem.getString("book_book_file");
							String meta = bookItem.getString("category_name");
							String externalLink = bookItem.getString("book_external_link");
							Item a = new Item(id, title, description, file, meta);
							if(file.equals("null")){
								a.setExternalLink(externalLink);
							}
							bookList.add(a);
					}
				}else{
					Log.v("REST RESPONSE", "status = "+status);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
				
		return bookList;
	}
	
	public static ArrayList<Item> createCategoryList(JSONObject jsonObj){
		ArrayList<Item> bookList = new ArrayList<Item>();
		JSONArray jsonArr = null;
		boolean status;
		if(jsonObj != null){
			try {
				status = jsonObj.getBoolean("status");
				if(status){
					jsonArr = jsonObj.getJSONArray("data");
					for(int i=0;i<jsonArr.length();i++){
							JSONObject bookItem = jsonArr.getJSONObject(i);
							int id = bookItem.getInt("category_id");
							String title = bookItem.getString("category_name");
							Item a = new Item(id, title);
							bookList.add(a);
					}
				}else{
					Log.v("REST RESPONSE", "status = "+status);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
				
		return bookList;
	}
	
	public static JSONObject readCachedFile(){
		File cached = new File(AppChecker.DATA_DIR,AppChecker.FILE_LATEST_JSON);
		JSONObject jsonObject = null;
		if(cached.exists()){
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(cached));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                jsonObject = new JSONObject(text.toString());
                return jsonObject;
            }catch (IOException e) {
            	e.printStackTrace();
            } catch (JSONException e) {
				e.printStackTrace();
			}
            
        }
        else{
        	return null;
        }
		return jsonObject;
	}
	
	public static LinearLayout createVerticalBookHolder(Context context){
		LinearLayout single_book_holder = new LinearLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		single_book_holder.setLayoutParams(params);
		single_book_holder.setOrientation(LinearLayout.VERTICAL);
		return single_book_holder;
	}
	
	public static boolean deleteBook(Item book){
		File book_file = new File(AppChecker.DATA_DIR+"/"+book.getFile());
		boolean deleted = book_file.delete();
		return deleted;
	}

}
