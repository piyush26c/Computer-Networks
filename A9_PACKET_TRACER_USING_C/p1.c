#include<stdio.h> //for standard things
#include<unistd.h>
#include<stdlib.h>    //malloc
#include<string.h>    //strlen

#include<netinet/udp.h>   //Provides declarations for udp header
#include<netinet/tcp.h>   //Provides declarations for tcp header
#include<netinet/ip.h>    //Provides declarations for ip header
#include<netinet/if_ether.h>  //For ETH_P_ALL
#include<netinet/in.h>

#include<net/ethernet.h>  //For ether_header
#include<netdb.h>

#include<sys/socket.h>
#include<sys/ioctl.h>
#include<sys/time.h>
#include<sys/types.h>

#include<arpa/inet.h>
#include<errno.h>

void ProcessPacket(unsigned char*, int);
void print_ip_header(unsigned char*, int);
void print_tcp_packet(unsigned char *, int);
void print_udp_packet(unsigned char *, int);


FILE *logfile;
struct sockaddr_in source, dest; //globlal structures _in
int tcp = 0, udp = 0, icmp = 0, others = 0, igmp = 0, total = 0, i, j; //[7 variables]

int main() {
    int saddr_size, data_size;
    struct sockaddr saddr; //local structures

    unsigned char *buffer = (unsigned char *) malloc(65536); //unsigned char buffer

    logfile = fopen("log.txt", "w"); //open log.txt file
    if (logfile == NULL) //validation of file open
    {
        printf("Unable to create log.txt file.");
    }
    printf("Starting...\n");

    //make socket
    //raw socket bypasses the normal TCP/IP processing and sends the packets to the specific user application
    int sock_raw = socket(AF_PACKET, SOCK_RAW, htons(ETH_P_ALL));

    if (sock_raw < 0) ///socket making validation
    {
        //Print the error with proper message
        perror("Socket Error");
        return 1;
    }
    while (1) {
        saddr_size = sizeof saddr;
        //Receive a packet
        data_size = recvfrom(sock_raw, buffer, 65536, 0, &saddr, (socklen_t*) & saddr_size);
        if (data_size < 0) {
            printf("Recvfrom error , failed to get packets\n");
            return 1;
        }
        //Now process the packet
        ProcessPacket(buffer, data_size);
    }
    close(sock_raw);
    printf("Finished");
    return 0;
}

void ProcessPacket(unsigned char* buffer, int size) {
    //Get the IP Header part of this packet , excluding the ethernet header
    struct iphdr *iph = (struct iphdr*) (buffer + sizeof (struct ethhdr)); //ip.h
    ++total;
    switch (iph->protocol) //Check the Protocol and do accordingly...
    {
        case 1: //ICMP Protocol
            ++icmp;
            // print_icmp_packet( buffer , size);
            break;

        case 2: //IGMP Protocol
            ++igmp;
            break;

        case 6: //TCP Protocol
            ++tcp;
            print_tcp_packet(buffer, size);
            break;

        case 17: //UDP Protocol
            ++udp;
            print_udp_packet(buffer, size);
            break;

        default: //Some Other Protocol like ARP etc.
            ++others;
            break;
    }
    printf("TCP : %d   UDP : %d   ICMP : %d   IGMP : %d   Others : %d   Total : %d\r", tcp, udp, icmp, igmp, others, total);
}

void print_ethernet_header(unsigned char* Buffer, int Size) {
    struct ethhdr *eth = (struct ethhdr *) Buffer;

    fprintf(logfile, "\n");
    fprintf(logfile, "Ethernet Header\n");

    fprintf(logfile, "   |-Destination Address : %.2X-%.2X-%.2X-%.2X-%.2X-%.2X \n", eth->h_dest[0], eth->h_dest[1], eth->h_dest[2], eth->h_dest[3], eth->h_dest[4], eth->h_dest[5]);

    fprintf(logfile, "   |-Source Address      : %.2X-%.2X-%.2X-%.2X-%.2X-%.2X \n", eth->h_source[0], eth->h_source[1], eth->h_source[2], eth->h_source[3], eth->h_source[4], eth->h_source[5]);

    fprintf(logfile, "   |-Protocol            : %u \n", (unsigned short) eth->h_proto);
}

