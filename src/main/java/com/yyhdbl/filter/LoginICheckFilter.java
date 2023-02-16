package com.yyhdbl.filter;

import com.alibaba.fastjson.JSON;
import com.yyhdbl.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginICheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的url
        String requestURL = request.getRequestURI();
        log.info("拦截到请求：{}", requestURL);
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };

//判断本次请求是否需要处理
        boolean check = check(urls, requestURL);

        //不需要处理 直接放行
        if (check) {
            log.info("不需要处理的请求：{}", requestURL);
            filterChain.doFilter(request, response); //放行
            return;
        }

        //判断登录状态，如果已经登录 直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录,用户id为：{}",request.getSession().getAttribute("employee") );
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        //如果未登录则返回未登录结果 给前端 让前端的页面跳转道登录页面
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));// 通过输出流的形式向前端传入数据
    }

    /**
     * 路径匹配，检查本次请求是否需要放行 就是判断获取到的url是否在不需要拦截的url数组中
     *
     * @param urls       放行的url数组
     * @param requestUrl 请求的路径
     * @return
     */
    public boolean check(String[] urls, String requestUrl) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if (match) {
                return true;
            }
        }
        return false;
    }


}
