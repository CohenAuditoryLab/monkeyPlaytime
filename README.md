# monkeyPlaytime
NHP enrichment app for playing audio, video, and mini games.

## Using the App

### Dependencies
64-bit versions of the following programs, installed in their default locations:
- Java JDK
- VLC

### Running the App
Download the ``media`` folder and ``monkeyPlaytime.jar``. Make sure the file and folder are in the same folder (e.g. Desktop).
Navigate to this folder in terminal and run ``java -jar monkeyPlaytime.jar`` to run the app.<br>

### Managing Sessions
Each session will prompt the user to select the monkey who will be playing, therefore sessions should be restricted to a single monkey. When a session is over, simply close the app; all data is already saved.

### General Play Instructions
The main page acts as navigation: press triangle for audio, square for video, and circle for auditory task. The red buttons on each of the media pages return the user to the main page. When a monkey selects a media file to play, the media file will play until completion before the monkey may select another media file to play. 

## File Management

### Customizing Media Files
The ``media`` folder contains ``mediaFiles.txt``. Change the file names in this file to change which media files the main app will play, making sure to also add these new files to the ``media`` folder.

### Customizing Monkey Names
The ``data`` folder contains ``monkeys.txt``. Each name should occupy its own line in the file, thus you may add and remove lines as necessary. Before saving, make sure to update the number of monkeys on the first line to reflect the number of names listed in the file.

### Data Files
Files are saved as CSV files titled with the monkey's name and the session start time. The first few lines of the file are dedicated to the metadata, which includes the monkey's
name, session start time, and three rows specifying the media files associated with each button.<br>
The remaining rows of the file are devoted to action data. When the monkey plays a media file or presses a response button in the task tab, this action data is written as a new row in the file; action data includes the time the action occurred, the current tab, the index of the button, and the media index played.