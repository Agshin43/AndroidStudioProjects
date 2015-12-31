package com.akaya.apps.smartnightlantern.colorpicker.builder;

import com.akaya.apps.smartnightlantern.colorpicker.ColorPickerView;
import com.akaya.apps.smartnightlantern.colorpicker.renderer.ColorWheelRenderer;
import com.akaya.apps.smartnightlantern.colorpicker.renderer.FlowerColorWheelRenderer;
import com.akaya.apps.smartnightlantern.colorpicker.renderer.SimpleColorWheelRenderer;

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