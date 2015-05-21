package nl.saxion.rn.projecttwitterclient;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import nl.rn.projecttwitterclient.model.Tweet;
import nl.rn.projecttwitterclient.model.TwitterModel;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
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
			
			SpannableString spanText = new SpannableString(t.getText());
						
			userName.setText("" + t.getUserName());
			text.setText(spanText);
			createdAt.setText("" + t.getCreatedAt());
			location.setText("" + t.getLocation());
			return convertView;
		}

		@Override
		public void update(Observable observable, Object data) {
			notifyDataSetChanged();
		}

}
