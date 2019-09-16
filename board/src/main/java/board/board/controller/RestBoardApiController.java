package board.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import board.board.dto.BoardDto;
import board.board.service.BoardService;

// Rest API를 사용한 게시판 매핑. Restful -> Rest API(화면과 서버가 구분.)
// Restful : 하나의 주소에서 비즈니스로직 처리와 화면 호출 둘 다함. 웹개발시 사용.
// Rest API : 하나의 주소에서 비즈니스 로직만 처리. 데이터만 보내므로 웹개발시 화면 지정이 안되므로 사용x.

//RestController = @Controller + @ResponseBody
// @ResponseBody : http 패킷의 body에 데이터를 담아 전송. 이 때, 데이터는 JSON 형태.
// 값이 여러개면 JSON의 Array 형식으로 전달.
@RestController
public class RestBoardApiController {
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value="/api/board", method=RequestMethod.GET)
	public List<BoardDto> openBoardList() throws Exception{
		return boardService.selectBoardList();
	}
	
	@RequestMapping(value="/api/board/write", method=RequestMethod.POST)
	public void insertBoard(@RequestBody BoardDto board) throws Exception{
		boardService.insertBoard(board, null);
	}
	
	@RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.GET)
	public BoardDto openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{
		
		return boardService.selectBoardDetail(boardIdx);
	}
	
	@RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.PUT)
	public String updateBoard(@RequestBody BoardDto board) throws Exception{
		boardService.updateBoard(board);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.DELETE)
	public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
		boardService.deleteBoard(boardIdx);
		return "redirect:/board";
	}
}
