package board.board.entity;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * @NoArgsConstructor : 기본 생성자 자동 추가
		ㄴ예시)@NoArgsConstructor(access = AccessLevel.PROTECTED)
		ㄴaccess = AccessLevel.PROTECTED : 기본생성자의 접근 권한을 protected로 제한
		ㄴ생성자로 protected Posts() {}와 같은 효과
		ㄴEntity 클래스를 프로젝트 코드상에서 기본생성자로 생성하는 것은 막되, JPA에서 Entity 클래스를 생성하는것은 허용하기 위해 추가
 */

@Entity //jpa 엔티티
@Table(name="t_jpa_board") //해당 테이블과 매핑
@NoArgsConstructor
@Data //lombok
public class BoardEntity {
	@Id //기본키(PK)임을 알림.
	@GeneratedValue(strategy = GenerationType.AUTO) //기본키 생성 전략. AUTO시, Mysql은 auto_increment / oracle은 기본키에 사용할 시퀀스를 생성한다.
	private int boardIdx;
	
	//@Column 으로 컬럼의 속성 지정/초기값 세팅 등이 가능하다.
	@Column(nullable = false) //컬럼 속성 지정. not null.
	private String title;
	
	@Column(nullable = false)
	private String contents;
	
	@Column(nullable = false)
	private int hitCnt=0;
	
	@Column(nullable = false)
	private String creatorId;
	
	@Column(nullable = false)
	private LocalDateTime createdDatetime = LocalDateTime.now(); //현재시간
	
	private String updaterId;
	
	private LocalDateTime updatedDateTime;
	
	//연관관계의 주인만이 외래 키를 관리(등록, 수정, 삭제) 할 수 있고, 반면 주인이 아닌 엔티티는 읽기만 할 수 있습니다.
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //1:N관계. 게시글1:파일N
	@JoinColumn(name="board_idx") //자동으로 boardEntity테이블의 PK
	private Collection<BoardFileEntity> fileList;
}//class
