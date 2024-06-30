package egov.board.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lib.model.UserVO;
import com.lib.pagination.PaginationInfo;
import com.lib.util.Validation_Form;

import egov.board.dao.BoardMapper;
import egov.board.service.BoardService;

@Service("BoardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService{

	@Resource(name="BoardMapper")
	BoardMapper boardMapper;
	
	@Resource(name="fileUploadProperty")
	Properties properties;

	@Override
	public void checkUser(HttpServletRequest request) throws Exception {
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		
	}
	
	@Override
	public void saveBoard(HttpServletRequest request) throws Exception {
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		
		//사용자요청을 데이터베이스로 전달
		String title= request.getParameter("title");
		String content =request.getParameter("mytextarea");
		if(title.length()>25)
		{
			throw new Exception("제목을 다시 확인해주세요.");
		}
		
		HashMap<String,Object> paramMap= new HashMap<String,Object>();
		paramMap.put("in_title", title);
		paramMap.put("in_content", content);
		paramMap.put("in_userid", ((UserVO)request.getSession().getAttribute("uservo")).getUserid());
		paramMap.put("out_state", 0);
		boardMapper.saveBoard(paramMap);
		
		String uploadPath = properties.getProperty("file.ImgPath"); //키를 입력하면 값에 해당하는 경로 저장
		String convertuid=""; //업로드될 파일 이름
		String originalEx = "";
		String filePath = "";
		
		if(request instanceof MultipartHttpServletRequest) //요청이 MultipartHttpServletRequest(파일업로드 지원)에 해당되는지 체크
		{
			final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			final Map<String, MultipartFile> files = multiRequest.getFileMap();
			
			File saveFolder = new File(uploadPath); //c드라이브 밑에 해당 폴더가 없으면 생성, 경로를 가지는 객체
			if(!saveFolder.exists()|| saveFolder.isFile())
			{
				saveFolder.mkdirs();
			}
			
			for(MultipartFile file:files.values())
			{
				if(!"".equals(file.getOriginalFilename())) //업로드가 요청된 파일의 이름 반환
				{
					int maxSize = 1 * 1024 * 1024;
					int fileSize = (int)file.getSize();
					if(fileSize>maxSize) 
					{
						throw new Exception("유효성검사실패");
					}

					Calendar cal = Calendar.getInstance();
					int year = cal.get ( Calendar.YEAR );
					int month = cal.get ( Calendar.MONTH ) + 1 ;
					int date = cal.get ( Calendar.DATE ) ;
					int hour = cal.get ( Calendar.HOUR_OF_DAY ) ;
					
					convertuid = UUID.randomUUID().toString().replace("-", "")+year+month+date+hour;
					originalEx = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1); //확장자얻음
					convertuid = convertuid + "." + originalEx;
					//서버의 파일경로(프로젝트 외부에 설정해줘야함)
					filePath = uploadPath + convertuid;
					file.transferTo(new File(filePath));//35:28
					
					HashMap<String,Object> paramMap2 = new HashMap<String,Object>();
					paramMap2.put("in_userid", ((UserVO)request.getSession().getAttribute("uservo")).getUserid());
					paramMap2.put("in_filename", convertuid);
					paramMap2.put("in_filetype", originalEx);
					paramMap2.put("in_fileurl", "http://localhost:8080/Egov_WEB/boardView/image.do?file="+convertuid); //실제로 배포한다면 SSL이 적용된 https와 도메인 이름으로 교체
					paramMap2.put("out_state", 0);
					boardMapper.saveFile(paramMap2);
				}
			}
		}
		
	}

	@Override
	public HashMap<String, Object> showBoard(HttpServletRequest request) throws Exception {

		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		//사용자요청을 데이터베이스로 전달
		String id=((UserVO)request.getSession().getAttribute("uservo")).getUserid();
		String brdid= request.getParameter("brdid");
		boolean validNumber = false;
		validNumber = Validation_Form.validNum(brdid);

		if(validNumber==false)
		{
			throw new Exception("유효성검사실패");
		}
		HashMap<String,Object> paramMap= new HashMap<String,Object>();
		paramMap.put("in_brdid", brdid);
		paramMap.put("out_state", 0);
		HashMap<String,Object> rusultMap= new HashMap<String,Object>();
		rusultMap=boardMapper.showBoard(paramMap);
		if(rusultMap==null)
		{
			throw new Exception("페이지찾을수없음");
		}
		rusultMap.put("loginid", id);
		
		return rusultMap;
	}

	@Override
	public ArrayList<HashMap<String, Object>> showBoardList(HttpServletRequest request) throws Exception {

		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		String pageNo=request.getParameter("pageNo");
		//url에 pageNo가 없거나 비어있을때
		if(pageNo==null||pageNo.equals(""))
		{
			pageNo="1";
		}
		else 
		{
			pageNo=request.getParameter("pageNo");
		}
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(Integer.parseInt(pageNo));
		paginationInfo.setPageSize(10); //하단에 보이는 페이지 번호
		paginationInfo.setRecordCountPerPage(10); //한 페이지당 보여질 게시글의 숫자
		
		HashMap<String,Object> paramMap= new HashMap<String,Object>();
		paramMap.put("pi_offset", (paginationInfo.getCurrentPageNo()-1)*paginationInfo.getRecordCountPerPage());
		paramMap.put("pi_recordCountPerPage", paginationInfo.getRecordCountPerPage());
		paramMap.put("out_listcount", 0); //전체 게시물 건수
		paramMap.put("out_state", 0);
		ArrayList<HashMap<String,Object>> list= new ArrayList<HashMap<String,Object>>();
		list=boardMapper.showBoardList(paramMap);
		int listCount = Integer.parseInt(paramMap.get("out_listcount").toString());
		paginationInfo.setTotalRecordCount(listCount);
		
		if(list==null)
		{
			throw new Exception("페이지찾을수없음");
		}
		
		HashMap<String,Object> resultMap= new HashMap<String,Object>();
		resultMap.put("paginationInfo", paginationInfo);
		resultMap.put("listCount", listCount);
		list.add(resultMap);
		
		return list;
	}
	
	//1. 원본 글의 번호가 숫자인지 유효성 체크 2. 로그인한 유저인지 체크해 주는 함수
	@Override
	public String checkReply(HttpServletRequest request) throws Exception { 
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		
		String boardid = request.getParameter("boardid"); 
		if(Validation_Form.validNum(boardid)==false)  
		{
			throw new Exception("유효성검사실패");
		}
		
		return boardid;
		
	}
	
	@Override
	public void saveReply(HttpServletRequest request) throws Exception {
		
		if(request.getSession().getAttribute("uservo")==null)
		{
			throw new Exception("로그인안했음");
		}
		
		//사용자요청을 데이터베이스로 전달
		String title= request.getParameter("title");
		String content =request.getParameter("mytextarea");
		String originalid = request.getParameter("originalid"); //게시판 번호
		if(Validation_Form.validNum(originalid)==false||content.length()>10000)  //숫자인지 체크해주는 유효성 검사 함수
		{
			throw new Exception("유효성검사실패");
		}
		
		if(title.length()>25)
		{
			throw new Exception("제목을 다시 확인해주세요.");
		}
		
		HashMap<String,Object> paramMap= new HashMap<String,Object>();
		paramMap.put("in_originalid", Integer.parseInt(originalid));
		paramMap.put("in_title", title);
		paramMap.put("in_content", content);
		paramMap.put("in_userid", ((UserVO)request.getSession().getAttribute("uservo")).getUserid());
		paramMap.put("out_state", 0);
		boardMapper.saveReply(paramMap);
		//프로시저에서 out_state에 1세팅 만일 1이 아니면 데이터베이스 작업이 정상적으로 처리되지 않은 것에 해당하기에 예외처리
		if(Integer.parseInt(paramMap.get("out_state").toString())!=1) //object->String->int
		{
			throw new Exception("DB작업실패");
		}
	}
}
