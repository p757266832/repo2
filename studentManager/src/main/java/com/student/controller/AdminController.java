package com.student.controller;

        import com.student.bean.Student;
        import com.student.bean.User;
        import com.student.service.StudentService;
        import com.student.service.UserService;
        import com.student.util.CpachaUtil;
        import org.apache.commons.lang3.StringUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.ModelAndView;

        import javax.imageio.ImageIO;
        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.awt.image.BufferedImage;
        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;
@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    //主页面
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
    //登录页面
    @RequestMapping(value={"/login"},method = RequestMethod.GET)
    public ModelAndView login(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/login");
        return modelAndView;
    }
    /*
    * 用户注销
    * */
    @RequestMapping(value="/exitUser",method = RequestMethod.GET)
    public String exitUser(HttpServletRequest request) {
        //把登录信息置空
        request.getSession().setAttribute("admin",null);
        return  "redirect:login";
    }
    //登录表单提交
    @ResponseBody
    @RequestMapping(value="/loginUser",method = RequestMethod.POST)
    public Map<String,String> login(@RequestParam(value="username") String username, @RequestParam(value="password") String password,@RequestParam(value = "vcode") String vcode,@RequestParam("type") int type, HttpServletRequest request) {
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isEmpty(username)) {
           map.put("type","error");
           map.put("msg","用户名不能为空");
           return map;
        }
        if(StringUtils.isEmpty(password)) {
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        if(StringUtils.isEmpty(vcode)) {
            map.put("type","error");
            map.put("msg","验证码不能为空");
            return  map;
        }
        //取出存放到session中的验证码
        String preCode = (String) request.getSession().getAttribute("code");
        if(StringUtils.isEmpty(preCode)) {
            map.put("type","error");
            map.put("msg","长时间不操作验证码失效");
            return  map;
        }
        //将当前输入的验证码与session中的验证码比较
        if(!vcode.equalsIgnoreCase(preCode)) {
            map.put("type","error");
            map.put("msg","验证码错误,请重新输入");
            return  map;
        }
        //验证码正确时,清空session中的验证码
        request.getSession().setAttribute("code",null);
        if(type==1) {
            System.out.println("进入管理员页面");
            //当验证码成功时,调用数据库查询,管理员信息
            User user = userService.findByAdminName(username);
            if(user ==null) {
                map.put("type","error");
                map.put("msg","不存在该用户");
                return  map;
            }
            //判断密码是否正确
            if(!password.equals(user.getPassword())) {
                map.put("type","error");
                map.put("msg","对不起密码错误");
                return  map;
            }
            //密码正确时,将信息放入session中
            request.getSession().setAttribute("admin", user);
        } else  if(type==2) {
            System.out.println("进入学生页面");
            Student loginStudent = studentService.findByUsername(username);
            if(loginStudent == null) {
                map.put("type","error");
                map.put("msg","用户名不存在");
                return  map;
            }
            if(!password.equals(loginStudent.getPassword())) {
                map.put("type","error");
                map.put("msg","对不起密码错误");
                return  map;
            }
            request.getSession().setAttribute("admin",loginStudent);
        }
        request.getSession().setAttribute("userType",type);
        map.put("type","success");
        map.put("msg","登录成功");
        return map;
    }
    //获取验证码
    @RequestMapping(value="/getCpacha",method = RequestMethod.GET)
    public void getCpacha(HttpServletRequest request,
                          @RequestParam(value = "vl",defaultValue = "4",required = false) int vl,
                          @RequestParam(value = "width",defaultValue = "88",required = false) int width,
                          @RequestParam(value = "height",defaultValue = "33",required = false) int height,
                          HttpServletResponse response) {
        //创建工具类对象
        CpachaUtil cpachaUtil = new CpachaUtil(vl,width,height);
        //生成验证码
        String vCode = cpachaUtil.generatorVCode();
        request.getSession().setAttribute("code",vCode);
        //生成验证码图片
        BufferedImage bufferedImage = cpachaUtil.generatorVCodeImage(vCode, true);
        try {
            //将图片以jpg的形式输出
            ImageIO.write(bufferedImage,"jpg",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
