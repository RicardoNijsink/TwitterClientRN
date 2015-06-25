package nl.saxion.rn.projecttwitterclient;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nl.rn.projecttwitterclient.model.HashTag;
import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.TwitterModel;
import nl.rn.projecttwitterclient.model.URL;
import nl.rn.projecttwitterclient.model.User;
import nl.rn.projecttwitterclient.model.UserMention;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends ArrayAdapter<Tweet> implements Observer{
	private LayoutInflater inflater;
	private Context context;
	private TwitterModel model;
	private Tweet t;
	private ImageView userProfilePicture;

	public TweetAdapter(Context context, int resource, List<Tweet> objects) {
		super(context, resource, objects);
		inflater = LayoutInflater.from(context);
		this.context = context;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(R.layout.tweet, parent, false);
			}
			
			t = getItem(position);
			
			TextView userName = (TextView)convertView.findViewById(R.id.textViewUserName);
			TextView text = (TextView)convertView.findViewById(R.id.textViewTweet);
			TextView createdAt = (TextView)convertView.findViewById(R.id.textViewTweetCreatedAt);
			TextView location = (TextView)convertView.findViewById(R.id.textViewLocation);
			userProfilePicture = (ImageView)convertView.findViewById(R.id.imageViewUserProfilePicture);
			
			userName.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), UserActivity.class);
					intent.putExtra("Position", position);
					getContext().startActivity(intent);
					
				}
			});
			
			if(t.getCreatedAt().length() > 0){
				createdAt.setText("" + t.getCreatedAt());
				createdAt.setVisibility(View.VISIBLE);
			}
			if(t.getUser().getName().length() > 0){
				userName.setText("" + t.getUser().getName());
				userName.setVisibility(View.VISIBLE);
			}
			if(t.getText().length() > 0){
				text.setText(setSpanColor(t));
				text.setVisibility(View.VISIBLE);
			}
			if(location != null){
				location.setText("" + t.getLocation());
			}
			new DownloadImageTask(userProfilePicture).execute(t.getUser().getProfileImageURL());
			
			return convertView;
		}

		@Override
		public void update(Observable observable, Object data) {
			notifyDataSetChanged();
		}
		
		private SpannableString setSpanColor(Tweet t) {
			SpannableString spanText = new SpannableString(t.getText());
			for(int i = 0; i < t.getHashTags().size(); i++){
				HashTag hashTag = t.getHashTags().get(i);
				spanText.setSpan(new ForegroundColorSpan(Color.BLUE), hashTag.getBeginPosition(), hashTag.getEndPosition(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			if(t.getUrls().size() != 0){
				for(int counter = 0; counter < t.getUrls().size(); counter++){
					URL url = t.getUrls().get(counter);
					spanText.setSpan(new ForegroundColorSpan(Color.BLUE), url.getBeginPosition(), url.getEndPosition(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			
			if(t.getUserMentions().size() != 0){
				for(int umCounter = 0; umCounter < t.getUrls().size(); umCounter++){
					UserMention userMention = t.getUserMentions().get(umCounter);
					spanText.setSpan(new ForegroundColorSpan(Color.BLUE), userMention.getBeginPosition(), userMention.getEndPosition(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			
			return spanText;
		}
		
		private ClickableSpan setClickableSpan(String text) {
			SpannableString string = new SpannableString(t.getText());
			
			//TODO
			return null;
		}
		
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
			  ImageView bmImage;

			  public DownloadImageTask(ImageView bmImage) {
			      this.bmImage = bmImage;
			  }

			  protected Bitmap doInBackground(String... urls) {
			      String urldisplay = urls[0];
			      Bitmap images = null;
			      try {
			        InputStream in = new java.net.URL(urldisplay).openStream();
			        images = BitmapFactory.decodeStream(in);
			      } catch (Exception e) {
			          Log.e("Error", e.getMessage());
			          e.printStackTrace();
			      }
			      return images;
			  }

			  protected void onPostExecute(Bitmap result) {
			      bmImage.setImageBitmap(result);
			  }
			}

}
