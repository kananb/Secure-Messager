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
To use the Secure-Messager application pair you will first have to compile the source files provided in this repository.

##### Client
1. Compile the client source files
2. Compress the files into a `.jar` file format (optional)
3. Run the Launcher class through the command line or double-click the newly created `.jar`

##### Server
1. Compile the server source files
2. Compress the files into a `.jar` file format (optional)
3. Run the Launcher class or the newly created `.jar` through the command line
