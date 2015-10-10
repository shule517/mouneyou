package shule517.mouneyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

/**
 * Created by shule517 on 2015/10/04.
 */
public class StampDynamicAdapter extends BaseDynamicGridAdapter {
    public StampDynamicAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StampViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_image_item, null);
            holder = new StampViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StampViewHolder) convertView.getTag();
        }
        holder.build((ListItem) getItem(position));

        return convertView;
    }

    private class StampViewHolder {
        private ImageView image;
        private View view;

        private StampViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.item_image);
            this.view = view;
        }

        void build(ListItem item) {
            // アイコンをセット
            String imageUrl = item.getImageUrl();

            if (item.getBitmap() == null) {
                // Bitmapを取得
                image.setImageResource(item.getImageId());
                image.setTag(imageUrl);

                // 画像取得スレッド起動
                StampImageGetTask task = new StampImageGetTask(view.getContext(), image, item);
                task.execute(imageUrl);
            } else {
                // 取得済みのBitmapを設定
                image.setImageBitmap(item.getBitmap());
            }
        }
    }
}
