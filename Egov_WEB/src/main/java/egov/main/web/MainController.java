package egov.main.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	@RequestMapping(value="main.do")
	public String main(HttpServletRequest request, ModelMap model)
	{
		return "main/main";
	}
}
