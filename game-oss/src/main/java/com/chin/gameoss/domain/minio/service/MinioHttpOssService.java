package com.chin.gameoss.domain.minio.service;

import com.chin.common.utils.ResultInfo;
import com.chin.common.utils.ResultInfoUtil;
import com.chin.gameoss.application.IMinioOssService;
import com.jvm123.minio.service.MinioFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/14 15:04
 */
@Service
public class MinioHttpOssService implements IMinioOssService {

    @Autowired
    MinioFileService minioFileService;

    @Override
    public ResultInfo create(String bucketName) {
        return minioFileService.createBucket(bucketName) ? ResultInfoUtil.buildSuccess() : ResultInfoUtil.buildError();
    }

    @Override
    public ResultInfo upload(MultipartFile file, String bucketName) {
        try {
            minioFileService.save(bucketName,file.getInputStream(),file.getOriginalFilename());
        } catch (IOException e) {
            return ResultInfoUtil.buildError();
        }
        return ResultInfoUtil.buildSuccess();
    }

    @Override
    public ResultInfo delete(String bucketName, String filename) {
        return minioFileService.delete(bucketName, filename) ? ResultInfoUtil.buildSuccess() : ResultInfoUtil.buildError();
    }

    @Override
    public void download(HttpServletResponse httpServletResponse, String bucketName, String filename) {
        try (InputStream inputStream= minioFileService.getStream(bucketName, filename)) {
            httpServletResponse.addHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream os = httpServletResponse.getOutputStream();
            minioFileService.writeTo(bucketName, filename, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
