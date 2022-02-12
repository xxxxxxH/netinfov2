package net.basicmodel.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.xxxxxxh.mailv2.utils.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class FileUtils {

    public static void saveFile(Context context, String content, String fileName) {

        File folder = new File(Constant.INSTANCE.getFilePath());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(Constant.INSTANCE.getFilePath() + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
            ClipUtils.INSTANCE.copy(context, Constant.INSTANCE.getFilePath() + File.separator + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static String readrFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        } else {
            try {
                FileReader reader = new FileReader(filePath);
                BufferedReader r = new BufferedReader(reader);
                return r.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
