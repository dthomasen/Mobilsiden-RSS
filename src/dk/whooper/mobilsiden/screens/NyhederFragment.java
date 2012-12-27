package dk.whooper.mobilsiden.screens;


import dk.whooper.mobilsiden.service.XMLDownloader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NyhederFragment extends Fragment {
	public static final String ARG_SECTION_NUMBER = "section_number";
	private static final String TAG="NyhederFragment";

	public NyhederFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		XMLDownloader xmlDownloader = new XMLDownloader();
		xmlDownloader.execute("http://www.mobilsiden.dk/xml/rssfeed.php");
		
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText("Nyheder");
		return textView;
	}
}