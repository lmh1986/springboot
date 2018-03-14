package com.yq.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yq.demo.dao.UserJpaRepository;
import com.yq.demo.entity.User;
import com.yq.demo.other.UserDemo;
import com.yq.demo.service.UserService;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/user") // This means URL's start with /demo (after Application path)
public class UserController {
    @Autowired // This means to get the bean called userRepository
               // Which is auto-generated by Spring, we will use it to handle the data
    private UserJpaRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping(path="/add") // Map ONLY GET Requests
    public @ResponseBody String addNewUser(@RequestParam String name
            , @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User user = new User();
        user.setUsername("userName");
        user.setFullname("fullName");
        user.setEmail(email);
        user.setActive(1);
        user.setUserType(1);

        userRepository.save(user);
        return "Saved";
    }

    @GetMapping(path="/find") // Map ONLY GET Requests
    public @ResponseBody User findByName (@RequestParam String name) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        return userRepository.getByUserName(name);
    }


    /**
     * 测试thymeleafHello2?name=zhangSan
     * @return
     */
    @RequestMapping(value = "/thymeleafHello2",method = RequestMethod.GET)
    public String hello2(Model model, @RequestParam("name") String name) {

        UserDemo user = new UserDemo(3, "John", "john@163.com", "John is a designer");
        model.addAttribute("name", name);
        model.addAttribute("user", user);
        return "thymeleafHello";
    }

    /*@GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
    */

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("userList", userList);
        return "admin/user/users";
    }

    @GetMapping(path="/init")
    public @ResponseBody Iterable<User> insertInitialUsers() {
        // This returns a JSON or XML with the users
        User user = new User();
        user.setUsername("张三");
        user.setEmail("zhangsan@qq.com");
        userRepository.save(user);

        user = new User();
        user.setUsername("李四");
        user.setEmail("lisi@qq.com");
        userRepository.save(user);

        user = new User();
        user.setUsername("王五");
        user.setEmail("wangwu@qq.com");
        userRepository.save(user);

        return userRepository.findAll();
    }

    @GetMapping(path="/delete/{id}")
    public @ResponseBody String deleteUser(@PathVariable Long id) {
         userRepository.delete(id);
         return  "delete one";
    }

    @RequestMapping("/findByName/{name}")
    @ResponseBody
    public Map<String,Object> getUserByName(@PathVariable String name){
        Map<String,Object> result = new HashMap<String, Object>();
        User user = userService.getUserByName(name);
        if (user != null) {
            result.put("name", user.getUsername());
            result.put("Email", user.getEmail());
            result.put("Type",user.getUserType());
        }
        return result;
    }

}