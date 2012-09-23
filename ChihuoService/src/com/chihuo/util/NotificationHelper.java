package com.chihuo.util;

import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.json.JSONException;
import org.json.JSONObject;

import com.chihuo.bussiness.Desk;

public class NotificationHelper {

    public static void sendNotifcationToUser(NotificationType notificationType, String message, String user) {
    	NotificationManager notificationManager = new NotificationManager();
		String url = "";
		String apiKey = Config.getString("apiKey", "");
		notificationManager.sendNotifcationToUser(apiKey, user, notificationType.toString(), message, url);
    }
    
    
    public static String getDeskString(Desk desk){
    	JSONObject jsonObject = new JSONObject();
    	try {
			jsonObject.put("id", desk.getId());
			jsonObject.put("name", desk.getName());
		} catch (JSONException e) {
		}
    	return jsonObject.toString();
    }
    
    
    public enum NotificationType{
    	AddMenu (11, "加减菜"),
    	RequestCheckOut (12, "请求结账"),
    	CheckOut (13, "结账"),
    	CheckIn (14, "撤单"),
    	
    	AddWater (21, "加水"),
    	AddDish (22, "加餐具"),
    	CallWaiter (23, "叫服务员");

        private final int type;   // in kilograms
        private final String description; // in meters
        NotificationType(int type, String des) {
            this.type = type;
            this.description = des;
        }
        
        public String toString() {
			return type + "";
		}
    }
    
}