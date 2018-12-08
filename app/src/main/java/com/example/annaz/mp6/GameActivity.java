package com.example.annaz.mp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;


import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }*/

    public void launchScoreScreen(View v) {
        Intent i = new Intent(this, scoreScreen.class);
        startActivity(i);

    }


    private static final String TAG = "Lab12:Main";



    private static RequestQueue requestQueue;

    void startAPICall() {
        String celebURL =
                "https://en.wikipedia.org/" +
                        "w/api.php?action=query&titles=Albert%20Einstein&format=json&prop=images";
        String celebURL2 = "https://en.wikipedia.org/w/api.php?action=query" +
                "&prop=pageimages&format=json&piprop=original&titles=Albert%20Einstein";

        //change height by changing 60!!
        String celebURL3 = "https://en.wikipedia.org/w/api.php?action=query&format=" +
                "json&formatversion=2&prop=pageimages|pageterms&piprop=thumbnail&pithumbsize=" +

                //60 = current height!
                "60&titles=Albert%20Einstein";


        try {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    celebURL2, //original: url: "",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            getImageURL(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            TextView answerLabel = findViewById(R.id.answerLabel);
            answerLabel.setText("we messed up"); //why doesn't this do anything???

        }

        //TextView answerLabel = findViewById(R.id.answerLabel);
        //answerLabel.setText(jsonObjectRequest.get("continue").toString());

        
    }

    void getImageURL(JSONObject object) {
        TextView answerLabel = findViewById(R.id.answerLabel);

        try {
            //myObj.cars.car2;
            JSONObject queryObject = object.getJSONObject("query");
            JSONObject pagesObject = queryObject.getJSONObject("pages");
            //String pageNumber = pagesObject.getString("pages"); //can't do this
            String pageNumber = pagesObject.names().toString();

            pageNumber = pageNumber.substring(2, pageNumber.length() - 2);
            answerLabel.setText(pageNumber);
            JSONObject pageNumberObject = pagesObject.getJSONObject(pageNumber);

            JSONObject originalObject = pageNumberObject.getJSONObject("original");
            String imageURL = originalObject.getString("source");

            answerLabel.setText(imageURL);

            //upload image to image view

            ImageView imageView = findViewById(R.id.celebImageView);

            

            //imageView.setImageBitmap();

        } catch(Exception e) {
            answerLabel.setText("none");
        }
    }

    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //set up a queue for our Volley requests

        requestQueue = Volley.newRequestQueue(this);

        //Load main layout
        setContentView(R.layout.activity_game);

        //attach handler to UI button

        final Button startAPICall = findViewById(R.id.startAPICall);

        startAPICall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                Log.d(TAG, "Start API button clicked");
                startAPICall();
            }

        });

        //make sure progress bar isn’t spinning (deleted)

    }


    //Make API call


}

