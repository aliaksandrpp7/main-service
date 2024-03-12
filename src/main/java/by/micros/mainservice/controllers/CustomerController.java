package by.micros.mainservice.controllers;

import by.micros.mainservice.repositories.CinemaRepository;
import by.micros.mainservice.repositories.CustomerRepository;
import by.micros.modelstore.model.Cinema;
import by.micros.modelstore.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${customer.backendServiceHost}")
    private String backendServiceHost;

    @Value("${customer.backendServicePort}")
    private Integer backendServicePort;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CinemaRepository cinemaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/add/{cinemaName}")
    public ResponseEntity<Customer> extractCustomerAndSave(@PathVariable String cinemaName) {
        String backEndServiceUrl = String.format("http://%s:%d/customer/user", backendServiceHost, backendServicePort);
        Customer customer = restTemplate.getForObject(backEndServiceUrl, Customer.class);

        List<Cinema> cinemaList = cinemaRepository.findByName(cinemaName).map(List::of).orElse(Collections.emptyList());
        if (customer != null) {
            customer.setCinemaList(cinemaList);
            Customer saveCustomer = customerRepository.save(customer);
            URI location = URI.create("/customers" + saveCustomer.getCustomerId());
            return ResponseEntity.created(location).body(saveCustomer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
