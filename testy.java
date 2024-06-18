import java.io.IOException;
import java.util.Scanner;

public class testy {
    public static void general_test() throws IOException {
        int W = 2;
        double A1 = 0.5;
        double A2 = 1;
        double Tb = 0.1;
        double fn = W / Tb;
        double fn1 = (W + 1)/Tb;
        double fn2 = (W + 2)/Tb;
        int Fp = 100;
        double a = 0.05; //<0;1>
        double B = 5.1; //<0;10>
        double[][] x;
        Scanner in = new Scanner(System.in);
        System.out.println("Wybierz rodzaj modulacji:");
        System.out.println("1. ASK");
        System.out.println("2. PSK");
        System.out.println("3. FSK\n");
        int choiceModulation = in.nextInt();
        System.out.println("Wybierz rodzaj kodera Hamminga:");
        System.out.println("1. Hamming(7,4)");
        System.out.println("2. Hamming(15,11)");
        int choiceCoder = in.nextInt();
        System.out.println("Wybierz rodzaj szumu:");
        System.out.println("0. Brak szumu");
        System.out.println("1. Tylko szum bialy");
        System.out.println("2. Tylko tlumienie");
        System.out.println("3. 1 - szum bialy 2 - tlumienie");
        System.out.println("4. 1 - Tlumienie 2 - szum bialy");
        int choiceBreaks = in.nextInt();
        System.out.println("Podaj ciag znakow, ktory bedzie przeslany: ");
        in.nextLine();
        String napis = in.nextLine();

        String codedArr,decodedArr;

        codedArr = switch(choiceCoder) {
            case 1 -> Hamming.Coder74(napis,false);
            default -> Hamming.Coder1511(napis,false);
        };

        x = switch (choiceModulation) {
            case 1 -> signals.generate_Za(Tb,fn,codedArr,A1,A2,Fp,false);
            case 2 -> signals.generate_Zp(Tb,fn,codedArr,Fp,false);
            default -> signals.generate_Zf(Tb,fn1,fn2,codedArr,Fp,false);
        };

        //generator szumu bialego
        switch(choiceBreaks) {
            case 0:
                break;
            case 1:
                x = signals.multiplyWithWhiteNoise(x,a);
                break;
            case 2:
                x = signals.generateMuffledSignal(x,B);
                break;
            case 3:
                x = signals.multiplyWithWhiteNoise(x,a);
                x = signals.generateMuffledSignal(x,B);
                break;
            default:
                x = signals.generateMuffledSignal(x,B);
                x = signals.multiplyWithWhiteNoise(x,a);
                break;
        }

        String bitsDem = "";
        switch(choiceModulation) {
            case 1:
                bitsDem = signals.demodulateASK(x, fn, Tb, Fp);
                //System.out.println("Bity po demodulacji: \n" + bitsDem);
                break;

            case 2:
                bitsDem = signals.demodulatePSK(x, fn, Tb, Fp);
                //System.out.println("Bity po demodulacji: \n" + bitsDem);
                break;
            case 3:
                bitsDem = signals.demodulateFSK(x,fn1,fn2,Tb,Fp);
                //System.out.println("Bity po demodulacji: \n" + bitsDem);
                break;
            default:
                System.out.println("what???");
        }

        decodedArr = switch(choiceCoder){
            case 1 -> Hamming.Decoder74(bitsDem);
            default -> Hamming.Decoder1511(bitsDem);
        };
        System.out.println("Wartosc wspolczynnika BER: "+utils.getBER(decodedArr,utils.bitStream(napis)));
        System.out.println("Odkodowana wiadomosc: "+utils.bitsToString(decodedArr));

        in.close();
    }

    public static void limitTesting() throws IOException {
        int W = 2;
        double A1 = 0.5;
        double A2 = 1;
        double Tb = 0.1;
        double fn = W / Tb;
        double fn1 = (W + 1)/Tb;
        double fn2 = (W + 2)/Tb;
        int Fp = 100;
        Scanner in = new Scanner(System.in);
        System.out.println("Podaj ciag znakow, ktory bedzie przeslany: ");
        String napis = in.nextLine();
        in.close();
        Hamming.ask_testing(napis,Tb,fn,A1,A2,Fp);
        System.out.println("");
        Hamming.fsk_testing(napis,Tb,fn1,fn2,Fp);
        System.out.println("");
        Hamming.psk_testing(napis,Tb,fn,Fp);
    }
}
