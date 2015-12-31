package com.agshin.brightpolicelights.colorpicker.builder;

import com.agshin.brightpolicelights.colorpicker.ColorPickerView;
import com.agshin.brightpolicelights.colorpicker.renderer.ColorWheelRenderer;
import com.agshin.brightpolicelights.colorpicker.renderer.FlowerColorWheelRenderer;
import com.agshin.brightpolicelights.colorpicker.renderer.SimpleColorWheelRenderer;

public class ColorWheelRendererBuilder {
	public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
		switch (wheelType) {
			case CIRCLE:
				return new SimpleColorWheelRenderer();
			case FLOWER:
				return new FlowerColorWheelRenderer();
		}
		throw new IllegalArgumentException("wrong WHEEL_TYPE");
	}
}