package by.micros.mainservice.repositories;

import by.micros.modelstore.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    Optional<Cinema> findByName(String cinemaName);
}
