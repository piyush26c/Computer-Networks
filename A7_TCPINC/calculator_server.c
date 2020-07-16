/*
        Server Side
        Please pass port no as command line argument
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <sys/types.h>
#include <sys/socket.h>

#include <netinet/in.h>
#include <ctype.h>

void error(const char *msg) {
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[]) {
    int sockfd, newsockfd, portno;
    struct sockaddr_in serv_addr, cli_addr;
    socklen_t clilen;
    char buffer[1024];
    int n;

    if (argc < 2) {
        fprintf(stderr, "ERROR, no port provided\n");
        exit(1);
    }
    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    if (sockfd < 0)
        error("ERROR opening socket");

    bzero((char *) &serv_addr, sizeof (serv_addr));
    portno = atoi(argv[1]);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);

    if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof (serv_addr)) < 0)
        error("ERROR on binding");

    listen(sockfd, 5); //(accepting connection, no. of connection it can withold in queue)
    clilen = sizeof (cli_addr);
    newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, &clilen);

    if (newsockfd < 0)
        error("ERROR on accept");

    int num1, num2, ans, choice;
S:
    n = write(newsockfd, "Enter Number 1 : ", strlen("Enter Number 1")); //Ask for Number 1
    if (n < 0) error("ERROR writing to socket");
    read(newsockfd, &num1, sizeof (int)); //Read No 1
    printf("Client - Number 1 is : %d\n", num1);

    n = write(newsockfd, "Enter Number 2 : ", strlen("Enter Number 2")); //Ask for Number 2
    if (n < 0) error("ERROR writing to socket");
    read(newsockfd, &num2, sizeof (int)); //Read Number 2
    printf("Client - Number 2 is : %d\n", num2);

    n = write(newsockfd, "Enter your choice : \n1.Addition\n2.Subtraction\n3.Multiplication\n4.Division\n5.Exit\n", strlen("Enter your choice : \n1.Addition\n2.Subtraction\n3.Multiplication\n4.Division\n5.Exit\n")); //Ask for choice

    if (n < 0)
        error("ERROR writing to socket");

    read(newsockfd, &choice, sizeof (int)); //Read choice
    printf("Client - Choice is : %d\n", choice);

    switch (choice) {
        case 1:
            ans = num1 + num2;
            break;
        case 2:
            ans = num1 - num2;
            break;
        case 3:
            ans = num1*num2;
            break;
        case 4:
            ans = num1 / num2;
            break;
        case 5:
            goto Q;
            break;
    }

    write(newsockfd, &ans, sizeof (int));
    if (choice != 5)
        goto S;

Q:
    close(newsockfd);
    close(sockfd);
    return 0;
}
/*
Server side terminal:
jj@ubuntu:~/Desktop$ gcc Server.c -o Server
jj@ubuntu:~/Desktop$ ./Server 4562
Client - Number 1 is : 50
Client - Number 2 is : 110
Client - Choice is : 1
Client - Number 1 is : 50
Client - Number 2 is : 60
Client - Choice is : 2
Client - Number 1 is : 4
Client - Number 2 is : 8
Client - Choice is : 3
Client - Number 1 is : 66
Client - Number 2 is : 11
Client - Choice is : 4
Client - Number 1 is : 5
Client - Number 2 is : 7
Client - Choice is : 5
jj@ubuntu:~/Desktop$


Client side Terminal:

jj@ubuntu:~/Desktop$ gcc client.c -o client
jj@ubuntu:~/Desktop$ ./client 192.168.244.139 4562
Server - Enter Number 1
50
Server - Enter Number 2
110
Server - Enter your choice :
1.Addition
2.Subtraction
3.Multiplication
4.Division
5.Exit
1
Server - The answer is : 160
Server - Enter Number 1
50
Server - Enter Number 2
60
Server - Enter your choice :
1.Addition
2.Subtraction
3.Multiplication
4.Division
5.Exit
2
Server - The answer is : -10
Server - Enter Number 1
4
Server - Enter Number 2
8
Server - Enter your choice :
1.Addition
2.Subtraction
3.Multiplication
4.Division
5.Exit
3
Server - The answer is : 32
Server - Enter Number 1
66
Server - Enter Number 2
11
Server - Enter your choice :
1.Addition
2.Subtraction
3.Multiplication
4.Division
5.Exit
4
Server - The answer is : 6
Server - Enter Number 1
5
Server - Enter Number 2
7
Server - Enter your choice :
1.Addition
2.Subtraction
3.Multiplication
4.Division
5.Exit
5
You chose to exit, Thank You so much.jj@ubuntu:~/Desktop$

 */