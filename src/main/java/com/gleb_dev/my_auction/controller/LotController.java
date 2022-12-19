package com.gleb_dev.my_auction.controller;

import com.gleb_dev.my_auction.entity.Image;
import com.gleb_dev.my_auction.entity.Lot;
import com.gleb_dev.my_auction.entity.User;
import com.gleb_dev.my_auction.entity.enums.Role;
import com.gleb_dev.my_auction.exceptions.ImageNotFoundException;
import com.gleb_dev.my_auction.request.LotRequest;
import com.gleb_dev.my_auction.response.LotWithImageResponse;
import com.gleb_dev.my_auction.services.BetService;
import com.gleb_dev.my_auction.services.ImageService;
import com.gleb_dev.my_auction.services.LotService;
import com.gleb_dev.my_auction.services.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Controller that is responsible for creating, showing and deletion of lots
@Controller
@RequestMapping("/lot")
public class LotController {

    Logger logger = LoggerFactory.getLogger(getClass());

    LotService lotService;
    BetService betService;
    ImageService imageService;
    UserService userService;

    @Autowired
    public LotController(LotService lotService, BetService betService, ImageService imageService, UserService userService) {
        this.lotService = lotService;
        this.betService = betService;
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping(value = {"/all", "/all/{active}"})
    public String showLots(@PathVariable Optional<Boolean> active,
                          Model model) {
        List<Lot> lots;
        if (active.isPresent()) {
            if (active.get()) {
                lots = lotService.getAllLots(true);
            } else {
                lots = lotService.getAllLots(false);
            }
        } else {
            lots = lotService.getAllLots(true);
        }

        List<LotWithImageResponse> lotsWithImages = imageService.getLotsWithImages(lots);

        model.addAttribute("lotsWithImages", lotsWithImages);
        return "lots";
    }

    @GetMapping("/{id}")
    public String showLot(@PathVariable("id") long id,
                          Principal principal,
                          Model model) {
        Lot lot = lotService.getLotById(id);

        try {
            Image image = imageService.getImageToLot(lot.getId());
            if (image.getImageBytes().length != 0) {
                model.addAttribute("image", Base64.encodeBase64String(image.getImageBytes()));
            }
        } catch (ImageNotFoundException ex) {
            logger.debug(ex.getMessage());
        }

        // Removing bets that are archival
        lot.setBets(lot.getBets().stream()
                .filter(bet -> !bet.isArchival())
                .collect(Collectors.toList()));
        model.addAttribute("lot", lot);

        User currUser = userService.getCurrentUser(principal);
        model.addAttribute("currUserId", currUser.getId());
        if (currUser.getRoles().contains(Role.ROLE_ADMIN)) {
            model.addAttribute("isAdmin", true);
        }

        return "lot";
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @GetMapping("/create")
    public String showLotCreateForm(Model model) {

        model.addAttribute("lotRequest", new LotRequest());
        return "lotCreateForm";
    }

    @Secured({"ROLE_ADMIN", "ROLE_AUCTIONEER"})
    @PostMapping("/create")
    public String processLotCreation(@Valid @ModelAttribute LotRequest lotRequest,
                                     BindingResult bindingResult,
                                     @RequestParam("image") MultipartFile file,
                                     Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {
            return "lotCreateForm";
        }

        Lot lot = lotService.createLot(lotRequest, principal);

        if (file != null) {
            imageService.uploadImageToLot(file, principal, lot.getId());
        }

        return "redirect:/lot/" + lot.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteLot(@PathVariable("id") long id,
                            Principal principal) {
        lotService.deleteLot(id, principal);
        return "lotDeleteSuccess";
    }

    @GetMapping("/search")
    public String findLotsByKeyword(@RequestParam("keyword") String keyword,
                                    Model model){
        List<Lot> lots = lotService.getLotsByKeyword(keyword);

        List<LotWithImageResponse> lotsWithImages = imageService.getLotsWithImages(lots);

        model.addAttribute("lotsWithImages", lotsWithImages);
        return "lots";
    }
}
