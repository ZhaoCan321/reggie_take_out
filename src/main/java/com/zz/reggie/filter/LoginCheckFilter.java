package com.zz.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.zz.reggie.common.BaseContext;
import com.zz.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
//    路径匹配，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      HttpServletResponse response = (HttpServletResponse) servletResponse;
      String requestURI = request.getRequestURI();
      String[] urls = new String[]{
              "/employee/login",
              "/employee/logout",
              "/backend/**",
              "/front/**",
      };
//      boolean check = check(urls, requestURI);
      boolean check = true;
      log.info("check{}", check);
      if(check) {
          filterChain.doFilter(request,response);
          return;
      }
      log.info("session{}", request.getSession().getAttribute("employee"));
      if(request.getSession().getAttribute("employee") != null) {

//          存储到 ThreadLocal 中
          Long empId = (Long) request.getSession().getAttribute("employee");
          BaseContext.setCurrentId(empId);

          filterChain.doFilter(request,response);
          return;
      }

      response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    /**
     * 检查请求是否放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
