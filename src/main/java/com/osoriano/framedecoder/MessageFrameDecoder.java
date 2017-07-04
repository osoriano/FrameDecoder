package com.osoriano.framedecoder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageFrameDecoder implements FrameDecoder {

    private final Logger log = LoggerFactory.getLogger(MessageFrameDecoder.class);

    /* Keep state for the current Frame being parsed */
    private int remainingBytes = 0;
    private FrameType type;
    private byte[] body;
    private int bodyIndex;
    private boolean expectingType;

    /*
     * Parse the bytes and return the decoded Frame.
     * The bytes are structured as below.
     *    >-- increasing byte index -->
     *    +-----------------+-----------------+------------------------+
     *    | FrameLength (1) | MessageType (1) | Body (FrameLength - 1) |
     *    +-----------------+-----------------+------------------------+
     *
     * Body is UTF-8 encoded. This function should buffer partial frames
     * since it may be called at a later time with the rest of the frame.
     *
     * Errors are thrown for:
     *  - Unsupported Encoding
     *  - Invalid Frame Length
     *  - Invalid Message Types (See the FrameType class)
     */
    public List<Frame> readBytes(byte[] bytes)
            throws UnsupportedEncodingException, InvalidFrameException {
        this.log.debug("Reading raw bytes: {}", bytes);
        List<Frame> result = new ArrayList<>();
        for (byte b: bytes) {
            if (this.expectingType) {
                this.type = this.parseType(b);
                log.debug("Frame type: {}", this.type);
                this.expectingType = false;
            } else if (this.remainingBytes == 0) {
                /* Add one to include this current byte */
                this.remainingBytes = b + 1;
                // Throw invalid frame error
                if (this.remainingBytes <= 1) {
                    throw new InvalidFrameException(
                        "Specified frame length must be at least one, but was " + b);
                }
                log.debug("New frame with length: {}",
                          this.remainingBytes);
                this.expectingType = true;
                this.body = new byte[b - 1];
                this.bodyIndex = 0;
            } else {
                log.debug("Appending to frame: {}", b);
                this.body[this.bodyIndex] = b;
                this.bodyIndex += 1;
            }

            this.remainingBytes -= 1;

            if (this.remainingBytes == 0 && this.type != null) {
                String decodedBody = new String(this.body, "UTF-8");
                Frame frame = new Frame(this.type, decodedBody);
                log.debug("Finished parsing frame: {}", frame);
                result.add(frame);
            }
        }
        return result;
    }

    public FrameType parseType(byte b) throws InvalidFrameException {
        if (b == 1) {
            return FrameType.PICK;
        } else if (b == 2) {
            return FrameType.DROP;
        }
        throw new InvalidFrameException(
                "Invalid frame type specified: " + b);
    }
}
