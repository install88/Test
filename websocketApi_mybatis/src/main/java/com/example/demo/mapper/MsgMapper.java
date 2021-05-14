package com.example.demo.mapper;

import com.example.demo.VO.MsgVO;
import java.util.List;


public interface MsgMapper{
    /**
     * 查詢所有的使用者
     * @return
     */
    List<MsgVO> queryMsgList();	
}
