package shule517.mouneyou;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by shule517 on 2015/09/27.
 */
public
// Image取得用スレッドクラス
class StampImageGetTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView image;
    private String imageUrl;
    private ListItem item;
    private Context context;

    public StampImageGetTask(Context context, ImageView image, ListItem item) {
        this.context = context;
        this.image = image;
        this.imageUrl = image.getTag().toString();
        this.item = item;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap image = null;
        BitmapFactory.Options options;
        String imageUrl = params[0];

        try {
            // アプリ内に画像が保存されているかチェック
            InputStream input = null;
            try {
                input = this.context.openFileInput(imageUrl.replace("/", ""));
                Bitmap bitmapImage = BitmapFactory.decodeStream(input);
                return bitmapImage;
            } catch (Exception e) {
                Log.e("画像取得", e.getMessage());
            }

            // スタンプ画像をdownload
            URL url = new URL(imageUrl);
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            InputStream is = (InputStream) url.getContent();
            image = BitmapFactory.decodeStream(is, null, options);
            is.close();

            // 画像を保存
            FileOutputStream out = null;
            try {
                out = this.context.openFileOutput(imageUrl.replace("/", ""), Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (FileNotFoundException e) {
                Log.e("画像取得", e.getMessage());
            } finally {
                if (out != null) {
                    out.close();
                    out = null;
                }
            }
        } catch (Exception e) {
            Log.e("画像取得", e.getMessage());
        }

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // Tagが同じものが確認して、同じであれば画像を設定する
        if (imageUrl.equals(image.getTag())) {
            image.setImageBitmap(result);
        }

        item.setBitmap(result);
    }
}
