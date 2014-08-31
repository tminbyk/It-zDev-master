package action.main;

import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

	private WebView webView =null;
	   /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */

	@SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView1);
        // ページ内の設定を有効にする
        webView.getSettings().setJavaScriptEnabled(true);
        // 遷移後もページをwebviewで表示するための設定
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new JsInterFace(this), "android");
        // ページ表示
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(action.main.R.menu.main, menu);
	return true;
    }

    public String doPost(String url, String userName, String password) {

    	HttpPost httpPost = new HttpPost(url);
    	DefaultHttpClient httpClient = new DefaultHttpClient();

    	try {
			StringEntity paramEntity = new StringEntity(userName, password);
			paramEntity.setChunked(false);
			paramEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(paramEntity);

			HttpResponse response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				throw new Exception("");
			}
			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public String doGet(String url) {
        try {
			HttpGet httpGet = new HttpGet(url);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			// ヘッダを設定する
			httpGet.setHeader("Connection", "Keep-Alive");
			HttpResponse response = httpClient.execute( httpGet );
			int status = response.getStatusLine().getStatusCode();
			if ( status != HttpStatus.SC_OK ) {
			    throw new Exception("");
			}
			return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
        	return null;
        }
    }

    public boolean parseXml(String resxml) {
    	XmlPullParser xmlPullParser = Xml.newPullParser();
    	try {
			xmlPullParser.setInput(new StringReader(resxml));
			int eventType = 0;
			String result = null;
			while((eventType = xmlPullParser.next()) != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && "data".equals(xmlPullParser.getName())) {
					result = xmlPullParser.nextText();
					Log.d("HttpSampleActivity", result);
				}
				if (result == "0") {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }

    public class JsInterFace {

    	private Context context = null;

    	public JsInterFace(Context context) {
    		this.context = context;
		}

    	@JavascriptInterface
    	public void registData(String returnValue) {

    		Log.d("戻り値", returnValue);
			String[] userData = returnValue.split(",");
			String resXml = doPost("http://10.0.2.2:8080/MemoryzDev/regist.do", userData[0], userData[1]);
			if(parseXml(resXml)) {
				webView.loadUrl("file:///android_asset/suscess.html");
			} else {
				webView.loadUrl("file:///android_asset/failure.html");
			}
		}
    }
}

