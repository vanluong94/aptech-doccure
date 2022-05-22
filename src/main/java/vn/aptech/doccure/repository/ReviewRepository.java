package vn.aptech.doccure.repository;

import org.springframework.data.repository.CrudRepository;
import vn.aptech.doccure.entities.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {
}
