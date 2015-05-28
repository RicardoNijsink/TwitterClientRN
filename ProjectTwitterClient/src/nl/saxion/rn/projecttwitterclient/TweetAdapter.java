package nl.saxion.rn.projecttwitterclient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nl.rn.projecttwitterclient.model.HashTag;
import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.User;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
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
			
			Tweet t = getItem(position);
			
			TextView userName = (TextView)convertView.findViewById(R.id.textViewUserName);
			TextView text = (TextView)convertView.findViewById(R.id.textViewTweet);
			TextView createdAt = (TextView)convertView.findViewById(R.id.textViewTweetCreatedAt);
			TextView location = (TextView)convertView.findViewById(R.id.textViewLocation);
			ImageView userProfilePicture = (ImageView)convertView.findViewById(R.id.imageViewUserProfilePicture);
			
			userName.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), UserActivity.class);
					intent.putExtra("Position", position);
					getContext().startActivity(intent);
					
				}
			});

		    createdAt.setText("" + t.getCreatedAt());
			userName.setText("" + t.getUser().getName());
			text.setText(setSpanColor(t));
			
			location.setText("" + t.getLocation());
			return convertView;
		}

		@Override
		public void update(Observable observable, Object data) {
			notifyDataSetChanged();
		}
		
		public SpannableString setSpanColor(Tweet t) {
			SpannableString spanText = new SpannableString(t.getText());
			for(int i = 0; i < t.getHashTags().size(); i++){
				HashTag hashTag = t.getHashTags().get(i);
				spanText.setSpan(new ForegroundColorSpan(Color.BLUE), hashTag.getBeginPosition(), hashTag.getEndPosition(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			return spanText;
		}

}
