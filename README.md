# Software Engineering group ps42
_**Authors:** [Luca Napoletano](https://github.com/lnapo94): 828037 **&** [Claudio Montanari](https://github.com/claudioMontanari): 829658_

### Welcome to our repository of the Software Engineering project!

This is our README file.

In this past months Claudio and I have implemented this game, _Lorenzo il Magnifico_, for our bachelor thesis. In this document we are going to explain how to start our implementation, focus on the Server and the Client, but first we want to explain which features we have implemented:

#### _Simplified rules_
We have implemented all the basic rules of the game

#### _Advanced rules_
We have implemented the advanced rules of the game, including the Bans effects and the Leader Cards

#### _Socket Connections_
Our Server and our Client can handle the Socket-type connections
#### _RMI Connection_
Our Server and our Client can handle the connections which use Remote Method Invocation Standard
#### _Graphical User Interface_
We have implemented a GUI for the Client to play at this game

### How to Use our Distributed Application?
Our application is divided in two main part, Server and Client. First, you must start the server. To do it, you should go to the server package (it.polimi.ingsw.ps42.server) and then start the Server.java

After that, you can start the client. You should go to the client package (it.polimi.ingsw.ps42.client) and start the Client.java.
Now you can choose from 2 type of connection: RMI or Socket, to choose the RMI connection type "R", to choose the Socket connection type "S". Then the Graphical User Interface will start in a few seconds. At the beginning of the match you have to choose your username to start the match. In the Server cannot be 2 equals username, but when you type your username, if the Server discard your chosen ID, you will can choose another one.

After all this operation, a client should be connected to the server, now you have to wait at least a second client to play this game.
When a second player will be connected to your same game, then a timer will start, and after 15 seconds (by default) the match will start with the players that are currently in that match.

The timers to wait the start of a match is setted to 15 seconds, while the timer to wait a player move is setted to 300 seconds (5 minutes). To change that you can go in Resource/Configuration/timers.json

Furthermore you can configure a lot of things with our Game, such as cards effects, bans effects, resources, positions bonuses/maluses, the bonus bars effects to give to the player and so on...
Finally, we have used a logger to know everything in our application. We decided to use log4j, and, if you want, you can configure this logger easily going in Logger/Properties/ to see all the configuration. At the moment, we have setted 3 kind of configuration, one for the client, one for the server and one for the test units. All this log file are stored in Logger/ directory.

### Do you want to know more about us and our implementation?
Please, if you want to know more about us and our implementation, check out our [Wiki](https://github.com/lnapo94/Software-Engineering-Project/wiki) on GitHub. There you can find a deeper description of the project, and some picture that explain the _UML_ of the entire project

**Enjoy our _Lorenzo il Magnifico_ application**
