package dk.whooper.mobilsiden.screens;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AnmeldelserFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";
	private static final String TAG="AnmeldelserFragment";

	public AnmeldelserFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		textView.setText("Anmeldelser");
		return textView;
	}
}