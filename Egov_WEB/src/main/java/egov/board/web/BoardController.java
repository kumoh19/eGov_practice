package egov.board.web;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egov.board.service.BoardService;

@Controller
public class BoardController {
	
	@Resource(name="BoardService")BoardService boardService;
	//jsp파일을 보여주는 곳
	
	@RequestMapping(value="/boardWrite.do")
	public String boardWrite(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			boardService.checkUser(request);
		} catch (Exception e) {
			
			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		
		return "board/boardwrite";
	}
	
	//jsp파일에서 제출을 누를 시 게시판 글저장을 처리할 곳
	@RequestMapping(value="/boardInsert.do")
	public String boardInsert(HttpServletRequest request,ModelMap model)
	{
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		try {
			boardService.saveBoard(request);
		} catch (Exception e) {
			
			//로그기록,상태코드반환 또는 에러페이지 전달
			String error = e.getMessage();
			if(error.equals("로그인안했음"))
			{
				return "redirect:/login.do";
			}
			else if(error.equals("제목을 다시 확인해주세요."))
			{
				return "redirect:/boardWrite.do"; //기존 입력하던 데이터 보존(jsp로 날리면 입력중인 데이터날라감)
			}
			else
			{
				//일반예외페이지
			}
			
			return "error/error";
		}
		
		return "board/boardwrite";
	}
}
