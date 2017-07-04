package com.osoriano.framedecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrameDecoderTest {
    private FrameDecoder decoder;
    private final Logger log = LoggerFactory.getLogger(FrameDecoderTest.class);


    public FrameDecoderTest() {
        this.decoder = new MessageFrameDecoder();
    }

    @Test
    public void testPickOneChunk()
            throws UnsupportedEncodingException,InvalidFrameException {
        log.info("Testing PICK message in one chunk");
        byte[] encodedMsg = new byte[] { 0x04, 0x01, 'f', 'o', 'o' };
        List<Frame> decodedMsg = this.decoder.readBytes(encodedMsg);
        assertEquals(1, decodedMsg.size());
        Frame decodedFrame = decodedMsg.get(0);
        assertEquals(new Frame(FrameType.PICK, "foo"), decodedFrame);
    }

    @Test
    public void testDropOneChunk()
            throws UnsupportedEncodingException, InvalidFrameException {
        log.info("Testing DROP message in one chunk");
        byte[] encodedMsg = new byte[] { 0x01, 0x02 };
        List<Frame> decodedMsg = this.decoder.readBytes(encodedMsg);
        assertEquals(1, decodedMsg.size());
        Frame decodedFrame = decodedMsg.get(0);
        assertEquals(new Frame(FrameType.DROP), decodedFrame);
    }

    @Test
    public void testPickTwoChunks()
            throws UnsupportedEncodingException, InvalidFrameException {
        log.info("Testing PICK message in two chunks");
        byte[] encodedChunk1 = new byte[] { 0x03, 0x01 };
        List<Frame> decodedChunk1 = this.decoder.readBytes(encodedChunk1);
        assertEquals(decodedChunk1.size(), 0);

        byte[] encodedChunk2 = new byte[] { 'm', 'e' };
        List<Frame> decodedChunk2 = this.decoder.readBytes(encodedChunk2);
        assertEquals(1, decodedChunk2.size());
        Frame decodedFrame = decodedChunk2.get(0);
        assertEquals(new Frame(FrameType.PICK, "me"), decodedFrame);
    }

    @Test
    public void testEmptyChunk()
            throws UnsupportedEncodingException, InvalidFrameException {
        log.info("Testing empty message");
        byte[] encodedMsg = new byte[] {};
        List<Frame> decodedMsg = this.decoder.readBytes(encodedMsg);
        assertEquals(decodedMsg.size(), 0);
    }

    @Test
    public void testTwoDropsOneChunk()
            throws UnsupportedEncodingException, InvalidFrameException {
        log.info("Testing two DROP messages in one chunk");
        byte[] encodedMsg = new byte[] { 0x01, 0x02, 0x01, 0x02 };
        List<Frame> decodedMsg = this.decoder.readBytes(encodedMsg);
        assertEquals(decodedMsg.size(), 2);
        assertEquals(new Frame(FrameType.DROP), decodedMsg.get(0));
        assertEquals(new Frame(FrameType.DROP), decodedMsg.get(1));
    }

    @Test
    public void testInvalidFrameLength()
            throws UnsupportedEncodingException, InvalidFrameException {
        log.info("Testing invalid frame length of 0");

        try {
            byte[] encodedMsg = new byte[] { 0x00, 0x02 };
            List<Frame> decodedMsg = this.decoder.readBytes(encodedMsg);
            fail();
        } catch (InvalidFrameException expectedException) {
            String errorMessage = expectedException.getMessage();
            String expectedMessage = "Specified frame length must be at "
                + "least one, but was 0";
            assertEquals(expectedMessage, errorMessage);
            log.info("Caught expected exception");
        }
    }

    @Test
    public void testInvalidFrameType()
            throws UnsupportedEncodingException, InvalidFrameException {
        log.info("Testing invalid frame length of 0");

        try {
            byte[] encodedMsg = new byte[] { 0x01, 0x00 };
            List<Frame> decodedMsg = this.decoder.readBytes(encodedMsg);
            fail();
        } catch (InvalidFrameException expectedException) {
            String errorMessage = expectedException.getMessage();
            String expectedMessage = "Invalid frame type specified: 0";
            assertEquals(expectedMessage, errorMessage);
            log.info("Caught expected exception");
        }
    }
}
