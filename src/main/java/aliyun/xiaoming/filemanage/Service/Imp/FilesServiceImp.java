package aliyun.xiaoming.filemanage.Service.Imp;

import aliyun.xiaoming.filemanage.EntityClass.FileUploadDTO;
import aliyun.xiaoming.filemanage.Mapper.FilesMapper;
import aliyun.xiaoming.filemanage.Service.FilesService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FilesServiceImp implements FilesService {

    @Autowired
    private FilesMapper filesMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public boolean uploadFile(FileUploadDTO uploadDTO) throws IOException {
        //拿到的完整文件名
        MultipartFile file = uploadDTO.getFile();
        String normalName = file.getOriginalFilename();
        String fileExtension = file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf("."));
        // 文件名 + 文件扩展名
        String uniqueFileName = UUID.randomUUID() + fileExtension;

        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // 将完整的文件名转变为绝对路径
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        // 1、文件名  2、文件路径（相对） 3、文件大小 4、文件类型 5、上传时间 6、上传者id
        return filesMapper.insertFile(normalName,uploadPath.toString(),
                file.getSize(), fileExtension, LocalDateTime.now(), uploadDTO.getUserId());
    }
}
