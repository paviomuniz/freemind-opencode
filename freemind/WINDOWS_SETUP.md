# Windows Development Setup for FreeMind

This guide provides detailed instructions for setting up a Windows development environment for FreeMind.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Step-by-Step Setup](#step-by-step-setup)
- [Environment Configuration](#environment-configuration)
- [IDE Setup](#ide-setup)
- [Build and Run](#build-and-run)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before you begin, ensure you have the following:

- Windows 10 or Windows 11 (64-bit recommended)
- Administrator access (for installing software)
- Internet connection (for downloading tools)

## Step-by-Step Setup

### 1. Install Java 11 JDK

FreeMind requires Java 11. Do not use newer versions as they may cause compatibility issues.

**Option A: Oracle JDK**
1. Visit https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
2. Download "Windows x64 Installer"
3. Run the installer with default settings

**Option B: Adoptium (Eclipse Temurin) - Recommended**
1. Visit https://adoptium.net/
2. Download "OpenJDK 11 (LTS)" for Windows x64
3. Run the installer with default settings

**Verify Installation:**
```cmd
java -version
```

Expected output:
```
openjdk version "11.0.x" 202x-xx-xx
OpenJDK Runtime Environment Temurin-11.0.x+xx
OpenJDK 64-Bit Server VM Temurin-11.0.x+xx
```

### 2. Install Apache Ant

**Download and Install:**
1. Visit https://ant.apache.org/bindownload.cgi
2. Download "apache-ant-1.10.x-bin.zip"
3. Extract to `C:\apache-ant-1.10.x` (create folder if needed)

### 3. Set Environment Variables

**Method 1: Using System Properties (Recommended)**

1. Press `Win + R`, type `sysdm.cpl`, press Enter
2. Click "Advanced" tab
3. Click "Environment Variables" button
4. Under "System variables", click "New..."

**Add these variables:**

```
Variable name: JAVA_HOME
Variable value: C:\Program Files\Eclipse Adoptium\jdk-11.0.x.x-hotspot
```

```
Variable name: ANT_HOME
Variable value: C:\apache-ant-1.10.x
```

**Edit PATH variable:**
1. Find "Path" in System variables
2. Click "Edit"
3. Click "New" and add:
   ```
   %JAVA_HOME%\bin
   %ANT_HOME%\bin
   ```
4. Click "OK" on all dialogs

**Method 2: Using Command Line (PowerShell - Administrator)**

```powershell
# Set JAVA_HOME
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Eclipse Adoptium\jdk-11.0.x.x-hotspot", "Machine")

# Set ANT_HOME
[Environment]::SetEnvironmentVariable("ANT_HOME", "C:\apache-ant-1.10.x", "Machine")

# Update PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$newPath = $currentPath + ";%JAVA_HOME%\bin;%ANT_HOME%\bin"
[Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
```

**Note:** Replace paths with your actual installation directories.

### 4. Verify Environment Setup

Open a **new** Command Prompt (CMD) and run:

```cmd
java -version
ant -version
```

Both commands should display version information without errors.

## Environment Configuration

### Project Directory Structure

Your project should be located at:
```
C:\src\freemind-code\
├── freemind\              # Main application
│   ├── build.xml          # Main build file
│   ├── freemind\          # Source code
│   ├── lib\               # Dependencies
│   ├── plugins\           # Plugin code
│   └── ...
└── bin\                   # Build output (created by Ant)
```

### Windows-Specific Files

The project includes Windows-specific files:
- `freemind/windows-launcher/` - Windows EXE launcher source
- `freemind/windows-launcher/FreeMind.exe` - Pre-built 32-bit launcher
- `freemind/windows-launcher/FreeMind64.exe` - Pre-built 64-bit launcher

## IDE Setup

### Eclipse IDE

**Step 1: Download Eclipse**
1. Visit https://www.eclipse.org/downloads/
2. Download "Eclipse IDE for Java Developers"
3. Extract to `C:\eclipse`

**Step 2: Import Project**
1. Open Eclipse
2. File → Import → Existing Projects into Workspace
3. Select root directory: `C:\src\freemind-code\freemind`
4. Click Finish

**Step 3: Verify Configuration**
- The `.classpath` file is pre-configured with all dependencies
- JRE should be set to Java 11
- Source folders are automatically detected

**Step 4: Build Project**
1. Right-click project → Build Project
2. Or use Project → Build Automatically

### IntelliJ IDEA

**Step 1: Install IntelliJ IDEA**
1. Download from https://www.jetbrains.com/idea/download/
2. Run installer with default settings

**Step 2: Import Project**
1. Open IntelliJ IDEA
2. File → New → Project from Existing Sources
3. Select `C:\src\freemind-code\freemind`
4. Choose "Create project from existing sources"
5. Follow import wizard, accepting defaults

**Step 3: Configure Dependencies**
1. File → Project Structure (Ctrl+Alt+Shift+S)
2. Libraries → Add all JARs from:
   - `freemind/lib/`
   - `freemind/plugins/*/`
3. Set Project SDK to Java 11

**Step 4: Configure Ant Build**
1. View → Tool Windows → Ant
2. Click "+" to add build file
3. Select `freemind/build.xml`
4. All Ant targets will be available

## Build and Run

### Command Line Build

Open Command Prompt in `C:\src\freemind-code\freemind\`:

```cmd
REM Navigate to project directory
cd C:\src\freemind-code\freemind

REM Clean previous builds
ant clean

REM Compile the project
ant dist

REM Run unit tests
ant test

REM Start FreeMind
ant run
```

### Build Output Locations

After successful build:
```
C:\src\freemind-code\bin\
├── classes\              # Compiled .class files
├── dist\                 # Distribution folder
│   ├── freemind.jar     # Main JAR
│   ├── lib\              # Dependencies
│   └── FreeMind.exe     # Windows launcher
└── testclasses\         # Test .class files
```

### Running from Distribution

```cmd
REM After ant post command
C:\src\freemind-code\bin\dist\FreeMind.exe

REM Or run JAR directly
java -jar C:\src\freemind-code\bin\dist\lib\freemind.jar
```

### Creating Distribution Package

```cmd
ant post
```

This creates a complete distributable package in `../bin/dist/`.

## Troubleshooting

### Common Issues

#### Issue: "'ant' is not recognized as an internal or external command"

**Solution:**
1. Verify ANT_HOME is set correctly
2. Verify %ANT_HOME%\bin is in PATH
3. Restart Command Prompt after setting environment variables
4. Check if ant.bat exists in `C:\apache-ant-1.10.x\bin\`

#### Issue: "javac: target release 11 conflicts with default source release"

**Solution:**
1. Ensure JAVA_HOME points to JDK (not JRE)
2. Verify with: `echo %JAVA_HOME%`
3. Check Java version: `%JAVA_HOME%\bin\java -version`

#### Issue: Build fails with "package org.jibx.runtime does not exist"

**Solution:**
Dependencies are missing from classpath. Ensure:
1. All JAR files exist in `freemind/lib/`
2. Run `ant dist` from the `freemind` directory
3. Check that `lib/jibx/jibx-run.jar` exists

#### Issue: "Unable to locate tools.jar"

**Solution:**
1. This usually means JAVA_HOME points to JRE instead of JDK
2. Reinstall JDK (not just JRE)
3. Update JAVA_HOME to point to JDK installation

#### Issue: "Out of memory" during build

**Solution:**
Set ANT_OPTS environment variable:
```cmd
set ANT_OPTS=-Xmx1024m -Xms512m
```

Or permanently add to System Environment Variables:
```
ANT_OPTS=-Xmx1024m -Xms512m
```

#### Issue: Tests fail with "ClassNotFoundException"

**Solution:**
1. Ensure junit.jar is in `freemind/lib/`
2. Run `ant clean` then `ant dist` to rebuild
3. Check classpath in build.xml includes junit

### IDE-Specific Issues

#### Eclipse: "Build path errors"

1. Right-click project → Build Path → Configure Build Path
2. Libraries tab → Remove any missing JARs
3. Add JARs from `freemind/lib/` and `freemind/plugins/`
4. Project → Clean → Clean all projects

#### IntelliJ: "SDK is not defined"

1. File → Project Structure
2. Project Settings → Project
3. Set Project SDK to Java 11
4. Apply and OK

### Getting Help

If you encounter issues not covered here:

1. Check the main README.md for general information
2. Review existing documentation in `freemind/doc/`
3. Check the project history in `freemind/history.txt`
4. Verify your environment variables are set correctly

### Windows Launcher Development (Optional)

To modify the Windows EXE launcher:

**Requirements:**
- Dev-C++ (http://www.bloodshed.net/devcpp.html) or MinGW

**Steps:**
1. Install Dev-C++ with default settings
2. Open `freemind/windows-launcher/Freemind.dev`
3. Build project (F9 or Execute → Compile)
4. Output: `FreeMind.exe`

**Note:** The launcher is written in C and provides:
- Custom application icon
- Drag-and-drop file support
- Command-line argument passing

## Quick Reference

### Essential Commands

```cmd
REM Build
cd C:\src\freemind-code\freemind
ant clean dist

REM Run
ant run

REM Test
ant test

REM Package
ant post
```

### File Locations

- **Source**: `C:\src\freemind-code\freemind\freemind\`
- **Libraries**: `C:\src\freemind-code\freemind\lib\`
- **Build output**: `C:\src\freemind-code\bin\`
- **Main class**: `freemind.main.FreeMindStarter`

### Environment Variables

```
JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-11.0.x.x-hotspot
ANT_HOME=C:\apache-ant-1.10.x
PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%PATH%
```

---

**You're now ready to develop FreeMind on Windows!** 🎉

For contribution guidelines, see [CONTRIBUTING.md](CONTRIBUTING.md).
