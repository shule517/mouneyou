package shule517.mouneyou;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Twitterの認証画面から発行されるIntentからUriを取得
        Uri uri = getIntent().getData();

        boolean result = GetAuthResult(uri);

        if (result) {
            // いいよ！を表示
            ImageView imageView = (ImageView) findViewById(R.id.resultImageView);
            imageView.setImageResource(R.drawable.iiyo);

            //oauth_verifierを取得する
            verifier = uri.getQueryParameter("oauth_verifier");
            AsyncTask<Context, Void, Boolean> task = new AsyncTask<Context, Void, Boolean>() {
                Context context;

                @Override
                protected Boolean doInBackground(Context... params) {
                    this.context = params[0];
                    //AccessTokenオブジェクトを取得
                    try {
                        token = MainActivity.oauth.getOAuthAccessToken(MainActivity.requestToken, verifier);
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
        } else {
            // だめですを表示
            ImageView imageView = (ImageView) findViewById(R.id.resultImageView);
            imageView.setImageResource(R.drawable.damedesu);

            TextView textView = (TextView) findViewById(R.id.resultTextView);
            textView.setText("認証に失敗しました・・・。\nもう一度Twitterの承認をしてください！");

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

    // 認証の判定
    private boolean GetAuthResult(Uri uri) {
        Log.d("TwitterOuth", "uri = " + uri.toString());
        if (uri == null) {
            Log.d("TwitterOuth", "認証失敗:uri = null");
            return false;
        } else if (uri.toString().startsWith("mouneyou://CallBackActivity?denied")) {
            Log.d("TwitterOuth", "認証失敗:uri = mouneyou://CallBackActivity?denied");
            return  false;
        } else if (uri.toString().startsWith("mouneyou://CallBackActivity")) {
            Log.d("TwitterOuth", "認証成功:uri = mouneyou://CallBackActivity");
            return true;
        } else {
            Log.d("TwitterOuth", "認証失敗:uri = else");
            return false;
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
