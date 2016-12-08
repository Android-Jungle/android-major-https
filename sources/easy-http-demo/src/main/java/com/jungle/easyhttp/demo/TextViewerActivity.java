/**
 * Android Jungle-Easy-Http demo framework project.
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

package com.jungle.easyhttp.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TextViewerActivity extends AppCompatActivity {

    private static final String EXTRA_TEXT = "extra_text";

    public static void start(Context context, String text) {
        Intent intent = new Intent(context, TextViewerActivity.class);
        intent.putExtra(EXTRA_TEXT, text);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.title_text_viewer);
        setContentView(R.layout.activity_text_viewer);

        TextView textView = (TextView) findViewById(R.id.text_content);
        textView.setText(getIntent().getStringExtra(EXTRA_TEXT));
    }
}
