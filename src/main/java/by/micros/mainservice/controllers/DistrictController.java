package by.micros.mainservice.controllers;

import by.micros.mainservice.repositories.DistrictRepository;
import by.micros.modelstore.model.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/districts")
public class DistrictController {
    @Autowired
    DistrictRepository districtRepository;

    @GetMapping("/{id}")
    public ResponseEntity<District> getById(@PathVariable Integer id) {
        Optional<District> item = districtRepository.findById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<District> addDepartment(@RequestBody District district) {
        District savedDistrict = districtRepository.save(district);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedDistrict.getDistrictId())
                .toUri();
        return ResponseEntity.created(location)
                .body(savedDistrict);
    }
}
