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
package freemind.ui.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JPanel;

import freemind.ui.theme.ThemeManager;

/**
 * Drop indicator that shows where a node will be dropped during drag operations.
 * Displays as a horizontal line for sibling drops or a highlight for child drops.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class DropIndicator extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public enum DropType {
		NONE,
		AS_CHILD,      // Drop as child of target
		AS_SIBLING_BEFORE,  // Drop as sibling before target
		AS_SIBLING_AFTER    // Drop as sibling after target
	}
	
	private final ThemeManager mThemeManager;
	private DropType mDropType = DropType.NONE;
	
	private static final int LINE_THICKNESS = 3;
	private static final int INDICATOR_SIZE = 8;
	
	public DropIndicator() {
		this.mThemeManager = ThemeManager.getInstance();
		setOpaque(false);
		setVisible(false);
	}
	
	/**
	 * Show indicator at position with specified drop type
	 */
	public void showAt(Point location, Dimension size, DropType dropType) {
		this.mDropType = dropType;
		
		switch (dropType) {
		case AS_CHILD:
			// Highlight entire node area
			setBounds(location.x - 4, location.y - 4, 
				size.width + 8, size.height + 8);
			break;
		case AS_SIBLING_BEFORE:
			// Line above node
			setBounds(location.x, location.y - LINE_THICKNESS - 2, 
				size.width, LINE_THICKNESS + 4);
			break;
		case AS_SIBLING_AFTER:
			// Line below node
			setBounds(location.x, location.y + size.height - 2, 
				size.width, LINE_THICKNESS + 4);
			break;
		default:
			setVisible(false);
			return;
		}
		
		setVisible(true);
		repaint();
	}
	
	/**
	 * Hide the indicator
	 */
	public void hide() {
		mDropType = DropType.NONE;
		setVisible(false);
	}
	
	/**
	 * Get current drop type
	 */
	public DropType getDropType() {
		return mDropType;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (mDropType == DropType.NONE) return;
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Color accentColor = mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY);
		
		switch (mDropType) {
		case AS_CHILD:
			paintChildIndicator(g2d, accentColor);
			break;
		case AS_SIBLING_BEFORE:
		case AS_SIBLING_AFTER:
			paintSiblingIndicator(g2d, accentColor);
			break;
		default:
			break;
		}
	}
	
	private void paintChildIndicator(Graphics2D g2d, Color color) {
		// Draw dashed border around drop area
		Stroke dashed = new BasicStroke(2, BasicStroke.CAP_ROUND, 
			BasicStroke.JOIN_ROUND, 0, new float[]{5, 5}, 0);
		g2d.setStroke(dashed);
		g2d.setColor(color);
		g2d.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 16, 16);
		
		// Fill with semi-transparent color
		Color fillColor = new Color(color.getRed(), color.getGreen(), 
			color.getBlue(), 30);
		g2d.setColor(fillColor);
		g2d.fillRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 16, 16);
	}
	
	private void paintSiblingIndicator(Graphics2D g2d, Color color) {
		// Draw thick line with arrowheads
		int y = getHeight() / 2;
		
		// Main line
		g2d.setStroke(new BasicStroke(LINE_THICKNESS, BasicStroke.CAP_ROUND, 
			BasicStroke.JOIN_ROUND));
		g2d.setColor(color);
		g2d.drawLine(INDICATOR_SIZE, y, getWidth() - INDICATOR_SIZE, y);
		
		// Left arrowhead
		g2d.drawLine(INDICATOR_SIZE, y, INDICATOR_SIZE + 6, y - 4);
		g2d.drawLine(INDICATOR_SIZE, y, INDICATOR_SIZE + 6, y + 4);
		
		// Right arrowhead
		g2d.drawLine(getWidth() - INDICATOR_SIZE, y, 
			getWidth() - INDICATOR_SIZE - 6, y - 4);
		g2d.drawLine(getWidth() - INDICATOR_SIZE, y, 
			getWidth() - INDICATOR_SIZE - 6, y + 4);
		
		// Glow effect
		Color glowColor = new Color(color.getRed(), color.getGreen(), 
			color.getBlue(), 60);
		g2d.setStroke(new BasicStroke(LINE_THICKNESS + 4));
		g2d.setColor(glowColor);
		g2d.drawLine(INDICATOR_SIZE, y, getWidth() - INDICATOR_SIZE, y);
	}
}
