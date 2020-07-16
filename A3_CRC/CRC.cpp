#include<bits/stdc++.h>
using namespace std;

class crc {
  public:
    int M[30], temp[30], G[30], CRC[30];
    int ms, gs, i, j, k, r;

    void accept() {
        cout << "\nEnter the size of message :";
        cin >> ms;
        cout << "\nEnter individual bits :";
        for (i = 0; i < ms; i++)
            cin >> M[i];
        cout << "Message code = ";
        for (i = 0; i < ms; i++)
            cout << M[i];
        cout << endl;

        cout << "\nEnter the size of Generator :";
        cin >> gs;
        cout << "\nEnter individual bits :";
        for (i = 0; i < gs; i++)
            cin >> G[i];
        cout << "Generator code = ";
        for (i = 0; i < gs; i++)
            cout << G[i];
        cout << endl;
    }

    void calculation_sender() {
        r = gs - 1;
        for (i = 0; i < ms; i++) //made copy of original message
            temp[i] = M[i];

        for (i = ms; i < ms + r; i++) //appending 0 at end
            temp[i] = 0;

        cout << "Dividend (Temp data) = ";
        for (i = 0; i < ms + r; i++)
            cout << temp[i];
        cout << endl;

        for (i = 0; i < ms; i++) {
            j = 0, k = i; //IMP

            if (temp[k] >= G[j]) {
                for (j = 0; j < gs; j++, k++) {
                    temp[k] = temp[k]^G[j];
                }
            }
        }
        //getting last r bits from temp(jithun zeros append zale hote )
        for (i = 0, k = ms; i < r; i++, k++)
            CRC[i] = temp[k];

        cout << "\nSender side CRC : ";
        for (i = 0; i < r; i++)
            cout << CRC[i];

        //appending that obtained crc at end of "original" message bit
        for (i = ms, k = 0; i < ms + r; i++, k++)
            M[i] = CRC[k];
        cout << endl;

        cout << "\nMessage after CRC appending :";
        for (i = 0; i < ms + r; i++)
            cout << M[i];
        cout << endl;
    }

    void calculation_receiver() {
        r = gs - 1;
        //for receiver the message contains CRC inbuilt hence their is no need for appending zeros
        //there no need of any appending here
        for (i = 0; i < ms; i++) //made copy of original message
            temp[i] = M[i];

        cout << "Dividend (Temp data) = ";
        for (i = 0; i < ms; i++)
            cout << temp[i];
        cout << endl;

        for (i = 0; i < ms - r; i++) //don't forget to do ms-r
        {
            j = 0, k = i; //IMP
            if (temp[k] >= G[j]) {
                for (j = 0; j < gs; j++, k++) {
                    temp[k] = temp[k]^G[j];
                }
            }
        }
        //getting last r bits from temp
        for (i = 0, k = ms - r; i < r; i++, k++)
            CRC[i] = temp[k];
        cout << "\nReceiver side CRC : ";
        for (i = 0; i < r; i++)
            cout << CRC[i];
    }

    void sender() { //enter only message bits without redundant zeros
        accept();
        calculation_sender();
    }

    void receiver() {
        cout << "***** Give the received message here ****";
        accept(); //give the entire received message here T(x)
        calculation_receiver();

        int sum = 0;

        for (i = 0; i < r; i++) {
            sum = sum + CRC[i];
        }

        if (sum == 0)
            cout << "\nNO ERROR IN THE RECEIVED MESSAGE";
        else
            cout << "\nERROR IN THE RECEIVED MESSAGE" << endl;
    }
};

int main() {
    crc obj;
    int ch;
    char z;
    do {
        cout << "\n***** CRC *****\n";
        cout << "\nEnter 1.For Sender";
        cout << "\nEnter 2.For receiver :";
        cin >> ch;

        switch (ch) {
            case 1: obj.sender();
                break;
            case 2: obj.receiver();
                break;
        }
        cout << "\nWant to continue? (y/n) :";
        cin >> z;
    } while (z == 'y' || z == 'Y');
}
