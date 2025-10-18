package aliyun.xiaoming.filemanage.Mapper;


import aliyun.xiaoming.filemanage.EntityClass.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("insert into user value (#{id},#{name})")
    void insertUser(User user);
}
