package com.zjc.friend.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 18:40 2020/2/25
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
@Slf4j
@Component
public class SimpleFileOperator implements FileOperator {

    /**
     * 保存图片的地址
     */
    @Value("${com.zjc.friend.image-path}")
    private String imgPath;

    //是否需要生成临时文件
    @Value("${com.zjc.friend.create-tmp-file}")
    private boolean createTmpFile;

    @Override
    public FileResult uploadFile(InputStream in, String fileName) {
        //获得文件的相对路径
        String relativePath = genRelativePath(fileName);
        //把相对路径中“/”符号，全改成系统定义文件路径符号（ Matcher.quoteReplacement(File.separator)--》windows就会为"\\"linux系统就为“/”）
        String partPath = relativePath.replaceAll("/", Matcher.quoteReplacement(File.separator));
        FileResult fr = new FileResult();
        try {
            String fullPath = imgPath + partPath;
            File file = new File(fullPath);
            //根据相对路径生成文件夹
            file.getParentFile().mkdirs();
            if (createTmpFile) {
                String tmpDir = System.getProperty("java.io.tmpdir");
                String tmpFullPath = tmpDir + partPath;
                File tmp = new File(tmpFullPath);
                tmp.getParentFile().mkdirs();
                //保存图片
                FileCopyUtils.copy(in, new FileOutputStream(tmp));
                FileCopyUtils.copy(tmp, file);
                fr.setLocalFile(tmp);
            } else {
                //保存图片
                FileCopyUtils.copy(in, new FileOutputStream(file));
                fr.setLocalFile(file);
            }
            fr.setRelativePath(relativePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件操作失败", e);
        }
        return fr;
    }


    /**
     * 确保目录存在，不存在则创建
     *
     * @param filePath
     */
    private static void makeDir(String filePath) {
        if (filePath.lastIndexOf('/') > 0) {
            String dirPath = filePath.substring(0, filePath.lastIndexOf('/'));
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    //根据区域生成文件相对路径
    private String genRelativePath(String fileName) {
        System.out.println(fileName);
        //获取图片类型
        String exName = fileName.substring(fileName.lastIndexOf("."));
        if (null != exName) {
            exName = exName.toLowerCase();
//            if("jpeg".equals(exName) || "png".equals(exName)){
//                exName = "jpg";
//            }
            if (".jpeg".equals(exName) || ".png".equals(exName)) {
                exName = ".jpg";
            }
        }
        //使用工具类UUID给图片重命名
        String newName = UUID.randomUUID().toString().replaceAll("-", "") + exName;
        int index = fileName.indexOf("/");
        if (index > 0) {
            newName = fileName.substring(0, index + 1) + newName;
        }

        Date now = new Date();
        String path = new DateFormatter("yyyy-MM-dd").print(now, Locale.CHINA) + "/" + newName;
        return path;
    }
}
