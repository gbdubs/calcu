import java.util.List;

public class Statistics 
{
    List<Integer> data;
    double size;    

    public Statistics(List<Integer> data) 
    {
        this.data = data;
        size = data.size();
    }   

    double getMean()
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    double getVariance()
    {
        double mean = getMean();
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }

    double getStdDev()
    {
        return Math.sqrt(getVariance());
    }
}