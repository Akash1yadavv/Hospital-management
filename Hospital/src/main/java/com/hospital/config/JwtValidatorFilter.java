package com.hospital.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.hospital.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JwtValidatorFilter extends OncePerRequestFilter {
    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */

    @Autowired private JwtUtil jwtUtil;
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtToken(request);
//            String[] jwtArr = {};
//            if(jwt != null)
//            	jwtArr = jwt.split("\\.");
//            && jwtArr.length == 3
//            StringUtils.hasText(jwt) && 
            if (jwt != null ) {
                Claims claims = jwtUtil.decodeToken(jwt);
                String authorities= (String)claims.get("authorities");
                String userId= (String)(claims.get("userId"));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("user:{},method:{},path:{},", authentication.getName(),request.getMethod(),request.getRequestURI());
            }else if(jwt == null) {
            	log.info("user:{},method:{},path:{},", "anonymousUser",request.getMethod(),request.getRequestURI());
            }
            filterChain.doFilter(request, response);

        } catch (AccessDeniedException | JwtException e) {
        	log.info("user:{},method:{},path:{},", "anonymousUserWithWrongToken",request.getMethod(),request.getRequestURI());
            handlerExceptionResolver.resolveException(request, response, null, e);


        }
    }


    private String getJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);

            return accessToken;
        }
        return null;
    }
}
