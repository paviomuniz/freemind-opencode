
# Personal Guide for this project

## Objectives

Get more advanced MindMapper

## Coonfiguration

Models used

❌- Kimi K2.5 Free

✅- MiniMax M2.5 free
- GLM 5 - free

## Problems

- Menu white when use dark mode

- Mind map window shows minimized
- text windows appeards maximized

## testar

- Salvar texto

## Future


## Documentation

## Main Window and Sub-Windows in FreeMind

### Main Application Window (JFrame)
- **FreeMind** - The primary application window
  - Title format: `[MapName][* if unsaved] - [Mode][ (read_only) if applicable] [file path]`
  - Defined in `freemind/main/FreeMind.java`

### Splash Screen
- **FreeMindSplashModern** - "FreeMind" - Startup splash screen with progress bar

### Core Application Dialogs (JDialog)
- **WelcomeScreen** - "Welcome to FreeMind"
- **FilterComposerDialog** - Filter composition dialog
- **GrabKeyDialog** - "grab-key.title" (keyboard shortcut capture)
- **FreeMindProgressMonitor** - Progress dialog
- **PreviewDialog** - Print preview dialog
- **EnterPasswordDialog** - Password entry for encrypted nodes
- **IconSelectionPopupDialog** - "select_icon"

### Plugin Dialogs
- **SearchViewPanel** - Search dialog
- **ScriptEditorPanel** - Script editing dialog
- **ExportPdfDialog** - "ExportPdfDialog.PDF_Export_Settings"
- **FormDialog** - "enter_password_dialog"
- **CalendarMarkingDialog** - "CalendarMarkingDialog.title"
- **ManagePatternsPopupDialog** - Pattern management
- **ChooseFormatPopupDialog** - Format selection dialog
- **MapSharingWizardView** - Jabber collaboration wizard with dynamic titles:
  - "Share Map"
  - "Connect To Jabber Instant Messaging Server"
  - "Select User (example: user@jabber.org)"
  - "Map Sharing Request"

### Edit Dialogs
- **EditDialog** (inner class in EditNodeBase) - Base dialog for editing node text

The main window title is set dynamically via `Controller.setTitle()` and reflects the current mind map name, save status, mode, and file path.

Based on the codebase, here are the names of the UI components in FreeMind's main screen:

## Main Screen Components

**1. Vertical Bar (Left Side)**
- **`MindMapToolBar`** - The left vertical toolbar containing icons for node formatting, colors, and other tools
- Located at: `freemind.modes.mindmapmode.MindMapToolBar`
- Referred to as `leftToolBar` in `Controller.java`

**2. Mind Map View Area (Center)**
- **`MapView`** - The main canvas where the mind map is displayed
- Located at: `freemind.view.mindmapview.MapView`
- This is contained within a **`JScrollPane`** (referenced as `mScrollPane` in `FreeMind.java`)

**3. Bottom Panel (Note/Attribute Window)**
- **`NodeNoteViewer`** - The note viewing panel that appears at the bottom when a node has notes
- Located at: `freemind.modes.browsemode.NodeNoteViewer`
- Displays in a split pane area managed by **`JOptionalSplitPane`**

## How to Refer to These:

When discussing these components with me, you can use these names:
- "the vertical toolbar" or "left toolbar"
- "the MapView" or "the main mind map area" 
- "the note panel" or "bottom split pane" or "NodeNoteViewer"

The main window itself is the **`FreeMind`** JFrame class, which contains:
- `mScrollPane` (JScrollPane holding the MapView)
- `mSplitPane` (JSplitPane that divides the main area from the note panel)
- `toolbar` (top toolbar)
- `leftToolBar` (vertical toolbar on the left)
- `mOptionalSplitPane` (manages the bottom panel for notes/attributes)

##