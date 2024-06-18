import org.knowm.xchart.*;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.io.IOException;

public class utils {
    public static String bitStream(String x){
        String ret = "";
        for (char character : x.toCharArray()){
            ret = ret + Integer.toBinaryString((int)character);
        }
        return ret;
    }

    public static String bitsToString(String x){
        String ret = "";
        int m = x.length();
        for(int i = 0; i <= m-7; i+=7){
            String str = x.substring(i,i+7);
            if(str.equals("0000000")){
                break;
            }
            int value = Integer.parseInt(str,2);
            char word = (char)(value);
            ret += word;
        }
        return ret;
    }

    public static int[][] multiplyMatrices(int[][] A, int[][] B){
        int row1 = A.length;
        int col1 = A[0].length;
        int row2 = B.length;
        int col2 = B[0].length;
        //System.out.println("row1 = " + row1 + " col1 = " + col1);
        //System.out.println("row2 = " + row2 + " col2 = " + col2);
        if(row2 != col1){
            System.err.println("Multiplication impossible!\n");
            return null;
        }
        int[][] C = new int[row1][col2];
        for(int i = 0; i < row1; i++){
            for(int j = 0; j < col2; j++)
                C[i][j] = 0;
        }

        for(int i = 0; i < row1; i++){
            for(int j = 0; j < col2; j++){
                for(int k = 0; k < col1; k++){
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
    public static double[] widmo(Complex[] X){
        int N = X.length/2 - 1;
        double[] M = new double[N];
        for(int k = 0; k < N; k++){
            M[k] = Math.sqrt(X[k].re * X[k].re + X[k].im * X[k].im);
        }
        return M;
    }

    public static double[] widmoDB(Complex[] X){
        int N = X.length/2 - 1;
        double[] M = widmo(X);
        for(int k = 0; k < N; k++){
            M[k] = 10.0f * Math.log10(M[k]);
        }
        return M;
    }


    //wyznaczanie skali czestotliwosci!!!

    public static double[] freqScale(double[] x, double fs){
        int N = x.length / 2 - 1;
        double[] f = new double[N];
        for(int k = 0; k < N; k++){
            f[k] = k * fs / x.length;
        }
        return f;
    }

    public static double[] freqScale(Complex[] x, double fs){
        int N = x.length / 2 - 1;
        double[] f = new double[N];
        for(int k = 0; k < N; k++){
            f[k] = k * fs / x.length;
        }
        return f;
    }


    //wyswietlic wykres widma amplitudowego!!!

    public static void showFreqChart(double[] M, double[] f, String name) throws IOException {

        for(int i = 0; i < M.length; i++){
            if(M[i] < 0){
                M[i] = 0;
            }
        }
        //XYChart chart = QuickChart.getChart(name, "M", "f", "widmo amplitudowe", f, M);

        XYChart chart = new XYChartBuilder().width(800).height(600).title(name).xAxisTitle("frequency [Hz]").yAxisTitle("amplitude [dB]").build();

        XYSeries series = chart.addSeries("amplitude spectrum",f,M);

        series.setLineStyle(SeriesLines.NONE);

        for(int i = 0; i < f.length; i++){
            series = chart.addSeries(""+i,new double[]{f[i],f[i]}, new double[]{0,M[i]});
            series.setLineStyle(SeriesLines.DOT_DOT);
            series.setLineColor(Color.BLUE);
            series.setMarker(SeriesMarkers.NONE);
            series.setMarkerColor(Color.BLUE);
        }
        chart.getStyler().setLegendVisible(false);
        // Show it
        new SwingWrapper(chart).displayChart();

        BitmapEncoder.saveBitmap(chart, "./"+name, BitmapEncoder.BitmapFormat.PNG);
    }


    public static void showTimeChart(double[] M, double[] f, String name) throws IOException {

        //XYChart chart = QuickChart.getChart(name, "M", "f", "widmo amplitudowe", f, M);

        XYChart chart = new XYChartBuilder().width(800).height(600).title(name).xAxisTitle("time [s]").yAxisTitle("value").build();

        XYSeries series = chart.addSeries("amplitude spectrum",f,M);

        series.setLineStyle(SeriesLines.NONE);

        for(int i = 0; i < f.length; i++){
            series = chart.addSeries(""+i,new double[]{f[i],f[i]}, new double[]{0,M[i]});
            series.setLineStyle(SeriesLines.DOT_DOT);
            series.setLineColor(Color.BLUE);
            series.setMarker(SeriesMarkers.NONE);
            series.setMarkerColor(Color.BLUE);
        }
        chart.getStyler().setLegendVisible(false);
        // Show it
        new SwingWrapper(chart).displayChart();

        BitmapEncoder.saveBitmap(chart, "./"+name, BitmapEncoder.BitmapFormat.PNG);
    }

    public static void showNormalChart(double[]x,double[]y,String title) throws IOException {
        XYChart chart = QuickChart.getChart(title,"X","Y","y(x)",y,x);
        new SwingWrapper(chart).displayChart();
        BitmapEncoder.saveBitmap(chart, "./"+title, BitmapEncoder.BitmapFormat.PNG);
    }

    public static void printSzerPasm(double fn,double[]freqs,double[]Mdb){
        double referencePiont = 0;
        for(int i = 0; i < Mdb.length; i++){
            if(fn == freqs[i])
                referencePiont = Mdb[i];   //znalezienie poziomu odniesiena
        }

        double[] B3dB = {-1,-1};
        double[] B6dB = {-1,-1};
        double[] B12dB = {-1,-1};
        for(int i = 0; i < Mdb.length; i++){
            if(B12dB[0] == -1 && Mdb[i] >= referencePiont - 12.0 ){
                B12dB[0] = freqs[i];
            }
            if(B6dB[0] == -1 && Mdb[i] >= referencePiont - 6.0 ){
                B6dB[0] = freqs[i];
            }
            if(B3dB[0] == -1 && Mdb[i] >= referencePiont - 3.0 ){
                B3dB[0] = freqs[i];
            }
            if(Mdb[i] >= referencePiont - 12.0){
                B12dB[1] = freqs[i];
            }
            if(Mdb[i] >= referencePiont - 6.0){
                B6dB[1] = freqs[i];
            }
            if(Mdb[i] >= referencePiont - 3.0){
                B3dB[1] = freqs[i];
            }
        }
        System.out.println("Szerokości pasm: ");
        double val = B3dB[1] - B3dB[0];
        System.out.println("B3dB = " + val + " Hz");
        val = B6dB[1] - B6dB[0];
        System.out.println("B6dB = " + val + " Hz");
        val = B12dB[1] - B12dB[0];
        System.out.println("B12dB = " + val + " Hz");
    }
    public static double getBER(String bitOut, String bitIn){
        double BER = 0;
        char[] out = bitOut.toCharArray();
        char[] in = bitIn.toCharArray();
        int N = out.length;
        if(in.length < N){
            N = in.length;
        }
        int E = 0;
        for(int i = 0; i < N; i++){
            if(out[i] != in[i]){
                E++;
            }
        }
        BER = (double)E / (double) N;
        return BER;
    }


}
