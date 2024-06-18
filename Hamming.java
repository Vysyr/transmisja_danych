import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.pow;

public class Hamming {

    public static String Coder74(String word, boolean breaker) {
        String codedArr = "";
        String byteArr = utils.bitStream(word);
        int m = byteArr.length();
        while(m%4 != 0){
            byteArr += '0';
            m++;
        }
        int indx = 0;
        while(indx < m - 1){
            String c = byteArr.substring(indx, indx + 4);
            char[] chs = c.toCharArray();
            for (int i = 0; i < 4; i++){
                chs[i] -= 48;
                //System.out.println("value:"+(int)chs[i]);
            }

            int x1 = (chs[0] + chs[1] + chs[3])%2;
            int x2 = (chs[0] + chs[2] + chs[3])%2;
            int x4 = (chs[1] + chs[2] + chs[3])%2;
            if(breaker) { //breaking stuff
                int rand = (int) (Math.random() * 3);
                if (chs[rand] == 0) {
                    chs[rand] = 1;
                } else {
                    chs[rand] = 0;
                }
            }
           // codedArr += Integer.toString(x1) + Integer.toString(x2) + chs[0] + Integer.toString(x4) + chs[1] + chs[2] + chs[3];
            codedArr += x1 +""+ x2 + Character.toString(chs[0] + 48) +
            x4 + Character.toString(chs[1] + 48) +  Character.toString(chs[2] + 48) +
                    Character.toString(chs[3] + 48);


            indx += 4;
        }

            return codedArr;
        }

        public static String Decoder74(String codedArr) {
            String decodedArr = "";
            char[] chars = codedArr.toCharArray();
            codedArr = new String(chars);
            int m = codedArr.length();
            int indx = 0;
            while(indx + 7 <= m){
                String c = codedArr.substring(indx,indx+7);
                char[] chs = c.toCharArray();
                for (int i = 0; i < 7; i++) {
                    chs[i] -= 48;
                }
                int x1p = (chs[2] + chs[4] + chs[6]) % 2;
                int x2p = (chs[2] + chs[5] + chs[6]) % 2;
                int x4p = (chs[4] + chs[5] + chs[6]) % 2;

                int x1 = (chs[0] + x1p) % 2;
                int x2 = (chs[1] + x2p) % 2;
                int x4 = (chs[3] + x4p) % 2;

                int S = x1 + x2 * 2 + x4 * 4;
                if(S != 0) {
                    S--;
                    if (chs[S] == 1)
                        chs[S] = 0;
                    else {
                        chs[S] = 1;
                    }
                }
                    decodedArr += Character.toString(chs[2] + 48) +
                            Character.toString(chs[4] + 48) +
                            Character.toString(chs[5] + 48) +
                            Character.toString(chs[6] + 48);

                indx += 7;
            }
            return decodedArr;
        }

