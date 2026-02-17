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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import freemind.main.FreeMindMain;
import freemind.ui.theme.ThemeManager;

/**
 * Welcome screen dialog that displays on startup with recent files,
 * quick actions, and templates.
 * 
 * @author FreeMind Contributors
 * @since 1.1.0
 */
public class WelcomeScreen extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final FreeMindMain mFreeMindMain;
	private final ThemeManager mThemeManager;
	
	// Callbacks
	private Runnable mOnNewMap;
	private Runnable mOnOpenFile;
	private java.util.function.Consumer<File> mOnOpenRecent;
	
	public WelcomeScreen(FreeMindMain pFreeMindMain) {
		super(pFreeMindMain.getJFrame(), "Welcome to FreeMind", true);
		this.mFreeMindMain = pFreeMindMain;
		this.mThemeManager = ThemeManager.getInstance();
		
		initializeUI();
	}
	
	private void initializeUI() {
		setSize(900, 600);
		setLocationRelativeTo(mFreeMindMain.getJFrame());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		// Main content panel with theme-aware background
		JPanel contentPanel = new JPanel(new BorderLayout(0, 32)) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_WINDOW));
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(48, 48, 48, 48));
		
		// Header with logo and title
		contentPanel.add(createHeader(), BorderLayout.NORTH);
		
		// Center panel with quick actions and recent files
		JPanel centerPanel = new JPanel(new BorderLayout(32, 0));
		centerPanel.setOpaque(false);
		centerPanel.add(createQuickActionsPanel(), BorderLayout.WEST);
		centerPanel.add(createRecentFilesPanel(), BorderLayout.CENTER);
		contentPanel.add(centerPanel, BorderLayout.CENTER);
		
		// Templates panel at bottom
		contentPanel.add(createTemplatesPanel(), BorderLayout.SOUTH);
		
		setContentPane(contentPanel);
	}
	
	private JPanel createHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		
		// Title
		JLabel title = new JLabel("FreeMind", SwingConstants.CENTER);
		title.setFont(new Font("Inter", Font.BOLD, 48));
		title.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
		header.add(title, BorderLayout.CENTER);
		
		// Subtitle
		JLabel subtitle = new JLabel("Visual Thinking & Mind Mapping", SwingConstants.CENTER);
		subtitle.setFont(new Font("Inter", Font.PLAIN, 18));
		subtitle.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_SECONDARY));
		subtitle.setBorder(new EmptyBorder(8, 0, 0, 0));
		header.add(subtitle, BorderLayout.SOUTH);
		
		return header;
	}
	
	private JPanel createQuickActionsPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1, 0, 16));
		panel.setOpaque(false);
		
		// Section label
		JLabel label = createSectionLabel("QUICK ACTIONS");
		panel.add(label);
		
		// New Map card
		ActionCard newMapCard = new ActionCard("➕", "New Map", "Create a blank mind map", 
			() -> { if (mOnNewMap != null) mOnNewMap.run(); dispose(); });
		panel.add(newMapCard);
		
		// Open File card
		ActionCard openFileCard = new ActionCard("📂", "Open File", "Open an existing mind map",
			() -> { if (mOnOpenFile != null) mOnOpenFile.run(); dispose(); });
		panel.add(openFileCard);
		
		return panel;
	}
	
	private JPanel createRecentFilesPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 16));
		panel.setOpaque(false);
		
		// Header with label and clear button
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		
		JLabel label = createSectionLabel("RECENT FILES");
		header.add(label, BorderLayout.WEST);
		
		JLabel clearLabel = new JLabel("Clear");
		clearLabel.setFont(new Font("Inter", Font.PLAIN, 12));
		clearLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY));
		clearLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		clearLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clearRecentFiles();
			}
		});
		header.add(clearLabel, BorderLayout.EAST);
		panel.add(header, BorderLayout.NORTH);
		
		// Recent files list
		JPanel filesPanel = new JPanel(new GridLayout(0, 1, 0, 8));
		filesPanel.setOpaque(false);
		
		List<File> recentFiles = getRecentFiles();
		if (recentFiles.isEmpty()) {
			JLabel emptyLabel = new JLabel("No recent files");
			emptyLabel.setFont(new Font("Inter", Font.PLAIN, 14));
			emptyLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_MUTED));
			filesPanel.add(emptyLabel);
		} else {
			for (File file : recentFiles) {
				RecentFileCard card = new RecentFileCard(file, 
					f -> { if (mOnOpenRecent != null) mOnOpenRecent.accept(f); dispose(); });
				filesPanel.add(card);
			}
		}
		
		JScrollPane scrollPane = new JScrollPane(filesPanel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		panel.add(scrollPane, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel createTemplatesPanel() {
		JPanel panel = new JPanel(new BorderLayout(0, 16));
		panel.setOpaque(false);
		
		JLabel label = createSectionLabel("TEMPLATES");
		panel.add(label, BorderLayout.NORTH);
		
		JPanel templatesGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
		templatesGrid.setOpaque(false);
		
		// Template cards
		templatesGrid.add(new TemplateCard("🎯", "Project Planning", "Project goals and milestones"));
		templatesGrid.add(new TemplateCard("📊", "Meeting Notes", "Meeting agenda and action items"));
		templatesGrid.add(new TemplateCard("💡", "Brainstorming", "Ideas and creative thinking"));
		templatesGrid.add(new TemplateCard("🗓️", "Weekly Planner", "Week schedule and tasks"));
		templatesGrid.add(new TemplateCard("🎓", "Learning Map", "Study topics and resources"));
		
		panel.add(templatesGrid, BorderLayout.CENTER);
		
		return panel;
	}
	
	private JLabel createSectionLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Inter", Font.BOLD, 11));
		label.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_MUTED));
		return label;
	}
	
	private List<File> getRecentFiles() {
		// TODO: Load from FreeMind preferences
		List<File> files = new ArrayList<>();
		// Add sample files for now
		return files;
	}
	
	private void clearRecentFiles() {
		// TODO: Clear recent files from preferences
	}
	
	// Setters for callbacks
	public void setOnNewMap(Runnable callback) {
		this.mOnNewMap = callback;
	}
	
	public void setOnOpenFile(Runnable callback) {
		this.mOnOpenFile = callback;
	}
	
	public void setOnOpenRecent(java.util.function.Consumer<File> callback) {
		this.mOnOpenRecent = callback;
	}
	
	/**
	 * Action card component for quick actions
	 */
	private class ActionCard extends JPanel {
		private static final long serialVersionUID = 1L;
		private boolean isHovered = false;
		
		public ActionCard(String icon, String title, String description, Runnable onClick) {
			setLayout(new BorderLayout(16, 0));
			setOpaque(false);
			setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			setPreferredSize(new Dimension(280, 80));
			
			// Icon
			JLabel iconLabel = new JLabel(icon);
			iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
			iconLabel.setBorder(new EmptyBorder(0, 16, 0, 0));
			add(iconLabel, BorderLayout.WEST);
			
			// Text panel
			JPanel textPanel = new JPanel(new GridLayout(2, 1));
			textPanel.setOpaque(false);
			
			JLabel titleLabel = new JLabel(title);
			titleLabel.setFont(new Font("Inter", Font.BOLD, 16));
			titleLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
			textPanel.add(titleLabel);
			
			JLabel descLabel = new JLabel(description);
			descLabel.setFont(new Font("Inter", Font.PLAIN, 12));
			descLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_SECONDARY));
			textPanel.add(descLabel);
			
			add(textPanel, BorderLayout.CENTER);
			
			// Click handler
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					onClick.run();
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					isHovered = true;
					repaint();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					isHovered = false;
					repaint();
				}
			});
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			// Background
			if (isHovered) {
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_ACTIVE));
			} else {
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_SURFACE));
			}
			g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
			
			// Border
			if (isHovered) {
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_ACCENT_PRIMARY));
			} else {
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BORDER_LIGHT));
			}
			g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
		}
	}
	
	/**
	 * Recent file card component
	 */
	private class RecentFileCard extends JPanel {
		private static final long serialVersionUID = 1L;
		private boolean isHovered = false;
		private final File mFile;
		
		public RecentFileCard(File file, java.util.function.Consumer<File> onClick) {
			this.mFile = file;
			setLayout(new BorderLayout(12, 0));
			setOpaque(false);
			setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			setPreferredSize(new Dimension(0, 48));
			
			// File icon
			JLabel iconLabel = new JLabel("📄");
			iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
			iconLabel.setBorder(new EmptyBorder(0, 12, 0, 0));
			add(iconLabel, BorderLayout.WEST);
			
			// Text panel
			JPanel textPanel = new JPanel(new GridLayout(2, 1));
			textPanel.setOpaque(false);
			
			JLabel nameLabel = new JLabel(file.getName());
			nameLabel.setFont(new Font("Inter", Font.PLAIN, 14));
			nameLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
			textPanel.add(nameLabel);
			
			String timeStr = formatTime(file.lastModified());
			JLabel timeLabel = new JLabel(timeStr);
			timeLabel.setFont(new Font("Inter", Font.PLAIN, 11));
			timeLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_MUTED));
			textPanel.add(timeLabel);
			
			add(textPanel, BorderLayout.CENTER);
			
			// Click handler
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					onClick.accept(file);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					isHovered = true;
					repaint();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					isHovered = false;
					repaint();
				}
			});
		}
		
		private String formatTime(long timestamp) {
			SimpleDateFormat sdf = new SimpleDateFormat("MMM d, h:mm a");
			return sdf.format(new Date(timestamp));
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			if (isHovered) {
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_HOVER));
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
			}
		}
	}
	
	/**
	 * Template card component
	 */
	private class TemplateCard extends JPanel {
		private static final long serialVersionUID = 1L;
		private boolean isHovered = false;
		
		public TemplateCard(String icon, String title, String description) {
			setLayout(new BorderLayout(0, 8));
			setOpaque(false);
			setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			setPreferredSize(new Dimension(140, 120));
			
			// Icon centered
			JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
			iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
			add(iconLabel, BorderLayout.CENTER);
			
			// Text panel at bottom
			JPanel textPanel = new JPanel(new GridLayout(2, 1));
			textPanel.setOpaque(false);
			
			JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
			titleLabel.setFont(new Font("Inter", Font.BOLD, 13));
			titleLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_PRIMARY));
			textPanel.add(titleLabel);
			
			JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
			descLabel.setFont(new Font("Inter", Font.PLAIN, 10));
			descLabel.setForeground(mThemeManager.getColor(ThemeManager.VAR_TEXT_MUTED));
			textPanel.add(descLabel);
			
			add(textPanel, BorderLayout.SOUTH);
			
			// Hover effects
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					isHovered = true;
					repaint();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					isHovered = false;
					repaint();
				}
			});
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			// Background
			if (isHovered) {
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_ACTIVE));
			} else {
				g2d.setColor(mThemeManager.getColor(ThemeManager.VAR_BG_HOVER));
			}
			g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
		}
	}
}
