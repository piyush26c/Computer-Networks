/*
        Client Side
        Please pass ip address and port no as command line arguments in the same sequence
        glad.txt file should be already created before you run the program.
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#include <sys/types.h>
#include <sys/socket.h>

#include <netinet/in.h>
#include <netdb.h>
#include<ctype.h>

void error(const char *msg) {
    perror(msg);
    exit(0);
}

int main(int argc, char *argv[]) {
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    char buffer[512];
    if (argc < 3) {
        fprintf(stderr, "usage %s hostname port\n", argv[0]);
        exit(0);
    }

    sockfd = socket(AF_INET, SOCK_STREAM, 0); //IPv4 protocol

    if (sockfd < 0)
        error("ERROR opening socket");

    server = gethostbyname(argv[1]);

    if (server == NULL) {
        fprintf(stderr, "ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof (serv_addr));
    portno = atoi(argv[2]);
    serv_addr.sin_family = AF_INET;
    bcopy((char *) server->h_addr, (char *) &serv_addr.sin_addr.s_addr, server->h_length);
    serv_addr.sin_port = htons(portno); //convert an IP port number in host byte order to the IP port number in network byte order.

    if (connect(sockfd, (struct sockaddr *) &serv_addr, sizeof (serv_addr)) < 0)
        error("ERROR connecting");

    bzero(buffer, 512);
    FILE *f;

    int words = 0;
    char c;
    f = fopen("glad.txt", "r");
    //Counting No of words in the file
    while ((c = getc(f)) != EOF) {
        //fscanf(f , "%s" , buffer);
        if (isspace(c) || c == '\t')
            words++;
    }
    //printf("Words = %d \n"  , words);	//Ignore

    write(sockfd, &words, sizeof (int));
    rewind(f);

    char ch;
    while (ch != EOF) {
        fscanf(f, "%s", buffer);
        //printf("%s\n" , buffer);	//Ignore
        write(sockfd, buffer, 512);
        ch = fgetc(f);
    }
    printf("The file was sent successfully");

    close(sockfd);
    return 0;
}