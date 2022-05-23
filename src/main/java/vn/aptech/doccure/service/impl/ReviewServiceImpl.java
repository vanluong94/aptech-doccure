package vn.aptech.doccure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.doccure.entities.Review;
import vn.aptech.doccure.repositories.ReviewRepository;
import vn.aptech.doccure.service.ReviewService;
import vn.aptech.doccure.utils.StringUtils;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository repo;

    @Override
    public Review save(Review review) throws Exception {
        if (StringUtils.isNullOrBlank(review.getTitle())) {
            throw new Exception("Title must not be null or empty!");
        }
        if (StringUtils.isNullOrBlank(review.getContent())) {
            throw new Exception("Content must not be null or empty!");
        }
        if (review.getTitle().length() < 2 || review.getTitle().length() > 100) {
            throw new Exception("Title size must be between 2 and 100");
        }
        if (review.getContent().length() < 2 || review.getContent().length() > 100) {
            throw new Exception("Content size must be between 2 and 100");
        }
        return repo.save(review);
    }

    @Override
    public Iterable<Review> findAll() {
        return repo.findAll();
    }
}
