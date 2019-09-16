package board.board.util;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardFileDto;
import board.board.entity.BoardFileEntity;

//스프링의 bean으로 추가. @Bean은 이미 정의된 기능을 bean으로 추가. @Component는 사용자가 정의한 객체를 bean으로 추가.
@Component
public class FileUtils {
	
	//JPA용 parseFileInfo
	//JPA의 @OneToMany애노테이션으로 연관관계를 가지기때문에, BoardFileEntity에 따로 게시글번호(boardIdx)를 저장할 필요없다.
	//따라서 jpa용 parseFileInfo()에서도 boardIdx(게시글번호)를 받지 않는다.
	public List<BoardFileEntity> parseFileInfo(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		//업로드한 파일이 없다면, 메소드를 여기서 종료한다.
		if(ObjectUtils.isEmpty(multipartHttpServletRequest)) {
			return null;
		}//end if
		
		List<BoardFileEntity> fileList = new ArrayList<>(); //데이터가 세팅된 entity 객체를 저장하는 list 객체.
		
		DateTimeFormatter dFormat = DateTimeFormatter.ofPattern("yyyyMMdd");//날짜별 폴더명
		ZonedDateTime current = ZonedDateTime.now();//오늘날짜 확인.
		
		String path = "images/"+current.format(dFormat); //프로젝트 폴더 안의 최상위 경로에 images 폴더안. 폴더 없으면 밑의 코드에 의해 images폴더부터 만들어진다.
		File file = new File(path);
		if(file.exists() == false) { //폴더없으면 폴더 생성
			file.mkdirs();
		}//end if
		
		Iterator<String> it = multipartHttpServletRequest.getFileNames();
		String newFileName, originalFileExtension, contentType;
		//각 파일당 처리.
		while(it.hasNext()) {
			List<MultipartFile> list = multipartHttpServletRequest.getFiles(it.next());
			for(MultipartFile mf : list) {//해당번째 파일 처리
				if(mf.isEmpty() == false) { //1개일땐 위에서 isEmpty()로 걸러서 if 없어도 상관없음. 2개 이상일 때, 필요한 if문. 다중 파일 처리이므로 필요. 
					
					contentType = mf.getContentType();
					if(ObjectUtils.isEmpty(contentType)) {
						break;
					}else { //contentType이 image의 jpg/png/gif일때, 확장자 추가해주는 작업.
						if(contentType.contains("image/jpeg")) {
							originalFileExtension = ".jpg";
						}
						else if(contentType.contains("image/png")) {
							originalFileExtension = ".png";
						}
						else if(contentType.contains("image/gif")) {
							originalFileExtension = ".gif";
						}
						else { // 위의 경우가 아니면 처리작업 X.
							break;
						}//inner if-else if
					}//outer if-else
					
					/////////// DB에 저장될 Entity에 데이터 추가 작업 ////////////
					//저장될 파일명 밀리초로 해도 파일명이 중복될 가능성이 있어서 나노초로 함.
					newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
					BoardFileEntity boardFile = new BoardFileEntity();
					boardFile.setFileSize(mf.getSize());
					boardFile.setOriginalFileName(mf.getOriginalFilename());
					boardFile.setStoredFilePath(path + "/" + newFileName); //파일 저장 경로. DB에 저장될
					boardFile.setCreatorId("admin");
					fileList.add(boardFile); // list객체에 엔티티 누적 저장.
					
					////////// 실제 경로에 파일을 추가 ///////////
					file = new File(path + "/" + newFileName);
					mf.transferTo(file); //파일을 경로에 추가.
				}//end if
			}//end for - 해당번째 파일 처리
		}//end while - 각 파일당 처리.
		return fileList;
	}//parseFileInfo()
	
	////////////////////////////////////////////////////////////////////////
	
	//dto, 마이바티스 용 parseFileInfo
	public List<BoardFileDto> parseFileInfo(int boardIdx, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
		//업로드한 파일이 없다면, 메소드를 여기서 종료한다.
		if(ObjectUtils.isEmpty(multipartHttpServletRequest)) {
			return null;
		}//end if
		
		List<BoardFileDto> fileList = new ArrayList<>(); //데이터가 세팅된 dto 객체를 저장하는 list 객체.
		DateTimeFormatter dFormat = DateTimeFormatter.ofPattern("yyyyMMdd");//날짜별 폴더명
		ZonedDateTime current = ZonedDateTime.now();//오늘날짜 확인.
		String path = "images/"+current.format(dFormat); //프로젝트 폴더 안의 최상위 경로에 images 폴더안. 폴더 없으면 밑의 코드에 의해 images폴더부터 만들어진다.
		File file = new File(path);
		if(file.exists() == false) { //폴더없으면 폴더 생성
			file.mkdirs();
		}//end if
		
		Iterator<String> it = multipartHttpServletRequest.getFileNames();
		String newFileName, originalFileExtension, contentType;
		//각 파일당 처리.
		while(it.hasNext()) {
			List<MultipartFile> list = multipartHttpServletRequest.getFiles(it.next());
			for(MultipartFile mf : list) {//해당번째 파일 처리
				if(mf.isEmpty() == false) { //1개일땐 위에서 isEmpty()로 걸러서 if 없어도 상관없음. 2개 이상일 때, 필요한 if문. 다중 파일 처리이므로 필요. 
					
					contentType = mf.getContentType();
					if(ObjectUtils.isEmpty(contentType)) {
						break;
					}else { //contentType이 image의 jpg/png/gif일때, 확장자 추가해주는 작업.
						if(contentType.contains("image/jpeg")) {
							originalFileExtension = ".jpg";
						}
						else if(contentType.contains("image/png")) {
							originalFileExtension = ".png";
						}
						else if(contentType.contains("image/gif")) {
							originalFileExtension = ".gif";
						}
						else { // 위의 경우가 아니면 처리작업 X.
							break;
						}//inner if-else if
					}//outer if-else
					
					/////////// DB에 저장될 DTO에 데이터 추가 작업 ////////////
					//저장될 파일명 밀리초로 해도 파일명이 중복될 가능성이 있어서 나노초로 함.
					newFileName = Long.toString(System.nanoTime()) + originalFileExtension;
					BoardFileDto boardFile = new BoardFileDto();
					boardFile.setBoardIdx(boardIdx);
					boardFile.setFileSize(mf.getSize());
					boardFile.setOriginalFileName(mf.getOriginalFilename());
					boardFile.setStoredFilePath(path + "/" + newFileName); //파일 저장 경로. DB에 저장될
					fileList.add(boardFile); // list객체에 dto 객체 누적 저장.
					
					////////// 실제 경로에 파일을 추가 ///////////
					file = new File(path + "/" + newFileName);
					mf.transferTo(file); //파일을 경로에 추가.
				}//end if
			}//end for - 해당번째 파일 처리
		}//end while - 각 파일당 처리.
		return fileList;
	}//parseFileInfo()
}//class
