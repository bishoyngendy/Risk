# RISK Game
_Risk_ is a strategy board _game_ of diplomacy, conflict and conquest for two to six players.
This is a simplified version of RISK with only two players. It can be multi human players or the player can choose an agent to play against. 
Our game is supported by multiple AI and non AI agents that the user can choose one of them or he can go beyond that and let two AI agents play against each other and watch the result.

### Non AI Agents:
- Human Agent
- Passive Agent
- Aggressive Agent
- Pacifiest Agent

### AI Agents:
- Greedy Agent
- A* Agent
- Real Time A* Agent

## Project Structure
- **core**: contains the logic of transition between states, adding
armies, attacking and so on.
- **agents**: contains implementation of the seven agents required.
- **models**: contains the data classes of the vertex, board and so
on
- **Views**: contains the fxml files of the UI.
- **controllers**: contains JavaFX controllers that connect UI with
models.

## Sample Runs
#### Human V.S Agreesive Agent
![HUMAN_VS_AGENT](/screenshots/1.png?raw=true)
![HUMAN_VS_AGENT](/screenshots/2.png?raw=true)
![HUMAN_VS_AGENT](/screenshots/3.png?raw=true)

#### Greedy V.S Passive Agent
![GREEDY_VS_PASSIVE](/screenshots/4.png?raw=true)

#### A* V.S Passive Agent
![AStar_VS_PASSIVE](/screenshots/5.png?raw=true)
