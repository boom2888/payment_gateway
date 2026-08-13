package com.games.framework.security.filter;

import com.games.common.core.domain.model.LoginUser;
import com.games.common.utils.SecurityUtils;
import com.games.common.utils.StringUtils;
import com.games.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token过滤器 验证token有效性
 *
 * @author lor
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        if(
                StringUtils.equalsAny(request.getRequestURI(), "/login") ||
                        StringUtils.endsWith(request.getRequestURI(), "/registerShopUser") ||
                        StringUtils.startsWith(request.getRequestURI(), "/common/") ||
                        StringUtils.startsWith(request.getRequestURI(), "/test/") ||
                        StringUtils.endsWith(request.getRequestURI(), "/webhook") ||
                        StringUtils.endsWith(request.getRequestURI(), "/callbackStripe") ||
                        StringUtils.endsWith(request.getRequestURI(), "/thunes/callback")
        ){
            chain.doFilter(request, response);
        }else{
            LoginUser loginUser = tokenService.getLoginUser(request);
            if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication()))
            {
                tokenService.verifyToken(loginUser);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            chain.doFilter(request, response);
        }
    }
}
