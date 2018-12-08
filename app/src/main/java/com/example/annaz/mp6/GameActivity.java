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
import 	android.graphics.Bitmap;

import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageRequest;



import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {

    private boolean timerHasEnded;
    private int score;

    public void guessFunction(View v) { //previously titled "launchScoreScreen"
        Intent i = new Intent(this, scoreScreen.class);
        i.putExtra("score", score); //passes score to the next screen


        if (answerIsCorrect("rihanna")) {
            String celebURL = getAPIURL("Rihanna"); //make sure it's capitalized and has spaces if necessary!
            startAPICall(celebURL);
            TextView scoreLabel = findViewById(R.id.scoreLabel);
            score++;
            scoreLabel.setText("Score: " + score);

        } else {

        }

        //startActivity(i);

        if (timerHasEnded) { //Anna: eventually maybe we should delete this and just launch the next screen when the timer ends
            startActivity(i);
        }


    }

    public boolean answerIsCorrect(String answer) {
        return true;
    }

    public String getAPIURL(String celeb) {



        return "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=original&titles=" + celeb;

    }





    private static final String TAG = "Lab12:Main";



    private static RequestQueue requestQueue;

    void startAPICall(String celebURL) {
        String celebURL4 =
                "https://en.wikipedia.org/" +
                        "w/api.php?action=query&titles=Albert%20Einstein&format=json&prop=images";
        String celebURL2 = "https://en.wikipedia.org/w/api.php?action=query" +
                "&prop=pageimages&format=json&piprop=original&titles=Albert%20Einstein";

        //change height by changing 60!!
        String celebURL3 = "https://en.wikipedia.org/w/api.php?action=query&format=" +
                "json&formatversion=2&prop=pageimages|pageterms&piprop=thumbnail&pithumbsize=" +

                //60 = current height!
                "60&titles=Albert%20Einstein";

        String rihannaURL = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=original&titles=Rihanna";


        try {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    celebURL, //original: url: "",
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
    }

    void getImageURL(JSONObject object) {
        TextView answerLabel = findViewById(R.id.answerLabel);

        try {
            //the JSONObject that we get from Wikipedia is reallllllllly nested, so I made a bunch
            //of JSONObjects to access the URL that we want.
            JSONObject queryObject = object.getJSONObject("query");
            JSONObject pagesObject = queryObject.getJSONObject("pages");
            String pageNumber = pagesObject.names().toString();
            pageNumber = pageNumber.substring(2, pageNumber.length() - 2);
            answerLabel.setText(pageNumber);
            JSONObject pageNumberObject = pagesObject.getJSONObject(pageNumber);
            JSONObject originalObject = pageNumberObject.getJSONObject("original");
            String imageURL = originalObject.getString("source");
            answerLabel.setText(imageURL);

            //upload image to image view
            ImageView imageView = findViewById(R.id.celebImageView);
            uploadCelebImage(imageURL, imageView);


        } catch(Exception e) {
            answerLabel.setText("none");
        }
    }

    /*
    ** changes image view to image of current celebrity.
     */
    //modified code from https://stackoverflow.com/questions/48379771/android-volley-api-imagerequest-is-not-working
    public void uploadCelebImage(String url, final ImageView imageView) {

        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>(){
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                        TextView answerLabel = findViewById(R.id.answerLabel);
                        //answerLabel.setText("I didn't mess up");

                    }
                }, 700,700, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                TextView answerLabel = findViewById(R.id.answerLabel);
                answerLabel.setText("I messed up");


                }


        });
        requestQueue.add(imageRequest);


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
    }





}

