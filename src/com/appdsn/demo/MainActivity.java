package com.appdsn.demo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appdsn.loopbanner.LoopBanner;
import com.appdsn.loopbanner.R;
import com.appdsn.loopbanner.SimplePageAdapter;

public class MainActivity extends Activity {

	ArrayList<String> datas = new ArrayList<String>();
	private LoopBanner banner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initData();
		banner = (LoopBanner) findViewById(R.id.banner);
		banner.setPageAdapter(new SimplePageAdapter(this, datas) {

			@Override
			public void onItemClick(String itemData) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "onClickimageView",
						Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	private void initData() {
		datas.add("http://weixin-10007714.image.myqcloud.com/weixin56a84a6144c3e1453869665");
		datas.add("http://weixin-10007714.image.myqcloud.com/weixin56a83f937f7cb1453866899");
		datas.add("http://weixin-10007714.image.myqcloud.com/weixin56a8396bdf49d1453865323");
	}

	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btn1:
			banner.startTurning(3000);
			break;
		case R.id.btn2:
			banner.stopTurning();
			break;
		case R.id.btn3:
			datas.add("http://www.2cto.com/uploadfile/Collfiles/20140726/2014072609061958.png");
			banner.notifyDataSetChanged();
			break;
		case R.id.btn4:
			datas.remove(0);
			banner.notifyDataSetChanged();
			break;
		case R.id.btn5:
			banner.setCanLoop(false);
			break;
		default:
			break;
		}

	}

}
