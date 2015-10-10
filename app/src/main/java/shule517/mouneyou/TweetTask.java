package shule517.mouneyou;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by shule517 on 2015/09/27.
 */
public
// Image取得用スレッドクラス
class TweetTask extends AsyncTask<String, Void, Boolean> {

    String text;
    String comment;
    Activity context;
    Bitmap bitmap;
    Twitter twitter;

    TweetTask(String text, String comment, Activity context, Bitmap bitmap, Twitter twitter) {
        this.text = text;
        this.comment = comment;
        this.context = context;
        this.bitmap = bitmap;
        this.twitter = twitter;
    }

    private Boolean tweet() {
        try {
            try {
                // get request token.
                // this will throw IllegalStateException if access token is already available
                RequestToken requestToken = twitter.getOAuthRequestToken();
                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                AccessToken accessToken = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                    return false;
                }
            }
            twitter4j.Status status = twitter.updateStatus(text);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
            return true;
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            return false;
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return tweet();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (!result) {
            Toast.makeText(this.context, "ツイートに失敗しました。", Toast.LENGTH_LONG).show();
            return;
        }

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_tweeted_toast, null);

        // スタンプ画像を設定
        ImageView stampImage = (ImageView) view.findViewById(R.id.stampImage);
        stampImage.setImageBitmap(this.bitmap);

        // 画面サイズを取得する
        Display disp = this.context.getWindowManager().getDefaultDisplay();

        // 画面の幅(Pixel)÷画像ファイルの幅(Pixel)＝画面いっぱいに表示する場合の倍率
        float factor = (disp.getWidth() / this.bitmap.getWidth()) * 0.8f;

        // 表示サイズ(Pixel)を指定して、LayoutParamsを生成(ImageViewはこのサイズになる)
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                (int) (this.bitmap.getWidth() * factor), (int) (this.bitmap.getHeight() * factor));
        // 中央に表示する
        lp.gravity = Gravity.CENTER;

        // LayoutParamsをImageViewに設定
        stampImage.setLayoutParams(lp);

        // ImageViewのMatrixに拡大率を指定
        Matrix m = stampImage.getImageMatrix();
        m.reset();
        m.postScale(factor, factor);
        stampImage.setImageMatrix(m);

        // ツイート内容を設定
        TextView tweetText = (TextView) view.findViewById(R.id.tweetText);
        tweetText.setText(this.text);
        Log.i("Tweet内容", this.text);

        // Toastを表示
        Toast toast = new Toast(this.context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}
