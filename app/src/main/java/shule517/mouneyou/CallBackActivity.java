package shule517.mouneyou;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

public class CallBackActivity extends AppCompatActivity {

    AccessToken token = null;
    String verifier;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_back);

        this.context = this;

        //Twitterの認証画面から発行されるIntentからUriを取得
        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith("mouneyou://oauth")) {
            //oauth_verifierを取得する
            verifier = uri.getQueryParameter("oauth_verifier");
            AsyncTask<Context, Void, Boolean> task = new AsyncTask<Context, Void, Boolean>() {
                Context context;

                @Override
                protected Boolean doInBackground(Context... params) {
                    this.context = params[0];
                    //AccessTokenオブジェクトを取得
                    try {
                        token = MainActivity._oauth.getOAuthAccessToken(MainActivity._req, verifier);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    MainActivity.AuthTwitter(token.getToken(), token.getTokenSecret());

                    // Tokenを保存
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
                    sp.edit().putString("Token", token.getToken()).commit();
                    sp.edit().putString("TokenSecret", token.getTokenSecret()).commit();
                }
            }.execute(this);

            Button button = (Button) findViewById(R.id.okButton);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Main画面へ戻る
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
