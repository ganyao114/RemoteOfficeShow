package com.gy.myframework.Service.loader.imgloader.strategy.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import com.gy.myframework.R;
import com.gy.myframework.Service.loader.imgloader.strategy.mystrategy.entity.PhotoToLoad;
import com.gy.myframework.Service.thread.pool.impl.MyWorkThreadQueue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class ImageLoader implements ImgLoadThreadCallBack {
	private MemoryCache memoryCache = new MemoryCache();
	public FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView,String>());
	private int DefautImg_id = R.drawable.loading;
	private Context context;
	private MyHandler handler;
	public ImageLoader(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		handler = new MyHandler(this);
		fileCache = new FileCache(context);
	}
	
	public ImageLoader(Context context,int imgSize) {
		// TODO Auto-generated constructor stub
		this.context = context;
		handler = new MyHandler(this);
		fileCache = new FileCache(context);
	}
	
	
	public void DisplayImage(String url,ImageView imageView)
	{
		imageViews.put(imageView, url);
		
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}else {
			queuePhoto(url, imageView);
			imageView.setImageResource(DefautImg_id);
		}
	}
	
	private void queuePhoto(String url,ImageView imageView)
	{	
		PhotoToLoad photoToLoad = new PhotoToLoad(url, imageView);
		MyWorkThreadQueue.AddTask(new ImageLoadThread(handler, photoToLoad, this));
	}
	

	public boolean imageViewReused(PhotoToLoad photoToLoad)
	{
		String tag = imageViews.get(photoToLoad.imageView);
		if(tag == null||!tag.equals(photoToLoad.url))
			return true;
		return false;
	}
	
	public static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 360;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}



	
	private void showTip(String tip)
	{
		Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
	}
	
	private static class MyHandler extends Handler{
		private final WeakReference<ImageLoader> mcontext;
		public MyHandler(ImageLoader context) {
			// TODO Auto-generated constructor stub
			this.mcontext = new WeakReference<ImageLoader>(context);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int Flag = msg.what;
			switch (Flag) {
			case 0:
				String errmsg = (String) msg.getData().getSerializable(
						"ErroMsg");
				((ImageLoader) mcontext.get()).showTip(errmsg);
				break;
			case 1:
				break;  
			default:
				break;
			}
		}
		
	}

	//���߳���
	@Override
	public void threadCall(PhotoToLoad photoToLoad, Bitmap bitmap) {
		// TODO Auto-generated method stub
		memoryCache.put(photoToLoad.url, bitmap);
		if (imageViewReused(photoToLoad))
			return;
		handler.post(new BitmapDisplayerThread(bitmap, photoToLoad));
	}
	
	class BitmapDisplayerThread implements Runnable {
		
		private Bitmap bitmap;
		private PhotoToLoad photoToLoad;
		
		public BitmapDisplayerThread(Bitmap bitmap,PhotoToLoad photoToLoad) {
			// TODO Auto-generated constructor stub
			this.bitmap = bitmap;
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(imageViewReused(photoToLoad))
				return;
			if(bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(DefautImg_id);
		}

	}
	
	public void clearCache()
	{
		memoryCache.clear();
		fileCache.clear();
	}


	@Override
	public ImageLoader getInstance() {
		// TODO Auto-generated method stub
		return this;
	}
	
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	
}
