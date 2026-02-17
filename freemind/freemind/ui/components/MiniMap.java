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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import freemind.modes.MindMapNode;
import freemind.ui.theme.ThemeManager;
import freemind.view.MapModule;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

/**
 * Mini-map widget that displays an overview of the entire mind map
 * with a draggable viewport indicator for navigation.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class MiniMap extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_WIDTH = 160;
	private static final int DEFAULT_HEIGHT = 120;
	private static final int MINI_NODE_SIZE = 4;
	
	private final ThemeManager mThemeManager;
	private MapView mMapView;
	private JScrollPane mScrollPane;
	
	// Viewport indicator
	private Rectangle mViewportRect;
	private boolean mIsDragging = false;
	private Point mDragStart;
	private Point mViewportStart;
	
	// Scale factors
	private double mScaleX = 1.0;
	private double mScaleY = 1.0;
	
	public MiniMap() {
		this.mThemeManager = ThemeManager.getInstance();
		
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setOpaque(false);
		
		mViewportRect = new Rectangle();
		
		// Add mouse listeners for dragging
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (mViewportRect.contains(e.getPoint())) {
					mIsDragging = true;
					mDragStart = e.getPoint();
					mViewportStart = new Point(mViewportRect.x, mViewportRect.y);
				} else {
					// Click to jump
					jumpToPoint(e.getPoint());
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mIsDragging = false;
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (mIsDragging && mScrollPane != null) {
					dragViewport(e.getPoint());
				}
			}
		});
	}
	
	/**
	 * Set the map view to display in the mini-map
	 */
	public void setMapView(MapView mapView, JScrollPane scrollPane) {
		this.mMapView = mapView;
		this.mScrollPane = scrollPane;
		updateScale();
		repaint();
	}
	
	/**
	 * Update the mini-map display
	 */
	public void update() {
		if (mMapView != null) {
			updateScale();
			updateViewportRect();
			repaint();
		}
	}
	
	private void updateScale() {
		if (mMapView == null) return;
		
		Rectangle mapBounds = mMapView.getInnerBounds();
		if (mapBounds.width > 0 && mapBounds.height > 0) {
			mScaleX = (double) (getWidth() - 8) / mapBounds.width;
			mScaleY = (double) (getHeight() - 8) / mapBounds.height;
			// Use uniform scale to maintain aspect ratio
			mScaleX = Math.min(mScaleX, mScaleY);
			mScaleY = mScaleX;
		}
	}
	
	private void updateViewportRect() {
		if (mScrollPane == null || mMapView == null) return;
		
		Rectangle viewRect = mScrollPane.getViewport().getViewRect();
		Rectangle mapBounds = mMapView.getInnerBounds();
		
		// Scale viewport rectangle
		mViewportRect.x = (int) ((viewRect.x - mapBounds.x) * mScaleX) + 4;
		mViewportRect.y = (int) ((viewRect.y - mapBounds.y) * mScaleY) + 4;
		mViewportRect.width = (int) (viewRect.width * mScaleX);
		mViewportRect.height = (int) (viewRect.height * mScaleY);
		
		// Constrain to bounds
		mViewportRect.width = Math.min(mViewportRect.width, getWidth() - 8);
		mViewportRect.height = Math.min(mViewportRect.height, getHeight() - 8);
	}
	
	private void dragViewport(Point point) {
		int dx = point.x - mDragStart.x;
		int dy = point.y - mDragStart.y;
		
		// Calculate new viewport position
		int newX = mViewportStart.x + dx;
		int newY = mViewportStart.y + dy;
		
		// Convert back to map coordinates
		Rectangle mapBounds = mMapView.getInnerBounds();
		int mapX = (int) ((newX - 4) / mScaleX) + mapBounds.x;
		int mapY = (int) ((newY - 4) / mScaleY) + mapBounds.y;
		
		// Scroll the map view
		mScrollPane.getViewport().setViewPosition(new Point(mapX, mapY));
	}
	
	private void jumpToPoint(Point point) {
		if (mScrollPane == null || mMapView == null) return;
		
		Rectangle mapBounds = mMapView.getInnerBounds();
		int mapX = (int) ((point.x - 4) / mScaleX) + mapBounds.x;
		int mapY = (int) ((point.y - 4) / mScaleY) + mapBounds.y;
		
		// Center viewport on clicked point
		Rectangle viewRect = mScrollPane.getViewport().getViewRect();
		mapX -= viewRect.width / 2;
		mapY -= viewRect.height / 2;
		
		mScrollPane.getViewport().setViewPosition(new Point(mapX, mapY));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Background
		g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_SURFACE));
		g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
		
		// Border
		g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
		g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
		
		if (mMapView == null) return;
		
		// Draw map content area
		g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_CANVAS));
		g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 4, 4);
		
		// Draw nodes
		paintNodes(g2d);
		
		// Draw viewport indicator
		paintViewportIndicator(g2d);
	}
	
	private void paintNodes(Graphics2D g2d) {
		if (mMapView == null || mMapView.getRoot() == null) return;
		
		Rectangle mapBounds = mMapView.getInnerBounds();
		
		// Traverse node tree starting from root
		paintNodeRecursive(g2d, mMapView.getRoot(), mapBounds);
	}
	
	private void paintNodeRecursive(Graphics2D g2d, NodeView nodeView, Rectangle mapBounds) {
		if (nodeView == null) return;
		
		MindMapNode node = nodeView.getModel();
		if (node != null) {
			// Get node position relative to map bounds
			int nodeX = nodeView.getX() - mapBounds.x;
			int nodeY = nodeView.getY() - mapBounds.y;
			int nodeW = nodeView.getWidth();
			int nodeH = nodeView.getHeight();
			
			// Scale to mini-map coordinates
			int miniX = (int) (nodeX * mScaleX) + 4;
			int miniY = (int) (nodeY * mScaleY) + 4;
			int miniW = Math.max(MINI_NODE_SIZE, (int) (nodeW * mScaleX));
			int miniH = Math.max(MINI_NODE_SIZE, (int) (nodeH * mScaleY));
			
			// Draw node rectangle
			Color nodeColor = nodeView.isSelected() 
				? mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY)
				: mThemeManager.getColor(ThemeManager.VAR_TEXT_SECONDARY);
			
			g2d.setColor(nodeColor);
			g2d.fillRect(miniX, miniY, miniW, miniH);
		}
		
		// Recursively paint children
		for (int i = 0; i < nodeView.getComponentCount(); i++) {
			java.awt.Component child = nodeView.getComponent(i);
			if (child instanceof NodeView) {
				paintNodeRecursive(g2d, (NodeView) child, mapBounds);
			}
		}
	}
	
	private void paintViewportIndicator(Graphics2D g2d) {
		// Semi-transparent fill
		Color fillColor = new Color(
			mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY).getRed(),
			mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY).getGreen(),
			mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY).getBlue(),
			40
		);
		g2d.setColor(fillColor);
		g2d.fillRect(mViewportRect.x, mViewportRect.y, 
			mViewportRect.width, mViewportRect.height);
		
		// Border
		g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY));
		g2d.drawRect(mViewportRect.x, mViewportRect.y, 
			mViewportRect.width - 1, mViewportRect.height - 1);
	}
}
