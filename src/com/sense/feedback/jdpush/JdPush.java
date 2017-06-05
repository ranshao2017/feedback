package com.sense.feedback.jdpush;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 极光推送
 * @author think
 */
public class JdPush {
	private static final Log logger = LogFactory.getLog(JdPush.class);
	
	private static final String appKey = "aeed4eb68d931b3ce5d408ca";
	private static final String masterSecret = "32e9b71d414c1363577443b1";
	private static final int TIMES = 3;//最多重复发送次数
	public static JPushClient jpushClient = new JPushClient(masterSecret, appKey, TIMES);
	
    /** 
     * 推送自定义消息接口 修改
     * @param content 推送内容 
     * @param clientMap <clientType,clientID[]>
     */  
    public static void sendPushMessage(String content, String title, Map<String, List<String>> clientMap) {  
    	if(StringUtils.isBlank(content) || null == clientMap || clientMap.size() < 1){
    		return;
    	}
    	
    	for(Entry<String, List<String>> entry : clientMap.entrySet()){
    		Builder builder = PushPayload.newBuilder();
        	builder.setPlatform(Platform.all());
    		builder.setAudience(Audience.registrationId(entry.getValue()));
    		Map<String, String> extras = new HashMap<String, String>();
    		if("android".equals(entry.getKey())){
    			builder.setNotification(Notification.android(content, title, extras));
    		}else if("ios".equals(entry.getKey())){
    			builder.setNotification(Notification.ios(title + ":" + content, extras));
    		}else if("winphone".equals(entry.getKey())){
    			builder.setNotification(Notification.winphone(title + ":" + content, extras));
    		}
    		PushPayload payload = builder.build();
			try {
				jpushClient.sendPush(payload);
			} catch (APIConnectionException e) {
				logger.error("推送消息连接异常", e);
			} catch (APIRequestException e) {
				logger.error("推送消息异常", e);
			}
    	}
    }
    
    public static void main(String[] args){
    	Map<String, List<String>> clientMap = new HashMap<String, List<String>>();
    	List<String> clientList = new ArrayList<String>();
    	clientList.add("13065ffa4e3927b1ef5");
    	clientMap.put("android", clientList);
    	JdPush.sendPushMessage("lalalalala啦啦啦啦啦", "同桌的你", clientMap);
    }
  
}