package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.VO.MsgVO;
import com.example.demo.mapper.MsgMapper;

@RestController
public class UserController {
    @Autowired
    private MsgMapper msgMapper;

    @GetMapping("/queryMsgList")
    public List<MsgVO> queryUserList(){    	
        return msgMapper.queryMsgList();
    }

}
