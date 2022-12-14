package com.chin.gameoss.interfaces;

import com.chin.common.utils.ResultInfo;
import com.chin.gameoss.application.IMinioOssService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @PROJECT_NAME: game-world
 * @DESCRIPTION:
 * @author: qi
 * @DATE: 2022/12/14 20:18
 */
@RestController
@RequestMapping("/oss/minio")
public class MinioOssController {

    @Resource
    private IMinioOssService minioOssService;

    /**
     * bucket
     * @param bucketName
     * @return
     */
    @GetMapping("/create")
    @ApiOperation(value="创建Bucket服务",notes = "创建Bucket服务")
    public ResultInfo create(@RequestParam("bucketName") String bucketName){
        return minioOssService.create(bucketName);
    }


    /**
     * 存储文件
     * @param file
     * @param bucketName
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value="上传文件",notes = "上传文件到指定Bucket服务")
    @ApiImplicitParam(name = "file", value = "上传的文件", dataType = "java.io.File", required = true)
    public ResultInfo upload(@RequestParam("file") MultipartFile file, @RequestParam("bucketName") String bucketName){
        return minioOssService.upload(file,bucketName);
    }

    /**
     * 删除文件
     * @param bucketName
     * @param bucketName
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value="删除文件",notes = "删除文件指定Bucket服务")
    public ResultInfo delete(@RequestParam("bucketName") String bucketName, @RequestParam("fileName") String fileName){
            return minioOssService.delete(bucketName,fileName);
    }


    /**
     * 下载文件
     * @param bucketName
     * @param bucketName
     * @return
     */
    @GetMapping("/download")
    @ApiOperation(value="下载文件",notes = "下载文件指定Bucket服务")
    public void download(HttpServletResponse httpServletResponse, @RequestParam("bucketName") String bucketName, @RequestParam("fileName") String fileName){
            minioOssService.download(httpServletResponse,bucketName,fileName);
    }

}
