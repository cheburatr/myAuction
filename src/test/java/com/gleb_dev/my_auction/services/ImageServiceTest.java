package com.gleb_dev.my_auction.services;

import com.gleb_dev.my_auction.entity.Image;
import com.gleb_dev.my_auction.entity.Lot;
import com.gleb_dev.my_auction.entity.User;
import com.gleb_dev.my_auction.exceptions.ImageNotFoundException;
import com.gleb_dev.my_auction.exceptions.LotNotFoundException;
import com.gleb_dev.my_auction.repository.ImageRepository;
import com.gleb_dev.my_auction.repository.LotRepository;
import com.gleb_dev.my_auction.repository.UserRepository;
import com.sun.security.auth.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
public class ImageServiceTest {

    @Autowired
    ImageService imageService;

    @MockBean
    private ImageRepository imageRepository;
    @MockBean
    private LotRepository lotRepository;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void uploadImageToLot_LotIsNull_ShouldThrowException() {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(new User()));
        Mockito.when(lotRepository.findLotByIdAndUserAndArchivalIsFalse(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(LotNotFoundException.class,
                () -> imageService.uploadImageToLot(new MockMultipartFile("testFile", "123123".getBytes()),
                        new UserPrincipal("user"), (long) 1));
        Assertions.assertNotNull(throwable);

    }

    @Test
    public void uploadImageToLot_LotIsNotNull_ShouldReturnImage() throws IOException {
        Mockito.when(userRepository.findUserByUsername(ArgumentMatchers.anyString())).
                thenReturn(Optional.of(new User()));
        Mockito.when(lotRepository.findLotByIdAndUserAndArchivalIsFalse(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).
                thenReturn(Optional.of(new Lot()));
        Mockito.when(imageRepository.save(ArgumentMatchers.any()))
                .thenReturn(new Image());

        Assertions.assertNotNull(imageService.uploadImageToLot(new MockMultipartFile("testFile", "123123".getBytes()),
                new UserPrincipal("user"), (long) 1));
    }

    @Test
    public void getImageToLot_ImageIsNull_ShouldThrowException() {
        Mockito.when(imageRepository.findByLotId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Throwable throwable = Assertions.assertThrows(ImageNotFoundException.class,
                () -> imageService.getImageToLot((long) 1));
        Assertions.assertNotNull(throwable);
    }


    @Test
    public void getImageToLot_ImageIsEmpty_ShouldReturnImage() {
        Image image = new Image();
        image.setImageBytes(new byte[]{1,1,1});
        Mockito.when(imageRepository.findByLotId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(image));

        Assertions.assertNotNull(imageService.getImageToLot((long) 1));
    }

    @Test
    public void getImageToLot_ImageIsNotEmpty_ShouldReturnImage() {
        Image image = new Image();
        image.setImageBytes("123321".getBytes());
        Mockito.when(imageRepository.findByLotId(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(image));

        Assertions.assertNotNull(imageService.getImageToLot((long) 1));
    }
}
