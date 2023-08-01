package sunbasedata.apiIntegration;
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
    public ResponseEntity<String> authenticateUser(AuthRequest authRequest) {

        return new ResponseEntity<>(customerService.login(authRequest), HttpStatus.OK);
    }

    @GetMapping ("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return new ResponseEntity<>(customerService.customers(), HttpStatus.OK);
    }


}
