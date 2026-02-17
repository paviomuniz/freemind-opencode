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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

/**
 * A toggle button for switching between light and dark themes.
 * Features a smooth sliding animation and sun/moon icons.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class ThemeToggleButton extends JComponent {
	
	private static final long serialVersionUID = 1L;
	
	private final ThemeManager themeManager;
	private boolean isDark;
	private float animationProgress = 0.0f;
	private javax.swing.Timer animationTimer;
	
	// Constants
	private static final int WIDTH = 60;
	private static final int HEIGHT = 32;
	private static final int KNOB_SIZE = 26;
	private static final int PADDING = 3;
	
	public ThemeToggleButton() {
		this.themeManager = ThemeManager.getInstance();
		this.isDark = themeManager.isDarkTheme();
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		// Setup animation timer
		animationTimer = new javax.swing.Timer(16, e -> {
			if (isDark) {
				animationProgress += 0.1f;
				if (animationProgress >= 1.0f) {
					animationProgress = 1.0f;
					animationTimer.stop();
				}
			} else {
				animationProgress -= 0.1f;
				if (animationProgress <= 0.0f) {
					animationProgress = 0.0f;
					animationTimer.stop();
				}
			}
			repaint();
		});
		
		// Add click listener
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				toggle();
			}
		});
		
		// Listen for theme changes
		themeManager.setThemeChangeListener((newTheme, variables) -> {
			isDark = themeManager.isDarkTheme();
			animationProgress = isDark ? 1.0f : 0.0f;
			repaint();
		});
	}
	
	private void toggle() {
		isDark = !isDark;
		animationTimer.start();
		themeManager.toggleTheme();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Calculate colors based on animation progress
		Color bgLight = themeManager.getColor(ThemeManager.VAR_BG_HOVER);
		Color bgDark = new Color(0x4A, 0x55, 0x68); // Darker for toggle
		Color bgColor = interpolateColor(bgLight, bgDark, animationProgress);
		
		// Draw background track
		g2d.setColor(bgColor);
		g2d.fillRoundRect(0, 0, WIDTH - 1, HEIGHT - 1, HEIGHT, HEIGHT);
		
		// Draw border
		Color borderColor = themeManager.getColor(ThemeManager.VAR_BORDER_LIGHT);
		g2d.setColor(borderColor);
		g2d.drawRoundRect(0, 0, WIDTH - 1, HEIGHT - 1, HEIGHT, HEIGHT);
		
		// Calculate knob position
		int startX = PADDING;
		int endX = WIDTH - KNOB_SIZE - PADDING;
		int knobX = startX + (int)((endX - startX) * animationProgress);
		int knobY = (HEIGHT - KNOB_SIZE) / 2;
		
		// Draw knob
		Color knobLight = Color.WHITE;
		Color knobDark = new Color(0x2D, 0x37, 0x48);
		Color knobColor = interpolateColor(knobLight, knobDark, animationProgress);
		g2d.setColor(knobColor);
		g2d.fillOval(knobX, knobY, KNOB_SIZE, KNOB_SIZE);
		
		// Draw icon
		g2d.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 14));
		String icon = animationProgress < 0.5f ? "☀️" : "🌙";
		java.awt.FontMetrics fm = g2d.getFontMetrics();
		int iconX = knobX + (KNOB_SIZE - fm.stringWidth(icon)) / 2;
		int iconY = knobY + (KNOB_SIZE + fm.getAscent() - fm.getDescent()) / 2 - 1;
		g2d.drawString(icon, iconX, iconY);
	}
	
	/**
	 * Interpolate between two colors
	 */
	private Color interpolateColor(Color c1, Color c2, float ratio) {
		int r = (int)(c1.getRed() + (c2.getRed() - c1.getRed()) * ratio);
		int g = (int)(c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
		int b = (int)(c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio);
		return new Color(r, g, b);
	}
	
	/**
	 * Get current theme state
	 */
	public boolean isDarkTheme() {
		return isDark;
	}
	
	/**
	 * Set theme state programmatically
	 */
	public void setDarkTheme(boolean dark) {
		if (this.isDark != dark) {
			toggle();
		}
	}
}