        public static String Coder1511(String word, boolean breaker){
            String ret = "";
            String codedArr = "";
            String byteArr = utils.bitStream(word);
            int m = byteArr.length();
            while(m%11 != 0){
                byteArr += '0';
                m++;
            }

            int[][] G = new int[][]{
                    //  P    |      I
                    {0,0,1,1, 1,0,0,0,0,0,0,0,0,0,0},
                    {0,1,0,1, 0,1,0,0,0,0,0,0,0,0,0},
                    {0,1,1,0, 0,0,1,0,0,0,0,0,0,0,0},
                    {0,1,1,1, 0,0,0,1,0,0,0,0,0,0,0},
                    {1,0,0,1, 0,0,0,0,1,0,0,0,0,0,0},
                    {1,0,1,0, 0,0,0,0,0,1,0,0,0,0,0},
                    {1,0,1,1, 0,0,0,0,0,0,1,0,0,0,0},
                    {1,1,0,0, 0,0,0,0,0,0,0,1,0,0,0},
                    {1,1,0,1, 0,0,0,0,0,0,0,0,1,0,0},
                    {1,1,1,0, 0,0,0,0,0,0,0,0,0,1,0},
                    {1,1,1,1, 0,0,0,0,0,0,0,0,0,0,1}
            };
            int indx = 0;
            while(indx < m - 1){
                String b = byteArr.substring(indx, indx + 11);
                char[] chs = b.toCharArray();
                for (int i = 0; i < 11; i++){
                    chs[i] -= 48;
                    //System.out.println("value:"+(int)chs[i]);
                }
                int[][] B = new int[1][11];//maybe blad here
                for(int i = 0; i < 11; i++){
                    B[0][i] = chs[i];
                    //System.out.print(chs[i]);
                }
                //System.out.print("\n");

                int[][] C = utils.multiplyMatrices(B,G);
                for(int i = 0; i < 15; i++) {
                    //System.out.print(C[0][i]);
                    C[0][i] = C[0][i] % 2;
                }
                //System.out.print("\n");
                if(breaker) { //breaking stuff
                    int rand = (int) (Math.random() * 14);
                    while(rand < 4){
                        rand = (int)(Math.random() * 14);
                    }
                    //System.out.print(rand+" ");
                    if (C[0][rand] == 0) {
                        C[0][rand] = 1;
                    } else {
                        C[0][rand] = 0;
                    }
                }

                for(int i = 0; i < 15; i++) {
                    ret+=C[0][i];
                }


                indx += 11;
            }


            //System.out.println("");
            return ret;
        }
        public static String Decoder1511(String codedArr){
            String decodedArr = "";
            char[] chars = codedArr.toCharArray();
            codedArr = new String(chars);
            int m = codedArr.length();
            int indx = 0;
            int[][] Ht = new int[][]{
                    {1,0,0,0},
                    {0,1,0,0},
                    {0,0,1,0},
                    {0,0,0,1},

                    {0,0,1,1},
                    {0,1,0,1},
                    {0,1,1,0},
                    {0,1,1,1},

                    {1,0,0,1},
                    {1,0,1,0},
                    {1,0,1,1},
                    {1,1,0,0},

                    {1,1,0,1},
                    {1,1,1,0},
                    {1,1,1,1}
            };

            while(indx + 15 <= m){
                String c = codedArr.substring(indx,indx+15);
                char[] chs = c.toCharArray();
                for (int i = 0; i < 15; i++) {
                    chs[i] -= 48;
                }
                int[][] C = new int[1][15];//maybe blad here
                for(int i = 0; i < 15; i++){
                    C[0][i] = chs[i];
                    //System.out.print(chs[i]);
                }
                int[][] s = utils.multiplyMatrices(C,Ht);

                int x1 = s[0][0] % 2;
                int x2 = s[0][1] % 2;
                int x4 = s[0][2] % 2;
                int x8 = s[0][3] % 2;
                int S = x1 * 8 + x2 * 4 + x4 * 2 + x8;
                //System.out.print(S+" ");
                if(S != 0){
                    if(S>=9)
                        S--;
                    else if (S==3){
                        S++;
                    }
                    if (chs[S] == 1)
                        chs[S] = 0;
                    else {
                        chs[S] = 1;
                    }
                }
                for(int i = 4; i < 15; i++)
                    decodedArr += Character.toString(chs[i] + 48);

                indx += 15;
            }
            //System.out.println("");
            return decodedArr;
        }

