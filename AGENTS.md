# AGENTS.md - FreeMind Coding Guidelines

This file provides guidelines for AI coding agents working on the FreeMind mind-mapping application.

## Build & Development Commands

All commands should be run from the `freemind/` directory:

```bash
# Navigate to project directory
cd freemind

# Build the project (compile all sources)
ant dist

# Run all unit tests
ant test

# Run a single test class (requires modifying AllTests.java)
# Edit tests/freemind/AllTests.java to include only the desired test
ant test

# Start the application
ant run

# Clean build artifacts
ant clean

# Create distribution package
ant post

# Generate Javadoc
ant doc
```

### Running Single Tests

To run a single test, edit `tests/freemind/AllTests.java` and comment out unwanted tests in the `suite()` method, then run `ant test`. Tests extend `junit.framework.TestCase`.

## Technology Stack

- **Language**: Java 11 (source and target)
- **Build Tool**: Apache Ant (build.xml)
- **Test Framework**: JUnit 3/4
- **GUI Framework**: Java Swing
- **Key Dependencies**: JiBX (XML binding), JGoodies Forms, Apache Batik (SVG), Apache Lucene

## Code Style Guidelines

### File Header Template

Every new Java file MUST include this exact header:

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

### Formatting Rules

- **Indentation**: Use tabs (not spaces)
- **Line Length**: Keep under 120 characters when possible
- **Braces**: Opening brace on same line
- **Imports**: Organize by package, no wildcards (except static), remove unused

### Naming Conventions

- **Classes**: `PascalCase` (e.g., `MindMapNode`, `FreeMindMain`)
- **Methods**: `camelCase` (e.g., `getNodeText()`, `setProperty()`)
- **Constants**: `UPPER_SNAKE_CASE` (e.g., `DEFAULT_FONT_SIZE`, `TRUE_VALUE`)
- **Packages**: lowercase (e.g., `freemind.modes`, `freemind.controller`)
- **Fields**: `camelCase` with `m` prefix for member variables (e.g., `mCheckBox`, `mFreeMindMain`)

### Class Organization

```java
public class MyClass {
    // Constants
    public static final String CONSTANT = "value";
    
    // Fields
    private String mFieldName;
    
    // Constructors
    public MyClass() { }
    
    // Public methods
    public void publicMethod() { }
    
    // Protected methods
    protected void protectedMethod() { }
    
    // Private methods
    private void privateMethod() { }
}
```

### JavaDoc Requirements

All public classes and methods must have JavaDoc:

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
    // implementation
}
```

### Package Structure

```
freemind/
├── common/          # Utility classes and property beans
├── controller/      # Main application controllers
├── extensions/      # Extension points and hooks
├── main/            # Entry points (FreeMind.java, FreeMindStarter.java)
├── modes/           # Map modes (browsemode, mindmapmode, filemode)
├── preferences/     # Settings and configuration
├── swing/           # Swing utilities
└── view/            # UI components and mindmap view
```

## Error Handling & Logging

### Logging

Use standard Java logging:

```java
import java.util.logging.Logger;
import java.util.logging.Level;

public class MyClass {
    private static final Logger logger = Logger.getLogger(MyClass.class.getName());
    
    public void myMethod() {
        logger.fine("Debug message");
        logger.info("Info message");
        logger.warning("Warning message");
        logger.severe("Error message");
    }
}
```

### Exception Handling

- Use checked exceptions for recoverable errors
- Use unchecked exceptions for programming errors
- Always log exceptions with appropriate level

```java
try {
    file.load();
} catch (IOException e) {
    logger.log(Level.WARNING, "Failed to load file: " + filePath, e);
    // Handle appropriately
}
```

### Resource Management

Use try-with-resources for AutoCloseable resources:

```java
try (InputStream is = new FileInputStream(file)) {
    // Process stream
} catch (IOException e) {
    logger.log(Level.SEVERE, "Error reading file", e);
}
```

## Testing Guidelines

### Test Structure

Tests extend `junit.framework.TestCase` (JUnit 3 style):

```java
package tests.freemind;

import junit.framework.TestCase;

public class MyTest extends FreeMindTestBase {
    
    public MyTest() throws IOException {
        super();
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Test setup
    }
    
    public void testMyFeature() {
        // Test code
        assertEquals(expected, actual);
        assertTrue(condition);
    }
}
```

### Adding Tests to Suite

Add new tests to `tests/freemind/AllTests.java`:

```java
public static Test suite() {
    TestSuite suite = new TestSuite("AllTests");
    suite.addTest(new TestSuite(MyNewTest.class));
    return suite;
}
```

## IDE Setup

### Eclipse (Recommended)

Project is pre-configured with `.project` and `.classpath` files. Simply import as existing project.

### IntelliJ IDEA

1. Import project from existing sources
2. Select Eclipse project format
3. Add all JARs from `lib/` and `plugins/` directories
4. Set Project SDK to Java 11

## Important Notes

- Build output goes to `../bin/` (outside the freemind directory)
- Dependencies are in `freemind/lib/` and `freemind/plugins/`
- Main class: `freemind.main.FreeMindStarter`
- Version info is extracted from `freemind/main/FreeMind.java`
- Windows launcher available in `windows-launcher/`
