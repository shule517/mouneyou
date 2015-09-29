package shule517.mouneyou;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shule517 on 2015/09/28.
 */
public class TestDialogFragment extends DialogFragment implements View.OnClickListener {
    ListItem stampImage;
    TextView textView;

    public TestDialogFragment() {
    }

    public TestDialogFragment(ListItem stampImage) {
        this.stampImage = stampImage;
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
    }

    public void tweet() {
        String comment = textView.getText().toString();
        if (comment.length() > 0) {
            comment += "\n";
        }
        String text = String.format("%s%s #てゆうかもう寝よう #すたンプ", comment, stampImage.getSrcUrl());
        TweetTask task = new TweetTask(text);
        task.execute();

        /*
        String text = Uri.encode(textView.getText().toString() + "\n");
        String tag = Uri.encode("#てゆうかもう寝よう #すたンプ");
        String intentUrl = String.format("twitter.com/intent/tweet?lang=ja&text=%s%s http://mouneyou.rgx6.com/ %s", text, stampImage.getSrcUrl(), tag);
        Uri uri = Uri.parse("https://" + intentUrl);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
        */
    }
}