        public static void ask_testing(String message, double Tb, double fn, double A1, double A2, int Fp) throws IOException {
            String Hmg74 = Hamming.Coder74(message,true);
            String Hmg1511 = Hamming.Coder1511(message,true);
            double[][] X74 = signals.generate_Za(Tb,fn,Hmg74,A1,A2,Fp,false);
            double[][] X1511 = signals.generate_Za(Tb,fn,Hmg1511,A1,A2,Fp,false);
            double BER = 0.0;
            int n = 0;
            double optimalA = 0.0;
            double worstA = 0.0;
            double optimalB = 0.0;
            double worstB = 0.0;
            double BERmin = 1.0;
            double BERmax = 0.0;
            //generating broken signals
            for(double a = 0.0; a <= 1.0; a += 0.01){
                for(double B = 0.0; B <= 10.0; B += 0.1){
                    n++;
                    double[][] x1 = signals.multiplyWithWhiteNoise(X74,a);
                    double[][] x2 = signals.generateMuffledSignal(X74,B);
                    double[][] x3 = signals.multiplyWithWhiteNoise(X74,a);
                    x3 = signals.generateMuffledSignal(x3,B);
                    double[][] x4 = signals.generateMuffledSignal(X74, B);
                    x4 = signals.multiplyWithWhiteNoise(x4,a);

                    double[][] x5 = signals.multiplyWithWhiteNoise(X1511,a);
                    double[][] x6 = signals.generateMuffledSignal(X1511,B);
                    double[][] x7 = signals.multiplyWithWhiteNoise(X1511,a);
                    x7 = signals.generateMuffledSignal(x7,B);
                    double[][] x8 = signals.generateMuffledSignal(X1511, B);
                    x8 = signals.multiplyWithWhiteNoise(x8,a);

                    double ber = 0.0;
                    String bitsDec="";
                    bitsDec = signals.demodulateASK(x1, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder74(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    bitsDec = signals.demodulateASK(x2, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder74(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    bitsDec = signals.demodulateASK(x3, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder74(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    bitsDec = signals.demodulateASK(x4, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder74(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    bitsDec = signals.demodulateASK(x5, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder1511(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    bitsDec = signals.demodulateASK(x6, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder1511(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    bitsDec = signals.demodulateASK(x7, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder1511(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    bitsDec = signals.demodulateASK(x8, fn, Tb, Fp);
                    bitsDec = Hamming.Decoder1511(bitsDec);
                    ber += utils.getBER(bitsDec,utils.bitStream(message));

                    ber /= 8;
                    BER += ber;

                    if(ber < BERmin){
                        optimalA = a;
                        optimalB = B;
                        BERmin = ber;
                    }
                    if(ber > BERmax){
                        worstA = a;
                        worstB = B;
                        BERmax = ber;
                    }
                }
            }
            double BERavg = BER / (double)n;
            System.out.println("Srednie BER dla ASK(" + n + " testow): " + BERavg);
            System.out.println("Najbardziej optymalne wartosci wspolczynnikow(a: " + optimalA +" B:" + optimalB + ", ber = "+ BERmin);
            System.out.println("Najmniej optymalne wartosci wspolczynnikow(a: " + worstA +" B:" + worstB + ", ber = "+ BERmax);
        }

    public static void fsk_testing(String message, double Tb, double fn1, double fn2, int Fp) throws IOException {
        String Hmg74 = Hamming.Coder74(message,true);
        String Hmg1511 = Hamming.Coder1511(message,true);
        double[][] X74 = signals.generate_Zf(Tb,fn1,fn2,Hmg74,Fp,false);
        double[][] X1511 = signals.generate_Zf(Tb,fn1,fn2,Hmg1511,Fp,false);
        double BER = 0.0;
        int n = 0;
        double optimalA = 0.0;
        double worstA = 0.0;
        double optimalB = 0.0;
        double worstB = 0.0;
        double BERmin = 1.0;
        double BERmax = 0.0;
        //generating broken signals
        for(double a = 0.0; a <= 1.0; a += 0.01){
            for(double B = 0.0; B <= 10.0; B += 0.1){
                n++;
                double[][] x1 = signals.multiplyWithWhiteNoise(X74,a);
                double[][] x2 = signals.generateMuffledSignal(X74,B);
                double[][] x3 = signals.multiplyWithWhiteNoise(X74,a);
                x3 = signals.generateMuffledSignal(x3,B);
                double[][] x4 = signals.generateMuffledSignal(X74, B);
                x4 = signals.multiplyWithWhiteNoise(x4,a);

                double[][] x5 = signals.multiplyWithWhiteNoise(X1511,a);
                double[][] x6 = signals.generateMuffledSignal(X1511,B);
                double[][] x7 = signals.multiplyWithWhiteNoise(X1511,a);
                x7 = signals.generateMuffledSignal(x7,B);
                double[][] x8 = signals.generateMuffledSignal(X1511, B);
                x8 = signals.multiplyWithWhiteNoise(x8,a);

                double ber = 0.0;
                String bitsDec="";
                bitsDec = signals.demodulateFSK(x1,fn1,fn2,Tb,Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulateFSK(x2, fn1, fn2, Tb, Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulateFSK(x3, fn1,fn2, Tb, Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulateFSK(x4, fn1, fn2, Tb, Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulateFSK(x5, fn1, fn2, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulateFSK(x6, fn1, fn2, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulateFSK(x7, fn1, fn2, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulateFSK(x8, fn1, fn2, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                ber /= 8;
                BER += ber;

                if(ber < BERmin){
                    optimalA = a;
                    optimalB = B;
                    BERmin = ber;
                }
                if(ber > BERmax){
                    worstA = a;
                    worstB = B;
                    BERmax = ber;
                }
            }
        }
        double BERavg = BER / (double)n;
        System.out.println("Srednie BER dla FSK(" + n + " testow): " + BERavg);
        System.out.println("Najbardziej optymalne wartosci wspolczynnikow(a: " + optimalA +" B:" + optimalB + ", ber = "+ BERmin);
        System.out.println("Najmniej optymalne wartosci wspolczynnikow(a: " + worstA +" B:" + worstB + ", ber = "+ BERmax);
    }
    public static void psk_testing(String message, double Tb, double fn, int Fp) throws IOException {
        String Hmg74 = Hamming.Coder74(message,true);
        String Hmg1511 = Hamming.Coder1511(message,true);
        double[][] X74 = signals.generate_Zp(Tb,fn,Hmg74,Fp,false);
        double[][] X1511 = signals.generate_Zp(Tb,fn,Hmg1511,Fp,false);
        double BER = 0.0;
        int n = 0;
        double optimalA = 0.0;
        double worstA = 0.0;
        double optimalB = 0.0;
        double worstB = 0.0;
        double BERmin = 1.0;
        double BERmax = 0.0;
        //generating broken signals
        for(double a = 0.0; a <= 1.0; a += 0.01){
            for(double B = 0.0; B <= 10.0; B += 0.1){
                n++;
                double[][] x1 = signals.multiplyWithWhiteNoise(X74,a);
                double[][] x2 = signals.generateMuffledSignal(X74,B);
                double[][] x3 = signals.multiplyWithWhiteNoise(X74,a);
                x3 = signals.generateMuffledSignal(x3,B);
                double[][] x4 = signals.generateMuffledSignal(X74, B);
                x4 = signals.multiplyWithWhiteNoise(x4,a);

                double[][] x5 = signals.multiplyWithWhiteNoise(X1511,a);
                double[][] x6 = signals.generateMuffledSignal(X1511,B);
                double[][] x7 = signals.multiplyWithWhiteNoise(X1511,a);
                x7 = signals.generateMuffledSignal(x7,B);
                double[][] x8 = signals.generateMuffledSignal(X1511, B);
                x8 = signals.multiplyWithWhiteNoise(x8,a);

                double ber = 0.0;
                String bitsDec="";
                bitsDec = signals.demodulatePSK(x1, fn, Tb, Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulatePSK(x2, fn, Tb, Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulatePSK(x3, fn, Tb, Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulatePSK(x4, fn, Tb, Fp);
                bitsDec = Hamming.Decoder74(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulatePSK(x5, fn, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulatePSK(x6, fn, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulatePSK(x7, fn, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                bitsDec = signals.demodulatePSK(x8, fn, Tb, Fp);
                bitsDec = Hamming.Decoder1511(bitsDec);
                ber += utils.getBER(bitsDec,utils.bitStream(message));

                ber /= 8;
                BER += ber;

                if(ber < BERmin){
                    optimalA = a;
                    optimalB = B;
                    BERmin = ber;
                }
                if(ber > BERmax){
                    worstA = a;
                    worstB = B;
                    BERmax = ber;
                }
            }
        }
        double BERavg = BER / (double)n;
        System.out.println("Srednie BER dla PSK(" + n + " testow): " + BERavg);
        System.out.println("Najbardziej optymalne wartosci wspolczynnikow(a: " + optimalA +" B:" + optimalB + ", ber = "+ BERmin);
        System.out.println("Najmniej optymalne wartosci wspolczynnikow(a: " + worstA +" B:" + worstB + ", ber = "+ BERmax);
    }
}
