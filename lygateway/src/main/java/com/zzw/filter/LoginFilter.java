package com.zzw.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zzw.config.FilterProperties;
import com.zzw.config.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties({FilterProperties.class, JwtProperties.class})
public class LoginFilter extends ZuulFilter {
    @Resource
    private JwtProperties prop;
    @Resource
    private FilterProperties filterProperties;
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String path = request.getRequestURI();
        return !isAllowPath(path);
    }
    private boolean isAllowPath(String path) {
        for (String allowPath:filterProperties.getAllowPath()
             ) {
            if(path.startsWith(allowPath)){
                return  true;
            }
        }
        return false;
    }


    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = CookieUtils.getCookieValue(request,prop.getCookieName());
        try {
            JwtUtils.getInfoFromToken(token, prop.getPublicKey());
        } catch (Exception e) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
        }
        return null;
    }
}
