package nl.saxion.rn.projecttwitterclient;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.squareup.picasso.Picasso;

import nl.rn.projecttwitterclient.model.HashTag;
import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.URL;
import nl.rn.projecttwitterclient.model.UserMention;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
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
			
			//Hier worden alle user interface-componenten en onClickListeners gedeclareerd
			TextView userName = (TextView)convertView.findViewById(R.id.textViewUserName);
			TextView text = (TextView)convertView.findViewById(R.id.textViewTweet);
			TextView createdAt = (TextView)convertView.findViewById(R.id.textViewTweetCreatedAt);
			TextView location = (TextView)convertView.findViewById(R.id.textViewLocation);
			userProfilePicture = (ImageView)convertView.findViewById(R.id.imageViewUserProfilePicture);
			
			userName.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), UserActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("naam", t.getUser().getName());
					intent.putExtra("Position", position);
					getContext().startActivity(intent);
					
				}
			});
			
			//Hier worden alle gegevens in de bijbehorende user interface-componenten ingevuld
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
			
			Picasso.with(getContext()).load(t.getUser().getProfileImageURL()).into(userProfilePicture);
			
			return convertView;
		}

		@Override
		public void update(Observable observable, Object data) {
			notifyDataSetChanged();
		}
		
		/**
		 * Methode voor het kleuren van alle user mentions, hashtags en urls.
		 * @param t
		 * @return
		 */
		private SpannableString setSpanColor(Tweet t) {
			SpannableString spanText = new SpannableString(t.getText());
			for(int i = 0; i < t.getHashTags().size(); i++){
				HashTag hashTag = t.getHashTags().get(i);
				Log.d("Hashtag", String.valueOf(hashTag.getBeginPosition()));
				spanText.setSpan(new ForegroundColorSpan(Color.BLUE), hashTag.getBeginPosition(), hashTag.getEndPosition(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			if(t.getUrls().size() != 0){
				for(int counter = 0; counter < t.getUrls().size(); counter++){
					URL url = t.getUrls().get(counter);
					Log.d("URL", String.valueOf(url.getBeginPosition()));
					spanText.setSpan(new ForegroundColorSpan(Color.BLUE), url.getBeginPosition(), url.getEndPosition(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			
			if(t.getUserMentions().size() != 0){
				for(int umCounter = 0; umCounter < t.getUserMentions().size(); umCounter++){
					UserMention userMention = t.getUserMentions().get(umCounter);
					Log.d("UserMention", String.valueOf(userMention.getBeginPosition()));
					spanText.setSpan(new ForegroundColorSpan(Color.BLUE), userMention.getBeginPosition(), userMention.getEndPosition(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			
			return spanText;
		}
}
