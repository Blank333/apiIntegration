package sunbasedata.apiIntegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {
    private String access_token;

    public String login(AuthRequest authRequest) throws JsonProcessingException {
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

        String authData = new ObjectMapper().writeValueAsString(authRequest);
        WebClient client = WebClient.create();

        String response = client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authData)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        access_token = mapper.readTree(response).get("access_token").asText();
        System.out.println(access_token);
        return access_token;
    }

    public List<Customer> customers() throws JsonProcessingException {
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access_token).build();

        String response = Objects.requireNonNull(client.get()
                        .uri(apiUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block())
                .trim();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response, new TypeReference<>() {
        });
    }

    public ResponseEntity<String> create(Customer customerRequest) throws JsonProcessingException {

        if (customerRequest.getFirst_name() == null || customerRequest.getFirst_name().isEmpty() || customerRequest.getLast_name() == null || customerRequest.getLast_name().isEmpty()) {
            return new ResponseEntity<>("First Name or Last Name is missing", HttpStatus.BAD_REQUEST);
        }
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access_token).build();
        String formData = new ObjectMapper().writeValueAsString(customerRequest);

        client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new ResponseEntity<>("Successfully Created", HttpStatus.CREATED);
    }

    public ResponseEntity<String> delete(String uuid) {
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=delete";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access_token).build();

        apiUrl += "&uuid=" + uuid;

        String response = client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        assert response != null;
        if (response.contains("Error Not deleted")) {
            return new ResponseEntity<>("Error Not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (response.contains("UUID not found")) {
            return new ResponseEntity<>("UUID not found", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> update(String uuid, Customer customerRequest) throws JsonProcessingException {
        if (customerRequest.getFirst_name() == null || customerRequest.getFirst_name().isEmpty() || customerRequest.getLast_name() == null || customerRequest.getLast_name().isEmpty()) {
            return new ResponseEntity<>("First Name or Last Name is missing", HttpStatus.BAD_REQUEST);
        }
        if (uuid == null || uuid.isEmpty()) {
            return new ResponseEntity<>("UUID not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=update";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access_token).build();

        apiUrl += "&uuid=" + uuid;

        String formData = new ObjectMapper().writeValueAsString(customerRequest);

        String response = client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
