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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.Border;

/**
 * Factory class for creating themed UI components that automatically
 * update their appearance when the theme changes.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class ThemedComponentFactory {
	
	private static final ThemeManager themeManager = ThemeManager.getInstance();
	
	/**
	 * Create a themed toolbar following the modern design
	 */
	public static JToolBar createThemedToolBar() {
		JToolBar toolBar = new JToolBar() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(themeManager.getColor(ThemeManager.VAR_BG_SURFACE));
				g2d.fillRect(0, 0, getWidth(), getHeight());
				
				// Draw bottom border
				g2d.setColor(themeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
				g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
			}
		};
		
		toolBar.setFloatable(false);
		toolBar.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
		return toolBar;
	}
	
	/**
	 * Create a themed button group container
	 */
	public static JPanel createButtonGroup() {
		JPanel group = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(themeManager.getColor(ThemeManager.VAR_BG_HOVER));
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
			}
		};
		group.setOpaque(false);
		return group;
	}
	
	/**
	 * Create a themed toolbar button
	 */
	public static JButton createToolBarButton(String icon, String tooltip) {
		JButton button = new JButton(icon) {
			private boolean isHovered = false;
			private boolean isPressed = false;
			
			{
				addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseEntered(java.awt.event.MouseEvent e) {
						isHovered = true;
						repaint();
					}
					
					@Override
					public void mouseExited(java.awt.event.MouseEvent e) {
						isHovered = false;
						repaint();
					}
					
					@Override
					public void mousePressed(java.awt.event.MouseEvent e) {
						isPressed = true;
						repaint();
					}
					
					@Override
					public void mouseReleased(java.awt.event.MouseEvent e) {
						isPressed = false;
						repaint();
					}
				});
			}
			
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Background
				if (isPressed) {
					g2d.setColor(themeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY));
				} else if (isHovered) {
					g2d.setColor(themeManager.getColor(ThemeManager.VAR_BG_ACTIVE));
				} else {
					g2d.setColor(themeManager.getColor(ThemeManager.VAR_BG_SURFACE));
				}
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
				
				// Border
				g2d.setColor(themeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
				g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
				
				// Icon/text
				if (isPressed) {
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setColor(themeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
				}
				
				// Center text
				java.awt.FontMetrics fm = g2d.getFontMetrics();
				int x = (getWidth() - fm.stringWidth(getText())) / 2;
				int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
				g2d.drawString(getText(), x, y);
			}
		};
		
		button.setToolTipText(tooltip);
		button.setPreferredSize(new java.awt.Dimension(32, 32));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		
		return button;
	}
	
	/**
	 * Create a themed panel
	 */
	public static JPanel createThemedPanel(String bgVariable) {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(themeManager.getColor(bgVariable));
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panel.setOpaque(false);
		return panel;
	}
	
	/**
	 * Create a themed sidebar panel
	 */
	public static JPanel createSidebarPanel() {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(themeManager.getColor(ThemeManager.VAR_BG_SURFACE));
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panel.setOpaque(false);
		
		// Add border
		Border border = BorderFactory.createMatteBorder(0, 0, 0, 1, 
			themeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
		panel.setBorder(BorderFactory.createCompoundBorder(
			border,
			BorderFactory.createEmptyBorder(16, 16, 16, 16)
		));
		
		return panel;
	}
	
	/**
	 * Create section header label
	 */
	public static javax.swing.JLabel createSectionHeader(String text) {
		javax.swing.JLabel label = new javax.swing.JLabel(text);
		label.setFont(new java.awt.Font("Inter", java.awt.Font.BOLD, 11));
		label.setForeground(themeManager.getColor(ThemeManager.VAR_TEXT_MUTED));
		
		// Store original color to update on theme change
		label.putClientProperty("themeColor", ThemeManager.VAR_TEXT_MUTED);
		
		return label;
	}
	
	/**
	 * Create a themed card panel
	 */
	public static JPanel createCardPanel() {
		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(themeManager.getColor(ThemeManager.VAR_BG_HOVER));
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
			}
		};
		card.setOpaque(false);
		return card;
	}
}
