package com.example.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.mybatis.SqlSessionLoader;
import com.example.demo.mybatis.po.User;
import com.example.demo.request.UserLoginRequest;
import com.example.demo.request.UserRegisterRequest;
import com.example.demo.response.ErrorResponse;
import com.example.demo.response.LoginResponse;
import com.example.demo.response.RegisterResponse;
import org.apache.ibatis.session.SqlSession;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody
    Object register(@RequestBody UserRegisterRequest request) throws IOException {
        SqlSession sqlSession = SqlSessionLoader.getSqlSession();
        User user = sqlSession.selectOne("com.example.demo.UserMapper.findUserByUsername", request.getUsername());
        if (user != null) {
            sqlSession.close();
            return new ErrorResponse("The username is already used");
        } else {
            sqlSession.insert("com.example.demo.UserMapper.addUser", new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getPhone()));
            sqlSession.commit();
            sqlSession.close();
            return new RegisterResponse(getToken(user)); // use your generated token here.
        }

    }
    public String getToken(User user) {
        String token = JWT.create().withAudience(user.getUsername()).sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public @ResponseBody
    Object login(@RequestBody UserLoginRequest request) throws IOException{
        SqlSession sqlSession=SqlSessionLoader.getSqlSession();
        User user=sqlSession.selectOne("com.example.demo.UserMapper.findUserByUsername",request.getUsername());
        if(user==null){
            return new ErrorResponse("The username doesn't exist");
        }else if(!user.getPassword().equals(request.getPassword())){
            return new ErrorResponse("the password is wrong");
        }else{
            return new LoginResponse(getToken(user));
        }
    }
  @RequestMapping("allusers")
        public @ResponseBody
    Object login() throws  IOException{
      SqlSession sqlSession=SqlSessionLoader.getSqlSession();
        List users=sqlSession.selectList("com.example.demo.UserMapper.showAllUsers");
        return users;
  }
}