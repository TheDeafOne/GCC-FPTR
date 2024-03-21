# Running the program:
## Compiling
ensure you are the root directory of the project (GCC-FTPR)
compile the java files: `javac .\src\*.java`

## Running
Split the terminal into two windows. In the first window, run the server: ` java -cp src FtpServer`
In the second window, run the client: `java -cp src FtpClient `

## Using the program
The client will prompt you for a command. The following commands are available:
- `ls` - list the files in the current directory
- `pwd` - print the current working directory
- `get <filename>` - download a file from the server
- `put <filename>` - upload a file to the server

## Exiting the program
To exit the program, type `quit` into the client terminal. This will close the client and server.