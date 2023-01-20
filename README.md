# ICS3U1-ASSIGNMENT-2

This program was written as a culminating for Grade 11 computer science

It includes a base solve, (SaveDesmond) that I quickly finished to get an idea of the game

The applet extension (SaveDesmondApplet) took 90% of the time and was the only thing I handed in. It uses Java Applets but in hindsight I probably should have used Swing.

**Program: Save Desmond Game**

Task: Find a lost child (Desmond) who randomly moves once per turn, and bring them home whilst avoiding zombies.

2100+ lines of code across 2 files

## Additional Features:
- Done in applets
- Difficulty selection (easy - you can see everything, medium - you can only see zombies, hard - everything is invisible)
- Minigames are played when the player runs into Desmond or a zombie.
- Zombie minigame: speed target test
- Desmond minigame: click the targets in the correct order
- Lives system - lives decrease if you lose a minigame
- Heart powerup - a heart spawns on the map and if you pick it up you gain a life
- Highscore system - stores the move count, time taken, and calculated highscore when someone wins a game
- Records the time it took for someone to beat the game
- Sorts all highscores and displays them on 3 seperate leaderboards for the 3 categories
- Developer mode - has WIN, GIVE UP, and CHEAT buttons, the WIN and GIVE UP buttons are self explanatory, the CHEAT button reveals Desmond's location
- Distance from Desmond is calculated every turn and the status is displayed
- Decaying path - player cannot step on a path they have already been on, resets after 10 turns
- Extended the board size to 17x17
- Random number of zombies (14-18)
- Desmond and zombies move randomly whilst avoiding obstacles (decaying path, other NPC's, powerup)
