package com.odilogy.ebookstore;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.odilogy.ebookstore.object.BookListAdapter;
import com.odilogy.ebookstore.object.Item;
import com.odilogy.mupdf.MuPDFActivity;
import com.odilogy.net.DownloadThread;
import com.odilogy.net.RESTRequest;
import com.odilogy.util.AppChecker;
import com.odilogy.util.DatabaseHandler;
import com.odilogy.util.SettingManager;
import com.odilogy.util.Utils;

public class Home extends Activity {

	Button btn_latest, btn_category, btn_library, btn_search;
	EditText txt_search;
	ListView listView;
	RESTRequest REST;
	BookListAdapter mAdapter;
	AppChecker app_checker;
	SettingManager settingmanager;
	DatabaseHandler dbHandler;
	protected ProgressDialog myPd_ring;
	ArrayList<Item> itemList;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		/** create RESTRequest object instance */
		REST = new RESTRequest(getApplicationContext(),
				this.getString(R.string.HOST),
				this.getString(R.string.BASE_API_URL));

		/** create AppChecker object instance */
		app_checker = new AppChecker(getApplicationContext());

		/** create SettingManager object instance */
		settingmanager = new SettingManager(getApplicationContext());

		/** create DatabaseHandler object instance */
		dbHandler = new DatabaseHandler(getApplicationContext());

		/** call initComponent methode */
		initComponent();

		/** set default tab position */
		 settingmanager.setCurrentTab(SettingManager.LIBRARY);
		

		/** init global itemList */
		itemList = new ArrayList<Item>();

		/**
		 * to global itemList last json response, could be empty on first launch
		 */
		itemList = dbHandler.getAllBooks();

		/** init global adapter for listview */
		mAdapter = new BookListAdapter(getApplicationContext(), itemList);

