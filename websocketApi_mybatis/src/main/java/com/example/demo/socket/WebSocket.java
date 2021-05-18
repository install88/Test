package com.example.demo.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.VO.MsgVO;
import com.example.demo.service.MsgService;

import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoader;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {	
	private static ApplicationContext applicationContext;
	 
	public static void setApplicationContext(ApplicationContext context) {
	    applicationContext = context;
	}
	private MsgService msgService;	 
	
	
	/**
     * 在線人數
     */
    public static int onlineNumber = 0;
    /**
     * 以User姓名为key，將WebSocket為對象保存起来
     */
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    /**
     * 儲存session
     */
    private Session session;
    /**
     * User名稱
     */
    private String username;

    /**
     * OnOpen 表示有瀏覽器連接過來的時候調用
     * OnClose 表示有瀏覽器發出關閉請求的時候被調用
     * OnMessage 表示有瀏覽器發消息的時候被調用
     * OnError 表示有錯誤發生，比如網路斷掉
     */

    /**
     * 建立連線
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
    	addOnlineCount();
        this.username = username;
        this.session = session;        
        try {
            //把自己的資訊加入到map管理
            clients.put(username, this);
            
            //查詢給自己的訊息
            msgService = applicationContext.getBean(MsgService.class);
            List lastMsg_list = msgService.getAllFromLastMessage(username);
            Map<String, Object> lastMsg_map = new HashMap<>();
            lastMsg_map.put("showLastMsg", lastMsg_list);
            sendMessageTo(JSON.toJSONString(lastMsg_map), username);
            //查詢未讀訊息count
            List msg_count_list = msgService.getUnreadCount(username);
            Map<String, Object> msg_count_map = new HashMap<>();
            msg_count_map.put("showMsgCount", msg_count_list);
            sendMessageTo(JSON.toJSONString(msg_count_map), username);                              
        } catch (IOException e) {
        	//上線的時候發生了錯誤
        	System.out.println(e.getMessage());
        }


    }

    @OnError
    public void onError(Session session, Throwable error) {
    	//Server發生錯誤
    	System.out.println(error.getMessage());
    }

    /**
     * 連接關閉
     */
    @OnClose
    public void onClose() {
        try {
        	subOnlineCount();
            clients.remove(username);
        } catch (Exception e) {
        	//下線發生錯誤
        	System.out.println(e.getMessage());
        }
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
        	msgService = applicationContext.getBean(MsgService.class);        	
        	//parse前端傳來的訊息，並封裝至msgVO中
            JSONObject jsonObject = JSON.parseObject(message);
            if("save".equals(jsonObject.getString("msg_type"))) {
            	//發送訊息保存至DB
                MsgVO msgVO = new MsgVO();
                msgVO.setMsg_from(jsonObject.getString("msg_from"));
                msgVO.setMsg_to(jsonObject.getString("msg_to"));
                msgVO.setMsg_content(jsonObject.getString("message"));            
                msgVO.setMsg_status(jsonObject.getInteger("msg_status"));
                msgService.saveMsg(msgVO);
                
                //將訊息推撥至對方
                Map<String, Object> msg_receive_map = new HashMap<>();
                msg_receive_map.put("receive", msgVO);
                if(clients.get(msgVO.getMsg_to())!= null) {
                	sendMessageTo(JSON.toJSONString(msg_receive_map), msgVO.getMsg_to());
                }                
            }else {
            	//查詢與對方的聊天紀錄
                MsgVO msgVO = new MsgVO();
                String msg_from = jsonObject.getString("msg_from");
                String msg_to = jsonObject.getString("msg_to");
                List msg_record_list = msgService.getConversationRecord(msg_from, msg_to);
                Map<String, Object> msg_record_map = new HashMap<>();
                msg_record_map.put("showRecord", msg_record_list);                
                System.out.println(msg_record_list);
                sendMessageTo(JSON.toJSONString(msg_record_map), msg_to);
                
                //令訊息狀態改為已讀
                msgService.msgUpdateStatus(msg_from, msg_to);
            }

                                   
            //呼叫getMemberInfoById
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "http://192.168.50.93:9082/api/v1/org/getMember/" + msgVO.getFrom();
//            HttpHeaders headers = new HttpHeaders();
//            HttpEntity<String> entity = new HttpEntity<String>(headers);
//            String strbody = restTemplate.exchange(url, HttpMethod.GET, entity,String.class).getBody();
//            JSONObject jsonObject2 = JSON.parseObject(strbody);
//            JSONObject usernameTest = jsonObject2.getJSONObject("RET");
            
            
            //如果不是发给所有，那么就发给某一个人
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
//            Map<String, Object> map1 = new HashMap<>();
//            map1.put("messageType", 4);
//            map1.put("textMessage", msgVO.getMessage());
//            map1.put("fromusername", msgVO.getFrom());
//            if (msgVO.getMsg_to().equals("All")) {
//                map1.put("tousername", "所有人");
//                sendMessageAll(JSON.toJSONString(msgVO));
//            } else {
//                map1.put("tousername", msgVO.getTo());
//                sendMessageTo(JSON.toJSONString(msgVO), msgVO.getMsg_to());
//            }
        } catch (Exception e) {
//            log.info("發生錯誤);
        }
    }

    
    //將訊息送至指定用戶
    public void sendMessageTo(String message, String ToUserName) throws IOException {
    	WebSocket itemTest = clients.get(ToUserName);
		itemTest.session.getAsyncRemote().sendText(message);    	
    }
    
    
    //廣播給在線上的所有User
    public void sendMessageAll(String message) throws IOException {
        for (WebSocket item : clients.values()) {
        	System.out.println(item.username);
            item.session.getAsyncRemote().sendText(message);
        }
    }

    //取得在線人數
    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }
    
    //用戶連線時，在線人數++    
    public static synchronized void addOnlineCount() {
    	WebSocket.onlineNumber++;
    }
    
    //用戶離線時，在線人數--    
    public static synchronized void subOnlineCount() {
    	WebSocket.onlineNumber--;
    }    

}
