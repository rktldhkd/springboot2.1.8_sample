package board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//@Bean과 @Component는 같은 기능. : bean 생성.
//차이점은 @Bean은 이미 만들어진 기능을 bean으로 등록하는 것이고, @Component는 개발자가
//직접 작성한 class를 bean으로 등록하는 것.	
@Slf4j
@Component
@Aspect
public class LoggerAspect {
	@Around("execution(* board..controller.*Controller.*(..)) or "
			   + "execution(* board..service.*Impl.*(..)) or "
			   + "execution(* board..mapper.*Mapper.*(..))")
	public Object logPrint(ProceedingJoinPoint joinpoint) throws Throwable{
		String type = "";
		String name = joinpoint.getSignature().getDeclaringTypeName(); //패키지~메소드 풀경로
		
		if(name.indexOf("Controller") > -1) {
			type = "Controller 	\t: ";
		}else if(name.indexOf("Service") > -1){
			type = "Service 		\t: ";
		}else if(name.indexOf("Mapper") > -1){
			type = "Mapper 		\t: ";
		}//end if-else if
		
		log.debug(type + name + "." + joinpoint.getSignature().getName() + "()"); //getName() : 메소드명?
		return joinpoint.proceed();
	}//logPrint()
}//class
