package com.osoriano.framedecoder;

import java.util.Objects;

public class Frame {
    private FrameType type;
    private String body;

    public Frame(FrameType type) {
        this(type, "");
    }

    public Frame(FrameType type, String body) {
        this.type = type;
        this.body = body;
    }

    public FrameType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        String result = "" + this.type;
        if (this.body != null && this.body != "") {
            result += "(\"" + this.body + "\")";
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Frame)) {
            return false;
        }
        Frame frame = (Frame) o;
        return (Objects.equals(this.type, frame.type) &&
                Objects.equals(this.body, frame.body));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.body);
    }
}
