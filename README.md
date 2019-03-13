# Secure Messager

# Summary
Secure-Messager is a pair of applications; a client and a server. The two applications work together to allow a user to securely send messages to other clients connected to the server. The server will generate a symmetric key for each client for communication. Symmetric keys are encrypted using RSA encryption before being sent to the client making it impossible for anyone using modern technology, except for the intended user, to decrypt the symmetric key.

</br>

# Features

	- 4096-bit RSA Encryption for secure symmetric key sharing
	- Vignere cipher used to encrypt all data transferred between the clients and the server
	- Custom, unique usernames for each client
	- Intuitive client GUI
	- Multi-threaded server to allow multiple clients to connect to the same server
	
</br>

# Compiling
First make a directory to clone the source files into and cd into that new directory
then do:
```
> git clone https://github.com/kananb/Secure-Messager
> cd Secure-Messager
> mkdir build
```

After that you can start compiling the source files

## Client
Starting inside the cloned directory (Secure-Messager)
```
> cd client
> javac -d ../build *.java gui/*.java gui/loading/*.java encryption/*.java
> cd ../build
> jar cvfe Client.jar client.Launcher client/*.class client/gui/*.class client/gui/loading/*.class client/encryption/*.class
```

Now there should be a file called Client.jar in the `build` directory. You can double click it to open the client application.

## Server
Starting inside the cloned directory (Secure-Messager)
```
> cd server
> javac -d ../build *.java encryption/*.java
> cd ../build
> jar cvfe Server.jar server.Launcher server/*.class server/encryption/*.class
```

Now there should be a file called Server.jar in `Secure-Messager/build`. See how to run the server in the **Usage** section below.

</br>

# Usage
To use the Secure-Messager application pair you will first have to compile the source files provided in this repository.

## Client
Simply double click Client.jar in the build directory. If you don't have this file or folder see the instructions for compiling the source code above.

## Server
Open your favorite command line interpreter, cd into the cloned directory (Secure-Messager), and enter the following command.
```
> java -jar build/Server.jar [port]
```
###### * the `port` argument is optional and will default to `12345` if left out
If you get this error: `Error: Unable to access jarfile build/Server.jar` see the instructions for compiling the source code above.
