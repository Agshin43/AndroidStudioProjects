package com.agshin.brightpolicelights;

public class PatternElement {
    private int duration;
    private PatternElementType type;
    private int color;

    public PatternElement(int duration, PatternElementType type, int color) {
        this.duration = duration;
        this.type = type;
        this.color = color;
    }

    public PatternElement(String patternElement, String separator){
        String[] list = patternElement.split(separator);
        this.duration = Integer.valueOf(list[2]);
        this.type = (list[0].equals("1")?PatternElementType.Light:PatternElementType.Pause);
        this.color = Integer.valueOf(list[1]);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setType(PatternElementType type) {
        this.type = type;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDuration() {
        return duration;
    }

    public PatternElementType getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public String toString(String separator){
        return ((type == PatternElementType.Light?"1":"0") + separator + color + separator + duration);
    }
}
