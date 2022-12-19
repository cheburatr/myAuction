package com.gleb_dev.my_auction.response;

import com.gleb_dev.my_auction.entity.Image;
import com.gleb_dev.my_auction.entity.Lot;
import org.apache.tomcat.util.codec.binary.Base64;

// Class to display the lot together with the picture
public class LotWithImageResponse {

    private Lot lot;
    private String image;

    public LotWithImageResponse(Lot lot, String image) {
        this.lot = lot;
        this.image = image;
    }

    public LotWithImageResponse(Lot lot, Image image) {
        this.lot = lot;
        if(image.getImageBytes().length != 0) {
            this.image = Base64.encodeBase64String(image.getImageBytes());
        }
        else{
            this.image = null;
        }
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage(Image image) {
        if(image.getImageBytes().length != 0) {
            this.image = Base64.encodeBase64String(image.getImageBytes());
        }
        else{
            this.image = null;
        }
    }
}
