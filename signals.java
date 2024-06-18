import java.io.IOException;
import java.util.Random;

import static java.lang.Math.*;

public class signals {


    //sygnały testowe
    static double test(double t, double f){
        return sin(2*Math.PI*f*t);
    }

    static double[][] ftest(double fn, int Fp, int N){
        double[][] ret = new double[2][N];
        for(int n = 0; n < (int) N; n++){
            double t = n/(double)Fp;
            double w = test(t,fn);
            ret[0][n] = w;
            ret[1][n] = t;
        }
        return ret;
    }

    static double m(double t, double fm){
        return  Math.sin(2.0f * Math.PI * fm * t);
    }

    static double Za(double t, double fn, double A1,double A2, char byt){
        if (byt == '0'){
            return A1 * Math.sin(2.0f * Math.PI * fn * t);
        }
        return A2 * Math.sin(2.0f * Math.PI * fn * t);
    }

    static double Zp(double t, double fn, char byt){
        if (byt == '0'){
            return Math.sin(2.0f * Math.PI * fn * t);
        }
        return Math.sin(2.0f * Math.PI * fn * t + Math.PI);
    }

    static double Zf(double t, double fn1, double fn2, char byt){
        if (byt == '0'){
            return Math.sin(2.0f * Math.PI * fn1 * t);
        }
        return Math.sin(2.0f * Math.PI * fn2 * t);
    }



    static double[][] generate_Za(double Tb, double fn, String byteArr, double A1, double A2, int Fp, boolean limit){
        int N = (int)floor(Fp * Tb * byteArr.length());
        if(limit){
            N = (int)floor(Fp * Tb * 10.0f);
        }
        double[][] ret = new double[2][N];
        int indx = -1;
        int chVal = (int)(Tb * Fp);
        char[] byteCarr = byteArr.toCharArray();
        //System.out.println("bity przed demodulacja:\n"+ byteArr);
        for(int i = 0; i < N; i++){
            if(i % chVal == 0){
                indx++;
            }
            double t = (double)i/(double)Fp;
            double w = Za(t,fn,A1,A2,byteCarr[indx]);
            ret[0][i] = w;
            ret[1][i] = t;
        }
        return ret;
    }

    static double[][] generate_Zp(double Tb, double fn, String byteArr, int Fp, boolean limit){
        int N = (int)floor(Fp * Tb * byteArr.length());
        if(limit){
            N = (int)floor(Fp * Tb * 10.0f);
        }
        double[][] ret = new double[2][N];
        int indx = -1;
        int chVal = (int)(Tb * Fp);
        char[] byteCarr = byteArr.toCharArray();
       // System.out.println("bity przed demodulacja:\n"+ byteArr);
        for(int i = 0; i < N; i++){
            if(i % chVal == 0){
                indx++;
            }
            double t = (double)i/(double)Fp;
            double w = Zp(t,fn,byteCarr[indx]);
            ret[0][i] = w;
            ret[1][i] = t;
        }
        return ret;
    }

    static double[][] generate_Zf(double Tb, double fn1, double fn2, String byteArr, int Fp, boolean limit){
        int N = (int)floor(Fp * Tb * byteArr.length());
        if(limit){
            N = (int)floor(Fp * Tb * 10.0f);
        }
        double[][] ret = new double[2][N];
        int indx = -1;
        int chVal = (int)(Tb * Fp);
        char[] byteCarr = byteArr.toCharArray();
       // System.out.println("bity przed demodulacja:\n"+ byteArr);
        for(int i = 0; i < N; i++){
            if(i % chVal == 0){
                indx++;
            }
            double t = (double)i/(double)Fp;
            double w = Zf(t,fn1,fn2,byteCarr[indx]);
            ret[0][i] = w;
            ret[1][i] = t;
        }
        return ret;
        }

