package mdad.networkdata.volleysqldatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnRegister,btnRestaurantList;
    EditText etUsername, etEmail, etPw;
    public static String SERVER_URL  = "http://2181017a.atspace.co.uk";
    private static String URL_USER_REGISTER = SERVER_URL+"/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRestaurantList= findViewById(R.id.btnRestaurantList);
        btnRegister = findViewById(R.id.btnRegister);
        etUsername=findViewById(R.id.etUsername);
        etEmail=findViewById(R.id.etEmail);
        etPw=findViewById(R.id.etPw);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle btn register
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String pw = etPw.getText().toString();

                if(username.isEmpty() || email.isEmpty() || pw.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please input all fields", Toast.LENGTH_LONG).show();
                } else {
                    // all fields has values
                    JSONObject reqBodyData = new JSONObject();
                    try {
                        reqBodyData.put("username",username);
                        reqBodyData.put("email",email);
                        reqBodyData.put("password",pw);

                    } catch (JSONException e) {}

                    postData(URL_USER_REGISTER,reqBodyData);
                    System.out.println(reqBodyData);
                }



            }
        }); // btnRegister.setOnClickListener end here

        btnRestaurantList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restListUi = new Intent(getApplicationContext(),RestaurantsActivity.class);
                startActivity(restListUi);
            }
        }); // btnRestaurantList.setOnClickListener end here



    } // onCreate end


    public void postData(String url, final JSONObject json){
        RequestQueue reqQ = Volley.newRequestQueue(this);
        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
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

    public void checkResponse(JSONObject response, JSONObject c){

        try{
            if(response.getInt("status") == 1){
                Toast.makeText(getApplicationContext(),"Request success", Toast.LENGTH_LONG).show();
                // go list
                Intent restListUi = new Intent(getApplicationContext(),RestaurantsActivity.class);
                startActivity(restListUi);

            }else{
                Toast.makeText(getApplicationContext(),"Request failed", Toast.LENGTH_LONG).show();
            }
        }catch(JSONException e){
            //catch
            Toast.makeText(getApplicationContext(),"Error in json", Toast.LENGTH_LONG).show();

        }

    } // checkResponse end


} // MainActivity end