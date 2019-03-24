package com.mja137.MJA137server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
import com.google.common.collect.Lists;

@Controller
public class MJA137Controller {

	@Autowired
	LoginDetailsDao loginDetailsDao;

	@Autowired
	UserDao userdao;

	@Autowired
	ChatMessageDao chatMessageDao;

	@RequestMapping({ "/loginPage","/home/{name}/{id}","/home"	 })
	public String index() {
		return "forward:/index.html";
	}

	@RequestMapping(value="/getDetails", method = RequestMethod.POST)
	@ResponseBody
	private UserEntity getDetails(@RequestParam(value = "empId") String empId, HttpSession session, HttpServletRequest request) throws IOException {
		String attribute = (String) session.getAttribute("name");
		String id = (String) session.getAttribute("id");
		request.getSession().setAttribute("name", attribute);
		request.getSession().setAttribute("id", id);
		if(attribute != null) {
			request.getSession().setAttribute("name", attribute);
			request.getSession().setAttribute("id", id);
			if(loginDetailsDao.existsById(empId) == true) {
				UserEntity result = userdao.findById(empId).get();
				return result;
			}
		}
		return null;
	}

	@RequestMapping(value="/homePage", method = RequestMethod.POST)
	@ResponseBody
	private Iterator<UserEntity> getAllDetails(HttpSession session, HttpServletRequest request) throws IOException {
		String attribute = (String) session.getAttribute("name");
		String id = (String) session.getAttribute("id");
		request.getSession().setAttribute("name", attribute);
		request.getSession().setAttribute("id", id);
		if(attribute != null) {
			request.getSession().setAttribute("name", attribute);
			request.getSession().setAttribute("id", id);
			Iterator<UserEntity> result = userdao.findAll().iterator();
			return result;
		}
		return null;
	}

	@RequestMapping(value="/updateDetails", method = RequestMethod.POST)
	@ResponseBody
	private UserEntity updateDetails(@RequestParam(value = "comment") String comment, HttpSession session, HttpServletRequest request) throws IOException {
		String attribute = (String) session.getAttribute("name");
		String id = (String) session.getAttribute("id");
		request.getSession().setAttribute("name", attribute);
		request.getSession().setAttribute("id", id);
		if(attribute != null) {
			request.getSession().setAttribute("name", attribute);
			request.getSession().setAttribute("id", id);
			if(loginDetailsDao.existsById(id) == true) {
				UserEntity result = userdao.findById(id).get();
				result.setComment(comment);
				userdao.save(result);
				Iterable<UserEntity> response = userdao.findAll();
				ObjectMapper mapper = new ObjectMapper();
				String path = System.getProperty("user.dir");
				FileOutputStream fos = new FileOutputStream("/src/main/resources/json/details.json");
				byte[] byteRes = mapper.writeValueAsString(response).getBytes();
				fos.write(byteRes);
				return result;
			}
		}
		return null;
	}

	@RequestMapping(value="/searchDetails", method = RequestMethod.POST)
	@ResponseBody
	private List<UserEntity> searchDetails(@RequestParam(value = "searchValue") String searchValue, HttpSession session, HttpServletRequest request) throws IOException {
		String attribute = (String) session.getAttribute("name");
		String id = (String) session.getAttribute("id");
		request.getSession().setAttribute("name", attribute);
		request.getSession().setAttribute("id", id);
		if(attribute != null) {
			request.getSession().setAttribute("name", attribute);
			request.getSession().setAttribute("id", id);
			List<UserEntity> nameResult = userdao.findByNameContainingIgnoreCase(searchValue);
			if(nameResult.size()!=0) {
				return nameResult;
			}
			else{
				List<UserEntity> idResult = userdao.findByIdContainingIgnoreCase(searchValue);
				if(idResult.size()!=0) {
					return idResult;
				}
			}
		}
		return null;
	}

