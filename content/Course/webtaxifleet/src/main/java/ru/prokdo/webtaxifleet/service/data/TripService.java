package ru.prokdo.webtaxifleet.service.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import ru.prokdo.webtaxifleet.dto.TripDTO;
import ru.prokdo.webtaxifleet.entity.TripEntity;
import ru.prokdo.webtaxifleet.repository.TripRepository;

@Service
public class TripService {

    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Specification<TripEntity> buildTripSpecification(Integer driverId, Integer carId, LocalDate endTime, Boolean inProgress) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (driverId != null) {
                predicates.add(criteriaBuilder.equal(root.get("driverId"), driverId));
            }

            if (carId != null) {
                predicates.add(criteriaBuilder.equal(root.get("carId"), carId));
            }

            if (endTime != null) {
                LocalDateTime startOfDay = endTime.atStartOfDay();
                LocalDateTime endOfDay = endTime.atTime(LocalTime.MAX);

                predicates.add(criteriaBuilder.between(root.get("endTime"), startOfDay, endOfDay));
            }

            if (inProgress != null) {
                if (inProgress) {
                    predicates.add(criteriaBuilder.isNull(root.get("endTime")));
                } else {
                    predicates.add(criteriaBuilder.isNotNull(root.get("endTime")));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public void add(TripDTO tripDTO) {
        TripEntity trip = new TripEntity();
        trip.setDriverId(tripDTO.getDriverId());
        trip.setCarId(tripDTO.getCarId());
        trip.setStartTime(tripDTO.getStartTime());
        trip.setEndTime(tripDTO.getEndTime());
        trip.setStartLocation(tripDTO.getStartLocation());
        trip.setEndLocation(tripDTO.getEndLocation());
        tripRepository.save(trip);
    }

    public boolean existsByDriverId(Integer driverId) {
        return tripRepository.existsByDriverId(driverId);
    }

    public boolean existsByCarId(Integer carId) {
        return tripRepository.existsByCarId(carId);
    }

    public boolean endTrip(Integer tripId) {
        Optional<TripEntity> tripOptional = tripRepository.findById(tripId);
        if (tripOptional.isPresent() && tripOptional.get().getEndTime() == null) {
            TripEntity trip = tripOptional.get();
            trip.setEndTime(LocalDateTime.now());
            tripRepository.save(trip);
            return true;
        }
        return false;
    }

    public List<TripDTO> findAll(Integer driverId, Integer carId, Boolean inProgress, LocalDate endTime, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        Specification<TripEntity> spec = buildTripSpecification(driverId, carId, endTime, inProgress);
        Page<TripEntity> tripPage = tripRepository.findAll(spec, pageable);

        return tripPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TripDTO convertToDTO(TripEntity tripEntity) {
        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(tripEntity.getId());
        tripDTO.setDriverId(tripEntity.getDriverId());
        tripDTO.setCarId(tripEntity.getCarId());
        tripDTO.setStartTime(tripEntity.getStartTime());
        tripDTO.setEndTime(tripEntity.getEndTime());
        tripDTO.setStartLocation(tripEntity.getStartLocation());
        tripDTO.setEndLocation(tripEntity.getEndLocation());
        return tripDTO;
    }
}
