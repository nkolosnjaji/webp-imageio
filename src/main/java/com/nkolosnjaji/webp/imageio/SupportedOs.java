package com.nkolosnjaji.webp.imageio;

enum SupportedOs {

    LINUX_AMD_64("linux", "amd64", "so"),
    MAC_X86_64("mac", "x86_64", "dylib"),
    MAC_AARM_64("mac", "aarm64", "dylib"),
    WIN_AMD_64("windows", "amd64", "dll");

    public final String osName;

    public final String arhc;

    public final String suffix;

    SupportedOs(String osName, String arch, String suffix) {
        this.osName = osName;
        this.arhc = arch;
        this.suffix = suffix;
    }

    public static SupportedOs getCurrent() {
        final var os = System.getProperty("os.name");
        final var arch = System.getProperty("os.arch");

        if (os.toLowerCase().startsWith("linux") && arch.equals("amd64")) {
            return LINUX_AMD_64;
        }
//        else if (os.toLowerCase().startsWith("windows") && arch.equals("amd64")) {
//            return WIN_AMD_64;
//        }
        else if (os.toLowerCase().startsWith("mac") && arch.equals("x86_64")) {
            return MAC_X86_64;
        }
        else if (os.toLowerCase().startsWith("mac") && arch.equals("aarch64")) {
            return MAC_AARM_64;
        }
        else {
            throw new RuntimeException("unsupported OS"); //TODO throw WebpException
        }
    }
}
