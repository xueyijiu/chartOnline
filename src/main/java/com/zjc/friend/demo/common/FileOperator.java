package com.zjc.friend.demo.common;

import com.baomidou.mybatisplus.generator.config.rules.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

public interface FileOperator {
    /**
     * 将输入流上传到服务器，并在本地创建临时文件。
     *
     * @param in       输入流
     * @param fileName 原始文件名称
     * @return 返回文件结果，包含本例临时文件以及上传后的相对路径
     */
    FileResult uploadFile(InputStream in, String fileName);

    /**
     * 添加多张图片
     *
     * @param files
     * @param
     * @return
     */
    Map<String, String> uploadFiles(MultipartFile[] files);
}
