#! /bin/bash

cwebp -quiet src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb.webp
cwebp -quiet src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba.webp

cwebp -quiet -m 0 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_m0.webp
cwebp -quiet -m 0 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_m0.webp

cwebp -quiet -q 50 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_q50.webp
cwebp -quiet -q 50 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_q50.webp

cwebp -quiet -preset icon src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_presetIcon.webp
cwebp -quiet -preset icon src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_presetIcon.webp

cwebp -quiet -hint photo src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_hintPhoto.webp
cwebp -quiet -hint photo src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_hintPhoto.webp

cwebp -quiet -resize 100 100 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_resize100100.webp
cwebp -quiet -resize 100 100 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_resize100100.webp

cwebp -quiet -crop 100 100 100 100 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_crop100100100100.webp
cwebp -quiet -crop 100 100 100 100 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_crop100100100100.webp

cwebp -quiet -crop 100 100 100 100 -resize 100 100 src/test/resources/images/input/rgb.png -o src/test/resources/images/cwebp/rgb_resize100100_crop100100100100.webp
cwebp -quiet -crop 100 100 100 100 -resize 100 100 src/test/resources/images/input/rgba.png -o src/test/resources/images/cwebp/rgba_resize100100_crop100100100100.webp



