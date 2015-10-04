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
                            "stamp001",
                            "http://pic.twitter.com/zJf9UxVkCy"
                    }, {
                    "stamp002",
                    "http://pic.twitter.com/gGWnG2T1N2"
            }, {
                    "stamp003",
                    "http://pic.twitter.com/8dnYyZqEoF"
            }, {
                    "stamp004",
                    "http://pic.twitter.com/zjx4PUhbmY"
            }, {
                    "stamp005",
                    "http://pic.twitter.com/kHnGxG95CI"
            }, {
                    "stamp006",
                    "http://pic.twitter.com/SbRtVCDOit"
            }, {
                    "stamp007",
                    "http://pic.twitter.com/zIdqC5MtxE"
            }, {
                    "stamp008",
                    "http://pic.twitter.com/yYZEcqQCGZ"
            }, {
                    "stamp009",
                    "http://pic.twitter.com/dYwZ3axpKW"
            }, {
                    "stamp010",
                    "http://pic.twitter.com/Z0yAX0QcHJ",
            }, {
                    "stamp011",
                    "http://pic.twitter.com/M6I6regjKd",
            }, {
                    "stamp012",
                    "http://pic.twitter.com/Iv8gvPUATM"
            }, {
                    "stamp013",
                    "http://pic.twitter.com/sQOagjwDRJ"
            }, {
                    "stamp014",
                    "http://pic.twitter.com/1iWz6NfP9t"
            }, {
                    "stamp015",
                    "http://pic.twitter.com/tyXVCqpQTc"
            }, {
                    "stamp016",
                    "http://pic.twitter.com/e1CILuyhgc"
            }, {
                    "stamp017",
                    "http://pic.twitter.com/jmUVSC3zXK"
            }, {
                    "stamp018",
                    "http://pic.twitter.com/2pd753IzXC"
            }, {
                    "stamp019",
                    "http://pic.twitter.com/e1B9k2MXgS"
            }, {
                    "stamp020",
                    "http://pic.twitter.com/wGwLC41tdH"
            }, {
                    "stamp021",
                    "http://pic.twitter.com/SYkLTqipqL"
            }, {
                    "stamp022",
                    "http://pic.twitter.com/wb79W80JeH"
            }, {
                    "stamp023",
                    "http://pic.twitter.com/YYwHReTUBS",
            }, {
                    "stamp024",
                    "http://pic.twitter.com/VnmJEufqY2"
            }, {
                    "stamp025",
                    "http://pic.twitter.com/6viZHnOBEn"
            }, {
                    "stamp026",
                    "http://pic.twitter.com/MMDWi2jWHB"
            }, {
                    "stamp027",
                    "http://pic.twitter.com/kDzFQfo6ty"
            }, {
                    "stamp028",
                    "http://pic.twitter.com/cuWqlvgib3"
            }, {
                    "stamp029",
                    "http://pic.twitter.com/RQdFyGXkcv"
            }, {
                    "stamp030",
                    "http://pic.twitter.com/4xZ45MTCNv"
            }, {
                    "stamp031",
                    "http://pic.twitter.com/M1zjqfk8pH"
            }, {
                    "stamp032",
                    "http://pic.twitter.com/agDkVgLd3n"
            }, {
                    "stamp033",
                    "http://pic.twitter.com/3wfkhxsCH1",
            }, {
                    "stamp034",
                    "http://pic.twitter.com/29RqnztSk9"
            }, {
                    "stamp035",
                    "http://pic.twitter.com/XiMVmrDUWp"
            }, {
                    "stamp036",
                    "http://pic.twitter.com/hXcB5g6Ygq"
            }, {
                    "stamp037",
                    "http://pic.twitter.com/XWopb3cOts"
            }, {
                    "stamp038",
                    "http://pic.twitter.com/E9kjtoqJRR"
            }, {
                    "stamp039",
                    "http://pic.twitter.com/CQkpQ8Altc"
            }, {
                    "stamp040",
                    "http://pic.twitter.com/sA362F6rix"
            }, {
                    "stamp041",
                    "http://pic.twitter.com/Gg4ehjqB97"
            }, {
                    "stamp042",
                    "http://pic.twitter.com/i0LrCSSpzB"
            }, {
                    "stamp043",
                    "http://pic.twitter.com/YaskkVfYyo"
            }, {
                    "stamp044",
                    "http://pic.twitter.com/Hg3WV1gKNj"
            }, {
                    "stamp045",
                    "http://pic.twitter.com/e2idANGP7m"
            }, {
                    "stamp046",
                    "http://pic.twitter.com/8ai4ePmRbt"
            }, {
                    "stamp047",
                    "http://pic.twitter.com/b8eOJc3rdn"
            }, {
                    "stamp048",
                    "http://pic.twitter.com/VHKZpeN8eU"
            }, {
                    "stamp049",
                    "http://pic.twitter.com/42hJHOGPHm"
            }, {
                    "stamp050",
                    "http://pic.twitter.com/arr2ECWUtM"
            }, {
                    "stamp051",
                    "http://pic.twitter.com/LUKtLqZeve"
            }, {
                    "stamp052",
                    "http://pic.twitter.com/v7l5tGeSdq"
            }, {
                    "stamp053",
                    "http://pic.twitter.com/uWYAxKQpmF"
            }, {
                    "stamp054",
                    "http://pic.twitter.com/a1O9gQjnx6"
            }
            };

            for (String[] item : list) {
                item[0] = "http://mouneyou.rgx6.com/images/stamp/" + item[0] + ".png";
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
