package com.chin.gameoss.application;

import com.chin.common.utils.ResultInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/14 15:05
 */
public interface IMinioOssService {

    /**
     * 创建bucket
     * @param bucketName
     * @return
     */
    ResultInfo create(String bucketName);

    /**
     * 上传文件
     * @param file
     * @param bucketName
     * @return
     */
    ResultInfo upload(MultipartFile file, String bucketName);

    /**
     * 删除文件
     * @param bucketName
     * @param filename
     * @return
     */
    ResultInfo delete(String bucketName, String filename);

    /**
     * 下载文件
     * @param httpServletResponse
     * @param bucketName
     * @param filename
     */
    void download(HttpServletResponse httpServletResponse, String bucketName, String filename);
}
