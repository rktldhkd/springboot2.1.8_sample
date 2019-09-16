package board.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;

@Mapper
public interface BoardMapper {
	List<BoardDto> selectBoardList() throws Exception;
	void insertBoard(BoardDto dto) throws Exception;
	void updateHitCnt(int boardIdx) throws Exception;
	BoardDto selectBoardDetail(int boardIdx) throws Exception;
	void updateBoard(BoardDto dto) throws Exception;
	void deleteBoard(int boardIdx) throws Exception;
	void insertBoardFileList(List<BoardFileDto> list) throws Exception;
	List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;
	
	//@Param 사용하여 map에 저장하거나, dto 사용하지 않아도 여러 개의 파라미터를 전달 가능. xml단에서 map으로 받는다.
	BoardFileDto selectBoardFileInformation(@Param("idx")int idx, 
															   @Param("boardIdx")int boardIdx);
}//interface
