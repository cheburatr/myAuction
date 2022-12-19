package com.gleb_dev.my_auction.repository;

import com.gleb_dev.my_auction.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByLotId(Long id);
}
