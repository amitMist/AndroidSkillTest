package amit.recruitment.com.androidskill;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tasomaniac.android.widget.DelayedProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AcmeActivity extends AppCompatActivity {



    private final String ACME_BASE_URL = "http://api.acme.international";
    private final String ACME_API_URL = ACME_BASE_URL+"/fortune";
    private final String REQUEST_TAG = "AcmeActivity_REQUEST";
    private final String CONTENT_TAG = "AcmeActivity_FORTUNE_CONTENT";
    private final String LOADING_TAG = "AcmeActivity_FORTUNE_LOADING";

    private final String APP_TAG = "AndroidSKILL";


    private String storedValue=null;
    private boolean wasLoadingState=false;

    private TextView mFortuneTextview;

    private RequestQueue mQueue=null;

    private DelayedProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acme);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFortuneTextview = (TextView) findViewById(R.id.tv_acme_fortune);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchRequest();
            }
        });

        if(savedInstanceState!=null){
            storedValue= savedInstanceState.getString(CONTENT_TAG,null);
            wasLoadingState = savedInstanceState.getBoolean(LOADING_TAG,false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(storedValue!=null){
            mFortuneTextview.setText(storedValue);

            if(wasLoadingState){
                // if it was in a loading state during orientation change, then dispatching again
                dispatchRequest();
            }

        }else{
            // 1st time loading case
            dispatchRequest();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mFortuneTextview!=null && mFortuneTextview.getText()!=null){
            // saving the content
            outState.putCharSequence(CONTENT_TAG,mFortuneTextview.getText());
        }

        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            // saving the loading state
            outState.putBoolean(LOADING_TAG,true);
        }else{
            outState.putBoolean(LOADING_TAG,false);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if(savedInstanceState!=null){
            String storedVal= savedInstanceState.getString(CONTENT_TAG);
            if(storedVal!=null){
                mFortuneTextview.setText(storedVal);
            }

            boolean wasLoadingState = savedInstanceState.getBoolean(LOADING_TAG,false);

            if(wasLoadingState){
                dispatchRequest();
            }
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    private void dispatchRequest(){

        if(mQueue==null){
            mQueue = Volley.newRequestQueue(this);
        }else{
            mQueue.cancelAll(REQUEST_TAG);
        }
        startLoading();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ACME_API_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        stopLoading();
                        StringBuilder stringBuilder = new StringBuilder();
                        try {
                            JSONArray fortuneArray = response.getJSONArray("fortune");

                            for(int i=0;i<fortuneArray.length();i++){

                                stringBuilder.append(fortuneArray.getString(i));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(APP_TAG,e.getMessage());
                            mFortuneTextview.setText("Invalid JSON Structure");
                        }
                        mFortuneTextview.setText("Response: " + stringBuilder.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        stopLoading();
                        // TODO: Handle error
                        if (error.getMessage()!=null)
                            Log.d(APP_TAG, error.getMessage().toString());
                        else
                            Log.d(APP_TAG, "VolleyError message is null");

                        mFortuneTextview.setText("Error : "+error.getLocalizedMessage());
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, /* 10 sec timeout policy */
                0, /*no retry*/
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjectRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mQueue!=null){
            mQueue.cancelAll(REQUEST_TAG);
        }
        stopLoading();
    }

    private void startLoading(){
        stopLoading();
        mProgressDialog = DelayedProgressDialog.make(this,getString(R.string.dialog_title),getString(R.string.dialog_message),true);
        mProgressDialog.show();
    }

    private void stopLoading(){
        if(mProgressDialog!=null){
            mProgressDialog.dismiss();
            mProgressDialog=null;
        }
    }

}
