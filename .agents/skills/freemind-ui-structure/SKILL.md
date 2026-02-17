---
name: freemind-ui-structure
description: Reference for FreeMind mind-mapping application UI structure and components. Use when making changes to the FreeMind user interface, modifying main window components, toolbars, panels, or understanding the Swing component hierarchy. Provides class names, file paths, and component relationships.
---

# FreeMind UI Structure Reference

This skill provides comprehensive reference for FreeMind's UI structure to facilitate modifications and enhancements.

## Quick Reference

Use these names when discussing UI components:

| Component | Class | File Path | Description |
|-----------|-------|-----------|-------------|
| Main Window | `FreeMind` | `freemind/main/FreeMind.java` | JFrame - primary application window |
| Map View Area | `MapView` | `freemind/view/mindmapview/MapView.java` | JPanel - displays the mind map canvas |
| Main Scroll Pane | `MapView.ScrollPane` (JScrollPane) | `freemind/view/mindmapview/MapView.java:115` | Wraps the MapView for scrolling |
| Vertical Toolbar | `MindMapToolBar` | `freemind/modes/mindmapmode/MindMapToolBar.java` | Left-side vertical toolbar with icons |
| Top Toolbar | `FreeMindToolBar` / mode-specific toolbars | `freemind/controller/` | Horizontal toolbar at top |
| Note Panel | `NodeNoteViewer` | `freemind/modes/browsemode/NodeNoteViewer.java` | Bottom panel for node notes |
| Main Split Pane | `JSplitPane` (mSplitPane) | `freemind/main/FreeMind.java:277` | Splits main area from bottom panel |
| Optional Split | `JOptionalSplitPane` | `freemind/controller/Controller.java` | Manages note/attribute panels |

## Component Hierarchy

```
FreeMind (JFrame)
├── MenuBar (top)
├── Content Area (BorderLayout.CENTER)
│   ├── TabbedPane (optional) or Single View
│   │   └── Content Component
│   │       ├── mScrollPane (JScrollPane)
│   │       │   └── MapView (JPanel) - the mind map
│   │       └── OR mSplitPane (JSplitPane)
│   │           ├── mScrollPane (top/left)
│   │           └── Component (bottom/right) - note panel, etc.
│   └── Vertical Toolbar (left side)
├── Status Bar (BorderLayout.SOUTH)
└── Controller manages toolbars
```

## Key Classes and Files

### Main Window
- **`FreeMind`** - `freemind/main/FreeMind.java` - Main JFrame, contains all UI
  - `mScrollPane` (line 275) - JScrollPane containing the MapView
  - `mSplitPane` (line 277) - JSplitPane for optional bottom panel
  - `mContentComponent` (line 279) - Current content (either scroll pane or split pane)

### View Components
- **`MapView`** - `freemind/view/mindmapview/MapView.java` - The mind map canvas
  - Extends JPanel
  - Contains NodeView components
  - Inner class `ScrollPane` extends JScrollPane

- **`NodeView`** - `freemind/view/mindmapview/NodeView.java` - Individual node views

### Toolbars
- **`MindMapToolBar`** - `freemind/modes/mindmapmode/MindMapToolBar.java` - Left vertical toolbar
  - Created in `MindMapController.getModeToolBar()`
  - Contains icons for formatting, colors, etc.

- **`FreeMindToolBar`** - `freemind/controller/FreeMindToolBar.java` - Base toolbar class

- **`Controller.getLeftToolBar()`** - Returns mode-specific left toolbar (line 690)

### Split Pane Management
- **`SplitComponentType`** enum - `freemind/controller/Controller.java:1952`
  - `NOTE_PANEL(0)` - Note viewer panel
  - `ATTRIBUTE_PANEL(1)` - Attribute panel

- **`insertComponentIntoSplitPane()`** - `Controller.java:1978`
  - Adds bottom panel (notes, attributes)
  - Creates JOptionalSplitPane if needed

- **`removeSplitPane()`** - `Controller.java:1993`
  - Removes bottom panel

### Note Panel
- **`NodeNoteViewer`** - `freemind/modes/browsemode/NodeNoteViewer.java` - Displays node notes
  - Shows at bottom when node selected
  - Uses JLabel inside JScrollPane

## Common Modification Patterns

### Adding a new toolbar button
1. Locate the mode-specific toolbar (e.g., `MindMapToolBar`)
2. Add button in constructor or initialization method
3. Connect to action in `MindMapController`

### Modifying the split pane behavior
1. Edit `FreeMind.insertComponentIntoSplitPane()` (line 1192)
2. Adjust `JSplitPane.VERTICAL_SPLIT` vs `HORIZONTAL_SPLIT`
3. Modify `setResizeWeight()` for sizing behavior

### Adding a new bottom panel
1. Create your component extending JComponent
2. Add new enum value to `SplitComponentType`
3. Call `Controller.insertComponentIntoSplitPane(component, SplitComponentType.YOUR_TYPE)`
4. Remove with `Controller.removeSplitPane(SplitComponentType.YOUR_TYPE)`

### Changing left toolbar visibility
- `Controller.setLeftToolbarVisible(boolean)` - Line 685
- Property: `"leftToolbarVisible"`

## Related Files

See `references/ui-components.md` for detailed component documentation.
