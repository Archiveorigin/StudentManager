package aliyun.xiaoming.filemanage.EntityClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Files {
    private Long id;
    private String fileName;
    private String filePath;
    private String fileType;
    private String fileSize;
    private LocalDateTime uploadTime;
    private Long userId;

}
