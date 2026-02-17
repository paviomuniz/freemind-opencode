/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2007  Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

package freemind.view.mindmapview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;

import freemind.main.Tools;
import freemind.modes.MindMapNode;
import freemind.ui.theme.ThemeManager;

@SuppressWarnings("serial")
class BubbleMainView extends MainView {
	final static Stroke DEF_STROKE = new BasicStroke();

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView.MainView#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		Dimension prefSize = super.getPreferredSize();
		prefSize.width += getNodeView().getMap().getZoomed(5);
		return prefSize;
	}

	public void paint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		final NodeView nodeView = getNodeView();
		final MindMapNode model = nodeView.getModel();
		if (model == null)
			return;

		Object renderingHint = getNodeView().getMap().setEdgesRenderingHint(g);
		
		// Enable anti-aliasing for smooth rendering
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		paintSelected(g);
		paintDragOver(g);

		// Draw a modern styled node with shadow and rounded corners
		Color edgeColor = model.getEdge().getColor();
		int arcWidth = getNodeView().getMap().getZoomed(16);
		int arcHeight = getNodeView().getMap().getZoomed(16);
		
		// Draw shadow for modern look
		if (!isCurrentlyPrinting()) {
			drawShadow(g, arcWidth, arcHeight);
		}
		
		// Draw border with theme-aware colors
		g.setStroke(DEF_STROKE);
		g.setColor(edgeColor);
		g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arcWidth, arcHeight);
		
		Tools.restoreAntialiasing(g, renderingHint);
		super.paint(g);
	}
	
	/**
	 * Draw a subtle shadow under the node for modern look
	 */
	private void drawShadow(Graphics2D g, int arcWidth, int arcHeight) {
		ThemeManager themeManager = ThemeManager.getInstance();
		Color shadowColor = themeManager.isDarkTheme() 
			? new Color(0, 0, 0, 60) 
			: new Color(0, 0, 0, 40);
		
		int shadowOffset = getNodeView().getMap().getZoomed(2);
		g.setColor(shadowColor);
		g.fillRoundRect(
			shadowOffset, 
			shadowOffset, 
			getWidth() - 3, 
			getHeight() - 3, 
			arcWidth, 
			arcHeight
		);
	}

	public void paintSelected(Graphics2D graphics) {
		super.paintSelected(graphics);
		if (getNodeView().useSelectionColors()) {
			ThemeManager themeManager = ThemeManager.getInstance();
			Color selectionColor = themeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY);
			
			// Draw selection with glow effect
			int arcWidth = getNodeView().getMap().getZoomed(16);
			int arcHeight = getNodeView().getMap().getZoomed(16);
			
			// Outer glow
			Color glowColor = new Color(
				selectionColor.getRed(),
				selectionColor.getGreen(),
				selectionColor.getBlue(),
				60
			);
			graphics.setColor(glowColor);
			graphics.fillRoundRect(-2, -2, getWidth() + 3, getHeight() + 3, arcWidth + 4, arcHeight + 4);
			
			// Inner selection fill
			graphics.setColor(new Color(
				selectionColor.getRed(),
				selectionColor.getGreen(),				selectionColor.getBlue(),
				30
			));
			graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
			
			// Selection border
			graphics.setColor(selectionColor);
			graphics.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
		}
	}

	protected void paintBackground(Graphics2D graphics, Color color) {
		ThemeManager themeManager = ThemeManager.getInstance();
		int arcWidth = getNodeView().getMap().getZoomed(16);
		int arcHeight = getNodeView().getMap().getZoomed(16);
		
		// Use theme-aware background color if node has no specific color
		Color fillColor = color;
		if (fillColor == null || fillColor.equals(Color.WHITE)) {
			fillColor = themeManager.getColor(ThemeManager.VAR_BG_SURFACE);
		}
		
		graphics.setColor(fillColor);
		graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
	}

	Point getLeftPoint() {
		Point in = new Point(0, getHeight() / 2);
		return in;
	}

	Point getCenterPoint() {
		Point in = getLeftPoint();
		in.x = getWidth() / 2;
		return in;
	}

	Point getRightPoint() {
		Point in = getLeftPoint();
		in.x = getWidth() - 1;
		return in;
	}

	protected int getMainViewWidthWithFoldingMark() {
		int width = getWidth();
		int dW = getZoomedFoldingSymbolHalfWidth() * 2;
		if (getNodeView().getModel().isFolded()) {
			width += dW;
		}
		return width + dW;
	}

	public int getDeltaX() {
		if (getNodeView().getModel().isFolded() && getNodeView().isLeft()) {
			return super.getDeltaX() + getZoomedFoldingSymbolHalfWidth() * 2;
		}
		return super.getDeltaX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView#getStyle()
	 */
	String getStyle() {
		return MindMapNode.STYLE_BUBBLE;
	}

	/**
	 * Returns the relative position of the Edge
	 */
	int getAlignment() {
		return NodeView.ALIGN_CENTER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView#getTextWidth()
	 */
	public int getTextWidth() {
		return super.getTextWidth() + getNodeView().getMap().getZoomed(5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.view.mindmapview.NodeView#getTextX()
	 */
	public int getTextX() {
		return super.getTextX() + getNodeView().getMap().getZoomed(2);
	}

}