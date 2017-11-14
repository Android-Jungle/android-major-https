/**
 * Android Jungle-Major-Https framework project.
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

package com.jungle.majorhttps.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.jungle.majorhttps.R;
import com.jungle.majorhttps.model.base.AbstractModel;
import com.jungle.majorhttps.model.listener.BothProxyModelListener;
import com.jungle.majorhttps.model.listener.ModelListener;
import com.jungle.majorhttps.request.base.NetworkResp;

public class MajorProgressLoadManager {

    private static MajorProgressLoadManager mInstance;

    public static MajorProgressLoadManager getInstance() {
        if (mInstance == null) {
            mInstance = new MajorProgressLoadManager();
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
    private LoadingDialogCreator mLoadingDialogCreator;


    private MajorProgressLoadManager() {
    }

    private void onCreate() {
    }

    public void onTerminate() {
        hideLoading();
    }

    public void setLoadingDialogCreator(LoadingDialogCreator creator) {
        mLoadingDialogCreator = creator;
    }

    public <T> int load(Context context, final AbstractModel<?, ?, T> model, String loadingText) {
        if (context == null) {
            return model.load();
        }

        ModelListener<T> listener = model.getListener();
        int seqId = model.load(new BothProxyModelListener<T>(listener) {
            @Override
            public void onSuccess(NetworkResp networkResp, T response) {
                hideLoading();
                super.onSuccess(networkResp, response);
            }

            @Override
            public void onError(int errorCode, String message) {
                hideLoading();
                super.onError(errorCode, message);
            }
        });

        showLoading(context, seqId, loadingText);
        return seqId;
    }

    public synchronized void showLoading(Context context, int seqId, String loadingText) {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }

        mLoadingDialog = createLoadingDialog(context);
        if (TextUtils.isEmpty(loadingText)) {
            loadingText = context.getString(R.string.majorhttps_loading_now);
        }

        updateLoadingText(loadingText);

        View iconView = mLoadingDialog.findViewById(R.id.majorhttps_request_loading_icon);
        Drawable drawable = iconView.getBackground();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }

        mLoadingDialog.show();
    }

    private void updateLoadingText(String loadingText) {
        TextView loadingTextView = (TextView) mLoadingDialog.findViewById(R.id.majorhttps_request_loading_text);
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
            dialog.setContentView(R.layout.majorhttps_dialog_loading_request);

            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
        }

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLoadingDialog = null;
            }
        });

        return dialog;
    }

    public synchronized void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
