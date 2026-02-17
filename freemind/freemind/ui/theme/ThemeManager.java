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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.UIManager;

/**
 * ThemeManager provides a centralized theme system for FreeMind with CSS-like
 * variable support. It manages light/dark themes and allows dynamic switching.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class ThemeManager {
	
	private static final Logger logger = Logger.getLogger(ThemeManager.class.getName());
	private static ThemeManager instance;
	
	// Theme property keys
	public static final String THEME_KEY = "theme";
	public static final String THEME_LIGHT = "light";
	public static final String THEME_DARK = "dark";
	public static final String THEME_SYSTEM = "system";
	
	// CSS-like variable names
	public static final String VAR_BG_WINDOW = "--bg-window";
	public static final String VAR_BG_SURFACE = "--bg-surface";
	public static final String VAR_BG_CANVAS = "--bg-canvas";
	public static final String VAR_BG_HOVER = "--bg-hover";
	public static final String VAR_BG_ACTIVE = "--bg-active";
	
	public static final String VAR_TEXT_PRIMARY = "--text-primary";
	public static final String VAR_TEXT_SECONDARY = "--text-secondary";
	public static final String VAR_TEXT_MUTED = "--text-muted";
	public static final String VAR_TEXT_DISABLED = "--text-disabled";
	
	public static final String VAR_ACCENT_PRIMARY = "--accent-primary";
	public static final String VAR_ACCENT_SUCCESS = "--accent-success";
	public static final String VAR_ACCENT_WARNING = "--accent-warning";
	public static final String VAR_ACCENT_DANGER = "--accent-danger";
	
	public static final String VAR_BORDER_LIGHT = "--border-light";
	public static final String VAR_BORDER_MEDIUM = "--border-medium";
	
	private final Map<String, Color> lightThemeColors;
	private final Map<String, Color> darkThemeColors;
	private final Map<String, String> currentVariables;
	
	private String currentTheme;
	private ThemeChangeListener themeChangeListener;
	
	public interface ThemeChangeListener {
		void onThemeChanged(String newTheme, Map<String, String> variables);
	}
	
	private ThemeManager() {
		lightThemeColors = new HashMap<>();
		darkThemeColors = new HashMap<>();
		currentVariables = new HashMap<>();
		
		initializeLightTheme();
		initializeDarkTheme();
		
		// Default to light theme
		currentTheme = THEME_LIGHT;
		applyTheme(currentTheme);
	}
	
	public static synchronized ThemeManager getInstance() {
		if (instance == null) {
			instance = new ThemeManager();
		}
		return instance;
	}
	
	private void initializeLightTheme() {
		// Backgrounds
		lightThemeColors.put(VAR_BG_WINDOW, new Color(0xF5, 0xF5, 0xF5));
		lightThemeColors.put(VAR_BG_SURFACE, new Color(0xFF, 0xFF, 0xFF));
		lightThemeColors.put(VAR_BG_CANVAS, new Color(0xFA, 0xFA, 0xFA));
		lightThemeColors.put(VAR_BG_HOVER, new Color(0xF7, 0xFA, 0xFC));
		lightThemeColors.put(VAR_BG_ACTIVE, new Color(0xEB, 0xF8, 0xFF));
		
		// Text
		lightThemeColors.put(VAR_TEXT_PRIMARY, new Color(0x1A, 0x20, 0x2C));
		lightThemeColors.put(VAR_TEXT_SECONDARY, new Color(0x4A, 0x55, 0x68));
		lightThemeColors.put(VAR_TEXT_MUTED, new Color(0x71, 0x80, 0x96));
		lightThemeColors.put(VAR_TEXT_DISABLED, new Color(0xA0, 0xAE, 0xC0));
		
		// Accents
		lightThemeColors.put(VAR_ACCENT_PRIMARY, new Color(0x42, 0x99, 0xE1));
		lightThemeColors.put(VAR_ACCENT_SUCCESS, new Color(0x48, 0xBB, 0x78));
		lightThemeColors.put(VAR_ACCENT_WARNING, new Color(0xED, 0x89, 0x36));
		lightThemeColors.put(VAR_ACCENT_DANGER, new Color(0xE5, 0x3E, 0x3E));
		
		// Borders
		lightThemeColors.put(VAR_BORDER_LIGHT, new Color(0xE2, 0xE8, 0xF0));
		lightThemeColors.put(VAR_BORDER_MEDIUM, new Color(0xCB, 0xD5, 0xE0));
	}
	
	private void initializeDarkTheme() {
		// Backgrounds
		darkThemeColors.put(VAR_BG_WINDOW, new Color(0x1A, 0x20, 0x2C));
		darkThemeColors.put(VAR_BG_SURFACE, new Color(0x2D, 0x37, 0x48));
		darkThemeColors.put(VAR_BG_CANVAS, new Color(0x17, 0x19, 0x23));
		darkThemeColors.put(VAR_BG_HOVER, new Color(0x4A, 0x55, 0x68));
		darkThemeColors.put(VAR_BG_ACTIVE, new Color(0x2C, 0x52, 0x82));
		
		// Text
		darkThemeColors.put(VAR_TEXT_PRIMARY, new Color(0xF7, 0xFA, 0xFC));
		darkThemeColors.put(VAR_TEXT_SECONDARY, new Color(0xA0, 0xAE, 0xC0));
		darkThemeColors.put(VAR_TEXT_MUTED, new Color(0x71, 0x80, 0x96));
		darkThemeColors.put(VAR_TEXT_DISABLED, new Color(0x4A, 0x55, 0x68));
		
		// Accents (same colors, adapted for dark mode if needed)
		darkThemeColors.put(VAR_ACCENT_PRIMARY, new Color(0x42, 0x99, 0xE1));
		darkThemeColors.put(VAR_ACCENT_SUCCESS, new Color(0x48, 0xBB, 0x78));
		darkThemeColors.put(VAR_ACCENT_WARNING, new Color(0xED, 0x89, 0x36));
		darkThemeColors.put(VAR_ACCENT_DANGER, new Color(0xF5, 0x65, 0x65));
		
		// Borders
		darkThemeColors.put(VAR_BORDER_LIGHT, new Color(0x4A, 0x55, 0x68));
		darkThemeColors.put(VAR_BORDER_MEDIUM, new Color(0x2D, 0x37, 0x48));
	}
	
	/**
	 * Apply a theme and update all UI components
	 */
	public void applyTheme(String theme) {
		if (!THEME_LIGHT.equals(theme) && !THEME_DARK.equals(theme) && !THEME_SYSTEM.equals(theme)) {
			logger.warning("Unknown theme: " + theme + ", using light theme");
			theme = THEME_LIGHT;
		}
		
		if (THEME_SYSTEM.equals(theme)) {
			// Detect system theme (simplified - always use light for now)
			theme = THEME_LIGHT;
		}
		
		currentTheme = theme;
		updateCurrentVariables();
		applyToUIManager();
		
		if (themeChangeListener != null) {
			themeChangeListener.onThemeChanged(theme, new HashMap<>(currentVariables));
		}
		
		logger.info("Theme applied: " + theme);
	}
	
	private void updateCurrentVariables() {
		currentVariables.clear();
		Map<String, Color> themeColors = THEME_DARK.equals(currentTheme) ? darkThemeColors : lightThemeColors;
		
		for (Map.Entry<String, Color> entry : themeColors.entrySet()) {
			currentVariables.put(entry.getKey(), colorToHex(entry.getValue()));
		}
	}
	
	private void applyToUIManager() {
		Color bgSurface = getColor(VAR_BG_SURFACE);
		Color bgWindow = getColor(VAR_BG_WINDOW);
		Color textPrimary = getColor(VAR_TEXT_PRIMARY);
		Color textSecondary = getColor(VAR_TEXT_SECONDARY);
		Color accent = getColor(VAR_ACCENT_PRIMARY);
		Color border = getColor(VAR_BORDER_LIGHT);
		
		// Apply to UIManager for Swing components
		UIManager.put("Panel.background", bgWindow);
		UIManager.put("OptionPane.background", bgWindow);
		UIManager.put("Frame.background", bgWindow);
		
		UIManager.put("Button.background", bgSurface);
		UIManager.put("Button.foreground", textPrimary);
		UIManager.put("Button.select", accent);
		
		// Menu colors - use dark colors for dark theme
		Color menuBg = isDarkTheme() ? new Color(0x2D, 0x37, 0x48) : bgSurface;
		Color menuFg = isDarkTheme() ? new Color(0xF7, 0xFA, 0xFC) : textPrimary;
		Color menuSel = isDarkTheme() ? new Color(0x42, 0x99, 0xE1) : getColor(VAR_BG_ACTIVE);
		
		UIManager.put("MenuBar.background", menuBg);
		UIManager.put("MenuBar.foreground", menuFg);
		UIManager.put("Menu.background", menuBg);
		UIManager.put("Menu.foreground", menuFg);
		UIManager.put("Menu.shadow", border);
		UIManager.put("Menu.border", border);
		UIManager.put("MenuItem.background", menuBg);
		UIManager.put("MenuItem.foreground", menuFg);
		UIManager.put("PopupMenu.background", menuBg);
		UIManager.put("PopupMenu.foreground", menuFg);
		
		// Menu selection colors
		UIManager.put("Menu.selectionBackground", menuSel);
		UIManager.put("Menu.selectionForeground", Color.WHITE);
		UIManager.put("MenuItem.selectionBackground", menuSel);
		UIManager.put("MenuItem.selectionForeground", Color.WHITE);
		UIManager.put("MenuItem.disabledForeground", textSecondary);
		
		UIManager.put("ToolBar.background", bgSurface);
		UIManager.put("ToolBar.foreground", textPrimary);
		
		UIManager.put("TextField.background", bgSurface);
		UIManager.put("TextField.foreground", textPrimary);
		UIManager.put("TextField.caretForeground", textPrimary);
		
		UIManager.put("TextArea.background", bgSurface);
		UIManager.put("TextArea.foreground", textPrimary);
		
		UIManager.put("Label.foreground", textPrimary);
		UIManager.put("Label.disabledForeground", textSecondary);
		
		UIManager.put("Separator.foreground", border);
		
		// Selection colors
		UIManager.put("textHighlight", accent);
		UIManager.put("textHighlightText", Color.WHITE);
		
		// Focus color
		UIManager.put("focusColor", accent);
		
		// Panel colors
		UIManager.put("Panel.background", bgWindow);
		UIManager.put("Panel.foreground", textPrimary);
		UIManager.put("TitledBorder.titleColor", textPrimary);
		
		// Scrollbar colors
		Color scrollBarBg = isDarkTheme() ? new Color(0x2D, 0x37, 0x48) : new Color(0xE2, 0xE8, 0xF0);
		UIManager.put("ScrollBar.background", scrollBarBg);
	}
	
	/**
	 * Get current theme name
	 */
	public String getCurrentTheme() {
		return currentTheme;
	}
	
	/**
	 * Check if dark theme is active
	 */
	public boolean isDarkTheme() {
		return THEME_DARK.equals(currentTheme);
	}
	
	/**
	 * Get color for a variable
	 */
	public Color getColor(String variableName) {
		Map<String, Color> themeColors = isDarkTheme() ? darkThemeColors : lightThemeColors;
		return themeColors.getOrDefault(variableName, Color.GRAY);
	}
	
	/**
	 * Get color as hex string
	 */
	public String getColorHex(String variableName) {
		return colorToHex(getColor(variableName));
	}
	
	/**
	 * Get all current theme variables
	 */
	public Map<String, String> getCurrentVariables() {
		return new HashMap<>(currentVariables);
	}
	
	/**
	 * Set theme change listener
	 */
	public void setThemeChangeListener(ThemeChangeListener listener) {
		this.themeChangeListener = listener;
	}
	
	/**
	 * Load theme preference from properties
	 */
	public void loadThemePreference(Properties props) {
		String savedTheme = props.getProperty(THEME_KEY, THEME_LIGHT);
		applyTheme(savedTheme);
	}
	
	/**
	 * Save theme preference to properties
	 */
	public void saveThemePreference(Properties props) {
		props.setProperty(THEME_KEY, currentTheme);
	}
	
	/**
	 * Toggle between light and dark themes
	 */
	public void toggleTheme() {
		if (isDarkTheme()) {
			applyTheme(THEME_LIGHT);
		} else {
			applyTheme(THEME_DARK);
		}
	}
	
	/**
	 * Convert Color to hex string
	 */
	private String colorToHex(Color color) {
		return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
	}
	
	/**
	 * Parse hex color string to Color
	 */
	public static Color parseColor(String hex) {
		if (hex == null || !hex.startsWith("#") || hex.length() != 7) {
			return Color.GRAY;
		}
		try {
			int r = Integer.parseInt(hex.substring(1, 3), 16);
			int g = Integer.parseInt(hex.substring(3, 5), 16);
			int b = Integer.parseInt(hex.substring(5, 7), 16);
			return new Color(r, g, b);
		} catch (NumberFormatException e) {
			return Color.GRAY;
		}
	}
}
