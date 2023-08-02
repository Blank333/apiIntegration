package sunbasedata.apiIntegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {
    private String bearerToken;

    public String login(AuthRequest authRequest) {
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
        bodyValues.add("login_id", authRequest.getLogin_id());
        bodyValues.add("password", authRequest.getPassword());

        System.out.println(bodyValues);
        WebClient client = WebClient.create();

        String response = client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(bodyValues), AuthRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assert response != null;
        bearerToken = response.trim().replaceAll("[{}\"]", "").split(":")[1];
        return bearerToken;
    }

    public List<Customer> customers() throws JsonProcessingException {
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken).build();

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

    public String create(Customer customerRequest) {
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken).build();


        MultiValueMap<String, String> formData = getStringStringMultiValueMap(customerRequest);

        String response = client.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(formData), Customer.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }

    private static MultiValueMap<String, String> getStringStringMultiValueMap(Customer customerRequest) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("first_name", customerRequest.getFirst_name());
        formData.add("last_name", customerRequest.getLast_name());
        formData.add("address", customerRequest.getAddress());
        formData.add("city", customerRequest.getCity());
        formData.add("state", customerRequest.getState());
        formData.add("email", customerRequest.getEmail());
        formData.add("phone", customerRequest.getPhone());
        return formData;
    }
}
