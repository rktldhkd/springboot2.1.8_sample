package board.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import board.interceptor.LoggerInterceptor;

/**
 * @author willow
 * 서버단, 사이트에 필요한 전반적인 기능들을 설정.
 * 인터셉터, 파일업로드/다운로드 등.
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
	
	//addPathPatterns() / excludePathPatterns() 를 사용하여
	//적용할 요청주소의 패턴과 제외할 요청 주소의 패턴을 선택적으로 설정 가능. 여기선 전 영역에서 인터셉터가 동작하게 할것이므로 사용 안함.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor());
	}//addInterceptors
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		commonsMultipartResolver.setMaxUploadSizePerFile(10 * 1024 * 1024); //byte 단위이다. 여기선 10Mb.
		return commonsMultipartResolver;
	}//multipartResolver()
}//class
