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
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.plaf.basic.BasicComboBoxUI;

import freemind.controller.Controller;
import freemind.main.Resources;
import freemind.main.Tools;
import freemind.modes.mindmapmode.MindMapController;
import freemind.ui.theme.ThemeManager;
import freemind.ui.theme.ThemeToggleButton;

/**
 * Modern toolbar implementation with grouped actions, font controls,
 * zoom slider, and theme toggle.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class ModernToolBar extends JToolBar {
	
	private static final long serialVersionUID = 1L;
	
	private final Controller mController;
	private final ThemeManager mThemeManager;
	
	// Action buttons
	private JButton mNewNodeBtn;
	private JButton mCutBtn;
	private JButton mCopyBtn;
	private JButton mPasteBtn;
	private JButton mUndoBtn;
	private JButton mRedoBtn;
	
	// Font controls
	private JComboBox<String> mFontFamilyCombo;
	private JComboBox<String> mFontSizeCombo;
	private JButton mBoldBtn;
	private JButton mItalicBtn;
	
	// Zoom control
	private JLabel mZoomLabel;
	private JButton mZoomOutBtn;
	private JButton mZoomInBtn;
	
	public ModernToolBar(Controller pController) {
		this.mController = pController;
		this.mThemeManager = ThemeManager.getInstance();
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
		setFloatable(false);
		setMinimumSize(new Dimension(400, 56));
		setPreferredSize(new Dimension(800, 56));
		setMaximumSize(new Dimension(Short.MAX_VALUE, 56));
		setBorder(BorderFactory.createEmptyBorder());
		setOpaque(true);
		setBackground(mThemeManager.getColor(ThemeManager.VAR_BG_SURFACE));
		
		initializeComponents();
	}
	
	private void initializeComponents() {
		// Create buttons first
		mNewNodeBtn = createIconButton("➕", "Add new child node", e -> addChildNode());
		mCutBtn = createIconButton("✂️", "Cut selected node", e -> cutNode());
		mCopyBtn = createIconButton("📋", "Copy selected node", e -> copyNode());
		mPasteBtn = createIconButton("📄", "Paste", e -> pasteNode());
		mUndoBtn = createIconButton("↩️", "Undo", e -> undo());
		mRedoBtn = createIconButton("↪️", "Redo", e -> redo());
		
		// Edit group
		add(createButtonGroup(new JButton[] { mNewNodeBtn, mCutBtn, mCopyBtn, mPasteBtn }));
		
		add(createSeparator());
		
		// Undo/Redo group
		add(createButtonGroup(new JButton[] { mUndoBtn, mRedoBtn }));
		
		add(createSeparator());
		
		// Font controls group
		add(createFontControlsGroup());
		
		add(createSeparator());
		
		// Zoom control
		add(createZoomControl());
		
		// Flexible space
		JPanel spacer = new JPanel();
		spacer.setOpaque(false);
		spacer.setPreferredSize(new Dimension(16, 32));
		add(spacer);
		
		// Theme toggle
		ThemeToggleButton themeToggle = new ThemeToggleButton();
		add(themeToggle);
	}
	
	private JPanel createButtonGroup(JButton[] buttons) {
		JPanel group = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_HOVER));
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
			}
		};
		group.setOpaque(false);
		group.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		for (JButton btn : buttons) {
			group.add(btn);
		}
		
		return group;
	}
	
	private JButton createIconButton(String icon, String tooltip, ActionListener listener) {
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
					g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY));
				} else if (isHovered) {
					g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_ACTIVE));
				} else {
					g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_SURFACE));
				}
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
				
				// Border
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
				g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
				
				// Icon
				if (isPressed) {
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
				}
				
				java.awt.FontMetrics fm = g2d.getFontMetrics();
				int x = (getWidth() - fm.stringWidth(getText())) / 2;
				int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
				g2d.drawString(getText(), x, y);
			}
		};
		
		button.setToolTipText(tooltip);
		button.setPreferredSize(new Dimension(32, 32));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setFont(new java.awt.Font("Segoe UI Emoji", java.awt.Font.PLAIN, 16));
		button.addActionListener(listener);
		
		return button;
	}
	
	private JPanel createFontControlsGroup() {
		JPanel group = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		group.setOpaque(false);
		
		// Font family dropdown
		mFontFamilyCombo = createStyledComboBox(new String[] {
			"Arial", "Helvetica", "Times New Roman", "Courier New", "Verdana"
		});
		mFontFamilyCombo.setPreferredSize(new Dimension(120, 32));
		mFontFamilyCombo.setSelectedItem("Arial");
		group.add(mFontFamilyCombo);
		
		// Font size dropdown
		mFontSizeCombo = createStyledComboBox(new String[] {
			"8", "9", "10", "11", "12", "14", "16", "18", "20", "24", "28", "32"
		});
		mFontSizeCombo.setPreferredSize(new Dimension(60, 32));
		mFontSizeCombo.setSelectedItem("14");
		group.add(mFontSizeCombo);
		
		// Bold button
		mBoldBtn = createIconButton("B", "Bold", e -> toggleBold());
		mBoldBtn.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
		group.add(mBoldBtn);
		
		return group;
	}
	
	private JComboBox<String> createStyledComboBox(String[] items) {
		JComboBox<String> combo = new JComboBox<>(items);
		combo.setUI(new BasicComboBoxUI() {
			@Override
			protected JButton createArrowButton() {
				JButton button = new JButton("▼");
				button.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 8));
				button.setContentAreaFilled(false);
				button.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
				button.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_MUTED));
				return button;
			}
		});
		combo.setBackground(mThemeManager.getColor(ThemeManager.VAR_BG_SURFACE));
		combo.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
		combo.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(mThemeManager.getColor(ThemeManager.VAR_BORDER_LIGHT)),
			BorderFactory.createEmptyBorder(4, 8, 4, 4)
		));
		return combo;
	}
	
	private JPanel createZoomControl() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_HOVER));
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
			}
		};
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
		
		mZoomOutBtn = createZoomButton("−");
		mZoomOutBtn.setToolTipText("Zoom out");
		mZoomOutBtn.addActionListener(e -> zoomOut());
		panel.add(mZoomOutBtn);
		
		mZoomLabel = new JLabel("100%");
		mZoomLabel.setFont(new java.awt.Font("Inter", java.awt.Font.PLAIN, 13));
		mZoomLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
		mZoomLabel.setPreferredSize(new Dimension(48, 24));
		mZoomLabel.setHorizontalAlignment(JLabel.CENTER);
		panel.add(mZoomLabel);
		
		mZoomInBtn = createZoomButton("+");
		mZoomInBtn.setToolTipText("Zoom in");
		mZoomInBtn.addActionListener(e -> zoomIn());
		panel.add(mZoomInBtn);
		
		return panel;
	}
	
	private JButton createZoomButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
		button.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_SECONDARY));
		button.setPreferredSize(new Dimension(24, 28));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		return button;
	}
	
	private JSeparator createSeparator() {
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		sep.setPreferredSize(new Dimension(1, 24));
		sep.setForeground(mThemeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
		sep.setBackground(mThemeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
		return sep;
	}
	
	// Action handlers
	private void addChildNode() {
		if (mController.getModeController() instanceof MindMapController) {
			MindMapController mmc = (MindMapController) mController.getModeController();
			// Add new node as child of selected node
			if (mmc.getSelected() != null) {
				mmc.addNewNode(mmc.getSelected(), mmc.getSelected().getChildCount(), false);
			}
		}
	}
	
	private void cutNode() {
		if (mController.getModeController() instanceof MindMapController) {
			((MindMapController) mController.getModeController()).cut();
		}
	}
	
	private void copyNode() {
		if (mController.getModeController() instanceof MindMapController) {
			((MindMapController) mController.getModeController()).copy();
		}
	}
	
	private void pasteNode() {
		if (mController.getModeController() instanceof MindMapController) {
			MindMapController mmc = (MindMapController) mController.getModeController();
			if (mmc.getSelected() != null) {
				// Paste as child of selected node
				mmc.paste(mmc.getSelected(), mmc.getSelected());
			}
		}
	}
	
	private void undo() {
		if (mController.getModeController() instanceof MindMapController) {
			MindMapController mmc = (MindMapController) mController.getModeController();
			if (mmc.undo != null && mmc.undo.isEnabled()) {
				mmc.undo.actionPerformed(null);
			}
		}
	}
	
	private void redo() {
		if (mController.getModeController() instanceof MindMapController) {
			MindMapController mmc = (MindMapController) mController.getModeController();
			if (mmc.redo != null && mmc.redo.isEnabled()) {
				mmc.redo.actionPerformed(null);
			}
		}
	}
	
	private void toggleBold() {
		// TODO: Implement bold toggle
	}
	
	private void zoomIn() {
		// TODO: Implement zoom in
	}
	
	private void zoomOut() {
		// TODO: Implement zoom out
	}
	
	/**
	 * Update zoom label display
	 */
	public void setZoomLabel(String zoom) {
		mZoomLabel.setText(zoom);
	}
	
	/**
	 * Activate/deactivate toolbar - called when switching modes
	 */
	public void activate(boolean visible) {
		// No-op for now - toolbar is always visible
		setVisible(visible);
	}
	
	/**
	 * Enable/disable all actions - called when no map is open
	 */
	public void setAllActions(boolean enabled) {
		// Enable/disable action buttons
		mNewNodeBtn.setEnabled(enabled);
		mCutBtn.setEnabled(enabled);
		mCopyBtn.setEnabled(enabled);
		mPasteBtn.setEnabled(enabled);
		mUndoBtn.setEnabled(enabled);
		mRedoBtn.setEnabled(enabled);
		mFontFamilyCombo.setEnabled(enabled);
		mFontSizeCombo.setEnabled(enabled);
		mBoldBtn.setEnabled(enabled);
	}
}
