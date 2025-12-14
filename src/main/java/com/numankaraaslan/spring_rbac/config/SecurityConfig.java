package com.numankaraaslan.spring_rbac.config;

import java.io.IOException;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

import com.numankaraaslan.spring_rbac.util.CustomAuthorizationManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig
{
	private final CustomAuthorizationManager customAuthManager;
	private final SessionRegistry sessionRegistry;
	private final RedirectSuccessHandler dbRedirectSuccessHandler;

	public SecurityConfig(CustomAuthorizationManager dbAuthManager, SessionRegistry sessionRegistry, RedirectSuccessHandler dbRedirectSuccessHandler)
	{
		this.customAuthManager = dbAuthManager;
		this.sessionRegistry = sessionRegistry;
		this.dbRedirectSuccessHandler = dbRedirectSuccessHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.sessionManagement(sm -> sm.maximumSessions(-1).sessionRegistry(sessionRegistry));
		http.csrf(custom -> custom.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
		http.headers(headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; " + "script-src 'self'; " + "style-src 'self' 'unsafe-inline'; " + "img-src 'self' data:; " + "font-src 'self'; " + "connect-src 'self'; " + "frame-ancestors 'none'; " + "object-src 'none'")).xssProtection(_ -> Customizer.withDefaults()).frameOptions(frame -> frame.deny()).referrerPolicy(ref -> ref.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)));
		http.authorizeHttpRequests(registry -> registry.requestMatchers("/", "/wellcome", "/authexception", "/login", "/logout", "/error", "/css/**", "/js/**").permitAll());
		http.authorizeHttpRequests(cus -> cus.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll());
		// -------------------------------------------------------------
		// Dynamic URL Authorization
		// Everything other than /login and static resources
		// must go through your DB-driven AuthorizationManager
		// -------------------------------------------------------------
		http.authorizeHttpRequests(reg -> reg.anyRequest().access(this.customAuthManager));
		http.exceptionHandling(ex -> ex.accessDeniedHandler(new AccessDeniedHandler()
		{
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException
			{
				response.sendRedirect(request.getContextPath() + "/authexception");
			}
		}));
		http.formLogin(login -> login.successHandler(dbRedirectSuccessHandler));
		return http.build();
	}
}
