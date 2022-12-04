To compile Battle Star Bird, change directory into the src folder using the command line. Once in the src folder, typing
javac *.java will compile our game. After compiling, typing java Main in the command line will run Battle Star Bird. You
will be asked to input a name so that your scores will be saved under that name. You can enter any name that is between 1 & 9 
characters (inclusive with no space). After entering your name,
a window with Battle Star Bird's title screen will pop up. From this start screen, you can press "l" to view historical
high scores, "s" for your individual player's historical stats, and "c" for instructions to play.
To start the game, choose your difficulty by pressing '1', '2', or '3'. To play the game, press spacebar to jump.
The objective of the game is to get the highest score
possible by passing as many of the brick columns as you can. Avoid losing health from the enemies (evil-looking
floating characters) by avoiding touching them or shoot them dead by pressing 's'. Be aware that you can only 
shoot a certain number of bullets before your character has to reload, which is a period 
of a couple seconds where your player cannot shoot (number of bullets & duration of period depends on difficulty level). 
Press 'p' to pause gameplay. 
Once your character dies by hitting the brick columns or
hitting too many enemies, the game will end. You will have the option to return to the start screen by pressing enter.
Once you return to the start screen, you can start a new game by choosing difficulty again.
Our program saves historical scores by reading an input file called "score.txt", and after every game the score
gets appended to this same file. If you would like to reset progress or save different scores, you can edit the "score.txt"
file. Each line corresponds to the result of one game where the player name, score, and difficulty level are separated
by one space. The player name must be a string with no space, the score must be an integer, the difficulty level must be an integer
between 1 and 3 inclusive.