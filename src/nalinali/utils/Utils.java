package nalinali.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {

	public Utils()
	{
	}
	
	
	/**
	 * 判定录入文字首尾是否有多余空格
	 * @param s
	 * @return
	 */
	public static String normalInput(String s)
	{
		if(s!=null){
			s=s.trim();
			if(s.length()<=0)s=""; 
		}else s="";
		return s;
	}
	/**
	 * 判断email是否有效 
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email)
	{
		return Pattern.compile("^[_\\.0-9a-zA-Z+-]+@([0-9a-zA-Z]+[0-9a-zA-Z-]*\\.)+[a-zA-Z]{2,4}$").matcher(email).find();
	}
	
	/**
	 * 验证手机号
	 * @param mobile
	 * @return
	 */
	public static boolean validateMobile(String mobile)
	{
		return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(mobile).find();
	}
	
	/**
	 * 判别用户名是否仅为数字、字母和下划线
	 * @param s
	 * @return
	 */
	public static boolean isOnlyNumAndLetter(String s)
	{
		return Pattern.compile("[a-zA-z0-9_]+").matcher(s).find();
	}
	
	public static String makeMD5(String s)
	{
		String result=null;
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte b[]=md.digest();
			
			int i;
			
			StringBuffer buf=new StringBuffer("");
			for(int k=0;k<b.length;k++)
			{
				i=b[k];
				if(i<0)i+=256;
				if(i<16)buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result=buf.toString();
			Log.i("taguage","32位加密="+result);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	
	
	
	/**
	 * 从网络上加载xml，注意还没有判断超时
	 * @param link
	 * @return String
	 */
	public static String getXmlString(String httpUrl,String code)
	{
		String xmlString=null;
		try {
			URL url=new URL(httpUrl);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			InputStream is=conn.getInputStream();
			
			String line=null;
			StringBuffer sb=new StringBuffer();
			BufferedReader buffer=new BufferedReader(new InputStreamReader(is,code));
			while((line=buffer.readLine())!=null)sb.append(line);
			
			is.close();
			xmlString=sb.toString();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("=========>"+"无法访问地址");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("=========>"+"接口错误或未添加权限");
		}			
		return xmlString;
	}
	
	/**
	 * 创建加载窗口
	 * @param context
	 * @param str
	 */
	public static ProgressDialog setProgressPop(Context context,String str)
	{
		ProgressDialog pd=new ProgressDialog(context);
		pd.setMessage(str);
		return pd;
	}
	
	/**
	 * 获取xml中的errorCode
	 * @param str
	 * @return
	 */
	public static String getErrorCodeFromXml(String str)
	{
		String s="";
		Pattern p = Pattern.compile("<ErrorCode>([^</ErrorCode>]*)");//匹配<ErrorCode>开头，</ErrorCode>结尾的文档
		Matcher m=p.matcher(str);
		if(m.find())s=m.group(1);
		return s;
	}
	
	/**
	 * 获取xml中指定节点值，节点必须是唯一
	 * @param str
	 * @param node
	 * @return
	 */
	public static String getCertainNode(String str,String node)
	{
		String s="";
		Pattern p=Pattern.compile("<"+node+">([^</"+node+">]*)");
		Matcher m=p.matcher(str);
		if(m.find())s=m.group(1);
		return s;
	}
	
	/**
	 * 获取xml中相同节点名的所有值
	 * @param xml
	 * @param nodeName
	 * @return
	 */
	public static List getNodes(String xml,String nodeName)
	{
		List results=new ArrayList();
		Pattern p=Pattern.compile("<"+nodeName+">([^</"+nodeName+">]*)");
		Matcher m=p.matcher(xml);
		while(m.find())results.add(m.group(1));
		return results;
	}
	
	/**
	 * 创建键值对集合
	 * @param keys 键的集合
	 * @param arr 值数组的集合，每个元素就是一个值数组，按顺序排列
	 * @param count 上面每个值数组的长度可以不相同，为了控制不出现空指针，这里直接传入值数组的长度值
	 * @return
	 */
	
	public static ArrayList<HashMap<String,Object>> createList(ArrayList<HashMap<String,Object>> currentList,
			String[] keys, ArrayList arr, int count)
	{
		for(int i=0;i<count;i++)
		{
			HashMap<String,Object> hm=new HashMap<String,Object>();
			int c=keys.length;
			for(int j=0;j<c;j++)
			{
				ArrayList<String> value=(ArrayList<String>) arr.get(j);
				hm.put(keys[j], value.get(i));
			}
			currentList.add(hm);
		}
		return currentList;
	}
	
	
	/**
	 * 当scrollview嵌套listview时，重新设置listview的高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	
	    int totalHeight = 0;
	    for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);  //计算子项View 的宽高
	        totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
	    }
	
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    //listView.getDividerHeight()获取子项间分隔符占用的高度
	    //params.height最后得到整个ListView完整显示需要的高度
	    listView.setLayoutParams(params);
	}
	
	/**
	 * 创建自定义可取消的对话框
	 */
	public static Dialog makeCancelableDialog(Context context, LayoutParams lp, int view, int title)
	{
		LayoutInflater inflater=LayoutInflater.from(context);
		View menuView=inflater.inflate(view, null);
		Dialog dialog=new Dialog(context);
		dialog.addContentView(menuView, lp);
		dialog.setTitle(title);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		
		return dialog;
	}
	
	public String postFile(String URL,List <NameValuePair> params) throws Exception 
	{
	// PostMethod   filePost=new PostMethod();

		String resultString="";
		HttpClient client=new DefaultHttpClient();
		Log.v("taguage", "response=0");
		try {
			HttpPost httpRequest = new HttpPost(URL);
			Log.v("taguage", "response=1");
			MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		
			/*if(cookie!=null) {
			httpRequest.addHeader("Cookie",cookie);
			httpRequest.addHeader("Connection","Keep-Alive"); 
			Log.v("客户端Cookie", "-------"+cookie+"---------");  
			}*/
			for(int index=0; index < params.size(); index++)
			 {          
			   if(params.get(index).getName().equalsIgnoreCase("image")) {                
				   // If the key equals to "image", we use FileBody to transfer the data    
				   mpEntity.addPart(params.get(index).getName(), new FileBody(new File(params.get(index).getValue())));           
			   } 
			   else
			   {  
			              // Normal string data                 
				   mpEntity.addPart(params.get(index).getName(), new StringBody(params.get(index).getValue()));             
			   }  
			  
			  }  

			httpRequest.setEntity(mpEntity) ; 
		
			//httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8"));  
			HttpResponse httpResponse = client.execute(httpRequest);
			Log.v("taguage", "response="+httpResponse.getStatusLine().getStatusCode());
			if(httpResponse.getStatusLine().getStatusCode() == 200)    
			{   
//				resultString= readstream(httpResponse.getEntity().getContent());
				Log.i("taguage", "response="+EntityUtils.toString(httpResponse.getEntity()));
				if(!resultString.contains("ERROR")){
				//getCookie();
				}
				else
				{
					throw new Exception("cookie is old,you need a new one.");
				}
			}
			else
			{
				Log.i("taguage", "response=wrong");
				throw new Exception("can't connect the network");
			}
			return resultString;
		}catch (Exception e) {
			throw e;
		}
	}


	private String readstream(InputStream content) {
		
		
		return null;
	}
	
	
	
	
}

