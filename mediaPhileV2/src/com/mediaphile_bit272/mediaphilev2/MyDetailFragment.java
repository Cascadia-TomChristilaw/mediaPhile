package com.mediaphile_bit272.mediaphilev2;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyDetailFragment extends Fragment {
	  
	  LinearLayout detailContainer;

	  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
		  View view = null;
		  Bundle bundle = getArguments();
		  String detail = bundle.getString("KEY_DETAIL", "no argument pass");

		  if (detail.matches("List Movies")) {
			  view = inflater.inflate(R.layout.layout_listmoviesfragment, null);
		  }
		  else if (detail.matches("Add Movie")) {
			  view = inflater.inflate(R.layout.layout_addmoviefragment, null);
		  }
	    	return view;
	   }

	  }
