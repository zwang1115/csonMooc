package com.cspiration.mooc.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{username}")
@Slf4j
public class WebSocket {

    private Session session;

    private String username;

    private String toUsername;

    private static Integer userNumber = 0;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        webSocketSet.add(this);
        userNumber++;
        this.username = username;


        Set<String> userList = new TreeSet<>();
        for (WebSocket webSocket : webSocketSet) {
            userList.add(webSocket.username);
        }

        Map<String, Object> map1 = new HashMap<>();
        map1.put("onlineUsers", userList);

        map1.put("messageType", 1);
        map1.put("username", username);
        map1.put("number", this.userNumber);
        sendMessageAll(JSON.toJSONString(map1), this.username);
        log.info("总人数为：{}",  this.userNumber);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("messageType", 3);
        map2.put("onlineUsers", userList);
        map2.put("number", this.userNumber);
        sendMessageAll(JSON.toJSONString(map2), this.username);

    }

    @OnClose
    public void onClose() throws IOException {
        webSocketSet.remove(this);
        userNumber--;


        Map<String, Object> map1 = new HashMap<>();
        map1.put("messageType", 2);
        map1.put("onlineUsers", this.webSocketSet);
        map1.put("username", this.username);
        map1.put("number", userNumber);
        sendMessageAll(JSON.toJSONString(map1), this.username);


        Map<String, Object> map2 = new HashMap<>();
        Set<String> userList = new TreeSet<>();
        for (WebSocket webSocket : webSocketSet) {
            userList.add(webSocket.username);
        }
        map2.put("messageType", 3);
        map2.put("onlineUsers", userList);
        map2.put("number", this.userNumber);
        sendMessageAll(JSON.toJSONString(map2), this.username);
        log.info("有人断开，现在总人数为：{}",  webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) throws IOException{
        log.info("收到浏览器发来的消息：{}", message);
        JSONObject jsonObject = JSON.parseObject(message);
        String textMessage = jsonObject.getString("message");
        String username = jsonObject.getString("username");
        String type = jsonObject.getString("type");
        String toUsername = jsonObject.getString("tousername");

        if (type.equals("群发")) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("messageType", 4);
            map1.put("onlineUsers", this.webSocketSet);
            map1.put("username", this.username);
            map1.put("number", userNumber);
            map1.put("textMessage", textMessage);
            sendMessageAll(JSON.toJSONString(map1), this.username);
        } else {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("messageType", 4);
            map2.put("onlineUsers", this.webSocketSet);
            map2.put("username", username);
            map2.put("number", userNumber);
            map2.put("textMessage", textMessage);
            sendMessageTo(JSON.toJSONString(map2), toUsername);
        }
    }

    public void sendMessageAll(String message, String FromUsername) throws IOException {
        for (WebSocket webSocket : webSocketSet) {
            webSocket.session.getBasicRemote().sendText(message);
        }
    }

    public void sendMessageTo(String message, String toUsername) throws IOException {
        for (WebSocket webSocket : webSocketSet) {
            if (webSocket.username.equals(toUsername)) {
                webSocket.session.getBasicRemote().sendText(message);
                log.info("【发送消息】:{}", this.username + "向" + toUsername + "发送消息：" + message);
            }
        }
    }
}
