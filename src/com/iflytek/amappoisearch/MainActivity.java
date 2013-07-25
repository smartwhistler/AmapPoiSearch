package com.iflytek.amappoisearch;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.search.core.AMapException;
import com.amap.api.search.poisearch.PoiSearch;
import com.amap.api.search.poisearch.PoiTypeDef;
import com.amap.api.search.poisearch.PoiPagedResult;
import com.amap.api.search.poisearch.PoiItem;

public class MainActivity extends Activity {
	private static final String TAG = "AMAP_POI_SEARCH";
	private static final String QUERY_DEFAULT = "安医二附院";
	private static final String CITY_DEFAULT = "合肥";
	private static final int RESULT_PAGE_SIZE_DEFAULT = 15;
	
	PoiSearch poiSearch;
	PoiSearch.Query poiSearchQuery;
	PoiPagedResult poiSearchResult;
	
    private EditText editText_Query;	// 搜索词
    private EditText editText_City;		// 城市
    private Button button_PoiSearch;	// 搜索按钮
	private TextView textview_ShowSearchResult;	// 显示搜索结果
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textview_ShowSearchResult = (TextView)findViewById(R.id.textview_PoiSearchResult);
		textview_ShowSearchResult.setFocusable(false);
		
		// 默认搜索
		poiSearchQuery = new PoiSearch.Query(QUERY_DEFAULT, PoiTypeDef.All, CITY_DEFAULT);
		poiSearch = new PoiSearch(MainActivity.this,
				poiSearchQuery);
		poiSearch.setPageSize(RESULT_PAGE_SIZE_DEFAULT);
		try {
			poiSearchResult = poiSearch.searchPOI();
			this.onPoiSearchResult();
		} catch (AMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		editText_Query=(EditText)findViewById(R.id.editText_Query);
		editText_Query.setText(QUERY_DEFAULT);
		//editText_Query.setFocusable(false);
		editText_City=(EditText)findViewById(R.id.editText_City);
		editText_City.setText(CITY_DEFAULT);
		//editText_City.setFocusable(false);
		button_PoiSearch = (Button)findViewById(R.id.button_Search);
		button_PoiSearch.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v) {
        		poiSearchQuery = new PoiSearch.Query(editText_Query.getText().toString(),
        				PoiTypeDef.All, editText_City.getText().toString());
        		poiSearch = new PoiSearch(MainActivity.this,
        				poiSearchQuery);
        		poiSearch.setPageSize(RESULT_PAGE_SIZE_DEFAULT);
        		try {
        			poiSearchResult = poiSearch.searchPOI();
        			onPoiSearchResult();
        		} catch (AMapException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        	}
        });
		button_PoiSearch.setFocusable(false);
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//按下键盘上返回按钮
        if(keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this)
                .setTitle(R.string.prompt)
                .setMessage(R.string.quit_desc)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                	@Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                	@Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
         
            return true;
        }else{       
            return super.onKeyDown(keyCode, event);
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(Menu.NONE, Menu.FIRST+1, 5, "exit");
		return true;
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case Menu.FIRST+1:
        	super.onDestroy();
        	System.exit(0);
        	break;
        }
        return true;
    }
	private void onPoiSearchResult() throws AMapException {
		textview_ShowSearchResult.setText("");
		Log.i(TAG, "共" + poiSearchResult.getPageCount() + "页。");
		textview_ShowSearchResult.append("共" + poiSearchResult.getPageCount() + "页，");
		if(poiSearchResult.getPageCount() >= 1) {
			Log.i(TAG, "第" + 1 + "页搜索结果:");
			textview_ShowSearchResult.append("第" + 1 + "页搜索结果:\n");
			List<PoiItem> poiSearchResultPage = poiSearchResult.getPage(1);
			for(int i =0; i < poiSearchResultPage.size(); i++) {
				Log.i(TAG, "POI名称:" + poiSearchResultPage.get(i).toString());
				Log.i(TAG, "地址:" + poiSearchResultPage.get(i).getSnippet());
				Log.i(TAG, "经纬度:" + poiSearchResultPage.get(i).getPoint());
				Log.i(TAG, "类型:" + poiSearchResultPage.get(i).getTypeDes());
				Log.i(TAG, "电话:" + poiSearchResultPage.get(i).getTel());
				textview_ShowSearchResult.append("*****"+(i+1)+"*****\n");
				textview_ShowSearchResult.append("POI名称:" + poiSearchResultPage.get(i).toString() + "\n");
				textview_ShowSearchResult.append("地址:" + poiSearchResultPage.get(i).getSnippet() + "\n");
				textview_ShowSearchResult.append("经纬度:" + poiSearchResultPage.get(i).getPoint() + "\n");
				textview_ShowSearchResult.append("类型:" + poiSearchResultPage.get(i).getTypeDes() + "\n");
				textview_ShowSearchResult.append("电话:" + poiSearchResultPage.get(i).getTel() + "\n");
			}
		}
	}
}
