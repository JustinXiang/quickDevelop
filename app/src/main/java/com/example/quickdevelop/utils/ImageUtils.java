package com.example.quickdevelop.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.quickdevelop.App;
import com.example.quickdevelop.R;
import com.example.quickdevelop.base.ExtensionKt;
import com.example.quickdevelop.myInterface.CompleteListener;
import com.example.quickdevelop.myInterface.OKBitmapListener;
import com.example.quickdevelop.myInterface.OkFileListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author LiHongCheng
 * @version 1.0
 * @E-mail diosamolee2014@gmail.com
 * @time 2019/7/12 11:53
 * @describe 图片相关工具类
 */
public class ImageUtils {


    public static String saveImageView(ImageView iv, CompleteListener completeListener) {
        BitmapDrawable drawable = ((BitmapDrawable) iv.getDrawable());
        return saveImageToGallery(drawable.getBitmap(), completeListener);
    }

    /**
     * 保存图片到相册
     *
     * @param bmp
     */
    public static String saveImageToGallery(Bitmap bmp, CompleteListener completeListener) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Download");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(App.mInstance.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        // 最后通知图库更新
        App.mInstance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));
        //回调通知
        completeListener.complete();
        return file.getAbsolutePath();
    }

    public static Bitmap createBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;
        opts.inDither = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, opts);
    }

    public static Bitmap createBitmap(String filePath, final int widthTo) {
        if (filePath == null || widthTo <= 0) {
            return null;
        }
        int thumbnailHeight;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        thumbnailHeight = (int) (((float) widthTo) / options.outWidth * options.outHeight);
        int m = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        BitmapFactory.Options newoptions = new BitmapFactory.Options();
        newoptions.inSampleSize = (m + widthTo - 1) / widthTo;
        newoptions.inJustDecodeBounds = false;
        newoptions.inDither = false;
        newoptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(filePath, newoptions);
        if (null == bitmap) {
            return null;
        }
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        int focusX = width / 2;
        int focusY = height / 2;
        int cropX;
        int cropY;
        int cropWidth;
        int cropHeight;
        if (widthTo * height < thumbnailHeight * width) {
            // Vertically constrained.
            cropWidth = widthTo * height / thumbnailHeight;
            cropX = Math.max(0, Math.min(focusX - cropWidth / 2, width - cropWidth));
            cropY = 0;
            cropHeight = height;
        } else {
            // Horizontally constrained.
            cropHeight = thumbnailHeight * width / widthTo;
            cropY = Math.max(0, Math.min(focusY - cropHeight / 2, height - cropHeight));
            cropX = 0;
            cropWidth = width;
        }
        final Bitmap finalBitmap = Bitmap.createBitmap(widthTo, thumbnailHeight,
                Bitmap.Config.RGB_565);// RGB_565
        final Canvas canvas = new Canvas(finalBitmap);
        final Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawColor(0);
        canvas.drawBitmap(bitmap, new Rect(cropX, cropY, cropX + cropWidth, cropY + cropHeight),
                new Rect(0, 0, widthTo, thumbnailHeight), paint);
        bitmap.recycle();
        return finalBitmap;
    }

    public static Bitmap createFitinBitmap(String path) {
        return createFitinBitmap(path, 1);
    }

    public static Bitmap createFitinBitmap(String path, int scale) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        try {

            DisplayMetrics dm = App.mInstance.getResources().getDisplayMetrics();
            int dstWidth = Math.min(dm.widthPixels, dm.heightPixels) / scale;
            int dstHeight = Math.max(dm.widthPixels, dm.heightPixels) / scale;

            int MAX_IMAGE_LENGTH = Math.max(dstWidth, dstHeight);

            BitmapFactory.decodeFile(path, opts);

            int sampleSize1 = opts.outWidth / MAX_IMAGE_LENGTH;
            int sampleSize2 = opts.outHeight / MAX_IMAGE_LENGTH;
            opts.inSampleSize = sampleSize1 > sampleSize2 ? sampleSize1 : sampleSize2;
            opts.inSampleSize = (opts.inSampleSize + 1) / 2 * 2;

            opts.inJustDecodeBounds = false;
            opts.inDither = false;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap temp0 = BitmapFactory.decodeFile(path, opts);
            if (temp0 == null) {
                return null;
            }

            Matrix m = new Matrix();

            float sample1 = dstWidth / ((float) opts.outWidth);
            float sample2 = dstHeight / ((float) opts.outHeight);
            float sample = sample1 < sample2 ? sample1 : sample2;

            Bitmap temp;
            if (sample < 1.0) {
                m.postScale(sample, sample);
                temp = Bitmap.createBitmap(temp0, 0, 0, temp0.getWidth(), temp0.getHeight(), m,
                        true);
                temp0.recycle();
            } else {
                temp = temp0;
            }
            if (temp.isMutable() && temp.getConfig() == Bitmap.Config.RGB_565) {
                return temp;
            } else {
            }

            Bitmap temp2 = temp.copy(Bitmap.Config.RGB_565, true);

            temp2.getWidth();
            temp2.getHeight();

            temp.recycle();
            temp = null;

            return temp2;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap ResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        if (bm == null || bm.isRecycled()) {
            return null;
        }
        final int w = bm.getWidth();
        final int h = bm.getHeight();

        final float sw = ((float) newWidth) / w;
        final float sh = ((float) newHeight) / h;
        float ratio = 0.0f;

        if (sw >= sh) {
            ratio = sh;
        } else {
            ratio = sw;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        return Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
    }

    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static String saveBmp(Context context, Bitmap bitmap) {
        String dirString = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/hiifit/";
        File dir = new File(dirString);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String name = dirString + "hjk" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        boolean isSaved = saveBmp(bitmap, name, 100);

        scanPhoto(context, name);

        if (isSaved) {
            return name;
        } else {
            return null;
        }
    }

    public static boolean saveBmp(Bitmap bitmap, String name) {
        return saveBmp(bitmap, name, 95);
    }

    public static boolean saveBmp(Bitmap bitmap, String name, int quality) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        try {
            File pf = new File(name);
            if (!pf.exists()) {
                pf.createNewFile();
            } else {
                pf.delete();
            }
            FileOutputStream stream;
            stream = new FileOutputStream(pf);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            stream.flush();
            stream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    public static boolean saveBmpToPNG(Bitmap bitmap, String name) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        try {
            File pf = new File(name);
            if (!pf.exists()) {
                pf.createNewFile();
            } else {
                pf.delete();
            }
            FileOutputStream stream;
            stream = new FileOutputStream(pf);
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, stream);
            stream.flush();
            stream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getExifOrientation(String filepath) {
       /* if (StringUtil.isStringEmpty(filepath)) {
            return 0;
        }*/
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize anim_share subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }

    public static Bitmap rotateAndMirror(Bitmap b, int degrees, boolean mirror, boolean needRgba) {
        if ((degrees != 0 || mirror) && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            if (mirror) {
                m.postScale(-1, 1);
                degrees = (degrees + 360) % 360;
                if (degrees == 0 || degrees == 180) {
                    m.postTranslate((float) b.getWidth(), 0);
                } else if (degrees == 90 || degrees == 270) {
                    m.postTranslate((float) b.getHeight(), 0);
                } else {
                    throw new IllegalArgumentException("Invalid degrees=" + degrees);
                }
            }

            m.rectStaysRect();
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
                if (android.os.Build.VERSION.SDK_INT <= 10 && needRgba) {
                    if (b.getConfig() == Bitmap.Config.RGB_565) {
                        Bitmap b3 = b.copy(Bitmap.Config.ARGB_8888, true);
                        if (b != b3) {
                            b.recycle();
                            b = b3;
                        }
                    }
                }

            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }


    /**
     * 把图片写入指定文件
     *
     * @param filename
     * @param bitmap
     * @param quality
     */
    private static void writeImage(String filename, Bitmap bitmap, int quality) {
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给图片右下角添加水印
     *
     * @param src
     * @param watermarkBitmap
     * @return
     */
    private static Bitmap addWatermarkBitmap(Bitmap src, Bitmap watermarkBitmap) {
        if (null != watermarkBitmap) {
            // 创建一个和src长宽一样的位图
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();
            Bitmap newBitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(src, 0, 0, null);
            Paint paint = new Paint();

            int watermarkWidth = watermarkBitmap.getWidth();
            int watermarkHeight = watermarkBitmap.getHeight();
            canvas.drawBitmap(watermarkBitmap, srcWidth - watermarkWidth - 15, srcHeight - watermarkHeight - 15, paint);

            canvas.save();
            canvas.restore();

            return newBitmap;
        }
        return src;
    }


    /**
     * 为图像添加环形外框
     *
     * @param srcBitmap
     * @param broderThickness
     * @param color
     * @return Bitmap
     */
    public static Bitmap drawCircleBorder(Bitmap srcBitmap, int broderThickness, int color) {

        int width = srcBitmap.getWidth() + 2 * broderThickness;
        int height = srcBitmap.getHeight() + 2 * broderThickness;
        int radius = (width < height ? width : height) / 2;
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        output.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();

        /* 去锯齿 */
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(color);
        /* 设置paint的style为STROKE：空心 */
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        /* 设置paint的外框宽度 */
        paint.setStrokeWidth(broderThickness);

        // 画圆
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        canvas.drawBitmap(srcBitmap, broderThickness, broderThickness, paint);
        return output;

    }

    /**
     * 将图像处理为背景透明的圆形头像
     *
     * @param bitmap
     * @return output
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }


    /**
     * 设置水印图片在左上角
     *
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskLeftTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, watermark,
                dp2px(context, paddingLeft), dp2px(context, paddingTop));
    }

    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
                                                int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
        canvas.save();
        // 存储
        canvas.restore();
        return newb;
    }

    /**
     * 设置水印图片在右下角
     *
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskRightBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark,
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到右上角
     *
     * @param src
     * @param watermark
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap createWaterMaskRightTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingTop) {
        return createWaterMaskBitmap(src, watermark,
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
                dp2px(context, paddingTop));
    }

    /**
     * 设置水印图片到左下角
     *
     * @param src
     * @param watermark
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap createWaterMaskLeftBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark, dp2px(context, paddingLeft),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 设置水印图片到中间
     *
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap createWaterMaskCenter(Bitmap src, Bitmap watermark) {
        return createWaterMaskBitmap(src, watermark,
                (src.getWidth() - watermark.getWidth()) / 2,
                (src.getHeight() - watermark.getHeight()) / 2);
    }

    /**
     * 给图片添加文字到左上角
     *
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
                                           int size, int color, int paddingLeft, int paddingTop) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 绘制文字到右下角
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text,
                                               int size, int color, int paddingRight, int paddingBottom) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 绘制文字到右上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text,
                                            int size, int color, int paddingRight, int paddingTop) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 压缩800为比例
     *
     * @param bitmap
     * @return
     */
    public static Bitmap draw800Compress(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int max = Math.max(height, width);
        double proportion = max / 800d;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / proportion), (int) (height / proportion), true);
        return scaledBitmap;
    }

    /**
     * 绘制文字到中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text, int location
            , int size, String colorStr, Typeface typeface) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        if (null != typeface) {
            paint.setTypeface(typeface);
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int max = Math.max(height, width);
        double proportion = max / 800d;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / proportion), (int) (height / proportion), true);


        int paddingTop = 0;
        if (2 == location) { //居中
            paddingTop = scaledBitmap.getHeight() / 2;
        } else if (1 == location) { //上
            paddingTop = scaledBitmap.getHeight() / 6;
        } else if (3 == location) { //下
            paddingTop = scaledBitmap.getHeight() / 6 * 5;
        }


        paint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right 这里我设置为center
        paint.setTextAlign(Paint.Align.CENTER);
        if (colorStr.length() >= 7) {
            paint.setColor(Color.parseColor(colorStr));
        }
        paint.setTextSize(DensityUtils.dp2px(size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, scaledBitmap, text, paint, bounds,
                scaledBitmap.getWidth() / 2, paddingTop);
    }


    /**
     * 绘制文字到中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToDown(Context context, Bitmap bitmap, String text,
                                        int size, int color) {
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 2,
                (bitmap.getHeight() + bounds.height()) / 2);
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           TextPaint paint, Rect bounds, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        int x = paddingLeft, y = paddingTop;
        for (String line : text.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }

//        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     * 缩放图片
     *
     * @param src
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**
     * 图片压缩
     * @param activity
     * @param outputFile
     * @param okFileListener
     */
    public static void flowablePic(Activity activity, File outputFile, OkFileListener okFileListener) {
        try {
            //使用鲁班压缩压缩上传图片
            Flowable.just(outputFile)
                    .observeOn(Schedulers.io())
                    .map(file -> {
                        List<File> files = Luban.with(activity).load(file).get();
                        return files.get(0);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            file -> okFileListener.complete(file)
                    );
        } catch (Exception e) {
            ExtensionKt.toast("压缩图片异常，请重试");
            e.printStackTrace();
        }
    }

    public static void createBitmap(Context context, Object url, OKBitmapListener okBitmapListener) {
        if (context == null || url == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(App.mInstance).asBitmap().apply(options).load(
                url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                okBitmapListener.complete(resource);
            }
        });
    }

    public static void createBitmap2(Context context, Object url, OKBitmapListener okBitmapListener) {
        if (context == null || url == null) {
            return;
        }
        RequestOptions options = new RequestOptions()
                .override(150,150)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(App.mInstance).asBitmap().apply(options).load(
                url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                okBitmapListener.complete(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                Glide.with(App.mInstance).asBitmap().apply(options).load(
                        R.mipmap.ic_launcher).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        okBitmapListener.complete(resource);
                    }
                });
            }
        });
    }
}
