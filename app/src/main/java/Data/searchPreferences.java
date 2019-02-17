package Data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.pce.haven.imdblibrary.R;

public class searchPreferences {
    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor editor;
    public searchPreferences(Activity activity){
        this.sharedPreferences = activity.getSharedPreferences(String.valueOf(R.string.search_file),Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public void setSearch(String searchQuery){
        editor.putString("search",searchQuery);
        editor.commit();
    }

    public String getSearch(){
        return sharedPreferences.getString("search","pokemon");
    }
}
