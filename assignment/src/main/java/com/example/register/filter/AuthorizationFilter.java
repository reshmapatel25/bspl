package com.example.register.filter;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer")) {
            filterChain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null) {
            String user = Jwts.parser().setSigningKey("SecretKeyToGenJWTs".getBytes())
                    .parseClaimsJws(token.replace("Bearer",""))
                    .getBody()
                    .getSubject();
            if(user != null) {
                List<GrantedAuthority> authorityList=new ArrayList<>();
                SimpleGrantedAuthority authority=null;
                if(user.equalsIgnoreCase("admin ")){
                    authority=new SimpleGrantedAuthority("ADMIN");
                }else{
                    authority=new SimpleGrantedAuthority("ROLE_USER");
                }
                authorityList.add(authority);

                return new UsernamePasswordAuthenticationToken(user, null, authorityList);
            }
            return null;
        }
        return null;
    }
}
