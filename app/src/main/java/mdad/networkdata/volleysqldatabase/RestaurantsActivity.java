package mdad.networkdata.volleysqldatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantsActivity extends AppCompatActivity {

    ListView resList;
    ArrayList<HashMap<String, String>> restaurantsList;
    //private static String URL_GET_RESTAURANTS = MainActivity.SERVER_URL+"/restaurants.php";
    private static String URL_GET_RESTAURANTS ="http://192.168.68.51:8080/reviews-api/restaurants.php";
    JSONArray restaurants = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        restaurantsList = new ArrayList<HashMap<String, String>>();
        postData(URL_GET_RESTAURANTS,null );
        resList = (ListView)findViewById(R.id.resList);

    } // onCreate end

    public void postData(String url, final JSONObject json){
        RequestQueue reqQ = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.GET, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checkResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Req error", Toast.LENGTH_LONG).show();
            }
        });
        reqQ.add(json_obj_req);

    } // postData end

    public void checkResponse(JSONObject response){
        try{
            if(response.getInt("status")==1){
                // get data
                JSONArray restaurants = response.getJSONArray("data");

                // Loop thru data response
                for (int i = 0; i < restaurants.length(); i++){
                    JSONObject r = restaurants.getJSONObject(i);
                    String restaurantId = r.getString("restaurantId");
                    String name = r.getString("name");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("restaurantId", restaurantId);
                    map.put("name",name);
                    restaurantsList.add(map);
                }
                System.out.println(restaurantsList);
                // update parsed JSON data into listview by using SimpleAdapter, which links the data to the listview
                ListAdapter adapter = new SimpleAdapter(RestaurantsActivity.this,restaurantsList,R.layout.restaurant_item, new String[]{"restaurantId","name"},new int[]{R.id.tvItemId,R.id.tvItemName});
                resList.setAdapter(adapter);
            }else {
                Toast.makeText(getApplicationContext(),"Request failed", Toast.LENGTH_LONG).show();
            }

        }catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"Request failed", Toast.LENGTH_LONG).show();
        }
    } // checkResponse end

}// RestaurantsActivity end