package shule517.mouneyou;

import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    List<ListItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        list = new ArrayList<ListItem>();

        String[][] stringList =
                {
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

        for (String[] str : stringList) {
            ListItem item = new ListItem();
            item.setImageUrl("http://mouneyou.rgx6.com" + str[0]);
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

        // adapterのインスタンスを作成
        ImageArrayAdapter adapter =
                new ImageArrayAdapter(this, R.layout.list_view_image_item, list);

        gridView = (GridView) findViewById(R.id.listview);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = list.get(position);

                // ダイアログを表示する
                DialogFragment newFragment = new TestDialogFragment(item);
                newFragment.show(getFragmentManager(), "test tweet!!");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
