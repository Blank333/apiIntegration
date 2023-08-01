package sunbasedata.apiIntegration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CustomerService {
    private String bearerToken;
    public String login(AuthRequest authRequest){
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

        bearerToken = response.trim().replaceAll("[{}\"]", "").split(":")[1];
        return bearerToken;
    }

    public List<Customer> customers(){
        String apiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
        WebClient client = WebClient.builder().defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer " + bearerToken).build();

        List<Customer> response = client.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToFlux(Customer.class)
                .collectList()
                .block();

        return response;
    }
}
