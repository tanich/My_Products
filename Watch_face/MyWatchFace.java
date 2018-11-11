/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.example.mycompany.digital_test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.text.format.DateUtils;
import android.view.SurfaceHolder;

import java.util.Calendar;

//MyWatchFaceの処理----------------------------------------------------------------------------------
/*
 * MyWatchFaceはその名の通り、WatchFaceの本体
 * 実際のところ、ほとんどの処理はEngineに記載されており、Innerクラスを排除するとこれだけコンパクトになる
 */
public class MyWatchFace extends CanvasWatchFaceService {

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        // 時間を取得するためのCalendar
        Calendar mCalendar;
        // 時計の背景色
        int mBackgroundColor = Color.GRAY;
        // 時間を描画するためのPaint
        Paint mTimePaint;
        // 日付を描画するためのPaint
        Paint mDatePaint;
        // 時間の位置を保持するためのPoint
        Point mTimePosition;
        // 日付の位置を保持するためのPoint
        Point mDatePosition;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            // 時間の文字色やサイズをセット
            mTimePaint = new Paint();
            mTimePaint.setColor(Color.rgb(255, 241, 15));
            mTimePaint.setTextSize(75);
            mTimePaint.setAntiAlias(true);

            // 日付の文字色やサイズをセット
            mDatePaint = new Paint();
            mDatePaint.setColor(Color.rgb(255, 241, 15));
            mDatePaint.setTextSize(20);
            mDatePaint.setAntiAlias(true);

            // Calendarを初期化
            mCalendar = Calendar.getInstance();

            // 位置を初期化
            mTimePosition = new Point();
            mDatePosition = new Point();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            mBackgroundColor = inAmbientMode ? Color.BLACK : Color.DKGRAY;
            invalidate();
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            // 分が変わった時
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            // 時間を更新
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            // 時間
            String s_time = DateUtils.formatDateTime(
                    MyWatchFace.this,
                    mCalendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_TIME
            );
            Rect s_time_bounds = new Rect();
            mTimePaint.getTextBounds(s_time, 0, s_time.length(), s_time_bounds);
            // 中央に配置するための座標を計算する
            mTimePosition.set(canvas.getWidth() / 2 - s_time_bounds.width() / 2, canvas.getHeight() / 2);
            // 日付
            String s_date = DateUtils.formatDateTime(//DateUtils.formatDateTime
                    MyWatchFace.this,
                    mCalendar.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY
            );
            // 時間のテキストの大きさを再取得
            mTimePaint.getTextBounds(s_time, 0, s_time.length(), s_time_bounds);
            Rect s_date_bounds = new Rect();
            // 日付のテキストの大きさを取得
            mDatePaint.getTextBounds(s_date, 0, s_date.length(), s_date_bounds);
            mDatePosition.set(canvas.getWidth() / 2 - s_date_bounds.width() / 2, canvas.getHeight() / 2 + s_time_bounds.height() / 2 + (int) (10 / getResources().getDisplayMetrics().density));

            canvas.drawColor(mBackgroundColor);
            canvas.drawText(s_time, mTimePosition.x, mTimePosition.y, mTimePaint);
            canvas.drawText(s_date, mDatePosition.x, mDatePosition.y, mDatePaint);
        }
    }
}
