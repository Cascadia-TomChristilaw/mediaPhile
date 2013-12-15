package com.mediaphile_bit272.mediaphilev2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mediaphile_bit272.mediaphilev2.RestHandler.MovieMaker;
import com.mediaphile_bit272.mediaphilev2.RestHandler.TitleMaker;

public class AddMovieFragment extends Fragment implements MovieMaker,
		TitleMaker {

	EditText movieTitleEditText;
	Button titleSearchButton;
	Button saveMovieButton;
	EditText releasedEditText;
	EditText mpaaRatingEditText;
	EditText genreEditText;
	EditText runtimeEditText;
	EditText castEditText;
	EditText plotEditText;
	// EditText imageUrlEditText;
	EditText formatEditText;
	EditText fileSizeEditText;
	EditText pathEditText;
	EditText personalNoteEditText;
	MovieMaker myMovieMaker;
	TitleMaker myTitleMaker;
	String[] searchResult;
	String[] returnedTitles;
	Movie curMovie;
	RestHandler rest;
	int MovieLocation;
	private static int SEARCH_TIME_OUT = 750;

	public void updateGivenFields() {
		this.movieTitleEditText.setText(curMovie.getTitle());
		this.releasedEditText.setText(curMovie.getReleaseDate());
		this.mpaaRatingEditText.setText(curMovie.getMpaa());
		this.genreEditText.setText(curMovie.getGenre());
		this.runtimeEditText.setText(curMovie.getRuntime());
		this.castEditText.setText(curMovie.getCast());
		this.plotEditText.setText(curMovie.getPlot());
	}

	public void setResult(String[] arr, String[] t) {
		this.searchResult = arr;
		this.returnedTitles = t;
	}

	public void setCurMovie(Movie result) {
		this.curMovie = result;
	}

	public Movie getMovie() {
		return this.curMovie;
	}

	OnClickListener titleSearchButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String searchString = movieTitleEditText.getText().toString();

			rest = new RestHandler(searchString, true, myMovieMaker,
					myTitleMaker);
			rest.execute();

			Toast.makeText(getActivity(),
					"Your search string is " + searchString, Toast.LENGTH_LONG)
					.show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					AlertDialog.Builder movieChoices = new AlertDialog.Builder(
							getActivity());
					movieChoices.setTitle("Select A Movie").setItems(
							searchResult,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(
											getActivity(),
											"You selected "
													+ searchResult[which],
											Toast.LENGTH_LONG).show();
									MovieLocation = which;
									Log.v("The search button is passing:",
											returnedTitles[MovieLocation]);
									RestHandler rt = new RestHandler(
											returnedTitles[MovieLocation],
											false, myMovieMaker, myTitleMaker);
									rt.execute();
								}
							});
					movieChoices.show();

				}
			}, SEARCH_TIME_OUT);
		}
	};

	OnClickListener saveMovieButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			DatabaseHandler db = new DatabaseHandler(getView().getContext());

			Movie movieToSave = new Movie(movieTitleEditText.getText()
					.toString().trim(), releasedEditText.getText().toString()
					.trim(),
					releasedEditText.getText().toString().split(" ")[2].trim(),
					mpaaRatingEditText.getText().toString().trim(),
					genreEditText.getText().toString().trim(), castEditText
							.getText().toString().trim(), plotEditText
							.getText().toString().trim(), runtimeEditText
							.getText().toString().trim(), formatEditText
							.getText().toString().trim(), fileSizeEditText
							.getText().toString().trim(), pathEditText
							.getText().toString().trim(), personalNoteEditText
							.getText().toString().trim());
			db.addMovie(movieToSave);

			Log.d("SUCCESS!! ", "The save movie button has been clicked.");
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.layout_addmoviefragment, null);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		myTitleMaker = (TitleMaker) this;
		myMovieMaker = (MovieMaker) this;

		movieTitleEditText = (EditText) getView().findViewById(
				R.id.movieTitleEditText);
		releasedEditText = (EditText) getView().findViewById(
				R.id.releasedEditText);
		mpaaRatingEditText = (EditText) getView().findViewById(
				R.id.mpaaRatingEditText);
		genreEditText = (EditText) getView().findViewById(R.id.genreEditText);
		runtimeEditText = (EditText) getView().findViewById(
				R.id.runtimeEditText);
		castEditText = (EditText) getView().findViewById(R.id.castEditText);
		plotEditText = (EditText) getView().findViewById(R.id.plotEditText);
		// imageUrlEditText = (EditText)
		// getView().findViewById(R.id.imageUrlEditText);
		formatEditText = (EditText) getView().findViewById(R.id.formatEditText);
		fileSizeEditText = (EditText) getView().findViewById(
				R.id.fileSizeEditText);
		pathEditText = (EditText) getView().findViewById(R.id.pathEditText);
		personalNoteEditText = (EditText) getView().findViewById(
				R.id.personalNoteEditText);

		titleSearchButton = (Button) getView().findViewById(
				R.id.titleSearchButton);
		titleSearchButton.setOnClickListener(titleSearchButtonListener);

		saveMovieButton = (Button) getView().findViewById(R.id.saveMovieButton);
		saveMovieButton.setOnClickListener(saveMovieButtonListener);

		Log.d("ACTIVITY!! ", "The AddMovieFragment activity has been created.");

	}

	@Override
	public void onArrayMade(String[] arrr, String[] titlesForReference) {
		Log.v("Yo", "I made it onArrayMade");
		setResult(arrr, titlesForReference);

	}

	@Override
	public void onMovieMade(Movie toGive) {
		Log.v("Yo", "I made it onMovieMade");
		setCurMovie(toGive);
		updateGivenFields();

	}

}