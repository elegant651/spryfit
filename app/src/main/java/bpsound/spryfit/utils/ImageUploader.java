package bpsound.spryfit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import bpsound.spryfit.R;


public class ImageUploader {
	
	private static Context mContext;
	private static DisplayImageOptions displayOptions;
	
	public interface OnCompletedListener{
		public void onCompleted(String result);
		public void onErrorRaised(Exception e);
	}
	
	public ImageUploader(){}
	
	private static ImageUploader mInstance;
	public static ImageUploader getInstance(){
		if(mInstance==null){
			mInstance = new ImageUploader();
		}
		return mInstance;
	}
	
	public static void imageLoaderInit(Context context){
		mContext = context;
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY-2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
		
		displayOptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY)
		.delayBeforeLoading(500)
		.cacheInMemory(true)
		.cacheOnDisc(true)		
		.considerExifParams(true)			
		.build();
	}
	
	public static void processDownloadImg(String user_id, final String file_id, final int num_idx, final ImageView iv, final boolean isBlur){

	 }
	
	public static void processDownloadImgForWeb(final String url, final ImageView iv){
		if(url==null || url.equals("1")){
			iv.setImageResource(R.drawable.ic_launcher);
			return;
		}
		
		ImageLoader.getInstance().displayImage(url, iv, displayOptions, new ImageLoadingListener(){

			@Override
			public void onLoadingStarted(String imageUri, View v) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View v) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				iv.setImageBitmap(loadedImage);				
			}

			@Override
			public void onLoadingFailed(String imageUri, View v,
					FailReason failReason) {
				// TODO Auto-generated method stub
				Log.e("anq", failReason.toString());
				if(failReason.getType()== FailReason.FailType.DECODING_ERROR){
					
				}
			}

		});
	}
	
	public static void processDownloadImgForFilePath(final String file_id, final ImageView iv, final boolean isBlur){
		final String localPath = ImageUploader.getSdPath()+"/"+file_id+".jpg";
		
		File checkFile = new File(localPath);
		if(checkFile.exists()){
			loadImageLoader(file_id, localPath, iv, isBlur);
		}else{
			downloadImg(file_id, localPath, iv, isBlur);
		}
	}
	
	private static void downloadImg(final String file_id, String localPath, final ImageView iv, final boolean isBlur){

	}
	
	public static void loadImageLoader(final String file_id, String filePath, final ImageView iv, final boolean isBlur){
		final String strfilePath = "file:/"+filePath;
		
		ImageLoader.getInstance().displayImage(strfilePath, iv, displayOptions, new ImageLoadingListener(){

			@Override
			public void onLoadingStarted(String imageUri, View v) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View v) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				iv.setImageBitmap(loadedImage);				
			}

			@Override
			public void onLoadingFailed(String imageUri, View v,
					FailReason failReason) {
				// TODO Auto-generated method stub
				Log.e("anq", failReason.toString());
				if(failReason.getType()== FailReason.FailType.DECODING_ERROR){
					if(file_id!=""){
						downloadImg(file_id, strfilePath, iv, isBlur);
					}
				}
			}

		});
	}
	
	public static String getSdPath(){
		String ext = Environment.getExternalStorageState();
		String sdPath = "";
		if(ext.equals(Environment.MEDIA_MOUNTED)){
			sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}else{
			sdPath = Environment.MEDIA_UNMOUNTED;
		}
		sdPath+="/.anq";
		
		File dir = new File(sdPath);
		dir.mkdir();
		writeNoMediaFile(dir.getAbsolutePath());
		
		return sdPath;
	}
	
	public static void removeFiles(){
		String ext = Environment.getExternalStorageState();
		String sdPath = "";
		if(ext.equals(Environment.MEDIA_MOUNTED)){
			sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}else{
			sdPath = Environment.MEDIA_UNMOUNTED;
		}
		sdPath+="/.anq";
		
		File dir = new File(sdPath);
		
		File[] files = dir.listFiles();
		for(File file : files){
			if(!file.delete()){
				Log.e("knowre", "Failed to delete " + file);
			}
		}
	}
	
	public static File doSaveFile(Bitmap bmp, String filename){
		String ext = Environment.getExternalStorageState();
		String sdPath = "";
		if(ext.equals(Environment.MEDIA_MOUNTED)){
			sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		}else{
			sdPath = Environment.MEDIA_UNMOUNTED;
		}
		
		File dir = new File(sdPath+"/.anq");
		dir.mkdir();
		writeNoMediaFile(dir.getAbsolutePath());
		File saveFile = new File(sdPath+"/.anq/"+filename+".jpg");
		
		if(saveFile.exists()){
			saveFile.delete();
		}
		
		OutputStream out = null;
		try {			
			saveFile.createNewFile();
			out = new FileOutputStream(saveFile);
		
			bmp.compress(CompressFormat.JPEG, 50, out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try{ out.close(); }catch(IOException e){e.printStackTrace();}
		}
		
		return saveFile;
	}
	
	public static boolean writeNoMediaFile( String directoryPath ){
	    String storageState = Environment.getExternalStorageState();

	    if ( Environment.MEDIA_MOUNTED.equals( storageState )){
	        try{
	            File noMedia = new File(directoryPath, ".nomedia");
	            
	            if(noMedia.exists()){	                
	                return true;
	            }
	            
	            FileOutputStream noMediaOutStream = new FileOutputStream(noMedia);
	            noMediaOutStream.write(0);
	            noMediaOutStream.close();
	        }catch ( Exception e ){
	            Log.e("dayatti", "error writing file");
	            e.printStackTrace();
	            return false;
	        }
	    }else{
	        Log.e("dayatti", "storage appears unwritable");
	        return false;
	    }
	    
	    return true;	    
	}
	
}
