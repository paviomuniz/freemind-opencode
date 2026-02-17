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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import freemind.ui.theme.ThemeManager;
import freemind.view.mindmapview.NodeView;

/**
 * Ghost node that follows the cursor during drag operations to provide
 * visual feedback of the node being dragged.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class DragGhost extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final ThemeManager mThemeManager;
	private BufferedImage mGhostImage;
	private String mNodeText;
	private Dimension mNodeSize;
	private Point mOffset;
	
	public DragGhost() {
		this.mThemeManager = ThemeManager.getInstance();
		setOpaque(false);
		setVisible(false);
	}
	
	/**
	 * Start dragging with a node view
	 */
	public void startDrag(NodeView nodeView, Point dragStart) {
		if (nodeView == null) return;
		
		// Capture node appearance
		mNodeText = nodeView.getModel().getText();
		mNodeSize = new Dimension(nodeView.getWidth(), nodeView.getHeight());
		
		// Calculate offset from mouse to node corner
		Point nodeLoc = SwingUtilities.convertPoint(nodeView.getParent(), 
			nodeView.getLocation(), this);
		mOffset = new Point(nodeLoc.x - dragStart.x, nodeLoc.y - dragStart.y);
		
		// Create ghost image
		createGhostImage();
		
		// Position and show
		setBounds(nodeLoc.x, nodeLoc.y, mNodeSize.width, mNodeSize.height);
		setVisible(true);
	}
	
	/**
	 * Update ghost position during drag
	 */
	public void updatePosition(Point location) {
		if (!isVisible()) return;
		
		int x = location.x + mOffset.x;
		int y = location.y + mOffset.y;
		setLocation(x, y);
		repaint();
	}
	
	/**
	 * End drag operation
	 */
	public void endDrag() {
		setVisible(false);
		mGhostImage = null;
	}
	
	private void createGhostImage() {
		mGhostImage = new BufferedImage(mNodeSize.width, mNodeSize.height, 
			BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = mGhostImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Draw semi-transparent background
		Color bgColor = mThemeManager.getColor(ThemeManager.VAR_BG_SURFACE);
		g2d.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), 
			bgColor.getBlue(), 180));
		g2d.fillRoundRect(0, 0, mNodeSize.width - 1, mNodeSize.height - 1, 16, 16);
		
		// Draw border
		Color accentColor = mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY);
		g2d.setColor(accentColor);
		g2d.drawRoundRect(0, 0, mNodeSize.width - 1, mNodeSize.height - 1, 16, 16);
		
		// Draw text (truncated if needed)
		g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
		g2d.setFont(getFont());
		
		String displayText = mNodeText;
		if (displayText.length() > 20) {
			displayText = displayText.substring(0, 17) + "...";
		}
		
		// Center text
		java.awt.FontMetrics fm = g2d.getFontMetrics();
		int textX = (mNodeSize.width - fm.stringWidth(displayText)) / 2;
		int textY = (mNodeSize.height + fm.getAscent() - fm.getDescent()) / 2;
		g2d.drawString(displayText, Math.max(4, textX), textY);
		
		g2d.dispose();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (mGhostImage == null) return;
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g2d.drawImage(mGhostImage, 0, 0, null);
	}
}
