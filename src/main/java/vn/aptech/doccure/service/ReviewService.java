package vn.aptech.doccure.service;

import vn.aptech.doccure.entities.Review;

public interface ReviewService {
    Review save(Review review) throws Exception;

    Iterable<Review> findAll();

    void deleteById(Long id);
}
