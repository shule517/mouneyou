package shule517.mouneyou;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import twitter4j.Twitter;

/**
 * Created by shule517 on 2015/09/28.
 */
public class TweetDialogFragment extends DialogFragment implements View.OnClickListener {
    private StampGridItem stampImage;
    private TextView textView;
    private Twitter twitter;
    private StampGridAdapter adapter;
    private int position;

    public TweetDialogFragment() {
    }

    public void setDialogFragment(StampGridItem stampImage, Twitter twitter, StampGridAdapter adapter, int position) {
        this.stampImage = stampImage;
        this.twitter = twitter;
        this.adapter = adapter;
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Viewを設定
        View view = inflater.inflate(R.layout.activity_tweet, null);
        builder.setView(view);

        // アイコン画像をセット
        ImageView image = (ImageView) view.findViewById(R.id.stamp);
        image.setImageBitmap(this.stampImage.getBitmap());

        // テキストインスタンスを取得
        textView = (TextView) view.findViewById(R.id.tweetEditText);

        // スタンプ画像タップイベント
        image.setClickable(true);
        image.setOnClickListener(this);

        // ボタンタップイベント
        Button button = (Button) view.findViewById(R.id.tweetButton);
        button.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        tweet();
        dismiss();

        // ツイートしたスタンプを左上に移動
        adapter.reorderItems(position, 0);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sp.edit();        // スタンプ数の保存
        editor.putInt("stamp_count", adapter.getCount());
        // 画像URLの保存
        for (int i = 0; i < adapter.getCount(); i++) {
            StampGridItem item = (StampGridItem) adapter.getItem(i);
            editor.putString("stamp" + i + "_imageurl", item.getImageUrl());
            editor.putString("stamp" + i + "_srcurl", item.getSrcUrl());
        }
        editor.commit();
    }

    public void tweet() {
        String comment = textView.getText().toString();
        if (comment.length() > 0) {
            comment += "\n";
        }
        String text = String.format("%s#てゆうかもう寝よう\n#すたンプ\nhttp://mouneyou.rgx6.com/\n%s", comment, stampImage.getSrcUrl());
        TweetTask task = new TweetTask(text, textView.getText().toString(), getActivity(), this.stampImage.getBitmap(), this.twitter);
        task.execute();
    }
}
