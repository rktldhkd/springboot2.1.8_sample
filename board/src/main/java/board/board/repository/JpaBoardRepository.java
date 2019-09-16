package board.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;

public interface JpaBoardRepository extends CrudRepository<BoardEntity, Integer>{
	List<BoardEntity> findAllByOrderByBoardIdxDesc();
	
	
	//서비스단에서 넘어온 변수명(@Param()) 과 repository의 메소드 파라미터와 매핑.
	// :변수명, ?숫자
	// ?숫자 : 조건절에 파라미터가 바인딩되는 순서. 메소드의 파라미터들의 순서대로 매핑
	// :변수명 : @Param이 있는 메소드의 파라미터와 대응.
	@Query(
				"SELECT file "
				+ "FROM BoardFileEntity file "
				+ "WHERE board_idx = :boardIdx AND idx = :idx"
			)
	BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);
}//interface
