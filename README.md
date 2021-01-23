# Game(Name undecided yet)
## Why I made this project
- This is a game I used to play when I was in my Primary school. I modified it a little bit. I cannot really remember all the details. So I added some.
- I have learned, and I am about to learn some Machine Learning algorithm. Instead of just practicing it in the Homework Proferssor gave. I would like to try those in real game to see is that better than the one I come up with without any learning.
- I hope I can design a game for myself. I want to be a Game Designer. 
## How to play this
Currently, the game is not playable online yet. There is two TODOs for this:
- Change the coding inline String to enums and provide English version of interface
- Put this into a website.
- Add more kind of players.
## Game Structure
This is a round game every player takes an available operation in one round, and keep doing so until they kill all the player that does not belongs to same team with them
## Roles
a/b means deal a damage when enemy is not defending, while deal b damage when enemy is defending
### Assassin/Ninja (name not decided yet)
Start with 0 energy, 1 health.
Always take one less damage
Ability:
- Shunsa

    takes 1 energy, target 1 enemy
    - a very small possibility to execute a enemy directly
    - a small possibility deal 10/6 damage
    - most likely deal 4/1 damage
    - a small possibility of miss
- Shuriken

    can be used only once, does not take energy, deal 2/0 damage
### Druid
- 
## The structure of the code
Player is the parent class of all kinds of players(Assassin, Druid...). That is also the core of this game.
Then the main part would be Game(SoloGame). Other class(record,Bot) is for machine learning. 
## If you want to help
I am very happy if you are going to help. Please Email yuan226@purdue.edu I will reply soon!   
