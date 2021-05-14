package com.example.demo.mapper;

import com.example.demo.VO.MsgVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MsgMapper{
    /**
     * 查詢所有的使用者
     * @return
     */
    List<MsgVO> queryMsgList();	
}