		/** attach adapter to listview */
		listView.setAdapter(mAdapter);
		openLibrary(btn_library);
	}

	/**
	 * Initialize all component include UI variable and application requirement
	 * */
	private void initComponent() {
		/** check for APP_DIR */
		if (!app_checker.isAlreadyDir()) {
			myPd_ring = ProgressDialog.show(this, "Please Wait", "Preparing..",
					true);
			myPd_ring.setCancelable(true);
			Thread threadinitbaseapp = new Thread() {
				public void run() {
					initBaseApp();
					
				}
			};
			threadinitbaseapp.start();
			myPd_ring.dismiss();
		}

		/** set all variable UI component */
		btn_latest = (Button) findViewById(R.id.btn_latest);
		btn_category = (Button) findViewById(R.id.btn_category);
		btn_library = (Button) findViewById(R.id.btn_library);
		btn_search = (Button) findViewById(R.id.btn_search);
		txt_search = (EditText) findViewById(R.id.txt_search);
		listView = (ListView) findViewById(R.id.main_list);

		/**
		 * register on long click item listener to context menu, this only
		 * perform on library tab
		 */
		registerForContextMenu(listView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long arg3) {
				Item book = itemList.get(position);
				if (settingmanager.getCurrentTab() == SettingManager.LATEST) {
					if (!dbHandler.isBookExist(book.getId())) {
						confirmDownload(book);
					}
				} else if (settingmanager.getCurrentTab() == SettingManager.CATEGORY) {
					Integer[] params = { SettingManager.CATEGORY_SEARCH,
							book.getId() };
					new getDataTask().execute(params);
					settingmanager
							.setCurrentTab(SettingManager.CATEGORY_SEARCH);
				} else if (settingmanager.getCurrentTab() == SettingManager.LIBRARY) {
					Intent pdfActivity = new Intent(getApplicationContext(),
							MuPDFActivity.class);
					pdfActivity.putExtra("bookName", book.getTitle());
					File book_file = new File(app_checker.DATA_DIR + "/"
							+ book.getId()+".pdf");
					Uri uri_data = Uri.parse(book_file.getAbsolutePath());
					pdfActivity.setAction(Intent.ACTION_VIEW);
					pdfActivity.setData(uri_data);
					startActivity(pdfActivity);
				} else if (settingmanager.getCurrentTab() == SettingManager.SEARCH) {
					confirmDownload(book);
				} else if (settingmanager.getCurrentTab() == SettingManager.CATEGORY_SEARCH) {
					if (!dbHandler.isBookExist(book.getId())) {
						confirmDownload(book);
					}
				}

			}
		});
		findViewById(R.id.relativeLayout1).requestFocus();
	}

	protected void initBaseApp() {
		if (app_checker.isAppDirReadyAndCreate()) {
			if (app_checker.copyAssets()) {
				String device_id = Secure.getString(getContentResolver(),
						Secure.ANDROID_ID);
				settingmanager.setFirstTime();
				settingmanager.setPrefDeviceId(device_id);
			}
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (settingmanager.getCurrentTab() == SettingManager.LIBRARY) {
			// AdapterView.AdapterContextMenuInfo info =
			// (AdapterView.AdapterContextMenuInfo)menuInfo;
			menu.add(R.string.remove_book);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		Item book = itemList.get(info.position);
		Log.v("item to delete", itemList.get(info.position).getTitle());
		if (Utils.deleteBook(book)) {
			dbHandler.deleteBook(book);
			itemList.remove(book);
			mAdapter.notifyDataSetChanged();
		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void getAllBook(View view) {
		if (settingmanager.getCurrentTab() != SettingManager.LATEST) {
			if (app_checker.internetChecker()) {
				Integer[] params = { SettingManager.LATEST };
				new getDataTask().execute(params);
				settingmanager.setCurrentTab(SettingManager.LATEST);
				resetButtonState(view);
				txt_search.setText("");
			} else {
				notifyConnection();
			}

		}
	}

	public void getCategory(View view) {
		if (settingmanager.getCurrentTab() != SettingManager.CATEGORY) {
			if (app_checker.internetChecker()) {
				Integer[] params = { SettingManager.CATEGORY };
				new getDataTask().execute(params);
				settingmanager.setCurrentTab(SettingManager.CATEGORY);
				resetButtonState(view);
				txt_search.setText("");
			} else {
				notifyConnection();
			}

		}

	}

	public void openLibrary(View view) {
		if (settingmanager.getCurrentTab() != SettingManager.LIBRARY) {
			settingmanager.setCurrentTab(SettingManager.LIBRARY);
			itemList.clear();
			itemList.addAll(dbHandler.getAllBooks());
			mAdapter.notifyDataSetChanged();
		}
		txt_search.setText("");
		resetButtonState(view);
	}

	public void searchBook(View view) {
		if (!txt_search.getText().toString().equals("")) {
			if (app_checker.internetChecker()) {
				Integer[] params = { SettingManager.SEARCH };
				new getDataTask().execute(params);
				settingmanager.setCurrentTab(SettingManager.SEARCH);
			}
		}
	}

	private void resetButtonState(View view) {
		switch (view.getId()) {
		case R.id.btn_latest:
			view.setBackgroundResource(R.drawable.current_tab);
			btn_category.setBackgroundResource(R.drawable.main_button);
			btn_library.setBackgroundResource(R.drawable.main_button);
			break;
		case R.id.btn_category:
			view.setBackgroundResource(R.drawable.current_tab);
			btn_library.setBackgroundResource(R.drawable.main_button);
			btn_latest.setBackgroundResource(R.drawable.main_button);
			break;
		case R.id.btn_library:
			view.setBackgroundResource(R.drawable.current_tab);
			btn_latest.setBackgroundResource(R.drawable.main_button);
			btn_category.setBackgroundResource(R.drawable.main_button);
			break;
		default:
			break;
		}
	}

	private void confirmDownload(final Item book) {
		dialog = new Dialog(Home.this);
		dialog.setContentView(R.layout.pop_up);
		dialog.setTitle("Confirmation");
		dialog.setCancelable(true);

		// set up text
		TextView text_title = (TextView) dialog.findViewById(R.id.txtTitleBook);
		text_title.setText(book.getTitle());

		// set up button
		Button close = (Button) dialog.findViewById(R.id.btnClose);
		close.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Button ok = (Button) dialog.findViewById(R.id.btnOk);
		ok.setVisibility(View.VISIBLE);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DownloadThread dl_thread = new DownloadThread(
						getApplicationContext(), book, dbHandler);
				dialog.dismiss();
				if (app_checker.internetChecker()) {
					dl_thread.execute();
					itemList.remove(book);
					mAdapter.notifyDataSetChanged();
				} else {
					notifyConnection();
				}
			}
		});

		dialog.show();

	}

	private void notifyConnection() {
		dialog = new Dialog(Home.this);
		dialog.setContentView(R.layout.pop_up);
		dialog.setTitle("Warning");
		dialog.setCancelable(true);

		// set up text
		TextView text_title = (TextView) dialog.findViewById(R.id.txtTitleBook);
		text_title.setText(R.string.no_connection);
		TextView txt_desc = (TextView) dialog.findViewById(R.id.txt_message);
		txt_desc.setText("Oops");

		// set up button
		Button close = (Button) dialog.findViewById(R.id.btnClose);
		close.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Button ok = (Button) dialog.findViewById(R.id.btnOk);
		ok.setVisibility(View.GONE);

		dialog.show();
	}

	/**
	 * getDataTask extends asynctask that retrieve data from server as
	 * background service
	 * */
	private class getDataTask extends AsyncTask<Integer, String, JSONObject> {
		JSONObject result;
		Integer[] parameter;

		protected void onPreExecute() {
			myPd_ring = ProgressDialog.show(Home.this, "Please Wait",
					"Loading..", true);
			myPd_ring.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Integer... params) {
			this.parameter = params;
			int key = params[0];
			if (key == SettingManager.LATEST) {
				result = REST.getAllBook();
				Utils.createJSONCachedFile(result, AppChecker.FILE_LATEST_JSON);
			} else if (key == SettingManager.CATEGORY) {
				result = REST.getAllCategory();
			} else if (key == SettingManager.LIBRARY) {

			} else if (key == SettingManager.CATEGORY_SEARCH) {
				result = REST.getBookByCategory(params[1]);
			} else if (key == SettingManager.SEARCH) {
				result = REST.getBookBySearch(txt_search.getText().toString());
			}

			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			ArrayList<Item> bookList = new ArrayList<Item>();
			int key = parameter[0];
			if (key == SettingManager.LATEST) {
				bookList = Utils.createBookList(result);
				filterBookList(bookList);
			} else if (key == SettingManager.CATEGORY) {
				bookList = Utils.createCategoryList(result);
				itemList.clear();
				itemList.addAll(bookList);
			} else if (key == SettingManager.LIBRARY) {

			} else if (key == SettingManager.SEARCH) {
				bookList = Utils.createBookList(result);
				filterBookList(bookList);
			} else if (key == SettingManager.CATEGORY_SEARCH) {
				bookList = Utils.createBookList(result);
				filterBookList(bookList);
			}

			mAdapter.notifyDataSetChanged();
			myPd_ring.dismiss();
			super.onPostExecute(result);
		}

		/**
		 * filter booklist, if book exist in DB in otherwords already downloaded
		 * remove it from list so only book that user not own that show
		 * */
		private void filterBookList(ArrayList<Item> bookList) {
			itemList.clear();
			for (int i = 0; i < bookList.size(); i++) {
				Item book = bookList.get(i);
				if (!dbHandler.isBookExist(book.getId())) {
					itemList.add(book);
				}
			}

		}

	}

}
