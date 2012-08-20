package com.reworlds.upload;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import nalinali.utils.ImageUtils;
import nalinali.utils.Utils;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadActivity extends Activity {

	private static final int GET_IMAGE_VIA_ALBUM=0x2311;
	private static final String IMAGE_TYPE="image/*";
	private static final int GET_BACK_IMG_SIZE=250;
	private static final String LINK="http://219.228.106.204:3080/AndroidCalls?request=102";
	
	EditText inputUrl;
	Button getImg,uploadImg;
	ImageView iv;
	TextView result;
	
	Bitmap tempPic;
	String localFile;
	
	Utils utils=new Utils();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        inputUrl=(EditText) findViewById(R.id.uploadUrl);
        getImg=(Button) findViewById(R.id.browse);
        uploadImg=(Button) findViewById(R.id.upload);
        iv=(ImageView) findViewById(R.id.img);
        result=(TextView) findViewById(R.id.result);
        
        File file=new File("abc.txt");
        
        inputUrl.setHint("输入上传地址");
        inputUrl.setText(LINK);
        getImg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT,null);
				getAlbum.setType(IMAGE_TYPE);
				getAlbum.putExtra("return-data", true);
				startActivityForResult(getAlbum,GET_IMAGE_VIA_ALBUM);
				
			}
		});
        
        uploadImg.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				uploadImg(inputUrl.getText().toString());
				
			}

			
		});
        
    }
    
    /**
     * 上传图片
     */
    private void uploadImg(String urlString) {
    	
    	/*Bitmap bm = BitmapFactory.decodeFile(localFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		byte[] b = baos.toByteArray(); 

		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);*/
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
	    params.add(new BasicNameValuePair("image", localFile));
	    
	    try {
			utils.postFile(urlString, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
	}
    
    /**
	 * 缩放图片
	 * @param src
	 * @return
	 */
	private Bitmap scaleBm(Bitmap src)
	{
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		float scaleRatio=(srcWidth>srcHeight)?GET_BACK_IMG_SIZE/srcWidth:GET_BACK_IMG_SIZE/srcHeight;
		src=ImageUtils.bitmapRoom(src, scaleRatio);
		return src;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK && requestCode==GET_IMAGE_VIA_ALBUM )
		{
			tempPic = (Bitmap) data.getParcelableExtra("data");  
			tempPic=scaleBm(tempPic);
			iv.setImageBitmap(tempPic);
			
			ContentResolver resolver = getContentResolver();
			Uri uri=data.getData();
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(uri, proj, null, null, null); 
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			localFile=cursor.getString(column_index);
			Log.i("taguage","localFile="+localFile);
			cursor.close();
		}
	}
}