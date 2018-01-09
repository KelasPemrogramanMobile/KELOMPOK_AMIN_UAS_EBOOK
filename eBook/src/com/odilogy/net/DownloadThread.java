
package com.odilogy.net;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.odilogy.ebookstore.R;
import com.odilogy.ebookstore.object.Item;
import com.odilogy.util.AppChecker;
import com.odilogy.util.DatabaseHandler;
import com.odilogy.util.NotificationHelper;

/**
 * DownloadThread handle download file background service
 * @author odilogy@gmail.com
 * */

public class DownloadThread extends AsyncTask<String, String, String> {
	
	/** Field Item object instance contains book details */	
	Item book;
	/** Field dbHanlder hold DatabaseHandler instance object  */
	DatabaseHandler dbHandler;
	/** Context   */
	Context mContext;
	/** NotificationHalper helper to create ongoing notification to show download progress	  */
	NotificationHelper mNotificationHelper;
	Notification mNotification;
	final int NOTIFICATION_ID = 10987;
	private String API_DOWNLOAD_FILE;
	private boolean use_external;

	/**
	 * Constructor
	 * @param Context application context
	 * @param Item book
	 * @param DatabaseHandler database handler
	 * */
	public DownloadThread(Context context,Item book,DatabaseHandler dbHandler){
		this.book =  book;
		this.dbHandler = dbHandler;
		this.mContext = context;
		this.API_DOWNLOAD_FILE = "http://"+mContext.getString(R.string.HOST)+mContext.getString(R.string.API_DOWNLOAD_FILE);
		mNotificationHelper = new NotificationHelper(context);
	}
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        mNotificationHelper.createNotification(book.getTitle(),NOTIFICATION_ID+(int)book.getId());
    }
	
	@Override
	protected String doInBackground(String... params) {
		int percentage;
        try {
        	URL url_book = null;        	
        	if(book.getFile().equals("null")){
        		this.use_external = true;
        		String externalBasePath = book.getExternalLink();
        		Log.v("checking book file","book.externalLink() ="+book.getExternalLink());
        		url_book = new URL(externalBasePath);
        	}else{
        		this.use_external = false;
        		url_book = new URL(API_DOWNLOAD_FILE+book.getFile());
        	}
        	
        	HttpURLConnection urlConnection = (HttpURLConnection) url_book.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            
            int totalSize = urlConnection.getContentLength();            
            Log.v("CONTENT LENGTH", "Header fields = "+urlConnection.getHeaderFields().toString());
            Log.v("TOTAL SIZE", "total size = "+totalSize);
            String filepath = AppChecker.DATA_DIR+"/"+book.getId()+".pdf"; //like your sdcard

            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(filepath);

            //this will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //variable to store total downloaded bytes

            double downloadedSize = 0.0;

            //create a buffer...
            byte[] buffer = new byte[5000];
            int bufferLength = 0; //used to store a temporary size of the buffer
            
            //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) 
                {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
//                percentage = (int) ((downloadedSize / totalSize) * 100);
                String str_total;
                if(totalSize != -1){
                	percentage = (int) ((downloadedSize/totalSize)*100);
                	str_total = String.valueOf(percentage);
                }else{
                	str_total = "Unknown";
                	percentage = (int)(downloadedSize/(1024*10));
                }
                String[] update = {""+percentage,str_total};
              publishProgress(update);

            }
            //close the output stream when done
            fileOutput.close();
            downloadedSize = 0.0;
            percentage = 0;
 
        } catch (Exception e) {
        	e.printStackTrace();
            Log.e("Error: ", e.getMessage());
        }
 
        return null;		
	} //params , progress, result

	@Override
	protected void onProgressUpdate(String... progress) {
		if(progress[1].equals("Unknown")){
			mNotificationHelper.setContentText("Progress "+progress[0]+" Kb");
		}else{
			mNotificationHelper.setContentText("Progress "+progress[0]+" %");
		}
		mNotificationHelper.progressUpdate(Integer.parseInt(progress[0]));
//		Log.v("DOWNLOAD PROGRESS", "Progress "+progress[0]+" %");
//      Log.v("DOWNLOAD TOTAL", "Total "+progress[1]+" ");
//      Log.v("DOWNLOAD LENGTH", "Total "+progress[2]+" ");
        
   }
	
	
	@Override
    protected void onPostExecute(String file_url) {
		dbHandler.addBook(this.book);
		mNotificationHelper.completed();
    }	
	
}
