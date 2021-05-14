package com.example.demo.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.VO.MsgVO;
import com.example.demo.service.MsgService;

import lombok.extern.slf4j.Slf4j;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {
	
	static MsgService msgService;
	 
    @Autowired
    public void setMsgService(MsgService msgService){
    	WebSocket.msgService = msgService;
    }
	/**
     * 在线人数
     */
    public static int onlineNumber = 0;
    /**
     * 以用户的姓名为key，WebSocket为对象保存起来
     */
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    /**
     * 会话
     */
    private Session session;
    /**
     * 用户名称
     */
    private String username;

    /**
     * OnOpen 表示有浏览器链接过来的时候被调用
     * OnClose 表示浏览器发出关闭请求的时候被调用
     * OnMessage 表示浏览器发消息的时候被调用
     * OnError 表示有错误发生，比如网络断开了等等
     */

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {         	
        onlineNumber++;
        this.username = username;
        this.session = session;
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
            //先给所有人发送通知，说我上线了
            Map<String, Object> map1 = new HashMap<>();
            map1.put("messageType", 1);
            map1.put("username", username);
            sendMessageAll(JSON.toJSONString(map1));

            //把自己的信息加入到map当中去
            clients.put(username, this);
            //给自己发一条消息：告诉自己现在都有谁在线
            Map<String, Object> map2 = new HashMap<>();
            map2.put("messageType", 3);
            //移除掉自己
            Set<String> set = clients.keySet();
            map2.put("onlineUsers", set);
            sendMessageTo(JSON.toJSONString(map2), username);
        } catch (IOException e) {
//            log.info(username + "上线的时候通知所有人发生了错误");
        }


    }

    @OnError
    public void onError(Session session, Throwable error) {
//        log.info("服务端发生了错误" + error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        //webSockets.remove(this);
        clients.remove(username);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String, Object> map1 = new HashMap<>();
            map1.put("messageType", 2);
            map1.put("onlineUsers", clients.keySet());
            map1.put("username", username);
            sendMessageAll(JSON.toJSONString(map1));
        } catch (IOException e) {
//            log.info(username + "下线的时候通知所有人发生了错误");
        }
//        log.info("有连接关闭！ 当前在线人数" + onlineNumber);
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
        	//parse前端傳來的訊息，並封裝至msgVO中
            JSONObject jsonObject = JSON.parseObject(message);
            MsgVO msgVO = new MsgVO();
            msgVO.setMsg_from(jsonObject.getString("memID"));
            msgVO.setMsg_to(jsonObject.getString("to"));
            msgVO.setMsg_content(jsonObject.getString("message"));            
            msgVO.setMsg_status(jsonObject.getInteger("msg_status"));
            msgService.saveMsg(msgVO);
                                   
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
            if (msgVO.getMsg_to().equals("All")) {
//                map1.put("tousername", "所有人");
                sendMessageAll(JSON.toJSONString(msgVO));
            } else {
//                map1.put("tousername", msgVO.getTo());
                sendMessageTo(JSON.toJSONString(msgVO), msgVO.getMsg_to());
            }
        } catch (Exception e) {
//            log.info("发生了错误了");
        }
    }

    public void sendMessageTo(String message, String ToUserName) throws IOException {
    	WebSocket itemTest = clients.get(ToUserName);
    	itemTest.session.getAsyncRemote().sendText(message);
    }

    public void sendMessageAll(String message) throws IOException {
        for (WebSocket item : clients.values()) {
        	System.out.println(item.username);
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }

}
