package com.example.task11;
import com.example.task11.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Task11Application {
	private static final String URL = "http://94.198.50.185:7081/api/users";
	private static final RestTemplate restTemplate = new RestTemplate();
	private static final HttpHeaders headers = new HttpHeaders();

	private static String resp1;
	private static String resp2;
	private static String resp3;
	private static String contResult;

	public static void main(String[] args) {
		SpringApplication.run(Task11Application.class, args);

		// 1. Получаем список пользователей
		getUsers();

		// 2. Добавляем нового пользователя
		User newUser = new User();
		newUser.setId(3L);
		newUser.setName("James");
		newUser.setLastName("Brown");
		newUser.setAge((byte) 52);
		saveUser(newUser);

		// 3. Изменяем пользователя
		newUser.setName("Thomas");
		newUser.setLastName("Shelby");
		updateUser(newUser);

		// 4. Удаляем пользователя
		deleteUser(3L);

		// 5. Конкатенация и вывод длины
		getContResult();
	}

	public static void getUsers() {
		ResponseEntity<User[]> response = restTemplate.exchange(URL, HttpMethod.GET, null, User[].class);
		List<User> users = Arrays.asList(response.getBody());

		// Сохраняем session id
		String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
		headers.add(HttpHeaders.COOKIE, sessionId);
		System.out.println("session id = " + sessionId);
		System.out.println("Users: " + users);
	}

	public static void saveUser(User user) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> request = new HttpEntity<>(user, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
		resp1 = response.getBody();
		System.out.println("Response after saving user: " + resp1);
		if (resp1 == null) {
			System.err.println("Failed to save user. Response is null.");
		}
	}

	public static void updateUser(User user) {
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> request = new HttpEntity<>(user, headers);
		ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.PUT, request, String.class);
		resp2 = response.getBody();
		System.out.println("Response after updating user: " + resp2);
		if (resp2 == null) {
			System.err.println("Failed to update user. Response is null.");
		}
	}

	private static void deleteUser(Long id) {
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, request, String.class);
		resp3 = response.getBody();
		System.out.println("Response after deleting user: " + resp3);
		if (resp3 == null) {
			System.err.println("Failed to delete user. Response is null.");
		}
	}

	private static void getContResult() {
		// Проверяем, были ли получены все ответы
		if (resp1 != null && resp2 != null && resp3 != null) {
			contResult = resp1 + resp2 + resp3; // Конкатенация ответов
			int length = contResult.length(); // Длина итогового результата
			System.out.println("Concatenated result: " + contResult);
			System.out.println("Length of concatenated result: " + length);
		} else {
			System.err.println("One or more responses are null. Cannot concatenate.");
		}
	}
}