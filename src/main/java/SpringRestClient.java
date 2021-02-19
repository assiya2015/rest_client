import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


public class SpringRestClient {

    public static void main(String[] args) {

        SpringRestClient client = new SpringRestClient();

        String sessionId = client.getCookie();
        User user = new User(3L, "James", "Brown", (byte) 30);
        client.createUser(user);
        System.out.println("New user was added to Database");

        user.setName("Thomas");
        user.setLastName("Shelby");
        client.updateUser(user);
        System.out.println("User was updated");

        client.deleteUser(user);
        System.out.println("User was deleted");
    }


    private static final String URL = "http://91.241.64.178:7081/api/users";
    RestTemplate restTemplate = new RestTemplate();
    String sessionId;


    public String getCookie() {
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, null,String.class);
        sessionId = response.getHeaders().get("Set-Cookie").toString().substring(12,44);
        System.out.println("Response: " + response.toString() + "\n");
        System.out.println("SessionId: " + sessionId +"\n");
        return sessionId;
    }

    private void createUser(User user) {
        String result = restTemplate.exchange(URL, HttpMethod.POST, userHttpEntity(user), String.class).getBody();
        System.out.println(result);
    }

    private void updateUser(User user) {
        String result = restTemplate.exchange(URL, HttpMethod.PUT, userHttpEntity(user), String.class).getBody();
        System.out.println(result);
    }

    private void deleteUser(User user) {
        String result = restTemplate.exchange(URL + "/" + user.getId(), HttpMethod.DELETE, userHttpEntity(user), String.class).getBody();
        System.out.println(result);
    }

    private HttpEntity<User> userHttpEntity(User user){
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Cookie", "JSESSIONID=" + sessionId);
        return new HttpEntity<>(user, requestHeaders);
    }
}