        static String demodulateASK(double[][] ASK_signal, double fn, double Tb, int Fp) throws IOException {
            String byteArr = "";
            double[][] demodulatedSignal = ftest(fn, Fp, ASK_signal[0].length);

            int N = ASK_signal[0].length;

            for(int i = 0; i < N; i++) {
                demodulatedSignal[0][i] *= ASK_signal[0][i];
            }
            //utils.showNormalChart(demodulatedSignal[0],demodulatedSignal[1],"x(t)");
            int Nb = (int)ceil(((double)ASK_signal[0].length/(double)Fp/Tb));
            //System.out.println(ASK_signal[0].length+" "+Fp+" "+Tb);
            int byteLength = ASK_signal[0].length/Nb;
            //System.out.println(byteLength);
            //System.out.println(Nb);
            double H = Tb/(double)Fp;
            //zakładamy że Tb jest l całk - jak nie jest to kaplica xdd
            int indx = 1;
            double[] p = new double[Nb];
            double[] pWykres = new double[N];
            double h = 0;
            for(int i = 0; i < Nb; i++){
                double derivative = 0;
                for(int j = indx; j < byteLength + indx - 1; j++){
                    derivative += ((demodulatedSignal[0][j-1] + demodulatedSignal[0][j])/2) * H;
                    pWykres[j] = derivative;
                }
                indx += byteLength;
                p[i] = derivative;
                h += derivative;
                //System.out.println(p[i]);
            }
            //utils.showNormalChart(pWykres,demodulatedSignal[1],"p(t)");
            h /= p.length;
            //System.out.println("h = " + h);
            double[] c = new double[N];
            indx = 0;
            for(int i = 0; i < p.length; i++){
                if(p[i] > h){
                    byteArr += "1";
                    for(int j = indx; j < byteLength + indx; j++){
                        c[j] = 1;
                    }
                    indx += byteLength;
                }
                else{
                    byteArr += "0";
                    for(int j = indx; j < byteLength + indx - 1; j++){
                        c[j] = 0;
                    }
                    indx += byteLength;
                }
            }
            //utils.showNormalChart(c,demodulatedSignal[1],"c(t)");
            return byteArr;
        }

    static String demodulatePSK(double[][] PSK_signal, double fn, double Tb, int Fp) throws IOException {
        String byteArr = "";
        double[][] demodulatedSignal = ftest(fn, Fp, PSK_signal[0].length);

        int N = PSK_signal[0].length;

        for(int i = 0; i < N; i++) {
            demodulatedSignal[0][i] *= PSK_signal[0][i];
        }
        //utils.showNormalChart(demodulatedSignal[0],demodulatedSignal[1],"x(t)");
        int Nb = (int)ceil((double)PSK_signal[0].length/(double)Fp/Tb);
        //System.out.println(ASK_signal[0].length+" "+Fp+" "+Tb);
        int byteLength = PSK_signal[0].length/Nb;
        //System.out.println(byteLength);
        //System.out.println(Nb);
        double H = Tb/(double)Fp;
        //zakładamy że Tb jest l całk - jak nie jest to kaplica xdd
        int indx = 1;
        double[] p = new double[Nb];
        double h = 0;
        double[] pWykres = new double[N];
        for(int i = 0; i < Nb; i++) {
            double derivative = 0;
            for (int j = indx; j < byteLength + indx - 1; j++) {
                derivative += ((demodulatedSignal[0][j - 1] + demodulatedSignal[0][j]) / 2) * H;
                pWykres[j] = derivative;
            }
            indx += byteLength;
            p[i] = derivative;
            //System.out.println(p[i]);
        }
        //System.out.println("h = " + h);

        //utils.showNormalChart(pWykres,demodulatedSignal[1],"p(t)");
        double[] c = new double[N];
        indx = 0;
        for(int i = 0; i < p.length; i++){
            if(p[i] < 0){
                byteArr += "1";
                for(int j = indx; j < byteLength + indx - 1; j++){
                    c[j] = 1;
                }
                indx += byteLength;
            }
            else{
                byteArr += "0";
                for(int j = indx; j < byteLength + indx - 1; j++){
                    c[j] = 0;
                }
                indx += byteLength;
            }
        }
        //utils.showNormalChart(c,demodulatedSignal[1],"c(t)");
        return byteArr;
    }

