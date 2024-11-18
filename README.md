# CVFS User Manual

## Table of Contents

1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
   - [Downloading the Project](#downloading-the-project)
   - [Running the CVFS Program](#running-the-cvfs-program)
   - [Creating a New Disk](#creating-a-new-disk)
3. [File and Directory Operations](#file-and-directory-operations)
   - [Creating Documents](#creating-documents)
   - [Creating Directories](#creating-directories)
   - [Deleting Files](#deleting-files)
   - [Renaming Files](#renaming-files)
4. [Working with Directories](#working-with-directories)
   - [Changing the Working Directory](#changing-the-working-directory)
   - [Listing Files](#listing-files)
   - [Recursive Listing](#recursive-listing)
5. [Criteria and Search](#criteria-and-search)
   - [Defining Simple Criteria](#defining-simple-criteria)
   - [Defining Composite Criteria](#defining-composite-criteria)
   - [Printing All Criteria](#printing-all-criteria)
   - [Searching Files](#searching-files)
   - [Recursive Search](#recursive-search)
6. [Saving and Loading Disks](#saving-and-loading-disks)
   - [Saving a Disk](#saving-a-disk)
   - [Loading a Disk](#loading-a-disk)
7. [Undo and Redo Operations](#undo-and-redo-operations)
   - [Undoing Commands](#undoing-commands)
   - [Redoing Commands](#redoing-commands)
8. [Exiting the System](#exiting-the-system)
9. [Troubleshooting](#troubleshooting)



## Introduction

The CVFS is a virtual disk management system that allows users to simulate a file system in a virtual disk through a command-line interface (CLI). You can create documents and directories, organize them hierarchically, and perform various operations.

### Features

- Create and manage virtual disks with specified maximum sizes.
- Support for documents of types: `txt`, `java`, and `html`.
- Hierarchical organization with directories and subdirectories.
- Define and use criteria to **search** for files.
- **Save and load** virtual disks from the local file system.
- Support for **undoing and redoing** certain operations.

### Restrictions

- File names must consist of only English letters and digits.
- File names can have at most 10 characters.
- Unique names are enforced for files within the same directory.
- Document types are not part of the document names.
- The size of a document: `40 + content.length * 2`.
- The size of a directory: `40 + total size of contained files`.



## Getting Started

### Downloading the Project

If you have not downloaded the CVFS project yet, you can clone it from GitHub using the following command:

```bash
git clone https://github.com/Deep-Cold/In-Memory-Virtual-File-System
```

This command will create a directory named `In-Memory-Virtual-File-System` in your current directory, containing all the project files.

### Running the CVFS Program

**Navigate to the Project Directory**

Change your working directory to the project's root directory:

```bash
cd In-Memory-Virtual-File-System
```

**Compile the Java Source Files**

Ensure you have Java Development Kit (JDK) installed on your system. You need to compile the Java source files before running the program.

From the project root directory, compile all `.java` files in the `src` directory:

```bash
javac src/*.java src/*/*.java
```

**Run the CVFS Program**

Run the `Processor` class, which contains the `main` method to start the program:

```bash
java src.Processor
```

**Note:** Depending on your Java setup, you might need to adjust the classpath or package names. Ensure that the `src` directory is included in your classpath, or adjust the commands accordingly.



### Creating a New Disk

To start working with CVFS, you need to create a new virtual disk.

**Command:**

```
newDisk diskSize
```

- **diskSize**: An integer representing the maximum size of the virtual disk.

**Example:**

```
newDisk 1000
```

**Effect:**

- Creates a new virtual disk with the specified maximum size.
- Sets the newly created disk as the working disk.
- Sets the working directory to the root directory of the new disk.



## File and Directory Operations

### Creating Documents

Create new documents within the working directory.

**Command:**

```
newDoc docName docType docContent
```

- **docName**: The name of the document (up to 10 characters, letters, and digits only).
- **docType**: The type of the document (`txt`, `java`, or `html`).
- **docContent**: The content of the document as a string.

**Example:**

```
newDoc mydoc txt "This is a sample document content."
```

**Effect:**

- Creates a new document in the working directory with the specified name, type, and content.
- The size of the document is calculated based on its content length.

**Note:**

- Document names must be unique within the directory.
- Document content should be enclosed in quotes if it contains spaces.

---

### Creating Directories

Create new directories within the working directory.

**Command:**

```
newDir dirName
```

- **dirName**: The name of the directory (up to 10 characters, letters, and digits only).

**Example:**

```
newDir mydir
```

**Effect:**

- Creates a new directory in the working directory with the specified name.

**Note:**

- Directory names must be unique within the directory.

---

### Deleting File

Delete an existing file (document or directory) from the working directory.

**Command:**

```
delete fileName
```

- **fileName**: The name of the file to delete.

**Example:**

```
delete mydir
```

**Effect:**

- Deletes the file with the specified name from the working directory.

---

### Renaming Files

Rename an existing file in the working directory.

**Command:**

```
rename oldFileName newFileName
```

- **oldFileName**: The current name of the file (must exsist).
- **newFileName**: The new name for the file (up to 10 characters, letters, and digits only).

**Example:**

```
rename mydoc mynewdoc
```

**Effect:**

- Renames the file from `oldFileName` to `newFileName` within the working directory.

## Working with Directories

### Changing the Working Directory

Change the current working directory to another directory.

**Command:**

```
changeDir dirName
```

- **dirName**: The name of the directory to change to. Use `..` to move to the parent directory.

**Example:**

```
changeDir mydir
```

```
changeDir ..
```

**Effect:**

- Changes the working directory to the specified directory if it exists in the current directory.
- If `dirName` is `..`, moves up to the parent directory (if not already at the root).

---

### Listing Files

List all files directly contained in the working directory.

**Command:**

```
list
```

**Example output:**

```
testdir1 40
testdir2 40
myDoc txt Hello world!
Total files:3, Total size: 144
```

**Effect:**

- Displays all files (documents and directories) in the working directory.
- For documents, shows the name, type, and size.
- For directories, shows the name and size.
- Reports the total number and size of files listed.

---

### Recursive Listing

Recursively list all files contained in the working directory and its subdirectories.

**Command:**

```
rList
```

**Effect:**

- Displays all files in the working directory and its subdirectories.
- Uses indentation to indicate the level of each file.
- Reports the total number and size of files listed.

**Example output:**

```
testdir1 200
    testdir3 80
    		testDir4 40
    myjava java public class main {}
testdir2 40
mydoc txt Hello world!
Total files: 6, Total size: 504
```



## Criteria and Search

CVFS allows you to define criteria to search for files based on attributes such as name, type, and size.

### Defining Simple Criteria

Define a simple criterion that can be used for searching.

The default criterion `IsDocument` is predefined and checks whether a file is a document.

**Command:**

```
newSimpleCri criName attrName op val
```

- **criName**: A unique name for the criterion (exactly two English letters).
- **attrName**: The attribute to filter by (`name`, `type`, or `size`).
- **op**: The operator to use in the criterion.
  - If `attrName` is `name`, `op` must be `contains`.
  - If `attrName` is `type`, `op` must be `equals`.
  - If `attrName` is `size`, `op` can be `>`, `<`, `>=`, `<=`, `==`, or `!=`.
- **val**: The value to compare against.
  - For `name` and `type`, `val` must be a string enclosed in double quotes.
  - For `size`, `val` must be an integer.

**Examples:**

1. Criterion to find files where the name contains "doc":

   ```
   newSimpleCri AA name contains "doc"
   ```
   
2. Criterion to find files of type "txt":

   ```
   newSimpleCri BB type equals "txt"
   ```
   
3. Criterion to find files where size is greater than 100:

   ```
   newSimpleCri CC size > 100
   ```

---

### Defining Composite Criteria

Combine simple criteria to form composite criteria using logical operators.

### Defining a Negation Criterion

**Command:**

```
newNegation criName1 criName2
```

- **criName1**: A unique name for the new criterion.
- **criName2**: The name of an existing criterion to negate.

**Example:**

```
newNegation DD BB
```

**Effect:**

- Creates a new criterion `DD` that represents the negation of criterion `BB`.

---

### Defining a Binary Criterion

**Command:**

```
newBinary criName1 criName3 logicOp criName4
```

- **criName1**: A unique name for the new criterion.
- **criName3** and **criName4**: Names of existing criteria.
- **logicOp**: Logical operator (`&&` for AND, `||` for OR).

**Example:**

```
newBinary EE AA && CC
```

**Effect:**

- Creates a new criterion `EE` that represents the combination of criteria `AA` AND `CC`.

---

### Printing All Criteria

List all defined criteria.

**Command:**

```
printAllCriteria
```

**Effect:**

- Prints all criteria that have been defined.
- Each criterion is resolved to its simple form or shows how it combines other criteria.

**Example output:**

```
AA name contains doc
BB type equals txt
CC size > 100
DD type "doesn't equal" txt
EE (name contains doc) && (size > 100)
IsDocument
```

---

### Searching Files

Search for files in the working directory that satisfy a criterion.

**Command:**

```
search criName
```

- **criName**: The name of an existing criterion.

**Effect:**

- Lists all files directly in the working directory that satisfy the specified criterion.
- Reports the total number and size of files listed.

**Example:**

```
search AA
mydoc txt This is a sample document content.
Total files: 1, Total size: 108
```

---

### Recursive Search

Recursively search for files in the working directory that satisfy a criterion.

**Command:**

```
rSearch criName
```

- **criName**: The name of an existing criterion.

**Example:**

```
rSearch EE
mydoc txt This is a sample document content.
mydoc2 txt This is another sample document content.
Total files: 2, Total size: 228
```

**Effect:**

- Lists all files in the working directory and its subdirectories that satisfy the specified criterion.
- Uses indentation to indicate the level of each file.
- Reports the total number and size of files listed.



## Saving and Loading Disks

### Saving a Disk

Save the current virtual disk to a file on the local file system.

**Command:**

```
save path
```

- **path**: The file path where the disk should be saved.

**Example:**

```
save test
```

**Effect:**

- Saves the current working disk, including all files and defined criteria, to the specified path.

---

### Loading a Disk

Load a virtual disk from a file on the local file system.

**Command:**

```
load path
```

- **path**: The file path from where the disk should be loaded.

**Example:**

```
load test
```

**Effect:**

- Loads the virtual disk from the specified file.
- Sets the loaded disk as the working disk.
- Sets the working directory to the root directory of the loaded disk.



## Undo and Redo Operations

CVFS supports undoing and redoing certain operations to help manage changes.

### Undoing Commands

**Command:**

```
undo
```

**Effect:**

- Undoes the last operation that can be undone.
- Applicable for commands: `newDoc`, `newDir`, `delete`, `rename`, `changeDir`, `newSimpleCri`, and `newNegation`.

---

### Redoing Commands

**Command:**

```
redo
```

**Effect:**

- Redoes the last operation that was undone.



## Exiting the System

Quitting CVFS

**Command:**

```
quit
```

**Effect:**

- Terminates the execution of the system.
- Ensure that you have saved any necessary data before quitting.



## Troubleshooting

