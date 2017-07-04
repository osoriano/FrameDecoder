package com.osoriano.framedecoder;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FrameTest {
    @Test
    public void testToString() {
        Frame frame = new Frame(FrameType.PICK, "foo");
        assertEquals("PICK(\"foo\")", frame.toString());

        frame = new Frame(FrameType.DROP);
        assertEquals("DROP", frame.toString());
    }

    @Test
    public void testFrameComparison() {
        Frame frame1 = new Frame(FrameType.PICK, "foo");
        Frame frame2 = new Frame(FrameType.PICK, "foo");
        assertEquals(frame1, frame2);
    }
}
