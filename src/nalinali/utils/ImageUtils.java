package nalinali.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;

public class ImageUtils {

	/**
	 * 绘制文字按钮
	 * @param context
	 * @param str
	 * @param size
	 * @return
	 */
	public static Bitmap drawTxtBtn(Context context,String str,int size,int bgColor,int txtColor)
	{
		int iconWidth=(int)context.getResources().getDimension(android.R.dimen.app_icon_size);
		int iconHeight=(int) (iconWidth*0.6);
		Bitmap bm=Bitmap.createBitmap(iconWidth, iconHeight, Config.ARGB_8888);
		Canvas canvas=new Canvas(bm);
		canvas.drawColor(bgColor);
		
		Paint tPaint=new Paint();
		tPaint.setColor(txtColor);
		tPaint.setFilterBitmap(true);
		tPaint.setTypeface(Typeface.DEFAULT_BOLD);
		tPaint.setTextSize(size);
		canvas.drawText(str, 2, iconHeight-(iconHeight-size)/2, tPaint);		

		return bm;
	}
	
	/**
	 * 缩放图片
	 * @param srcBitmap
	 * @param scaleWidth
	 * @param scaleHeight
	 * @return
	 */
	public static Bitmap bitmapRoom(Bitmap srcBitmap, float scaleWidth,
			float scaleHeight) {
		int srcWidth = srcBitmap.getWidth();
		int srcHeight = srcBitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth,
				srcHeight, matrix, true);
		if (resizedBitmap != null) {
			return resizedBitmap;
		} else {
			return srcBitmap;
		}

	}
	/**
	 * 缩放图片，等比
	 * @param srcBitmap
	 * @param ratio
	 * @return
	 */
	public static Bitmap bitmapRoom(Bitmap srcBitmap,float ratio)
	{
		int srcWidth = srcBitmap.getWidth();
		int srcHeight = srcBitmap.getHeight();
		Matrix matrix=new Matrix();
		matrix.postScale(ratio, ratio);
		Bitmap resizedBitmap=Bitmap.createBitmap(srcBitmap,0,0,srcWidth,srcHeight,matrix,true);
		if(resizedBitmap!=null){
			return resizedBitmap;
		}else{
			return srcBitmap;
		}
	}
}
