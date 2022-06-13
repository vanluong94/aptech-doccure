package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    Iterable<Review> findAllByDoctorId(Long id);
}
