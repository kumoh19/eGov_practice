package egov.main.web;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lib.model.UserVO;

import egov.main.service.MainService;

@Controller
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
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
			
			String userid = ((UserVO)resultMap.get("uservo")).getUserid();
			request.getSession().setAttribute("uservo",resultMap.get("uservo"));
			model.addAttribute("serverId", userid);
			
			logger.info("=====================ST접속정보기록=====================");
			logger.info("유저아이디:"+userid);
			logger.info("=====================ED접속정보기록=====================");
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
	
	@RequestMapping(value="/testxml.do")
	public void testxml(HttpServletRequest request,HttpServletResponse response)
	{
		try {
			Document doc = new Document();
			Element root = new Element("MYAnimal");
			root.setAttribute("myName2", "abc2");
			
			Element item1 = new Element("animal1");
			item1.setText("코끼리");
			Element item2 = new Element("animal2");
			item2.setText("토끼");
			Element item3 = new Element("animal3");
			item3.setText("고양이");
			Element item4 = new Element("animal4");
			Element item4_sub = new Element("animal5");
			item4_sub.setText("물개");
			
			root.addContent(item1);
			root.addContent(item2);
			root.addContent(item3);
			root.addContent(item4_sub);
			root.addContent(item4);
			
			doc.addContent(root);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
		
			response.getWriter().print(new XMLOutputter().outputString(doc));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
