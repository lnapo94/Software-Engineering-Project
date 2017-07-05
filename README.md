# Software Engineering group ps42
_**Authors:** [Luca Napoletano](https://github.com/lnapo94): 828037 **&** [Claudio Montanari](https://github.com/claudioMontanari): 829658_

### Welcome to our repository of the Software Engineering project!

This is our README file.

In this past months Claudio and I have implemented this game, _Lorenzo il Magnifico_, for our bachelor thesis. In this document we are going to explain how to start our implementation, focus on the Server and the Client, but first we want to explain which features we have implemented:

#### _Simplify rules_
We have implemented all the pricipals rules of the game

#### _Advanced rules_
We have implemented the Bans effects and the Leader Cards

#### _Socket Connections_
Our Server and our Client can handle the Socket-type connections
#### _RMI Connection_
Our Server and our Client can handle the connections which use Remote Method Invocation Standard
#### _Graphical User Interface_
We have implemented the GUI for the Client to play at this game

### How to Use our Distributed Application?
Our application is divided in two main part, Server and Client. First, you must start the server. To do it, you should go to the server package (it.polimi.ingsw.ps42.server) and then start the Server.java

After that, you can start the client. You should go to the client package (it.polimi.ingsw.ps42.client) and start the Client.java.
Now you can choose from 2 type of connection: RMI or Socket, to choose the RMI connection type "R", to choose the Socket connection type "S". Then you can choose from 2 type of user experience, the GUI interface or the CLI interface. We recommend you to use the GUI, and to do that type "G", but if you prefer an old-style user interface, you can choose the CLI typing "C" in the console. 

After all this operation, a client should be connected to the server, now you have to wait at least a second client to play this game.
When a second player will be connected to your same game, then a timer will start, and after 30 seconds (by default) the match will start with the players that are currently in that match.

The timers to wait the start of a match or to wait the player move is setted to 30 seconds, to change that you can go in Resource/Configuration/timers.json

Furthermore you can configure a lot of thing with our Game, such as cards effects, bans effects, resources, positions bonuses/maluses, the bonus bars effects to give to the player and so on...
Finally, we have used a logger to know everything in our application. We decided to use log4j, and, if you want, you can configure this logger easily, you should go in Logger/Properties/ to see all the configuration. At the moment, we have setted 3 kind of configuration, one for the client, one for the server and one for the test units. All this log file are stores Logger/ directory.

**Enjoy our _Lorenzo il Magnifico_ application**
