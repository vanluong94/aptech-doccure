package vn.aptech.doccure.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.doccure.entities.Review;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
}
