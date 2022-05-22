package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.Review;
import vn.aptech.doccure.repositories.ReviewRepository;
import vn.aptech.doccure.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository repo;

    @Override
    public Review save(Review review) {
        return repo.save(review);
    }

    @Override
    public Iterable<Review> findAll() {
        return repo.findAll();
    }
}
