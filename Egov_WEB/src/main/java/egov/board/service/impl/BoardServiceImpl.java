package egov.board.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egov.board.dao.BoardMapper;
import egov.board.service.BoardService;

@Service("BoardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService{

	@Resource(name="BoardMapper")
	BoardMapper boardMapper;

	@Override
	public void checkUser(HttpServletRequest request) throws Exception {
		if(request.getSession().getAttribute("myid")==null)
		{
			throw new Exception("로그인안했음");
		}
	}
	
	@Override
	public void saveBoard(HttpServletRequest request) throws Exception {
		if(request.getSession().getAttribute("myid")==null)
		{
			throw new Exception("로그인안했음");
		}
		//사용자 요청을 데이터베이스로 전달
		String title = request.getParameter("title");
		String content = request.getParameter("mytextarea");
		if(title.length()>25)
		{
			throw new Exception("제목을 다시 확인해주세요.");
		}
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("in_title", title);
		paramMap.put("in_content", content);
		paramMap.put("in_userid", request.getSession().getAttribute("myid"));
		paramMap.put("out_state",0);
		boardMapper.saveBoard(paramMap);
	}


}
