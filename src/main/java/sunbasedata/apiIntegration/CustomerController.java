package sunbasedata.apiIntegration;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(customerService.login(authRequest), HttpStatus.OK);
    }

    @PostMapping("/create-customer")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customerRequest) {
        return new ResponseEntity<>(customerService.create(customerRequest), HttpStatus.CREATED);
    }

    @GetMapping ("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() throws JsonProcessingException {
        return new ResponseEntity<>(customerService.customers(), HttpStatus.OK);
    }


}
