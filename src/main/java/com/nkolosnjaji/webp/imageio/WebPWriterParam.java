package com.nkolosnjaji.webp.imageio;

import javax.imageio.ImageWriteParam;
import java.util.Set;

public final class WebPWriterParam extends ImageWriteParam {

    public static final float DEFAULT_COMPRESSION_QUALITY = 0.75F;

    public static final Set<String> compressions = Set.of(WebPWriter.COMPRESSION_LOSSLESS, WebPWriter.COMPRESSION_LOSSY);

    private Integer method;
    private WebPWriterPreset preset;
    private WebPWriterHint imageHint;

    private Boolean multiThreading;
    private Boolean lowMemory;
    private Crop crop = null;
    private Resize resize = null;

    public WebPWriterParam() {
        this(DEFAULT_COMPRESSION_QUALITY, WebPWriterPreset.DEFAULT);
    }

    public WebPWriterParam(WebPWriterPreset preset) {
        this(DEFAULT_COMPRESSION_QUALITY, preset);
    }

    public WebPWriterParam(float compressionQuality) {
        this(compressionQuality, WebPWriterPreset.DEFAULT);
    }

    public WebPWriterParam(float compressionQuality, WebPWriterPreset preset) {
        super.canWriteCompressed = true;
        super.compressionTypes = compressions.toArray(String[]::new);
        super.setCompressionMode(MODE_EXPLICIT);
        super.setCompressionType(WebPWriter.COMPRESSION_LOSSY);
        super.setCompressionQuality(compressionQuality);
        this.setPreset(preset);
    }

    public Crop getCrop() {
        return crop;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public WebPWriterPreset getPreset() {
        return preset;
    }

    public void setPreset(WebPWriterPreset preset) {
        this.preset = preset;
    }

    public WebPWriterHint getImageHint() {
        return imageHint;
    }

    public void setImageHint(WebPWriterHint imageHint) {
        this.imageHint = imageHint;
    }

    public Boolean getMultiThreading() {
        return multiThreading;
    }

    public void setMultiThreading(Boolean multiThreading) {
        this.multiThreading = multiThreading;
    }

    public Resize getResize() {
        return resize;
    }

    public void setResize(Resize resize) {
        this.resize = resize;
    }

    public Boolean getLowMemory() {
        return lowMemory;
    }

    public void setLowMemory(Boolean lowMemory) {
        this.lowMemory = lowMemory;
    }

    public enum WebPWriterPreset {
        DEFAULT,
        PHOTO,
        PICTURE,
        DRAWING,
        ICON,
        TEXT
    }

    public enum WebPWriterHint {
        DEFAULT,
        PICTURE,
        PHOTO,
        GRAPH
    }

}
