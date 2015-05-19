# MCTF.web

The project is part of "Multi-Channel Telecommunication Framework", responsible for relaying messags between local applications,
consisting of "STUN Server" and "TURN Server".

"STUN Server" acts as a rendezvous server in "UDP hole punching" method to solve NAT traversal problem, providing services for discovering endpoints. 
It has three services, "REGISTER" is for registering private and public endpoints of the requesting entity, 
"GETINFO" is for getting endpoints of a specific entity 
and "UNREGISTER" is for removing the record of endpoints of the requesting entity.

"TURN Server" is responsible for relaying messages among entities. 
It has two services, "RELAY" is for notifying the server to relay messages for the requesting entity 
and "UNRELAY" is for stopping the relay.
