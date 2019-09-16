package board.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.mapper.BoardMapper;
import board.board.util.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j //log 객체 자동 생성
@Service
public class BoardServiceImpl implements BoardService{
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private FileUtils fileUtils;

	@Override
	public List<BoardDto> selectBoardList() throws Exception {
		return boardMapper.selectBoardList();
	}//selectBoardList()

	@Override
	public BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception {
		return boardMapper.selectBoardFileInformation(idx, boardIdx);
	}//selectBoardFileInformation
	
	@Override
	public void insertBoard(BoardDto dto, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		//insert후, 마이바티스의 useGeneratedKeys, keyProperty속성에 의해 
		//레코드가 추가되어 증가한 id 값이 dto에 자동 세팅된다. getter 메소드 호출 시, 증가된 id값이 호출됨.
		boardMapper.insertBoard(dto); 
		
		List<BoardFileDto> list = fileUtils.parseFileInfo(dto.getBoardIdx(), multipartHttpServletRequest);
		if(CollectionUtils.isEmpty(list) == false) { //처리된 파일이 존재.
			boardMapper.insertBoardFileList(list);
		}//end if
		
		/*
		//업로드되 파일의 정보 확인하는 코드.
		//파일첨부가 되었다면,
		if(ObjectUtils.isEmpty(multipartHttpServletRequest)==false) {
			Iterator<String> it = multipartHttpServletRequest.getFileNames();
			String name;
			
			while(it.hasNext()) {
				name = it.next();
				log.debug("file tag name : " + name);
				//해당번째 파일의 이름/용량등의 정보를 담는 list 객체.
				List<MultipartFile> list = multipartHttpServletRequest.getFiles(name);
				for(MultipartFile mf : list) {
					log.debug("llllllllllll start file info lllllllllllll");
					log.debug("file name : " + mf.getOriginalFilename());
					log.debug("file size : " + mf.getSize() + " byte");
					log.debug("file content type : " + mf.getContentType()); //파일형식, jpg같은 이미지인가, txt같은 파일인가
					log.debug("llllllllllll end file info lllllllllllll\n");
				}//end for
			}//end while
		}//end if - 파일업로드 테스트
		*/
	}//insertBoard

	@Override
	@Transactional //트랜잭션 처리
	public BoardDto selectBoardDetail(int boardIdx) throws Exception {
		BoardDto dto = boardMapper.selectBoardDetail(boardIdx); //게시글 상세 가져오기
		
		List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
		dto.setFileList(fileList);
		
		boardMapper.updateHitCnt(boardIdx); //조회수 갱신
		
		return dto;
	}//selectBoardDetail

	@Override
	public void updateBoard(BoardDto dto) throws Exception {
		boardMapper.updateBoard(dto);
	}//updateBoard

	@Override
	public void deleteBoard(int boardIdx) throws Exception {
		boardMapper.deleteBoard(boardIdx);
	}//deleteBoard
	
}//class