	@RequestMapping(value="/updateDetailsAdmin", method = RequestMethod.POST)
	@ResponseBody
	private UserEntity updateDetailsAdmin(@RequestParam(value = "userEntity") String user, HttpSession session, HttpServletRequest request) throws IOException {
		String attribute = (String) session.getAttribute("name");
		String id = (String) session.getAttribute("id");
		request.getSession().setAttribute("name", attribute);
		request.getSession().setAttribute("id", id);
		ObjectMapper mapper = new ObjectMapper();
		UserEntity userEntity = mapper.readValue(user, UserEntity.class);
		if(id.equals("1495489") || id.equals("1495339")) {
			UserEntity result = userdao.save(userEntity);
			return result;
		}
		return null;
	}

	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, String> login(@RequestParam(value = "userId") String userId, @RequestParam(value = "password") String password, HttpServletRequest request) throws IOException {
		if(userId.equals("gbkadmin") && password.equals("kochiadmin")) {
			loginDetailEntry();
			userEntry();
		}

		Map<String, String> result = new HashMap<>();
		result.put("status", "fail");
		if(loginDetailsDao.existsById(userId) == true) {
			LoginDetailsEntity response = loginDetailsDao.findById(userId).get();
			if (userId.equals(response.getUserId()) && password.equals(response.getPassword())){
				request.getSession().setAttribute("name", response.getName());
				request.getSession().setAttribute("id", response.getUserId());
				result.put("status", "success");
				result.put("name", response.getName());
				result.put("id", response.getUserId());
				return result;
			}
		}
		return result;
	}

	@RequestMapping(value="/logout", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, String> logout(HttpSession session, HttpServletRequest request) throws IOException{
		Map<String, String> result = new HashMap<>();
		request.getSession().invalidate();
		result.put("status", "success");
		return result;
	}

	@RequestMapping(value="/loginDetailEntry", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, String> loginDetailEntry() throws IOException{
		Map<String, String> result = new HashMap<>();
		result.put("status","fail");
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<LoginDetailsEntity>> typeReference = new TypeReference<List<LoginDetailsEntity>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/loginDetails.json");
		try {
			List<LoginDetailsEntity> loginDetails = mapper.readValue(inputStream,typeReference);
			loginDetailsDao.saveAll(loginDetails);
			result.put("status","success");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		return result;
	}

	@RequestMapping(value="/userEntry", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, String> userEntry() throws IOException{
		Map<String, String> result = new HashMap<>();
		result.put("status","fail");
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<UserEntity>> typeReference = new TypeReference<List<UserEntity>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/details.json");
		try {
			List<UserEntity> userDetails = mapper.readValue(inputStream,typeReference);
			userdao.saveAll(userDetails);
			result.put("status","success");
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
		return result;
	}

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(ChatMessageEntity message) throws Exception {
		ObjectMapper maper = new ObjectMapper();
		JSONObject jsonmessage = new ObjectMapper().readValue(maper.writeValueAsString(message), JSONObject.class);
		List<ChatMessageEntity> chatList = new ArrayList<ChatMessageEntity>();
		Iterator<ChatMessageEntity> chatResult = chat(jsonmessage.getString("message"), jsonmessage.getString("name"), jsonmessage.getString("empId"));
		List<ChatMessageEntity> list = Lists.newArrayList(chatResult);
		for(Iterator<ChatMessageEntity> chat =  list.iterator(); chat.hasNext();) {
			ChatMessageEntity chatObject = chat.next();
			chatList.add(chatObject);
		}
		return new Greeting(chatList);
	}

	@RequestMapping(value="/chat", method = RequestMethod.POST)
	@ResponseBody
	private Iterator<ChatMessageEntity> chat(@RequestParam(value = "message") String message, String session, String request) throws IOException {
		if(session != null) {
			ChatMessageEntity result = new ChatMessageEntity();
			result.setMessage(message);
			result.setEmpId(request);
			result.setName(session);
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
			result.setDateTime(String.valueOf(df.format(new Date())));
			chatMessageDao.save(result);
			Iterator<ChatMessageEntity> finalChat = chatMessageDao.findAll().iterator();
			return finalChat;
		}
		return null;
	}

	@RequestMapping(value="/getChat", method = RequestMethod.POST)
	@ResponseBody
	private Greeting getChat(HttpSession session, HttpServletRequest request) throws IOException {
		List<ChatMessageEntity> chatList = new ArrayList<ChatMessageEntity>();
		Iterator<ChatMessageEntity> finalChat = chatMessageDao.findAll().iterator();
		List<ChatMessageEntity> list = Lists.newArrayList(finalChat);
		for(Iterator<ChatMessageEntity> chat =  list.iterator(); chat.hasNext();) {
			ChatMessageEntity chatObject = chat.next();
			chatList.add(chatObject);
		}
		return new Greeting(chatList);
	}
}