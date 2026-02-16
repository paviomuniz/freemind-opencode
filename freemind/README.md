# FreeMind

A premier free mind-mapping software written in Java, ideal for project management, brainstorming, knowledge management, and getting organized.

## Overview

FreeMind is a mature, feature-rich mind mapping application that runs on Windows, macOS, and Linux. It uses a hierarchical structure to help users organize ideas, projects, and information in a visual format.

### Key Features

- **Hierarchical mind mapping** with multiple modes (MindMap, Browse, File)
- **Plugin architecture** for extensibility
- **Export capabilities**: HTML, PDF, SVG, Flash, and more
- **Collaboration features**: Socket, database, and Jabber support
- **Scripting support** via Groovy
- **Spell checking** and rich text editing
- **Internationalization** supporting 42+ languages
- **Cross-platform** support (Windows, macOS, Linux)

## Technology Stack

### Language
- **Java 11** (both source and target compatibility)
- Swing GUI framework

### Build System
- **Apache Ant** - Primary build tool
- Build file: `freemind/build.xml`

### Key Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| JiBX | 1.2.5 | XML data binding |
| JGoodies Forms | 1.8.0 | UI form layout |
| Apache Batik | 1.7 | SVG export |
| Apache Lucene | 4.6.0 | Search functionality |
| Groovy | 2.1.8 | Scripting support |
| JOrtho | - | Spell checking |
| SimplyHTML | - | HTML editor |
| Xalan/Xerces | - | XSLT processing |

## Project Structure

```
C:\src\freemind-code\freemind\
├── freemind/                 # Core Java source code
│   ├── common/               # Common utilities
│   ├── controller/           # Main controllers
│   ├── extensions/           # Extension points
│   ├── main/                 # Main classes (FreeMind.java)
│   ├── modes/                # Map modes (browse, mindmap, file)
│   ├── preferences/          # Settings/preferences
│   ├── swing/                # Swing utilities
│   └── view/                 # UI views
├── tests/                    # JUnit test suite
├── plugins/                  # Plugin system
│   ├── collaboration/        # Socket/database/jabber collaboration
│   ├── help/                 # Help system
│   ├── latex/                # LaTeX support
│   ├── map/                  # Geographic maps
│   ├── script/               # Scripting (Groovy)
│   ├── search/               # Lucene search
│   └── svg/                  # SVG export (Batik)
├── accessories/              # Export/import XSL transformations
├── lib/                      # Third-party libraries
├── doc/                      # Documentation
├── images/                   # Icons and images
└── windows-launcher/         # Windows EXE launcher
```

## Quick Start

### Prerequisites

- **Java 11 JDK** or higher
- **Apache Ant** 1.9 or higher
- **Git** (optional, for version control)

### Installation

1. **Clone or download** the source code
2. **Navigate** to the freemind directory:
   ```bash
   cd freemind
   ```

3. **Build** the project:
   ```bash
   ant dist
   ```

4. **Run** FreeMind:
   ```bash
   ant run
   ```

### Build Commands

| Command | Description |
|---------|-------------|
| `ant dist` | Compile all sources |
| `ant post` | Create distribution package |
| `ant run` | Start FreeMind from sources |
| `ant test` | Run unit tests |
| `ant clean` | Clean build artifacts |

### Build Output

- **Compiled classes**: `../bin/classes/`
- **Distribution**: `../bin/dist/`
- **Libraries**: `../bin/dist/lib/`
- **macOS app**: `../bin/dist_macos/`

## Development Environment Setup

### IDE Support

#### Eclipse
1. Import as existing project
2. `.project` and `.classpath` files are pre-configured
3. All dependencies automatically resolved

#### IntelliJ IDEA
1. Import project from existing sources
2. Select Eclipse project format
3. All JARs in `lib/` and `plugins/` are required

### Windows Development

See [WINDOWS_SETUP.md](WINDOWS_SETUP.md) for detailed Windows-specific setup instructions.

## Version Information

- **Current Version**: 1.1.0 Beta 2
- **Main Class**: `freemind.main.FreeMindStarter`
- **License**: GNU General Public License v2+

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for:
- Code style guidelines
- Development workflow
- Submission process

## Resources

- **Documentation**: `freemind/doc/freemind.mm` (mindmap format)
- **Changelog**: `freemind/history.txt`
- **License**: `freemind/license`

## Authors

- Joerg Mueller
- Daniel Polansky
- Christian Foltin
- Dimitry Polivaev
- And many other contributors

## License

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

See the `license` file for full details.
