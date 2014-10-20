package com.example.assignment9;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.*;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.widget.WebDialog;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	protected  StockDataBean stockbean;
	   private Facebook fbObject;
	   private AsyncFacebookRunner fbAsyncRunner;
	   private static String APP_ID = "1412117385724530";
	   private SharedPreferences sharedPref;
	   private String toPostStr, toPostCaption;
	   private String inputText, type;
	   private ProgressDialog dialog;
	   private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		

		toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
	      //toast.setGravity(Gravity.BOTTOM, 0, 0);
	  	//code for assignment 9
			fbObject = new Facebook(APP_ID);
	         fbAsyncRunner = new AsyncFacebookRunner(fbObject);
	//Code for assignment 9   
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
		//	getFragmentManager().beginTransaction()
			//		.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		hideDynamicView();
		
		AutoCompleteTextView actv;
		actv = (AutoCompleteTextView) findViewById(R.id.symbolEditText); 
		
		AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.symbolEditText);
        acTextView.setAdapter(new SuggestionAdapter(this,acTextView.getText().toString()));
        acTextView.setOnItemClickListener(new OnItemClickListener() {

            

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String selection = (String)arg0.getItemAtPosition(arg2);
				if(selection!=null || "".equals(selection)==false)
				{
					if(selection.indexOf(",")!=-1)
					{
						AutoCompleteTextView acTextView1 = (AutoCompleteTextView) findViewById(R.id.symbolEditText);
						acTextView1.setText(selection.split(",")[0]);
						fetchDataFromServlet(arg1);
						
					}
				}
				
			}
        });
        
        acTextView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				AutoCompleteTextView acTextView1 = (AutoCompleteTextView) findViewById(R.id.symbolEditText);
				String text = acTextView1.getText().toString();
				if(text!=null || "".equals(text)==false)
				{
					if(text.indexOf(",")!=-1)
					{
						acTextView1.setText(text.split(",")[0]);
						
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
	}
	
	
	private void hideDynamicView() {
		RelativeLayout dynamicLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);
		dynamicLayout.setVisibility(View.INVISIBLE);
		
		RelativeLayout dynamicLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout3);
		dynamicLayout2.setVisibility(View.INVISIBLE);
		
		
		TableLayout dynamicTable = (TableLayout) findViewById(R.id.stockDataTable);
		dynamicTable.setVisibility(View.INVISIBLE);
		
	}
	
	private void showDynamicView(boolean value) {
		RelativeLayout dynamicLayout = (RelativeLayout) findViewById(R.id.relativeLayout2);
		dynamicLayout.setVisibility(View.VISIBLE);
		
		if(value == true)
		{
			Button b1 = (Button)findViewById(R.id.newsHeadLines);
			b1.setVisibility(View.INVISIBLE);
		}
		else
		{
			Button b1 = (Button)findViewById(R.id.newsHeadLines);
			b1.setVisibility(View.VISIBLE);
		}
		
		TableLayout dynamicTable = (TableLayout) findViewById(R.id.stockDataTable);
		dynamicTable.setVisibility(View.VISIBLE);
		
		RelativeLayout dynamicLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout3);
		dynamicLayout2.setVisibility(View.VISIBLE);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	/** This method is responsible for fetching the data from the servlet and performing the other validations. This method accepts the edit text parameter from the 
	 * 
	 * @param view
	 */
	public void fetchDataFromServlet(View view)
	{
		hideDynamicView();
		
		
		//get location
				AutoCompleteTextView symbol = (AutoCompleteTextView) findViewById(R.id.symbolEditText);
				String symbolVal = symbol.getText().toString().replaceAll("\\s+", " ");
				
				String errorMessage = "";
				boolean hasError=false;
				boolean locationIsCity = false;
				
				//is the search query empty?
				if (hasError = (symbolVal==""))
					errorMessage = "Please enter a company/symbol.";
				else {
					
					if(symbolVal.indexOf(",")!=-1)
		            {
						symbolVal = symbolVal.split(",")[0].trim();
		            }
				}
				
				if (hasError) {
					AlertDialog.Builder invalidInputDialogBuilder = new AlertDialog.Builder(this);
					invalidInputDialogBuilder
						.setTitle("Error")
						.setMessage(errorMessage)
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//do nothing
							}
						})
						.show();
				}
				else
				{
					String url = "http://cs-server.usc.edu:13562/examples/servlet/StockServletAssignment9?symbol=";
					
					try {
						
						url = url.concat(symbolVal);
						
						System.out.println(url);
					
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					
					new RESTCallTask().execute(this, url);
				}
				
		
				
				
	}				
				
				
	public void displayRSSFeeds(View view)
	{
		Intent intent = new Intent(this, RSSFeedsActivity.class);
		String s="";
		for(StockFeedsBean sbean :this.stockbean.getStockFeeds())
		{
			s += sbean.getTitle()+"@@@@@@"+sbean.getLink()+"####";
		}
		
		intent.putExtra("Feeds", s);
		startActivity(intent);

	}
	

	/***********************************************Code for async call to Servlet***************************************************************/
	
	private class RESTCallTask extends AsyncTask<Object, Integer, String> {
		private MainActivity context;
		
		protected String doInBackground(Object... params) {
			context = (MainActivity) params[0];
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet((String)params[1]);
			HttpResponse response;
			
			try
			{
				System.out.println("Rest call started");
				response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				return result;
			}
			
			catch(Exception ex)
			{
				String message = ex.getMessage();
				return message;
			}
		}

		protected void onPostExecute(String json) {
			
			try
			{
				
			//check if json contains error message.
			JSONObject jsonObj = new JSONObject(json);
			
			try
			{
			String errorMessage = (String) jsonObj.get("message");
			
			if (errorMessage != null) {
				AlertDialog.Builder cannotFind = new AlertDialog.Builder(context);
				cannotFind
					.setTitle("No stock")
					.setMessage(errorMessage)
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//do nothing
						}
					})
					.show();
				return;
			}
			}
			catch(Throwable objThrowable)
			{
				System.out.println("Normal execution");
			}
			
			StockDataBean objStockDataBean = new StockDataBean();
			objStockDataBean.extractJSON(json);
		
			
			//update UI
			TextView nameText = (TextView) findViewById(R.id.textView5);
			nameText.setText(objStockDataBean.getSymbol()+" ("+objStockDataBean.getName()+")");
			
			TextView lastTradePriceText = (TextView) findViewById(R.id.textView6);
			lastTradePriceText.setText(objStockDataBean.getLastTradePriceOnly());
			
			TextView changeText = (TextView) findViewById(R.id.textView7);
			changeText.setText(objStockDataBean.getChange());
			
			TextView  changePercentText = (TextView) findViewById(R.id.textView8);
			changePercentText.setText("("+objStockDataBean.getChangeInPercent()+")");
			
			ImageView stockImageURLView = (ImageView) findViewById(R.id.stockImageView);
			if(objStockDataBean.getChangeType().equals("+"))
			{
				stockImageURLView.setImageResource(R.drawable.up_g);
				changePercentText.setTextColor(Color.GREEN);
				changeText.setTextColor(Color.GREEN);
			}
			else
			{
				stockImageURLView.setImageResource(R.drawable.down_r);
				changePercentText.setTextColor(Color.RED);
				changeText.setTextColor(Color.RED);
			}
			
			
			TextView prevCloseText = (TextView) findViewById(R.id.textView10);
			prevCloseText.setText(objStockDataBean.getPreviousClose());
			
			TextView openText = (TextView) findViewById(R.id.textView12);
			openText.setText(objStockDataBean.getOpen());
		
			TextView bidText = (TextView) findViewById(R.id.textView14);
			bidText.setText(objStockDataBean.getBid());
			
			TextView askText = (TextView) findViewById(R.id.textView16);
			askText.setText(objStockDataBean.getAsk());
			
			TextView yrTargeText = (TextView) findViewById(R.id.textView18);
			yrTargeText.setText(objStockDataBean.getOneYearTargetPrice());
			
			TextView dayRangeText = (TextView) findViewById(R.id.textView20);
			dayRangeText.setText(objStockDataBean.getDaysLow()+"-"+objStockDataBean.getDaysHigh());
			
			TextView yearRangeText = (TextView) findViewById(R.id.textView22);
			yearRangeText.setText(objStockDataBean.getYearLow()+"-"+objStockDataBean.getYearHigh());
			
			TextView volumeText = (TextView) findViewById(R.id.textView24);
			volumeText.setText(objStockDataBean.getVolume());
			
			TextView avgvolumeText = (TextView) findViewById(R.id.textView26);
			avgvolumeText.setText(objStockDataBean.getAverageDailyVolume());
			
			TextView marketCapText = (TextView) findViewById(R.id.marketCap);
			volumeText.setText(objStockDataBean.getMarketCapitalization());
			
			
			
			(new DownloadImageTask()).execute(objStockDataBean.getImgURL());
			
			context.stockbean = objStockDataBean;
			
			ArrayList<StockFeedsBean> objArrayList = objStockDataBean.getStockFeeds();
			
			
			if(objArrayList.size()==1 && objArrayList.get(0).getTitle().equals("No records found"))
			{
				showDynamicView(true);
			}
			else
			{
			showDynamicView(false);
			}
			
			
			
			}
			catch(Throwable objThrowable)
			{
				objThrowable.printStackTrace();
				AlertDialog.Builder cannotFind = new AlertDialog.Builder(context);
				cannotFind
					.setTitle("Error")
					.setMessage("Error in processing data")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//do nothing
						}
					})
					.show();
				return;
			}
		}
		
		private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
			protected Bitmap doInBackground(String... params) {
				try {
					URL imageURL = new URL(params[0]);
					Bitmap imageData = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
					return imageData;
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
				return null;
			}
			
			protected void onPostExecute(Bitmap image) {
				ImageView stockImageURLView = (ImageView) findViewById(R.id.stockImageURLView);
				stockImageURLView.setImageBitmap(image);
			}
			
		
		}
	}
				
				
				
				
				
