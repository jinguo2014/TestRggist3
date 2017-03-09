package com.example.cyt.testrggist;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class HttpUpload {

	private HttpUtils http = new HttpUtils();

	protected final String TAG = "JGFileUpload";

	//用于多张图片批量上传记录handler
	private ArrayList<HttpHandler> httpHs = new ArrayList<HttpHandler>();

	/**
	 * 上传图片
	 * @param context
	 * @param params  上传时带的参数
	 * @param uploadHost  上传地址
     */
	public void uploadMethod(Context context, final RequestParams params,
			final String uploadHost) {
		uploadMethod(context, params, uploadHost, null,0);
	}

	/**
	 * 上传图片
	 * @param context
	 * @param params 上传时带的参数
	 * @param uploadHost  上传地址
	 * @param upLoadCallBackListenner  上传后的回调
     * @param upLoadType  上传后回调里图片类型
     */
	public void uploadMethod(final Context context, final RequestParams params,
			final String uploadHost,
			final UploadCallBackListenner upLoadCallBackListenner,final int upLoadType)

	{

		HttpHandler handler = http.send(HttpRequest.HttpMethod.POST,
				uploadHost, params, new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// msgTextview.setText("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							if (upLoadCallBackListenner != null) {
								upLoadCallBackListenner.onUploading(upLoadType);
							}
						} else {
							// msgTextview.setText("reply: " + current + "/"+
							// total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// msgTextview.setText("reply: " + responseInfo.result);
						// ((JGActivity) context).showToast("上传成功!");
						String result = responseInfo.result;
						Log.e(TAG, responseInfo.result);

						JSONObject response = JSON.parseObject(result);

						String code = response.getString("code");

						if ("0".equals(code)) {
							if (upLoadCallBackListenner != null) {
								upLoadCallBackListenner.onSuccessCallBack(upLoadType);
							}
						} else if ("203".equals(code)) {

							String msg = response.getString("msg");

							if (TextUtils.isEmpty(msg)) {

								msg = "你的存储空间不足";
							}
							Toast.makeText(context, msg, Toast.LENGTH_SHORT)
									.show();

						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// msgTextview.setText(error.getExceptionCode() + ":" +
						// msg);
						if (upLoadCallBackListenner != null) {
							upLoadCallBackListenner.onFailureCallBack();
						}

						Log.e(TAG, error.getExceptionCode() + ":" + msg);
					}
				});

		httpHs.add(handler);

	}

	/**
	 * 取消上传
	 */
	public void stopAllHttp() {

		for (int i = 0; i < httpHs.size(); i++) {
			HttpHandler handler = httpHs.get(i);
			handler.cancel();
		}
		httpHs.clear();
	}

	/**
	 * 上传回调接口
	 */
	public interface UploadCallBackListenner {

		void onSuccessCallBack(int upLoadType);

		void onFailureCallBack();

		void onUploading(int upLoadType);
	}

}
