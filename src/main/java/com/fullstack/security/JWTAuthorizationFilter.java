package com.fullstack.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Request-Width,    Access-Control-Request-Method,  Access-Control-Request-Headers,    authorization,   Content-Type");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,   Access-Control-Allow-Credentials,   authorization");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else if(request.getRequestURI().equals("/login"))
        {
            filterChain.doFilter(request,response);
            return;
        }
        else {
            String jwt=request.getHeader(SecurityParams.JWT_HEADER_NAME);
            System.out.println("Request Authorization = "+jwt);

            if(jwt == null || !jwt.startsWith(SecurityParams.TOKEN_PREFIX))
            {
                filterChain.doFilter(request,response);
                return;
            }

            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
            String token = jwt.substring(SecurityParams.TOKEN_PREFIX.length());
            DecodedJWT decodedJWT= verifier.verify(token);
            String username= decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);

            Collection<GrantedAuthority> authorities=new ArrayList<>();
            roles.forEach(role->{
                authorities.add(new SimpleGrantedAuthority(role));
            });
            //our user Spring => authenticate this user in spring security context
            UsernamePasswordAuthenticationToken userToken=
                    new UsernamePasswordAuthenticationToken(username,null,authorities);

            SecurityContextHolder.getContext().setAuthentication(userToken);
            filterChain.doFilter(request,response);

        }



    }
}
