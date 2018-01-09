package com.odilogy.ebookstore.object;

import java.util.ArrayList;

import com.odilogy.ebookstore.R;
import com.odilogy.util.DatabaseHandler;


import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * List Adapter for listview
 * @author odilogy@gmail.com 
 * */
public class BookListAdapter extends BaseAdapter {
	/** Context application context*/
	private Context mContext;
	/** ArrayList of Item object instance*/
	private ArrayList<Item> ItemList;
	/** Inflater to override default layout*/
	private LayoutInflater inflater;
	/** dbHandler to use database utility*/
	private DatabaseHandler dbHandler;
	
	/**
	 * Constructor
	 * @param Context context aplication context
	 * @param ArrayList<Item> ArrayList of Item object instance
	 * */
	public BookListAdapter(Context context, ArrayList<Item> itemList){
		this.mContext = context;
		this.ItemList = itemList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dbHandler = new DatabaseHandler(mContext);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ItemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ItemList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null){
			vi = inflater.inflate(R.layout.list_item,null);
		}
		
		LinearLayout row_layout = (LinearLayout) vi.findViewById(R.id.row);
		if(position%2 == 0){
			/** set different color for even row*/
			row_layout.setBackgroundColor(Color.parseColor("#FFCC00"));
		}else{
			/** set different color for odd row*/
			row_layout.setBackgroundColor(Color.parseColor("#FF9900"));
		}
		TextView title = (TextView) vi.findViewById(R.id.txt_title);
		TextView description = (TextView) vi.findViewById(R.id.txt_description);
		Item book = ItemList.get(position);
		String meta = "";
		if(book.getTitle() == null && book.getMeta() != null){
			title.setText(ItemList.get(position).getMeta());
		}else{
			title.setText(ItemList.get(position).getTitle());
			if(dbHandler.isBookExist(book.getId())){
				meta = "Downloaded";
			}
		}
		
		if(ItemList.get(position).getDescription() != null){
			description.setVisibility(View.VISIBLE);
			description.setText(Html.fromHtml(ItemList.get(position).getDescription()));
		}else{
			description.setVisibility(View.GONE);
		}
		
		return vi;
	}

}
