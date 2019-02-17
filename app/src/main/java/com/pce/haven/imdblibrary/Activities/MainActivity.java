package com.pce.haven.imdblibrary.Activities;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.Transition;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pce.haven.imdblibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Data.MovieRecyclerViewAdapter;

import Data.searchPreferences;
import Model.Movie;
import Util.Utilities;

public class MainActivity extends AppCompatActivity {

    private AlertDialog searchDialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView searchText;
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter adapter;
    private List<Movie> receivedMovieList;
    private searchPreferences preferences;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialogBuilder = new AlertDialog.Builder(this);
        queue = Volley.newRequestQueue(MainActivity.this);

        setupList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSearchPopup();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<Movie> getReceivedMovieList(String searchTerm) {
        final List<Movie> movieList = new ArrayList<>();
        movieList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Utilities.LEFT_URL + "s=" + searchTerm + Utilities.RIGHT_URL
                ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Movie movie;
                            JSONArray movieArray = response.getJSONArray("Search");
                            for (int i = 0; i < movieArray.length(); i++) {
                                movie = new Movie();
                                JSONObject jsonObject = movieArray.getJSONObject(i);
                                movie.setImdbID(jsonObject.getString("imdbID"));
                                movie.setYear(jsonObject.getString("Year"));
                                movie.setTitle(jsonObject.getString("Title"));
                                movie.setType(jsonObject.getString("Type"));
                                movie.setPoster(jsonObject.getString("Poster"));
                                movieList.add(movie);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);
        return movieList;
    }

    public void setupList(){
        searchText = findViewById(R.id.searchText);
        recyclerView = findViewById(R.id.movieResultsList);
        preferences = new searchPreferences(MainActivity.this);
        String search = preferences.getSearch();
        searchText.setText("Search results for : '"+search+"'");
        receivedMovieList = getReceivedMovieList(search);
        adapter = new MovieRecyclerViewAdapter(MainActivity.this,receivedMovieList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void createSearchPopup(){
        View view = getLayoutInflater().inflate(R.layout.search_popup,null);
        searchDialog = dialogBuilder.create();
        final TextView enteredSearchTerm = view.findViewById(R.id.enteredSearchTerm);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredText = enteredSearchTerm.getText().toString();
                if (!enteredSearchTerm.getText().toString().isEmpty()) {
                    preferences.setSearch(enteredText);
                    receivedMovieList.clear();
                    receivedMovieList = getReceivedMovieList(enteredText);
                    searchText.setText("Search results for : '"+enteredText+"'");
                    adapter.notifyDataSetChanged();
                    searchDialog.dismiss();
                } else {
                    Snackbar.make(view.getRootView(),"Please enter a term!",Snackbar.LENGTH_LONG);
                }
            }
        });
        searchDialog.setView(view);
        searchDialog.show();

    }
}
