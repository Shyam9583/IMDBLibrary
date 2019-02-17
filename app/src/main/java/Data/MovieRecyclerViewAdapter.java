package Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pce.haven.imdblibrary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Movie;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MyViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public MovieRecyclerViewAdapter(Context context,List<Movie> movieList) {
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_row,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        Movie movie = movieList.get(i);
        viewHolder.movieTitle.setText(movie.getTitle());
        viewHolder.movieYear.setText("Released: "+movie.getYear());
        viewHolder.movieType.setText("Type: "+movie.getType());
        Picasso.get().load(movie.getPoster()).placeholder(android.R.drawable.alert_dark_frame).error(android.R.drawable.alert_light_frame).into(viewHolder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView movieTitle;
        private TextView movieYear;
        private TextView movieType;
        private ImageView moviePoster;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieName);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieYear = itemView.findViewById(R.id.movieYear);
            movieType = itemView.findViewById(R.id.movieType);
        }
    }
}
