package com.edu.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.springboot.jdbc.BoardDTO;
import com.edu.springboot.jdbc.IBoardService;
import com.edu.springboot.jdbc.ParameterDTO;

import jakarta.servlet.http.HttpServletRequest;
import utils.PagingUtil;

@Controller
public class MainController {

	//DAO호출을 위한 빈 자동주입. 이 인터페이스를 통해 Mapper를 호출한다. 
	@Autowired
	IBoardService dao;
	
	@RequestMapping("/")
	public String home() {
		return "home";
	}
 
	//목록 
	@RequestMapping("/list.do")
	public String boardList(Model model, HttpServletRequest req, 
			ParameterDTO parameterDTO) {
		/* 매개변수는 View로 전달할 데이터를 저장하기 위한 Model, 요청을 받아 
		처리하기 위한 request 내장객체, 커맨드객체로 사용할 DTO를 추가한다. */
		
		//게시물의 갯수 카운트(검색어가 있는 경우 DTO객체에 자동으로 저장됨) 
		int totalCount = dao.getTotalCount(parameterDTO);
		//페이징을 위한 설정값(하드코딩)
		int pageSize = 2; 
		int blockPage = 2; 
		/* 목록에 첫 진입시에는 페이지번호가 없으므로 무조건 1로 설정하고, 파라미터로
		 * 전달된 페이지번호가 있다면 받은후 정수로 변경한다. */
		int pageNum = (req.getParameter("pageNum")==null 
			|| req.getParameter("pageNum").equals("")) 
			? 1 : Integer.parseInt(req.getParameter("pageNum"));
		//현재 페이지에 출력할 게시물의 구간을 계산한다.
		int start = (pageNum-1) * pageSize + 1;
		int end = pageNum * pageSize;
		//계산된 결과는 DTO에 저장한다. 
		parameterDTO.setStart(start);
		parameterDTO.setEnd(end);
		
		//View에서 게시물의 가상번호 계산을 위한 값을 Map에 저장 
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("totalCount", totalCount);
		maps.put("pageSize", pageSize);
		maps.put("pageNum", pageNum);
		model.addAttribute("maps", maps);
		
		//DB에서 인출한 게시물의 목록을 Model에 저장 
		ArrayList<BoardDTO> lists = dao.listPage(parameterDTO);
		model.addAttribute("lists", lists);
		
		//게시판 하단에 출력할 페이지번호를 String저장 후 Model에 저장 
		String pagingImg =
			PagingUtil.pagingImg(totalCount, pageSize, 
				blockPage, pageNum,
				req.getContextPath()+"/list.do?");
		model.addAttribute("pagingImg", pagingImg);
		
		//View로 포워드한다. 
		return "list";       
	}	
	
	//입력1 : 작성 페이지 매핑
	@GetMapping("/write.do")
	public String boardWriteGet(Model model) {		
		return "write";       
	}	
	//입력2 : 사용자가 작성한 값으로 입력 처리 
	@PostMapping("/write.do")
	public String boardWritePost(Model model, HttpServletRequest req) {
		//request 내장객체를 통해 폼값을 받음 
		String name = req.getParameter("name");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		int result = dao.write(name, title, content);
		System.out.println("글쓰기결과:"+ result);
		return "redirect:list.do";       
	}
	
	//내용보기
	@RequestMapping("/view.do")
	public String boardView(Model model, BoardDTO boardDTO) {
		//게시물 레코드를 DTO에 저장한 후 반환한다. 
		boardDTO = dao.view(boardDTO);
		//내용의 경우 줄바꿈 처리를 한 후 View로 전달해야한다. 
		boardDTO.setContent(boardDTO.getContent()
				.replace("\r\n","<br/>"));
		model.addAttribute("boardDTO", boardDTO);
		return "view";       
	}
	
	//수정1 : 저장된 내용을 불러와서 작성폼에 추가 
	@GetMapping("/edit.do")
	public String boardEditGet(Model model, BoardDTO boardDTO) {
		boardDTO = dao.view(boardDTO);
		model.addAttribute("boardDTO", boardDTO);
		return "edit";       
	}	
	//수정2 : 입력한 내용으로 수정 처리 
	@PostMapping("/edit.do")
	public String boardEditPost(BoardDTO boardDTO) {
		int result = dao.edit(boardDTO);
		System.out.println("글수정결과:"+ result);
		//수정 처리가 완료된 후 내용보기 페이지로 이동한다. 
		return "redirect:view.do?idx="+ boardDTO.getIdx();       
	}
	
	@PostMapping("/delete.do")
	public String boardDeletePost(HttpServletRequest req) {
		int result = dao.delete(req.getParameter("idx"));
		System.out.println("글삭제결과:"+ result);
		return "redirect:list.do";       
	}
}
 