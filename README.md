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

### Bugs that I accidentally handed in:
- If you press enter at the very start it takes you straight to the desmond minigame
- EASY, MEDIUM, and HARD lines on the welcome message are coloured wrong - colour is shifted down by one

### IF I HAD A TIME MACHINE
- I WOULD COMMENT OUT LINE 287 AND FIX THE ENTIRE GAME
> nameInput.addActionListener(this);

- I WOULD FIX THE COLOURING AT LINES 164 - 169 FROM
>           g.drawString(welcomeMessage[6], WELCOMEX, WELCOMEY+80);
>           g.setColor(lightGreen);
>           g.drawString(welcomeMessage[7], WELCOMEX, WELCOMEY+120);
>           g.setColor(orange);
>           g.drawString(welcomeMessage[8], WELCOMEX, WELCOMEY+160);
>           g.setColor(red);
>           g.drawString(welcomeMessage[9], WELCOMEX, WELCOMEY+200);
>           g.setColor(black);
>           g.drawString(welcomeMessage[10], WELCOMEX, WELCOMEY+240);    
TO
>           g.setColor(lightGreen);
>           g.drawString(welcomeMessage[6], WELCOMEX, WELCOMEY+80);
>           g.setColor(orange);
>           g.drawString(welcomeMessage[7], WELCOMEX, WELCOMEY+120);
>           g.setColor(red);
>           g.drawString(welcomeMessage[8], WELCOMEX, WELCOMEY+160);
>           g.setColor(black);
>           g.drawString(welcomeMessage[9], WELCOMEX, WELCOMEY+200);
>           g.drawString(welcomeMessage[10], WELCOMEX, WELCOMEY+240);    

### What I lost marks for
Program not marked yet
