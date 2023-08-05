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
    private static String URL_GET_RESTAURANTS = MainActivity.SERVER_URL+"/restaurants.php";
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
                checkResponse(response,json);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Req error", Toast.LENGTH_LONG).show();
            }
        });
        reqQ.add(json_obj_req);

    } // postData end

    public void checkResponse(JSONObject response, JSONObject c) {
        try {
            if (response.getInt("status") == 1) {
                System.out.println("status ok. here");
                restaurants = response.getJSONArray("data");
                System.out.println("data length: " + restaurants.length());
                for (int i = 0; i < restaurants.length(); i++) {
                    JSONObject r = restaurants.getJSONObject(i);
                    System.out.println("data r " + r);

                    int restaurantId = r.getInt("restaurantId");
                    String r_id = String.valueOf(restaurantId);
                    System.out.println("data r " + r_id);
                    System.out.println("data r id " + i + " " + restaurantId);

                    String name = r.getString("name");
                    System.out.println("data name " + i + " " + name);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("restaurantId", r_id);
                    map.put("name", name);
                    System.out.println("map here " + map);
                    restaurantsList.add(map);
                }

                System.out.println("restaurants>>>>>>  " + restaurants);
                // Updating parsed JSON data into ListView by using SimpleAdapter, which links the data
                // to the ListView
                ListAdapter adapter = new SimpleAdapter(RestaurantsActivity.this, restaurantsList, R.layout.restaurant_item, new String[]{"restaurantId", "name"}, new int[]{R.id.tvItemId, R.id.tvItemName});
                resList.setAdapter(adapter);

                // res status success here
            } else {
                Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // catch
            Toast.makeText(getApplicationContext(), "Error in json", Toast.LENGTH_LONG).show();
        }
    } // checkResponse end

}// RestaurantsActivity end