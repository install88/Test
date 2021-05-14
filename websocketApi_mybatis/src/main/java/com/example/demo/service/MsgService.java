package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.VO.MsgVO;
import com.example.demo.mapper.MsgMapper;

@Service
public class MsgService {	

	@Autowired
    private MsgMapper msgMapper;
    
    public void saveMsg(MsgVO msgVO) {
    	msgMapper.Msginsert(msgVO);
    }
}
