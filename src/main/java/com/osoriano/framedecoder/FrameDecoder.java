package com.osoriano.framedecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface FrameDecoder {
  /**
   * Returns zero or more fully buffered frames by reading `bytes` as they
   * arrive and decoding any complete frames.
   *
   * @param bytes the input bytes containing zero or more frames.
   *
   * @return zero or more decoded frames.
   */
  public List<Frame> readBytes(byte[] bytes)
      throws UnsupportedEncodingException, InvalidFrameException;
}
