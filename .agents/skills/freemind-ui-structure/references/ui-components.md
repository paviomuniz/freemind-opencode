# FreeMind UI Components Reference

Detailed reference for all major UI components in FreeMind.

## Main Window Components

### FreeMind (JFrame)
**File:** `freemind/main/FreeMind.java`
**Extends:** JFrame
**Implements:** FreeMindMain, ActionListener

The main application window that contains all other UI components.

#### Key Fields:
```java
private JScrollPane mScrollPane = null;  // Line 275
private JSplitPane mSplitPane;           // Line 277
private JComponent mContentComponent = null; // Line 279
private JTabbedPane mTabbedPane = null;  // Line 281
```

#### Key Methods:
- `setScreenBounds()` - Line 893 - Sets up the main window layout
- `insertComponentIntoSplitPane()` - Line 1192 - Adds split pane for bottom panels
- `removeSplitPane()` - Line 1241 - Removes split pane
- `getScrollPane()` - Line 1279 - Returns the main scroll pane

## View Components

### MapView
**File:** `freemind/view/mindmapview/MapView.java`
**Extends:** JPanel
**Implements:** ViewAbstraction, Printable, Autoscroll

The main canvas component that displays the entire mind map.

#### Key Inner Classes:
- `ScrollPane` (line 115) - Custom JScrollPane for the map

#### Key Methods:
- `getRoot()` - Returns the root node view
- `getSelected()` - Returns currently selected node view
- `selectAsTheOnlyOneSelected()` - Selects a single node
- `setZoom()` - Adjusts zoom level

### NodeView
**File:** `freemind/view/mindmapview/NodeView.java`
**Extends:** JPanel

Represents a single node in the mind map view.

#### Key Methods:
- `getModel()` - Returns the MindMapNode model
- `getMap()` - Returns parent MapView
- `getMainView()` - Returns the visual MainView component
- `isSelected()` - Check if node is selected

## Toolbar Components

### MindMapToolBar
**File:** `freemind/modes/mindmapmode/MindMapToolBar.java`
**Extends:** JPanel

The left-side vertical toolbar with formatting buttons and icons.

#### Key Fields:
```java
private JToolBar iconToolBar;  // Line 71
```

#### Key Methods:
- Constructor creates all toolbar buttons
- `add()` methods add components to toolbar

### Mode-Specific Toolbars

Each mode has its own toolbar:
- **MindMapMode:** `MindMapToolBar`
- **BrowseMode:** `BrowseToolBar` (`freemind/modes/browsemode/BrowseToolBar.java`)
- **FileMode:** `FileToolBar` (`freemind/modes/filemode/FileToolBar.java`)

## Split Pane Components

### JOptionalSplitPane
**File:** Used within `Controller.java`
**Location:** `freemind/controller/Controller.java:1968`

Manages optional panels (notes, attributes) at the bottom or side.

#### Split Types:
```java
public enum SplitComponentType {
    NOTE_PANEL(0),      // For note viewer
    ATTRIBUTE_PANEL(1)  // For attribute panel
}
```

#### Key Methods in Controller:
- `insertComponentIntoSplitPane(JComponent, SplitComponentType)` - Line 1978
- `removeSplitPane(SplitComponentType)` - Line 1993
- `storeOptionSplitPanePosition()` - Line 2006

### Main JSplitPane (mSplitPane)
**File:** `freemind/main/FreeMind.java`
**Field:** Line 277

Splits the main scroll pane from optional bottom panels.

#### Configuration:
- `splitType` - VERTICAL_SPLIT (default) or HORIZONTAL_SPLIT
- `setContinuousLayout(true)` - Smooth resizing
- `setResizeWeight(1.0d)` - Map area gets extra space

## Note Panel Components

### NodeNoteViewer
**File:** `freemind/modes/browsemode/NodeNoteViewer.java`
**Extends:** NodeNoteBase
**Implements:** NodeSelectionListener

