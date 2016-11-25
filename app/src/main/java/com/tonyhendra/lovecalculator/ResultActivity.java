package com.tonyhendra.lovecalculator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {
    private String TAG = ResultActivity.class.getSimpleName();
    private String f_name,s_name,txt_result,percentage;
    private static String lovecalculator_url;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle b = getIntent().getExtras();
        if(b!=null){
            f_name = b.getString("first");
            f_name = f_name.replaceAll(" ","+");
            s_name = b.getString("second");
            s_name = s_name.replaceAll(" ","+");
            lovecalculator_url = "https://love-calculator.p.mashape.com/getPercentage?fname="+f_name+"&sname="+s_name;
            new getResult().execute();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private class getResult extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ResultActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            HTTPHandler sh = new HTTPHandler();
            String jsonStr = sh.makeServiceCall(lovecalculator_url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr!=null){
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    f_name = jsonObj.getString("fname");
                    s_name = jsonObj.getString("sname");
                    txt_result = jsonObj.getString("result");
                    percentage = jsonObj.getString("percentage");

                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
                pDialog.dismiss();
            try{
                TextView tv_fname = (TextView)findViewById(R.id.tv_first);
                TextView tv_sname = (TextView)findViewById(R.id.tv_second);
                TextView tv_result = (TextView)findViewById(R.id.tv_result);
                TextView tv_and = (TextView)findViewById(R.id.tv_and);
                tv_and.setText("and");
                final TextView tv_resultPercentage = (TextView)findViewById(R.id.tv_resultPercentage);
                tv_fname.setText(f_name);
                tv_sname.setText(s_name);
                tv_result.setText(txt_result);
                ProgressBar mProgressBar;
                mProgressBar = (ProgressBar)findViewById(R.id.prog_result);
                int result = Integer.parseInt(percentage);
                ValueAnimator animator = new ValueAnimator();
                animator.setObjectValues(0, result);
                animator.setDuration(3000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tv_resultPercentage.setText("" + (int) animation.getAnimatedValue()+"%");
                    }
                });
                animator.start();
                ObjectAnimator anim = ObjectAnimator.ofInt(mProgressBar, "Progress",0,result);
                anim.setDuration(3000);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.start();
            }catch (Exception e){

            }
        }
    }
}
