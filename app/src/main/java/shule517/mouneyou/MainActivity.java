package shule517.mouneyou;

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

        List<String[]> stringList = loadStampSettings();

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
        gridView.setClipToPadding(false);
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
                TweetDialogFragment newFragment = new TweetDialogFragment();
                newFragment.setDialogFragment(item, twitter, adapter, position);
                newFragment.show(getFragmentManager(), "");
            }
        });
    }

    @NonNull
    private List<String[]> loadStampSettings() {

        // Tokenの取得
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int stampCount = sp.getInt("stamp_count", 0);

        if (stampCount != 0) {
            List<String[]> list = new ArrayList<String[]>();
            for (int i = 0; i < stampCount; i++) {
                String imageUrl = sp.getString("stamp" + i + "_imageurl", "");
                String srcUrl = sp.getString("stamp" + i + "_srcurl", "");
                String[] item = {imageUrl, srcUrl};
                list.add(item);
            }
            return list;
        } else {
            /*
            List<String[]> list = new ArrayList<String[]>();
            try {
                SrcGetTask task = new SrcGetTask(list);
                task.execute("http://mouneyou.rgx6.com/javascripts/client.js");
                task.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
            List<String[]> list = new ArrayList<String[]>();
            list.add(new String[]{"stamp001", "http://pic.twitter.com/zJf9UxVkCy"});
            list.add(new String[]{"stamp002", "http://pic.twitter.com/gGWnG2T1N2"});
            list.add(new String[]{"stamp003", "http://pic.twitter.com/8dnYyZqEoF"});
            list.add(new String[]{"stamp004", "http://pic.twitter.com/zjx4PUhbmY"});
            list.add(new String[]{"stamp005", "http://pic.twitter.com/kHnGxG95CI"});
            list.add(new String[]{"stamp006", "http://pic.twitter.com/SbRtVCDOit"});
            list.add(new String[]{"stamp007", "http://pic.twitter.com/zIdqC5MtxE"});
            list.add(new String[]{"stamp008", "http://pic.twitter.com/yYZEcqQCGZ"});
            list.add(new String[]{"stamp009", "http://pic.twitter.com/dYwZ3axpKW"});
            list.add(new String[]{"stamp010", "http://pic.twitter.com/Z0yAX0QcHJ"});
            list.add(new String[]{"stamp011", "http://pic.twitter.com/M6I6regjKd"});
            list.add(new String[]{"stamp012", "http://pic.twitter.com/Iv8gvPUATM"});
            list.add(new String[]{"stamp013", "http://pic.twitter.com/sQOagjwDRJ"});
            list.add(new String[]{"stamp014", "http://pic.twitter.com/1iWz6NfP9t"});
            list.add(new String[]{"stamp015", "http://pic.twitter.com/tyXVCqpQTc"});
            list.add(new String[]{"stamp016", "http://pic.twitter.com/e1CILuyhgc"});
            list.add(new String[]{"stamp017", "http://pic.twitter.com/jmUVSC3zXK"});
            list.add(new String[]{"stamp018", "http://pic.twitter.com/2pd753IzXC"});
            list.add(new String[]{"stamp019", "http://pic.twitter.com/e1B9k2MXgS"});
            list.add(new String[]{"stamp020", "http://pic.twitter.com/wGwLC41tdH"});
            list.add(new String[]{"stamp021", "http://pic.twitter.com/SYkLTqipqL"});
            list.add(new String[]{"stamp022", "http://pic.twitter.com/wb79W80JeH"});
            list.add(new String[]{"stamp023", "http://pic.twitter.com/YYwHReTUBS"});
            list.add(new String[]{"stamp024", "http://pic.twitter.com/VnmJEufqY2"});
            list.add(new String[]{"stamp025", "http://pic.twitter.com/6viZHnOBEn"});
            list.add(new String[]{"stamp026", "http://pic.twitter.com/MMDWi2jWHB"});
            list.add(new String[]{"stamp027", "http://pic.twitter.com/kDzFQfo6ty"});
            list.add(new String[]{"stamp028", "http://pic.twitter.com/cuWqlvgib3"});
            list.add(new String[]{"stamp029", "http://pic.twitter.com/RQdFyGXkcv"});
            list.add(new String[]{"stamp030", "http://pic.twitter.com/4xZ45MTCNv"});
            list.add(new String[]{"stamp031", "http://pic.twitter.com/M1zjqfk8pH"});
            list.add(new String[]{"stamp032", "http://pic.twitter.com/agDkVgLd3n"});
            list.add(new String[]{"stamp033", "http://pic.twitter.com/3wfkhxsCH1"});
            list.add(new String[]{"stamp034", "http://pic.twitter.com/29RqnztSk9"});
            list.add(new String[]{"stamp035", "http://pic.twitter.com/XiMVmrDUWp"});
            list.add(new String[]{"stamp036", "http://pic.twitter.com/hXcB5g6Ygq"});
            list.add(new String[]{"stamp037", "http://pic.twitter.com/XWopb3cOts"});
            list.add(new String[]{"stamp038", "http://pic.twitter.com/E9kjtoqJRR"});
            list.add(new String[]{"stamp039", "http://pic.twitter.com/CQkpQ8Altc"});
            list.add(new String[]{"stamp040", "http://pic.twitter.com/sA362F6rix"});
            list.add(new String[]{"stamp041", "http://pic.twitter.com/Gg4ehjqB97"});
            list.add(new String[]{"stamp042", "http://pic.twitter.com/i0LrCSSpzB"});
            list.add(new String[]{"stamp043", "http://pic.twitter.com/YaskkVfYyo"});
            list.add(new String[]{"stamp044", "http://pic.twitter.com/Hg3WV1gKNj"});
            list.add(new String[]{"stamp045", "http://pic.twitter.com/e2idANGP7m"});
            list.add(new String[]{"stamp046", "http://pic.twitter.com/8ai4ePmRbt"});
            list.add(new String[]{"stamp048", "http://pic.twitter.com/VHKZpeN8eU"});
            list.add(new String[]{"stamp049", "http://pic.twitter.com/42hJHOGPHm"});
            list.add(new String[]{"stamp050", "http://pic.twitter.com/arr2ECWUtM"});
            list.add(new String[]{"stamp051", "http://pic.twitter.com/LUKtLqZeve"});
            list.add(new String[]{"stamp052", "http://pic.twitter.com/v7l5tGeSdq"});
            list.add(new String[]{"stamp053", "http://pic.twitter.com/uWYAxKQpmF"});
            list.add(new String[]{"stamp054", "http://pic.twitter.com/a1O9gQjnx6"});

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
