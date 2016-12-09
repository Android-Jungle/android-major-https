/**
 * Android Jungle-Major-Http framework project.
 *
 * Copyright 2016 Arno Zhang <zyfgood12@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jungle.majorhttp.model.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.jungle.majorhttp.R;
import com.jungle.majorhttp.model.base.AbstractBizModel;
import com.jungle.majorhttp.model.base.BizModelListener;
import com.jungle.majorhttp.request.base.NetworkResp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProgressModelLoadManager {

    private static ProgressModelLoadManager mInstance;

    public static ProgressModelLoadManager getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressModelLoadManager();
            mInstance.onCreate();
        }

        return mInstance;
    }


    public static interface LoadingDialogCreator {
        Dialog createDialog(Context context);
    }


    private static class LoadingInfo {
        private int mSeqId;
        private String mLoadingText;

        public LoadingInfo(int seqId, String loadingText) {
            mSeqId = seqId;
            mLoadingText = loadingText;
        }
    }


    private Dialog mLoadingDialog;
    private List<LoadingInfo> mLoadingInfoList = new LinkedList<>();
    private LoadingDialogCreator mLoadingDialogCreator;


    private ProgressModelLoadManager() {
    }

    private void onCreate() {
    }

    public void onTerminate() {
        mLoadingInfoList.clear();

        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void setLoadingDialogCreator(LoadingDialogCreator creator) {
        mLoadingDialogCreator = creator;
    }

    public <T> int load(Context context, final AbstractBizModel<?, T> model, String loadingText) {
        BizModelListener<T> listener = model.getListener();
        int seqId = model.load(new ProxyBaseBizModelListener<T>(listener) {
            @Override
            public void onSuccess(NetworkResp networkResp, T response) {
                hideLoading(model.getSeqId());
                super.onSuccess(networkResp, response);
            }

            @Override
            public void onError(int errorCode, String message) {
                hideLoading(model.getSeqId());
                super.onError(errorCode, message);
            }
        });

        showLoading(context, seqId, loadingText);
        return seqId;
    }

    private synchronized void showLoading(Context context, int seqId, String loadingText) {
        if (mLoadingDialog == null) {
            mLoadingDialog = createLoadingDialog(context);
        }

        if (TextUtils.isEmpty(loadingText)) {
            loadingText = context.getString(R.string.loading_now);
        }

        updateLoadingText(loadingText);
        mLoadingInfoList.add(new LoadingInfo(seqId, loadingText));

        if (!mLoadingDialog.isShowing()) {
            View iconView = mLoadingDialog.findViewById(R.id.request_loading_icon);
            Drawable drawable = iconView.getBackground();
            if (drawable instanceof AnimationDrawable) {
                ((AnimationDrawable) drawable).start();
            }

            mLoadingDialog.show();
        }
    }

    private void updateLoadingText(String loadingText) {
        TextView loadingTextView = (TextView) mLoadingDialog.findViewById(R.id.request_loading_text);
        if (loadingTextView != null) {
            loadingTextView.setText(loadingText);
        }
    }

    private Dialog createLoadingDialog(Context context) {
        Dialog dialog = null;
        if (mLoadingDialogCreator != null) {
            dialog = mLoadingDialogCreator.createDialog(context);
        }

        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_loading_request);

            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
        }

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private synchronized void hideLoading(int seqId) {
        if (mLoadingDialog == null) {
            return;
        }

        for (Iterator<LoadingInfo> iterator = mLoadingInfoList.iterator(); iterator.hasNext(); ) {
            LoadingInfo info = iterator.next();
            if (info.mSeqId == seqId) {
                iterator.remove();
                break;
            }
        }

        if (mLoadingInfoList.isEmpty()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        } else {
            LoadingInfo last = mLoadingInfoList.get(mLoadingInfoList.size() - 1);
            updateLoadingText(last.mLoadingText);
        }
    }
}
