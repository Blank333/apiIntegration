package sunbasedata.apiIntegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

//        assert response != null;
//        System.out.println(response);
//        bearerToken = response.trim().replaceAll("[{}\"]", "").split(":")[1];

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

    public String create(Customer customerRequest) throws JsonProcessingException {

        if (customerRequest.getFirst_name() == null || customerRequest.getFirst_name().isEmpty() || customerRequest.getLast_name() == null || customerRequest.getLast_name().isEmpty()) {
            return String.valueOf(new ResponseEntity<>("First Name or Last Name is missing", HttpStatus.BAD_REQUEST));
        }
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + access_token).build();
        String formData = new ObjectMapper().writeValueAsString(customerRequest);

        String response = client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(formData);

        return response;
    }

}
