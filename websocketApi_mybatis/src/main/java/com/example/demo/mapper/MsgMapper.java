package com.example.demo.mapper;

import com.example.demo.VO.MsgVO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface MsgMapper{
    /**
     * 聊天室相關
     * @return
     */
    //insert訊息至資料庫
    int msgSave(MsgVO msgVO);
        
    //User登入時點開好友清單，只顯示其好友們最新一筆資料(像line一樣)
    List<MsgVO> getAllFromLastMessage(String msg_to);  
        
    //點開對象聊天室後，查詢與該對象的聊天紀錄
    List<MsgVO> getConversationRecord(String msg_from, String msg_to);
    
    //將該好友訊息狀態從未讀改成已讀
    int msgUpdateStatus(String msg_from,String msg_to,Timestamp msg_time);
    
    //取得未讀訊息筆數
    List<Map<String,Object>> getUnreadCount(String msg_to);

    //取得關鍵字搜尋筆數
    List<Map<String,Object>> searchKeyword(MsgVO msgVO);
        
    //將未讀訊息修改成已讀狀態
    int msgUpdateStatus(String msg_from,String msg_to);
 
}
