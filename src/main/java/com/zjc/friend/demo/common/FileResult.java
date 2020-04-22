package com.zjc.friend.demo.common;

import java.io.File;

/**
 * 文件操作结果。
 */
public class FileResult {
    //本例缓存文件
    private File localFile;
    //上传后的相对路径
    private String relativePath;

    public File getLocalFile() {
        return localFile;
    }

    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
