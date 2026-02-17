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
package freemind.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import freemind.main.Resources;
import freemind.ui.theme.ThemeColorUtil;

/**
 * SVGIcon provides a bridge to SVG-based icons with PNG fallback.
 * When Batik SVG library is available, it renders SVG at requested size.
 * Otherwise, falls back to PNG icons.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class SVGIcon implements Icon {

	private static final Logger logger = Logger.getLogger(SVGIcon.class.getName());
	
	// Cache for rendered icons
	private static final Map<String, ImageIcon> iconCache = new HashMap<>();
	
	// Default icon sizes
	public static final int SIZE_SMALL = 16;
	public static final int SIZE_MEDIUM = 24;
	public static final int SIZE_LARGE = 32;
	
	private final String iconName;
	private final int width;
	private final int height;
	private Icon delegateIcon;
	private boolean isPlaceholderIcon = false;
	
	/**
	 * Creates an icon with default size (24px).
	 * 
	 * @param iconName the name of the icon (e.g., "filenew", "idea")
	 */
	public SVGIcon(String iconName) {
		this(iconName, SIZE_MEDIUM);
	}
	
	/**
	 * Creates an icon with specified size.
	 * 
	 * @param iconName the name of the icon
	 * @param size the icon size in pixels
	 */
	public SVGIcon(String iconName, int size) {
		this.iconName = iconName;
		this.width = size;
		this.height = size;
		this.delegateIcon = loadIcon();
	}
	
	/**
	 * Loads the icon - tries SVG first, falls back to PNG.
	 */
	private Icon loadIcon() {
		// Check cache first
		String cacheKey = iconName + "_" + width;
		ImageIcon cached = iconCache.get(cacheKey);
		if (cached != null) {
			return cached;
		}
		
		// Try to load SVG (if Batik is available)
		Icon svgIcon = loadSVG();
		if (svgIcon != null) {
			return svgIcon;
		}
		
		// Fall back to PNG
		Icon pngIcon = loadPNG();
		if (pngIcon != null) {
			return pngIcon;
		}
		
		// Return placeholder if nothing found
		logger.warning("Icon not found: " + iconName);
		return createPlaceholderIcon();
	}
	
	/**
	 * Attempts to load SVG icon.
	 * Currently returns null - will be implemented when Batik is integrated.
	 */
	private Icon loadSVG() {
		// Check if SVG file exists
		URL svgUrl = Resources.getInstance().getResource("images/svg/" + iconName + ".svg");
		if (svgUrl == null) {
			svgUrl = Resources.getInstance().getResource("images/svg/toolbar/" + iconName + ".svg");
		}
		if (svgUrl == null) {
			svgUrl = Resources.getInstance().getResource("images/svg/nodes/" + iconName + ".svg");
		}
		
		if (svgUrl != null) {
			// SVG exists - for now, log it and fall back to PNG
			// In the future, this will render the SVG using Batik
			logger.fine("SVG icon found (not yet rendered): " + iconName);
		}
		
		return null;
	}
	
	/**
	 * Loads PNG icon as fallback.
	 */
	private Icon loadPNG() {
		// Map of SVG icon names to their PNG equivalents
		String[] pngEquivalents = getPNGEquivalents(iconName);
		
		// Try different paths for each possible name
		for (String pngName : pngEquivalents) {
			String[] paths = {
				"images/" + pngName + ".png",
				"images/icons/" + pngName + ".png", 
				"images/" + pngName + ".gif"
			};
			
			for (String path : paths) {
				try {
					URL url = Resources.getInstance().getResource(path);
					if (url != null) {
						ImageIcon icon = new ImageIcon(url);
						if (icon.getIconWidth() > 0) {
							// Scale to requested size if needed
							if (icon.getIconWidth() != width || icon.getIconHeight() != height) {
								icon = scaleIcon(icon, width, height);
							}
							iconCache.put(iconName + "_" + width, icon);
							return icon;
						}
					}
				} catch (Exception e) {
					// Try next path
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get PNG equivalent names for an SVG icon name.
	 * This handles cases where SVG name differs from PNG name.
	 */
	private String[] getPNGEquivalents(String name) {
		// Mapping of SVG names to their PNG equivalents
		switch (name) {
			case "cut": return new String[]{"editcut", "cut"};
			case "copy": return new String[]{"editcopy", "copy"};
			case "paste": return new String[]{"editpaste", "paste"};
			case "delete": return new String[]{"editdelete", "delete", "remove"};
			case "add": return new String[]{"add", "insert", "list-add-font"};
			case "find": return new String[]{"find", "search"};
			case "search": return new String[]{"find", "search"};
			case "redo": return new String[]{"redo"};
			case "undo": return new String[]{"undo"};
			case "leftarrow": return new String[]{"1leftarrow", "leftarrow", "arrow-left"};
			case "rightarrow": return new String[]{"1rightarrow", "draw-arrow-forward", "rightarrow", "arrow-right"};
			case "zoom-in": return new String[]{"zoom-in", "zoomin", "page-zoom"};
			case "zoom-out": return new String[]{"zoom-out", "zoomout"};
			case "save": return new String[]{"filesave", "save"};
			case "saveas": return new String[]{"filesaveas", "saveas"};
			case "attributes": return new String[]{"attributes", "attribute"};
			case "details": return new String[]{"details", "info"};
			case "filter": return new String[]{"filter"};
			case "close": return new String[]{"close", "window-close"};
			case "editfilter": return new String[]{"Btn_edit", "edit"};
			case "unfold": return new String[]{"unfold"};
			case "link": return new String[]{"Link", "link"};
			default: return new String[]{name};
		}
	}
	
	/**
	 * Scales an icon to the requested size.
	 */
	private ImageIcon scaleIcon(ImageIcon original, int w, int h) {
		java.awt.Image img = original.getImage();
		java.awt.Image scaled = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}
	
	/**
	 * Creates a placeholder icon when no icon is found.
	 */
	private Icon createPlaceholderIcon() {
		isPlaceholderIcon = true;
		return new Icon() {
			public void paintIcon(Component c, Graphics g, int x, int y) {
				g.setColor(ThemeColorUtil.getBorderColor(Color.GRAY));
				g.drawRect(x, y, width - 1, height - 1);
			}
			public int getIconWidth() { return width; }
			public int getIconHeight() { return height; }
		};
	}
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (delegateIcon != null) {
			delegateIcon.paintIcon(c, g, x, y);
		}
	}
	
	@Override
	public int getIconWidth() {
		return width;
	}
	
	@Override
	public int getIconHeight() {
		return height;
	}
	
	/**
	 * Clears the icon cache.
	 * Call this when the theme changes.
	 */
	public static void clearCache() {
		iconCache.clear();
		logger.info("Icon cache cleared");
	}
	
	/**
	 * Checks if an icon exists (either SVG or PNG).
	 * 
	 * @param iconName the icon name
	 * @return true if the icon file exists
	 */
	public static boolean exists(String iconName) {
		// Check SVG
		if (Resources.getInstance().getResource("images/svg/" + iconName + ".svg") != null) {
			return true;
		}
		if (Resources.getInstance().getResource("images/svg/toolbar/" + iconName + ".svg") != null) {
			return true;
		}
		// Check PNG
		if (Resources.getInstance().getResource("images/" + iconName + ".png") != null) {
			return true;
		}
		if (Resources.getInstance().getResource("images/icons/" + iconName + ".png") != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if this is a placeholder icon (shown when no icon is found).
	 * 
	 * @return true if this is a placeholder
	 */
	public boolean isPlaceholder() {
		return isPlaceholderIcon;
	}
}
