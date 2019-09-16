package board.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

/**
 * @author willow
 * 예외처리위한 자바 설정 클래스.(에러 처리하는 컨트롤러 클래스)
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandler {
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public ModelAndView defaultExceptionHandler(HttpServletRequest req, Exception exception) {
		ModelAndView mv = new ModelAndView("error/error_default"); //view 파일 경로 지정.
		mv.addObject("exception", exception);
		log.error("exception : [ " + exception + " ]");
		return mv;
	}//defaultExceptionHandler
}//class
