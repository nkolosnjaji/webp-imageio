#! /bin/bash

cwebp -quiet src/test/resources/images/input/4.png -o src/test/resources/images/cwebp/4.webp
cwebp -quiet src/test/resources/images/input/alpha.png -o src/test/resources/images/cwebp/alpha.webp

dwebp -quiet src/test/resources/images/cwebp/default_witout_alpha.webp -o src/test/resources/images/dwebp/default_witout_alpha.png