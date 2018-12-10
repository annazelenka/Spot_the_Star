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
import android.graphics.Bitmap;
import android.widget.TextView;
import android.widget.EditText;


//CountDownTimer stuff
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import java.util.Locale;

//API stuff
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.ImageRequest;

//so that keyboard doesn't show up
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import android.content.Context;





import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {

    private String[] celebNames = {"Kanye West", "Kylie Jenner", "Angelina Jolie", "Geoff Challen",
            "Rihanna", "Beyoncé", "Brad Pitt", "Justin Bieber", "Kim Kardashian", "Selena Gomez"};

    private String geoffURL = "https://www.bluegroup.systems/people/challen@buffalo.edu/xphoto.jpg.pagespeed.ic.tfnNawc0a2.jpg";

    private String currentCeleb;
    private int currentArrValue = 0;



    private TextView time;
    private TextView textViewCountDown;
    private static long COUNTDOWN_IN_MILLIS = 3000000;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private boolean timerHasEnded;
    private int score;



    public void guessFunction(View v) { //previously titled "launchScoreScreen"

        if (answerIsCorrect()) {
            currentArrValue += 1;

            if (currentArrValue >= celebNames.length) {
                currentArrValue = 0;
            } else if (currentArrValue == 3) {
                ImageView imageView = findViewById(R.id.celebImageView);
                uploadCelebImage(geoffURL, imageView);

            } else { //if it's not Geoff
                currentCeleb = celebNames[currentArrValue];
                String celebURL = getAPIURL(currentCeleb);
                startAPICall(celebURL);
            }

            //uploadCelebImage("https://www.bluegroup.systems/people/challen@buffalo.edu/xphoto.jpg.pagespeed.ic.tfnNawc0a2.jpg", imageView);
            TextView scoreLabel = findViewById(R.id.scoreLabel);
            score++;
            scoreLabel.setText("Score: " + score);

        }


    }

    public boolean answerIsCorrect() {

        //check challen case

        EditText editText = findViewById(R.id.editText);

        String userAnswer = editText.getText().toString().trim().toLowerCase();

        if (currentCeleb != null) {
            if (currentCeleb.equals("Geoff Challen")) {
                if (userAnswer.equals("challen")) {
                    return true;
                }
            } else if (currentCeleb.trim().toLowerCase().equals(userAnswer)) {
                return true;
            }

        }

        return false;
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
            EditText answerLabel = findViewById(R.id.editText);
            answerLabel.setText("we messed up"); //why doesn't this do anything???

        }
    }

    void getImageURL(JSONObject object) {
        EditText answerLabel = findViewById(R.id.editText);

        try {
            //the JSONObject that we get from Wikipedia is reallllllllly nested, so I made a bunch
            //of JSONObjects to access the URL that we want.
            JSONObject queryObject = object.getJSONObject("query");
            JSONObject pagesObject = queryObject.getJSONObject("pages");
            String pageNumber = pagesObject.names().toString();
            pageNumber = pageNumber.substring(2, pageNumber.length() - 2);
            //answerLabel.setText(pageNumber);
            JSONObject pageNumberObject = pagesObject.getJSONObject(pageNumber);
            JSONObject originalObject = pageNumberObject.getJSONObject("original");
            String imageURL = originalObject.getString("source");
            //answerLabel.setText(imageURL);

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

        try {
            ImageRequest imageRequest = new ImageRequest(url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imageView.setImageBitmap(response);
                            TextView answerLabel = findViewById(R.id.editText);
                            //answerLabel.setText("I didn't mess up");

                        }
                    }, 700, 700, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            TextView answerLabel = findViewById(R.id.editText);
                            answerLabel.setText("I messed up");


                        }


                    });

            requestQueue.add(imageRequest);
        } catch(Exception e) {
            TextView answerLabel = findViewById(R.id.editText);
            answerLabel.setText("there's a mistake");

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




        //set up count down timer

        textViewCountDown = findViewById(R.id.textView2);
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();

        //set first celeb
        currentCeleb = celebNames[0];

        score = 0;

        hideKeyboard(GameActivity.this);


    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();


                Intent i = new Intent(GameActivity.this, scoreScreen.class);
                i.putExtra("score", score); //passes score to the next screen
                startActivity(i);

                countDownTimer.cancel();

            }
        }.start();
    }


    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);
        textViewCountDown.setTextColor(Color.RED);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideTextFieldKeyboard(View v) {
        hideKeyboard(GameActivity.this);

    }
}

