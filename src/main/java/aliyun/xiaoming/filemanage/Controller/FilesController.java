package aliyun.xiaoming.filemanage.Controller;


import aliyun.xiaoming.filemanage.EntityClass.FileUploadDTO;
import aliyun.xiaoming.filemanage.EntityClass.Files;
import aliyun.xiaoming.filemanage.EntityClass.Result;
import aliyun.xiaoming.filemanage.Service.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FilesController {

    @Autowired
    private FilesService filesService;

    @PostMapping()
    public Result InsertFile(@RequestParam("file") MultipartFile file,
                          @RequestParam("userId") Long userId) throws IOException {
        log.info("接受前端传参：file={}, userId={}",file,userId);
        // 将前端传来的参数封装为一个类参
        FileUploadDTO dto = new FileUploadDTO();
        dto.setUserId(userId);
        dto.setFile(file);

        return Result.success(filesService.uploadFile(dto));
    }
}
