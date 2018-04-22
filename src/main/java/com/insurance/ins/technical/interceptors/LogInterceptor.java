package com.insurance.ins.technical.interceptors;

import com.insurance.ins.technical.entites.LogDetails;
import com.insurance.ins.technical.entites.User;
import com.insurance.ins.technical.repositories.LogDetailsRepository;
import com.insurance.ins.utils.annotations.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by K on 12.3.2018 г..
 */
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private final LogDetailsRepository logDetailsRepository;

    public LogInterceptor(LogDetailsRepository logDetailsRepository) {
        this.logDetailsRepository = logDetailsRepository;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        if(!(handler instanceof HandlerMethod)){
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof java.lang.String){
            username = principal.toString();
        }else
        {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            username = user.getUsername();
        }
        if(handlerMethod.getMethod()
                .isAnnotationPresent(Log.class)) {
            Date date = new Date();
            LogDetails logDetails = new LogDetails();
            logDetails.setTime(date);
            String[] splitURI = request.getRequestURI().split("/");
            if(splitURI.length==2){
                logDetails.setOperation("Batch Run");
                logDetails.setModifiedTable(splitURI[1]);
            }else{
                logDetails.setOperation(Character.toUpperCase(splitURI[2].charAt(0)) + splitURI[2].substring(1));
                logDetails.setModifiedTable(Character.toUpperCase(splitURI[1].charAt(0)) + splitURI[1].substring(1,splitURI[1].length()-1));
            }
            logDetails.setName(username);
            this.logDetailsRepository.save(logDetails);
        }

    }
}