/*********************************************Code Ends***********************************************************************************/		
	
	
/***********************************************Facebook API******************************************************************/
	
	public void postInfo(View view)
	{
		postStockInformation();
	}
	
	
	public void postStockInformation() {
		System.out.println("starting post stock execution");
		/*Session.StatusCallback statusCallback = new SessionStatusCallback(this, true);
		Session session = Session.getActiveSession();*/
		
		try {
		/*	if (session != null && !session.isOpened() && !session.isClosed()) {
				System.out.println("Active session still open");
				session.openForRead(new Session.OpenRequest(this)
					.setPermissions(Arrays.asList("basic_info"))
					.setCallback(statusCallback));
			}
			else {
				System.out.println("In else of open active session");
				Session.openActiveSession(this, true, statusCallback);
			}	*/
		loginToFacebook();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private class SessionStatusCallback implements Session.StatusCallback {
		private Activity context;
		private boolean value;
		
		public SessionStatusCallback(Activity context, boolean value) {
			this.context = context;
			this.value = value;
		}
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			System.out.println("Starting call"+state.isOpened());
			if (state.isOpened()) {
				if (exception == null) {
					List<String> permissions = session.getPermissions();
					System.out.println(permissions.toString());
					if (permissions.contains("publish_actions"))
					{	
						System.out.println("Can publish values");
						publishFeedDialog(value);
						
					}
					else {
						
						Session.NewPermissionsRequest newPermissionsRequest = new Session
							   .NewPermissionsRequest(context, Arrays.asList("publish_actions"));
						session.requestNewPublishPermissions(newPermissionsRequest);
					}
				}
				else
				{
					exception.printStackTrace();
				}
			}
			else
			{
				System.out.println("No state opened");
			}
			System.out.println("End call");
		}
		
		private void publishFeedDialog(boolean shareCurrentWeather) {
		    StockDataBean data = ((MainActivity)context).stockbean;
		    
			Bundle params = new Bundle();
			
			
			params.putString("name", data.getName());
		    params.putString("link", "http://finance.yahoo.com/q?s="+data.getSymbol());
		    params.putString("caption", "Stock Information of "+data.getName()+" ("+ data.getSymbol()+")");
			String description = "Last Trade Price: "+data.getLastTradePriceOnly()+",Change: "+data.getChangeType()+data.getChange()+" ("+data.getChangeInPercent()+")";   		
			 params.putString("description", description);
			 params.putString("picture", data.getImgURL());
		
			 try
			 {
			 JSONObject properties = new JSONObject();
			 JSONObject lookAtDetails = new JSONObject();
			 lookAtDetails.put("text", "here");
			 lookAtDetails.put("href", "");
			 properties.put("Look at details", lookAtDetails);
			 params.putString("properties", properties.toString());
			 }catch(Exception e)
			 {
				 System.out.println(e.getMessage());
			 }
			System.out.println("Before feed dialog");
		    WebDialog feedDialog = (
		        new WebDialog.FeedDialogBuilder(context,
		            Session.getActiveSession(),
		            params))
		        .setOnCompleteListener(new WebDialog.OnCompleteListener() {

		            @Override
		            public void onComplete(Bundle values,
		                FacebookException error) {
		                if (error == null) {
		                    // When the story is posted, echo the success
		                    // and the post Id.
		                    final String postId = values.getString("post_id");
		                    if (postId != null) {
		                        Toast.makeText(context,
		                            "Posted story, id: "+postId,
		                            Toast.LENGTH_SHORT).show();
		                    } else {
		                        // User clicked the Cancel button
		                        Toast.makeText(context.getApplicationContext(), 
		                            "Publish cancelled", 
		                            Toast.LENGTH_SHORT).show();
		                    }
		                } else if (error instanceof FacebookOperationCanceledException) {
		                    // User clicked the "x" button
		                    Toast.makeText(context.getApplicationContext(), 
		                        "Publish cancelled", 
		                        Toast.LENGTH_SHORT).show();
		                } else {
		                    // Generic, ex: network error
		                    Toast.makeText(context.getApplicationContext(), 
		                        "Error posting story", 
		                        Toast.LENGTH_SHORT).show();
		                }
		            }

		        })
		        .build();
		    
		    feedDialog.show();
		}

	}	
	
	
	
	
	
	
	
	
	
	
	 
	
	
	
	
	
	
	/*********************************************End FB API*******************************************************************/
	

	
	
/***************************************** Facebook 2 ************************************************************************************/
	
	public void loginToFacebook() { //Need for assignment 9
	
		 sharedPref = getPreferences(MODE_PRIVATE);
	      String accessToken = sharedPref.getString("access_token", null);
	      long expireDuration = sharedPref.getLong("access_expires", 0);

	      if (accessToken != null) {
	         fbObject.setAccessToken(accessToken);
	      }
	      if (expireDuration != 0) {
	         fbObject.setAccessExpires(expireDuration);
	      }
	      if (!fbObject.isSessionValid()) {
	         fbObject.authorize(this, new String[] { "email", "publish_stream","offline_access" }, new DialogListener() {

	            @Override
	            public void onCancel() {
	               // Function to handle cancel event
	            }

	            @Override
	            public void onComplete(Bundle values) {
	               // Function to handle complete event
	               // Edit Preferences and update facebook acess_token
	               SharedPreferences.Editor editor = sharedPref.edit();
	               editor.putString("access_token", fbObject.getAccessToken());
	               editor.putLong("access_expires", fbObject.getAccessExpires());
	               editor.commit();
	            }

	            @Override
	            public void onFacebookError(FacebookError e) {
	           
	               toast.setText("Error while logging in Facebook");
	               toast.show();
	            }

	            @Override
	            public void onError(DialogError e) {
	               toast.setText("Error while logging in Facebook");
	               toast.show();
	            }

	         });
	      }
	      postToFacebook();
	   }

	   private void postToFacebook() { //Code needed for assignment 9
	      Bundle params = new Bundle();
	      	params.putString("method", "feed");
	      StockDataBean data = this.stockbean;
		  params.putString("name", data.getName());
		    params.putString("link", "http://finance.yahoo.com/q?s="+data.getSymbol());
		    params.putString("caption", "Stock Information of "+data.getName()+" ("+ data.getSymbol()+")");
			String description = "Last Trade Price: "+data.getLastTradePriceOnly()+",Change: "+data.getChangeType()+data.getChange()+" ("+data.getChangeInPercent()+")".replaceAll("+","");   		
			 params.putString("description", description);
			 params.putString("picture", data.getImgURL());


		 fbObject.dialog(MainActivity.this, "feed", params, new DialogListener() {

		    @Override
		    public void onFacebookError(FacebookError e) {
		       toast.setText("There was an error while connecting to Facebook");
		       toast.show();
		    }

		    @Override
		    public void onError(DialogError e) {
		       toast.setText("There was an error while connecting to Facebook");
		       toast.show();
		    }

		    @Override
		    public void onComplete(Bundle values) {
		       String postID = values.getString("post_id");
		       if (postID != null) {
		    	   final String successText = (new StringBuffer().append(("Posted Successfully\n"+postID))).toString();
		          fbAsyncRunner.request(postID, new RequestListener() {

		           
		             @Override
		             public void onFacebookError(FacebookError e, Object state) {
		                toast.setText("Error while connecting to Facebook");
		                toast.show();
		             }

		             @Override
		             public void onComplete(String response, Object state) {
		                toast.setText(successText);
		                toast.show();
		             }
		             
		             

					@Override
					public void onIOException(IOException e, Object state) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onFileNotFoundException(
							FileNotFoundException e, Object state) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onMalformedURLException(
							MalformedURLException e, Object state) {
						// TODO Auto-generated method stub
						
					}
		          });
		       }
		       else
		       {
	                toast.setText("Publish cancel");
	                toast.show();
		       }
		    }

		    @Override
		    public void onCancel() {
		    	System.out.println("Publish cancelled");
		       toast.setText("Publish cancel");
		       toast.show();
		    }
		 });
	   }
	
	
/**************************************** Facebook 2 ends *******************************************************************************/	
	
	
	

	
}
