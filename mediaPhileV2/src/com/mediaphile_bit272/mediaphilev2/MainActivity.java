package com.mediaphile_bit272.mediaphilev2;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
 
 // if run on phone, isSinglePane = true
 // if run on tablet, isSinglePane = false
 static boolean isSinglePane;
 
 static String[] options ={
   "List Movies", "Add Movie"};

 public static class MyListFragment extends ListFragment {

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
   super.onActivityCreated(savedInstanceState);
   
   ListAdapter myArrayAdapter = 
     new ArrayAdapter<String>(
       getActivity(), android.R.layout.simple_list_item_1, options);
   setListAdapter(myArrayAdapter);
   
  }
 
  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
   
   String clickedDetail = (String)l.getItemAtPosition(position);
   
   
	MyDetailFragment myDetailFragment = new MyDetailFragment();
	Bundle bundle = new Bundle();
	bundle.putString("KEY_DETAIL", clickedDetail);
	myDetailFragment.setArguments(bundle);
	FragmentTransaction fragmentTransaction =
	  getActivity().getFragmentManager().beginTransaction();
	
	if(isSinglePane == true){
	    /*
	     * The second fragment not yet loaded. 
	     * Load MyDetailFragment by FragmentTransaction, and pass 
	     * data from current fragment to second fragment via bundle.
	     */
	fragmentTransaction.replace(R.id.phone_container, myDetailFragment);	
	}else{
		/*
	     * Activity have two fragments. Pass data between fragments
	     * via reference to fragment
	     */
		fragmentTransaction.replace(R.id.detail_fragment_container, myDetailFragment);
	}	
	fragmentTransaction.addToBackStack(null);
	fragmentTransaction.commit();    
  }   
 }
 
 @Override
 public void onBackPressed(){
     FragmentManager fm = getFragmentManager();
     if (fm.getBackStackEntryCount() > 0) {
         Log.i("MainActivity", "popping backstack");
         fm.popBackStack();
     } else {
         Log.i("MainActivity", "nothing on backstack, calling super");
         super.onBackPressed();  
     }
 }
 
 public static class MyDetailFragment extends Fragment {
  
  LinearLayout detailContainer;

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
	  View view = null;
	  Bundle bundle = getArguments();
	  String detail = bundle.getString("KEY_DETAIL", "no argument pass");

	  if (detail.matches("List Movies")) {
		  view = inflater.inflate(R.layout.layout_detailfragment, null);
	  }
	  else if (detail.matches("Add Movie")) {
		  view = inflater.inflate(R.layout.layout_addmoviefragment, null);
	  }
    	return view;
   }

  }
 
 

  

 

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  
  View v = findViewById(R.id.phone_container);
  if(v == null){
   //it's run on tablet
   isSinglePane = false;
   
   if(savedInstanceState == null){
	    //if's the first time created
	   MyListFragment myListFragment = new MyListFragment();
	    FragmentTransaction listFragmentTransaction = getFragmentManager().beginTransaction();
	    listFragmentTransaction.add(R.id.list_fragment_container, myListFragment);
	    listFragmentTransaction.commit();
	    

	   }
   
  }else{
   //it's run on phone
   //Load MyListFragment programmatically
   isSinglePane = true;
   
   if(savedInstanceState == null){
    //if's the first time created
    MyListFragment myListFragment = new MyListFragment();
    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.phone_container, myListFragment);
    fragmentTransaction.commit();
   }
  }
  
  DatabaseHandler db = new DatabaseHandler(this);
  
  /**
   * CRUD Operations
   * */
  // Inserting Movies
  Log.d("Insert: ", "Inserting .."); 
  db.addMovie(new Movie("The Matrix", "31 Mar 1999", "1999", "R",
		  "Action, Adventure, Sci-Fi", "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss, Hugo Weaving",
		  "Thomas A. Anderson is a man living two lives. By day he is an average computer programmer and by night a hacker known as Neo. Neo has always questioned his reality, but the truth is far beyond his imagination. Neo finds himself targeted by the police when he is contacted by Morpheus, a legendary computer hacker branded a terrorist by the government. Morpheus awakens Neo to the real world, a ravaged wasteland where most of humanity have been captured by a race of machines that live off of the humans' body heat and electrochemical energy and who imprison their minds within an artificial reality known as the Matrix. As a rebel against the machines, Neo must return to the Matrix and confront the agents: super-powerful computer programs devoted to snuffing out Neo and the entire human rebellion.",
		  "2 h 16 min",
		  "MP4", "1.98GB", "Seagate external 2 TB HDD", "\\movies", "Brett",
		  "http://ia.media-imdb.com/images/M/MV5BMjEzNjg1NTg2NV5BMl5BanBnXkFtZTYwNjY3MzQ5._V1_SX300.jpg",
		  "This movie was a turning point in cinematic special effects")); 
  
  db.addMovie(new Movie("Die Hard", "22 Jul 1988", "1988", "R",
		  "Action, Thriller", "Bruce Willis, Alan Rickman, Bonnie Bedelia, Reginald VelJohnson",
		  "New York City Detective John McClane has just arrived in Los Angeles to spend Christmas with his wife. Unfortunatly, it is not going to be a Merry Christmas for everyone. A group of terrorists, led by Hans Gruber is holding everyone in the Nakatomi Plaza building hostage. With no way of anyone getting in or out, it's up to McClane to stop them all. All 12!",
		  "2 h 11 min",
		  "DVD", "", "Shelf 3, Brown Bookshelf", "", "Tom",
		  "http://ia.media-imdb.com/images/M/MV5BMTY4ODM0OTc2M15BMl5BanBnXkFtZTcwNzE0MTk3OA@@._V1_SX300.jpg",
		  "Yippee-Ki-Yay MuthaFucka..."));
   
  // Reading all contacts
  Log.d("Reading: ", "Reading all movies.."); 
  List<Movie> movies = db.getAllMovies();       
   
  for (Movie mv : movies) {
      String log = "Id: "+mv.getID()+
    		  " ,Title: " + mv.getTitle() +
    		  " ,Release Date: " + mv.getReleaseDate()+
    		  " ,Year: " + mv.getYear()+
    		  " ,MPAA Rating: " + mv.getMpaa()+
    		  " ,Genre: " + mv.getGenre()+
    		  " ,Cast: " + mv.getCast()+
    		  " ,Plot: " + mv.getPlot()+
    		  " ,Runtime: " + mv.getRuntime()+
    		  " ,Format: " + mv.getFormat()+
    		  " ,Size: " + mv.getSize()+
    		  " ,Location: " + mv.getLocation()+
    		  " ,Path: " + mv.getPath()+
    		  " ,Lent To: " + mv.getLentTo()+
    		  " ,Image URL: " + mv.getImageUrl()+
    		  " ,Personal Note: " + mv.getNote();
  // Writing Contacts to log
  Log.d("Movie: ", log);
}
 }
 
 

}