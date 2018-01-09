package com.odilogy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

/**
 * AppChecker is utility to initialize requirement on first launch application
 * this class also holds constant field that use by another class.
 * @author odilogy@gmail.com
 * */
public class AppChecker {
	
	/** Context aplication context*/
	private Context context;	
	/** root directory to save the ebook file*/
	public static final String APP_DIR = Environment.getExternalStorageDirectory()+ "/basicEbook";
	
	/** data directory tha contains ebook file*/	
	public static final String DATA_DIR = APP_DIR + "/data";
	/** filename for cached file store last json response from server*/
	public final static String FILE_LATEST_JSON = "latest";
	
	
	/**
	 * Constructor
	 * @param Context application context
	 * */
	public AppChecker(Context c){
		this.context = c;
	}
	
	/**
	 * Function to check whether required directory exist and create it if not exist
	 * @return {@link Boolean} true if exist otherwise false
	 * */
	public boolean isAppDirReadyAndCreate(){
		File dir_app = new File(APP_DIR);
		if(!dir_app.exists()){
			boolean create_dir = dir_app.mkdir();
			if(create_dir){				
				File data = new File(DATA_DIR);
				boolean data_dir = data.mkdir();
				return data_dir;
			}else{
				return false;
			}
		}else{
			return dir_app.exists();
		}
		
	}
	
	/**
	 * Function to check whether required directory exist
	 * @return {@link Boolean} true if exist otherwise false
	 * */
	public boolean isAlreadyDir(){
		File dir_app = new File(APP_DIR);
		if(dir_app.exists()){
			if(dir_app.isDirectory()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * Function to copy all necessary file to APP_DIR
	 * @return {@link Boolean} true if copying success
	 * */
	public boolean copyAssets(){		
		AssetManager assetManager = context.getAssets();
    	InputStream in = null;
        OutputStream out = null;
        boolean success = false;
		try {
	          in = assetManager.open("DONT_REMOVE_THIS_DIRECTORY");
	          out = new FileOutputStream(APP_DIR+"/DONT_REMOVE_THIS_DIRECTORY");
	          Utils.CopyStream(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	          success = true;
	        } catch(IOException e) {
	        	Log.e("tag", "Failed to copy asset file ", e);
	        	success = false;
	        }       
		
		return success;
//		return true;
	}
	
	/**
	 * Function to check has internet connection or not
	 * @return {@link Boolean} true if exist otherwise false
	 * */
	public boolean internetChecker(){
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			final NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (final NetworkInfo netInfoCheck : netInfo) {
				if (netInfoCheck.getTypeName().equalsIgnoreCase("WIFI")) {
					if (netInfoCheck.isConnected()) {
						haveConnectedWifi = true;
					}
				}
				if (netInfoCheck.getTypeName().equalsIgnoreCase("MOBILE")) {
					if (netInfoCheck.isConnected()) {
						haveConnectedMobile = true;
					}
				}
			}
		}

		return haveConnectedWifi || haveConnectedMobile;
	}
	
}
