package com.gerenvip.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.gerenvip.banner.utils.LogHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

/**
 * 无限循环adapter
 *
 * @author wangwei_cs
 */
public class AdImagePagerAdapter extends PagerAdapter {

    protected static final String TAG = "AdImagePagerAdapter";
    private List<String> imageUrls;
    private LayoutInflater inflater;
    private Context mCxt;
    private ViewPagerItemClickListener mListener;
    protected ImageLoader imageLoader;
    private DisplayImageOptions options;

    /**
     * ViewPager的item点击事件监听
     *
     * @author wangwei_cs
     */
    public interface ViewPagerItemClickListener {
        /**
         * @param position 被点击的角标
         */
        public void OnViewPagerItemClick(int position);
    }

    public AdImagePagerAdapter(Context context, List<String> imageUrls, ViewPagerItemClickListener listener) {
        this.imageUrls = imageUrls;
        this.mCxt = context;
        inflater = LayoutInflater.from(context);
        this.mListener = listener;
        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ad_default)
                .showImageOnFail(R.drawable.ad_default)
                .showImageOnLoading(R.drawable.ad_default)
                .resetViewBeforeLoading(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public int getCount() {
        if (imageUrls.size() < 2) {
            return imageUrls.size();
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int index = position % imageUrls.size();
        View view = inflater.inflate(R.layout.item_pager_image, container, false);
        assert view != null;
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.img_loading);
        imageLoader.displayImage(imageUrls.get(index), imageView, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        spinner.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        String message = null;
                        switch (failReason.getType()) {
                            case IO_ERROR:
                                message = "Input/Output error";
                                break;
                            case DECODING_ERROR:
                                message = "Image can't be decoded";
                                break;
                            case NETWORK_DENIED:
                                message = "Downloads are denied";
                                break;
                            case OUT_OF_MEMORY:
                                message = "Out Of Memory error";
                                break;
                            case UNKNOWN:
                                message = "Unknown error";
                                break;
                        }
                        LogHelper.e(TAG, message);
                        spinner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        spinner.setVisibility(View.GONE);
                    }
                });

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnViewPagerItemClick(index);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
