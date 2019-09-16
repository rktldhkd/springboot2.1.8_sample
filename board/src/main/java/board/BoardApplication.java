package board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*
 * @SpringBootApplication에는 스프링 부트의 3개의 애노테이션으로 구성된다.
 * 1.@EnableAutoConfiguration : 스프링의 다양한 설정이 자동으로 완료되게하는 어노테이션.
 * 
 * 2.@componentScan : 컴포넌트 자동 검색 및 구성 기능 제공. 여러가지 컴포넌트 클래스를 검색하고, 
 * 검색된 컴포넌트 및 빈 클래스를 스프링 어플리케이션 컨텍스트에 등록하는 역할을 한다.(컨트롤러같은 자원 자동 탐색)
 * 
 * 3.@Configuration : 자바기반설정파일 기능을 제공. 엄밀히 말하면 이 어노테이션을 직접 포함하진 않음. 
 * 하지만 포함되는 @SpringBootConfiguration이 @Configuration 어노테이션을 포함하고 있다. 
 * @Configuration 이 붙은 클래스가설정 파일임을 스프링에 알려준다.
 */
// exclude 속성 : 다음에 설정한 기능들은 자동 구성에서 제외한다. 파일업로드 기능에서 multipart 처리할 bean을 WebMvcConfiguration.java에서 
//따로 설정하였기에 아래와 같이 설정해준다.

//@EnableJpaAuditing, @EntityScan : 스프링데이터JPA 설정.
//@EntityScan : basePackages 속성에 설정한 패키지의 하위에서 JPA 엔티티(@Entity)를 찾는다.
//Jsr310JpaConverters.class = 자바8의 새로운 날짜 관련 클래스. mysql버전에 따라 이 클래스가 문제가 생기는 경우가 있는데, 이를 방지하기 위함.

@EnableJpaAuditing
@EntityScan(
		basePackageClasses = {Jsr310JpaConverters.class},
		basePackages = {"board"}
)
@SpringBootApplication(exclude= {MultipartAutoConfiguration.class}) //스프링 부트의 핵심 애노테이션이라 할 수 있다.
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
