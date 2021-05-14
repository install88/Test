package com.example.demo.mapper;

import com.example.demo.VO.MsgVO;

import java.sql.Timestamp;
import java.util.List;


public interface MsgMapper{
    /**
     * 聊天室相關
     * @return
     */
	
	//測試查詢message全部資料
    List<MsgVO> queryMsgList();
    
    //insert訊息至資料庫
    int Msginsert(MsgVO msgVO);
        
//    select * 
//    from message msg 
//    where msg_to = "陳維正" and msg_time=(select max(msg_time) from message where msg_from=msg.msg_from);
    //User登入時點開好友清單，只顯示其好友們最新一筆資料(像line一樣)
    List<MsgVO> getAllFromLastMessage(String msg_to);  
    
    
    //點開對象聊天室後，查詢與該對象的聊天紀錄
    List<MsgVO> getConversationRecord(String msg_from, String msg_to);
    
    //將該好友訊息狀態從未讀改成已讀
    int msgUpdateStatus(String msg_from,String msg_to,Timestamp msg_time);
}
