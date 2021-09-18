package utils;

import java.awt.image.BufferedImage;

public class ImageTrimmer {

    public static BufferedImage trim(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        //============================ Height Top
        int hOf = 0;
        boolean isMargin;
        for (int hIx = 0; hIx < height; hIx++) {
            isMargin = true;
            for (int wIx = 0; wIx < width; wIx++) {
                if (image.getRGB(wIx, hIx) != 0xFF000000) {
                    isMargin = false;
                    break;
                }
            }
            if (isMargin) {
                hOf++;
            } else {
                break;
            }
        }
        if (hOf > 100) {
            hOf -= 99;
        } else {
            hOf = 0;
        }

        //============================ Height Bottom
        int hEnd = height;
        for (int hIx = height - 1; hIx > 0; hIx--) {
            isMargin = true;
            for (int wIx = 0; wIx < width; wIx++) {
                if (image.getRGB(wIx, hIx) != 0xFF000000) {
                    isMargin = false;
                    break;
                }
            }
            if (isMargin) {
                hEnd--;
            } else {
                break;
            }
        }
        if (hEnd < height - 100) {
            hEnd += 99;
        } else {
            hEnd = height;
        }


        //============================ Width Left
        int wOf = 0;
        for (int wIx = 0; wIx < width; wIx++) {
            isMargin = true;
            for (int hIx = 0; hIx < height; hIx++) {
                if (image.getRGB(wIx, hIx) != 0xFF000000) {
                    isMargin = false;
                    break;
                }
            }
            if (isMargin) {
                wOf++;
            } else {
                break;
            }
        }
        if (wOf > 100) {
            wOf -= 99;
        } else {
            wOf = 0;
        }
        //============================ Width Right
        int wEnd = width;
        for (int wIx = width - 1; wIx > 0; wIx--) {
            isMargin = true;
            for (int hIx = 0; hIx < height; hIx++) {
                if (image.getRGB(wIx, hIx) != 0xFF000000) {
                    isMargin = false;
                    break;
                }
            }
            if (isMargin) {
                wEnd--;
            } else {
                break;
            }
        }
        if (wEnd < width - 100) {
            wEnd += 99;
        } else {
            wEnd = width;
        }

        //============================//============================
        return image.getSubimage(
                wOf,
                hOf,
                wEnd - wOf, // width
                hEnd - hOf // height
        );
    }

}
