package by.micros.mainservice.controllers;

import by.micros.mainservice.exeptions.DistrictNotFoundException;
import by.micros.mainservice.repositories.CinemaRepository;
import by.micros.mainservice.repositories.DistrictRepository;
import by.micros.modelstore.model.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cinemas")
public class CinemaController {
    @Autowired
    CinemaRepository cinemaRepository;

    @Autowired
    DistrictRepository districtRepository;

    @GetMapping("")
    public ResponseEntity<List<Cinema>> getAll() {
        List<Cinema> list = cinemaRepository.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cinema> getById(@PathVariable Integer id) {
        Optional<Cinema> item = cinemaRepository.findById(id);
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/district/{id}")
    public ResponseEntity<Cinema> addCinema(@RequestBody Cinema item, @PathVariable Integer id) {
        Cinema savedCinema = districtRepository.findById(id)
                .map(
                        district -> {
                            item.setDistrict(district);
                            item.setDistrictId(district.getDistrictId());
                            return cinemaRepository.save(item);
                        }
//                ).orElseThrow(() -> new RuntimeException(String.format("District with %d is absent", id)));
                ).orElseThrow(() -> new DistrictNotFoundException(id));

        URI location = MvcUriComponentsBuilder
                .fromMethodName(this.getClass(), "getById", savedCinema.getCinemaId())
                .buildAndExpand(savedCinema.getCinemaId())
                .toUri();

        return ResponseEntity.created(location).body(savedCinema);
    }
}
