package com.sky.smartbus.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author renshijie
 * @email 1192306691@qq.com
 * @createTime 2024/1/19
 * @describe FileUtil
 **/
public class FileUtil {
    /**
     * 保存bitmap
     *
     * @param bm
     * @param fileName
     * @param fileDirName
     * @throws IOException
     */
    public static boolean saveBitmap(final Bitmap bm, final String fileName, final String fileDirName, Bitmap.CompressFormat compressFormat) throws IOException {
        try {
            if (bm != null) {

                File dirFile = new File(fileDirName);

                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }
                File myCaptureFile = new File(fileDirName + fileName);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                bm.compress(compressFormat, 100, bos);
                bos.flush();
                bos.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Drawable转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        // 获取 Drawable 对象的尺寸
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        // 创建一个 Bitmap 对象
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 将 Drawable 对象绘制到 Bitmap 上
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);

        return bitmap;
    }
}
