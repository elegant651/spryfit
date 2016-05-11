package bpsound.spryfit.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
{
    private static final double ASPECT_RATIO = 4.0 / 4.0;
    private static final int PICTURE_SIZE_MAX_WIDTH = 426;
    private static final int PREVIEW_SIZE_MAX_WIDTH = 426;
    
	private OnPreviewRunningListener mOnPreviewRunningListener;
	private OnPictureIsTakenListener mOnPictureIsTakenListener;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mCameraId;
    private int mRotate;
    private boolean mPreviewRunning;
    
    public CameraPreview(Context context)
	{
		this(context, null, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CameraPreview(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init();
    }
    
    private void init() 
    {
    	mRotate = 0;
    	mHolder = getHolder();
    	mHolder.addCallback(this);
    }

    public void setRotate(int angle)
    {
    	this.mRotate = angle;
    }
    
    public void surfaceCreated(SurfaceHolder holder)
    {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
    	startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
    	stopPreviewAndFreeCamera();
    }

    public boolean isPreviewRunning()
    {
    	return mPreviewRunning;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if (width > height * ASPECT_RATIO) {
             width = (int) (height * ASPECT_RATIO + .5);
        } else {
            height = (int) (width / ASPECT_RATIO + .5);
        }

        setMeasuredDimension(width, height);
    }
    
    public boolean isSurfaceValid()
    {
   		if (mHolder.getSurface() == null)
    	{
    		return false;
    	}
   		
   		return mHolder.getSurface().isValid();
    }
    
    public synchronized void startPreview()
    {

    	if (!isSurfaceValid())
    	{
    		return;
    	}
    	
   		stopPreviewAndFreeCamera();
   		
    	CameraInfo cameraInfo = new CameraInfo();
    	int cameraCount = Camera.getNumberOfCameras();
    	
    	for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) 
    	{
    	    Camera.getCameraInfo(camIdx, cameraInfo);
    	    
    	    if ( cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK  )
    	    {
    	        try 
    	        {
    	        	mCamera = Camera.open(camIdx);
    	        	mCameraId = camIdx;
    	        } catch (RuntimeException e) {
    	        	e.printStackTrace();
    	        }
    	        
    	        break;
    	    }
    	}
        
    	if (isCamera())
    	{
    		setupCamera();
    		determineDisplayOrientation();
    		requestLayout();
    		
            try 
            {
            	mCamera.setPreviewDisplay(mHolder);
            	mCamera.startPreview();

            	mPreviewRunning = true;
            	
            	firePreviewRunningEvent();
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }    		
    	}
    }
    
    public synchronized void stopPreviewAndFreeCamera() 
    {
        if (mCamera != null)
        {
            mCamera.stopPreview();
            mCamera.release();
            
            mPreviewRunning = false;
            mCamera = null;
        }
    }
    
	public void setOnPictureIsTakenListener(OnPictureIsTakenListener listener)
	{
		mOnPictureIsTakenListener = listener;
	}
	
	public interface OnPictureIsTakenListener
	{
		void onPictureIsTaken(Bitmap bitmap);
	}
	
    public void takeAPicture()
    {  
        PictureCallback mPictureCallback = new PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
            	Bitmap dstBmp = null;
            	final BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
            	sizeOptions.inJustDecodeBounds = true;
            	Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, sizeOptions);
            	
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                
                
                Matrix matrix = new Matrix();

                matrix.preRotate(90 + mRotate);
//                matrix.preScale(-1.0f, 1.0f);

                Bitmap srcBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                dstBmp = Bitmap.createScaledBitmap(srcBmp, PICTURE_SIZE_MAX_WIDTH, PICTURE_SIZE_MAX_WIDTH, false);
                
				if(mOnPictureIsTakenListener != null)
				{
					mOnPictureIsTakenListener.onPictureIsTaken(dstBmp);
										
					srcBmp = null;
					dstBmp = null;					
				}
            }
        };
        
        if (mCamera != null && mPreviewRunning)
        {
       		mCamera.takePicture(null, null, mPictureCallback);	
        }
    }  
    
    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly.
     */
    public void determineDisplayOrientation() {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(mCameraId, cameraInfo);

        int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
        int degrees  = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int displayOrientation;

        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

       	mCamera.setDisplayOrientation(displayOrientation);
    }

    /**
     * Setup the camera parameters.
     */
    public void setupCamera() {
        Camera.Parameters parameters = mCamera.getParameters();

        Size bestPreviewSize = determineBestPreviewSize(parameters);
        Size bestPictureSize = determineBestPictureSize(parameters);

        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);

        mCamera.setParameters(parameters);
    }

    private Size determineBestPreviewSize(Camera.Parameters parameters) {
        List<Size> sizes = parameters.getSupportedPreviewSizes();

        return determineBestSize(sizes, PREVIEW_SIZE_MAX_WIDTH);
    }

    private Size determineBestPictureSize(Camera.Parameters parameters) {
        List<Size> sizes = parameters.getSupportedPictureSizes();

        return determineBestSize(sizes, PICTURE_SIZE_MAX_WIDTH);
    }

    protected Size determineBestSize(List<Size> sizes, int widthThreshold) {
        Size bestSize = null;

        for (Size currentSize : sizes) {
            boolean isDesiredRatio = (currentSize.width / 4) == (currentSize.height / 4);
            boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
            boolean isInBounds = currentSize.width <= widthThreshold;

            if (isDesiredRatio && isInBounds && isBetterSize) {
                bestSize = currentSize;
            }
        }

        if (bestSize == null) {
            return sizes.get(0);
        }

        return bestSize;
    }    
    
    public boolean isCamera()
    {
    	return (mCamera != null);
    }
    
    public void setOnPreviewRunningListener(OnPreviewRunningListener listener) 
    {
    	mOnPreviewRunningListener = listener;
    }
    
    protected void firePreviewRunningEvent()
    {
    	if (mOnPreviewRunningListener != null) 
    	{
    		mOnPreviewRunningListener.onPreviewRunning(this);
    	}
    }
    
    public interface OnPreviewRunningListener 
    {
        void onPreviewRunning(CameraPreview view);
    }
}
