package aliyun.xiaoming.filemanage.Mapper;


import aliyun.xiaoming.filemanage.EntityClass.Files;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface FilesMapper {
    boolean insertFile(String name, String path, Long size, String type, LocalDateTime time, Long userId);
}
