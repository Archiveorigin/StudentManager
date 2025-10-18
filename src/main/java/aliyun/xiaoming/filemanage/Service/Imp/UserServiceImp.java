package aliyun.xiaoming.filemanage.Service.Imp;

import aliyun.xiaoming.filemanage.EntityClass.User;
import aliyun.xiaoming.filemanage.Mapper.UserMapper;
import aliyun.xiaoming.filemanage.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
    }
}
