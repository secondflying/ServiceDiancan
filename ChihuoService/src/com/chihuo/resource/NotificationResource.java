package com.chihuo.resource;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.Session;
import org.androidpn.server.xmpp.session.SessionManager;
import org.xmpp.packet.Presence;

import com.chihuo.bussiness.SessionVO;

@Path("/notification")
public class NotificationResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response send2OnlineUser(String string) {
		System.out.println(string);
		NotificationManager notificationManager = new NotificationManager();
        String title = "ffff";
        String message = "sdsdsdds";
        String uri = "www.baidu.com";

        String apiKey = Config.getString("apiKey", "");
        notificationManager.sendBroadcast(apiKey, title, message, uri);
		return Response.ok().build();
	}
	
	@Path("{username}")
	@POST
	public Response send2OneUser(@PathParam("username") String username) {
		NotificationManager notificationManager = new NotificationManager();
        String title = "ffff";
        String message = "sdsdsdds";
        String uri = "www.baidu.com";

        String apiKey = Config.getString("apiKey", "");
        
        notificationManager.sendNotifcationToUser(apiKey, username, title,
              message, uri);
        		
		return Response.ok().build();
	}
	
	@GET
	@Produces("application/json; charset=UTF-8")
	public Response getOnlineSession() throws UserNotFoundException, UnknownHostException {
		ClientSession[] sessions = new ClientSession[0];
        sessions = SessionManager.getInstance().getSessions().toArray(sessions);

        List<SessionVO> voList = new ArrayList<SessionVO>();
        for (ClientSession sess : sessions) {
            SessionVO vo = new SessionVO();
            vo.setUsername(sess.getUsername());
            vo.setResource(sess.getAddress().getResource());
            // Status
            if (sess.getStatus() == Session.STATUS_CONNECTED) {
                vo.setStatus("CONNECTED");
            } else if (sess.getStatus() == Session.STATUS_AUTHENTICATED) {
                vo.setStatus("AUTHENTICATED");
            } else if (sess.getStatus() == Session.STATUS_CLOSED) {
                vo.setStatus("CLOSED");
            } else {
                vo.setStatus("UNKNOWN");
            }
            // Presence
            if (!sess.getPresence().isAvailable()) {
                vo.setPresence("Offline");
            } else {
                Presence.Show show = sess.getPresence().getShow();
                if (show == null) {
                    vo.setPresence("Online");
                } else if (show == Presence.Show.away) {
                    vo.setPresence("Away");
                } else if (show == Presence.Show.chat) {
                    vo.setPresence("Chat");
                } else if (show == Presence.Show.dnd) {
                    vo.setPresence("Do Not Disturb");
                } else if (show == Presence.Show.xa) {
                    vo.setPresence("eXtended Away");
                } else {
                    vo.setPresence("Unknown");
                }
            }
            vo.setClientIP(sess.getHostAddress());
            vo.setCreatedDate(sess.getCreationDate());
            voList.add(vo);
        }
		
		GenericEntity<List<SessionVO>> entity = new GenericEntity<List<SessionVO>>(
				voList) {
		};
		return Response.ok(entity).build();
	}
}