Displays node notes in a panel at the bottom.

#### Key Fields:
```java
private JComponent noteScrollPane;  // Line 43
private JLabel noteViewer;          // Line 45
```

#### Key Methods:
- `getNoteViewerComponent(String text)` - Line 55 - Creates the scroll pane
- `onFocusNode(NodeView)` - Line 71 - Shows panel when node has notes
- `onLostFocusNode(NodeView)` - Line 67 - Hides panel

### NodeNoteEditor
**File:** `freemind/modes/mindmapmode/dialogs/NodeNoteEditor.java`

Editable version for MindMapMode (vs viewer for BrowseMode).

## Controller Components

### Controller
**File:** `freemind/controller/Controller.java`

Central controller managing UI state and interactions.

#### Toolbar Management:
```java
private JToolBar toolbar;           // Line 132
private JToolBar filterToolbar;     // Line 137
boolean leftToolbarVisible = true;  // Line 156
```

#### Key Methods:
- `getToolbar()` - Line 681 - Returns top toolbar
- `setLeftToolbarVisible(boolean)` - Line 685 - Controls left toolbar
- `getModeController().getLeftToolBar()` - Line 690 - Gets mode-specific left toolbar
- `insertComponentIntoSplitPane()` - Line 1978 - Adds bottom panel
- `removeSplitPane()` - Line 1993 - Removes bottom panel

## Dialog Components

### EditNodeDialog
**File:** `freemind/view/mindmapview/EditNodeDialog.java`

Dialog for editing node text with full formatting.

### EnterPasswordDialog
**File:** `freemind/modes/common/dialogs/EnterPasswordDialog.java`

Password dialog for encrypted nodes.

### IconSelectionPopupDialog
**File:** `freemind/modes/common/dialogs/IconSelectionPopupDialog.java`

Dialog for selecting node icons.

### FilterComposerDialog
**File:** `freemind/controller/filter/FilterComposerDialog.java`

Dialog for creating filter conditions.

## Layout Managers

### MindMapLayout
**File:** `freemind/view/mindmapview/MindMapLayout.java`
**Implements:** LayoutManager

Custom layout manager for the MapView.

### NodeViewLayout
**File:** `freemind/view/mindmapview/NodeViewLayout.java`
**Interface**

Layout strategy interface for positioning nodes.

Implementations:
- `VerticalRootNodeViewLayout`
- `RightNodeViewLayout`
- `LeftNodeViewLayout`
- etc.

## UI Utility Components

### MiniMap
**File:** `freemind/ui/components/MiniMap.java`

Miniature overview of the entire mind map.

### WelcomeScreen
**File:** `freemind/ui/components/WelcomeScreen.java`

Startup welcome dialog with recent files and templates.

### FreeMindProgressMonitor
**File:** `freemind/common/FreeMindProgressMonitor.java`

Progress dialog for long operations.

## File Paths Summary

### Main Application
- `freemind/main/FreeMind.java` - Main window
- `freemind/main/FreeMindMain.java` - Interface for main window
- `freemind/main/FreeMindApplet.java` - Applet version

### Controllers
- `freemind/controller/Controller.java` - Main controller
- `freemind/modes/ModeController.java` - Mode controller interface
- `freemind/modes/ControllerAdapter.java` - Base adapter

### View
- `freemind/view/mindmapview/MapView.java` - Main map view
- `freemind/view/mindmapview/NodeView.java` - Node views
- `freemind/view/mindmapview/NodeViewFactory.java` - Factory for node views

### Modes
- `freemind/modes/mindmapmode/MindMapController.java` - Mind map mode
- `freemind/modes/mindmapmode/MindMapToolBar.java` - Mind map toolbar
- `freemind/modes/browsemode/BrowseController.java` - Browse mode
- `freemind/modes/browsemode/NodeNoteViewer.java` - Note viewer
- `freemind/modes/filemode/FileController.java` - File mode
