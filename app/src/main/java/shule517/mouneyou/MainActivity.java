package shule517.mouneyou;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

public class MainActivity extends AppCompatActivity {

    private DynamicGridView gridView;
    private StampDynamicAdapter adapter;
    private List<ListItem> list;

    public static RequestToken _req = null;
    public static OAuthAuthorization _oauth = null;
    public static Twitter twitter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        // Twitter認証
        AsyncTask<Context, Void, Boolean> task = new AsyncTask<Context, Void, Boolean>() {
            Context context;

            @Override
            protected Boolean doInBackground(Context... params) {
                try {
                    //Twitetr4Jの設定を読み込む
                    Configuration conf = ConfigurationContext.getInstance();

                    //Oauth認証オブジェクト作成
                    _oauth = new OAuthAuthorization(conf);
                    //Oauth認証オブジェクトにconsumerKeyとconsumerSecretを設定
                    _oauth.setOAuthConsumer("wgrDtnR6zi3sv2d6m6k3C9olS", "mZGlg1tOgElsoX4Ge7ymZzGlMaj2IRG8l3pWsxGQYVU0ECYKAZ");

                    // Tokenの取得
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(params[0]);
                    String token = sp.getString("Token", "");
                    String tokenSecret = sp.getString("TokenSecret", "");

                    if (token.length() > 0 && tokenSecret.length() > 0) {
                        AuthTwitter(token, tokenSecret);
                    } else {
                        //アプリの認証オブジェクト作成
                        try {
                            _req = _oauth.getOAuthRequestToken("mouneyou://oauth");
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                        String _uri;
                        _uri = _req.getAuthorizationURL();
                        startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(_uri)), 0);
                    }
                } catch (Exception ex) {
                    Log.e("twitter", ex.toString());
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
            }
        };
        task.execute(this);

        list = new ArrayList<ListItem>();

        String[][] stringList = loadStampSettings();

        for (String[] str : stringList) {
            ListItem item = new ListItem();
            item.setImageUrl(str[0]);
            item.setSrcUrl(str[1]);
            item.setImageId(R.drawable.sta);
            list.add(item);
        }

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this.getBaseContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getBaseContext(), 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("てゆうかツイートしよう。")
                .setSmallIcon(R.drawable.sta)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        manager.notify(0, notification);

        // スタンプ一覧Gridの設定
        gridView = (DynamicGridView) findViewById(R.id.listview);
        adapter = new StampDynamicAdapter(this, list, 4);
        gridView.setAdapter(adapter);

