package board.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author willow
 * DB 설정을 위한 자바 설정 클래스.
 * database에 접근하기 위한 설정 파일. DataSource객체 생성.
 * 히카리(커넥션 풀 라이브러리)를 사용한 커넥션 풀
 * 트랜잭션 설정.
 */
@Configuration
//yml설정파일 사용하므로 properties 사용하기위해선 주석 삭제. @PropertySource("classpath:/application.properties")
@EnableTransactionManagement //트랜잭션 설정. 애노테이션 선언 후, 클래스 안에서 메소드로 transaction 객체를 불러오는 메소드를 @Bean설정 하면 되겠따.
public class DatabaseConfiguration {
	@Autowired
	private ApplicationContext applicationContext; //마이바티스 설정 위함.
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}//hikariConfig() application.properties의 값을 사용하여 hikariConfig 객체 설정/생성
	
	@Bean
	public DataSource dataSource() throws Exception {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		System.out.println(dataSource.toString());
		return dataSource;
	}//datasource
	
	//JPA 설정(스프링데이터JPA)
	@Bean
	@ConfigurationProperties(prefix = "spring.jpa")
	public Properties hibernateConfig() {
		return new Properties();
	}//hibernateConfig
	
	//마이바티스 설정
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/sql-*.xml"));
		sqlSessionFactoryBean.setConfiguration(mybatisConfig());//카멜,스네이크표기법 호환
		
		return sqlSessionFactoryBean.getObject();
	}//sqlSessionFactory()
	
	//SqlSessionFactory 클래스를 Bean으로 설정했기 때문에, 생성할 때 자동으로 Bean을 던져주는 것 같다.
	//template의 파라미터 SqlSessionFactory는 위의 함수에서 정의한 작업 다 하고 bean으로 던져짐?
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}//sqlSessionTemplate()
	
	//DTO객체와 DB필드명 간의 카멜,스네이크 표기법 호환.
	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration") //application.properties 파일에서 해당 설정 불러옴
	public org.apache.ibatis.session.Configuration mybatisConfig(){
		return new org.apache.ibatis.session.Configuration(); //불러온 설정을 자바클래스로 만들어서 반환
	}//mybatisConfig()
	
	//트랜잭션 처리를 위한 메소드
	@Bean
	public PlatformTransactionManager transactionManager() throws Exception {
		return new DataSourceTransactionManager(dataSource());
	}//transactionManager
}//class