void print_ip_header(unsigned char* Buffer, int Size) {
    print_ethernet_header(Buffer, Size);

    unsigned short iphdrlen;

    struct iphdr *iph = (struct iphdr *) (Buffer + sizeof (struct ethhdr));
    iphdrlen = iph->ihl * 4;

    memset(&source, 0, sizeof (source));
    source.sin_addr.s_addr = iph->saddr;

    memset(&dest, 0, sizeof (dest));
    dest.sin_addr.s_addr = iph->daddr;

    fprintf(logfile, "\n");
    fprintf(logfile, "IP Header\n");
    fprintf(logfile, "   |-IP Version        : %d\n", (unsigned int) iph->version);
    fprintf(logfile, "   |-IP Header Length  : %d DWORDS or %d Bytes\n", (unsigned int) iph->ihl, ((unsigned int) (iph->ihl))*4);
    fprintf(logfile, "   |-Type Of Service   : %d\n", (unsigned int) iph->tos);
    fprintf(logfile, "   |-IP Total Length   : %d  Bytes(Size of Packet)\n", ntohs(iph->tot_len));
    fprintf(logfile, "   |-Identification    : %d\n", ntohs(iph->id));
    //fprintf(logfile , "   |-Reserved ZERO Field   : %d\n",(unsigned int)iphdr->ip_reserved_zero);
    //fprintf(logfile , "   |-Dont Fragment Field   : %d\n",(unsigned int)iphdr->ip_dont_fragment);
    //fprintf(logfile , "   |-More Fragment Field   : %d\n",(unsigned int)iphdr->ip_more_fragment);
    fprintf(logfile, "   |-TTL      : %d\n", (unsigned int) iph->ttl);
    fprintf(logfile, "   |-Protocol : %d\n", (unsigned int) iph->protocol);
    fprintf(logfile, "   |-Checksum : %d\n", ntohs(iph->check));
    fprintf(logfile, "   |-Source IP        : %s\n", inet_ntoa(source.sin_addr));
    fprintf(logfile, "   |-Destination IP   : %s\n", inet_ntoa(dest.sin_addr));
}

void print_tcp_packet(unsigned char* Buffer, int Size) {
    unsigned short iphdrlen;

    struct iphdr *iph = (struct iphdr *) (Buffer + sizeof (struct ethhdr));
    iphdrlen = iph->ihl * 4;

    struct tcphdr *tcph = (struct tcphdr*) (Buffer + iphdrlen + sizeof (struct ethhdr));

    //int header_size =  sizeof(struct ethhdr) + iphdrlen + tcph->doff*4;

    fprintf(logfile, "\n\n***********************TCP Packet*************************\n");

    print_ip_header(Buffer, Size);

    fprintf(logfile, "\n");
    fprintf(logfile, "TCP Header\n");
    fprintf(logfile, "   |-Source Port      : %u\n", ntohs(tcph->source)); //16
    fprintf(logfile, "   |-Destination Port : %u\n", ntohs(tcph->dest)); //16
    fprintf(logfile, "   |-Sequence Number    : %u\n", ntohl(tcph->seq)); //32
    fprintf(logfile, "   |-Acknowledge Number : %u\n", ntohl(tcph->ack_seq)); //32
    fprintf(logfile, "   |-Header Length      : %d DWORDS or %d BYTES\n", (unsigned int) tcph->doff, (unsigned int) tcph->doff * 4);
    //fprintf(logfile , "   |-CWR Flag : %d\n",(unsigned int)tcph->cwr);
    //fprintf(logfile , "   |-ECN Flag : %d\n",(unsigned int)tcph->ece);
    fprintf(logfile, "   |-Urgent Flag          : %d\n", (unsigned int) tcph->urg);
    fprintf(logfile, "   |-Acknowledgement Flag : %d\n", (unsigned int) tcph->ack);
    fprintf(logfile, "   |-Push Flag            : %d\n", (unsigned int) tcph->psh);
    fprintf(logfile, "   |-Reset Flag           : %d\n", (unsigned int) tcph->rst);
    fprintf(logfile, "   |-Synchronise Flag     : %d\n", (unsigned int) tcph->syn);
    fprintf(logfile, "   |-Finish Flag          : %d\n", (unsigned int) tcph->fin);
    fprintf(logfile, "   |-Window         : %d\n", ntohs(tcph->window));
    fprintf(logfile, "   |-Checksum       : %d\n", ntohs(tcph->check));
    fprintf(logfile, "   |-Urgent Pointer : %d\n", tcph->urg_ptr);
    fprintf(logfile, "\n");

    //  fprintf(logfile , "IP Header\n");
    // fprintf(logfile , "TCP Header\n");
    fprintf(logfile, "\n###########################################################");
}

void print_udp_packet(unsigned char *Buffer, int Size) {

    unsigned short iphdrlen;

    struct iphdr *iph = (struct iphdr *) (Buffer + sizeof (struct ethhdr));
    iphdrlen = iph->ihl * 4;

    struct udphdr *udph = (struct udphdr*) (Buffer + iphdrlen + sizeof (struct ethhdr));

    //int header_size =  sizeof(struct ethhdr) + iphdrlen + sizeof udph;

    fprintf(logfile, "\n\n***********************UDP Packet*************************\n");

    print_ip_header(Buffer, Size);

    fprintf(logfile, "\nUDP Header\n");
    fprintf(logfile, "   |-Source Port      : %d\n", ntohs(udph->source));
    fprintf(logfile, "   |-Destination Port : %d\n", ntohs(udph->dest));
    fprintf(logfile, "   |-UDP Length       : %d\n", ntohs(udph->len));
    fprintf(logfile, "   |-UDP Checksum     : %d\n", ntohs(udph->check));

    fprintf(logfile, "\n");
    //fprintf(logfile , "IP Header\n");
    //fprintf(logfile , "UDP Header\n");
    fprintf(logfile, "\n###########################################################");
}