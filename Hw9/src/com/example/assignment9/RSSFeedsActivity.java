package com.example.assignment9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class RSSFeedsActivity extends MainActivity {

	private ArrayList<StockFeedsBean> objFeedList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rssfeeds);

		Intent intent = getIntent();
		String message = intent.getStringExtra("Feeds");
		System.out.println(message);
		objFeedList = new ArrayList<StockFeedsBean>();
		String feeds[] = message.split("####");
		System.out.println("Total Feed length"+feeds.length);
		
		for(String s : feeds)
		{
			if(s.equalsIgnoreCase("")==false)
			{
				String []feedValues = s.split("@@@@@@");
				System.out.println("Feed value length"+feedValues.length);
				StockFeedsBean sbean = new StockFeedsBean();
				sbean.setTitle(feedValues[0].trim());
				sbean.setLink(feedValues[1].trim());
				System.out.println(sbean);
				objFeedList.add(sbean);
			}
		}
		
		String text = "Showing "+objFeedList.size()+" headlines.";
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;	
		Toast.makeText(context, text, duration).show();
		
		ListView listView = (ListView)findViewById(R.id.list);
		
		ArrayList<String> list = new ArrayList<String>();
		for(StockFeedsBean sbean: objFeedList)
		{
			list.add(sbean.getTitle());
			
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		 listView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
	               
	            	final int position = pos;
	            /*	 AlertDialog.Builder cannotFind = new AlertDialog.Builder(RSSFeedsActivity.this);
	     			cannotFind
	     				.setTitle("View News")
	     				.setMessage("View News")
	     				.setCancelable(true)
	     				.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
	     					public void onClick(DialogInterface dialog,int which){
	     						//Do nothing
	     					}
	     				})
	     				.setPositiveButton("View", new DialogInterface.OnClickListener() {
	     					@Override
	     					public void onClick(DialogInterface dialog, int which) {
	     						System.out.println(RSSFeedsActivity.this.objFeedList.get(position).getLink());
	     						Intent intent = new Intent(Intent.ACTION_VIEW, 
	     								Uri.parse(RSSFeedsActivity.this.objFeedList.get(position).getLink()));
	     							startActivity(intent);
	     						
	     					}
	     				})
	     				
	     				.show();};
	     			*/
	            	
	            	
	            ArrayList<String> names = new ArrayList<String>();
	            	names.add("View");
	            	names.add("Cancel");
	                
	            //	String names[] = {"View","Cancel"};
	            	final AlertDialog.Builder d = new AlertDialog.Builder(RSSFeedsActivity.this);
	            	final AlertDialog alertDialog = d.create();
	                LayoutInflater inflater = getLayoutInflater();
	                View convertView = (View) inflater.inflate(R.layout.activity_rssfeeds_alert, null);
	                alertDialog.setView(convertView);
	                alertDialog.setTitle("List");
	                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
	                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(RSSFeedsActivity.this,android.R.layout.simple_list_item_1,names);
	                lv.setAdapter(adapter1);
	                alertDialog.setTitle("View News");
	                alertDialog.setCancelable(true);
	                alertDialog.show();
	                
	                
	                lv.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							if(arg2 == 0)
							{
							Intent intent = new Intent(Intent.ACTION_VIEW, 
     								Uri.parse(RSSFeedsActivity.this.objFeedList.get(position).getLink()));
     							startActivity(intent);
							}
							else
							{
								alertDialog.dismiss();
							}
							}
						});
	                
	                
	                
					
	            }

			
	    });
		
		
		
		
	}
	
	/* @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // do something with the data
		 
		 System.out.println("sds");
		 AlertDialog.Builder cannotFind = new AlertDialog.Builder(this);
			cannotFind
				.setTitle("View News")
				.setMessage("View News")
				.setCancelable(false)
				.setPositiveButton("View", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
							
					}
				})
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog,int which){
						//Do nothing
					}
				})
				.show();
			
			
			return;
		 
	
		 
		 
		 
	  }*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rssfeeds, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_rssfeeds,
					container, false);
			return rootView;
		}
	}

}
