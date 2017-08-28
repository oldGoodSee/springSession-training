package com.bocom.controller.view;

import com.bocom.dao.SysUserDao;
import com.bocom.domain.SysUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {


    // log
    private static Logger logger = LoggerFactory
            .getLogger(LoginController.class);


    @Resource
    private SysUserDao sysUserDao;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    @RequestMapping(method = RequestMethod.GET)
    public String homePage() {

        SysUserInfo userInfo = (SysUserInfo) request.getSession().getAttribute("sysUser");
        if (null == userInfo) {
            return "home/login";
        } else {
            return "home/index";
        }
    }

    /**
     * 结合sso自动登录访问地址
     */
    @RequestMapping(value = "/manager/loginAction/loginCas")
    public String loginCas(ModelMap model, HttpSession session) {
        return "home/homepage";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public String login() {
        try {
            String userName = request.getParameter("userName");
            String pwd = request.getParameter("password");
            SysUserInfo sysUserInfo = new SysUserInfo();
            sysUserInfo.setUserName(userName);
            sysUserInfo.setPassword(pwd);
            SysUserInfo userInfo = sysUserDao.getUserByLogin(sysUserInfo);
            if (null == userInfo) {
                return "home/login";
            } else {
                request.getSession().setAttribute("sysUser", userInfo);
                return "home/index";
            }
        } catch (Exception e) {
            logger.error("login error ", e);
            return "home/login";
        }
    }
}
