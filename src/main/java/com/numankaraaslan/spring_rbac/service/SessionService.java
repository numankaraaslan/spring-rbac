package com.numankaraaslan.spring_rbac.service;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class SessionService
{
	private final SessionRegistry sessionRegistry;

	public SessionService(SessionRegistry sessionRegistry)
	{
		this.sessionRegistry = sessionRegistry;
	}

	public void invalidateAll(HttpServletRequest request)
	{
		// I AM THE INVALIDATOOOOORRRR !!!
		String currentSessionId = null;
		if (request != null)
		{
			HttpSession session = request.getSession(false);
			if (session != null)
			{
				currentSessionId = session.getId();
			}
		}
		for (Object principal : sessionRegistry.getAllPrincipals())
		{
			for (SessionInformation si : sessionRegistry.getAllSessions(principal, false))
			{
				if (currentSessionId != null && currentSessionId.equals(si.getSessionId()))
				{
					continue;
				}
				si.expireNow();
			}
		}
	}
}