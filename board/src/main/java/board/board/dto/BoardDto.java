package board.board.dto;

import java.util.List;

import lombok.Data;

//롬북의 Data 애노테이션을 설정하여, 클래스 자원들의 getter/setter, toString(), equals(), hashCode() 등의
//메소드를 자동 생성 시켜준다. 클래스에서는 변수만 선언하면 된다.
//단, setter의 경우 final이 붙지 않은 필드에만 적용.
@Data
public class BoardDto {
	private int boardIdx;
	private String title;
	private String contents;
	private int hitCnt;
	private String creatorId;
	private String createdDatetime;
	private String updaterId;
	private String updatedDatetime;
	private List<BoardFileDto> fileList;
}//class
