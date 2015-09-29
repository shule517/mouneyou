package shule517.mouneyou;

/**
 * Created by shule517 on 2015/08/25.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImageArrayAdapter extends ArrayAdapter<ListItem> {

    private int resourceId;
    private List<ListItem> items;
    private LayoutInflater inflater;

    public ImageArrayAdapter(Context context, int resourceId, List<ListItem> items) {
        super(context, resourceId, items);

        this.resourceId = resourceId;
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }

        ListItem item = this.items.get(position);

        // テキストをセット
        /*
        TextView appInfoText = (TextView)view.findViewById(R.id.item_text);
        appInfoText.setImageUrl(stampImage.getImageUrl());
        */
        String imageUrl = item.getImageUrl();
        String str = String.format("test : %s", imageUrl);
        //Log.d("tag", str);

        // アイコンをセット
        ImageView image = (ImageView) view.findViewById(R.id.item_image);
        if (item.getBitmap() == null) {
            // Bitmapを取得
            image.setImageResource(item.getImageId());
            image.setTag(imageUrl);

            // 画像取得スレッド起動
            ImageGetTask task = new ImageGetTask(image, item);
            task.execute(imageUrl);
        } else {
            // 取得済みのBitmapを設定
            image.setImageBitmap(item.getBitmap());
        }

        return view;
    }
}
