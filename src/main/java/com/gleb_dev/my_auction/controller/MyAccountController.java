package com.gleb_dev.my_auction.controller;

import com.gleb_dev.my_auction.entity.Lot;
import com.gleb_dev.my_auction.entity.User;
import com.gleb_dev.my_auction.entity.WinningBet;
import com.gleb_dev.my_auction.request.LotRequest;
import com.gleb_dev.my_auction.services.LotService;
import com.gleb_dev.my_auction.services.UserService;
import com.gleb_dev.my_auction.services.WinningBetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

// Controller that is responsible for showing information about user, lots that he created, won or participated in
@Controller
@RequestMapping("/myAccount")
public class MyAccountController {

    UserService userService;
    LotService lotService;
    WinningBetService winningBetService;

    @Autowired
    public MyAccountController(UserService userService, LotService lotService, WinningBetService winningBetService) {
        this.userService = userService;
        this.lotService = lotService;
        this.winningBetService = winningBetService;
    }

    @GetMapping("/")
    public String showMyAccount(Principal principal,
                                Model model){

        User currUser = userService.getCurrentUser(principal);

        model.addAttribute("user", currUser);

        return "myAccount";
    }

    @GetMapping("/createdLots")
    public String showCreatedLots(Principal principal,
                                  Model model){

        List<Lot> activeLots = lotService.getAllLotsForUser(principal, true);
        List<Lot> completeLots = lotService.getAllLotsForUser(principal, false);

        model.addAttribute("activeLots", activeLots);
        model.addAttribute("completeLots", completeLots);

        return "accountLots";
    }

    @GetMapping("/participatedLots")
    public String showLotsUserParticipateIn(Principal principal,
                                            Model model){

        List<Lot> participatedLots = lotService.getLotsUserParticipateIn(principal, true);
        model.addAttribute("participatedLots", participatedLots);

        return "accountLots";
    }

    @GetMapping("/wonLots")
    public String showLotsUserWon(Principal principal,
                                  Model model){
        List<WinningBet> winningBets = winningBetService.getAllWinningBetsForUser(principal);
        List<Lot> wonLots = new ArrayList<>();

        winningBets.stream()
                .forEach(winningBet -> wonLots.add(winningBet.getBet().getLot()));

        model.addAttribute("wonLots", wonLots);

        return "accountLots";
    }

}
