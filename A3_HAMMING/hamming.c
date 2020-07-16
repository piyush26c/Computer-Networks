#include<stdio.h>

void main() {
    int data[12], rec[12];
    int c1, c2, c4, c8, sum, i, ch;
    char z;
    do {
        printf("********** HAMMING CODE *********");
        printf("\nEnter 1.For Sender 2.For receiver :");
        scanf("%d", &ch);

        switch (ch) {

            case 1:

                printf("\nSENDER SIDE"); //only do hamming code generation here
                printf("\nEnter 8 message bits : ");
                scanf("%d%d%d%d%d%d%d%d", &data[0], &data[1], &data[2], &data[3], &data[5], &data[6], &data[7], &data[9]);

                data[11] = data[9]^data[7]^data[5]^data[3]^data[1];
                data[10] = data[9]^data[6]^data[5]^data[2]^data[1];
                data[8] = data[7]^data[6]^data[5]^data[0];
                data[4] = data[3]^data[2]^data[1]^data[0];

                printf("\nHamming code is : ");
                for (i = 0; i < 12; i++) {
                    printf("%d ", data[i]);
                }
                break;

            case 2:
                printf("\nRECEIVING SIDE"); //only do error detection here
                printf("\nEnter the receiving side code : ");
                for (i = 0; i < 12; i++) {
                    scanf("%d", &rec[i]);
                }
                c1 = rec[11]^rec[9]^rec[7]^rec[5]^rec[3]^rec[1];
                c2 = rec[10]^rec[9]^rec[6]^rec[5]^rec[2]^rec[1];
                c4 = rec[8]^rec[7]^rec[6]^rec[5]^rec[0];
                c8 = rec[4]^rec[3]^rec[2]^rec[1]^rec[0];

                sum = (c8 * 8)+(c4 * 4)+(c2 * 2)+(c1 * 1);

                if (sum == 0)
                    printf("\nNo error");
                else {
                    printf("\nThere is error at position :  %d", sum);
                    //correcting the error
                    if (rec[12 - sum] == 0)
                        rec[12 - sum] = 1;
                    else
                        rec[12 - sum] = 0;
                }
                printf("\nThe corrected hamming code is :");
                for (i = 0; i < 12; i++) {
                    printf("%d ", rec[i]);
                }
                break;
        }
        printf("\nWant to continue? (Y/N) :");
        scanf("%s", &z); /*use %s only*/
    } while (z == 'y' || z == 'Y');
}