        // スタンプ長押しで並び替え
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d("GridView", "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d("GridView", String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        // スタンプタップでツイート画面を表示
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = (ListItem) adapter.getItem(position);
                DialogFragment newFragment = new TestDialogFragment(item, twitter, adapter, position);
                newFragment.show(getFragmentManager(), "");
            }
        });
    }

    @NonNull
    private String[][] loadStampSettings() {

        // Tokenの取得
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int stampCount = sp.getInt("stamp_count", 0);

        if (stampCount != 0) {
            String[][] list = new String[stampCount][2];
            for (int i = 0; i < stampCount; i++) {
                String imageUrl = sp.getString("stamp" + i + "_imageurl", "");
                String srcUrl = sp.getString("stamp" + i + "_srcurl", "");
                String[] item = {imageUrl, srcUrl};
                list[i] = item;
            }
            return list;
        } else {

            String[][] list = new String[][]{
                    {
                            "/images/stamp/stamp001.png",
                            "http://pic.twitter.com/zJf9UxVkCy"
                    }, {
                    "/images/stamp/stamp002.png",
                    "http://pic.twitter.com/gGWnG2T1N2"
            }, {
                    "/images/stamp/stamp003.png",
                    "http://pic.twitter.com/8dnYyZqEoF"
            }, {
                    "/images/stamp/stamp004.png",
                    "http://pic.twitter.com/zjx4PUhbmY"
            }, {
                    "/images/stamp/stamp005.png",
                    "http://pic.twitter.com/kHnGxG95CI"
            }, {
                    "/images/stamp/stamp006.png",
                    "http://pic.twitter.com/SbRtVCDOit"
            }, {
                    "/images/stamp/stamp007.png",
                    "http://pic.twitter.com/zIdqC5MtxE"
            }, {
                    "/images/stamp/stamp008.png",
                    "http://pic.twitter.com/yYZEcqQCGZ"
            }, {
                    "/images/stamp/stamp009.png",
                    "http://pic.twitter.com/dYwZ3axpKW"
            }, {
                    "/images/stamp/stamp010.png",
                    "http://pic.twitter.com/Z0yAX0QcHJ",
            }, {
                    "/images/stamp/stamp011.png",
                    "http://pic.twitter.com/M6I6regjKd",
            }, {
                    "/images/stamp/stamp012.png",
                    "http://pic.twitter.com/Iv8gvPUATM"
            }, {
                    "/images/stamp/stamp013.png",
                    "http://pic.twitter.com/sQOagjwDRJ"
            }, {
                    "/images/stamp/stamp014.png",
                    "http://pic.twitter.com/1iWz6NfP9t"
            }, {
                    "/images/stamp/stamp015.png",
                    "http://pic.twitter.com/tyXVCqpQTc"
            }, {
                    "/images/stamp/stamp016.png",
                    "http://pic.twitter.com/e1CILuyhgc"
            }, {
                    "/images/stamp/stamp017.png",
                    "http://pic.twitter.com/jmUVSC3zXK"
            }, {
                    "/images/stamp/stamp018.png",
                    "http://pic.twitter.com/2pd753IzXC"
            }, {
                    "/images/stamp/stamp019.png",
                    "http://pic.twitter.com/e1B9k2MXgS"
            }, {
                    "/images/stamp/stamp020.png",
                    "http://pic.twitter.com/wGwLC41tdH"
            }, {
                    "/images/stamp/stamp021.png",
                    "http://pic.twitter.com/SYkLTqipqL"
            }, {
                    "/images/stamp/stamp022.png",
                    "http://pic.twitter.com/wb79W80JeH"
            }, {
                    "/images/stamp/stamp023.png",
                    "http://pic.twitter.com/YYwHReTUBS",
            }, {
                    "/images/stamp/stamp024.png",
                    "http://pic.twitter.com/VnmJEufqY2"
            }, {
                    "/images/stamp/stamp025.png",
                    "http://pic.twitter.com/6viZHnOBEn"
            }, {
                    "/images/stamp/stamp026.png",
                    "http://pic.twitter.com/MMDWi2jWHB"
            }, {
                    "/images/stamp/stamp027.png",
                    "http://pic.twitter.com/kDzFQfo6ty"
            }, {
                    "/images/stamp/stamp028.png",
                    "http://pic.twitter.com/cuWqlvgib3"
            }, {
                    "/images/stamp/stamp029.png",
                    "http://pic.twitter.com/RQdFyGXkcv"
            }, {
                    "/images/stamp/stamp030.png",
                    "http://pic.twitter.com/4xZ45MTCNv"
            }, {
                    "/images/stamp/stamp031.png",
                    "http://pic.twitter.com/M1zjqfk8pH"
            }, {
                    "/images/stamp/stamp032.png",
                    "http://pic.twitter.com/agDkVgLd3n"
            }, {
                    "/images/stamp/stamp033.png",
                    "http://pic.twitter.com/3wfkhxsCH1",
            }, {
                    "/images/stamp/stamp034.png",
                    "http://pic.twitter.com/29RqnztSk9"
            }, {
                    "/images/stamp/stamp035.png",
                    "http://pic.twitter.com/XiMVmrDUWp"
            }, {
                    "/images/stamp/stamp036.png",
                    "http://pic.twitter.com/hXcB5g6Ygq"
            }, {
                    "/images/stamp/stamp037.png",
                    "http://pic.twitter.com/XWopb3cOts"
            }, {
                    "/images/stamp/stamp038.png",
                    "http://pic.twitter.com/E9kjtoqJRR"
            }, {
                    "/images/stamp/stamp039.png",
                    "http://pic.twitter.com/CQkpQ8Altc"
            }, {
                    "/images/stamp/stamp040.png",
                    "http://pic.twitter.com/sA362F6rix"
            }, {
                    "/images/stamp/stamp041.png",
                    "http://pic.twitter.com/Gg4ehjqB97"
            }, {
                    "/images/stamp/stamp042.png",
                    "http://pic.twitter.com/i0LrCSSpzB"
            }, {
                    "/images/stamp/stamp043.png",
                    "http://pic.twitter.com/YaskkVfYyo"
            }, {
                    "/images/stamp/stamp044.png",
                    "http://pic.twitter.com/Hg3WV1gKNj"
            }, {
                    "/images/stamp/stamp045.png",
                    "http://pic.twitter.com/e2idANGP7m"
            }, {
                    "/images/stamp/stamp046.png",
                    "http://pic.twitter.com/8ai4ePmRbt"
            }, {
                    "/images/stamp/stamp047.png",
                    "http://pic.twitter.com/b8eOJc3rdn"
            }, {
                    "/images/stamp/stamp048.png",
                    "http://pic.twitter.com/VHKZpeN8eU"
            }, {
                    "/images/stamp/stamp049.png",
                    "http://pic.twitter.com/42hJHOGPHm"
            }, {
                    "/images/stamp/stamp050.png",
                    "http://pic.twitter.com/arr2ECWUtM"
            }, {
                    "/images/stamp/stamp051.png",
                    "http://pic.twitter.com/LUKtLqZeve"
            }, {
                    "/images/stamp/stamp052.png",
                    "http://pic.twitter.com/v7l5tGeSdq"
            }, {
                    "/images/stamp/stamp053.png",
                    "http://pic.twitter.com/uWYAxKQpmF"
            }, {
                    "/images/stamp/stamp054.png",
                    "http://pic.twitter.com/a1O9gQjnx6"
            }
            };

            for (String[] item : list) {
                item[0] = "http://mouneyou.rgx6.com/" + item[0];
            }
            return list;
        }
    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sp.edit();

            // スタンプ数の保存
            editor.putInt("stamp_count", adapter.getCount());
            // 画像URLの保存
            for (int i = 0; i < adapter.getCount(); i++) {
                ListItem item = (ListItem) adapter.getItem(i);
                editor.putString("stamp" + i + "_imageurl", item.getImageUrl());
                editor.putString("stamp" + i + "_srcurl", item.getSrcUrl());
            }
            editor.commit();
        } else {
            super.onBackPressed();
        }
    }

    // Twitter認証
    public static void AuthTwitter(String token, String tokenSecret) {
        AccessToken accessToken = new AccessToken(token, tokenSecret);

        //twitterオブジェクトの作成
        twitter = new TwitterFactory().getInstance();

        //Consumer keyとConsumer key seacretの設定
        twitter.setOAuthConsumer("wgrDtnR6zi3sv2d6m6k3C9olS", "mZGlg1tOgElsoX4Ge7ymZzGlMaj2IRG8l3pWsxGQYVU0ECYKAZ");

        //AccessTokenオブジェクトを設定
        twitter.setOAuthAccessToken(accessToken);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem target = menu.add(0,0,0,"1");
        menu.add(0,1,0,"2");
        target.setIcon(R.drawable.sta);

        return true;
    }
    */

    /*
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
