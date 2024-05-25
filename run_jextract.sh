#! /bin/bash

JEXTRACT_HOME=~/Downloads/jextract-22/

rm src/main/java/com/nkolosnjaji/webp/gen/*.java

${JEXTRACT_HOME}/jextract --output src/main/java -t com.nkolosnjaji.webp.gen \
  --header-class-name LibWebP \
  --include-function WebPGetEncoderVersion \
  --include-function WebPConfigLosslessPreset \
  --include-function WebPConfigInitInternal \
  --include-function WebPValidateConfig \
  --include-function WebPMemoryWrite \
  --include-function WebPMemoryWriterInit \
  --include-function WebPMemoryWriterClear \
  --include-function WebPPictureImportBGR \
  --include-function WebPPictureImportBGRA \
  --include-function WebPPictureInitInternal \
  --include-function WebPPictureRescale \
  --include-function WebPPictureCrop \
  --include-function WebPPictureFree \
  --include-function WebPPictureView \
  --include-function WebPPictureRescale \
  --include-function WebPEncode \
  --include-function WebPInitDecoderConfigInternal \
  --include-function WebPGetInfo \
  --include-function WebPGetFeaturesInternal \
  --include-function WebPDecode \
  --include-function WebPFreeDecBuffer \
  --include-struct WebPDecoderConfig \
  --include-struct WebPConfig \
  --include-struct WebPMemoryWriter \
  --include-struct WebPPicture \
  --include-struct WebPBitstreamFeatures \
  --include-struct WebPDecBuffer \
  --include-struct WebPDecoderOptions \
  --include-struct WebPRGBABuffer \
  --include-struct WebPYUVABuffer \
  --include-typedef WebPWriterFunction \
  --include-constant WEBP_ENCODER_ABI_VERSION \
  --include-constant VP8_ENC_ERROR_BAD_DIMENSION \
  --include-constant VP8_ENC_ERROR_BAD_WRITE \
  --include-constant VP8_ENC_ERROR_BITSTREAM_OUT_OF_MEMORY \
  --include-constant VP8_ENC_ERROR_FILE_TOO_BIG \
  --include-constant VP8_ENC_ERROR_INVALID_CONFIGURATION \
  --include-constant VP8_ENC_ERROR_LAST \
  --include-constant VP8_ENC_ERROR_NULL_PARAMETER \
  --include-constant VP8_ENC_ERROR_OUT_OF_MEMORY \
  --include-constant VP8_ENC_ERROR_PARTITION_OVERFLOW \
  --include-constant VP8_ENC_ERROR_PARTITION0_OVERFLOW \
  --include-constant VP8_ENC_ERROR_USER_ABORT \
  --include-constant VP8_ENC_OK \
  --include-constant WEBP_PRESET_DEFAULT \
  --include-constant WEBP_PRESET_DRAWING \
  --include-constant WEBP_PRESET_ICON \
  --include-constant WEBP_PRESET_PHOTO \
  --include-constant WEBP_PRESET_PICTURE \
  --include-constant WEBP_PRESET_TEXT \
  --include-constant WEBP_DECODER_ABI_VERSION \
  --include-constant VP8_STATUS_BITSTREAM_ERROR \
  --include-constant VP8_STATUS_INVALID_PARAM \
  --include-constant VP8_STATUS_NOT_ENOUGH_DATA \
  --include-constant VP8_STATUS_OK \
  --include-constant VP8_STATUS_OUT_OF_MEMORY \
  --include-constant VP8_STATUS_SUSPENDED \
  --include-constant VP8_STATUS_UNSUPPORTED_FEATURE \
  --include-constant VP8_STATUS_USER_ABORT \
  --include-constant MODE_RGB \
  --include-constant MODE_RGBA \
  --include-constant WEBP_HINT_DEFAULT \
  --include-constant WEBP_HINT_GRAPH \
  --include-constant WEBP_HINT_LAST \
  --include-constant WEBP_HINT_PHOTO \
  --include-constant WEBP_HINT_PICTURE \
  --include-constant WEBP_MAX_DIMENSION \
  libwebp.h









