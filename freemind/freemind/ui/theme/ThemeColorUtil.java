/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2024 FreeMind Contributors
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package freemind.ui.theme;

import java.awt.Color;

import javax.swing.UIManager;

/**
 * Utility class for theme-aware color operations.
 * Provides helper methods to get themed colors with proper fallbacks.
 * 
 * @author FreeMind Contributors
 */
public class ThemeColorUtil {

	/**
	 * Gets a color from the current theme, or returns a fallback if not available.
	 * 
	 * @param variableName the CSS variable name (e.g., "--bg-canvas")
	 * @param fallback the fallback color if theme value is not available
	 * @return the themed color or fallback
	 */
	public static Color getThemeColor(String variableName, Color fallback) {
		ThemeManager themeManager = ThemeManager.getInstance();
		if (themeManager != null) {
			Color themeColor = themeManager.getColor(variableName);
			if (themeColor != null) {
				return themeColor;
			}
		}
		return fallback;
	}

	/**
	 * Gets the canvas background color (mind map area).
	 * 
	 * @param fallback the fallback color
	 * @return the canvas background color
	 */
	public static Color getCanvasBackground(Color fallback) {
		return getThemeColor(ThemeManager.VAR_BG_CANVAS, fallback);
	}

	/**
	 * Gets the window background color.
	 * 
	 * @param fallback the fallback color
	 * @return the window background color
	 */
	public static Color getWindowBackground(Color fallback) {
		return getThemeColor(ThemeManager.VAR_BG_WINDOW, fallback);
	}

	/**
	 * Gets the surface/elevated background color.
	 * 
	 * @param fallback the fallback color
	 * @return the surface background color
	 */
	public static Color getSurfaceBackground(Color fallback) {
		return getThemeColor(ThemeManager.VAR_BG_SURFACE, fallback);
	}

	/**
	 * Gets the primary text color.
	 * 
	 * @param fallback the fallback color
	 * @return the primary text color
	 */
	public static Color getTextPrimary(Color fallback) {
		return getThemeColor(ThemeManager.VAR_TEXT_PRIMARY, fallback);
	}

	/**
	 * Gets the secondary text color.
	 * 
	 * @param fallback the fallback color
	 * @return the secondary text color
	 */
	public static Color getTextSecondary(Color fallback) {
		return getThemeColor(ThemeManager.VAR_TEXT_SECONDARY, fallback);
	}

	/**
	 * Gets the accent color.
	 * 
	 * @param fallback the fallback color
	 * @return the accent color
	 */
	public static Color getAccentColor(Color fallback) {
		return getThemeColor(ThemeManager.VAR_ACCENT_PRIMARY, fallback);
	}

	/**
	 * Gets the selection color.
	 * 
	 * @param fallback the fallback color
	 * @return the selection color
	 */
	public static Color getSelectionColor(Color fallback) {
		return getThemeColor(ThemeManager.VAR_ACCENT_PRIMARY, fallback);
	}

	/**
	 * Gets the hover background color.
	 * 
	 * @param fallback the fallback color
	 * @return the hover background color
	 */
	public static Color getHoverBackground(Color fallback) {
		return getThemeColor(ThemeManager.VAR_BG_HOVER, fallback);
	}

	/**
	 * Gets the border color.
	 * 
	 * @param fallback the fallback color
	 * @return the border color
	 */
	public static Color getBorderColor(Color fallback) {
		return getThemeColor(ThemeManager.VAR_BORDER_MEDIUM, fallback);
	}

	/**
	 * Gets the light border color.
	 * 
	 * @param fallback the fallback color
	 * @return the light border color
	 */
	public static Color getBorderLight(Color fallback) {
		return getThemeColor(ThemeManager.VAR_BORDER_LIGHT, fallback);
	}

	/**
	 * Gets the shadow color with specified alpha.
	 * In dark mode, shadows are more prominent.
	 * 
	 * @param baseAlpha the base alpha value (0-255)
	 * @param fallback the fallback color
	 * @return the shadow color
	 */
	public static Color getShadowColor(int baseAlpha, Color fallback) {
		ThemeManager themeManager = ThemeManager.getInstance();
		if (themeManager != null && themeManager.isDarkTheme()) {
			// Dark mode: stronger shadows
			return new Color(0, 0, 0, Math.min(255, (int)(baseAlpha * 1.5)));
		}
		// Light mode: normal shadows
		return new Color(0, 0, 0, baseAlpha);
	}

	/**
	 * Adjusts a color's brightness for the current theme.
	 * In dark mode, makes colors brighter. In light mode, makes them darker.
	 * 
	 * @param baseColor the base color
	 * @param adjustment the adjustment amount (-255 to 255)
	 * @return the adjusted color
	 */
	public static Color adjustBrightnessForTheme(Color baseColor, int adjustment) {
		ThemeManager themeManager = ThemeManager.getInstance();
		if (themeManager != null && themeManager.isDarkTheme()) {
			// In dark mode, reverse the adjustment
			adjustment = -adjustment;
		}
		
		int r = clamp(baseColor.getRed() + adjustment);
		int g = clamp(baseColor.getGreen() + adjustment);
		int b = clamp(baseColor.getBlue() + adjustment);
		int a = baseColor.getAlpha();
		
		return new Color(r, g, b, a);
	}

	/**
	 * Gets a contrasting color for text based on background luminance.
	 * 
	 * @param backgroundColor the background color
	 * @param darkText the text color to use for light backgrounds
	 * @param lightText the text color to use for dark backgrounds
	 * @return the appropriate text color
	 */
	public static Color getContrastingTextColor(Color backgroundColor, Color darkText, Color lightText) {
		// Calculate luminance using standard formula
		double luminance = (0.299 * backgroundColor.getRed() + 
		                   0.587 * backgroundColor.getGreen() + 
		                   0.114 * backgroundColor.getBlue()) / 255.0;
		
		return luminance > 0.5 ? darkText : lightText;
	}

	/**
	 * Checks if the current theme is dark mode.
	 * 
	 * @return true if dark theme is active
	 */
	public static boolean isDarkTheme() {
		ThemeManager themeManager = ThemeManager.getInstance();
		return themeManager != null && themeManager.isDarkTheme();
	}

	/**
	 * Gets the appropriate color for the current theme.
	 * Returns lightColor in light mode, darkColor in dark mode.
	 * 
	 * @param lightColor color to use in light mode
	 * @param darkColor color to use in dark mode
	 * @return the appropriate color
	 */
	public static Color getThemedColor(Color lightColor, Color darkColor) {
		return isDarkTheme() ? darkColor : lightColor;
	}

	private static int clamp(int value) {
		return Math.max(0, Math.min(255, value));
	}
}
