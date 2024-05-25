package com.nkolosnjaji.webp.imageio;

import javax.imageio.ImageReadParam;

public final class WebPReaderParam extends ImageReadParam {

    private Crop crop;

    private Resize resize;

    private Boolean flip;

    private Boolean multiThreading;

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public Resize getResize() {
        return resize;
    }

    public void setResize(Resize resize) {
        this.resize = resize;
    }

    public Boolean getFlip() {
        return flip;
    }

    public void setFlip(Boolean flip) {
        this.flip = flip;
    }

    public Boolean getMultiThreading() {
        return multiThreading;
    }

    public void setMultiThreading(Boolean multiThreading) {
        this.multiThreading = multiThreading;
    }
}
