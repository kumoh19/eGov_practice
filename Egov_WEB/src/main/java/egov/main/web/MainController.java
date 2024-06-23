package egov.main.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lib.model.UserVO;

import egov.main.service.MainService;

@Controller
public class MainController {
	
	@Resource(name="MainService")
	MainService mainService;
	
	@RequestMapping(value="/main.do")
	public String main(HttpServletRequest request, ModelMap model)
	{
		return "main/main";
	}
	
	@RequestMapping(value="/main2.do")
	public String main2(HttpServletRequest request, ModelMap model)
	{
		return "main/main2";
	}
	
	@RequestMapping(value="/main3.do")
	public String main3(@RequestParam("pw")String pw, HttpServletRequest request, ModelMap model)
	{
		String id = request.getParameter("id");
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		
		if(id.equals("qwer"))
		{
			model.addAttribute("userid", pw);
		}
		else 
		{
			model.addAttribute("userid", pw);
		}
		
		userNo = userNo + 5;
		model.addAttribute("userNo", userNo);
		return "main/main3";
	}
	
	@RequestMapping(value="/main4/{userNo}.do")
	public String main4(@PathVariable String userNo, HttpServletRequest request, ModelMap model)
	{
		model.addAttribute("userNo", userNo);
		return "main/main3";
	}
	
	@RequestMapping(value="/main5.do")
	public String main5(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			resultMap= mainService.selectMain(request);
			model.addAttribute("serverId", resultMap.get("userid").toString());
		} catch (Exception e) {
			//로그기록,상태코드반환 또는 에러페이지 전달
			return "error/error";
		}
		return "main/main";
	}
	
	@RequestMapping(value="/login.do")
	public String login(HttpServletRequest request,ModelMap model)
	{
		
		return "login/login";
	}
	
	@RequestMapping(value="/loginSubmission.do")
	public String loginSubmission(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			resultMap= mainService.selectLogin(request);
			request.getSession().setAttribute("uservo",resultMap.get("uservo"));
			model.addAttribute("serverId", ((UserVO)resultMap.get("uservo")).getUserid());
		} catch (Exception e) {
			
			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("resultError_idnotFound"))
			{
				return "redirect:/login.do";
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		return "main/main";
	}
	
	@RequestMapping(value="/logout.do")
	public String logout(HttpServletRequest request,ModelMap model)
	{
		System.out.println(request.getSession().getAttribute("myid").toString());
		request.getSession().invalidate();
		System.out.println(",");
		
		return "login/login";
	}
	
	@RequestMapping(value="/testjson.do")
	public void testjson(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			request.setCharacterEncoding("UTF-8");
		
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Name", "abc123");
			jsonObject.put("Text", "물고기");
			
			JSONObject jsonObject2 = new JSONObject();
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("column1", "100");
			resultMap.put("column2", 101);
			resultMap.put("column3", 102);
			
			jsonObject2.putAll(resultMap); //resultMap이 jsonObject2에 json형태(키,값)으로 저장
			
			JSONObject jsonObject3 = new JSONObject();
			jsonObject3.put("Name", "abc123");
			jsonObject3.put("Text", "물고기");
			
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(jsonObject2);
			jsonArray.add(jsonObject3);
			
			jsonObject.put("mylist", jsonArray);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");

			response.getWriter().print(jsonObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//로그 기록하거나 http상태코드를 반환해주면 된다.
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/showtestpage.do")
	public String showtestpage(HttpServletRequest request,ModelMap model)
	{
		return "common/test";
	}
}
