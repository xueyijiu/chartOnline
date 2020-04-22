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

    @Value("${com.zjc.friend.image-path}")
    private String imgPath;

    //是否需要生成临时文件
    @Value("${com.zjc.friend.create-tmp-file}")
    private boolean createTmpFile;

    @Override
    public FileResult uploadFile(InputStream in, String fileName) {
        String relativePath = genRelativePath(fileName);
        String partPath = relativePath.replaceAll("/", Matcher.quoteReplacement(File.separator));
        FileResult fr = new FileResult();
        try {
            String fullPath = imgPath + partPath;
            File file = new File(fullPath);
            file.getParentFile().mkdirs();
            if (createTmpFile) {
                String tmpDir = System.getProperty("java.io.tmpdir");
                String tmpFullPath = tmpDir + partPath;
                File tmp = new File(tmpFullPath);
                tmp.getParentFile().mkdirs();
                FileCopyUtils.copy(in, new FileOutputStream(tmp));
                FileCopyUtils.copy(tmp, file);
                fr.setLocalFile(tmp);
            } else {
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

    @Override
    public Map<String, String> uploadFiles(MultipartFile[] files) {
        String filePath = "";
        List<Map> list = new ArrayList<>();
        Map<String, String> map = new HashMap();
        if (files == null || files.length == 0) {
            return null;
        }
        if (imgPath.endsWith("/")) {
            imgPath = imgPath.substring(0, imgPath.length() - 1);
        }
        for (MultipartFile file : files) {
            filePath = imgPath + file.getOriginalFilename();
            makeDir(imgPath);
            File dest = new File(filePath);
            try {
                file.transferTo(dest);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                throw new RuntimeException("文件操作失败", e);
            }
            map.put(file.getOriginalFilename(), filePath);
//            list.add(map);
        }
        return map;
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
