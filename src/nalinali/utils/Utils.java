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
	 * �ж�¼��������β�Ƿ��ж���ո�
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
	 * �ж�email�Ƿ���Ч 
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email)
	{
		return Pattern.compile("^[_\\.0-9a-zA-Z+-]+@([0-9a-zA-Z]+[0-9a-zA-Z-]*\\.)+[a-zA-Z]{2,4}$").matcher(email).find();
	}
	
	/**
	 * ��֤�ֻ���
	 * @param mobile
	 * @return
	 */
	public static boolean validateMobile(String mobile)
	{
		return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(mobile).find();
	}
	
	/**
	 * �б��û����Ƿ��Ϊ���֡���ĸ���»���
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
			Log.i("taguage","32λ����="+result);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	
	
	
	/**
	 * �������ϼ���xml��ע�⻹û���жϳ�ʱ
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
			System.out.println("=========>"+"�޷����ʵ�ַ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("=========>"+"�ӿڴ����δ���Ȩ��");
		}			
		return xmlString;
	}
	
	/**
	 * �������ش���
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
	 * ��ȡxml�е�errorCode
	 * @param str
	 * @return
	 */
	public static String getErrorCodeFromXml(String str)
	{
		String s="";
		Pattern p = Pattern.compile("<ErrorCode>([^</ErrorCode>]*)");//ƥ��<ErrorCode>��ͷ��</ErrorCode>��β���ĵ�
		Matcher m=p.matcher(str);
		if(m.find())s=m.group(1);
		return s;
	}
	
	/**
	 * ��ȡxml��ָ���ڵ�ֵ���ڵ������Ψһ
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
	 * ��ȡxml����ͬ�ڵ���������ֵ
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
	 * ������ֵ�Լ���
	 * @param keys ���ļ���
	 * @param arr ֵ����ļ��ϣ�ÿ��Ԫ�ؾ���һ��ֵ���飬��˳������
	 * @param count ����ÿ��ֵ����ĳ��ȿ��Բ���ͬ��Ϊ�˿��Ʋ����ֿ�ָ�룬����ֱ�Ӵ���ֵ����ĳ���ֵ
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
	 * ��scrollviewǶ��listviewʱ����������listview�ĸ߶�
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        //��ȡListView��Ӧ��Adapter
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	
	    int totalHeight = 0;
	    for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()�������������Ŀ
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);  //��������View �Ŀ��
	        totalHeight += listItem.getMeasuredHeight();  //ͳ������������ܸ߶�
	    }
	
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    //listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
	    //params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
	    listView.setLayoutParams(params);
	}
	
	/**
	 * �����Զ����ȡ���ĶԻ���
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
			Log.v("�ͻ���Cookie", "-------"+cookie+"---------");  
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

