package com.cspiration.mooc.controller;

import com.cspiration.mooc.entity.Comment;
import com.cspiration.mooc.entity.UserAuth;
import com.cspiration.mooc.feign.MoocMSDeclareService;
import com.cspiration.mooc.service.ICommentService;
import com.cspiration.mooc.service.IUserAuthService;
import com.cspiration.mooc.service.imp.ElasticSearchService;
import com.cspiration.mooc.support.UserSupport;
import com.cspiration.mooc.utils.JsonResponse;
import com.cspiration.mooc.utils.RedisUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * 本代码来自 Cspiration，由 @Cspiration 提供
 * - 讲师：Edward Shi
 * - 官方网站：https://cspiration.com
 * - 版权所有，禁止转发和分享
 */
@Slf4j
@RestController
public class UserAuthRESTController {

    @Autowired
    private IUserAuthService iUserAuthService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private ICommentService iCommentService;

    @Autowired
    private MoocMSDeclareService moocMSDeclareService;

    @HystrixCommand(fallbackMethod = "error",
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000"
                    )
            }
    )
    @GetMapping("/timeout-Hystrix")
    public String hystrix(@RequestParam Long time) {
        return moocMSDeclareService.timeout(time);
    }

    public String error(Long time) {
        return "超时出错";
    }


    //    @OperLog(message = "用户注册", operation = OperationType.SIGNIN)
    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody UserAuth userAuth) {
//        iUserAuthService.insert(userAuth);
//        return JsonResponse.success();

//        userAuth.setId(1L);
//        iUserAuthService.insert2(userAuth);

        userAuth = iUserAuthService.insert(userAuth);
        redisUtils.set(userAuth.getUsername(), userAuth);
        elasticSearchService.addUser(userAuth);
        return JsonResponse.success();
    }

    @PostMapping("/comment")
    public JsonResponse<String> addComment(Comment comment) {
        comment = iCommentService.insert(comment);
        elasticSearchService.addComment(comment);
        return JsonResponse.success();
    }

    @GetMapping("/es-comments")
    public JsonResponse<Comment> getComments(@RequestParam String keyword) {
        Comment comment = elasticSearchService.getComment(keyword);
        return new JsonResponse<>(comment);
    }

    @GetMapping("/es-users")
    public JsonResponse<UserAuth> getUsers(@RequestParam String keyword) {
        UserAuth userAuth = elasticSearchService.getUser(keyword);
        return new JsonResponse<>(userAuth);
    }

    @GetMapping("/contents")
    public JsonResponse<List<Map<String, Object>>> getContents(@RequestParam String keyword,
                                                               @RequestParam Integer pageNo,
                                                               @RequestParam Integer pageSize) throws IOException {
        List<Map<String, Object>> list = elasticSearchService.getContents(keyword, pageNo, pageSize);
        return new JsonResponse<>(list);
    }

    @GetMapping("/users/{username}")
    public boolean getUser(@PathVariable String username) {
        return iUserAuthService.getByUsername(username);
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody UserAuth userAuth) {
        iUserAuthService.update(userAuth);
        log.info(redisUtils.get(userAuth.getUsername()).toString());
        return JsonResponse.success();
    }

    @PostMapping("/users-tokens")
    public JsonResponse<String> login(@RequestBody UserAuth userAuth) {
        String token = iUserAuthService.authREST(userAuth);
        return new JsonResponse<>(token);
    }

    @GetMapping("/users")
    public JsonResponse<UserAuth> getUsers() throws NoSuchAlgorithmException {
        Long userId = userSupport.getCurrentUserId();
        UserAuth userAuth = iUserAuthService.getByUserId(userId);
        return new JsonResponse<>(userAuth);
    }


}
