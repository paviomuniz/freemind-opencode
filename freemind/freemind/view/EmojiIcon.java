/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2024 FreeMind Contributors
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or or any later version.
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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * EmojiIcon provides support for using emoji as node icons.
 * This allows users to use Unicode emoji characters as icons in mind maps.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class EmojiIcon implements Icon {

	private static final Logger logger = Logger.getLogger(EmojiIcon.class.getName());
	
	// Cache for rendered emoji icons
	private static final Map<String, ImageIcon> emojiCache = new HashMap<>();
	
	// Common emoji mappings to icon names
	public static final Map<String, String> EMOJI_MAP = new HashMap<>();
	
	static {
		// Popular emoji for mind map icons
		EMOJI_MAP.put("star", "\u2B50");       // ⭐
		EMOJI_MAP.put("heart", "\u2764");     // ❤
		EMOJI_MAP.put("flag", "\u2691");       // ⚑
		EMOJI_MAP.put("idea", "\u2753");       // ❓
		EMOJI_MAP.put("question", "\u2754");   // ❔
		EMOJI_MAP.put("warning", "\u26A0");    // ⚠
		EMOJI_MAP.put("info", "\u2139");       // ℹ
		EMOJI_MAP.put("check", "\u2714");      // ✔
		EMOJI_MAP.put("cross", "\u2716");      // ✖
		EMOJI_MAP.put("music", "\u266B");      // ♫
		EMOJI_MAP.put("mail", "\u2709");       // ✉
		EMOJI_MAP.put("phone", "\u2706");      // ✆
		EMOJI_MAP.put("camera", "\u270F");     // ✏
		EMOJI_MAP.put("clock", "\u23F0");      // ⏰
		EMOJI_MAP.put("calendar", "\uD83D\uDCC5"); // 📅
		EMOJI_MAP.put("dollar", "\uD83D\uDCB0"); // 💰
		EMOJI_MAP.put("chart", "\uD83D\uDCC8"); // 📈
		EMOJI_MAP.put("book", "\uD83D\uDCDA"); // 📚
		EMOJI_MAP.put("bulb", "\uD83D\uDCA1"); // 💡
		EMOJI_MAP.put("fire", "\uD83D\uDD25"); // 🔥
		EMOJI_MAP.put("rocket", "\uD83D\uDE80"); // 🚀
		EMOJI_MAP.put("trophy", "\uD83C\uDFC6"); // 🏆
		EMOJI_MAP.put("target", "\uD83D\uDDFA"); // 📯
		EMOJI_MAP.put("gift", "\uD83C\uDF81"); // 🎁
		EMOJI_MAP.put("sun", "\u2600");         // ☀
		EMOJI_MAP.put("cloud", "\u2601");       // ☁
		EMOJI_MAP.put("rain", "\uD83C\uDF27");  // 🌧
		EMOJI_MAP.put("snow", "\u2744");        // ❄
		EMOJI_MAP.put("flower", "\uD83C\uDF38"); // 🌸
		EMOJI_MAP.put("tree", "\uD83C\uDF33"); // 🌳
		EMOJI_MAP.put("paw", "\uD83D\uDC3B");  // 🐻
		EMOJI_MAP.put("smiley", "\uD83D\uDE0A"); // 😊
		EMOJI_MAP.put("thumbsup", "\uD83D\uDC4D"); // 👍
		EMOJI_MAP.put("thumbsdown", "\uD83D\uDC4E"); // 👎
		EMOJI_MAP.put("star2", "\uD83C\uDF1F"); // 🟋
		EMOJI_MAP.put("sparkle", "\u2728");    // ✨
		EMOJI_MAP.put("cloudy", "\u2601");      // ☁
		EMOJI_MAP.put("umbrella", "\u2602");    // ☂
		EMOJI_MAP.put("snowflake", "\u2744");  // ❄
		EMOJI_MAP.put("anchor", "\u2693");      // ⚓
		EMOJI_MAP.put("car", "\uD83D\uDE97");  // 🚗
		EMOJI_MAP.put("plane", "\u2708");      // ✈
		EMOJI_MAP.put("home", "\uD83C\uDFE0"); // 🏠
		EMOJI_MAP.put("work", "\uD83C\uDFBC"); // 🎼
		EMOJI_MAP.put("note", "\uD83C\uDFB5"); // 🎵
		EMOJI_MAP.put("video", "\uD83C\uDFAC"); // 🎬
		EMOJI_MAP.put("globe", "\uD83C\uDF0D"); // 🌍
		EMOJI_MAP.put("lock", "\uD83D\uDD12"); // 🔒
		EMOJI_MAP.put("key", "\uD83D\uDD11"); // 🔑
		EMOJI_MAP.put("bell", "\uD83D\uDD14"); // 🔔
		EMOJI_MAP.put("mail2", "\uD83D\uDCE7"); // 📧
		EMOJI_MAP.put("calendar2", "\uD83D\uDCC6"); // 📆
		EMOJI_MAP.put("pin", "\uD83D\uDCCD");   // 📍
		EMOJI_MAP.put("tag", "\uD83C\uDF97");  // 🎗
		EMOJI_MAP.put("prize", "\uD83C\uDF96"); // 🎖
		EMOJI_MAP.put("medal", "\uD83C\uDFC5"); // 🏅
		EMOJI_MAP.put("crown", "\uD83D\uDC51"); // 👑
		EMOJI_MAP.put("gem", "\uD83D\uDC8E"); // 💎
		EMOJI_MAP.put("bomb", "\uD83D\uDCA3"); // 💣
		EMOJI_MAP.put("flash", "\u26A1");      // ⚡
		EMOJI_MAP.put("knife", "\uD83D\uDD2A"); // 🔪
		EMOJI_MAP.put("pill", "\uD83D\uDC8A"); // 💊
		EMOJI_MAP.put("hourglass", "\u23F3"); // ⏳
		EMOJI_MAP.put("alarm", "\u23F0");      // ⏰
		EMOJI_MAP.put("watch", "\u231A");      // ⌚
		EMOJI_MAP.put("box", "\uD83D\uDCE6");  // 📦
		EMOJI_MAP.put("folder", "\uD83D\uDCC1"); // 📁
		EMOJI_MAP.put("pencil", "\u270F");     // ✏
		EMOJI_MAP.put("scissors", "\u2702");   // ✂
		EMOJI_MAP.put("ruler", "\uD83D\uDCCF"); // 📏
		EMOJI_MAP.put("paperclip", "\uD83D\uDDCE"); // 📎
		EMOJI_MAP.put("pushpin", "\uD83D\uDCCC"); // 📌
		EMOJI_MAP.put("magnify", "\uD83D\uDD0D"); // 🔍
		EMOJI_MAP.put("magnify2", "\uD83D\uDD0E"); // 🔎
		EMOJI_MAP.put("phone2", "\uD83D\uDCDE"); // 📞
		EMOJI_MAP.put("mobile", "\uD83D\uDCF5"); // 📵
		EMOJI_MAP.put("computer", "\uD83D\uDCBB"); // 💻
		EMOJI_MAP.put("printer", "\uD83D\uDDA8"); // 🖨
		EMOJI_MAP.put("battery", "\uD83D\uDD0B"); // 🔋
		EMOJI_MAP.put("bulb2", "\uD83D\uDCA1"); // 💡
		EMOJI_MAP.put("map", "\uD83D\uDDFA");  // 📯
		EMOJI_MAP.put("compass", "\uD83E\uDDD0"); // 🧐
		EMOJI_MAP.put("tools", "\uD83D\uDEE0"); // 🛠
		EMOJI_MAP.put("wrench", "\uD83D\uDD27"); // 🔧
		EMOJI_MAP.put("hammer", "\uD83D\uDD28"); // 🔨
		EMOJI_MAP.put("gear", "\u2699");        // ⚙
		EMOJI_MAP.put("balance", "\u2696");     // ⚖
		EMOJI_MAP.put("percent", "\uD83D\uDCAF"); // 💯
	}
	
	private final String emojiChar;
	private final int size;
	private final Font emojiFont;
	
	/**
	 * Creates an emoji icon.
	 * 
	 * @param emojiChar the emoji character
	 * @param size the icon size in pixels
	 */
	public EmojiIcon(String emojiChar, int size) {
		this.emojiChar = emojiChar;
		this.size = size;
		// Try to use system emoji font, fallback to default
		this.emojiFont = getEmojiFont(size);
	}
	
	/**
	 * Creates an emoji icon with default size (24px).
	 * 
	 * @param emojiChar the emoji character
	 */
	public EmojiIcon(String emojiChar) {
		this(emojiChar, 24);
	}
	
	/**
	 * Gets an emoji icon by name.
	 * 
	 * @param name the emoji name (e.g., "star", "heart")
	 * @param size the icon size
	 * @return EmojiIcon or null if not found
	 */
	public static EmojiIcon getByName(String name, int size) {
		String emoji = EMOJI_MAP.get(name.toLowerCase());
		if (emoji != null) {
			return new EmojiIcon(emoji, size);
		}
		return null;
	}
	
	/**
	 * Checks if an emoji exists for the given name.
	 * 
	 * @param name the emoji name
	 * @return true if emoji exists
	 */
	public static boolean exists(String name) {
		return EMOJI_MAP.containsKey(name.toLowerCase());
	}
	
	/**
	 * Gets all available emoji names.
	 * 
	 * @return array of emoji names
	 */
	public static String[] getAllEmojiNames() {
		return EMOJI_MAP.keySet().toArray(new String[0]);
	}
	
	/**
	 * Gets the emoji character for a name.
	 * 
	 * @param name the emoji name
	 * @return emoji character or null
	 */
	public static String getEmojiChar(String name) {
		return EMOJI_MAP.get(name.toLowerCase());
	}
	
	private Font getEmojiFont(int size) {
		// Try to get a font that supports emoji
		String[] fontNames = {
			"Segoe UI Emoji",
			"Apple Color Emoji", 
			"Twitter Color Emoji",
			"Noto Color Emoji",
			"EmojiOne Color",
			"Symbola",
			Font.SANS_SERIF
		};
		
		for (String fontName : fontNames) {
			try {
				Font font = new Font(fontName, Font.PLAIN, size);
				// Test if font can render the emoji
				if (canRender(font)) {
					return font;
				}
			} catch (Exception e) {
				// Try next font
			}
		}
		
		// Fallback to default sans-serif
		return new Font(Font.SANS_SERIF, Font.PLAIN, size);
	}
	
	private boolean canRender(Font font) {
		FontMetrics fm = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
			.createGraphics().getFontMetrics(font);
		return fm.charWidth(emojiChar.charAt(0)) > 0;
	}
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g;
		
		// Store original settings
		Font originalFont = g2d.getFont();
		Color originalColor = g2d.getColor();
		Object originalAA = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		
		// Enable antialiasing
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Set emoji font
		g2d.setFont(emojiFont);
		
		// Draw emoji centered in the icon area
		FontMetrics fm = g2d.getFontMetrics(emojiFont);
		int charWidth = fm.charWidth(emojiChar.charAt(0));
		int yOffset = (size - fm.getAscent()) / 2 + fm.getAscent();
		int xOffset = (size - charWidth) / 2;
		
		g2d.drawString(emojiChar, x + xOffset, y + yOffset);
		
		// Restore settings
		g2d.setFont(originalFont);
		g2d.setColor(originalColor);
		if (originalAA != null) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, originalAA);
		}
	}
	
	@Override
	public int getIconWidth() {
		return size;
	}
	
	@Override
	public int getIconHeight() {
		return size;
	}
	
	/**
	 * Converts emoji to ImageIcon for compatibility with existing code.
	 * 
	 * @return ImageIcon rendered from emoji
	 */
	public ImageIcon toImageIcon() {
		String cacheKey = emojiChar + "_" + size;
		
		if (emojiCache.containsKey(cacheKey)) {
			return emojiCache.get(cacheKey);
		}
		
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintIcon(null, g2d, 0, 0);
		g2d.dispose();
		
		ImageIcon icon = new ImageIcon(img);
		emojiCache.put(cacheKey, icon);
		return icon;
	}
	
	/**
	 * Clears the emoji cache.
	 * Call this when theme changes.
	 */
	public static void clearCache() {
		emojiCache.clear();
		logger.info("Emoji icon cache cleared");
	}
}
