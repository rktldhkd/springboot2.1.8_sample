package board.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.service.BoardService;

@Controller
public class BoardController {
	@SuppressWarnings("unused")
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BoardService boardService;
	
	//////////////////////////// view page mapping ////////////////////////////
	/*
	 * view 경로 지정할때, 맨앞에는 '/'를 붙이지 않는다. ex)  "board/boardList"
	 * */
	
	//@GetMapping, @PostMapping 으로도 매핑 가능
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:/board/openBoardList";
	}//index
	
	@RequestMapping(value = "/board/openBoardList")
	public ModelAndView openBoardList() throws Exception{
		ModelAndView mv = new ModelAndView("board/boardList"); //template파일 위치 board 앞에 /는 안붙여도 된다.
		List<BoardDto> list = boardService.selectBoardList();//몇 개의 boardDTO 객체들을 갖는 list

//		Iterator it = list.iterator();
//		String result = "";
//		while(it.hasNext()) {
//			System.out.println(it.next());
//		}//while
		
		mv.addObject("list", list);
		
		return mv;
	}//openBoardList()

	@RequestMapping(value = "/board/openBoardWrite")
	public String  openBoardWrite() throws Exception{
		return "board/boardWrite";
	}//openBoardWrite
	
	//@RequestParam 으로 파라미터 받을 때, 스프링에서의 변수명과 URL의 쿼리스트링의 변수명이 같아야한다
	//@RequestParam으로 URL의 쿼리스트링의 변수를 가져온다.
	//form으로 넘긴 값은 그냥 "자료형 변수명" 사용해도 자동설정 되는 듯. @RequestParam 사용 X.
	@RequestMapping(value = "/board/openBoardDetail")
	public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
		ModelAndView mv = new ModelAndView("board/boardDetail");
		
		BoardDto dto = boardService.selectBoardDetail(boardIdx);
		mv.addObject("board", dto);
		
		return mv;
	}//openBoardDetail()
	
	//////////////////////////// CRUD ////////////////////////////
	
	//스프링부트는 form으로 보낸 값들을 dto 객체에서 자동으로 받을수있게 설정되어있다.
	@RequestMapping(value = "/board/insertBoard")
	public String insertBoard(BoardDto dto, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		boardService.insertBoard(dto, multipartHttpServletRequest);
		return "redirect:/board/openBoardList";
	}//insertBoard
	
	//@RequestParam으로 URL의 쿼리스트링의 변수를 가져온다.
	//form으로 넘긴 값은 그냥 "자료형 변수명" 사용해도 자동설정 되는 듯. @RequestParam 사용 X.
	@RequestMapping(value = "/board/updateBoard")
	public String updateBoard(BoardDto dto) throws Exception{
		int id = dto.getBoardIdx();
		String path = "redirect:/board/openBoardDetail?boardIdx=" + id;
		
		boardService.updateBoard(dto);
		
		//return "redirect:/board/openBoardList";
		return path;
	}//updateBoard
	
	//form으로 넘긴 값은 그냥 "자료형 변수명" 사용해도 자동설정 되는 듯. @RequestParam 사용 X.
	//input hidden의 값도 그냥 "자료형 변수명" 의 plain형식으로 받는다.
	@RequestMapping(value = "/board/deleteBoard")
	public String deleteBoard(int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board/openBoardList";
	}//deleteBoard
	
	@RequestMapping(value = "/board/downloadBoardFile")
	public void downloadBoardFile(@RequestParam int idx, @RequestParam  int boardIdx, HttpServletResponse res) throws Exception {
		BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);
		
		if(ObjectUtils.isEmpty(boardFile) == false) {
			String fileName = boardFile.getOriginalFileName();
			
			//org.apache.common.io.FileUtils
			//실제경로에서 건너온 파일들이 byte[] 형태로 변환된다.
			byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
			
			res.setContentType("application/octet-stream");
			res.setContentLength(files.length);
			res.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");
			res.setHeader("Content-Transfer-Encoding", "binary");
			
			res.getOutputStream().write(files); //파일정보의 바이트배열 데이터를 response에 작성
			res.getOutputStream().flush();//버퍼정리
			res.getOutputStream().close();//닫아줌
		}//end if
	}//downloadBoardFile
}//class
