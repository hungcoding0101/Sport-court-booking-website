package com.hung.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.hung.Models.Player;

@WebListener
public class SessionListener implements HttpSessionListener{

	private static final Map<String, HttpSession> allSessions = new HashMap<String, HttpSession>();
	
	@Override
		public void sessionCreated(HttpSessionEvent se) {
		System.out.println("HERE: NEW SESSION CREATED: " + se.getSession().getId() + "||" + (((Player)se.getSession().getAttribute("verifiedPlayer"))));
			allSessions.put(se.getSession().getId(), se.getSession());
		}

	@Override
		public void sessionDestroyed(HttpSessionEvent se) {
			allSessions.remove(se.getSession().getId());
		}

	public List<HttpSession> getActiveSessions(){
		return new ArrayList<HttpSession>(allSessions.values());
	}
	
	@PostConstruct
		public void announce() {
			System.out.println("HERE: SESSION LISTENTER CREATED");
	}
}
