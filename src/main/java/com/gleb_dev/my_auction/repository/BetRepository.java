package com.gleb_dev.my_auction.repository;

import com.gleb_dev.my_auction.entity.Bet;
import com.gleb_dev.my_auction.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    Optional<Bet> findByIdAndArchivalIsFalse(Long id);

    List<Bet> findAllByLotAndArchivalIsFalse(Lot lot);

}
