package com.gleb_dev.my_auction.services;

import com.gleb_dev.my_auction.entity.Bet;
import com.gleb_dev.my_auction.entity.Lot;
import com.gleb_dev.my_auction.entity.User;
import com.gleb_dev.my_auction.entity.enums.Role;
import com.gleb_dev.my_auction.exceptions.BetCreateException;
import com.gleb_dev.my_auction.exceptions.BetDeleteException;
import com.gleb_dev.my_auction.exceptions.BetNotFoundException;
import com.gleb_dev.my_auction.exceptions.LotNotFoundException;
import com.gleb_dev.my_auction.repository.BetRepository;
import com.gleb_dev.my_auction.repository.LotRepository;
import com.gleb_dev.my_auction.repository.UserRepository;
import com.gleb_dev.my_auction.request.BetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Class that implements the business logic of the bets
@Service
public class BetService {

    private BetRepository betRepository;
    private LotRepository lotRepository;
    private UserRepository userRepository;

    @Autowired
    public BetService(BetRepository betRepository, LotRepository lotRepository, UserRepository userRepository) {
        this.betRepository = betRepository;
        this.lotRepository = lotRepository;
        this.userRepository = userRepository;
    }

    public Bet createBet(Long lotId, BetRequest betRequest, Principal principal) {
        User user = getUserByPrincipal(principal);
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found"));

        // Check if lot is active or bet is enough
        if (!lot.isActive() || betRequest.getAmount().compareTo(lot.getInitCost())<0 || lot.getBets()
                .stream()
                .filter(bet -> !bet.isArchival())
                .anyMatch(bet -> {
                    return bet.getAmount().compareTo(betRequest.getAmount()) >= 0;
                })) {
            throw new BetCreateException("Too low bet or lot isn't active");
        }

        if(lot.getUser().equals(user)){
            throw new BetCreateException("You can't bet on your lot");
        }

        Bet bet = new Bet();
        bet.setAmount(betRequest.getAmount());
        bet.setCreatedDate(LocalDateTime.now());
        bet.setLot(lot);
        bet.setUserId(user.getId());

        return betRepository.save(bet);
    }

    public List<Bet> getAllBetsForLot(Long lotId) {
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found"));

        List<Bet> bets = betRepository.findAllByLotAndArchivalIsFalse(lot);

        return bets;
    }

    public void deleteBet(Long betId, Principal principal) {
        User currUser = getUserByPrincipal(principal);
        Bet bet = betRepository.findByIdAndArchivalIsFalse(betId)
                .orElseThrow(() -> new BetNotFoundException("Bet with id " + betId + " cannot be found"));

        if(!bet.getLot().isActive()){
            throw new BetDeleteException("Lot isn' active");
        }

        // Check deletion rights
        if(bet.getUserId() == currUser.getId() || currUser.getRoles().contains(Role.ROLE_ADMIN)) {
            bet.setArchival(true);
            betRepository.save(bet);
        }
        else {
            throw new AccessDeniedException("Only admin can delete data that is not his");
        }
    }

    public Bet getBetById(Long betId){
        return betRepository.findByIdAndArchivalIsFalse(betId)
                .orElseThrow(()->new BetNotFoundException("Bet with id "+ betId +" cannot be found"));
    }

    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }

}
