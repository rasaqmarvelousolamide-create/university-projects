public class Distance
{
    private double kilometres;

    public Distance(double kilometres)
    {
        this.kilometres = kilometres;
    }

    public StepStats convert()
    {
        int totalSteps;

        totalSteps = (int)Math.round(kilometres * 1312);

        int thousands;
        int hundreds;
        int tens;

        thousands = totalSteps / 1000;

        hundreds = (totalSteps % 1000) / 100;

        tens = (totalSteps % 100) / 10;

        StepStats stats;

        stats = new StepStats(thousands, hundreds, tens);

        return stats;
    }
}
    

