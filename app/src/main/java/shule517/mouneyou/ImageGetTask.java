package shule517.mouneyou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by shule517 on 2015/09/27.
 */
public
// Image取得用スレッドクラス
class ImageGetTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView image;
    private String imageUrl;
    private ListItem item;

    public ImageGetTask(ImageView _image, ListItem _item) {
        image = _image;
        imageUrl = _image.getTag().toString();
        item = _item;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        // ここでHttp経由で画像を取得します。取得後Bitmapで返します。

        Bitmap image = null;
        BitmapFactory.Options options;
        String imageUrl = params[0];
        try {
            // インターネット上の画像を取得して、Bitmap に変換
            URL url = new URL(imageUrl);
            options = new BitmapFactory.Options();
            Log.i("画像取得", "imageUrl:" + imageUrl);

            // 実際に読み込む
            options.inJustDecodeBounds = false;
            InputStream is = (InputStream) url.getContent();
            image = BitmapFactory.decodeStream(is, null, options);
            is.close();
        } catch (Exception e) {
            Log.i("button", e.getMessage());
        }

        Log.i("画像取得", "OK url:" + imageUrl);
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
