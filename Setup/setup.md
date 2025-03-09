Latest update: 9.3.2025

# Setup Instructions

This document explains the main setup needed. We will be using [SuperCollider](https://supercollider.github.io/).

You will need the following SuperCollider extensions:

- `MIRLC 2.0` (under `SC extensions`)
- [Freesound.quark](https://github.com/g-roma/Freesound.sc)
- [FluCoMa](https://github.com/flucoma)
- `MIRLCa` (under `SC extensions`)

**Note**: The `MIRLC 2.0` and `MIRLCa` code are under development. This repository includes the latest version. Previous released versions are:

* MIRLC: [https://github.com/axambo/MIRLC](https://github.com/axambo/MIRLC)
* MIRLCa: [https://github.com/mirlca/code](https://github.com/mirlca/code)

## Step 1: Install SuperCollider

If you don't have SuperCollider installed in your computer, please make sure to first install SuperCollider. You can download it from: 

[https://supercollider.github.io/download](https://supercollider.github.io/download)

There are versions available for Mac, Linux, and Windows.

**For MacOS users**: both signed/notarized and not signed/notarized versions of SuperCollider should work (if available), but we recommend the not-signed/not-notarized version for a less disruptive experience.

If you are unfamiliar with SuperCollider, you can find extensive tutorials online:

* [Getting Started tutorial series](https://doc.sccode.org/Tutorials/Getting-Started/00-Getting-Started-With-SC.html)
* [Eli Fieldsteel's video playlists](https://www.youtube.com/c/elifieldsteel/playlists)
* [The SuperCollider Book](https://mitpress.mit.edu/books/supercollider-book)


### Dealing with directories

Several commands will tell you where the common directories of SuperCollider are. Type any of the below commands on the SuperCollider code editor, position your cursor on the line of the command and press `cmd+Enter`. You will see the path on the post window.

Default directory for temporary files:

```
Platform.defaultTempDir
```


System extension directory:

```
Platform.systemExtensionDir
```

Platform recordings directory:

```
Platform.recordingsDir
```

For more information about the Platform class, position your cursor next to Platform and press `control+D`.


### Get familiar with Extensions

SuperCollider supports the addition of **extensions**. You can install extensions by copying them to the Extensions folder. Depending on your platform, the folder will be located on a different place. Use this command to know its location:

System extension directory:

```
Platform.systemExtensionDir
```

You can find more information about *Using Extensions* on the SuperCollider Help browser.

#### Geting familiar with Quarks 

Similarly, SuperCollider supports the addition of **quarks**. 

Quarks are packages of SuperCollider code containing classes, extension methods, documentation and server UGen plugins. You can easily install/uninstall quarks using the GUI provided by SuperCollider.

You can find more information about *Using Quarks* on the SuperCollider Help browser as well as here:

* [Using Quarks](https://doc.sccode.org/Guides/UsingQuarks.html)
* [Installing Quarks into Supercollider](https://www.youtube.com/watch?v=m5QrwV5Msgk) (video)

SuperCollider includes a manager for downloading these packages. You can launch the Quarks GUI by typing and executing the following line on the SuperCollider code editor:

```
Quarks.gui
```

**For Windows users**: 

You will need to install Git on Windows in order to execute the command `Quarks.gui` successfully.

To install Git on Windows go to [https://gitforwindows.org](https://gitforwindows.org) and follow the instructions.

More info can be found here: [https://git-scm.com/book/en/v2/Getting-Started-Installing-Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

### Get familiar with getting access to Library

An important directory used by SuperCollider is the Library, which usually is hidden by default in MacOS and Windows.

In this directory you will find the quarks, extensions, help files, and the tmp folder, among others.

To find out where the Library is located, execute the following command in the SuperCollider code editor.

System application support directory:

```
Platform.systemAppSupportDir
```

Try to get access to this directory from your File manager. If you can't find it, you will need to tell the OS to make it visible.

**For MacOS users**: The folder is located at `~/Library/Application Support/SuperCollider`. 

If you cannot find this folder, here there are two links that explain how to make it visible:

* [Three Ways to Access the Library Folder on Your Mac](https://www.lifewire.com/os-x-is-hiding-your-library-folder-2260833)
* [https://www.macrumors.com/how-to/reveal-library-folder-in-macos/](https://www.macrumors.com/how-to/reveal-library-folder-in-macos/)

**For Windows users**: The folder is located at `~\AppData\Local\SuperCollider\Extensions\`

To make it visible, got to the directory Local using the File manager and select from the top menu `View > Hidden items`.

**For Linux users**: The folder is located at `~/.local/share/SuperCollider/Extensions/`.


## Step 2: Install cURL

`cURL` is a command-line tool that lets you transfer data to/from a server using various protocols. It allows for making HTTP requests without a web browser.

### Check whether you have `cURL` installed

`cURL` should be installed in your computer so that Freesound.quark and the extensions built upon Freesound quark work. 

**For Windows users**: 

If you have version 1803 or later of Windows 10, cURL is installed by default. You can check whether you have `curl` installed by opening the command prompt (Windows).

If you don't know how to open the command prompt in Windows, here's a link that explains how to do it:

[https://www.ionos.co.uk/digitalguide/server/tools/open-command-prompt/](https://www.ionos.co.uk/digitalguide/server/tools/open-command-prompt/)

Once you have the command prompt open, type the following:

```
curl --help
```

If a help file is displayed, this means that curl is installed. If it is not installed, you will need to install it. 


**For Mac/Linux users**: 

`cURL` is installed by default on MacOS. Open the computer's terminal and type:

```
curl --help
```

If a help file is displayed, this means that curl is installed. If it is not installed, you will need to install it. 

### Installing `cURL` (if need be)

**For Windows users**: 

Here you can find two tutorials on how to install cURL on Windows: 

* [Installing and using cURL on Windows](https://developer.zendesk.com/documentation/developer-tools/getting-started/installing-and-using-curl/#windows)
* [Learn how to install & run cURL on Windows](https://help.ubidots.com/en/articles/2165289-learn-how-to-install-run-curl-on-windows-macosx-linux)

**For macOS users**: 

Here you can find two tutorials on how to install cURL on Mac: 

* [Installing and using cURL on macOS](https://developer.zendesk.com/documentation/developer-tools/getting-started/installing-and-using-curl/#macos)
* [Learn how to install & run cURL on macOS](https://help.ubidots.com/en/articles/2165289-learn-how-to-install-run-curl-on-windows-macosx-linux)

**For Linux users**: 

Here you can find one tutorial on how to install cURL on Linux using the computer's terminal: 

* [Learn how to install & run cURL on Windows](https://help.ubidots.com/en/articles/2165289-learn-how-to-install-run-curl-on-windows-macosx-linux)


## Step 3: Install Extensions

### Install Freesound Quark

Once you have executed `Quarks.gui` on the SuperCollider code editor, you will see a new window with a list of available Quarks. 

Select `Freesound` from the list and click the `+` button to install it.

Recompile SuperCollider from the upper menu: `Language > Recompile class library`

Type `Freesound` in the code editor and then press `control+D` to open the Help file.

**Note**: The quark has been installed the system application support directory `Platform.systemAppSupportDir` under the folder `downloaded-quarks`. To check that the installation has been successful, you can check it via your OS file directory manager (e.g. Finder on MacOS or File Manager on Windows).

To open the help file, press `command+D` next to the word `Freesound` in the SuperCollider code editor. As you will see in the Help file, you will need an API key to make it work:

```
Freesound.authType = "token";// default, only needed if you changed it
Freesound.token="<your_api_key>";
```

In this session, you will be provided with one. However, you are encouraged to request your own after the session. This will require that you have a Freesound.org user account. If you don't have a user account, consider creating one!

You can find more information about the API key here:

* [Freesound API for Developers explained](https://freesound.org/help/developers/)
* [Your Freesound API access credentials](https://freesound.org/apiv2/apply/)


### Install FluCoMa

Install FluCoMa from [https://www.flucoma.org](https://www.flucoma.org/download/)

Latest version: [https://github.com/flucoma/flucoma-sc/releases/latest](https://github.com/flucoma/flucoma-sc/releases/latest)

* `FluCoMa-SC-Linux.tar.gz` (1.0.8)
* `FluCoMa-SC-Mac.dmg` (1.0.8)
* `FluCoMa-SC-Windows.zip` (1.0.8)

Unzip the file.

Move the files to your SuperCollider `Extensions` folder. You can find its location typing `Platform.systemExtensionDir` on the SuperCollider code editor.

To check that it has been successfully installed, first recompile SuperCollider from the upper menu: `Language > Recompile class library`

**For MacOS users**: Before running the code, make sure to remove the quarantine attribute of the `FluidCorpusManipulation` folder typing the following in the Terminal:

```
sudo xattr -r -d com.apple.quarantine /path/to/MyApp.app
```

For example:

```
sudo xattr -r -d com.apple.quarantine /Library/Application Support/SuperCollider/Extensions/FluidCorpusManipulation/
```

You can find more info about the quarantine attribute here: 

* [Clearing the quarantine extended attribute from downloaded applications](https://derflounder.wordpress.com/2012/11/20/clearing-the-quarantine-extended-attribute-from-downloaded-applications/)

### Install MIRLC 2.0 & MIRLCa

MIRLC 2.0 and MIRLCa are two SuperCollider extensions (under development!). You can download a version under `SC Extensions` of this code repository.

Move the files to your SuperCollider `Extensions` folder. You can find its location typing `Platform.systemExtensionDir` on the SuperCollider code editor.

To check that it has been successfully installed, first recompile SuperCollider from the upper menu: `Language > Recompile class library`

**Note**: If you have MIRLCa installed from a previous version (e.g. 2021 MIRLCa workshops), it is worth noting that it does not work with the latest version of FluCoMa. There's a workaround but we recommend that you update the MIRLCa code as well. The error is related to the new version of FluCoMa and the generated `pca.JSON`, which will not work unless you change the following. Search for  `"cols": 26,` and rename it to `"rows": 26,` 
You can find more info here: 

* [ERROR: FluidPCA - Invalid JSON format #33 ](https://github.com/flucoma/flucoma-sc/issues/33)

### How to change the Freesound API key token on `MIRLC 2.0`

You will have to create your own Freesound API key token to make it work. 

To do that, you will need to request a Freesound API key token here: [https://freesound.org/apiv2/apply](https://freesound.org/apiv2/apply)

You can find more information about Token authentication on Freesound here:
[https://freesound.org/docs/api/authentication.html](https://freesound.org/docs/api/authentication.html)

Then, you can change the Freesound API key token on the class file `MIRLCRep2` under the `MIRLC 2.0` folder in the `Extensions` folder.

## Step 4: Test the extensions

If you haven't done it yet: In order to include the extensions in SuperCollider, you will need to recompile the class library. For that, click on the option `Language` at the top menu and select `Recompile Class Library`: 

`Language > Recompile class library`

## Step 5: Tutorial time!

Please refer to the tutorials of the workshop to investigate how each of the extensions work. Happy coding!

