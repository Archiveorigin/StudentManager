package aliyun.xiaoming.filemanage.Controller;


import aliyun.xiaoming.filemanage.EntityClass.Result;
import aliyun.xiaoming.filemanage.EntityClass.User;
import aliyun.xiaoming.filemanage.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("api")
public class UserController {

    @Autowired
    private UserService userService;

    public Result login(){
        log.info("用户登录");

        return Result.success();
    }
    public Result register(){
        log.info("用户注册");

        return Result.success();
    }

    @PostMapping("/userAdd")
    public Result insertUser(@RequestParam User user){
        log.info("新用户上传文件");
        userService.insertUser(user);
        return Result.success();
    }
}
