# Secure Messager

### Summary
Secure-Messager is a pair of applications; a client and a server. The two applications work together to allow a user to securely send messages to other clients connected to the server. The server will generate a symmetric key for each client for communication. Symmetric keys are encrypted using RSA encryption before being sent to the client making it impossible for anyone using modern technology, except for the intended user, to decrypt the symmetric key.

</br>

### Features

	- 4096-bit RSA Encryption for secure symmetric key sharing
	- Vignere cipher used to encrypt all data transferred between the clients and the server
	- Custom, unique usernames for each client
	- Intuitive client GUI
	- Multi-threaded server to allow multiple clients to connect to the same server
	
</br>

### Usage
To run Secure-Messager make sure that Java is installed on the system. Source files for each application need to be compiled separately (compiling to a .jar format may make running the application easier). Then you may either run the 
