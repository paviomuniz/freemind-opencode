# Contributing to FreeMind

Thank you for your interest in contributing to FreeMind! This document provides guidelines and instructions for contributing to the project.

## Table of Contents

- [Development Setup](#development-setup)
- [Code Style Guidelines](#code-style-guidelines)
- [Development Workflow](#development-workflow)
- [Testing](#testing)
- [Submitting Changes](#submitting-changes)

## Development Setup

### Prerequisites

1. **Java 11 JDK** - Required for building
2. **Apache Ant** - Build tool
3. **Git** - Version control

### Quick Setup

```bash
# Navigate to the freemind directory
cd freemind

# Verify Java version (should be 11 or higher)
java -version

# Verify Ant is installed
ant -version

# Build the project
ant dist

# Run tests
ant test
```

## Code Style Guidelines

### Java Code Style

#### General Conventions

- **Indentation**: Use tabs for indentation
- **Line Length**: Keep lines under 120 characters when possible
- **Braces**: Opening brace on the same line
- **Naming Conventions**:
  - Classes: `PascalCase` (e.g., `MindMapNode`)
  - Methods: `camelCase` (e.g., `getNodeText()`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `DEFAULT_FONT_SIZE`)
  - Packages: lowercase (e.g., `freemind.modes`)

#### File Header Template

All new Java files should include this header:

```java
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
```

#### Code Organization

1. **Package Structure**:
   ```
   freemind/
   ├── common/          # Utility classes
   ├── controller/      # Main application controllers
   ├── extensions/      # Extension points and hooks
   ├── main/            # Entry points (FreeMind.java)
   ├── modes/           # Map modes implementation
   ├── preferences/     # Settings management
   ├── swing/           # Swing utilities
   └── view/            # UI components
   ```

2. **Class Organization**:
   - Public methods at the top
   - Protected methods next
   - Private methods last
   - Group related methods together

3. **Imports**:
   - Organize imports by package
   - Remove unused imports
   - Use specific imports, not wildcards (except for static imports)

### Comments and Documentation

#### JavaDoc

- All public classes and methods must have JavaDoc comments
- Use `@param`, `@return`, and `@throws` appropriately
- Example:

```java
/**
 * Creates a new mind map node with the specified text.
 *
 * @param parent the parent node, or null for root
 * @param text the initial text content
 * @return the newly created node
 * @throws IllegalArgumentException if text is null
 */
public MindMapNode createNode(MindMapNode parent, String text) {
    // Implementation
}
```

#### Inline Comments

- Use `//` for single-line comments
- Explain WHY, not WHAT (the code should be self-explanatory)
- Comment complex algorithms or business logic

### Error Handling

- Use checked exceptions for recoverable errors
- Use unchecked exceptions for programming errors
- Always log exceptions with appropriate level:
  - `SEVERE` for critical errors
  - `WARNING` for recoverable issues
  - `INFO` for general information
  - `FINE` for debugging

Example:
```java
try {
    file.load();
} catch (IOException e) {
    logger.log(Level.WARNING, "Failed to load file: " + filePath, e);
    // Handle error appropriately
}
```

### Logging

Use the standard Java logging framework:

```java
import java.util.logging.Logger;

public class MyClass {
    private static final Logger logger = Logger.getLogger(MyClass.class.getName());
    
    public void myMethod() {
        logger.fine("Entering myMethod");
        // ... code ...
    }
}
```

### Resource Management

- Use try-with-resources for AutoCloseable resources
- Close resources in finally blocks when try-with-resources is not applicable

Example:
```java
try (InputStream is = new FileInputStream(file)) {
    // Process stream
} catch (IOException e) {
    logger.log(Level.SEVERE, "Error reading file", e);
}
```

## Development Workflow

### 1. Create a Branch

```bash
git checkout -b feature/your-feature-name
```

Use descriptive branch names:
- `feature/add-svg-export`
- `bugfix/fix-memory-leak`
- `docs/update-readme`

### 2. Make Changes

- Write clean, documented code
- Follow the code style guidelines
- Keep commits focused and atomic

### 3. Build and Test

Before submitting, ensure:

```bash
# Clean build
ant clean

# Compile
ant dist

# Run tests
ant test

# Run application
ant run
```

### 4. Commit Messages

Use clear, descriptive commit messages:

```
Short (50 chars or less) summary

More detailed explanatory text, if necessary. Wrap it to about 72
characters. The blank line separating the summary from the body is
critical.

- Bullet points are okay
- Use imperative mood ("Add feature" not "Added feature")
- Reference issues: "Fixes #123"
```

Examples:
```
Add PDF export functionality

Implement PDF export using Apache FOP library. Supports both
single-page and multi-page mind maps.

Fixes #456
```

```
Fix NullPointerException in node selection

Check for null parent before accessing children array.
```

## Testing

### Running Tests

```bash
# Run all tests
ant test

# Clean and rebuild tests
ant clean test
```

### Writing Tests

- Use JUnit 4 (as included in lib/junit.jar)
- Place test files in `tests/freemind/` mirroring the source structure
- Name test classes with `Test` suffix (e.g., `MindMapNodeTest`)
- Name test methods descriptively

Example:
```java
package freemind.modes;

import org.junit.Test;
import static org.junit.Assert.*;

public class MindMapNodeTest {
    
    @Test
    public void testCreateNodeWithText() {
        MindMapNode node = new MindMapNode("Test Node");
        assertEquals("Test Node", node.getText());
    }
    
    @Test
    public void testAddChildNode() {
        MindMapNode parent = new MindMapNode("Parent");
        MindMapNode child = new MindMapNode("Child");
        parent.addChild(child);
        assertTrue(parent.getChildren().contains(child));
    }
}
```

## Submitting Changes

### Before Submitting

1. **Update documentation** if needed
2. **Add tests** for new functionality
3. **Run full test suite** and ensure all tests pass
4. **Check code style** consistency
5. **Update CHANGELOG** if applicable

### Pull Request Process

1. **Push your branch** to your fork
2. **Create a Pull Request** with:
   - Clear title describing the change
   - Detailed description of what and why
   - Reference to any related issues
   - Screenshots for UI changes

### Code Review

- Address reviewer feedback promptly
- Keep discussion technical and constructive
- Be open to suggestions

## Questions?

If you have questions about contributing:
- Check existing documentation in `freemind/doc/`
- Review the code style in existing files
- Ask in the project discussions

Thank you for contributing to FreeMind!