    static String demodulateFSK(double[][] FSK_signal, double fn1,double fn2, double Tb, int Fp) throws IOException {
        String byteArr = "";
        double[][] demodulatedSignal1 = ftest(fn1, Fp, FSK_signal[0].length);
        double[][] demodulatedSignal2 = ftest(fn2, Fp, FSK_signal[0].length);

        int N = FSK_signal[0].length;

        for(int i = 0; i < N; i++) {
            demodulatedSignal1[0][i] *= FSK_signal[0][i];
            demodulatedSignal2[0][i] *= FSK_signal[0][i];
        }
        //utils.showNormalChart(demodulatedSignal1[0],demodulatedSignal2[1],"x1(t)");
        //utils.showNormalChart(demodulatedSignal2[0],demodulatedSignal2[1],"x2(t)");
        int Nb = (int)ceil((double)FSK_signal[0].length/(double)Fp/Tb);
        //System.out.println(ASK_signal[0].length+" "+Fp+" "+Tb);
        int byteLength = FSK_signal[0].length/Nb;
        //System.out.println(byteLength);
        //System.out.println(Nb);
        double H = Tb/(double)Fp;
        //zakładamy że Tb jest l całk - jak nie jest to kaplica xdd
        int indx = 1;
        double[] p1 = new double[Nb];
        double[] p2 = new double[Nb];
        double h = 0;
        double[] p1Wykres = new double[N];
        double[] p2Wykres = new double[N];
        double[] pWykres = new double[N];
        for(int i = 0; i < Nb; i++) {
            double derivative1 = 0;
            double derivative2 = 0;
            for (int j = indx; j < byteLength + indx - 1; j++) {
                derivative1 += ((demodulatedSignal1[0][j - 1] + demodulatedSignal1[0][j]) / 2) * H;
                derivative2 += ((demodulatedSignal2[0][j - 1] + demodulatedSignal2[0][j]) / 2) * H;
                p1Wykres[j] = derivative1;
                p2Wykres[j] = derivative2;
                pWykres[j] = derivative2 - derivative1;
            }
            indx += byteLength;
            p1[i] = derivative1;
            p2[i] = derivative2;
            //System.out.println(p[i]);
        }
        //utils.showNormalChart(p1Wykres,demodulatedSignal1[1],"p1(t)");
        //utils.showNormalChart(p2Wykres,demodulatedSignal2[1],"p2(t)");
        //utils.showNormalChart(pWykres,demodulatedSignal2[1],"p(t)");
        //System.out.println("h = " + h);
        double[] p = new double[Nb];
        double[] c = new double[N];
        indx = 0;
        for(int i = 0; i < p.length; i++){
            p[i] = p2[i] - p1[i];
            if(p[i] > 0){
                byteArr += "1";
                for(int j = indx; j < byteLength + indx - 1; j++){
                    c[j] = 1;
                }
                indx += byteLength;
            }
            else{
                byteArr += "0";
                for(int j = indx; j < byteLength + indx - 1; j++){
                    c[j] = 0;
                }
                indx += byteLength;
            }
        }
        //utils.showNormalChart(c,demodulatedSignal1[1],"c(t)");
        return byteArr;
    }

    public static double[] generateWhiteNoise(int N) {
        double[] whiteNoise = new double[N];
        Random random = new Random();

        for (int i = 0; i < N; i++) {
            whiteNoise[i] = random.nextDouble() * 2 - 1; // Generowanie wartości w zakresie [-1, 1]
        }

        return whiteNoise;
    }
    public static double[][] multiplyWithWhiteNoise(double[][] signal, double a){
        double[] noise = generateWhiteNoise(signal[0].length);
        for(int i = 0; i < signal[0].length; i++){
            signal[0][i] += a * noise[i];
        }
        return signal;
    }

    public static double[][] generateMuffledSignal(double[][] signal, double B){
        for(int i = 0; i < signal[0].length; i++){
            signal[0][i] *= Math.exp(-B * signal[1][i]);
        }
        return signal;
    }



    }

