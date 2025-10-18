package aliyun.xiaoming.filemanage.Service;

import aliyun.xiaoming.filemanage.EntityClass.FileUploadDTO;

import java.io.IOException;

public interface FilesService {
    boolean uploadFile(FileUploadDTO uploadDTO) throws IOException;
}
